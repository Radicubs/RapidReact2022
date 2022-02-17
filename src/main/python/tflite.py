from tflite_runtime import interpreter as tflite
from PIL import Image
import numpy as np

model = tflite.Interpreter(model_path="unoptimized.tflite")
model.allocate_tensors()

input_details = model.get_input_details()

for a in range(0, 250):
    print(a)
    im = Image.open("img_2.jpg")

    left = (1280 - 720) / 2
    right = 1280 - left

    res_im = im.crop((left, 0, right, 720))

    res_im = res_im.resize((300, 300))

    np_res_im = np.array(res_im)

    np_res_im = (np_res_im).astype('uint8')

    print(input_details[0]['shape'])

    if len(np_res_im.shape) == 3:
        np_res_im = np.expand_dims(np_res_im, 0)
    # Test the model on random input data.
    input_shape = input_details[0]['shape']
    input_data = np_res_im
    model.set_tensor(input_details[0]['index'], input_data)

    model.invoke()

    # The function `get_tensor()` returns a copy of the tensor data.
    # Use `tensor()` in order to get a pointer to the tensor.
    output_details = model.get_output_details()
    output_data = model.get_tensor(output_details[0]['index'])

    out = [x * 300 for x in output_data[0][0]]
    x, y, xx, yy = out
    print(x)
    print(y)
    print(xx)
    print(yy)