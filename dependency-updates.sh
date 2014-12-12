#!/usr/bin/env bash

echo "Getting information about newer versions of dependencies using in this build.."
mvn versions:display-property-updates
