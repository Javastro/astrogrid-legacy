#!/bin/bash
OLDDIR=$PWD

CHECKOUTHOME=/data/cvsDownloads/itn05
SCRIPTHOME=/home/integration/autobuilds
BUILDHOME=/data/cvsDownloads/itn05/astrogrid/integrationTests/auto-integration
LOGFILE=/home/integration/autobuilds/auto.log

export CVS_RSH=ssh
export CVSROOT=:pserver:anoncvs@cvs.astrogrid.org:/devel

#update from cvs first
cd $CHECKOUTHOME
cvs update -P astrogrid

#run maven goals
cd $BUILDHOME
echo $BUILDHOME
maven undeploy-all >> $LOGFILE
maven deploy-all >> $LOGFILE
maven astrogrid-deploy-all >> $LOGFILE
