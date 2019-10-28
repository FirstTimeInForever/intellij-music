import os
import re
from random import choice


class AudioStorage:
    def __init__(self, samples_dir: str):
        self.samples = {}
        for root, _, files in os.walk(samples_dir):
            for filename in files:
                if filename.endswith('.mp3'):
                    basename = os.path.splitext(filename)[0]
                    target_path = os.path.join(root, filename)
                    key = self._parse_filename(basename)
                    self.samples[key] = self._load_file(target_path)
                else:
                    print(f'Ignoring non .mp3 file: {filename}')

    def get_random_audio(self):
        random_key = choice(list(self.samples.keys()))
        return self.samples[random_key]

    def get_note(self, note):
        return self.samples[note]

    @staticmethod
    def _load_file(filename: str):
        with open(filename, mode='rb') as file:
            return file.read()

    @staticmethod
    def _parse_filename(filename: str):
        matches = re.match(r'([a-zA-Z]+)([0-9]+)', filename)
        return matches.group(1), int(matches.group(2))