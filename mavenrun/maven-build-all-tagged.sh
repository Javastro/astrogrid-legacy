TAG_TO_BUILD=$1

BUILD_DIR=/home/maven/build
SCRIPTHOME=/home/maven/mavenrun

DOCLOCATION=$MAVEN_PUBLIC/docs
RELEASEDOCS=$DOCLOCATION/snapshot

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
$SCRIPTHOME/maven-build-tagged-new.sh maven-base $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
#Now the "real" projects.
$SCRIPTHOME/maven-build-tagged.sh common $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh applications $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh community $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged-new.sh datacenter $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh jes $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh mySpace $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh portal $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh registry $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh scripting $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh warehouse $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-tagged.sh workflow $TAG_TO_BUILD >> $BUILD_DIR/$LOG_FILE 2>&1

$SCRIPTHOME/maven-build-new.sh maven-site >> $BUILD_DIR/$LOG_FILE 2>&1

echo "Moving docs to release location" >> $BUILD_DIR/$LOG_FILE 2>&1
cp -r $MAVEN_PUBLIC/build $RELEASEDOCS
echo "Copying redirect pages over old location" >> $BUILD_DIR/$LOG_FILE 2>&1
cp -r $DOCLOCATION/redirect $MAVEN_PUBLIC/build

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
