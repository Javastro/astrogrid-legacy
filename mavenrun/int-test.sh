#!/bin/bash
# $Id: int-test.sh,v 1.4 2004/12/02 21:41:10 jdt Exp $ 
########################################################
# Script to install AGINAB, run the integration
# tests and publish the results
# If an argument is supplied it is used as a tag for cvs
########################################################
# 
# Environment variables that need to be set locally
# CHECKOUTHOME=/data/cvsDownloads/itn06
# MAVEN_OPTS=-Mmx1024m #options for the JVM (optional)
# MY_MAVEN_OPTS = -Dmaven.download.meter=bootstrap #options to Maven itself (optional)
# CATALINA_HOME
# DOCLOCATION = /var/www/www/maven/docs/HEAD
# DOCMACHINE = maven@www.astrogrid.org
# ADMIN_EMAIL (optional, defaults to clq and jdt)
# These scripts need to be on your path!

# Some reminders
${CHECKOUTHOME?"Value of CHECKOUTHOME (ie where to checkout sources) must be set"}
${DOCLOCATION?"Value of DOCLOCATION (ie where to send docs) must be set"}
${DOCMACHINE?"Value of DOCMACHINE (e.g. maven@www.astrogrid.org) must be set"}

OLDDIR=$PWD

echo "Going to deploy docs to $DOCLOCATION on $DOCMACHINE - hit ctrl-c if this isn't what you want"

# Fairly fixed variables
TESTMODULE=astrogrid/integrationTests/auto-integration
BUILDHOME=$CHECKOUTHOME/$TESTMODULE
export BUILDHOME
LOGFILE=/tmp/intTest.log
DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`
ADMIN_EMAIL=${ADMIN_EMAIL:-"jdt@roe.ac.uk clq2@star.le.ac.uk"}
TOMLOGS=$CATALINA_HOME/logs


# Processing Starts Here
rm $LOGFILE
echo "Integration Test Log $DATE" >> $LOGFILE
echo "=============================" >> $LOGFILE

echo "Running Integration Tests"
echo "Have you remembered to edit ~/build.properties with the test version numbers?"
echo "Checking out maven-base and AGINAB from cvs" >> $LOGFILE
if cvs-checkout.sh $TESTMODULE $1 >> $LOGFILE 2>&1
then
   echo "OK" >> $LOGFILE
else
   echo "***FAILURE***" >> $LOGFILE
    cat $LOGFILE | mail -s "run-int-test: cvs failure" $ADMIN_EMAIL
exit 1
fi
echo "Reinstalling AstroGrid" >> $LOGFILE
if reinstall-aginab.sh >> $LOGFILE 2>&1
then
    echo "OK" >> $LOGFILE
else
    echo "***FAILURE***" >> $LOGFILE
    cat $LOGFILE | mail -s "run-int-test: reinstall aginab failure" $ADMIN_EMAIL
exit 1
fi
echo "Bouncing Tomcat" >> $LOGFILE
bounce-tomcat.sh >> $LOGFILE 2>&1
echo "Running Tests" >> $LOGFILE
if run-and-publish-tests.sh >> $LOGFILE 2>&1
then
   echo "OK" >> $LOGFILE
else
   echo "FAILURE" >> $LOGFILE
    cat $LOGFILE | mail -s "run-int-test: run-and-publish-tests.sh failure" $ADMIN_EMAIL
fi
bounce-tomcat.sh >> $LOGFILE 2>&1
echo "Installing Portal" >> $LOGFILE
if install-portal.sh >> $LOGFILE 2>&1
then
  echo "OK" >> $LOGFILE
else
  echo "***FAILURE***" >> $LOGFILE
    cat $LOGFILE | mail -s "run-int-test: install-portal.sh failure" $ADMIN_EMAIL
exit 1
fi

#backup tests and logs
FROM=$DOCLOCATION/integrationTests
TO=$DOCLOCATION/backupReports/integrationTests/$TIMESTAMP
echo "Backing up reports and logs from $FROM to $TO" >> $LOGFILE
ssh $DOCMACHINE mkdir $TO >> $LOGFILE 2>&1
ssh $DOCMACHINE cp -rf $FROM/* $TO >> $LOGFILE 2>&1
scp $LOGFILE $DOCMACHINE:$DOCLOCATION/log/integration.log >> $LOGFILE 2>&1
#back up those that logs that will be useful..
cd $TOMLOGS >> $LOGFILE 2>&1
tar -cvzf catalina.logs.tar.gz * >> $LOGFILE 2>&1
scp catalina.logs.tar.gz $DOCMACHINE:$TO >> $LOGFILE 2>&1
scp $LOGFILE $DOCMACHINE:$TO >> $LOGFILE 2>&1
rm catalina.logs.tar.gz >> $LOGFILE 2>&1

cd $OLDDIR


echo "See http://www.astrogrid.org/maven/docs/HEAD/integrationTests" | mail -s "Integration Tests have completed" $ADMIN_EMAIL