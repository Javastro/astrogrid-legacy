#!/bin/bash
# $Id: install-portal.sh,v 1.3 2004/11/28 21:36:57 jdt Exp $ 
######################################################
# Script to install the portal only,
# Temporary measure while we're having memory problems
# assumes TOMCAT is running, and the portal isn't
# already installed
######################################################
#
if [ -z "$BUILDHOME" ]; then
	echo "Value of BUILDHOME (ie where AGINAB is) must be set"
	exit 1
fi
echo "Installing Portal from $BUILDHOME"
OLDDIR=$PWD
cd $BUILDHOME 

echo "Cleaning out Tomcat..."
maven $MY_OPTS CLEANTOMCAT

echo "Deploying the portal"
if maven $MY_OPTS init portal-deploy 
then
   echo "*** SUCCESS ***" 
else
   echo "*** FAILURE ***" 
   cd $OLDDIR
   exit 1
fi

cd $OLDDIR

