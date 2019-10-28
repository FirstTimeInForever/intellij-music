import random

notes = {
    'C': ['C', 'D', 'E', 'F', 'G', 'A', 'B', 'C'],
    'D': ['D', 'E', 'Gb', 'G', 'A', 'B', 'Db', 'D'],
    'E': ['E', 'Gb', 'Ab', 'A', 'B', 'Db', 'Eb', 'E'],
    'F': ['F', 'G', 'A', 'Bb', 'C', 'D', 'E', 'F'],
    'G': ['G', 'A', 'B', 'C', 'D', 'E', 'Gb', 'G'],
    'A': ['A', 'B', 'Db', 'D', 'E', 'Gb', 'Ab', 'A'],
    'B': ['B', 'Db', 'Eb', 'E', 'Gb', 'Ab', 'Bb', 'B']
}


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
