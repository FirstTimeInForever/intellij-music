def generate_midi_notes():
    notes = ['C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab', 'A', 'Bb', 'B']
    target = {}
    current_value = 0
    for octave in range(7):
        for note in notes:
            assert(current_value < 128)
            target[current_value] = (note, octave)
            current_value += 1
    return target
