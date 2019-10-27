from .BaseController import BaseController

from src.player.AudioPlayer import AudioPlayer
from src.player.AudioStorage import AudioStorage


class RandomController(BaseController):
    def __init__(self, path: str):
        super().__init__()
        self.audio_storage = AudioStorage(path)
        self.audio_player = AudioPlayer()

    def keyboard_pressed(self, key_info: dict):
        super().keyboard_pressed(key_info)

        self.__play_random()

    def __play_random(self):
        sound = self.audio_storage.get_random_audio()

        self.audio_player.play_sound(sound)
