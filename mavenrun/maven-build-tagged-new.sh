#!/bin/bash
OLDDIR=$PWD
PROJECT_NAME=$1
TAGNAME=$2

DATE=`date`
BUILD_HOME=/home/maven/build/$TAGNAME
SCRIPTHOME=/home/maven/mavenrun
PROJECT_HOME=$BUILD_HOME/astrogrid/$PROJECT_NAME
DOC_HOME=/var/www/www/maven/docs
ASTROGRID_VERSION=$TAGNAME
LOG_FILE=$BUILD_HOME/maven-build-$PROJECT_NAME.log
ADMIN_EMAIL=jdt@roe.ac.uk

echo
echo "[ag-build] building $PROJECT_NAME $ASTROGRID_VERSION"
echo "[ag-build] build log: maven-build-$PROJECT_NAME.log"
echo





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
rm -fr $BUILD_HOME/astrogrid/maven-base >> $LOG_FILE 2>&1
rm -fr $PROJECT_HOME >> $LOG_FILE 2>&1



echo "[ag-build-$PROJECT_NAME] removing astrogrid files from local maven repository"
rm -rf  ~/.maven/repository/astrogrid*
rm -rf  ~/.maven/repository/org.astrogrid

echo "[ag-build-$PROJECT_NAME] cvs checkout for tag $TAGNAME"
#first need to check out the common maven stuff
cvs -d $CVSROOT export -kv -r $TAGNAME astrogrid/maven-base >> $LOG_FILE 2>&1
cvs -d $CVSROOT export -kv -r $TAGNAME astrogrid/$PROJECT_NAME >> $LOG_FILE 2>&1



echo "[ag-build-$PROJECT_NAME] project home: $PROJECT_HOME"
cd $PROJECT_HOME >> $LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] generate and deploy $TAGNAME"
echo "Executing astrogrid-deploy-artifact" >> $LOG_FILE 2>&1 
#Note that unit tests are (not) skipped at this stage, since they have already
#been run for the site docs
if maven -Dmaven.test.skip=false -Dastrogrid.iteration=$ASTROGRID_VERSION -Dmaven.site.central.directory=$DOC_HOME astrogrid-deploy-artifact >> $LOG_FILE 2>&1
then
   echo "*** SUCCESS ***" >> $LOG_FILE
else
   echo "*** FAILURE ***" >> $LOG_FILE
   cat $LOG_FILE | mail -s "astrogrid-deploy-artifact Failure for $PROJECT_NAME, $ASTROGRID_VERSION" $ADMIN_EMAIL  
fi

echo "[ag-build-$PROJECT_NAME] generate and deploy site"
echo "Executing astrogrid-deploy-site" >> $LOG_FILE 2>&1 
if maven -Dastrogrid.iteration=$ASTROGRID_VERSION -Dmaven.site.central.directory=$DOC_HOME astrogrid-deploy-site >> $LOG_FILE 2>&1 
then
   echo "*** SUCCESS ***\n" >> $LOG_FILE
else
   echo "*** FAILURE ***\n" >> $LOG_FILE
   cat $LOG_FILE | mail -s "astrogrid-deploy-site Failure for $PROJECT_NAME, $ASTROGRID_VERSION" $ADMIN_EMAIL 
fi


echo `date` "[ag-build-$PROJECT_NAME] deploy build log"

scp $LOG_FILE maven@uluru.star.le.ac.uk:/home/maven/maven/docs/Itn06_case3-SNAPSHOT/log

echo `date` "[ag-build-$PROJECT_NAME] back to start dir: $OLDDIR"
cd $OLDDIR



echo
