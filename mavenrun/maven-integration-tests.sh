BUILD_DIR=/home/maven/build
SCRIPTHOME=/home/maven/mavenrun

DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`

LOG_FILE=maven-integration-tests.log

mv $BUILD_DIR/$LOG_FILE $BUILD_DIR/$TIMESTAMP-$LOG_FILE

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1

$SCRIPTHOME/maven-build.sh integrationTests >> $BUILD_DIR/$LOG_FILE 2>&1

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
