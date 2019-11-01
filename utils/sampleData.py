import os
import random
import time
import glob
import subprocess


def get_size(filename):
    st = os.stat(filename)
    return st.st_size

def shuffle_array(array):
    return random.sample(array, len(array))

def generate_set_of_files(array, limit=6.4e+7, timestamp=True):
    current_size = 0.0
    current_files = []
    i = 0
    last_item = 0
    last_item_size = 0

    while i < len(array) - 1 and current_size + get_size(array[i]) < limit:
        last_item = array[i]
        last_item_size = get_size(last_item) # in Bytes
        current_files.append(last_item)
        current_size += last_item_size
        i += 1
    timstr=""
    if timestamp:
        timestr = time.strftime("(%Y%m%d-%H%M%S)")
    with open('sample{}.dat'.format(timstr), 'w+') as fp:
        # Write the name of the chosen files
        [fp.write("%s\n" % i) for i in current_files]
    return current_files

def sample(filespath):
    filenames = [os.path.join(filespath, fn)
                 for fn in os.listdir(filespath)]
    files = shuffle_array(filenames)
    files = generate_set_of_files(files)
    # TODO: Instead of call, maybe it would be better to use Popen(check)
    subprocess.call(['hdfs', 'dfs', '-rm', '-r', '/input/*'])
    for i in files:
        subprocess.call(['hdfs', 'dfs', '-put', i, '/input'])

sample("./pen-dataset/zip")
