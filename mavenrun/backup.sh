#!/bin/bash
#scrip for copy test dirs, put this on uluru under /home/maven/mavenrun/ - correspond to autorun.sh run on twmbarlwm, backup the reports before wipe them out, so that we can compare. -CLQ 19 Aug 2004
OLDDIR=$PWD

DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`

FROM=/var/www/www/maven/docs/SNAPSHOT/integrationTests
TO=/var/www/www/maven/docs/SNAPSHOT/backupReports/integrationTests/$TIMESTAMP

mkdir $TO

cp -rf $FROM/* $TO


