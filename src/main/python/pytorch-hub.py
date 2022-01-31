from turtle import color
import torch
import sys
from torchsummary import summary
import cv2
import numpy as np
import imutils
import time
sys.path.insert(0, './yolov5')

# device = 'cuda' if torch.cuda.is_available() else 'cpu'

model = torch.hub.load('ultralytics/yolov5', 'custom', path="models/pytorch.pt")
model.conf = 0.05

original_img = cv2.imread('test_img/3.jpg')
img = original_img[..., ::-1] # OpenCV image (BGR to RGB)

results = model(img, size=640)

results_df = results.pandas().xyxy[0]
print(results_df)

#for detection in results_df:
#    print(detection)
#    start_point = (int(detection['xmin']), int(detection['ymin']))
#    end_point = (int(detection['xmax']), int(detection['ymax']))
#    color = (255, 0, 0) 
#    thickness = 2
#    original_img = cv2.rectangle(original_img, start_point, end_point, color, thickness)

points = [((int(x1), int(y1)), (int(x2), int(y2))) for x1, y1, x2, y2 in zip(results_df["xmin"], results_df["ymin"], results_df["xmax"], results_df["ymax"])]

color = (255, 0, 0) 
thickness = 10
for p in points:
    original_img = cv2.rectangle(original_img, p[0], p[1], color, thickness)

cv2.imshow("test", original_img)
cv2.waitKey()
time.sleep(10)
cv2.destroyAllWindows()