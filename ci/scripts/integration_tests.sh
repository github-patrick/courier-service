#!/bin/sh
set -e

cd git-courier-resource
mvn clean verify -Pintegration-tests