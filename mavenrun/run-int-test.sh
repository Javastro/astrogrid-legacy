#!/bin/bash
# $Id: run-int-test.sh,v 1.1 2004/11/28 21:22:03 jdt Exp $ 
######################################################
# Script to install AGINAB, run the integration
# tests and publish the results
# If an argument is supplied it is used as a tag for cvs
######################################################
# 
# Environment variables that need to be set locally
# CHECKOUTHOME=/data/cvsDownloads/itn06
# MAVEN_OPTS=-Mmx1024m #options for the JVM 
# MY_MAVEN_OPTS = -Dmaven.download.meter=bootstrap #options to Maven itself
# SCRIPTHOME=/home/integration/autobuilds #location of scripts on this machine
# CATALINA_HOME
# DOCLOCATION = /var/www/www/maven/docs/HEAD
# DOCMACHINE = maven@www.astrogrid.org

# Some reminders
if [-z "$SCRIPTHOME"]; then
   echo "Value of SCRIPTHOME (ie location of this script) must be set"
   exit 1
fi
if [-z "$CHECKOUTHOME"]; then
	echo "Value of CHECKOUTHOME (ie where to checkout sources) must be set"
	exit 1
fi

OLDDIR=$PWD

# Variables for which we can supply defaults
DOCMACHINE=${DOCMACHINE:-maven@www.astrogrid.org\}
DOCLOCATION=${DOCLOCATION:-/var/www/www/maven/docs/HEAD\}

echo "Going to deploy docs to $DOCLOCATION on $DOCMACHINE - hit ctrl-c if this isn't what you want"

# Fairly fixed variables
TESTMODULE=astrogrid/integrationTests/auto-integration
BUILDHOME=$CHECKOUTHOME/$TESTMODULE
LOGFILE=$SCRIPTHOME/intTest.log
DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`
ADMIN_EMAIL="jdt@roe.ac.uk clq2@star.le.ac.uk"
TOMLOGS=$CATALINA_HOME/logs
export CVS_RSH=ssh
export CVSROOT=:pserver:anoncvs@cvs.astrogrid.org:/devel


# Processing Starts Here
rm $LOGFILE
echo "Integration Test Log $DATE" >> $LOGFILE
echo "=============================" >> $LOGFILE


if $SCRIPTHOME/cvs-checkout.sh $TESTMODULE $1 >> $LOGFILE 2>&1
then
else
    cat $LOGFILE | mail -s "run-int-test: cvs failure" $ADMIN_EMAIL
	exit 1
fi

if $SCRIPTHOME/reinstall-aginab.sh >> $LOGFILE 2>&1
then
else
    cat $LOGFILE | mail -s "run-int-test: reinstall aginab failure" $ADMIN_EMAIL
	exit 1
fi

$SCRIPTHOME/bounce-tomcat.sh >> $LOGFILE 2>&1

if $SCRIPTHOME/run-and-publish-tests.sh >> $LOGFILE 2>&1
then
else
    cat $LOGFILE | mail -s "run-int-test: run-and-publish-tests.sh failure" $ADMIN_EMAIL
fi

if $SCRIPTHOME/install-portal.sh >> $LOGFILE 2>&1
then
else
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

