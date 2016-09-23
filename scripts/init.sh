#!/bin/bash
if [ "$#" -eq 1 ]; then
DIR=$(mktemp -d -t pplacer.XXXXXXXX) || exit 1
arrDIR=(${DIR//./ })
echo "${arrDIR[0]}.$1"
rm -r "$DIR"
else
DIR=$(mktemp -d -t pplacer.XXXXXXXX) || exit 1
#filename=$(basename "$DIR")
#echo $filename
#filename="${filename##*.}"
#echo $filename
echo "$DIR"
fi
