#!/bin/bash
# $Id: autorun.sh,v 1.16 2004/07/04 22:59:14 jdt Exp $ 
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
ADMIN_EMAIL=jdt@roe.ac.uk
export CVS_RSH=ssh
export CVSROOT=:pserver:anoncvs@cvs.astrogrid.org:/devel


rm $LOGFILE
echo "Integration Test Log $DATE" >> $LOGFILE
echo "=============================" >> $LOGFILE

cd $BUILDHOME >> $LOGFILE 2>&1
echo "Undeploying old apps..." >> $LOGFILE
if maven undeploy-all >> $LOGFILE 2>&1
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

echo $BUILDHOME >> $LOGFILE
maven CLEANTOMCAT >> $LOGFILE 2>&1
echo "Starting Tomcat" >> $LOGFILE
$CATALINA_HOME/bin/startup.sh >> $LOGFILE 2>&1

echo "Deploying new apps..." >> $LOGFILE

if maven  deploy-all >> $LOGFILE 2>&1
then
   echo "*** SUCCESS ***" >> $LOGFILE
else
   echo "*** FAILURE ***" >> $LOGFILE
   cat $LOGFILE | mail -s "deploy-all Failure in integration tests" $ADMIN_EMAIL 
fi

echo "Running tests..." >> $LOGFILE

if maven -Dorg.astrogrid.autobuild=true astrogrid-deploy-site >> $LOGFILE 2>&1
then
   echo "*** SUCCESS ***" >> $LOGFILE
else
   echo "*** FAILURE ***" >> $LOGFILE
   cat $LOGFILE | mail -s "astrogrid-deploy-site Failure in integration tests" $ADMIN_EMAIL 
fi

scp $LOGFILE maven@www.astrogrid.org:/var/www/www/maven/docs/snapshot/log/integration.log


cd $OLDDIR
