#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

echo ""
echo "Installing AstroGrid Tomcat"
echo "  ASTROGRID_HOST : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_HOME : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER : ${ASTROGRID_USER:?"undefined"}"
echo "  ASTROGRID_PASS : ${ASTROGRID_PASS:?"undefined"}"

#
# Set the Tomcat version.
export TOMCAT_VERSION=5.5.20
echo "  TOMCAT_VERSION : ${TOMCAT_VERSION:?"undefined"}"
#
# Set the Tomcat zipfile.
export TOMCAT_ZIPFILE=apache-tomcat-${TOMCAT_VERSION}.zip
echo "  TOMCAT_ZIPFILE : ${TOMCAT_ZIPFILE:?"undefined"}"
#
# Set the Tomcat-admin zipfile.
export TOMCAT_ADMIN_ZIPFILE=apache-tomcat-${TOMCAT_VERSION}-admin.zip
echo "  TOMCAT_ADMIN_ZIPFILE : ${TOMCAT_ADMIN_ZIPFILE:?"undefined"}"
#
# Set the Tomcat download site.
export TOMCAT_MIRROR=http://www.mirrorservice.org/sites/ftp.apache.org/tomcat/tomcat-5/v${TOMCAT_VERSION}/bin
echo "  TOMCAT_MIRROR  : ${TOMCAT_MIRROR:?"undefined"}"

#
# Check the home directory.
if [ ! -d ${ASTROGRID_HOME} ]
then
    echo "Unable to locate ASTROGRID_HOME"
    echo "  ASTROGRID_HOME : ${ASTROGRID_HOME:?"undefined"}"
    exit 1
fi

#
# Create downloads directory
if [ ! -d ${ASTROGRID_HOME}/downloads ]
then
	echo ""
    echo "Creating AstroGrid downloads directory"
    echo "  Path : ${ASTROGRID_HOME}/downloads"
    mkdir ${ASTROGRID_HOME}/downloads
    chmod a+rwx ${ASTROGRID_HOME}/downloads
fi

#
# Get the current Tomcat distro.
if [ ! -f ${ASTROGRID_HOME}/downloads/${TOMCAT_ZIPFILE} ]
then
	echo ""
	echo "Downloading Tomcat zipfile"
	pushd ${ASTROGRID_HOME}/downloads
    	wget ${TOMCAT_MIRROR}/${TOMCAT_ZIPFILE}
    	wget ${TOMCAT_MIRROR}/${TOMCAT_ADMIN_ZIPFILE}
	popd
fi

#
# Unpack the Tomcat distro.
if [ ! -f ${ASTROGRID_HOME}/downloads/${TOMCAT_ZIPFILE} ]
then
    echo "Unable to locate Tomcat zip file"
	echo "  ${ASTROGRID_HOME}/downloads/${TOMCAT_ZIPFILE}"
    exit 1
else
    pushd ${ASTROGRID_HOME}
		echo ""
        echo "Unpacking Tomcat zip file"
        unzip -o ${ASTROGRID_HOME}/downloads/${TOMCAT_ZIPFILE}
        echo "Unpacking Tomcat-admin zip file"
        unzip -o ${ASTROGRID_HOME}/downloads/${TOMCAT_ADMIN_ZIPFILE}
    popd
fi

#
# Set CATALINA_HOME environment variable.
echo ""
echo "Setting CATALINA_HOME"
export CATALINA_HOME=${ASTROGRID_HOME}/apache-tomcat-${TOMCAT_VERSION}
if [ ! -d ${CATALINA_HOME} ]
then
    echo "ERROR : Unable to locate CATALINIA_HOME"
	echo "  CATALINA_HOME : ${CATALINA_HOME:?"undefined"}"
    exit 1
fi

#
# Set the CATALINA opts.
# The Tomcat startup scripts look for a setenv file in ${CATALINA_HOME}/bin
# For high traffic or large volume system, you will need to increase the memory the JVM is allowed to use.
echo ""
echo "Setting CATALINA_OPTS"
cat >> ${CATALINA_HOME}/bin/setenv.sh << EOF
export CATALINA_OPTS="-Xmx512M"
EOF

#
# Set the permissions on the Tomcat control scripts.
echo ""
echo "Setting Tomcat script permissions"
chmod a+x ${CATALINA_HOME}/bin/*.sh

#
# Add the Tomcat login account.
# *separate <role> elements are not required in Tomcat 5.5 
echo ""
echo "Patching Tomcat users"
pushd ${CATALINA_HOME}/conf
patch -p1 tomcat-users.xml << EOF
*** old/tomcat-users.xml  2007-02-05 14:53:39.000000000 +0000
--- new/tomcat-users.xml  2007-02-05 14:53:39.000000000 +0000
***************
*** 7,10 ****
--- 7,11 ----
    <user name="tomcat" password="tomcat" roles="tomcat" />
    <user name="role1"  password="tomcat" roles="role1"  />
    <user name="both"   password="tomcat" roles="tomcat,role1" />
+   <user username="astrogrid" password="${ASTROGRID_PASS}" roles="manager,admin,paladmin"/>
  </tomcat-users>
EOF
popd

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




