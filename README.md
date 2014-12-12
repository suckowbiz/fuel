#Fuel
A tool to manage vehicle refuelings based on Java EE 7 stack meant to be run on JBoss Wildfly (but should be portable to any other Java EE compliant server). Developed under principles of [Clean Code](http://de.wikipedia.org/wiki/Clean_Code).

Maven is used for build management.

Main purpose of this project is being taken as reference for Java EE development.

##Automated Tests
Tests are triggered automatically on scm commit via a web hook at [drone.io](drone.io) but can be triggered manually as well (see below).

Actual state: [![Build Status](https://drone.io/bitbucket.org/tsuckow/fuel/status.png)](https://drone.io/bitbucket.org/tsuckow/fuel/latest)

###Unit Tests
Unit tests are supported by [Surefire Plugin](http://maven.apache.org/surefire/maven-surefire-plugin/). To run Unit Tets use your IDE or by command line: 
  ``mvn test``

###Integration Tests
To run integration tests the [Failsafe Plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/) is used. Together with invocation of the Failsafe Plugin an embedded JBoss server is launched.

Run with: 
  ``mvn verify``
	
Debug with [Failsafe Debug](http://maven.apache.org/surefire/maven-failsafe-plugin/examples/debugging.html): 
  ``mvn -Dmaven.failsafe.debug verifyi``
    
###Arquillian Test from Eclipse
###Setup
  - install TestNG Plugin from within Eclipse using Marketplace
  - install JBoss Tools with Arquillian support as "install new software" 
  ``http://download.jboss.org/jbosstools/targetplatforms/jbosstoolstarget/luna/``
  
###Configuration
``JBoss Tools->Arquillian->Enable default VM arguments`` (enable "Add the default VM arguments to the JUnit/TestNG launch configurations") and add ``-Djava.util.logging.manager=org.jboss.logmanager.LogManager`` and  ``-Djboss.home=target/wildfly-8.1.0.Final``

Now you are ready to run/debug from within Eclipse by right clickinga test and selecting "Run/Debug As -> TestNG Test".
    
#License
The software is available under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
