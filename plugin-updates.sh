#!/usr/bin/env bash

echo "Getting information about newer versions of plugins using in this build.."
mvn versions:display-plugin-updates
