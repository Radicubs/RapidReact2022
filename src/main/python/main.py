import cv2
import numpy as np
import imutils
from collections import deque
import time

pts1 = deque(maxlen=32) # 32 for now
pts2 = deque(maxlen=32) # 32 for now
pts3 = deque(maxlen=32) # 32 for now
# pts4 = deque(maxlen=32)

BLUE = True
RED = not BLUE

FRAME_WIDTH = 700
FRAME_HEIGHT = 700
SLEEP_TIME = 1 # ms

if BLUE:
    icol = (89, 80, 180, 125, 240, 255)
else:
    icol = (0, 120, 180, 10, 240, 255)

def find_ball(icol, frame, pts):
    lowHue, lowSat, lowVal, highHue, highSat, highVal = icol
    frameHSV = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    mask = cv2.inRange(frameHSV, (lowHue, lowSat, lowVal), (highHue, highSat, highVal))
    mask = cv2.erode(mask, None, iterations=2)
    mask = cv2.dilate(mask, None, iterations=2)
    cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    cnts = imutils.grab_contours(cnts)
    center = None
    if len(cnts) > 0:
        c = max(cnts, key=cv2.contourArea)
        (x, y), radius = cv2.minEnclosingCircle(c)
        M = cv2.moments(c)
        center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))
        if radius > 10:
            cv2.circle(frame, (int(x), int(y)), int(radius), (0, 255, 255), 2)
            cv2.circle(frame, center, 5, (0, 0, 255), -1)
        print(radius, center[0], center[1])
    pts.appendleft(center)
    for i in range(1, len(pts)):
        if pts[i - 1] is None or pts[i] is None:
            continue
        thickness = int(np.sqrt(64 / float(i + 1)) * 2.5)
        cv2.line(frame, pts[i - 1], pts[i], (0, 0, 255), thickness)
    return frame

def concat_images(width, height, img1, img2, img3):#, img4):
    img1 = cv2.resize(img1, (width, height))
    img2 = cv2.resize(img2, (width, height))
    img3 = cv2.resize(img3, (width, height))
    # img4 = cv2.resize(img4, (width, height))
    return np.concatenate((img1, img2, img3), axis=1)#, img4), axis=0)

cam = cv2.VideoCapture(0)
cam2 = cv2.VideoCapture(1)
cam3 = cv2.VideoCapture(2)
# cam4 = cv2.VideoCapture(3)
cv2.namedWindow("bigman")
cam.set(cv2.CAP_PROP_FRAME_WIDTH, FRAME_WIDTH)
cam.set(cv2.CAP_PROP_FRAME_HEIGHT, FRAME_HEIGHT)
cam2.set(cv2.CAP_PROP_FRAME_WIDTH, FRAME_WIDTH)
cam2.set(cv2.CAP_PROP_FRAME_HEIGHT, FRAME_HEIGHT)
cam3.set(cv2.CAP_PROP_FRAME_WIDTH, FRAME_WIDTH)
cam3.set(cv2.CAP_PROP_FRAME_HEIGHT, FRAME_HEIGHT)
# cam4.set(cv2.CAP_PROP_FRAME_WIDTH, FRAME_WIDTH)
# cam4.set(cv2.CAP_PROP_FRAME_HEIGHT, FRAME_HEIGHT)

while True:
    _, frame1 = cam.read()
    _, frame2 = cam2.read()
    _, frame3 = cam3.read()
    # _, frame4 = cam4.read()
    frame1 = cv2.GaussianBlur(frame1, (11, 11), 0)
    frame2 = cv2.GaussianBlur(frame2, (11, 11), 0)
    frame3 = cv2.GaussianBlur(frame3, (11, 11), 0)
    # frame4 = cv2.GaussianBlur(frame4, (11, 11), 0)
    img = find_ball(icol, frame1, pts1)
    img2 = find_ball(icol, frame2, pts2)
    img3 = find_ball(icol, frame3, pts3)
    # img4 = find_ball(icol, frame4, pts4)
    cv2.imshow(f"bigman", concat_images(500, 500, img, img2, img3))#, img4))
    k = cv2.waitKey(5) & 0xFF
    if k == 27:
        break
    time.sleep(SLEEP_TIME/1000)

cv2.destroyAllWindows()
cam.release()
cam2.release()
cam3.release()





