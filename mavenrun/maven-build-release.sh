#!/bin/sh
# builds a maven release for a particular project
OLDDIR=$PWD

PROJECT_NAME=$1

DATE=`date`

echo
echo "[ag-build] building $PROJECT_NAME"
echo "[ag-build] build log: maven-build-$PROJECT_NAME.log"
echo

BUILD_HOME=/home/maven/build/release
SCRIPTHOME=/home/maven/mavenrun
PROJECT_HOME=$BUILD_HOME/astrogrid/$PROJECT_NAME
LOG_FILE=$BUILD_HOME/maven-release-$PROJECT_NAME.log
cd $BUILD_HOME
echo "[ag-build-$PROJECT_NAME] remove old log"
rm $LOG_FILE

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

cvs -d $CVSROOT export -r HEAD astrogrid/astrogrid-project-version.properties

TAGNAME=`awk -F= /$PROJECT_NAME/'{print $2}' astrogrid/astrogrid-project-version.properties`

echo "[ag-build-$PROJECT_NAME] cvs checkout for tag $TAGNAME"
cvs -d $CVSROOT export -kv -r $TAGNAME astrogrid/$PROJECT_NAME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] project home: $PROJECT_HOME"
cd $PROJECT_HOME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] generate and deploy site; deploy"
maven java:compile clover:html-report >> $LOG_FILE 2>&1
maven site >> $LOG_FILE 2>&1 
maven site:fsdeploy >> $LOG_FILE 2>&1 
maven jar:deploy >> $LOG_FILE 2>&1
maven war:deploy >> $LOG_FILE 2>&1
echo "[ag-build-$PROJECT_NAME] deploy build log"
cp $LOG_FILE /var/www/www/maven/build/log

echo "[ag-build-$PROJECT_NAME] back to start dir: $OLDDIR"
cd $OLDDIR


echo
