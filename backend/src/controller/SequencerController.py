from .BaseController import BaseController
from src.player.AudioPlayer import AudioPlayer
from src.player.AudioStorage import AudioStorage
from src.controller.NoteSequencer import CircleOfFifthsSequencer


class SequencerController(BaseController):
    def __init__(self, path):
        super().__init__()
        self.sequencer = CircleOfFifthsSequencer()
        self.audio_storage = AudioStorage(path)
        self.audio_player = AudioPlayer()

    def keyboard_pressed(self, key_info: dict):
        note = self.sequencer.next_note()
        print(note)
        try:
            sound = self.audio_storage.get_note(note)
            self.audio_player.play_sound(sound)
        except:
            print(f'{note} was a failure')
