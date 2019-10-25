all: class
	jar cf wc.jar *.class

class:
	hadoop com.sun.tools.javac.Main *.java

clean:
	rm -f *.class
