#!/bin/sh
#
# Shell script to build our project.
#
# <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/Attic/build.sh,v $</cvs:source>
# <cvs:date>$Author: dave $</cvs:date>
# <cvs:author>$Date: 2003/06/12 18:19:38 $</cvs:author>
# <cvs:version>$Revision: 1.1 $</cvs:version>
#
# <cvs:log>
# $Log: build.sh,v $
# Revision 1.1  2003/06/12 18:19:38  dave
# Initial import into cvs ...
#
# Revision 1.1  2003/06/03 13:16:47  dave
# Added initial iter 02 code
#
# Revision 1.1  2003/05/25 11:06:06  Dumbledore
# First import into local CVS.
#
# </cvs:log>
#
#

#
# FIXME
# Set our Java config path.
#JAVA_CONFIG=~/local/projects/config/java
#JAVA_CONFIG=/home/dave/projects/java
JAVA_CONFIG=/var/projects/config/java

#
# Configure our JDK.
# ${JAVA_CONFIG:undefined}/bin/sunos/config-jdk.sh
. ${JAVA_CONFIG:undefined}/bin/linux/config-jdk.sh

#
# Configure Ant.
# ${JAVA_CONFIG:undefined}/bin/sunos/config-ant.sh
. ${JAVA_CONFIG:undefined}/bin/linux/config-ant.sh

#
# Configure Junit.
# ${JAVA_CONFIG:undefined}/bin/sunos/config-junit.sh
. ${JAVA_CONFIG:undefined}/bin/linux/config-junit.sh

#
# Set our build classpath.
BUILD_CLASSPATH=${JUNIT_JARS:?"undefined"}:${ANT_JARS:?"undefined"}:${JDK_JARS:?"undefined"}

#
# Run Ant
echo
echo "----\"----"
echo "Running Ant"
echo
echo "BUILD_CLASSPATH : "
echo $BUILD_CLASSPATH
echo


#${JDK_HOME:?"undefined"}/bin/java -classpath ${BUILD_CLASSPATH:?"undefined"} -Dant.home=${ANT_HOME:?"undefined"} org.apache.tools.ant.Main $1 $2
${JDK_HOME:?"undefined"}/bin/java -classpath ${BUILD_CLASSPATH:?"undefined"} -Djava.endorsed.dirs=xml-cocoon2/lib/endorsed -Dant.home=${ANT_HOME:?"undefined"} org.apache.tools.ant.Main $1 $2

echo
echo "----\"----"

