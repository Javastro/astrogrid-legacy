#!/bin/bash
OLDDIR=$PWD

#setup paths etc
source /etc/profile
CHECKOUTHOME=/data/cvsDownloads/itn05
SCRIPTHOME=/home/integration/autobuilds
BUILDHOME=/data/cvsDownloads/itn05/astrogrid/integrationTests/auto-integration
LOGFILE=/home/integration/autobuilds/auto.log


export CVS_RSH=ssh
export CVSROOT=:pserver:anoncvs@cvs.astrogrid.org:/devel
# Restart tomcat
$CATALINA_HOME/bin/shutdown.sh >> $LOGFILE 2>&1
echo "Waiting for tomcat to shutdown...." >> $LOGFILE
sleep 15
$CATALINA_HOME/bin/startup.sh >> $LOGFILE 2>&1

#update from cvs 
cd $CHECKOUTHOME/astrogrid/integrationTests >> $LOGFILE 2>&1
cvs update -PCd  >> $LOGFILE 2>&1

#run maven goals
cd $BUILDHOME >> $LOGFILE 2>&1
echo $BUILDHOME >> $LOGFILE
maven undeploy-all >> $LOGFILE 2>&1
maven deploy-all >> $LOGFILE 2>&1
maven astrogrid-deploy-site >> $LOGFILE 2>&1
cd $OLDDIR
