import cv2
import numpy as np
import imutils
from collections import deque
import time

HEADLESS = True
FANCY = False

if FANCY:
    HEADLESS = True

BLUE = False
RED = not BLUE
if BLUE:
    icol = (89, 70, 160, 125, 240, 250)
else:
    icol = (0, 90, 150, 12, 255, 255)

if not HEADLESS:
    cv2.namedWindow("bigman")

FRAME_WIDTH = 700
FRAME_HEIGHT = 700
SLEEP_TIME = 1  # ms

NUM_CAMERAS = 1
cams = [cv2.VideoCapture(i) for i in range(NUM_CAMERAS)]
for cam in cams:
    cam.set(cv2.CAP_PROP_FRAME_WIDTH, FRAME_WIDTH)
    cam.set(cv2.CAP_PROP_FRAME_HEIGHT, FRAME_HEIGHT)
pts = [deque(maxlen=32) for cam in cams]

def find_ball(icol, frame, pts):
    frame = cv2.GaussianBlur(frame, (11, 11), 0)
    lowHue, lowSat, lowVal, highHue, highSat, highVal = icol
    frameHSV = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    mask = cv2.inRange(frameHSV, (lowHue, lowSat, lowVal), (highHue, highSat, highVal))
    mask = cv2.erode(mask, None, iterations=2)
    mask = cv2.dilate(mask, None, iterations=2)
    cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    cnts = imutils.grab_contours(cnts)
    center = None
    radius = 0
    x = 0
    y = 0
    if len(cnts) > 0:
        c = max(cnts, key=cv2.contourArea)
        (x, y), radius = cv2.minEnclosingCircle(c)
        M = cv2.moments(c)
        center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))
        if radius > 10:
            cv2.circle(frame, (int(x), int(y)), int(radius), (0, 255, 255), 2)
            cv2.circle(frame, center, 5, (0, 0, 255), -1)
        radius = radius
        x, y = center
    if FANCY:
        pts.appendleft(center)
        for i in range(1, len(pts)):
            if pts[i - 1] is None or pts[i] is None:
                continue
            thickness = int(np.sqrt(64 / float(i + 1)) * 2.5)
            cv2.line(frame, pts[i - 1], pts[i], (0, 0, 255), thickness)
    return frame, (x, y), radius

def concat_images(width, height, *imgs):
    return np.concatenate([cv2.resize(img, (width, height)) for img in imgs], axis=1)

img_index = 0
while True:
    imgs = []
    coords = []
    radii = []
    for i, cam in enumerate(cams):
        _, frame = cam.read()
        img, (x, y), r = find_ball(icol, frame, pts[i])
        imgs.append(img)
        coords.append((x, y))
        radii.append(r)
    if np.argmax(radii) != 0 and coords[np.argmax(radii)] != (0, 0):
        print(f"cam {np.argmax(radii)} - {coords[np.argmax(radii)]}")
    if not HEADLESS:
        cv2.imshow(f"bigman", imgs[img_index])
        k = cv2.waitKey(5) & 0xFF

        if FANCY:
            if k == 27:
                break
            elif 48 <= k < 48+NUM_CAMERAS:
                img_index = k-48
        time.sleep(SLEEP_TIME / 1000)
    print(list(zip(coords, radii)))

if not HEADLESS:
    cv2.destroyAllWindows()
for cam in cams:
    cam.release()