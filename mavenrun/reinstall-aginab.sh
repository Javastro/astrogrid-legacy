#!/bin/bash
# $Id: reinstall-aginab.sh,v 1.4 2004/11/29 18:00:35 jdt Exp $ 
######################################################
# Script to reinstall AGINAB, assumes TOMCAT is running
######################################################
#
if [ -z "$BUILDHOME" ]; then
	echo "Value of BUILDHOME (ie where AGINAB is) must be set"
	exit 1
fi
echo "Reinstalling AGINAB from $BUILDHOME"
OLDDIR=$PWD
cd $BUILDHOME 
echo "Undeploying old apps..." 
if maven $MY_MAVEN_OPTS undeploy-all 
then
   echo "*** SUCCESS ***" 
else
   echo "*** FAILURE ***" 
fi

echo "Cleaning out Tomcat..."
maven $MY_MAVEN_OPTS CLEANTOMCAT

#While we have memory problems restrict ourselves to reinstalling all bar the portal
echo "Deploying all AGINAB components *except portal*"
if maven $MY_MAVEN_OPTS deploy-all-except-portal 
then
   echo "*** SUCCESS ***" 
else
   echo "*** FAILURE ***" 
   cd $OLDDIR
   exit 1
fi
cd $OLDDIR
