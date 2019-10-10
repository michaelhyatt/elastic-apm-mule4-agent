#!/bin/bash -fv

# Before running this example, pelase ensure the Mule APM agent is built and installed, as below:
#  mvn install:install-file -Dfile=<path-to-elastic-mule4-apm-agent.jar> -DgroupId=co.elastic.apm -DartifactId=mule4-agent -Dversion=<agent-version> -Dpackaging=jar


# Build component1 and component2 projects
mvn clean package -f mule/component2/pom.xml
rm -rf mule/component2/apps
mkdir mule/component2/apps
cp mule/component2/target/component2-1.0.0-SNAPSHOT-mule-application.jar mule/component2/apps

mvn clean package -f mule/component1/pom.xml
rm -rf mule/component1/apps
mkdir mule/component1/apps
cp mule/component1/target/component1-1.0.0-SNAPSHOT-mule-application.jar mule/component1/apps


# docker build . -t mh/mule4
# docker run --name mymule -P -p 8081:8081 -v $PWD/apps:/opt/mule/apps -v $PWD/logs:/opt/mule/logs -i -t --rm mh/mule4
