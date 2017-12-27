#!/bin/bash
echo $0
basepath=$(cd `dirname $0`; pwd)
cp $basepath/../build/distributions/zhgtrade-task.tar $basepath/zhgtrade-task.tar
tag="$1"
if [ "$tag" = "" ]; then
 tag="latest"
fi
host=""
image="task:$tag"
sudo docker build -t $host$image $basepath
