#!/bin/bash
# $Id: run-and-publish-tests.sh,v 1.13 2004/12/11 17:57:47 jdt Exp $ 
######################################################
# Run the tests and publish the results to Uluru
# (or wherever)
######################################################
#
echo Going to run the tests in ${CHECKOUTHOME?"Value of CHECKOUTHOME (ie where sources are checked out to e.g. /home/integration/workspace) must be set"}
echo DOCLOCATION is ${DOCLOCATION?"Value of DOCLOCATION (ie where documents should be published) must be set"}

TESTMODULE=astrogrid/integrationTests/auto-integration
BUILDHOME=$CHECKOUTHOME/$TESTMODULE

echo "Running tests from $BUILDHOME"
OLDDIR=$PWD
cd $BUILDHOME 
#Switch off filename globbing
set -f

#
#  Because we're running out of memory on Tomcat with all these tests, we're going to split them
#  up.

# Start with non-workflow, non-portal
TINCS=**/integration/**/*Test.java
TEXCS=**/workflow/integration/**/*Test.java
TEXCS1=**/portal/integration/**/*Test.java
echo "Including $TINCS"
echo "Excluding $TEXCS, $TEXCS1"

if maven $MY_MAVEN_OPTS -Dastrogrid.inttest.includes=$TINCS\
                        -Dastrogrid.inttest.excludes=$TEXCS\
                        -Dastrogrid.inttest.excludes1=$TEXCS1\
                        -Dastrogrid.docs.root=$DOCLOCATION int-test 
then
   echo "OK" 
else
   echo "*** FAILURE ***"
   exit 1 
fi

bounce-tomcat.sh

# Start now workflow only
TINCS=**/workflow/integration/**/*Test.java
unset TEXCS
unset TEXCS1
echo "Including $TINCS"
echo "Excluding $TEXCS, $TEXCS1"

if maven $MY_MAVEN_OPTS -Dastrogrid.inttest.includes=$TINCS\
                        -Dastrogrid.inttest.excludes=$TEXCS\
                        -Dastrogrid.inttest.excludes1=$TEXCS1\
                        -Dastrogrid.docs.root=$DOCLOCATION astrogrid-deploy-site 
then
   echo "OK" 
else
   echo "*** FAILURE ***"
   exit 1 
fi




set +f
cd $OLDDIR
