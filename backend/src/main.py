from flask import Flask, request
from src.controller.RandomController import RandomController
from src.controller.SequencerController import SequencerController

app = Flask(__name__)
# controller = RandomController(path='../audio/acoustic_grand_piano-mp3')
controller = SequencerController(path='../audio/acoustic_grand_piano-mp3')


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
    '''
    json:
    {
        'code': number
        'layout': string
    }
    :return:
    '''
    content = request.get_json()
    controller.keyboard_pressed(content)
    return '', 200
