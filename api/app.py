# Anubhav Natani
# Cybros Hackthon

from flask import Flask, request, jsonify
import traceback
import torchvision
import torchvision.datasets as dset
import torchvision.transforms as transforms
from torch.utils.data import DataLoader, Dataset
import torchvision.utils
import numpy as np
import random
from PIL import Image
import torch
from torch.autograd import Variable
import PIL.ImageOps
import torch.nn as nn
from torch import optim
import torch.nn.functional as F
import ast


class SiameseNetwork(nn.Module):
    def __init__(self):
        super(SiameseNetwork, self).__init__()
        self.cnn1 = nn.Sequential(
            nn.ReflectionPad2d(1),
            nn.Conv2d(1, 4, kernel_size=3),
            nn.ReLU(inplace=True),
            nn.BatchNorm2d(4),

            nn.ReflectionPad2d(1),
            nn.Conv2d(4, 8, kernel_size=3),
            nn.ReLU(inplace=True),
            nn.BatchNorm2d(8),


            nn.ReflectionPad2d(1),
            nn.Conv2d(8, 8, kernel_size=3),
            nn.ReLU(inplace=True),
            nn.BatchNorm2d(8),


        )

        self.fc1 = nn.Sequential(
            nn.Linear(8*100*100, 500),
            nn.ReLU(inplace=True),

            nn.Linear(500, 500),
            nn.ReLU(inplace=True),

            nn.Linear(500, 5))

    def forward_once(self, x):
        output = self.cnn1(x)
        output = output.view(output.size()[0], -1)
        output = self.fc1(output)
        return output

    def forward(self, input1, input2):
        output1 = self.forward_once(input1)
        output2 = self.forward_once(input2)
        return output1, output2


class SiameseNetworkDataset(Dataset):

    def __init__(self, imageFolderDataset, transform=None, should_invert=True):
        self.imageFolderDataset = imageFolderDataset
        self.transform = transform
        self.should_invert = should_invert

    def __getitem__(self, index):
        img1_tuple = self.imageFolderDataset.imgs[index]
        img1 = Image.open(img1_tuple[0])
        img1 = img1.convert("L")

        if self.should_invert:
            # img0 = PIL.ImageOps.invert(img0)
            img1 = PIL.ImageOps.invert(img1)

        if self.transform is not None:
            # img0 = self.transform(img0)
            img1 = self.transform(img1)

        return img1

    def __len__(self):
        return len(self.imageFolderDataset.imgs)


folder_dataset_test = dset.ImageFolder(root="./data/")
siamese_dataset = SiameseNetworkDataset(imageFolderDataset=folder_dataset_test,
                                        transform=transforms.Compose([transforms.Resize((100, 100)),
                                                                      transforms.ToTensor()]), should_invert=False)


pnet = SiameseNetwork()
pnet.load_state_dict(torch.load("./models/net.pth"))
# API defination
app = Flask(__name__)


@app.route("/")
def hello():
    return "Got App running"


@app.route('/predict', methods=['POST'])
def predict():
    try:

        json_ = request.form['image']
        image = ast.literal_eval(json_)

        # pnet = SiameseNetwork()

        # print(type(image))
        x0 = torch.FloatTensor(image)
        x0 = x0.view(1, 1, 100, 100)
        # json to image conversion"
        # image is x0
        # print(query)

        c = 0
        k = -1
        # print(siamese_dataset.__len__())
        for i in range(siamese_dataset.__len__()):
            x1 = siamese_dataset.__getitem__(i)
            x1 = x1.view(1, 1, 100, 100)
            output1, output2 = pnet(Variable(x0), Variable(x1))
            euclidean_distance = F.pairwise_distance(output1, output2)
            if(euclidean_distance.item() < 0.3):
                c = 1
                k = i
                break
            if(i == siamese_dataset.__len__()):
                k = -1

        return jsonify({'prediction': c,
                        'id': k})

    except:
        request.stream.read()
        return jsonify({'trace': traceback.format_exc()})


if __name__ == '__main__':

    app.run(debug=True)
