import cv2
from flask import Flask, render_template, Response

camera = cv2.VideoCapture(2)

app = Flask(__name__)

def gen():
    while True:
        _, frame = cv2.imencode('.JPEG', camera.read()[1])
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame.tostring() + b'\r\n')

@app.route('/')
def video_feed():
    return Response(gen(),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8083)