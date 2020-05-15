#!/bin/sh
set -e

cd git-courier-resource
mvn clean install -Dspring.profiles.active=dev

