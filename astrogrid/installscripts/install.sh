#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# Get the install script name and path.
SCRIPT_NAME=`basename "$0"`
SCRIPT_PATH=`dirname "$0"`

#
# Set the install variables.
INSTALL_DATE=`date "+%Y%m%d%H%M%S"`
INSTALL_USER=`whoami`

#
# Test settings.
export JAVA_HOME=/usr/java/jdk1.5.0_08

export ASTROGRID_HOME=/home/astrogrid
export ASTROGRID_USER=astrogrid
export ASTROGRID_PASS=W6G117

export ASTROGRID_HOST=`hostname -f`
export ASTROGRID_PORT=8080

export ASTROGRID_MAVEN=http://www.astrogrid.org/maven/org.astrogrid

export ASTROGRID_AUTH=org.metagrid.caltech-demo
export ASTROGRID_EMAIL=astrogrid.demo@metagrid.org
export ASTROGRID_ADMIN="Astrogrid admin"
export ASTROGRID_BASE=http://${ASTROGRID_HOST}:${ASTROGRID_PORT}


for SETTING in JAVA_HOME ASTROGRID_HOME ASTROGRID_USER ASTROGRID_PASS ASTROGRID_HOST ASTROGRID_PORT ASTROGRID_AUTH ASTROGRID_EMAIL ASTROGRID_ADMIN ASTROGRID_BASE
do
echo "$SETTING setting: ${!SETTING} - return for OK or enter new value"
read SETTING_NEW
if [ ! $SETTING_NEW = "" ]
then
    export $SETTING=$SETTING_NEW
else
    echo "setting unchanged"
fi
done

echo "Installing AstroGrid services"
echo "  JAVA_HOME       : ${JAVA_HOME:?"undefined"}"
echo "  ASTROGRID_HOME  : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER  : ${ASTROGRID_USER:?"undefined"}"
echo "  ASTROGRID_PASS  : ${ASTROGRID_PASS:?"undefined"}"

echo "  ASTROGRID_HOST  : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_PORT  : ${ASTROGRID_PORT:?"undefined"}"

echo "  ASTROGRID_AUTH  : ${ASTROGRID_AUTH:?"undefined"}"
echo "  ASTROGRID_EMAIL : ${ASTROGRID_EMAIL:?"undefined"}"
echo "  ASTROGRID_ADMIN : ${ASTROGRID_ADMIN:?"undefined"}"
echo "  ASTROGRID_BASE  : ${ASTROGRID_BASE:?"undefined"}"

echo "Are these settings OK (y/n)?"
read SETTINGSOK
if [ $SETTINGSOK = "y" ]
then
    echo "OK, continuing..."
else
    echo "settings not OK - stopping"
    exit 1
fi    
    
#
# Check system tools already installed
. ${SCRIPT_PATH}/system.sh
#
# Install Tomcat.
. ${SCRIPT_PATH}/tomcat.sh
#
# Install AstroGrid registry.
. ${SCRIPT_PATH}/registry.sh
#
# Install AstroGrid filestore.
. ${SCRIPT_PATH}/filestore.sh
