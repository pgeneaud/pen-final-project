#!/usr/bin/env python3

import os.path
import subprocess
import threading
import time

from time import sleep
from os import system
from subprocess import STDOUT, PIPE


def get_ram_stats(fp):
    ram_percentage = 0
    ram_stat_file = '/proc/meminfo'
    with open(ram_stat_file) as fpr:
        total_ram = float(fpr.readline().split()[1])
        free_ram = float(fpr.readline().split()[1])
        available_ram = float(fpr.readline().split()[1])

    ram_percentage = (available_ram / total_ram) * 100
    
    fp.write("{0:.2f}\n".format(ram_percentage))
    return total_ram, available_ram


def get_cpu_stats(fp, prev_total, prev_idle):
    cpu_percentage = 0
    cpu_stat_file = '/proc/stat'
    
    with open(cpu_stat_file) as fpc:
        cpu_stat = fpc.readline().split()[1:]
        cpu_stat = [float(x) for x in cpu_stat]

    total = sum(cpu_stat[0:8])
    idle = sum(cpu_stat[3:5])

    diff_total = total - prev_total
    diff_idle = idle - prev_idle
    cpu_usage = diff_total - diff_idle

    if diff_total != 0:
        cpu_percentage = (cpu_usage / diff_total) * 100

    fp.write("{0:.2f}\n".format(cpu_percentage))

    return total, idle


def run_java():
    args_list = ['hadoop', 'jar', './wc.jar', 'WordCounter', '/input', '/output']
    subprocess.call(args_list)

try:
    prev_total_cpu = 0
    prev_idle_cpu = 0

    path = "./pen-dataset/zip"
    
    t = threading.Thread(target=run_java)
    t.start()
    fr = open('stats_ram.dat', 'w+')
    fc = open('stats_cpu.dat', 'w+')
    while t.is_alive():
        (prev_total_cpu, prev_idle_cpu) = get_cpu_stats(fc, prev_total_cpu, prev_idle_cpu)
        get_ram_stats(fr)
        sleep(1)
    t.join()
except KeyboardInterrupt:
    print("Done")
