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
echo "  JAVA_HOME      : ${JAVA_HOME:?"undefined"}"

echo "  ASTROGRID_HOME  : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER  : ${ASTROGRID_USER:?"undefined"}"
echo "  ASTROGRID_PASS  : ${ASTROGRID_PASS:?"undefined"}"

echo "  ASTROGRID_HOST  : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_PORT  : ${ASTROGRID_PORT:?"undefined"}"

echo "  ASTROGRID_AUTH  : ${ASTROGRID_AUTH:?"undefined"}"
echo "  ASTROGRID_EMAIL : ${ASTROGRID_EMAIL:?"undefined"}"
echo "  ASTROGRID_ADMIN : ${ASTROGRID_ADMIN:?"undefined"}"
echo "  ASTROGRID_BASE  : ${ASTROGRID_BASE:?"undefined"}"

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
    echo "ERROR : Unable to locate ASTROGRID_HOME"
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
    echo "ERROR : Unable to locate Tomcat zip file"
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
echo "  CATALINA_HOME : ${CATALINA_HOME}"
if [ ! -d ${CATALINA_HOME} ]
then
    echo "ERROR : Unable to locate CATALINIA_HOME"
    exit 1
fi

#
# Set the CATALINA opts.
# The Tomcat startup scripts look for a setenv file in ${CATALINA_HOME}/bin
# For high traffic or large volume system, you will need to increase the memory the JVM is allowed to use.
echo ""
echo "Setting CATALINA_OPTS"
cat >> ${CATALINA_HOME}/bin/setenv.sh << EOF
CATALINA_OPTS="-Xmx512M"
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
patch -b -p1 tomcat-users.xml << EOF
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
echo ""
echo "Starting Tomcat"
${CATALINA_HOME}/bin/startup.sh

#
# Pause to let Tomcat startup.
echo ""
echo "Waiting for Tomcat to start"
sleep 10s

#
# Check Tomcat home page.
echo ""
echo "Checking Tomcat home page"
echo "  URL : ${ASTROGRID_BASE}/"
if [ `curl -s --head ${ASTROGRID_BASE}/ | grep -c "200 OK"` = 1 ]
then
    echo "  PASS"
else
    echo "  ERROR : Error accessing Tomcat home page"
    exit 1
fi
#
# Check Tomcat admin login
echo ""
echo "Checking Tomcat admin"
echo "  URL  : ${ASTROGRID_BASE}/admin/"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head -u ${ASTROGRID_USER}:${ASTROGRID_PASS} ${ASTROGRID_BASE}/admin/ | grep -c "200 OK"` = 1 ]
then
    echo "  PASS"
else
    echo "  ERROR : Error accessing Tomcat admin page"
    exit 1
fi
#
# Check Tomcat manager login
echo ""
echo "Checking Tomcat manager"
echo "  URL  : ${ASTROGRID_BASE}/manager/html"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head -u ${ASTROGRID_USER}:${ASTROGRID_PASS} ${ASTROGRID_BASE}/manager/html | grep -c "200 OK"` = 1 ]
then
    echo "  PASS"
else
    echo "  ERROR : Error accessing Tomcat manager page"
    exit 1
fi

