#!/bin/bash
OLDDIR=$PWD
PROJECT_NAME=$1
TAGNAME=$2

DATE=`date`

echo
echo "[ag-build] building $PROJECT_NAME"
echo "[ag-build] build log: maven-build-$PROJECT_NAME.log"
echo

BUILD_HOME=/home/maven/build/release
SCRIPTHOME=/home/maven/mavenrun
PROJECT_HOME=$BUILD_HOME/astrogrid/$PROJECT_NAME
LOG_FILE=$BUILD_HOME/maven-build-$PROJECT_NAME.log
cd $BUILD_HOME
echo "[ag-build-$PROJECT_NAME] remove old log"
rm $LOG_FILE

echo "Build Log for $PROJECT_NAME $TAGNAME on $DATE" >> $LOG_FILE 2>&1
echo "====================================" >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] sourcing java"
source $HOME/.bash-config/java >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] sourcing maven"
source $HOME/.bash-config/maven >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] sourcing cvs"
source $HOME/.bash-config/cvs >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] build home: $BUILD_HOME"
cd $BUILD_HOME >> $LOG_FILE 2>&1


echo "[ag-build-$PROJECT_NAME] removing $PROJECT_HOME"
rm -fr $PROJECT_HOME >> $LOG_FILE 2>&1
rm -fr $BUILD_HOME/astrogrid/maven-base >> $LOG_FILE 2>&1

#both snapshot and branch deploy to same area, so safer to
#delete first in case there's a build problem
echo "[ag-build-$PROJECT_NAME] remove old site"
sh $SCRIPTHOME/clean-site.sh $PROJECT_NAME >> $LOG_FILE 2>&1


echo "[ag-build-$PROJECT_NAME] removing astrogrid files from local maven repository"
rm -rf  ~/.maven/repository/astrogrid*
rm -rf  ~/.maven/repository/org.astrogrid

echo "[ag-build-$PROJECT_NAME] cvs checkout for tag $TAGNAME"
#first need to check out the common maven stuff
cvs -d $CVSROOT export -kv -r $TAGNAME astrogrid/maven-base >> $LOG_FILE 2>&1
cvs -d $CVSROOT export -kv -r $TAGNAME astrogrid/$PROJECT_NAME >> $LOG_FILE 2>&1



echo "[ag-build-$PROJECT_NAME] project home: $PROJECT_HOME"
cd $PROJECT_HOME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] generate and deploy site"
echo "Executing astrogrid-deploy-site" >> $LOG_FILE 2>&1 
maven astrogrid-deploy-site >> $LOG_FILE 2>&1 
echo "[ag-build-$PROJECT_NAME] generate and deploy artifact"
echo "Executing astrogrid-deploy-artifact" >> $LOG_FILE 2>&1 
maven astrogrid-deploy-artifact >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] deploy build log"
cp $LOG_FILE /var/www/www/maven/build/log
echo "[ag-build-$PROJECT_NAME] back to start dir: $OLDDIR"
cd $OLDDIR


echo
