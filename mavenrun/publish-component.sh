#!/bin/bash
# $Id: publish-component.sh,v 1.3 2004/12/01 23:33:45 jdt Exp $ 
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


COMPONENT_NAME=$1


# Some reminders
if [ -z "$COMPONENT_NAME" ]; then
	echo "A component name must be specified as the first argument (e.g. common)"
	exit 1
fi

if [ -z "$CHECKOUTHOME" ]; then
	echo "Value of CHECKOUTHOME (ie where to checkout sources) must be set"
	exit 1
fi
if [ -z "$DOCLOCATION" ]; then
	echo "Value of DOCLOCATION (ie where to send docs) must be set"
	exit 1
fi
if [ -z "$DOCMACHINE" ]; then
	echo "Value of DOCMACHINE (e.g. maven@www.astrogrid.org) must be set"
	exit 1
fi


OLDDIR=$PWD

# Fairly fixed variables
MODULE=astrogrid/$COMPONENT_NAME
BUILDHOME=$CHECKOUTHOME/$MODULE

DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`
LOGFILE=$COMPONENT_NAME-build.log
rm $LOGFILE



# Processing Starts Here
echo "Deploying $COMPONENT_NAME, $2 on $DATE" >> $LOGFILE 2>&1
echo "====================================" >> $LOGFILE 2>&1
echo "Have you remembered to edit ~/build.properties with the correct version numbers?"
echo "Checking out maven-base and $COMPONENT_NAME from cvs into $CHECKOUTHOME" >> $LOGFILE 2>&1
if cvs-checkout.sh $MODULE $2 >> $LOGFILE 2>&1
then
	echo "OK" >> $LOGFILE 2>&1
else
        echo "publish-component: cvs failure" >> $LOGFILE 2>&1
fi

cd $BUILDHOME
echo >> $LOGFILE 2>&1
maven astrogrid-echo-versions  >> $LOGFILE 2>&1
echo >> $LOGFILE 2>&1

echo "Building and deploying artifacts" >> $LOGFILE 2>&1
if maven $MY_MAVEN_OPTS astrogrid-deploy-artifact >> $LOGFILE 2>&1
then
    echo "OK" >> $LOGFILE 2>&1
else
    echo "publish-component: build failure" >> $LOGFILE 2>&1
    cd $OLDDIR
fi
scp $LOG_FILE $DOCMACHINE:$DOCLOCATION/log/maven-build-$COMPONENT_NAME.log 
cd $OLDDIR
