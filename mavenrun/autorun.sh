#!/bin/bash
OLDDIR=$PWD

CHECKOUTHOME=/data/cvsDownloads/itn05
SCRIPTHOME=/home/integration/autobuilds
BUILDHOME=/data/cvsDownloads/itn05/astrogrid/integrationTests/auto-integration

export CVS_RSH=ssh
export CVSROOT=:pserver:anoncvs@cvs.astrogrid.org:/devel

#update from cvs first
cd $CHECKOUTHOME
cvs checkout -P astrogrid

#run maven goals
cd $BUILDHOME
echo $BUILDHOME
maven undeploy-all
maven deploy-all
maven astrogrid-deploy-all
