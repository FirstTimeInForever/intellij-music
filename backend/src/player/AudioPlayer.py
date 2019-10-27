from pydub import AudioSegment, playback
import pydub
from io import BytesIO


class AudioPlayer:
    def play_sound(self, raw_sound):
        sound = AudioSegment.from_mp3(BytesIO(raw_sound))
        playback._play_with_simpleaudio(sound)
