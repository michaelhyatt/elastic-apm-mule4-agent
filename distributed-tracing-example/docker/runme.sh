#!/bin/bash -fv

# Before you start: drop the elastic-apm-agent and apm-agent-attach jars into hte ./lib directory


# Install dependencies from ./lib directory
mvn install:install-file -Dfile=lib/elastic-apm-agent-1.9.1-SNAPSHOT.jar -DgroupId=co.elastic.apm -DartifactId=elastic-apm-agent -Dversion=1.9.1-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=lib/apm-agent-attach-1.9.1-SNAPSHOT.jar -DgroupId=co.elastic.apm -DartifactId=apm-agent-attach -Dversion=1.9.1-SNAPSHOT -Dpackaging=jar


# Build the projects
pushd .
cd projects/mule4-agent
mvn clean install -DskipTests
cd ../dep-test
mvn clean package
popd


# Copy the mule project jar into app directory for docker container to pick it up
rm -rf apps/*
chmod +w apps/ logs/
cp projects/dep-test/target/dep-test-1.0.0-SNAPSHOT-mule-application.jar apps


# Build and run the docker container
# Test it by doing GET to http://localhost:8081, like with `curl http://localhost:8081`
# ^C should exit it
docker build . -t mh/mule4
docker run --name mymule -P -p 8081:8081 -v $PWD/apps:/opt/mule/apps -v $PWD/logs:/opt/mule/logs -i -t --rm mh/mule4

