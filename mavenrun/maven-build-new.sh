#!/bin/bash
OLDDIR=$PWD

PROJECT_NAME=$1

DATE=`date`
BUILD_HOME=/home/maven/build
SCRIPTHOME=/home/maven/mavenrun
PROJECT_HOME=$BUILD_HOME/astrogrid/$PROJECT_NAME
LOG_FILE=$BUILD_HOME/maven-build-$PROJECT_NAME.log
ADMIN_EMAIL=jdt@roe.ac.uk

echo
echo "[ag-build] building $PROJECT_NAME"
echo "[ag-build] build log: maven-build-$PROJECT_NAME.log"
echo





cd $BUILD_HOME 
echo "[ag-build-$PROJECT_NAME] remove old log"
rm $LOG_FILE

echo "Build Log for $PROJECT_NAME SNAPSHOT on $DATE" >> $LOG_FILE 2>&1
echo "====================================" >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] sourcing java"
source $HOME/.bash-config/java >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] sourcing maven"
source $HOME/.bash-config/maven >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] sourcing cvs"
source $HOME/.bash-config/cvs >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] build home: $BUILD_HOME"
cd $BUILD_HOME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] remove old SNAPSHOTS"
sh $SCRIPTHOME/maven-remove-jars.sh $PROJECT_NAME >> $LOG_FILE 2>&1

#both snapshot and branch deploy to same area, so safer to
#delete first in case there's a build problem
echo "[ag-build-$PROJECT_NAME] remove old site"
sh $SCRIPTHOME/clean-site.sh $PROJECT_NAME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] removing $PROJECT_HOME"
rm -fr $BUILD_HOME/astrogrid/maven-base >> $LOG_FILE 2>&1
rm -fr $PROJECT_HOME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] cvs checkout"
cvs -d $CVSROOT co -A astrogrid/maven-base >> $LOG_FILE 2>&1
cvs -d $CVSROOT co -A astrogrid/$PROJECT_NAME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] project home: $PROJECT_HOME"
cd $PROJECT_HOME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] generate and deploy site" 
echo "Executing astrogrid-deploy-site" >> $LOG_FILE 2>&1 
if maven -Dastrogrid.iteration=snapshot -Dmaven.site.central.directory="/var/www/www/maven/docs" astrogrid-deploy-site >> $LOG_FILE 2>&1 
then
   echo "*** SUCCESS ***\n" >> $LOG_FILE
else
   echo "*** FAILURE ***\n" >> $LOG_FILE
   cat $LOG_FILE | mail -s "astrogrid-deploy-site Failure for $PROJECT_NAME" $ADMIN_EMAIL 
fi


echo "[ag-build-$PROJECT_NAME] generate and deploy SNAPSHOT"
echo "Executing astrogrid-deploy-snapshot" >> $LOG_FILE 2>&1 
if maven -Dastrogrid.iteration=snapshot -Dmaven.site.central.directory="/var/www/www/maven/docs" astrogrid-deploy-snapshot >> $LOG_FILE 2>&1
then
   echo "*** SUCCESS ***" >> $LOG_FILE
else
   echo "*** FAILURE ***" >> $LOG_FILE
   cat $LOG_FILE | mail -s "astrogrid-deploy-snapshot Failure for $PROJECT_NAME" $ADMIN_EMAIL  
fi

echo "[ag-build-$PROJECT_NAME] deploy build log"
cp $LOG_FILE /var/www/www/maven/build/log

echo "[ag-build-$PROJECT_NAME] back to start dir: $OLDDIR"
cd $OLDDIR


echo
