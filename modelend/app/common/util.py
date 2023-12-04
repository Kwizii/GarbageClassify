import base64
import io

import torch
from PIL import Image
from torchvision import transforms

from app.common.consts import PREPROCESS_ARGS


class Resize(object):
    def __init__(self, size, interpolation=Image.BILINEAR):
        self.size = size
        self.interpolation = interpolation

    def __call__(self, img):
        # padding
        ratio = self.size[0] / self.size[1]
        w, h = img.size
        if w / h < ratio:
            t = int(h * ratio)
            w_padding = (t - w) // 2
            img = img.crop((-w_padding, 0, w + w_padding, h))
        else:
            t = int(w / ratio)
            h_padding = (t - h) // 2
            img = img.crop((0, -h_padding, w, h + h_padding))

        img = img.resize(self.size, self.interpolation)

        return img


def pred_transform(size, mean=PREPROCESS_ARGS['mean'], std=PREPROCESS_ARGS['std']):
    return transforms.Compose([
        Resize((int(size * (256 / 224)), int(size * (256 / 224)))),
        transforms.CenterCrop(size),
        transforms.ToTensor(),
        transforms.Normalize(mean=mean, std=std),
    ])


def preprocess(inputs, size=PREPROCESS_ARGS['size'], mean=PREPROCESS_ARGS['mean'], std=PREPROCESS_ARGS['std'],
               half=False):
    transform = pred_transform(size, mean, std)
    tensors = transform(inputs).cuda()
    if len(tensors.shape) == 3:
        tensors = tensors.unsqueeze(0)
    if half:
        tensors = tensors.half()
    return tensors


def postprocess(inputs, names=None, topk=5, T=1):
    probs, classIdxes = torch.topk(inputs, k=topk, dim=-1)
    probs = torch.nn.functional.softmax(probs / T, dim=-1)
    preds = []
    for prob, classIdx in zip(probs, classIdxes):
        preds.append(
            [{'cls': names[str(c.item())] if names else c.item(), 'conf': str(round(p.item() * 100, 1)) + '%'} for p, c
             in
             zip(prob, classIdx)])
    return preds


def rgb_to_hex(rgb):
    # 将RGB值转换为十六进制颜色代码
    r, g, b = rgb
    hex_code = "#{:02x}{:02x}{:02x}".format(r, g, b)
    return hex_code


def img2base64(image: Image):
    # Convert the image to a bytes buffer
    with io.BytesIO() as buffer:
        image.save(buffer, format='JPEG')
        buffer.seek(0)
        # Encode the bytes buffer as base64
        encoded_image = base64.b64encode(buffer.read()).decode('utf-8')
        return encoded_image


def base64_hash(data: bytes):
    encoded_bytes = base64.b64encode(data)
    return encoded_bytes.decode('utf-8')


def base64_to_file(data: str, path: str):
    decoded_data = base64.b64decode(data)
    with open(path, 'wb') as f:
        f.write(decoded_data)


if __name__ == '__main__':
    # print(sm3_hash("ywy@123456".encode('utf-8')))
    # print(sm3_hash("yRFXBao1nBMC30qnju3STA==" + "dc2abb1edfed49b692b6db415e6f4a26" + "SluUXJWtO4K+oIjh1NYw7w=="))
    # print(sm3_hash("2016f132fa96dff740e1e2283d6d2140d3e4ebfab15a1df523cd103ef4ae602d"))
    # print(base64_hash(open('../../samples/x.json', 'rb').read()))
    # print(os.path.exists('M'))
    pass
