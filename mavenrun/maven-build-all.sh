#!/bin/sh
BUILD_DIR=/home/maven/build
SCRIPTHOME=/home/maven/mavenrun

DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`

LOG_FILE=maven-build-all.log

mv $BUILD_DIR/$LOG_FILE $BUILD_DIR/$TIMESTAMP-$LOG_FILE

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1

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

MAVEN_PUBLIC=/var/www/www/maven
DOCLOCATION=$MAVEN_PUBLIC/docs
SNAPSHOTDOCS=$DOCLOCATION/snapshot
echo "Moving docs to snapshot location" >> $BUILD_DIR/$LOG_FILE 2>&1
#rm -r $SNAPSHOTDOCS >> $BUILD_DIR/$LOG_FILE 2>&1
cp -r $MAVEN_PUBLIC/build/* $SNAPSHOTDOCS >> $BUILD_DIR/$LOG_FILE 2>&1
echo "Copying redirect pages over old location" >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh maven-site-releases >> $BUILD_DIR/$LOG_FILE 2>&1

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
