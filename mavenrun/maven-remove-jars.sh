OLDDIR=$PWD

# Maven repository.
MAVEN_PUBLIC_REPO=/var/www/www/maven
PROJECT_NAME=$1

# Change to Maven repository directory for project.
cd $MAVEN_PUBLIC_REPO/astrogrid-$PROJECT_NAME/jars

# Ensure snapshot is not deleted.
SNAPSHOT_VERSION=`cat astrogrid-$PROJECT_NAME-snapshot-version`
REAL_SNAPSHOT_JAR=astrogrid-$PROJECT_NAME-$SNAPSHOT_VERSION.jar

# Remove all but current and today's snapshot.
#if [ -e $REAL_SNAPSHOT_JAR ]
#then
#  echo "Removing ..."
#  echo ">>>"
#  find -name '*.jar' -and -not -name "*`date +%Y%m%d`*.jar" -and -not -name "*SNAPSHOT*" -and -not -name "$REAL_SNAPSHOT_JAR" -exec rm '{}' ';' -print
#  echo "<<<"
#else  
#  echo "No snapshot jar... looked in <$PWD> for <$REAL_SNAPSHOT_JAR>"
#fi 

## alternative - get rid of all the snapshot type files older than 8 days
find . -regex ".*-200[0-9]+\.[0-9]+\.\(war\|jar\)\(\.md5\)?" -ctime +8 -exec rm '{}' ';' -print

cd $OLDDIR
