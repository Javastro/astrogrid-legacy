#!/bin/sh
BUILD_DIR=/home/maven/build
SCRIPTHOME=/home/maven/mavenrun

DOCLOCATION=$MAVEN_PUBLIC/docs
SNAPSHOTDOCS=$DOCLOCATION/snapshot

DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`

LOG_FILE=maven-build-all.log

mv $BUILD_DIR/$LOG_FILE $BUILD_DIR/$TIMESTAMP-$LOG_FILE

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1

#Check out the maven project first, since this may act as a base
#from which other projects may inherit
$SCRIPTHOME/maven-build-new.sh maven-base >> $BUILD_DIR/$LOG_FILE 2>&1
#Now the "real" projects.
$SCRIPTHOME/maven-build-new.sh common >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh workflow-objects >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh applications >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh community >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh datacenter >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh jes >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh mySpace >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh portal >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh registry >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh scripting >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh warehouse >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh workflow >> $BUILD_DIR/$LOG_FILE 2>&1

$SCRIPTHOME/maven-build-new.sh maven-site >> $BUILD_DIR/$LOG_FILE 2>&1

echo "Moving docs to snapshot location" >> $BUILD_DIR/$LOG_FILE 2>&1
cp -r $MAVEN_PUBLIC/build/* $SNAPSHOTDOCS >> $BUILD_DIR/$LOG_FILE 2>&1
echo "Copying redirect pages over old location" >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh maven-site-releases >> $BUILD_DIR/$LOG_FILE 2>&1

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
