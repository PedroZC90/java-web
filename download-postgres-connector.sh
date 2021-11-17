#!/bin/bash

# download postgresql jdbc connector
curl --location \
    --request GET \
    --output $PWD/postgresql-42.3.1.jar \
    'https://jdbc.postgresql.org/download/postgresql-42.3.1.jar'

# move it to tomat lib directory
mv -v $PWD/postgresql-42.3.1.jar ${PWD}/apache-tomcat-*/lib/
