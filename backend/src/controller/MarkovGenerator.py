from collections import Counter, defaultdict, namedtuple
import random
import mido

Note = namedtuple('Note', ['note', 'duration'])


class MarkovChain:
    def __init__(self):
        self.chain = defaultdict(Counter)
        self.sums = defaultdict(int)

    @staticmethod
    def create_from_dict(dict):
        m = MarkovChain()
        # bugged!
        for from_note, to_notes in dict.items():
            for k, v in to_notes.items():
                m.add(from_note, k, v)
        return m

    def _serialize(self, note, duration):
        return Note(note, duration)

    def __str__(self):
        return str(self.get_chain())

    def add(self, from_note, to_note, duration):
        self.chain[from_note][self._serialize(to_note, duration)] += 1
        self.sums[from_note] += 1

    def get_next(self, seed_note):
        if seed_note is None or seed_note not in self.chain:
            random_chain = self.chain[random.choice(list(self.chain.keys()))]
            return random.choice(list(random_chain.keys()))
        next_note_counter = random.randint(0, self.sums[seed_note])
        for note, frequency in self.chain[seed_note].items():
            next_note_counter -= frequency
            if next_note_counter <= 0:
                return note

    def merge(self, other):
        assert isinstance(other, MarkovChain)
        self.sums = defaultdict(int)
        for from_note, to_notes in other.chain.items():
            self.chain[from_note].update(to_notes)
        for from_note, to_notes in self.chain.items():
            self.sums[from_note] = sum(self.chain[from_note].values())

    def get_chain(self):
        return {k: dict(v) for k, v in self.chain.items()}

    def print_as_matrix(self, limit=10):
        columns = []
        for from_note, to_notes in self.chain.items():
            for note in to_notes:
                if note not in columns:
                    columns.append(note)
        _col = lambda string: '{:<8}'.format(string)
        _note = lambda note: '{}:{}'.format(note.note, note.duration)
        out = _col('')
        out += ''.join([_col(_note(note)) for note in columns[:limit]]) + '\n'
        for from_note, to_notes in self.chain.items():
            out += _col(from_note)
            for note in columns[:limit]:
                out += _col(to_notes[note])
            out += '\n'
        print(out)


class Parser:
    def __init__(self, filename, verbose=False):
        """
        This is the constructor for a Serializer, which will serialize
        a midi given the filename and generate a markov chain of the
        notes in the midi.
        """
        self.filename = filename
        # The tempo is number representing the number of microseconds
        # per beat.
        self.tempo = None
        # The delta time between each midi message is a number that
        # is a number of ticks, which we can convert to beats using
        # ticks_per_beat.
        self.ticks_per_beat = None
        self.markov_chain = MarkovChain()
        self._parse(verbose=verbose)

    def _parse(self, verbose=False):
        """
        This function handles the reading of the midi and chunks the
        notes into sequenced "chords", which are inserted into the
        markov chain.
        """
        midi = mido.MidiFile(self.filename)
        self.ticks_per_beat = midi.ticks_per_beat
        previous_chunk = []
        current_chunk = []
        for track in midi.tracks:
            for message in track:
                if verbose:
                    print(message)
                if message.type == "set_tempo":
                    self.tempo = message.tempo
                elif message.type == "note_on":
                    if message.time == 0:
                        current_chunk.append(message.note)
                    else:
                        self._sequence(previous_chunk,
                                       current_chunk,
                                       message.time)
                        previous_chunk = current_chunk
                        current_chunk = []

    def _sequence(self, previous_chunk, current_chunk, duration):
        """
        Given the previous chunk and the current chunk of notes as well
        as an averaged duration of the current notes, this function
        permutes every combination of the previous notes to the current
        notes and sticks them into the markov chain.
        """
        for n1 in previous_chunk:
            for n2 in current_chunk:
                self.markov_chain.add(
                    n1, n2, self._bucket_duration(duration))

    def _bucket_duration(self, ticks):
        """
        This method takes a tick count and converts it to a time in
        milliseconds, bucketing it to the nearest 250 milliseconds.
        """
        try:
            ms = ((ticks / self.ticks_per_beat) * self.tempo) / 1000
            return int(ms - (ms % 250) + 250)
        except TypeError:
            raise TypeError(
                "Could not read a tempo and ticks_per_beat from midi")

    def get_chain(self):
        return self.markov_chain
