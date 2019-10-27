import os
import re


class AudioStorage:
    def __init__(self, samples_dir):
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

    @staticmethod
    def _load_file(filename):
        with open(filename, mode='rb') as file:
            return file.read()

    @staticmethod
    def _parse_filename(filename):
        matches = re.match(r'([a-zA-Z]+)([0-9]+)', filename)
        return matches.group(1), int(matches.group(2))


def __test_stuff():
    storage = AudioStorage('../audio/acoustic_grand_piano-mp3')
    pass

# test_stuff()
