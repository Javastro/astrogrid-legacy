#!/bin/bash
# $Id: autorun.sh,v 1.21 2004/08/24 10:29:34 clq2 Exp $ 
# Script to run the integration tests/AGINAB
OLDDIR=$PWD

#setup paths etc
source /etc/profile
CHECKOUTHOME=/data/cvsDownloads/itn06
TESTMODULE=astrogrid/integrationTests/auto-integration
SCRIPTHOME=/home/integration/autobuilds
BUILDHOME=$CHECKOUTHOME/$TESTMODULE
LOGFILE=/home/integration/mavenrun/auto.log
DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`
ADMIN_EMAIL="clq2@star.le.ac.uk jdt@roe.ac.uk"
export CVS_RSH=ssh
export CVSROOT=:pserver:anoncvs@cvs.astrogrid.org:/devel


rm $LOGFILE
echo "Integration Test Log $DATE" >> $LOGFILE
echo "=============================" >> $LOGFILE
#Use old style download counter
MY_OPTS=-Dmaven.download.meter=bootstrap

cd $BUILDHOME >> $LOGFILE 2>&1
echo "Undeploying old apps..." >> $LOGFILE
if maven $MY_OPTS undeploy-all >> $LOGFILE 2>&1
then
   echo "*** SUCCESS ***" >> $LOGFILE
else
   echo "*** FAILURE ***" >> $LOGFILE
   cat $LOGFILE | mail -s "undeploy-all Failure in integration tests" $ADMIN_EMAIL 
fi

# Restart tomcat and clean it out
echo "Shutting down Tomcat" >> $LOGFILE
$CATALINA_HOME/bin/shutdown.sh >> $LOGFILE 2>&1
echo "Waiting for tomcat to shutdown...." >> $LOGFILE
sleep 15

#update from cvs 
cd $CHECKOUTHOME >> $LOGFILE 2>&1
rm -r $TESTMODULE >> $LOGFILE 2>&1
cvs checkout -P $TESTMODULE >> $LOGFILE 2>&1

#run maven goals
cd $BUILDHOME >> $LOGFILE 2>&1
echo $BUILDHOME >> $LOGFILE
maven CLEANTOMCAT >> $LOGFILE 2>&1
echo "Starting Tomcat" >> $LOGFILE
$CATALINA_HOME/bin/startup.sh >> $LOGFILE 2>&1

echo "Deploying new apps..." >> $LOGFILE

#delete the olds from working dir
rm -rf /home/integration/working/*

if maven $MY_OPTS deploy-all >> $LOGFILE 2>&1
then
   echo "*** SUCCESS ***" >> $LOGFILE
else
   echo "*** FAILURE ***" >> $LOGFILE
   cat $LOGFILE | mail -s "deploy-all Failure in integration tests" $ADMIN_EMAIL 
fi

#before running tests, backup the previous tests results 
CURRENT=`pwd`
echo $CURRENT
ssh maven@uluru ./mavenrun/backup.sh

echo "Running tests..." >> $LOGFILE

if maven $MY_OPTS astrogrid-deploy-site >> $LOGFILE 2>&1
then
   echo "*** SUCCESS ***" >> $LOGFILE
else
   echo "*** FAILURE ***" >> $LOGFILE
   cat $LOGFILE | mail -s "astrogrid-deploy-site Failure in integration tests" $ADMIN_EMAIL 
fi

scp $LOGFILE maven@www.astrogrid.org:/var/www/www/maven/docs/snapshot/log/integration.log


cd $OLDDIR
