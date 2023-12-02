import traceback

from flask import Flask, request, Response

from app.api import config_bp
from app.common.api_response import error_response
from app.common.exceptions import CustomException


def create_app():
    app = Flask(__name__)
    config_bp(app)

    @app.before_request
    def log_request_info():
        # 获取请求的方法、路径和请求数据
        method = request.method
        path = request.path
        content_type = request.content_type
        data = None
        if content_type is not None:
            if 'application/json' in content_type:
                data = request.json
            else:
                data = request.data
        # 打印请求信息
        print(f"Request: {method} {content_type}: {path}")
        if data is not None:
            print(f"Data: {str(data)}")

    # 自定义错误处理函数
    @app.errorhandler(Exception)
    def handle_exception(e: Exception):
        if isinstance(e, AssertionError):
            return error_response(e.args[0])
        elif isinstance(e, CustomException):
            return error_response(e.args[0], e.data)
        else:
            traceback.print_exception(e)
        return error_response('服务器错误'), 200

    @app.after_request
    def log_response_info(response: Response):
        # 打印响应信息
        print(f"Response: {response.json}")
        return response

    return app
