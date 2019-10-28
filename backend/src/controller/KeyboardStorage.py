from datetime import datetime
from collections import namedtuple, deque
from threading import Event, Thread

KeyboardEvent = namedtuple('KeyboardEvent', ['time', 'key_code', 'layout'])


class KeyboardStorage:
    def __init__(self, time_interval: float):
        self.__update_event = Event()
        self.__update_thread = Thread(target=self.__update_storage_loop, daemon=True)
        self.__keyboard_taps = deque(maxlen=None)
        self.time_interval = time_interval

        self.__update_thread.start()

    def add_event(self, key_info: dict):
        self.__keyboard_taps.append(KeyboardEvent(datetime.now(), key_info['code'], key_info['layout']))

        self.__update_event.set()

    def __update_storage_loop(self):
        while True:
            self.__update_event.wait()
            current_date = datetime.now()

            while self.__keyboard_taps and (current_date - self.__keyboard_taps[0].time).seconds >= self.time_interval:
                self.__keyboard_taps.popleft()

    @property
    def current_frequency(self):
        return len(self.__keyboard_taps) / self.time_interval
