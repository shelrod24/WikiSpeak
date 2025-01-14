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

# getting current version of ffmpeg as depending on version, t={max,fill}
version=`ffmpeg -version | grep "ffmpeg version" | sed -e 's/.*version \(.*\) Copyright.*/\1/' | sed 's/\(^.\).*/\1/'`
if [ $version -ge 4 ] ; then
	type=`echo "fill"`
else
	type=`echo "max"`
fi

# make slideshow with fps equal to that of the numebr of images ie duration should last for a second (it really doesnt)
ffmpeg -y -r ${IMAGE_COUNT} -i image-%3d.jpg -vf "scale=iw*min(720/iw\,480/ih):ih*min(720/iw\,480/ih) ,drawbox=y=ih/2-24: color=black@0.4: width=iw: height=48: t=${type}, drawtext=fontfile=comic.ttf : fontsize=30 : fontcolor=white : x=(w-text_w)/2:y=(h-text_h)/2 : text='${1}', pad=w=720: h=480: x=(720-iw)/2: y=(480-ih)/2: color=#333333" "TEMPSHORT.mp4"
# make redacted video which consists of the above, but without the term
ffmpeg -y -r ${IMAGE_COUNT} -i image-%3d.jpg -vf "scale=iw*min(720/iw\,480/ih):ih*min(720/iw\,480/ih), drawbox=y=ih/2-24: color=black@0.4: width=iw: height=48: t=${type}, pad=w=720: h=480: x=(720-iw)/2: y=(480-ih)/2: color=#333333" "TEMPSHORTREDACT.mp4"

# cd to original folder
cd ..
cd ..

# move mp4 to final
mv "${IMAGE_DIR}/TEMPSHORT.mp4" "${FINAL_DIR}/TEMPSHORT.mp4"
mv "${IMAGE_DIR}/TEMPSHORTREDACT.mp4" "${FINAL_DIR}/TEMPSHORTREDACT.mp4"
