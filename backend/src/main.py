from flask import Flask, request
from src.controller.AppController import AppController

app = Flask(__name__)
controller = AppController(path='../audio/acoustic_grand_piano-mp3')


@app.route('/start', methods=['POST'])
def start_listen():
    print('Start listen')
    return '', 200


@app.route('/stop', methods=['POST'])
def stop_listen():
    print('Stop listen')
    return '', 200


@app.route('/keyboard-event', methods=['POST'])
def keyboard_event():
    content = request.json
    print(content)
    # controller.play_random()
    controller.play_sequenced()
    return '', 200
