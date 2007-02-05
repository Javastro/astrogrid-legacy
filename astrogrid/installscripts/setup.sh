#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
#   <meta:description>
#     Initial setup script for the test machine.
#     This script needs to be run as root.
#   </meta:description>
# </meta:header>
#

#
# Get the install script name and path.
SCRIPT_NAME=`basename "$0"`
SCRIPT_PATH=`dirname "$0"`

#
# Test settings.
export ASTROGRID_HOST=`hostname -f`
export ASTROGRID_HOME=/home/astrogrid
export ASTROGRID_USER=astrogrid

#
# Set the Java RPM version.
export JAVA_RPM=jdk-1_5_0_08-linux-i586.rpm
export JAVA_DIR=jdk1.5.0_08
#
# Set JAVA_HOME environment variable.
export JAVA_HOME=/usr/java/${JAVA_DIR}

echo "Test settings ...."
echo "  ASTROGRID_HOST : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_HOME : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER : ${ASTROGRID_USER:?"undefined"}"

#
# Create the astrogrid user account.
if [ ! -d ${ASTROGRID_HOME} ]
then
	echo ""
    echo "Creating AstroGrid user account"
    echo "   Name : ${ASTROGRID_USER}"
    echo "   Home : ${ASTROGRID_HOME}"
    useradd -c "AstroGrid" -m -d ${ASTROGRID_HOME} ${ASTROGRID_USER} 
    chmod a+rx ${ASTROGRID_HOME}
fi

#
# Create the downloads directory.
if [ ! -d ${ASTROGRID_HOME}/downloads ]
then
	echo ""
    echo "Creating AstroGrid downloads directory"
    echo "  Path : ${ASTROGRID_HOME}/downloads"
    mkdir ${ASTROGRID_HOME}/downloads
#    chmod a+rwx ${ASTROGRID_HOME}/downloads
    chown ${ASTROGRID_USER}.users ${ASTROGRID_HOME}/downloads
fi

#
# Get the target java JDK.
# Need to post latest JDK to our repo.
if [ ! -f ${ASTROGRID_HOME}/downloads/${JAVA_RPM} ]
then
	pushd $ASTROGRID_HOME/downloads
		echo ""
		echo "Downloading Java JDK .."
    	wget http://www.astrogrid.org/maven/java--downloads/${JAVA_RPM}
	popd
fi

#
# Install the JDK (need to be root).
if [ ! -e ${JAVA_HOME}/bin/java ]
then
	echo ""
	echo "Installing Java JDK .."
	rpm -i ${ASTROGRID_HOME}/downloads/${JAVA_RPM}
fi

#
# Check the Java settings.
echo ""
echo "Checking Java settings .."
echo "  JAVA_HOME    : ${JAVA_HOME}"
if [ ! -d ${JAVA_HOME} ]
then
    echo "Unable to locate JAVA_HOME"
    exit 1
else
    echo "JAVA_HOME is ok"
fi

if [ ! -e ${JAVA_HOME}/bin/java ]
then
    echo "Unable to locate JAVA_HOME/bin/java"
    exit 1
else
    echo "JAVA_HOME/bin is ok"
fi

#
# Check the Java version.
echo "Checking Java version ..."
${JAVA_HOME}/bin/java -version

