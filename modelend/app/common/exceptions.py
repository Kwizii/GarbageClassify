class CustomException(Exception):
    def __init__(self, message='服务器异常', data=None):
        super().__init__(message)
        self.data = data
