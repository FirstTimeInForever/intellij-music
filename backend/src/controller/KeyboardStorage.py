from datetime import datetime
from collections import namedtuple

KeyboardEvent = namedtuple('KeyboardEvent', ['time', 'key_code', 'layout'])


class KeyboardStorage:
    def __init__(self, time_interval: float):
        self.keyboard_taps = []
        self.time_interval = time_interval

    def add_event(self, key_info: dict):
        self.keyboard_taps.append(KeyboardEvent(datetime.now(), key_info['code'], key_info['layout']))

    def __update_storage(self):
        current_date = datetime.now()
        self.keyboard_taps = [event for event in self.keyboard_taps if
                              (current_date - event.time).seconds <= self.time_interval]

    @property
    def current_frequency(self):
        self.__update_storage()
        return len(self.keyboard_taps) / self.time_interval
