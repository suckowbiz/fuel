#!/usr/bin/env bash

URL="target/site/jacoco-it/index.html"
if which xdg-open > /dev/null
then
  xdg-open $URL
elif which gnome-open > /dev/null
then
  gnome-open $URL
fi
