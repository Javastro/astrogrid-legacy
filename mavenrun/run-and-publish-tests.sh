#!/bin/bash
# $Id: run-and-publish-tests.sh,v 1.14 2004/12/13 00:33:19 jdt Exp $ 
######################################################
# Run the tests and publish the results to Uluru
# (or wherever)
######################################################
#
#function just executes tests, doesn't publish docs
#first argument supplied is pattern for test classes
function testonly {
	TINCS=$1
	echo "Including $TINCS"

	if maven $MY_MAVEN_OPTS -Dastrogrid.inttest.includes=$TINCS\
				-Dastrogrid.docs.root=$DOCLOCATION int-test 
	then
	   echo "OK" 
	else
	   echo "*** FAILURE ***"
	   exit 1 
fi
bounce-tomcat.sh
}


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

testonly org/astrogrid/applications/integration/**/*Test.java
testonly org/astrogrid/community/integration/*Test.java
testonly org/astrogrid/datacenter/integration/**/*Test.java
testonly org/astrogrid/filemanager/integration/*Test.java
testonly org/astrogrid/filestore/integration/*Test.java
testonly org/astrogrid/installation/integration/*Test.java
testonly org/astrogrid/myspace/integration/*Test.java
testonly org/astrogrid/portal/integration/*Test.java
testonly org/astrogrid/registry/integration/*Test.java
testonly org/astrogrid/store/**/integration/**/*Test.java
testonly org/astrogrid/workflow/integration/intwfssiap/**/*Test.java
testonly org/astrogrid/workflow/integration/*Test.java


# Finally, last few tests, and publish
TINCS=org/astrogrid/workflow/integration/itn6/solarevent/*Test.java

echo "Including $TINCS"

if maven $MY_MAVEN_OPTS -Dastrogrid.inttest.includes=$TINCS\
                        -Dastrogrid.docs.root=$DOCLOCATION astrogrid-deploy-site 
then
   echo "OK" 
else
   echo "*** FAILURE ***"
   exit 1 
fi




set +f
cd $OLDDIR
