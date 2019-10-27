from src.player.AudioPlayer import AudioPlayer
from src.player.AudioStorage import AudioStorage


class AppController:
    def __init__(self):
        self.audio_storage = AudioStorage('../../audio/acoustic_grand_piano-mp3')
        self.audio_player = AudioPlayer()

    def play_some(self):
        sound = self.audio_storage.samples[('G', 3)]
        self.audio_player.play_sound(sound)


def test_stuff():
    controller = AppController()
    while input():
        controller.play_some()


if __name__ == '__main__':
    test_stuff()
