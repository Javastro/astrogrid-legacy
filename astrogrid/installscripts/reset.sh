#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# Read existing settings
if [ -f astrogrid-settings ]
then
    . astrogrid-settings
fi

#
# Default settings.
ASTROGRID_HOME=${ASTROGRID_HOME:-"/home/astrogrid"}
ASTROGRID_USER=${ASTROGRID_USER:-"astrogrid"}
ASTROGRID_HOST=${ASTROGRID_HOST:-`hostname -f`}

export JAVA_HOME=${JAVA_HOME:-"/usr/java/jdk1.5.0_08"}
export CATALINA_HOME=${CATALINA_HOME:-"unknown"}

echo ""
echo "Removing AstroGrid services"

#
# Shutdown tomcat.
if [ ${CATALINA_HOME} ]
then
    if [ -d ${CATALINA_HOME} ]
    then
    	${CATALINA_HOME}/bin/shutdown.sh
    fi
fi

#
# Remove the astrogrid directories.
if [ -d ${ASTROGRID_HOME}/downloads ]
then
    rm -rf ${ASTROGRID_HOME}/downloads
fi

if [ -d ${ASTROGRID_HOME}/registry ]
then
    rm -rf ${ASTROGRID_HOME}/registry
fi

if [ -d ${ASTROGRID_HOME}/filestore ]
then
    rm -rf ${ASTROGRID_HOME}/filestore
fi

if [ -d ${ASTROGRID_HOME}/filemanager ]
then
    rm -rf ${ASTROGRID_HOME}/filemanager
fi

if [ -d ${ASTROGRID_HOME}/community ]
then
    rm -rf ${ASTROGRID_HOME}/community
fi

