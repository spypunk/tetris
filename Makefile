build:
	mvn clean package assembly:single
	mvn clean
	mvn package
	mvn exec:java

