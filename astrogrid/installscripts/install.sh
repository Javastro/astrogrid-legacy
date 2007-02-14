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
# Set the install date (used in registration documents).
INSTALL_USER=`whoami`
INSTALL_DATE=`date "+%Y-%m-%dT%H:%M:%SZ%z"`

#
# Read existing settings
if [ -f astrogrid-settings ]
then
    . astrogrid-settings
fi

#
# Default settings.
export JAVA_HOME=${JAVA_HOME:-/usr/java/jdk1.5.0_08}

export ASTROGRID_USER=${ASTROGRID_USER:-"astrogrid"}
export ASTROGRID_PASS=${ASTROGRID_PASS:-"W6G117"}
export ASTROGRID_HOST=${ASTROGRID_HOST:-`hostname -f`}
#
# Set the install date (used in registration documents).
INSTALL_DATE=`date "+%Y-%m-%dT%H:%M:%SZ%z"`

export ASTROGRID_PORT=${ASTROGRID_PORT:-"8080"}
export ASTROGRID_MAVEN=${ASTROGRID_MAVEN:-"http://www.astrogrid.org/maven"}
export ASTROGRID_AUTH=${ASTROGRID_AUTH:-"org.astrogrid.demo"}
export ASTROGRID_EMAIL=${ASTROGRID_EMAIL:-"demo@astrogrid.org"}
export ASTROGRID_ADMIN=${ASTROGRID_ADMIN:-"Demo admin"}


for SETTING in JAVA_HOME ASTROGRID_USER ASTROGRID_PASS ASTROGRID_HOST ASTROGRID_PORT ASTROGRID_AUTH ASTROGRID_EMAIL ASTROGRID_ADMIN
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

export ASTROGRID_HOME=${ASTROGRID_HOME:-"/home/${ASTROGRID_USER}"}
export ASTROGRID_INTERNAL=http://${ASTROGRID_HOST}:${ASTROGRID_PORT}
export ASTROGRID_EXTERNAL=${ASTROGRID_EXTERNAL:-${ASTROGRID_INTERNAL}}

for SETTING in ASTROGRID_HOME ASTROGRID_INTERNAL ASTROGRID_EXTERNAL
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
echo "  INTERNAL_URL    : ${ASTROGRID_INTERNAL:?"undefined"}"
echo "  EXTERNAL_URL    : ${ASTROGRID_EXTERNAL:?"undefined"}"

echo "Are these settings OK (Y/n)?"
read SETTINGSOK
if [ ! ${SETTINGSOK:-y} = "y" ]
then
    echo "settings not OK - stopping"
    exit 1
else
    echo "OK, continuing..."
fi    

#
# Save the settings.
cat > astrogrid-settings << EOF
    #
    # Java settings
    JAVA_HOME=${JAVA_HOME:?"undefined"}
    #
    # AstroGrid install settings
    ASTROGRID_HOME=${ASTROGRID_HOME:?"undefined"}
    ASTROGRID_USER=${ASTROGRID_USER:?"undefined"}
    ASTROGRID_PASS=${ASTROGRID_PASS:?"undefined"}
    ASTROGRID_HOST=${ASTROGRID_HOST:?"undefined"}
    ASTROGRID_PORT=${ASTROGRID_PORT:?"undefined"}
    ASTROGRID_MAVEN=${ASTROGRID_MAVEN:?"undefined"}
    ASTROGRID_AUTH=${ASTROGRID_AUTH:?"undefined"}
    ASTROGRID_EMAIL=${ASTROGRID_EMAIL:?"undefined"}
    ASTROGRID_ADMIN="${ASTROGRID_ADMIN:?"undefined"}"
    ASTROGRID_INTERNAL=${ASTROGRID_INTERNAL:?"undefined"}
    ASTROGRID_EXTERNAL=${ASTROGRID_EXTERNAL:?"undefined"}
EOF
    
#
# Check system tools already installed
echo ""
echo "System check (Y/n) ?"
read RESPONSE
if [ ${RESPONSE:-y} = "y" ]
then
    . ${SCRIPT_PATH}/system.sh
fi
#
# Install Tomcat.
echo ""
echo "Install Tomcat (Y/n) ?"
read RESPONSE
if [ ${RESPONSE:-y} = "y" ]
then
    . ${SCRIPT_PATH}/tomcat.sh
    if [ -f astrogrid-settings ]
    then
cat >> astrogrid-settings << EOF
    #
    # Tomcat settings
    CATALINA_HOME=${CATALINA_HOME:?"undefined"}
EOF
    fi
fi
#
# Export the Tomcat settings.
export CATALINA_HOME=${CATALINA_HOME:?undefined}

#
# Install Registry.
echo ""
echo "Install Registry (Y/n) ?"
read RESPONSE
if [ ${RESPONSE:-y} = "y" ]
then
    . ${SCRIPT_PATH}/registry.sh
    if [ -f astrogrid-settings ]
    then
cat >> astrogrid-settings << EOF
    #
    # Registry settings
    REGISTRY_ADMIN=${REGISTRY_ADMIN:?"undefined"}
    REGISTRY_QUERY=${REGISTRY_QUERY:?"undefined"}
    REGISTRY_ENTRY=${REGISTRY_ENTRY:?"undefined"}
EOF
    fi
fi
#
# Export the Registry settings.
export REGISTRY_ADMIN=${REGISTRY_ADMIN:?undefined}
export REGISTRY_QUERY=${REGISTRY_QUERY:?undefined}
export REGISTRY_ENTRY=${REGISTRY_ENTRY:?undefined}

#
# Install FileStore.
echo ""
echo "Install FileStore (Y/n) ?"
read RESPONSE
if [ ${RESPONSE:-y} = "y" ]
then
    . ${SCRIPT_PATH}/filestore.sh
fi
#
# Install FileManager.
echo ""
echo "Install FileManager (Y/n) ?"
read RESPONSE
if [ ${RESPONSE:-y} = "y" ]
then
    . ${SCRIPT_PATH}/filemanager.sh
fi
#
# Install Community.
echo ""
echo "Install Community (Y/n) ?"
read RESPONSE
if [ ${RESPONSE:-y} = "y" ]
then
    . ${SCRIPT_PATH}/community.sh
fi
#
# Install Example application.
echo ""
echo "Install Example (Y/n) ?"
read RESPONSE
if [ ${RESPONSE:-y} = "y" ]
then
    . ${SCRIPT_PATH}/example.sh
fi
