#Fuel
This software is under development. The system to build is a fuel consumption tracking tool based on a relation database, accessable via REST api.

#Development
To build the application Maven is used to have a high degree of automation. Source code is developed under principles of Clean Code to ensure code quality with Java 7. Package organization follows Entity/Control/Boundary pattern as of presentations of Adam Bien to standardize package structure and serve business first. For development the web application Java EE 7 standard stack is used. The goal is to stay with the Java EE 7 standard as much as possible. Database PostgresQL serves for backend storage.

##Artifact
Packaging this software results in file fuel.war that is meant to be deployed to Jboss Wildfly 8 application server.

##Tests
Tests are triggered automatically on scm commit at [drone.io](drone.io) but can be triggered manually as well (see below).

Actual state: [![Build Status](https://drone.io/bitbucket.org/tsuckow/fuel/status.png)](https://drone.io/bitbucket.org/tsuckow/fuel/latest)

###Unit Tests
Unit tests are supported by [Surefire Plugin](http://maven.apache.org/surefire/maven-surefire-plugin/). To run Unit Tets use IDE or invoke:

    mvn test

###Integration Tests
To run integration tests the [Failsafe Plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/) is used. Together with invokation of the Failsafe Plugin an embedded JBoss Wildfly server is launched.

Run with: 
	
	mvn verify
	
Debug with [Failsafe Debug](http://maven.apache.org/surefire/maven-failsafe-plugin/examples/debugging.html):

    mvn -Dmaven.failsafe.debug verify
    
#License
The software is available under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).