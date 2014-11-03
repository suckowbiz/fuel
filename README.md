#Development
##Tests [![Build Status](https://drone.io/bitbucket.org/tsuckow/fuel/status.png)](https://drone.io/bitbucket.org/tsuckow/fuel/latest)
###Unit Tests
Unit tests are supported by [Surefire Plugin](http://maven.apache.org/surefire/maven-surefire-plugin/). To run Unit Tets use IDE or invoke:

    mvn test

###Integration Tests
To run integration tests the [Failsafe Plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/) is used. Together with invokation of the Failsafe Plugin an embedded JBoss Wildfly server is launched.

Run with: 
	
	mvn verify
	
Debug with [Failsafe Debug](http://maven.apache.org/surefire/maven-failsafe-plugin/examples/debugging.html):

    mvn -Dmaven.failsafe.debug verify