#!/bin/sh
#
# Shell script to build our project.
#
# <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/Attic/build.sh,v $</cvs:source>
# <cvs:date>$Date: 2003/08/28 17:33:56 $</cvs:date>
# <cvs:author>$Author: dave $</cvs:author>
# <cvs:version>$Revision: 1.1 $</cvs:version>
#
# <cvs:log>
#   $Log: build.sh,v $
#   Revision 1.1  2003/08/28 17:33:56  dave
#   Initial policy prototype
#
# </cvs:log>
#
#

#
# FIXME
# Set our Java config path.
JAVA_CONFIG=/var/projects/config/java

#
# Configure our JDK.
. ${JAVA_CONFIG:undefined}/bin/linux/config-jdk.sh

#
# Configure Ant.
. ${JAVA_CONFIG:undefined}/bin/linux/config-ant.sh

#
# Configure Junit.
. ${JAVA_CONFIG:undefined}/bin/linux/config-junit.sh

#
# Set our build classpath.
BUILD_CLASSPATH=${JDK_JARS:?"undefined"}:${ANT_JARS:?"undefined"}:${JUNIT_JARS:?"undefined"}
#BUILD_CLASSPATH=${JDK_JARS:?"undefined"}:${ANT_JARS:?"undefined"}

#
# Run Ant
echo
echo "----\"----"
echo "Running Ant"
echo
echo "BUILD_CLASSPATH : "
echo $BUILD_CLASSPATH
echo

${JDK_HOME:?"undefined"}/bin/java -classpath ${BUILD_CLASSPATH:?"undefined"} -Dant.home=${ANT_HOME:?"undefined"} org.apache.tools.ant.Main $1 $2

echo
echo "----\"----"

