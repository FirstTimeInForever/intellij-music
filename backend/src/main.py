from flask import Flask, request
from src.controller import AppController

app = Flask(__name__)
controller = AppController()


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
    print(f'{content["jope"]}')
    return '', 200
