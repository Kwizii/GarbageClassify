from args import args
from build_net import make_model
import torch
from collections import OrderedDict
from torchsummary import summary
from PIL import Image
from torchvision import transforms

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
            img = img.crop((-w_padding, 0, w+w_padding, h))
        else:
            t = int(w / ratio)
            h_padding = (t - h) // 2
            img = img.crop((0, -h_padding, w, h+h_padding))

        img = img.resize(self.size, self.interpolation)

        return img

if __name__ == '__main__':
    checkpoint = torch.load('./res_16_288_last1/model_21_9989_9236.pth')
    model = make_model(args)
    model.load_state_dict(OrderedDict((k.replace('module.',''),v) for (k,v) in checkpoint.items()))
    # model = model.cuda()
    # model.half()
    # img = Image.open('data/garbage_classify/train_data/img_1.jpg')
    # size = 288
    # mean, std = [0.485, 0.456, 0.406], [0.229, 0.224, 0.225]
    # transforms = transforms.Compose([
        # Resize((int(size * (256 / 224)), int(size * (256 / 224)))),
        # transforms.CenterCrop(size),
        # transforms.ToTensor(),
        # transforms.Normalize(mean=mean, std=std),
    # ])
    # img_tensor = transforms(img).unsqueeze(0).cuda()
    # result = model(img_tensor)
    # preds = torch.argmax(result)
    # print(preds)
    # summary(model,(3,329,329))
    scripted_model = torch.jit.script(model)
    scripted_model.save('./res_16_288_last1/scripted_float.pt')