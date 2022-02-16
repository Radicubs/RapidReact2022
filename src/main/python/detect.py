import enum
import os
from sre_constants import SUCCESS
import sys
import cv2
import math
import torch
import torch.backends.cudnn as cudnn
import numpy as np
import time
from PIL import Image
from threading import Thread
 
sys.path.insert(0, './yolov5')
 
from models.common import DetectMultiBackend
from utils.general import (check_img_size, check_imshow, non_max_suppression)
from utils.torch_utils import select_device
 
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
            captured_count += 1
            if captured_count % 60 == 0:
                print("FPS: " + str(1.0 / (time.time() - start_time)))
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
        
            
    def __iter__(self):
        return self
 
    def __next__(self):
        #assert all(x.is_alive() for x in self.threads)
        return self.imgs, self.scaled_ims.copy()
 
 
cams = [0]
# device = "cpu"
device = "0" # gpu
headless = False
 
weights = "./models/pytorch_3.pt"
dnn=False  # use OpenCV DNN for ONNX inference
data = "models/data.yaml"
imgsz=(640, 352)  # inference size (width, height)
h,w = imgsz
conf_thres = 0.25
iou_thres = 0.45
classes = None
agnostic_nms = False
max_det = 10
 
if device == "cpu":
    os.environ['CUDA_VISIBLE_DEVICES'] = '-1'  # force torch.cuda.is_available() = False
else:  # non-cpu device requested
    os.environ['CUDA_VISIBLE_DEVICES'] = device  # set environment variable - must be before assert is_available()
 
with torch.no_grad():
    device = select_device(device)
    model = DetectMultiBackend(weights, device=device, dnn=dnn, data=data)
    model.eval()
    # set model to eval mode
    stride, names, pt, jit, onnx, engine = model.stride, model.names, model.pt, model.jit, model.onnx, model.engine
    imgsz = check_img_size(imgsz, s=stride)  # check image size
    half = True
 
    # Half precision
    # essentially we make sure that we are not running on cpu and are on of the supported model types
    half &= (pt or jit or onnx or engine) and device.type != 'cpu'  # FP16 supported on limited backends with CUDA
    if pt or jit:
        if half: 
            model.model.half()
            print("running half precision") 
        else:
            model.model.float()
            print("running full precision")
 
    if not headless:
        view_img = check_imshow()
 
    cudnn.benchmark = True
    dataset = LoadWebcams(cams, imgsz)
 
    gn = np.array(dataset.scaled_ims[0].shape)[[1, 0, 1, 0]]
    for batch, original_imgs in dataset:
        start_time = time.time()
        im = torch.from_numpy(batch)
        im = im.half() if half else im.float()  # uint8 to fp16/32
        im /= 255  # 0 - 255 to 0.0 - 1.0
        if len(im.shape) == 3:
            im = im[None]  # expand for batch dim
            # honestly might be a more efficient torch operation to do this
            # this will operate in gpu memory
        im.to(device)
        preds = model(im)
        preds = non_max_suppression(preds, conf_thres, iou_thres, classes, agnostic_nms, max_det=max_det)
 
        if not headless: # we want to visualize
            for i, img_pred in enumerate(preds):
                for *xyxy, conf, cls in img_pred:
                    det = (np.array(xyxy) / gn) * np.array([h,w,h,w])
                    original_imgs[i] = cv2.rectangle(original_imgs[i], (round(det[0].item()), round(det[1].item())), (round(det[2].item()), round(det[3].item())), (0, 255, 0), 2)
            cv2.imshow("a", original_imgs[i])
            cv2.waitKey(1)
        print("FPS inference: " + str(1.0 / (time.time() - start_time)))