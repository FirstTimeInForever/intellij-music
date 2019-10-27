from pydub import AudioSegment, playback, effects
from io import BytesIO


def play_sound(sound, volume=0, speed=None):
    sound += volume
    if speed is not None:
        if speed > 1:
            sound = effects.speedup(sound, playback_speed=speed)
        else:
            sound = speed_change(sound, speed=speed)

    playback._play_with_simpleaudio(sound)


def speed_change(sound, speed=1.0):
    sound_with_altered_frame_rate = sound._spawn(sound.raw_data, overrides={
        "frame_rate": int(sound.frame_rate * speed)
    })
    return sound_with_altered_frame_rate.set_frame_rate(sound.frame_rate)


class AudioPlayer:
    def play_sound(self, raw_sound):
        sound = AudioSegment.from_mp3(BytesIO(raw_sound))
        play_sound(sound, speed=1.5)
