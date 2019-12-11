import os
#import random
#import time
#import glob
import subprocess
#import argparse

split_sizes = ["128K", "256K", "512K", "1M", "2M"]
compression_ratios = range(1, 10)
genfilename = "generateDataset.sh"
communicator = "sampleData_any {} {}"


dir_path = os.path.dirname(os.path.realpath(__file__))
genpath = dir_path + "/" + genfilename


def generateDataset(splitsize, comp_ratio):
    print ("Generating the dataset with split size {} and {}...".format(splitsize, comp_ratio))
    subprocess.call(['sh', genpath, dir_path, splitsize, str(comp_ratio)])


#TODO: implement a parser for the results


def removeZipFiles():
    print ("Removing zip files...")
    subprocess.call(['rm', dir_path+"/../pen-dataset/zip/*"])


def main():
    for splitsize in split_sizes:
        for comp_ratio in compression_ratios:
            generateDataset(splitsize, compratio)
            execfile(dir_path + communicator.format(splitsize, comp_ratio))
            removeZipFiles()


main()

