#!/bin/bash

curl --remote-name https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.53/bin/apache-tomcat-9.0.53-windows-x64.zip

find "${PWD}" -type f -iname *.zip | while read file; do unzip ${file} -d ${PWD} && rm ${file} ; done
