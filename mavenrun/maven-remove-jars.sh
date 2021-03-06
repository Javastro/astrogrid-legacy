#!/bin/bash
OLDDIR=$PWD


# Change to Maven repository directory for project.
cd /var/www/www/maven/org.astrogrid/



## alternative - get rid of all the snapshot type files older than 14 days
find . -regex ".*-200[0-9]+\.[0-9]+\(-src\)?\.\(tar\.gz\|zip\|pom\|war\|jar\)\(\.md5\)?" -ctime +14 -exec rm '{}' ';' -print

cd $OLDDIR
