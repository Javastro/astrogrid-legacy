#!/bin/bash
# $Id: run-and-publish-tests.sh,v 1.4 2004/11/29 18:00:35 jdt Exp $ 
######################################################
# Run the tests and publish the results to Uluru
# (or wherever)
######################################################
#
if [ -z "$BUILDHOME" ]; then
	echo "Value of BUILDHOME (ie where AGINAB is) must be set"
	exit 1
fi
if [ -z "$DOCLOCATION" ]; then
	echo "Value of DOCLOCATION (ie where documents should be published) must be set"
	exit 1
fi
echo "Running tests from $BUILDHOME"
OLDDIR=$PWD
cd $BUILDHOME 
echo "Undeploying old apps..." 
if maven $MY_MAVEN_OPTS -Dastrogrid.docs.root=$DOCLOCATION astrogrid-deploy-site 
then
   echo "*** SUCCESS ***" 
else
   echo "*** FAILURE ***"
   exit 1 
fi
cd $OLDDIR
