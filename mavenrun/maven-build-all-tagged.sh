#!/bin/bash
TAG_TO_BUILD=$1

echo "Building tag $TAG_TO_BUILD"

BUILD_DIR=/home/maven/build/$TAG_TO_BUILD
SCRIPTHOME=/home/maven/mavenrun



DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`

LOG_FILE=maven-build-all.log

mv $BUILD_DIR/$LOG_FILE $BUILD_DIR/$TIMESTAMP-$LOG_FILE

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE) [$TAG_TO_BUILD]" >> $BUILD_DIR/$LOG_FILE 2>&1
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1

$SCRIPTHOME/maven-build-tagged-new.sh common $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh workflow-objects $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh applications $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh community $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh datacenter $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh jes $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh mySpace $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh portal $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh registry $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh scripting $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh security $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh warehouse $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh workflow $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh ui $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
#Index pages for this release
$SCRIPTHOME/maven-build-maven-site-tagged.sh  $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
#index page for all releases
$SCRIPTHOME/maven-build-new.sh maven-site-releases >> $BUILD_DIR/$LOG_FILE 2>&1

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
