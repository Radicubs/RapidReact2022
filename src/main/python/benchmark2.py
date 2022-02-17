import torch
import pyperf
import numpy as np
import sys
sys.path.insert(0, './yolov5')

from models.common import DetectMultiBackend
from utils.general import (check_img_size, check_imshow)
from utils.torch_utils import select_device
weights = "./models/pytorch_3.pt"
model = DetectMultiBackend(weights, device="cpu")

half = False
def func2():
    batch = np.random.rand(3, 3, 360, 640)
    im = torch.from_numpy(batch).to("cpu")
    im = im.half() if half else im.float()  # uint8 to fp16/32
    #im /= 255  # 0 - 255 to 0.0 - 1.0
    if len(im.shape) == 3:
        im = im[None]  # expand for batch dim
            # honestly might be a more efficient torch operation to do this
            # this will operate in gpu memory
    model(batch)
    
        
runner = pyperf.Runner()
runner.bench_func('func', func2)