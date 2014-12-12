Drone.io automated ... [![Build Status](https://drone.io/bitbucket.org/tsuckow/fuel/status.png)](https://drone.io/bitbucket.org/tsuckow/fuel/latest)

#What is it?
You are reading the README.md file distributed with the root pom of a project that contains tooling to manage vehicle refuelings.

For further information please see README.md distributed with child pom(s).

#Vehicle Refuelings?
This is a tool to manage refueling/fuel consumption of your vehicle(s). Its artifacts are based on Java EE 7 stack meant to be run on JBoss Wildfly (but should be portable to any other Java EE compliant server). Developed under consideration of the principles of [Clean Code](http://de.wikipedia.org/wiki/Clean_Code) with the purpose to have a reference project reflecting state of the art Java EE 7 instrumentation.

For build management maven is used. 

###Unit Tests
Unit tests are supported by Maven [Surefire Plugin](http://maven.apache.org/surefire/maven-surefire-plugin/). To run Unit Tets use your IDE or by command line:   ``mvn test``

###Integration Tests
To run integration tests the Maven [Failsafe Plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/) is used. Together with invocation of the Failsafe Plugin an embedded JBoss server is launched.

Run with:   ``mvn verify``
	
Debug with [Failsafe Debug](http://maven.apache.org/surefire/maven-failsafe-plugin/examples/debugging.html):   ``mvn -Dmaven.failsafe.debug verify``

##Automated Tests
Tests are triggered automatically on scm commit via a web hook at [drone.io](drone.io) but can be triggered manually as well (see below).

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