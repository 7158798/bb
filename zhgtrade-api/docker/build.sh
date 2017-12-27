#!/bin/bash
echo $0
basepath=$(cd `dirname $0`; pwd)
cp $basepath/../build/libs/zhgtrade-api.war $basepath/ROOT.war
tag="$1"
if [ "$tag" = "" ]; then
 tag="latest"
fi
host=""
image="api:$tag"
sudo docker build -t $host$image $basepath
