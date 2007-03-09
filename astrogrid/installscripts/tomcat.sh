#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# Set the Tomcat version.
TOMCAT_VERSION=${TOMCAT_VERSION:=5.5.20}
#
# Set the Tomcat zipfile.
TOMCAT_COREZIP=${TOMCAT_COREZIP:=apache-tomcat-${TOMCAT_VERSION}.zip}
#
# Set the Tomcat-admin zipfile.
TOMCAT_ADMINZIP=${TOMCAT_ADMINZIP:=apache-tomcat-${TOMCAT_VERSION}-admin.zip}
#
# Set the Tomcat download site.
#TOMCAT_MIRROR=${TOMCAT_MIRROR:=http://www.mirrorservice.org/sites/ftp.apache.org/tomcat/tomcat-5/v${TOMCAT_VERSION}/bin}
TOMCAT_MIRROR=${TOMCAT_MIRROR:=ftp://ftp.mirror.ac.uk/sites/ftp.apache.org/tomcat/tomcat-5/v${TOMCAT_VERSION}/bin}

#
# Set the CATALINA_HOME
CATALINA_HOME=${ASTROGRID_HOME}/apache-tomcat-${TOMCAT_VERSION}

echo ""
echo "Installing AstroGrid Tomcat"

echo ""
echo "  TOMCAT_VERSION  : ${TOMCAT_VERSION:?"undefined"}"
echo "  TOMCAT_COREZIP  : ${TOMCAT_COREZIP:?"undefined"}"
echo "  TOMCAT_ADMINZIP : ${TOMCAT_ADMINZIP:?"undefined"}"
echo "  TOMCAT_MIRROR   : ${TOMCAT_MIRROR:?"undefined"}"

echo ""
echo "  JAVA_HOME       : ${JAVA_HOME:?"undefined"}"
echo "  CATALINA_HOME   : ${CATALINA_HOME:?"undefined"}"

echo ""
echo "  ASTROGRID_HOME  : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER  : ${ASTROGRID_USER:?"undefined"}"
echo "  ASTROGRID_PASS  : ${ASTROGRID_PASS:?"undefined"}"

echo "  ASTROGRID_HOST  : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_PORT  : ${ASTROGRID_PORT:?"undefined"}"

echo "  ASTROGRID_AUTH  : ${ASTROGRID_AUTH:?"undefined"}"
echo "  ASTROGRID_EMAIL : ${ASTROGRID_EMAIL:?"undefined"}"
echo "  ASTROGRID_ADMIN : ${ASTROGRID_ADMIN:?"undefined"}"

echo "  INTERNAL_URL    : ${ASTROGRID_INTERNAL:?"undefined"}/"
echo "  EXTERNAL_URL    : ${ASTROGRID_EXTERNAL:?"undefined"}/"

#
# Check the home directory.
if [ ! -d ${ASTROGRID_HOME} ]
then
    echo "ERROR : Unable to locate ASTROGRID_HOME"
    echo "  ASTROGRID_HOME : ${ASTROGRID_HOME:?"undefined"}"
    exit 1
fi
#
# Create temp directory
if [ ! -d ${ASTROGRID_TEMP} ]
then
	echo ""
    echo "Creating AstroGrid temp directory"
    echo "  Path : ${ASTROGRID_TEMP}"
    mkdir ${ASTROGRID_TEMP}
    chmod a+rwx ${ASTROGRID_TEMP}
fi
#
# Get the current Tomcat distro.
if [ ! -f ${ASTROGRID_TEMP}/${TOMCAT_COREZIP} ]
then
	echo ""
	echo "Downloading Tomcat zipfile"
	wget -P ${ASTROGRID_TEMP} ${TOMCAT_MIRROR}/${TOMCAT_COREZIP}
fi

#
# Unpack the Tomcat distro.
if [ ! -f ${ASTROGRID_TEMP}/${TOMCAT_COREZIP} ]
then
    echo "ERROR : Unable to locate Tomcat zip file"
	echo "  ${ASTROGRID_TEMP}/${TOMCAT_COREZIP}"
    exit 1
else
    pushd ${ASTROGRID_HOME}
		echo ""
        echo "Unpacking Tomcat zip file"
        unzip -o ${ASTROGRID_TEMP}/${TOMCAT_COREZIP}
        echo "Unpacking Tomcat-admin zip file"
    popd
fi

#
# Check CATALINA_HOME environment variable.
echo ""
echo "Checking CATALINA_HOME"
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
# Get the HSQLDB driver jar.
HSQLDB_JAR=hsqldb-1.7.1.jar
if [ ! -f ${ASTROGRID_TEMP}/${HSQLDB_JAR} ]
then
	echo ""
	echo "Downloading HSQLDB driver jar"
    wget -P ${ASTROGRID_TEMP} ${ASTROGRID_MAVEN}/hsqldb/jars/${HSQLDB_JAR}
fi
#
# Install the HSQLDB driver jar.
echo ""
echo "Installing HSQLBD driver jar"
cp ${ASTROGRID_TEMP}/${HSQLDB_JAR} ${CATALINA_HOME}/common/lib/
#
# Start Tomcat.
echo ""
echo "Starting Tomcat"
${CATALINA_HOME}/bin/startup.sh

#
# Pause to let Tomcat startup.
echo ""
echo "Waiting for Tomcat to start"
sleep 20s

#
# Check Tomcat home page.
echo ""
echo "Checking Tomcat home page"
echo "  URL : ${ASTROGRID_INTERNAL}/"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/ \
      | grep -c "200 OK"` = 1 ]
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
echo "  URL  : ${ASTROGRID_INTERNAL}/admin/"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --url ${ASTROGRID_INTERNAL}/admin/ \
      | grep -c "200 OK"` = 1 ]
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
echo "  URL  : ${ASTROGRID_INTERNAL}/manager/html"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --url ${ASTROGRID_INTERNAL}/manager/html \
      | grep -c "200 OK"` = 1 ]
then
    echo "  PASS"
else
    echo "  ERROR : Error accessing Tomcat manager page"
    exit 1
fi

