#!/bin/bash
# $Id: publish-component.sh,v 1.6 2004/12/11 17:57:47 jdt Exp $ 
####################################################################
# Build a component into the local repository.
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
LOGFILE=/tmp/$COMPONENT_NAME-build.log
rm $LOGFILE

(
COMPONENT_NAME=$1
# Some reminders
echo Checking out ${COMPONENT_NAME?"A component name must be specified as the first argument (e.g. common)"}
echo to ${CHECKOUTHOME?"Value of CHECKOUTHOME (ie where to checkout sources) must be set"}
#echo Documents will be deployed to ${DOCLOCATION?"Value of DOCLOCATION (ie where to send docs on remote machine) must be set"}
#echo Under user ${DOCMACHINE?"Value of DOCMACHINE (e.g. maven@www.astrogrid.org) must be set"
echo Admin email addresses for error logs: ${ADMIN_EMAIL:="jdt@roe.ac.uk clq2@star.le.ac.uk"}

OLDDIR=$PWD

# Fairly fixed variables
MODULE=astrogrid/$COMPONENT_NAME
BUILDHOME=$CHECKOUTHOME/$MODULE

# Processing Starts Here
echo "Deploying $COMPONENT_NAME, $2 on $DATE" 
echo "====================================" 
echo "Have you remembered to edit ~/build.properties with the correct version numbers?"
echo "Checking out maven-base and $COMPONENT_NAME from cvs into $CHECKOUTHOME" 
if cvs-checkout.sh $MODULE $2 
then
	echo "OK" 
else
        echo "publish-component: cvs failure" 
        cat $LOGFILE | mail -s "Publish Component" $LOGFILE
fi

cd $BUILDHOME
echo 
maven astrogrid-echo-versions  
echo 

echo "Building and deploying artifacts" 
if maven $MY_MAVEN_OPTS astrogrid-deploy-artifact 
then
    echo "OK" 
else
    echo "publish-component: build failure" 
    cat $LOGFILE | mail -s "Publish Component" $LOGFILE
    cd $OLDDIR
fi
scp $LOGFILE $DOCMACHINE:$DOCLOCATION/log/maven-build-$COMPONENT_NAME.log 
cd $OLDDIR
) 2>&1 | tee $LOGFILE