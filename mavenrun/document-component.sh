#!/bin/bash
# $Id: document-component.sh,v 1.3 2004/12/05 23:35:23 jdt Exp $ 
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

DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`
LOGFILE=/tmp/$COMPONENT_NAME-docs.log
rm $LOGFILE



# Processing Starts Here
echo "Documenting $COMPONENT_NAME, $2 on $DATE" >> $LOGFILE 2>&1
echo "====================================" 
echo "Have you remembered to edit ~/build.properties with the correct version numbers?"
echo "Checking out maven-base and $COMPONENT_NAME from cvs into $CHECKOUTHOME" >> $LOGFILE 2>&1
if cvs-checkout.sh $MODULE $2 >> $LOGFILE 2>&1
then
	echo "OK" >> $LOGFILE 2>&1
else
        echo "document-component: cvs failure" >> $LOGFILE 2>&1
	exit 1
fi

cd $BUILDHOME
echo >> $LOGFILE 2>&1
maven astrogrid-echo-versions >> $LOGFILE 2>&1
echo >> $LOGFILE 2>&1

echo "Building and deploying artifacts" >> $LOGFILE 2>&1
if maven $MY_MAVEN_OPTS astrogrid-deploy-site >> $LOGFILE 2>&1
then
    echo "OK" >> $LOGFILE 2>&1
else
    echo "document-component: site documentation failure" >> $LOGFILE 2>&1
    cd $OLDDIR
    exit 1
fi
scp $LOGFILE $DOCMACHINE:$DOCLOCATION/log/maven-build-$COMPONENT_NAME.log
cd $OLDDIR
