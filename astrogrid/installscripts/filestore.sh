#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# FileStore settings.
#FILESTORE_VERSION=2006.3.01fs
FILESTORE_VERSION=2007.1fs
FILESTORE_WARFILE=astrogrid-filestore-${FILESTORE_VERSION}.war
FILESTORE_CONTEXT=astrogrid-filestore
#
# Set the install date (used in registration documents).
INSTALL_DATE=`date "+%Y-%m-%dT%H:%M:%SZ%z"`

echo ""
echo "Installing AstroGrid FileStore"

echo ""
echo "  FILESTORE_VERSION : ${FILESTORE_VERSION:?"undefined"}"
echo "  FILESTORE_CONTEXT : ${FILESTORE_CONTEXT:?"undefined"}"

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

echo "  INTERNAL_URL    : ${ASTROGRID_INTERNAL:?"undefined"}/${FILESTORE_CONTEXT:?"undefined"}/"
echo "  EXTERNAL_URL    : ${ASTROGRID_EXTERNAL:?"undefined"}/${FILESTORE_CONTEXT:?"undefined"}/"

#
# Create FileStore directories.
if [ -d ${ASTROGRID_HOME}/filestore ]
then
    echo ""
    echo "FileStore directory already exists"
    echo "  Path : ${ASTROGRID_HOME}/filestore"
	exit 1
fi

echo ""
echo "Creating FileStore directories"
echo "  Path : ${ASTROGRID_HOME}/filestore"
mkdir ${ASTROGRID_HOME}/filestore
mkdir ${ASTROGRID_HOME}/filestore/data
mkdir ${ASTROGRID_HOME}/filestore/webapp

#
# Get the filestore war file.
echo ""
echo "Downloading FileStore war file"
pushd ${ASTROGRID_HOME}/filestore/webapp
	wget ${ASTROGRID_MAVEN}/org.astrogrid/wars/${FILESTORE_WARFILE}
popd

#
# Generate the webapp context.
echo ""
echo "Generating webapp context"
cat > ${ASTROGRID_HOME}/filestore/webapp/context.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
<Context
    displayName="AstroGrid FileStore"
    docBase="${ASTROGRID_HOME}/filestore/webapp/${FILESTORE_WARFILE}"
    path="/${FILESTORE_CONTEXT}"
    >
    <!-- Configure the filestore service identifier -->
    <Environment
        description="The FileStore service identifier"
        name="org.astrogrid.filestore.service.ivorn"
        type="java.lang.String"
        value="ivo://${ASTROGRID_AUTH}/filestore"
        />
    <!-- Configure the data access URL -->
    <Environment
        description="The FileStore access URL"
        name="org.astrogrid.filestore.service.url"
        type="java.lang.String"
        value="${ASTROGRID_EXTERNAL}/${FILESTORE_CONTEXT}/filestore"
        />
    <!-- Configure the repository location -->
    <Environment
        description="The FileStore repository location"
        name="org.astrogrid.filestore.repository"
        type="java.lang.String"
        value="${ASTROGRID_HOME}/filestore/data"
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
echo "Starting FileStore webapp"
echo "  URL : ${ASTROGRID_INTERNAL}/manager/html/deploy"
echo "  CXT : /${FILESTORE_CONTEXT}"
echo "  XML : file://${ASTROGRID_HOME}/filestore/webapp/context.xml"
if [ `curl -s \
      --url ${ASTROGRID_INTERNAL}/manager/html/deploy \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --data "deployPath=/${FILESTORE_CONTEXT}" \
      --data "deployConfig=file://${ASTROGRID_HOME}/filestore/webapp/context.xml" \
      | grep -c "OK - Deployed application at context path /${FILESTORE_CONTEXT}"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error starting FileStore webapp"
    exit 1
fi

#
# Check the FileStore home page.
echo ""
echo "Checking FileStore home page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${FILESTORE_CONTEXT}/"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${FILESTORE_CONTEXT}/ \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing FileStore home page"
    exit 1
fi

#
# Check the FileStore admin page
echo ""
echo "Checking FileStore admin page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${FILESTORE_CONTEXT}/admin/index.jsp"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --url ${ASTROGRID_INTERNAL}/${FILESTORE_CONTEXT}/admin/index.jsp \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing FileStore admin page"
    exit 1
fi

#
# Generate the registration document.
# ** Change this to generate a v1.0 resource **
echo ""
echo "Generating service registration"
cat > ${ASTROGRID_HOME}/filestore/resource.xml << EOF
<vor:VOResources
    xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"
    xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3"
    xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5"
    xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
    <vor:Resource xsi:type="vr:Service"  updated="${INSTALL_DATE}" status="active">
        <title>FileStore demo service</title>
        <identifier>ivo://${ASTROGRID_AUTH}/filestore</identifier>
        <curation>
            <publisher>AstroGrid</publisher>
            <contact>
                <name>${ASTROGRID_ADMIN}</name>
                <email>${ASTROGRID_EMAIL}</email>
            </contact>
        </curation>
        <content>
            <subject>FileStore</subject>
            <subject>VOStore</subject>
            <description>FileStore service</description>
            <referenceURL>http://www.astrogrid.org</referenceURL>
            <type>BasicData</type>
            <relationship>
            	<relationshipType>derived-from</relationshipType>
            	<relatedResource ivo-id="ivo://org.astrogrid/FileStoreKind">FileStore Kind</relatedResource>
            </relationship>  
        </content>
        <interface xsi:type="vs:WebService">
        	<accessURL use="full">${ASTROGRID_EXTERNAL}/${FILESTORE_CONTEXT}/services/FileStore</accessURL>
        </interface> 
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
echo "  XML  : file://${ASTROGRID_HOME}/filestore/resource.xml"
if [ `curl -s -i \
     --url  ${REGISTRY_ENTRY} \
     --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
     --data "addFromURL=true" \
     --data "uploadFromURL=upload"  \
     --data "docurl=file://${ASTROGRID_HOME}/filestore/resource.xml" \
     | head -n 10 | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Failed to register the service"
    exit 1
fi
