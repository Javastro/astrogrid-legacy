#!/bin/bash
# $Id: run-and-publish-tests.sh,v 1.11 2004/12/03 10:49:20 jdt Exp $ 
######################################################
# Run the tests and publish the results to Uluru
# (or wherever)
######################################################
#
echo ${BUILDHOME?"Value of BUILDHOME (ie where AGINAB is) must be set"}
echo ${DOCLOCATION?"Value of DOCLOCATION (ie where documents should be published) must be set"}

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
