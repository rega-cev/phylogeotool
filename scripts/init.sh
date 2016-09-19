#!/bin/bash
DIR=$(mktemp -d -t pplacer) || exit 1
#filename=$(basename "$DIR")
#echo $filename
#filename="${filename##*.}"
#echo $filename
echo "$DIR"
