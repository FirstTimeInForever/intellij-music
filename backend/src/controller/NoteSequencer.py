import random
import math

notes = {
    'C': ['C', 'D', 'E', 'F', 'G', 'A', 'B', 'C'],
    'D': ['D', 'E', 'Gb', 'G', 'A', 'B', 'Db', 'D'],
    'E': ['E', 'Gb', 'Ab', 'A', 'B', 'Db', 'Eb', 'E'],
    'F': ['F', 'G', 'A', 'Bb', 'C', 'D', 'E', 'F'],
    'G': ['G', 'A', 'B', 'C', 'D', 'E', 'Gb', 'G'],
    'A': ['A', 'B', 'Db', 'D', 'E', 'Gb', 'Ab', 'A'],
    'B': ['B', 'Db', 'Eb', 'E', 'Gb', 'Ab', 'Bb', 'B']
}

ionian_mode_shifts = [
    [1, 2, 3, 4, 5, 6, 7],
    [],
    [],
    [1],
    [4, 7],
    [],
    [5, 1],
    [1, 2, 3, 4, 5, 6, 7]
]

def sign(value):
    if value < 0:
        return -1
    return 1


class NoteSequencer:
    def __init__(self):
        self.prev_note = 'F'
        self.current_scale = 'C'

    def find_scale(self):
        for scale in notes.keys():
            if scale == self.current_scale:
                continue
            for note in notes[scale]:
                if note == notes[scale][3]:
                    return scale
        for scale in notes.keys():
            if scale == self.current_scale:
                continue
            for note in notes[scale]:
                if note in notes[scale][3]:
                    return scale

    def next_note(self):
        if random.choice([True, False]):
            if random.choice([True, False]):
                self.prev_note = notes[self.current_scale][1]
            elif random.choice([True, False]):
                self.prev_note = notes[self.current_scale][6]
            else:
                self.prev_note = notes[self.current_scale][3]
        else:
            self.current_scale = self.find_scale()
            self.prev_note = notes[self.current_scale][3]
        print(self.prev_note)
        if random.choice([True, False]):
            self.prev_note = random.choice(notes[self.current_scale])
        return self.prev_note


class CircleOfFifthsSequencer:
    def __init__(self):
        self.prev_note = 0
        self.current_scale = 'C'
        self.current_octave = random.choice([3, 4])
        self.notes_played_in_current_scale = 0
        self.notes_played_in_current_octave = 0

    def next_note(self):
        # self._try_to_change_scale()
        self.notes_played_in_current_scale += 1
        self.notes_played_in_current_octave += 1
        return self._shift_ionian_note()

    def _shift_ionian_note(self):
        if ionian_mode_shifts[self.prev_note]:
            self.prev_note = random.choice(ionian_mode_shifts[self.prev_note])
        else:
            self.prev_note = random.choice([0, 4, 5, 7])
        scale = notes[self.current_scale]
        if random.choice([True, False]):
            if self.prev_note in [0, 7]:
                self._shift_octave()
        return scale[self.prev_note], self.current_octave

    def _try_to_change_scale(self):
        other_scale = 'F' if self.current_scale == 'F' else 'C'
        pr = self.notes_played_in_current_scale * 0.005
        # res = random.choices([True, False], [pr, 1 - pr])
        # print(f'pr: {pr} pressed: {self.notes_played_in_current_scale} res: {res}')
        if random.random() < pr:
            print('Changing scale')
            if self.prev_note == other_scale:
                self.current_scale = other_scale
            else:
                self.current_scale = other_scale
            self.notes_played_in_current_scale = 0

    def _shift_octave(self):
        pr = self.notes_played_in_current_octave * 0.1
        # pr *= max(self.current_octave, 1 - self.current_octave)
        if random.random() < pr:
            pr = abs(3 - self.current_octave) / 7
            pr *= 2
            self.current_octave += random.choices([-1, +1], [pr, 1 - pr])[0] * sign(pr)
            self._normalize_octave()
            self.notes_played_in_current_octave = 0

    def _normalize_octave(self):
        if self.current_octave < 1:
            self.current_octave = 1
        elif self.current_octave > 7:
            self.current_octave = 7
