#!/bin/bash
# $Id: reinstall-aginab.sh,v 1.2 2004/11/28 21:27:04 jdt Exp $ 
######################################################
# Script to reinstall AGINAB, assumes TOMCAT is running
######################################################
#
if [-z "$BUILDHOME"]; then
	echo "Value of BUILDHOME (ie where AGINAB is) must be set"
	exit 1
fi
echo "Reinstalling AGINAB from $BUILDHOME"
OLDDIR=$PWD
cd $BUILDHOME 
echo "Undeploying old apps..." 
if maven $MY_OPTS undeploy-all 
then
   echo "*** SUCCESS ***" 
else
   echo "*** FAILURE ***" 
fi

echo "Cleaning out Tomcat..."
maven $MY_OPTS CLEANTOMCAT

#While we have memory problems restrict ourselves to reinstalling all bar the portal
echo "Deploying all AGINAB components *except portal*"
if maven $MY_OPTS deploy-all-except-portal 
then
   echo "*** SUCCESS ***" 
else
   echo "*** FAILURE ***" 
   cd $OLDDIR
   exit 1
fi
cd $OLDDIR
