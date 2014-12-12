#!/usr/bin/env bash

URL="target/jacoco/index.html"
if which xdg-open > /dev/null
then
  xdg-open $URL
elif which gnome-open > /dev/null
then
  gnome-open $URL
fi
