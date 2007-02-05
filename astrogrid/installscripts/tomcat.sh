#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

echo "Installing AstroGrid Tomcat"
echo "  ASTROGRID_HOST : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_HOME : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER : ${ASTROGRID_USER:?"undefined"}"

#
# Set the Tomcat version.
export TOMCAT_VERSION=5.5.20
echo "  TOMCAT_VERSION : ${TOMCAT_VERSION:?"undefined"}"
#
# Set the Tomcat download site.
export TOMCAT_MIRROR=http://www.mirrorservice.org/sites/ftp.apache.org/tomcat/tomcat-5/v5.5.20/bin/apache-tomcat-5.5.20.zip
echo "  TOMCAT_MIRROR : ${TOMCAT_MIRROR:?"undefined"}"
#
# Set the Tomcat source.
export TOMCAT_SOURCE=${TOMCAT_MIRROR}/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.zip
echo "  TOMCAT_SOURCE : ${TOMCAT_SOURCE:?"undefined"}"

#
# Create downloads directory
if [ ! -d $ASTROGRID_HOME ]
    echo "Creating AstroGrid downloads directory"
    echo "  Path : ${ASTROGRID_HOME}/downloads"
    mkdir ${ASTROGRID_HOME}/downloads
    chmod a+rwx ${ASTROGRID_HOME}/downloads
fi

#
# Get the current Tomcat distro.
echo "Downloading Tomcat zipfile"
pushd ${ASTROGRID_HOME}/downloads
    wget ${TOMCAT_SOURCE}
popd

#
# Unpack the Tomcat distro.
echo "Unpacking Tomcat zipfile"
pushd ${ASTROGRID_HOME}
    unzip ${ASTROGRID_HOME}/downloads/apache-tomcat-${TOMCAT_VERSION}.zip
popd

#
# Set CATALINA_HOME environment variable.
export CATALINA_HOME=${ASTROGRID_HOME}/apache-tomcat-${TOMCAT_VERSION}
echo "Setting CATALINA_HOME"
echo "  CATALINA_HOME : ${CATALINA_HOME:?"undefined"}"

#
# Set the CATALINA opts.
# For high traffic or large volume system, you will need to increase the memory the JVM is allowed to use.
export CATALINA_OPTS="-Xmx512M"
echo "Setting CATALINA_OPTS"
echo "  CATALINA_OPTS : ${CATALINA_OPTS:?"undefined"}"
#
# Add this so a script file in Tomcat bin.
#

#
# Set the permissions on the Tomcat control scripts.
echo "Setting Tomcat script permissions"
chmod a+x ${CATALINA_HOME}/bin/*.sh

#
# Add the wokshop account and roles.
# *separate <role> elements are not required in Tomcat 5.5 
#vi $CATALINA_HOME/conf/tomcat-users.xml
#
#    <tomcat-users>
#      <user username="tomcat" password="tomcat" roles="tomcat"/>
#      <user username="both" password="tomcat" roles="tomcat,role1"/>
#      <user username="role1" password="tomcat" roles="role1"/>
#+     <user username="workshop" password="qwerty" roles="manager,admin,paladmin"/>
#    </tomcat-users>
#

#
# Start Tomcat.
#$CATALINA_HOME/bin/startup.sh

#
# Check Tomcat running.
# http://${ASTROGRID_HOSTNAME}:8080/

#
# Check Tomcat admin login (workshop, qwerty)
# http://${ASTROGRID_HOSTNAME}:8080/admin

#
# Check Tomcat manager login (workshop, qwerty)
# http://${ASTROGRID_HOSTNAME}:8080/manager/html




