#!/bin/bash
# $Id: install-portal.sh,v 1.4 2004/12/01 09:59:35 jdt Exp $ 
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

echo "Deploying the portal"
if maven $MY_MAVEN_OPTS init portal-deploy 
then
   echo "*** SUCCESS ***" 
else
   echo "*** FAILURE ***" 
   cd $OLDDIR
   exit 1
fi

cd $OLDDIR

