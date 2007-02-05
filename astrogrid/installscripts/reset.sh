#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# Test settings.
export ASTROGRID_HOST=`hostname -f`
export ASTROGRID_HOME=/home/astrogrid
export ASTROGRID_USER=astrogrid

echo "Removing AstroGrid services"
echo "  ASTROGRID_HOST : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_HOME : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER : ${ASTROGRID_USER:?"undefined"}"

#
# Remove the dowloads directory.
rm -rf /home/astrogrid/downloads
rm -rf /home/astrogrid/apache-tomcat-5.5.20
