from flask import jsonify


def response(code, message, data):
    return jsonify({
        'code': code,
        'message': message,
        'data': data,
    })


def success_response(message='ok', data=None):
    return response(1, message, data)


def error_response(message='error', data=None):
    return response(0, message, data)
