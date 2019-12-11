#!/bin/sh

DATA_PATH="$1/../pen-dataset/original"
UNZIP="$1/../pen-dataset/unzip"
SPLIT="$1/../pen-dataset/split"
ZIP="$1/../pen-dataset/zip"
SPLITSIZE="$2"
COMPRESSION_RATIO=$3
# UNZIP ORIGINAL DATA
gunzip -k "$DATA_PATH"/*
mv "$DATA_PATH"/*.txt $UNZIP

# RENAME
for entry in "$UNZIP"/*
do
    NOEXTENSION=${entry%.txt}
    mv $entry $NOEXTENSION
done
mv "$UNZIP"/* $SPLIT
# cp "$UNZIP"/* $SPLIT

# SPLIT
for entry in "$SPLIT"/*
do
    split -b $SPLITSIZE "$entry" "$entry"_
    rm $entry
done
mv "$SPLIT"/* $ZIP
# /bin/cp "$SPLIT"/* $ZIP

# ZIP
for entry in "$ZIP"/*
do
    zip "-$COMPRESSION_RATIO" "$entry.zip" "$entry"
    rm $entry
done
