from src.player.AudioPlayer import AudioPlayer
from src.player.AudioStorage import AudioStorage

class AppController:
    def __init__(self, path):
        self.audio_storage = AudioStorage(path)
        self.audio_player = AudioPlayer()

    def play_random(self):
        sound = self.audio_storage.get_random_audio()

        self.audio_player.play_sound(sound)


def test_stuff():
    controller = AppController()
    while input():
        controller.play_random()


if __name__ == '__main__':
    test_stuff()
