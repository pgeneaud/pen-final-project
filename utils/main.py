import os
import random
import time
import glob
import subprocess

import sampleData

split_sizes = ["128K", "256K", "512K", "1M", "2M"]
compression_ratios = range(1, 10)
genpath = "./utils/generateDataset.sh"
sampler = "./utils/sampleData.py"


def generateDataset(split_size, comp_ratio):
    print ("Generating the dataset with split size {} and {}...".format(split_size, comp_ratio))
    subprocess.call(['sh', genpath, split_size, str(comp_ratio)])


#TODO: implement a parser for the results


def removeZipFiles():
    print ("Removing zip files...")
    subprocess.call(['rm', "./pen-dataset/zip/*"])


def main():
    for split_size in split_sizes:
        for comp_ratio in compression_ratios:
            generateDataset(split_size, comp_ratio)
            sampleData.sample("./pen-dataset/zip")
            subprocess.call(['hdfs', 'dfs', '-rm', '/input/*'])
            removeZipFiles()

main()

