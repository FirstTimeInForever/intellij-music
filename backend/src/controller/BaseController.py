from .KeyboardStorage import KeyboardStorage


class BaseController:
    TIME_INTERVAL = 10.0

    def __init__(self):
        self.keyboard_storage = KeyboardStorage(BaseController.TIME_INTERVAL)

    def keyboard_pressed(self, key_info: dict):
        self.keyboard_storage.add_event(key_info)
