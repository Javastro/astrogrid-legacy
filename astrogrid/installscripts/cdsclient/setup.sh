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
# Global settings.
export ASTROGRID_HOST=`hostname -f`
export ASTROGRID_HOME=/home/astrogrid
export ASTROGRID_TEMP=/home/astrogrid/temp
export ASTROGRID_USER=astrogrid

#
# Needs gcc installed
yum -y install gcc

#
# Check the temp directory
if [ ! -d ${ASTROGRID_TEMP} ]
then
	echo ""
    echo "Creating AstroGrid temp directory"
    echo "  Path : ${ASTROGRID_TEMP}"
    mkdir ${ASTROGRID_TEMP}
    chmod astrogrid.users ${ASTROGRID_TEMP}
    chmod a+rwx ${ASTROGRID_TEMP}
fi

#
# Install the vizquery program
pushd ${ASTROGRID_TEMP}
    wget http://vizier.u-strasbg.fr/viz-bin/ftp-index?/ftp/pub/sw/cdsclient.tar.gz
    tar -xvf cdsclient.tar 
    pushd cdsclient-2.86
        ./configure
        make
        make install
    popd
popd

#
# Repair any permissions
chown -R astrogrid.users ${ASTROGRID_TEMP}
chmod -R a+rw ${ASTROGRID_TEMP}
