#!/bin/bash
OLDDIR=$PWD


# Change to Maven repository directory for project.
cd /var/www/www/maven/org.astrogrid/



## alternative - get rid of all the snapshot type files older than 5 days
find . -regex ".*-200[0-9]+\.[0-9]+\.\(zip\|pom\|war\|jar\)\(\.md5\)?" -ctime +5 -exec rm '{}' ';' -print

cd $OLDDIR
