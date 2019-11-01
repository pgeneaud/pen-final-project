.PHONY: clean run drun compile

OUTPUT=/output
INPUT=/input
JAR=wc.jar

all: cleanhdfs compile

compile:
	hadoop com.sun.tools.javac.Main *.java
	jar cf $(JAR) *.class

run:
	hadoop jar wc.jar WordCounter $(INPUT) $(OUTPUT)

drun: cleanhdfs run

cleanhdfs:
	hdfs dfs -rm -r $(OUTPUT)

clean:
	rm -f *.class
