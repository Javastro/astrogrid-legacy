#!/bin/bash
# $Id: reinstall-aginab.sh,v 1.6 2004/12/21 10:42:28 jdt Exp $ 
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
   echo "OK" 
else
   echo "*** FAILURE ***" 
fi

#Shutting down Tomcat helps, especially on windows
$CATALINA_HOME/bin/shutdown.sh
sleep 60
echo "Cleaning out Tomcat..."
maven $MY_MAVEN_OPTS CLEANTOMCAT
$CATALINA_HOME/bin/startup.sh
sleep 10

#While we have memory problems restrict ourselves to reinstalling all bar the portal
echo "Deploying all AGINAB components *except portal*"
if maven $MY_MAVEN_OPTS deploy-all-except-portal 
then
   echo "OK" 
else
   echo "*** FAILURE ***" 
   cd $OLDDIR
   exit 1
fi
cd $OLDDIR
