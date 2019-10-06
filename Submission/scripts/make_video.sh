#!/bin/bash

# Makes a video that lasts for a second
# Video consists of a slideshow of all images in temp with the format image-###.jpg
# Images will automatically be obtained from temps folder
# First input is the term to be shown in video
# Author: Leon Chin (lchi184)

IMAGE_DIR="./temps/image"
FINAL_DIR="./temps/final"
mkdir -p ${IMAGE_DIR}

# change directory to temps folder
cd ${IMAGE_DIR}

# getting the number of images in temp folder and minusing 1 as it tends to only give a single frame to the last image for some reason
# thus final slideshow is shorter than expected
IMAGE_COUNT=`ls image-???.jpg | wc -l`


if [ $IMAGE_COUNT -eq 1 ] ; then
	ffmpeg -loop 1 -y -t 0.3 -i image-000.jpg -vf "drawtext=fontfile=comic.ttf : fontsize=30 : fontcolor=white : x=(w-text_w)/2:y=(h-text_h)/2 : text='${1}',pad=ceil(iw/2)*2:ceil(ih/2)*2" "TEMPSHORT.mp4"
else
	# make slideshow with fps equal to that of the numebr of images ie duration should last for a second (it really doesnt)
	ffmpeg -y -r ${IMAGE_COUNT} -i image-%3d.jpg -vf "drawtext=fontfile=comic.ttf : fontsize=30 : fontcolor=white : x=(w-text_w)/2:y=(h-text_h)/2 : text='${1}',pad=ceil(iw/2)*2:ceil(ih/2)*2" "TEMPSHORT.mp4"
fi

# cd to original folder
cd ..
cd ..

# move mp4 to final
mv "${IMAGE_DIR}/TEMPSHORT.mp4" "${FINAL_DIR}/TEMPSHORT.mp4"