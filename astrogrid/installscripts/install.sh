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
# Test settings.
export ASTROGRID_HOST=`hostname`
export ASTROGRID_HOME=/var/local/projects/astrogrid-caltech
export ASTROGRID_USER=astrogrid

#
# Test settings.
if [ -d ${ASTROGRID_HOME} ]
	echo "Deleting existing AstroGrid home directory"
	echo "  Path : ${ASTROGRID_HOME}"
	rm -r ${ASTROGRID_HOME}
fi

echo "Creating AstroGrid home directory"
echo "  Path : ${ASTROGRID_HOME}"
mkdir ${ASTROGRID_HOME}

echo "Installing AstroGrid services"
echo "  ASTROGRID_HOST : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_HOME : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER : ${ASTROGRID_USER:?"undefined"}"

#
# Install Tomcat.
${SCRIPT_PATH}/tomcat.sh
