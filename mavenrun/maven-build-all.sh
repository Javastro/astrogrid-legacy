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

$SCRIPTHOME/maven-build.sh applications >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh common >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh community >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh datacenter >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh jes >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh mySpace >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh portal >> $BUILD_DIR/$LOG_FILE 2>&1
#$SCRIPTHOME/maven-build.sh portalB >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh registry >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh templateservice >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh tools >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh ui >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh warehouse >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build.sh workflow >> $BUILD_DIR/$LOG_FILE 2>&1
$SCRIPTHOME/maven-build-maven-site.sh >> $BUILD_DIR/$LOG_FILE 2>&1


echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
