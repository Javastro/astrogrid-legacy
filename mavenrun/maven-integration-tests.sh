BUILD_DIR=/home/maven/build
SCRIPTHOME=/home/maven/mavenrun
TMP=/tmp
OLDDIR=$PWD
PROJECT_NAME=integrationTests
LOG_FILE=maven-build-integrationTests.log
TOMCAT_SERVERS=tomcatServers.xml

DATE=`date`
TIMESTAMP=`date +%Y%m%d-%T`



mv $BUILD_DIR/$LOG_FILE $BUILD_DIR/$TIMESTAMP-$LOG_FILE

echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1


echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "[ag-build] building $PROJECT_NAME" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "[ag-build] build log: maven-build-$PROJECT_NAME.log" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1


PROJECT_HOME=$BUILD_DIR/astrogrid/$PROJECT_NAME

cd $BUILD_DIR
echo "[ag-build-$PROJECT_NAME] remove old log" >> $BUILD_DIR/$LOG_FILE 2>&1
rm $BUILD_DIR/$LOG_FILE

echo "[ag-build-$PROJECT_NAME] sourcing java" >> $BUILD_DIR/$LOG_FILE 2>&1
source $HOME/.bash-config/java >> $BUILD_DIR/$LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] sourcing maven" >> $BUILD_DIR/$LOG_FILE 2>&1
source $HOME/.bash-config/maven >> $BUILD_DIR/$LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] sourcing cvs" >> $BUILD_DIR/$LOG_FILE 2>&1
source $HOME/.bash-config/cvs >> $BUILD_DIR/$LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] build home: $BUILD_DIR" >> $BUILD_DIR/$LOG_FILE 2>&1
cd $BUILD_DIR >> $BUILD_DIR/$LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] remove old SNAPSHOTS" >> $BUILD_DIR/$LOG_FILE 2>&1
sh $SCRIPTHOME/maven-remove-jars.sh $PROJECT_NAME >> $BUILD_DIR/$LOG_FILE 2>&1

echo "Saving tomcatServers.xml config file"
cp $PROJECT_HOME/$TOMCAT_SERVERS $TMP >> $BUILD_DIR/$LOG_FILE 2>&1
echo "[ag-build-$PROJECT_NAME] removing $PROJECT_HOME" >> $BUILD_DIR/$LOG_FILE 2>&1
rm -fr $PROJECT_HOME >> $BUILD_DIR/$LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] cvs checkout" >> $BUILD_DIR/$LOG_FILE 2>&1
cvs -d $CVSROOT co -A astrogrid/$PROJECT_NAME >> $BUILD_DIR/$LOG_FILE 2>&1
echo "Restoring tomcatServers.xml config file"
mv $TMP/$TOMCAT_SERVERS $PROJECT_HOME >> $BUILD_DIR/$LOG_FILE 2>&1


echo "[ag-build-$PROJECT_NAME] project home: $PROJECT_HOME" >> $BUILD_DIR/$LOG_FILE 2>&1
cd $PROJECT_HOME >> $BUILD_DIR/$LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] generate and deploy site; deploy SNAPSHOT" >> $BUILD_DIR/$LOG_FILE 2>&1

maven deployWebapps >> $BUILD_DIR/$LOG_FILE 2>&1 
maven site >> $BUILD_DIR/$LOG_FILE 2>&1 
maven site:fsdeploy >> $BUILD_DIR/$LOG_FILE 2>&1

echo "[ag-build-$PROJECT_NAME] deploy build log" >> $BUILD_DIR/$LOG_FILE 2>&1
cp $BUILD_DIR/$LOG_FILE /var/www/www/maven/build/log

echo "[ag-build-$PROJECT_NAME] back to start dir: $OLDDIR" >> $BUILD_DIR/$LOG_FILE 2>&1
cd $OLDDIR


echo >> $BUILD_DIR/$LOG_FILE 2>&1



##################################################################################################








echo >> $BUILD_DIR/$LOG_FILE 2>&1
echo "AstroGrid Build ($DATE)" >> $BUILD_DIR/$LOG_FILE 2>&1
echo "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" >> $BUILD_DIR/$LOG_FILE 2>&1
echo >> $BUILD_DIR/$LOG_FILE 2>&1
