#!/bin/bash
OLDDIR=$PWD

PROJECT_NAME=maven-site

echo "[ag-build] building $PROJECT_NAME"

BUILD_HOME=/home/maven/build
LOG_FILE=/home/maven/build/maven-build-$PROJECT_NAME.log
PROJECT_HOME=$BUILD_HOME/astrogrid/$PROJECT_NAME

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

echo "[ag-build-$PROJECT_NAME] cvs checkout"
cvs -d $CVSROOT co astrogrid/$PROJECT_NAME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] project home: $PROJECT_HOME"
cd $PROJECT_HOME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] generate and deploy site"
maven site:generate site:fsdeploy >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] back to start dir: $OLDDIR"
cd $OLDDIR

