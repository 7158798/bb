#!/bin/bash
echo $0
basepath=$(cd `dirname $0`; pwd)
cp $basepath/../build/libs/zhgtrade-newfront.war $basepath/ROOT.war
tag="$1"
if [ "$tag" = "" ]; then
 tag="latest"
fi
host=""
image="frontend:$tag"
sudo docker build -t $host$image $basepath
