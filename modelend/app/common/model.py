import os
from collections import OrderedDict

import torch

from app.common import consts
from app.common.resnetxt_wsl import resnext101_32x16d_wsl
import torchvision.models as models
import torchvision.transforms as transforms
from PIL import Image
from torch import nn

from app.common.util import Resize

class2names = {
    "0": "其他垃圾/一次性快餐盒",
    "1": "其他垃圾/污损塑料",
    "2": "其他垃圾/烟蒂",
    "3": "其他垃圾/牙签",
    "4": "其他垃圾/破碎花盆及碟碗",
    "5": "其他垃圾/竹筷",
    "6": "厨余垃圾/剩饭剩菜",
    "7": "厨余垃圾/大骨头",
    "8": "厨余垃圾/水果果皮",
    "9": "厨余垃圾/水果果肉",
    "10": "厨余垃圾/茶叶渣",
    "11": "厨余垃圾/菜叶菜根",
    "12": "厨余垃圾/蛋壳",
    "13": "厨余垃圾/鱼骨",
    "14": "可回收物/充电宝",
    "15": "可回收物/包",
    "16": "可回收物/化妆品瓶",
    "17": "可回收物/塑料玩具",
    "18": "可回收物/塑料碗盆",
    "19": "可回收物/塑料衣架",
    "20": "可回收物/快递纸袋",
    "21": "可回收物/插头电线",
    "22": "可回收物/旧衣服",
    "23": "可回收物/易拉罐",
    "24": "可回收物/枕头",
    "25": "可回收物/毛绒玩具",
    "26": "可回收物/洗发水瓶",
    "27": "可回收物/玻璃杯",
    "28": "可回收物/皮鞋",
    "29": "可回收物/砧板",
    "30": "可回收物/纸板箱",
    "31": "可回收物/调料瓶",
    "32": "可回收物/酒瓶",
    "33": "可回收物/金属食品罐",
    "34": "可回收物/锅",
    "35": "可回收物/食用油桶",
    "36": "可回收物/饮料瓶",
    "37": "有害垃圾/干电池",
    "38": "有害垃圾/软膏",
    "39": "有害垃圾/过期药物"
}

args = {}
args['arch'] = 'resnext101_32x16d_wsl'
args['pretrained'] = False
args['num_classes'] = 40
args['image_size'] = 320


class ClassifyService:
    def __init__(self, model_path):
        self.model = self.build_model(model_path)
        self.model.eval()
        self.transforms = self.pred_transforms()
        self.device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    def build_model(self, model_path):
        if args['arch'] == 'resnext101_32x16d_wsl':
            model = resnext101_32x16d_wsl(pretrained=False, progress=False)
        if args['arch'] == 'resnext101_32x8d':
            model = models.__dict__[args['arch']]()
        layerName, layer = list(model.named_children())[-1]
        exec("model." + layerName + "=nn.Linear(layer.in_features," + str(args['num_classes']) + ")")
        if torch.cuda.is_available():
            modelState = torch.load(model_path)
            modelState = dict((k.replace('module.', ''), v) for (k, v) in modelState.items())
            modelState['fc.weight'] = modelState['fc.1.weight']
            modelState['fc.bias'] = modelState['fc.1.bias']
            del modelState['fc.1.weight']
            del modelState['fc.1.bias']
            model.load_state_dict(modelState)
            model = model.cuda()
        else:
            modelState = torch.load(model_path, map_location='cpu')
            model.load_state_dict(modelState)
        return model

    def pred_transforms(self):
        mean, std = [0.485, 0.456, 0.406], [0.229, 0.224, 0.225]
        infer_transformation = transforms.Compose([
            Resize((args['image_size'], args['image_size'])),
            transforms.ToTensor(),
            transforms.Normalize(mean=mean, std=std),
        ])
        return infer_transformation

    def preprocess(self, data):
        return self.transforms(data)

    def inference(self, data):
        data = data.unsqueeze(0)
        data = data.to(self.device)
        with torch.no_grad():
            pred = self.model(data)
            return pred

    def postprocess(self, inputs, names=None, topK=5, T=1):
        if names is None:
            names = class2names
        probs, classIdxes = torch.topk(inputs, k=topK, dim=-1)
        probs = torch.nn.functional.softmax(probs / T, dim=-1)
        preds = []
        for prob, classIdx in zip(probs, classIdxes):
            preds.append(
                [{'cls': names[str(c.item())] if names else c.item(),
                  'conf': str(round(p.item() * 100, 1)) + '%'} for p, c in zip(prob, classIdx)])
        return preds


# classify_model = torch.jit.load('app/static/scripted_float.pt').cuda()

classify_service = ClassifyService(consts.MODEL_PATH)

if __name__ == '__main__':
    model_path = '../static/model_21_9989_9236.pth'
    service = ClassifyService(model_path)
    img = Image.open(r"C:\Users\ChanvoBook\Desktop\garbage_classify\train_data\img_1.jpg")
    img = service.preprocess(img)
    result = service.inference(img)
    result = service.postprocess(result)
    print(result)
    # input_dir = '../data/garbage_classify/test'
    # files = os.listdir(input_dir)
    # t1 = int(time.time() * 1000)
    # for file_name in files:
    #     file_path = os.path.join(input_dir, file_name)
    #     img = Image.open(file_path)
    #
    #     img = infer.pre_img(img)
    #     tt1 = int(time.time() * 1000)
    #     result = infer._inference({'input_img': img})
    #     print(result)
    #     tt2 = int(time.time() * 1000)
    #     print((tt2 - tt1) / 100)
    # t2 = int(time.time() * 1000)
    # print((t2 - t1) / 100)
