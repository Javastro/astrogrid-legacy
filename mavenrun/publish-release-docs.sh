#!/bin/bash
#$id$
########################################################
# Script to publish the root Maven docs, that is,
# the overall docs for a release.
# Should probably be run by the cron.
# If an argument is supplied it is used as a tag for cvs
########################################################
# 
# Environment variables that need to be set locally
# CHECKOUTHOME=/data/cvsDownloads/itn06
# MAVEN_OPTS=-Mmx1024m #options for the JVM 
# MY_MAVEN_OPTS = -Dmaven.download.meter=bootstrap #options to Maven itself
# DOCLOCATION = /var/www/www/maven/docs/HEAD
# DOCMACHINE = maven@www.astrogrid.org
# These scripts need to be on your path!

echo Checking out to ${CHECKOUTHOME?"Value of CHECKOUTHOME (ie where to checkout sources) must be set"}

OLDDIR=$PWD

TAG=$1

echo Going to deploy docs for this release ${TAG:=HEAD} to ${DOCLOCATION?"Value of DOCLOCATION (ie where documents should be published) must be set"} - hit ctrl-c if this isn't what you want

# Fairly unvariable variables
BUILDHOME=$CHECKOUTHOME/astrogrid
LOGFILE=/tmp/releasedocs.log
DATE=`date`
#${ADMIN_EMAIL:="jdt@roe.ac.uk clq2@star.le.ac.uk"}

# Processing Starts Here
rm $LOGFILE

echo "Release $TAG Docs Log $DATE" >> $LOGFILE
echo "=============================" >> $LOGFILE

# Checkout a rather precise set of files since we don't want to get the whole damn tree:
#@TODO does this overwrite?  If not, think about delete
if [ -d $CHECKOUTHOME ]; then
   echo
else
   echo "Creating $CHECKOUTHOME"
   mkdir $CHECKOUTHOME
fi

cd $CHECKOUTHOME

cvs-checkout-clean.sh astrogrid/maven-base $TAG  >> $LOGFILE
cvs-checkout-clean.sh astrogrid/xdocs $TAG  >> $LOGFILE
cvs-checkout-clean.sh astrogrid/project.xml $TAG  >> $LOGFILE
cvs-checkout-clean.sh astrogrid/project.properties $TAG  >> $LOGFILE
cvs-checkout-clean.sh astrogrid/maven.xml $TAG  >> $LOGFILE


cd $BUILDHOME

if maven $MY_MAVEN_OPTS -Dastrogrid.docs.root=$DOCLOCATION astrogrid-deploy-site >> $LOGFILE 
then
   echo "OK" >> $LOGFILE
else
   echo "*** FAILURE ***" >> $LOGFILE 2>&1
   cd $OLDDIR
   exit 1 
fi
cd $OLDDIR
