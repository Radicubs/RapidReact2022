import numpy as np
from setuptools import setup
import pyperf
from PIL import Image
import cv2

runner = pyperf.Runner()

runner.timeit(name="Pillow",
              stmt="im = Image.fromarray(im, 'RGB'); im.thumbnail((640, 360))",
              setup='from PIL import Image; import cv2; im = cv2.imread("/Users/raptor/Downloads/ASalazar_art_11-10.6902-1230.jpg", cv2.IMREAD_UNCHANGED)')

runner.timeit(name="cv2",
              stmt="cv2.resize(im, (w, (w/im.shape[1])*im.shape[0]))",
              setup='import cv2; im = cv2.imread("/Users/raptor/Downloads/ASalazar_art_11-10.6902-1230.jpg", cv2.IMREAD_UNCHANGED)')

runner.timeit(name="imutils",
              stmt="imutils.resize(im, width=640)",
              setup='import cv2; import imutils; im = cv2.imread("/Users/raptor/Downloads/ASalazar_art_11-10.6902-1230.jpg", cv2.IMREAD_UNCHANGED)')
