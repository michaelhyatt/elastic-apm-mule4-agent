# Elastic APM agent for Mule 4.x
## important note
Until version 1, the agent is considered work in progress, where some of the features may be broken or missing. Please feel free to download the agent, raise issues, test and contribute to make it better.
### Work in progress
These items are still missing and still need to be developed:
* Distributed tracing with HTTP.
* Generic distributed tracing for non-HTTP protocols.
* Capturing flow variables and properties in the flow.
* Documentation.

## Overview
The agent allows tracking the timing of Mule flow steps executions and captures data, metrics and exceptions raised in Mule flows. is included as Maven dependency and is instantiated in the flow. The data collected by the agent is stored in Elasticsearch and can be visualised in Kibana in a way compatible with the rest of Elastic APM stack. This allows Mule 4 components to be monitored and profiled alongside other technologies in [Elastic APM](https://www.elastic.co/products/apm). The agent is built using [Elastic Java APM agent](https://www.elastic.co/guide/en/apm/agent/java/1.x/index.html) and is compatible with Java APM agent [configuration options](https://www.elastic.co/guide/en/apm/agent/java/1.x/configuration.html).
## Installation
Download the latest jar from the [Releases](https://./releases) and install it using Maven. Make sure to populate the right values for `PATH_TO_JAR` and `AGENT_VERSION`.
```bash
mvn install:install-file -Dfile=$PATH_TO_JAR -DgroupId=co.elastic.apm -DartifactId=apm-mule3-agent -Dversion=$AGENT_VERSION -Dpackaging=jar
```
## Setup in Mule
## Configuration
## Test projects
### dep-test
