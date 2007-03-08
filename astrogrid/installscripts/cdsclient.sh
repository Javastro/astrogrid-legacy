#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# How to create the application description.
#http://www2.astrogrid.org/software/astrogrid-component-descriptions/command-line-application-server/Application-description%20file%20for%20command-line-application%20server/

#
# Cdsclient settings.
APPLICATION_VERSION=2007.1a
APPLICATION_WARFILE=astrogrid-cea-commandline-${APPLICATION_VERSION}.war
APPLICATION_CONTEXT=astrogrid-cdsclient
#
# Set the install date (used in registration documents).
INSTALL_DATE=`date "+%Y-%m-%dT%H:%M:%SZ%z"`

echo ""
echo "Installing CDSClient application"

echo ""
echo "  APPLICATION_VERSION : ${APPLICATION_VERSION:?"undefined"}"
echo "  APPLICATION_CONTEXT : ${APPLICATION_CONTEXT:?"undefined"}"

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

echo "  REGISTRY_ADMIN  : ${REGISTRY_ADMIN:?"undefined"}"
echo "  REGISTRY_QUERY  : ${REGISTRY_QUERY:?"undefined"}"
echo "  REGISTRY_ENTRY  : ${REGISTRY_ENTRY:?"undefined"}"

echo "  INTERNAL_URL    : ${ASTROGRID_INTERNAL:?"undefined"}/${APPLICATION_CONTEXT:?"undefined"}/"
echo "  EXTERNAL_URL    : ${ASTROGRID_EXTERNAL:?"undefined"}/${APPLICATION_CONTEXT:?"undefined"}/"

#
# Create cdsclient directories.
if [ -d ${ASTROGRID_HOME}/cdsclient ]
then
    echo ""
    echo "Cdsclient directory already exists"
    echo "  Path : ${ASTROGRID_HOME}/cdsclient"
	exit 1
fi

echo ""
echo "Creating cdsclient directories"
echo "  Path : ${ASTROGRID_HOME}/cdsclient"
mkdir ${ASTROGRID_HOME}/cdsclient
mkdir ${ASTROGRID_HOME}/cdsclient/base
mkdir ${ASTROGRID_HOME}/cdsclient/base/temp
mkdir ${ASTROGRID_HOME}/cdsclient/base/records
mkdir ${ASTROGRID_HOME}/cdsclient/base/config

mkdir ${ASTROGRID_HOME}/cdsclient/webapp

#
# Get the CEA war file.
echo ""
echo "Downloading CEA war file"
pushd ${ASTROGRID_HOME}/cdsclient/webapp
	wget ${ASTROGRID_MAVEN}/org.astrogrid/wars/${APPLICATION_WARFILE}
popd

#
# Generate the webapp context.
echo ""
echo "Generating webapp context"
cat > ${ASTROGRID_HOME}/cdsclient/webapp/context.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
<Context
    displayName="AstroGrid experiment"
    docBase="${ASTROGRID_HOME}/cdsclient/webapp/${APPLICATION_WARFILE}"
    path="/${APPLICATION_CONTEXT}"
    >
    <!-- Configure the base directory location -->
    <Environment
        description="The application base directory"
        name="cea.base.dir"
        type="java.lang.String"
        value="${ASTROGRID_HOME}/cdsclient/base"
        />
    <!-- Configure the application manager class -->
    <Environment
        description="The application manager class"
        name="cea.component.manager.class"
        type="java.lang.String"
        value="org.astrogrid.applications.commandline.CommandLineCEAComponentManager"
        />
    <!-- Configure the external access URL -->
    <Environment
        description="The application access URL"
        name="cea.webapp.url"
        type="java.lang.String"
        value="${ASTROGRID_EXTERNAL}/${APPLICATION_CONTEXT}"
        />
    <!-- Enable chunked data transfer -->
    <Environment
        description="Enable chunked data transfer"
        name="org.astrogrid.filestore.chunked.send"
        type="java.lang.String"
        value="enabled"
        />
    <!-- Configure the local publishing registry endpoint -->
    <Environment
        description="The publishing Registry endpoint URL"
        name="org.astrogrid.registry.admin.endpoint"
        type="java.lang.String"
        value="${REGISTRY_ADMIN}"
        />
    <!-- Configure the global query registry endpoint -->
    <Environment
        description="The query Registry endpoint URL"
        name="org.astrogrid.registry.query.endpoint"
        type="java.lang.String"
        value="${REGISTRY_QUERY}"
        />
</Context>
EOF

#
# Ask Tomcat to load the webapp.
echo ""
echo "Starting cdsclient webapp"
echo "  URL : ${ASTROGRID_INTERNAL}/manager/html/deploy"
echo "  CXT : /${APPLICATION_CONTEXT}"
echo "  XML : file://${ASTROGRID_HOME}/cdsclient/webapp/context.xml"
if [ `curl -s \
      --url ${ASTROGRID_INTERNAL}/manager/html/deploy \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --data "deployPath=/${APPLICATION_CONTEXT}" \
      --data "deployConfig=file://${ASTROGRID_HOME}/cdsclient/webapp/context.xml" \
      | grep -c "OK - Deployed application at context path /${APPLICATION_CONTEXT}"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error starting cdsclient webapp"
    exit 1
fi

#
# Check the cdsclient home page.
echo ""
echo "Checking cdsclient home page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${APPLICATION_CONTEXT}/"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${APPLICATION_CONTEXT}/ \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing cdsclient home page"
    exit 1
fi

#
# Check the cdsclient admin page
echo ""
echo "Checking cdsclient admin page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${APPLICATION_CONTEXT}/admin/index.jsp"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --url ${ASTROGRID_INTERNAL}/${APPLICATION_CONTEXT}/admin/index.jsp \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing cdsclient admin page"
    exit 1
fi
