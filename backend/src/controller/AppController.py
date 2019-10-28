from src.player.AudioPlayer import AudioPlayer
from src.player.AudioStorage import AudioStorage
from src.controller.NoteSequencer import NoteSequencer
from src.controller.MarkovGenerator import Parser
import sys
import random


def parse(filename):
    return Parser(filename, verbose=False).get_chain()


class AppController:
    def __init__(self, path):
        self.audio_storage = AudioStorage(path)
        self.audio_player = AudioPlayer()
        self.note_sequencer = NoteSequencer()

    def play_random(self):
        sound = self.audio_storage.get_random_audio()
        self.audio_player.play_sound(sound)

    def play_note(self, note):
        octave = random.choice([2, 3, 4])
        sound = self.audio_storage.get_note((note, octave))
        self.audio_player.play_sound(sound)

    def play_sequenced(self):
        self.play_note(self.note_sequencer.next_note())


def test_stuff():
    controller = AppController(path='../../audio/acoustic_grand_piano-mp3')
    while True:
        _ = sys.stdin.read(1)
        # controller.play_random()
        controller.play_sequenced()
