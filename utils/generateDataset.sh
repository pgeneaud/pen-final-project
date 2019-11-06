#!/bin/sh

DATA_PATH="./../pen-dataset/original"
UNZIP="./../pen-dataset/unzip"
SPLIT="./../pen-dataset/split"
ZIP="./../pen-dataset/zip"
SPLITSIZE="512K"
COMPRESSION_RATIO=7

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
