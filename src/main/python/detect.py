# export OPENBLAS_CORETYPE=ARMV8

import enum
import os
from re import X
from sre_constants import SUCCESS
import sys
import cv2
import math
import networktables
import torch
import torch.backends.cudnn as cudnn
import numpy as np
import time
from PIL import Image
from threading import Thread
from networktables import NetworkTables
import socket
from flask import Flask, render_template, Response

#os.chdir("/home/radicubs/RapidReact2022/src/main/python")
sys.path.insert(0, './yolov5')

from models.common import DetectMultiBackend
from utils.general import (check_img_size, check_imshow, non_max_suppression)
from utils.torch_utils import select_device

app = Flask("frc vision")

class LoadWebcams:
    def __init__(self, sources, img_size=(640, 360), stride=32):
        self.img_size = img_size
        self.stride = stride
        self.cams = sources
        n = len(self.cams)

        # ALL CAMERAS MUST BE SAME W and H
        # w = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        # h = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        w, h = img_size

        self.imgs = np.zeros((n, 3, h, w)) # batch size, channels, height, width
        self.scaled_ims = [np.zeros((3, h, w))] * n
        self.encoded_frames = [np.zeros((3, h, w))] * n
        self.streams = [socket.socket(socket.AF_INET, socket.SOCK_DGRAM)] * n
        self.fps, self.frames, self.threads = [0] * n, [0] * n, [None] * n

        for i, s in enumerate(self.cams): # index, source
            cap = cv2.VideoCapture(s)
            assert cap.isOpened(), f'{st}Failed to open {s}'

            fps = cap.get(cv2.CAP_PROP_FPS)
            self.frames[i] = max(int(cap.get(cv2.CAP_PROP_FRAME_COUNT)), 0) or float('inf')
            self.fps[i] = max((fps if math.isfinite(fps) else 0) % 100, 0) or 30  # 30 FPS fallback
            print(f'Camera {i} running at {self.fps[i]} fps')

            im = cap.read()[1]

            # We need to crop to the aspect ratio first and then scale down
            # Step 1: find the margin we need to cut
            h_scaled = int((im.shape[1] / w) * h)
            top_margin = int((im.shape[0] - h_scaled) / 2)
            scaled_im = cv2.resize(im[top_margin:(top_margin+h_scaled), :], (w, h))
            print("Cropped and scaled to " + str(scaled_im.shape))
            if not headless:
                self.scaled_ims[i] = scaled_im.copy()
            self.imgs[i] = scaled_im[..., ::-1].transpose(2, 0, 1) # explanation is like 10 lines below

            self.threads[i] = Thread(target=self.capture, args=([i, cap, s]), daemon=True)
            print(f"Success ({self.frames[i]} frames {w}x{h} at {self.fps[i]:.2f} FPS)")
            self.threads[i].start()

    def capture(self, i, cap, stream):
        captured_count = 0
        start_time = time.time()
        while cap.isOpened(): # this runs 60 times a second even without the time.sleep
            #for _ in range(40):
            # cap.grab()
            # im = cv2.imread('/Users/raptor/cs/RapidReact2022/src/main/python/test_img/3.jpg')
            # success = True
            success, im = cap.read() # reads in height, width, channels AND in BGR
            Thread(target=self.process_frame, args=([success, im, i]), daemon=True).start()
            Thread(target=self.imencode_stream, args=([success, im, i]), daemon=True).start()
            captured_count += 1
            if captured_count % 60 == 0:
                print("Camera seconds per frame: " + str(1.0 / (time.time() - start_time)))
                start_time = time.time()


    def process_frame(self, success, im, i):
        if success:
            w, h = self.img_size
            # We need to crop to the aspect ratio first and then scale down
            # Step 1: find the margin we need to cut
            h_scaled = int((im.shape[1] / w) * h)
            top_margin = int((im.shape[0] - h_scaled) / 2)
            scaled_im = cv2.resize(im[top_margin:(top_margin+h_scaled), :], (w, h))
            if not headless:
                self.scaled_ims[i] = scaled_im.copy()
            self.imgs[i] = scaled_im[..., ::-1].transpose(2, 0, 1) # explanation is like 10 lines below
        else:
            print("WARNING: video stream yikes yikes unresponsive")
            self.imgs[i] = np.zeros_like(self.imgs[i])
        # time.sleep(1 / (self.fps(i)+10)) # NOT NEEDED

    def imencode_stream(self, success, im, i):
        if success:
            _, frame = cv2.imencode('.JPEG', im)
            self.encoded_frames[i] = frame
    
    def get_encoded_frame(self, idx):
        while True:
            yield(b'--frame\r\n'
            b'Content-Type: image/jpeg\r\n\r\n' + self.encoded_frames[idx].tostring() + b'\r\n')
            Thread.sleep(0.025)


    def __iter__(self):
        return self

    def __next__(self):
        #assert all(x.is_alive() for x in self.threads)
        return self.imgs, self.scaled_ims.copy()


@app.route('/')    
def video_feed():
    return Response(dataset.get_encoded_frame(0),
                    mimetype='multipart/x-mixed-replace; boundary=frame')

cams = [2]
# device = "cpu"
device = "cpu" # gpu
headless = False

#weights = "./models/pytorch_3_b1_fp16.onnx"
weights="./models/pytorch_3.pt"
dnn=False  # use OpenCV DNN for ONNX inference
data = "models/data.yaml"
imgsz=(640, 320)  # inference size (width, height)
h,w = imgsz
conf_thres = 0.25
iou_thres = 0.45
classes = None
agnostic_nms = False
max_det = 10
bs = len(cams) # batch size

usenetworktables = True
if usenetworktables:
    ip = "roborio-7503-FRC.local"
    NetworkTables.initialize(server=ip)
    datatable = NetworkTables.getTable("data")

if device == "cpu":
    os.environ['CUDA_VISIBLE_DEVICES'] = '-1'  # force torch.cuda.is_available() = False
else:  # non-cpu device requested
    os.environ['CUDA_VISIBLE_DEVICES'] = device  # set environment variable - must be before assert is_available()

with torch.no_grad():
    device = select_device(device)
    half = True
    model = DetectMultiBackend(weights, device=device, dnn=dnn, data=data, fp16=half)
    model.eval()
    # set model to eval mode
    stride, names, pt = model.stride, model.names, model.pt
    imgsz = check_img_size(imgsz, s=stride)  # check image size

    if not headless:
        view_img = check_imshow()
    else:
        view_img = False

    cudnn.benchmark = True
    model.warmup(imgsz=(1 if pt else bs, 3, *imgsz))  # warmup
    dataset = LoadWebcams(cams, imgsz)
    
    Thread(target=app.run, args=(['0.0.0.0', 8083]), daemon=True).start()
    
    gn = np.array(dataset.scaled_ims[0].shape)[[1, 0, 1, 0]]
    for batch, original_imgs in dataset:

        start_time = time.time()
        im = torch.from_numpy(batch)
        im = im.half() if model.fp16 else im.float()  # uint8 to fp16/32
        im /= 255  # 0 - 255 to 0.0 - 1.0
        if len(im.shape) == 3:
            im = im[None]
        im = im.to(device)
        preds = model(im)
        preds = non_max_suppression(preds, conf_thres, iou_thres, classes, agnostic_nms, max_det=max_det)

        detected_str = "nothing"

        for i, img_pred in enumerate(preds):
            for *xyxy, conf, cls in img_pred:
                det = (np.array(xyxy) / gn) * np.array([h,w,h,w])
                x1 = round(det[0].item())
                y1 = round(det[1].item())
                x2 = round(det[2].item())
                y2 = round(det[3].item())

                if (detected_str == "nothing"):
                    detected_str = ""
                detected_str += " ".join([str(x) for x in [((x1 + x2) / 2), ((y1 + y2) / 2), (0.5 * math.sqrt((x2 - x1)^2 + (y2 - y1)^2))]])
                detected_str += "  " # separates out ball elements

                if view_img:
                    original_imgs[i] = cv2.rectangle(original_imgs[i], (x1, y1), (x2, y2), (0, 255, 0), 2)
            if view_img:
                cv2.imshow(str(i), original_imgs[i])
                cv2.waitKey(1)

        if usenetworktables:
            datatable.putString(str(i), detected_str)

            print(detected_str)

        print("FPS inference: " + str(1.0 / (time.time() - start_time)))
