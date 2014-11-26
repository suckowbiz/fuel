#Fuel
This software is under development. The system to build is a fuel consumption tracking tool based on a relation database, accessible via REST api. Motivation for this project is to have a Java EE 7 project for reference (following status quo) and for real use.

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
To run integration tests the [Failsafe Plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/) is used. Together with invocation of the Failsafe Plugin an embedded JBoss server is launched.

Run with: 
	
	mvn verify
	
Debug with [Failsafe Debug](http://maven.apache.org/surefire/maven-failsafe-plugin/examples/debugging.html):

    mvn -Dmaven.failsafe.debug verify
    
###Eclipse Setup
Run ``mvn eclipse:eclipse eclipse:configure-workspace install``.

###Test from Eclipse
###Setup
  - install TestNG Plugin from within Eclipse using Marketplace
  - install JBoss Tools with Arquillian support as "install new software" 
  ``http://download.jboss.org/jbosstools/targetplatforms/jbosstoolstarget/luna/``
  
###Configuration
``JBoss Tools->Arquillian->Enable default VM arguments`` (enable "Add the default VM arguments to the JUnit/TestNG launch configurations") and add ``-Djava.util.logging.manager=org.jboss.logmanager.LogManager`` and  ``-Djboss.home=target/wildfly-8.1.0.Final``

Now you are ready to run/debug from within eclipse by right click a test and "Run/Debug As -> TestNG Test"
    
#License
The software is available under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).