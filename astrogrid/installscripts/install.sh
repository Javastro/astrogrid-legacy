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
export ASTROGRID_HOST=`hostname -f`
export ASTROGRID_HOME=/home/astrogrid
export ASTROGRID_USER=astrogrid
export ASTROGRID_PASS=W6G117

echo "Installing AstroGrid services"
echo "  ASTROGRID_HOST : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_HOME : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER : ${ASTROGRID_USER:?"undefined"}"
echo "  ASTROGRID_PASS : ${ASTROGRID_PASS:?"undefined"}"

#
# Install Tomcat.
${SCRIPT_PATH}/tomcat.sh
