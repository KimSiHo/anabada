#!/bin/bash

if [ -d /home/ec2-user/app/zip ]; then
    sudo rm -rf /home/ec2-user/app/zip
fi
sudo mkdir -vp /home/ec2-user/app/zip