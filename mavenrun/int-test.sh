#!/bin/bash
# $Id: int-test.sh,v 1.9 2004/12/21 16:49:54 jdt Exp $ 
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

LOGFILE=/tmp/intTest.log
DATE=`date`

(

# Some reminders
echo Going to check out to ${CHECKOUTHOME?"Value of CHECKOUTHOME (ie where to checkout sources) must be set"}
echo and then deploy docs to ${DOCLOCATION?"Value of DOCLOCATION (ie where to send docs) must be set"}
echo under user ${DOCMACHINE?"Value of DOCMACHINE (e.g. maven@www.astrogrid.org) must be set"}
echo "hit ctrl-c if this isn't what you want"

OLDDIR=$PWD

# Fairly fixed variables
TESTMODULE=astrogrid/integrationTests/auto-integration
BUILDHOME=$CHECKOUTHOME/$TESTMODULE
export BUILDHOME


echo admininstrator email is ${ADMIN_EMAIL:="jdt@roe.ac.uk clq2@star.le.ac.uk"}



# Processing Starts Here
rm $LOGFILE
echo "Integration Test Log $DATE" 
echo "=============================" 

echo "Running Integration Tests"
echo "Have you remembered to edit ~/build.properties with the test version numbers?"
echo "Checking out maven-base and AGINAB from cvs" 
if cvs-checkout.sh $TESTMODULE $1  
then
   echo "OK" 
else
   echo "***FAILURE***" 
    cat $LOGFILE | mail -s "run-int-test: cvs failure" $ADMIN_EMAIL
exit 1
fi
echo "Reinstalling AstroGrid" 
if reinstall-aginab.sh  
then
    echo "OK" 
else
    echo "***FAILURE***" 
    cat $LOGFILE | mail -s "run-int-test: reinstall aginab failure" $ADMIN_EMAIL
exit 1
fi
echo "Bouncing Tomcat" 
bounce-tomcat.sh  
echo "Running Tests" 
if run-and-publish-tests.sh  
then
   echo "OK" 
else
   echo "FAILURE" 
    cat $LOGFILE | mail -s "run-int-test: run-and-publish-tests.sh failure" $ADMIN_EMAIL
fi
bounce-tomcat.sh  
echo "Installing Portal" 
if install-portal.sh  
then
  echo "OK" 
else
  echo "***FAILURE***" 
    cat $LOGFILE | mail -s "run-int-test: install-portal.sh failure" $ADMIN_EMAIL
exit 1
fi



cd $OLDDIR


echo "See http://www.astrogrid.org/maven/docs/HEAD/integrationTests" | mail -s "Integration Tests have completed" $ADMIN_EMAIL

) 2>&1 | tee $LOGFILE

#backup tests and logs
TIMESTAMP=`date +%Y%m%d-%T`
FROM=$DOCLOCATION/integrationTests
TO=$DOCLOCATION/backupReports/integrationTests/$TIMESTAMP
echo "Backing up reports and logs from $FROM to $TO" 
ssh $DOCMACHINE mkdir $TO  
ssh $DOCMACHINE cp -rf $FROM/* $TO  
scp $LOGFILE $DOCMACHINE:$DOCLOCATION/log/integration.log  
#back up those that logs that will be useful..
TOMLOGS=$CATALINA_HOME/logs
cd $TOMLOGS  
tar -cvzf catalina.logs.tar.gz *  
scp catalina.logs.tar.gz $DOCMACHINE:$TO  
scp $LOGFILE $DOCMACHINE:$TO  
rm catalina.logs.tar.gz  