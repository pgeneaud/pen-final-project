# Description
This code is adapted from the code of Michael Cutler (http://cutler.io/2012/07/hadoop-processing-zip-files-in-mapreduce/). It's a wordcount, but with zip files and other utils that will help us automatize its performance.

# Repository setup
`git clone --recurse-submodules https://github.com/osmarcedron/pen-final-project

# How to use it
0. Ensure that you have a correct working bullet-proof hadoop environment, i.e. `hadoop`, `hdfs` commands work properly.
1. Cut and compress the data
   - The bash script [generateDataset.sh](./utils/generateDataset.sh) grabs our [raw data](https://github.com/osmarcedron/pen-dataset) and apply the following steps to make it usable:
	 - First of all, we **unzip** the content of our dataset files and then do some proper renaming which will turn out useful in the following steps.
	 - Once the .txt files are unziped, they are **splitted** given a `SPLITSIZE`(e.g. 512KB).
	 - Finally, we use **zip** with a new compression ratio (e.g. -7) over the split files.
2. Put our zip file into the HDFS directory */input* (or wathever you call it)
   - The python script [sampleData.py](./utils/sampleData.py) is in charge of selecting which files (already zipped) will be used as our input (the list of chosen files is found in the generated `sample.dat` file, timestamp option available), as an upside there is no need to manually checking that these files are less than the HDFS limit (e.g. 64MB), the script does it automagically! (be warned that it might take a while to move all the files to HDFS).
3. Use the Makefile to get things done!
   - To run: `make run` or `make drun`(cleans the */output* folder in HDFS before executing the code)
   - To clean: `make clean`
   - To (just) compile: `make all` or `make`
4. Getting metrics (so far only two)
   - Thanks to the steps 1 and 2 already automatized, it is possible to easily perform the metrics such as CPU consumption and RAM utilization in order to evaluate the map/reduce operations performed. The main idea is simply to start a thread that does the map/reduce and while this thread is alive save the aforementioned metrics. The code can be found in [sampleData.py](./utils/getMetrics.py) and it saves all the data collected in two files (`stats_ram.dat` and `stats_cpu.dat`).

# Initial code
It is assumed that you are at the root of the project.

To run the code:
```
sh utils/generateDataset.sh
python utils/sampleData.py
make drun

```

In order to run metrics:
```
sh utils/generateDataset.sh
python utils/sampleData.py
make all
python utils/getMetrics.py

```
