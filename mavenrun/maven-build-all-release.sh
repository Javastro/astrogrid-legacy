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

#Check out the maven project first, since this may act as a base
#from which other projects may inherit
#Need to think about this wrt to the current branches discussion
$SCRIPTHOME/cvs-checkout.sh maven-base >> $BUILD_DIR/$LOG_FILE 2>&1
#Now the "real" projects.
$SCRIPTHOME/maven-build-release.sh common >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh applications >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh community >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh datacenter >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh jes >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh mySpace >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh portal >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh registry >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh scripting >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh warehouse >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-release.sh workflow >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-maven-site.sh >> $BUILD_DIR/$LOG_FILE 2>&1


echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
