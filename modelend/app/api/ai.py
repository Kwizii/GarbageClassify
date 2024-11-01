import io

from PIL import Image
from flask import Blueprint, request

from app.common.api_response import success_response
from app.common.model import classify_service

# from app.common.model import classify_model, names
# from app.common.util import preprocess, postprocess

ai_bp = Blueprint('ai', __name__, url_prefix='/ai')


@ai_bp.route("/model", methods=["POST"])
def classify():
    files = []
    for fname in request.files:
        if request.files[fname].filename:
            files.append(fname)
    file = request.files[files[0]]
    image = Image.open(io.BytesIO(file.stream.read())).convert('RGB')
    image = classify_service.preprocess(image)
    result = classify_service.inference(image)
    preds = classify_service.postprocess(result)
    # inputs = preprocess(image)
    # outputs = classify_model(inputs)
    # preds = postprocess(outputs, names=names, topk=2, T=0.25)
    return success_response(data=preds[0])
