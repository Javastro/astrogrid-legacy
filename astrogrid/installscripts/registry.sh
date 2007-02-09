#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# Registry settings.
REGISTRY_VERSION=2006.3.03r
REGISTRY_WARFILE=astrogrid-registry-${REGISTRY_VERSION}.war
REGISTRY_CONTEXT=astrogrid-registry

echo ""
echo "Installing AstroGrid registry"

echo ""
echo "  REGISTRY_VERSION : ${REGISTRY_VERSION:?"undefined"}"
echo "  REGISTRY_CONTEXT : ${REGISTRY_CONTEXT:?"undefined"}"

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

echo "  INTERNAL_URL    : ${ASTROGRID_INTERNAL:?"undefined"}/${REGISTRY_CONTEXT:?"undefined"}/"
echo "  EXTERNAL_URL    : ${ASTROGRID_EXTERNAL:?"undefined"}/${REGISTRY_CONTEXT:?"undefined"}/"

#
# Set the local registry endpoints.
REGISTRY_ADMIN=${ASTROGRID_INTERNAL}/${REGISTRY_CONTEXT}/services/RegistryUpdate
REGISTRY_ENTRY=${ASTROGRID_INTERNAL}/${REGISTRY_CONTEXT}/admin/addResourceEntry.jsp
REGISTRY_INTERNAL=${ASTROGRID_INTERNAL}/${REGISTRY_CONTEXT}/services/RegistryQuery
REGISTRY_EXTERNAL=${ASTROGRID_EXTERNAL}/${REGISTRY_CONTEXT}/services/RegistryQuery

#
# Set the external registry endpoints.
ASTROGRID_DEV=http://katatjuta.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery
ASTROGRID_LIVE=http://galahad.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery
#
# Set the query registry endpoint.

if [ ! ${REGISTRY_QUERY} ]
then
    REGISTRY_QUERY=${REGISTRY_INTERNAL}
fi

echo ""
echo "Please select the external registry endpoint :"
echo ""
echo "1) Local registry (internal) :"
echo "   ${REGISTRY_INTERNAL}"
echo ""
echo "2) Local registry (external) :"
echo "   ${REGISTRY_EXTERNAL}"
echo ""
echo "3) AstroGrid dev registry :"
echo "   ${ASTROGRID_DEV}"
echo ""
echo "4) AstroGrid live registry :"
echo "   ${ASTROGRID_LIVE}"
echo ""
echo "*) Current setting :"
echo "   ${REGISTRY_QUERY}"

read RESPONSE
case $RESPONSE in

    1)
        REGISTRY_QUERY=${REGISTRY_INTERNAL}
        ;;

    2)
        REGISTRY_QUERY=${REGISTRY_EXTERNAL}
        ;;

    3)
        REGISTRY_QUERY=${ASTROGRID_DEV}
        ;;

    4)
        REGISTRY_QUERY=${ASTROGRID_LIVE}
        ;;

    *)
        ;;

esac

if [ ! ${REGISTRY_QUERY} ]
then
    echo ""
    echo "ERROR : External registry endpoint required"
fi

echo ""
echo "Registry endpoints"
echo "  REGISTRY_ADMIN  : ${REGISTRY_ADMIN:?"undefined"}"
echo "  REGISTRY_QUERY  : ${REGISTRY_QUERY:?"undefined"}"
echo "  REGISTRY_ENTRY  : ${REGISTRY_ENTRY:?"undefined"}"

#
# Create registry directories.
if [ -d ${ASTROGRID_HOME}/registry ]
then
    echo ""
    echo "Registry directory already exists"
    echo "  Path : ${ASTROGRID_HOME}/registry"
	exit 1
fi

echo ""
echo "Creating registry directories"
echo "  Path : ${ASTROGRID_HOME}/registry"
mkdir ${ASTROGRID_HOME}/registry
mkdir ${ASTROGRID_HOME}/registry/log
mkdir ${ASTROGRID_HOME}/registry/exist
mkdir ${ASTROGRID_HOME}/registry/webapp

#
# Get the registry war file.
echo ""
echo "Downloading registry war file"
pushd ${ASTROGRID_HOME}/registry/webapp
	wget ${ASTROGRID_MAVEN}/org.astrogrid/wars/${REGISTRY_WARFILE}
popd

#
# Extract the default registry config.
echo ""
echo "Extracting eXist config"
unzip -j -d ${ASTROGRID_HOME}/registry/exist \
	${ASTROGRID_HOME}/registry/webapp/${REGISTRY_WARFILE} \
	WEB-INF/conf.xml

#
# Configure the eXist database location.
echo ""
echo "Patching eXist config"
patch -b -l ${ASTROGRID_HOME}/registry/exist/conf.xml << EOF
49c49
<       <db-connection database="native" files="data"
---
>       <db-connection database="native" files="${ASTROGRID_HOME}/registry/exist"
66c66
<               <recovery enabled="yes" sync-on-commit="no" journal-dir="data"  size="10M"/>
---
>               <recovery enabled="yes" sync-on-commit="no" journal-dir="${ASTROGRID_HOME}/registry/exist" size="10M"/>
EOF

#
# Generate the webapp context.
echo ""
echo "Generating webapp context"
cat > ${ASTROGRID_HOME}/registry/webapp/context.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
<Context
    displayName="AstroGrid publishing registry"
    docBase="${ASTROGRID_HOME}/registry/webapp/${REGISTRY_WARFILE}"
    path="/${REGISTRY_CONTEXT}"
    >
    <!-- Configure the main authority ID -->
    <Environment
        description="The main authority ID for this registry"
        name="reg.amend.authorityid"
        type="java.lang.String"
        value="${ASTROGRID_AUTH}"
        />
    <!-- Configure the OAI registry name -->
    <Environment
        description="The OAI identify verb"
        name="reg.amend.identify.repositoryName"
        type="java.lang.String"
        value="AstroGrid publishing registry"
        />
    <!-- Configure the OAI administrator email -->
    <Environment
        description="The OAI administrator email"
        name="reg.amend.identify.adminEmail"
        type="java.lang.String"
        value="mailto:${ASTROGRID_EMAIL}"
        />
    <!-- Disable harvesting -->
    <Environment
        description="Enable harvesting."
        name="reg.amend.harvest"
        type="java.lang.String"
        value="false"
        />
    <!-- Configure the log directory -->
    <Environment
        description="Registry log directory."
        name="reg.custom.loggingdirectory"
        type="java.lang.String"
        value="${ASTROGRID_HOME}/registry/log"
        />
    <!-- Configure the eXist config file and data directory -->
    <Environment
        description="eXist configuration and data directory"
        name="reg.custom.exist.configuration"
        type="java.lang.String"
        value="${ASTROGRID_HOME}/registry/exist/conf.xml"
        />
    <!-- Configure the internal (localhost) OAI URLs. -->
    <Environment
        description="The the OAI publishing url for 0.10 (not required)"
        name="reg.amend.oaipublish.0.10"
        type="java.lang.String"
        value="http://localhost:${ASTROGRID_PORT}/${REGISTRY_CONTEXT}/OAIHandlerv0_10"
        />
    <Environment
        description="The the OAI publishing url for 0.1 (not required)"
        name="reg.amend.oaipublish.0.1"
        type="java.lang.String"
        value="http://localhost:${ASTROGRID_PORT}/${REGISTRY_CONTEXT}/OAIHandlerv0_10"
        />
</Context>
EOF

#
# Ask Tomcat to load the webapp.
echo ""
echo "Starting registry webapp"
echo "  URL : ${ASTROGRID_INTERNAL}/manager/html/deploy"
echo "  CXT : /${REGISTRY_CONTEXT}"
echo "  XML : file://${ASTROGRID_HOME}/registry/webapp/context.xml"
if [ `curl -s \
      --url ${ASTROGRID_INTERNAL}/manager/html/deploy \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --data "deployPath=/${REGISTRY_CONTEXT}" \
      --data "deployConfig=file://${ASTROGRID_HOME}/registry/webapp/context.xml" \
      | grep -c "OK - Deployed application at context path /${REGISTRY_CONTEXT}"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error starting Registry webapp"
    exit 1
fi

#
# Check the registry home page.
echo ""
echo "Checking registry home page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${REGISTRY_CONTEXT}/"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${REGISTRY_CONTEXT}/ \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing Registry home page"
    exit 1
fi

#
# Check the registry admin page
echo ""
echo "Checking registry admin page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${REGISTRY_CONTEXT}/admin/index.jsp"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${REGISTRY_CONTEXT}/admin/index.jsp \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing Registry admin page"
    exit 1
fi

#
# Generate the registration document.
# ** Change this to generate a v1.0 resource **
echo ""
echo "Generating service registration"
cat > ${ASTROGRID_HOME}/registry/resource.xml << EOF
<vor:VOResources
    xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"
    xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3"
    xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5"
    xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
    <vor:Resource xsi:type="vg:Registry" updated="2004-11-20T15:34:22Z" status="active">
        <title>AstroGrid registry</title>
        <identifier>ivo://${ASTROGRID_AUTH}/org.astrogrid.registry.RegistryService</identifier>
        <curation>
            <publisher>AstroGrid</publisher>
            <contact>
                <name>${ASTROGRID_ADMIN}</name>
                <email>${ASTROGRID_EMAIL}</email>
            </contact>
        </curation>
        <content>
            <subject>registry</subject>
            <description>Astrogrid Registry</description>
            <referenceURL>${ASTROGRID_EXTERNAL}/${REGISTRY_CONTEXT}</referenceURL>
            <type>Archive</type>
        </content>
        <interface xsi:type="vs:WebService">
            <accessURL use="full">${ASTROGRID_EXTERNAL}/${REGISTRY_CONTEXT}/services/RegistryHarvest</accessURL>
        </interface>
        <vg:managedAuthority>${ASTROGRID_AUTH}</vg:managedAuthority>
    </vor:Resource>
</vor:VOResources>
EOF

#
# Register the service.
# ** No validation for this version of registry **
# ** Server does not return an error code if it fails **
echo ""
echo "Registering service"
echo "  URL  : ${REGISTRY_ENTRY}"
echo "  XML  : file://${ASTROGRID_HOME}/registry/resource.xml"
if [ `curl -s -i \
     --url  ${REGISTRY_ENTRY} \
     --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
     --data "addFromURL=true" \
     --data "uploadFromURL=upload"  \
     --data "docurl=file://${ASTROGRID_HOME}/registry/resource.xml" \
     | head -n 10 | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Failed to register the service"
    exit 1
fi

