#!/bin/bash
BUILD_DIR=/home/maven/build/SNAPSHOT
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
$SCRIPTHOME/maven-build-new.sh scripting >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh registry >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh community >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh mySpace >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh security >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh applications >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh jes >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh warehouse >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh datacenter >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh workflow >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh ui >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-new.sh portal >> $BUILD_DIR/$LOG_FILE 2>&1
#Index pages for this release
$SCRIPTHOME/maven-build-maven-site.sh >> $BUILD_DIR/$LOG_FILE 2>&1
#index page for all releases
$SCRIPTHOME/maven-build-new.sh maven-site-releases >> $BUILD_DIR/$LOG_FILE 2>&1

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
