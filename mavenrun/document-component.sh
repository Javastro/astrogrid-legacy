#!/bin/bash
# $Id: document-component.sh,v 1.5 2004/12/13 11:12:09 jdt Exp $ 
####################################################################
# Document a component on our website
# First argument (required) cvs name of component _under_ astrogrid
# Second argument (optional) cvs tag
# Just like build-component.sh except
#    The binaries go to Uluru
#    Logs are produced
####################################################################
# 
# Environment variables that need to be set locally
# CHECKOUTHOME=/data/cvsDownloads/itn06
# MAVEN_OPTS=-Mmx1024m #options for the JVM 
# MY_MAVEN_OPTS = -Dmaven.download.meter=bootstrap #options to Maven itself
# These scripts need to be on your path!

DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`
LOGFILE=/tmp/$COMPONENT_NAME-docs.log
rm $LOGFILE

(
COMPONENT_NAME=$1


# Some reminders
echo Documenting ${COMPONENT_NAME?"A component name must be specified as the first argument (e.g. common)"}
echo Checking out to ${CHECKOUTHOME?"Value of CHECKOUTHOME (ie where to checkout sources) must be set"}
echo Deploying docs to ${DOCLOCATION?"Value of DOCLOCATION (ie where to send docs) must be set"}
echo under user ${DOCMACHINE?"Value of DOCMACHINE (e.g. maven@www.astrogrid.org) must be set"}

OLDDIR=$PWD

# Fairly fixed variables
MODULE=astrogrid/$COMPONENT_NAME
BUILDHOME=$CHECKOUTHOME/$MODULE





# Processing Starts Here
echo "Documenting $COMPONENT_NAME, $2 on $DATE" 
echo "====================================" 
echo "Have you remembered to edit ~/build.properties with the correct version numbers?"
echo "Checking out maven-base and $COMPONENT_NAME from cvs into $CHECKOUTHOME" 
if cvs-checkout.sh $MODULE $2 
then
	echo "OK" 
else
        echo "document-component: cvs failure" 
	exit 1
fi

cd $BUILDHOME
echo 
maven astrogrid-echo-versions 
echo 

echo "Building and deploying artifacts" 
if maven $MY_MAVEN_OPTS astrogrid-deploy-site -Dastrogrid.docs.root=$DOCLOCATION 
then
    echo "OK" 
else
    echo "document-component: site documentation failure" 
    cd $OLDDIR
    exit 1
fi
scp $LOGFILE $DOCMACHINE:$DOCLOCATION/log/maven-build-$COMPONENT_NAME.log
cd $OLDDIR
) 2>&1 | tee $LOGFILE
