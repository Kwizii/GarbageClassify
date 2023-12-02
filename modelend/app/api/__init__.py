from flask import Flask

from .ai import ai_bp


def config_bp(app: Flask):
    app.register_blueprint(ai_bp)
