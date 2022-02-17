import torch
import sys
from torchsummary import summary
import cv2
import numpy as np
import imutils
sys.path.insert(0, './yolov5')

# todo: look into poloyv5/detect.py for fp16 and cudnn optimizations
# lots of stuff in there that we need to wade through

device = 'cuda' if torch.cuda.is_available() else 'cpu'

model = torch.load("models/pytorch.pt", map_location=device)['model']

img = cv2.imread("test_img.jpg")

# would this following operation benefit from CUDA?
img = imutils.resize(img, width=640)
img = img.transpose((2, 0, 1))[::-1]  # HWC to CHW, BGR to RGB
img = np.ascontiguousarray(img)

im = torch.from_numpy(img).to(device)
im.float()
im = im / 255
print(im)
if len(im.shape) == 3:
    im = im[None]  # expand for batch dim

# .float() needed to cast torch.HalfTensor to torch.FloatTensor
# which is bc of our GPU trained model incompt with CPU inference
model.float().eval()
print(model(im))