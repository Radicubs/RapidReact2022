import numpy as np
import onnxruntime as ort
import cv2

original_img = cv2.imread('test_img/3.jpg')
img = original_img[..., ::-1] # OpenCV image (BGR to RGB)

print(img)

img = np.expand_dims(img, axis=0)

sess = ort.InferenceSession("./models/tflitemodel.onnx")

input = [ins.name for ins in sess.get_inputs()]
outputs = [out.name for out in sess.get_outputs()]
print(input)
print(outputs)

result = sess.run(outputs, {input[0]: img})
print(result)
num_detections, detection_boxes, detection_scores, detection_classes = result
