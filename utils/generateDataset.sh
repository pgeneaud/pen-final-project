# #!/bin/sh

PATH="./../pen-dataset/original"
UNZIP="./../pen-dataset/unzip"
SPLIT="./../pen-dataset/split"
ZIP="./../pen-dataset/zip"
SPLITSIZE="512K"
COMPRESSION_RATIO=7

# UNZIP ORIGINAL DATA
/bin/gunzip -k "$PATH"/*
/bin/mv "$PATH"/*.txt $UNZIP

# RENAME
for entry in "$UNZIP"/*
do
    NOEXTENSION=${entry%.txt}
    /bin/mv $entry $NOEXTENSION
done
/bin/mv "$UNZIP"/* $SPLIT
# /bin/cp "$UNZIP"/* $SPLIT

# SPLIT
for entry in "$SPLIT"/*
do
    /usr/bin/split -b $SPLITSIZE "$entry" "$entry"_
    /bin/rm $entry
done
/bin/mv "$SPLIT"/* $ZIP
# /bin/cp "$SPLIT"/* $ZIP

# ZIP
for entry in "$ZIP"/*
do
    /usr/bin/zip "-$COMPRESSION_RATIO" "$entry.zip" "$entry"
    /bin/rm $entry
done
