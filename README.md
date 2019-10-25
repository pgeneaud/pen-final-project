# Description #
This code is adapted from the code of Michael Cutler (http://cutler.io/2012/07/hadoop-processing-zip-files-in-mapreduce/). It's a wordcount, but with zip files.

# How to use it #

* Compress our text files into an (or multiple) zip file.
* Put our zip file into the directory /input (or wathever you call it) of hdfs
* Compile the code with  
  > make  
You can remove *.class files with   
  > make clean  
Ensure that you have a correct hadoop environment.
* Run it on hadoop with the command   
  > hadoop jar wc-zip.jar WordCounter /input /output

