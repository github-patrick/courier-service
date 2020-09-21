#!/bin/sh
set -e

cd git-courier-resource
mvn clean verify -Pe2e-tests