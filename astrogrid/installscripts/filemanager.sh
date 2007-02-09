#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# FileManager settings.
FILEMANAGER_VERSION=2006.3.01fm
FILEMANAGER_WARFILE=astrogrid-filemanager-${FILEMANAGER_VERSION}.war
FILEMANAGER_CONTEXT=astrogrid-filemanager

echo ""
echo "Installing AstroGrid FileManager"

echo ""
echo "  FILEMANAGER_VERSION : ${FILEMANAGER_VERSION:?"undefined"}"
echo "  FILEMANAGER_CONTEXT : ${FILEMANAGER_CONTEXT:?"undefined"}"

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

echo "  INTERNAL_URL    : ${ASTROGRID_INTERNAL:?"undefined"}/${FILEMANAGER_CONTEXT:?"undefined"}/"
echo "  EXTERNAL_URL    : ${ASTROGRID_EXTERNAL:?"undefined"}/${FILEMANAGER_CONTEXT:?"undefined"}/"

#
# Create FileManager directories.
if [ -d ${ASTROGRID_HOME}/filemanager ]
then
    echo ""
    echo "FileManager directory already exists"
    echo "  Path : ${ASTROGRID_HOME}/filemanager"
	exit 1
fi

echo ""
echo "Creating FileManager directories"
echo "  Path : ${ASTROGRID_HOME}/filemanager"
mkdir ${ASTROGRID_HOME}/filemanager
mkdir ${ASTROGRID_HOME}/filemanager/data
mkdir ${ASTROGRID_HOME}/filemanager/webapp

#
# Get the filemanager war file.
echo ""
echo "Downloading FileManager war file"
pushd ${ASTROGRID_HOME}/filemanager/webapp
	wget ${ASTROGRID_MAVEN}/org.astrogrid/wars/${FILEMANAGER_WARFILE}
popd

#
# Generate the webapp context.
echo ""
echo "Generating webapp context"
cat > ${ASTROGRID_HOME}/filemanager/webapp/context.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
<Context
    displayName="AstroGrid FileManager"
    docBase="${ASTROGRID_HOME}/filemanager/webapp/${FILEMANAGER_WARFILE}"
    path="/${FILEMANAGER_CONTEXT}"
    >
    <!-- Configure the service identifier -->
    <Environment
        description="The FileManager service identifier"
        name="org.astrogrid.filemanager.service.ivorn"
        type="java.lang.String"
        value="ivo://${ASTROGRID_AUTH}/filemanager"
        />
    <!-- Configure the default filestore -->
    <Environment
        description="The default FileStore identifier"
        name="org.astrogrid.filemanager.filestore.ivorn"
        type="java.lang.String"
        value="ivo://${ASTROGRID_AUTH}/filestore"
        />
    <!-- Configure the filemanager repository location -->
    <Environment
        description="The FileManager repository location"
        name="org.astrogrid.filemanager.basedir"
        type="java.lang.String"
        value="${ASTROGRID_HOME}/filemanager/data"
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
echo "Starting registry webapp"
echo "  URL : ${ASTROGRID_INTERNAL}/manager/html/deploy"
echo "  CXT : /${FILEMANAGER_CONTEXT}"
echo "  XML : file://${ASTROGRID_HOME}/filemanager/webapp/context.xml"
if [ `curl -s \
      --url ${ASTROGRID_INTERNAL}/manager/html/deploy \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --data "deployPath=/${FILEMANAGER_CONTEXT}" \
      --data "deployConfig=file://${ASTROGRID_HOME}/filemanager/webapp/context.xml" \
      | grep -c "OK - Deployed application at context path /${FILEMANAGER_CONTEXT}"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error starting FileManager webapp"
    exit 1
fi

#
# Check the FileManager home page.
echo ""
echo "Checking FileManager home page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${FILEMANAGER_CONTEXT}/"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${FILEMANAGER_CONTEXT}/ \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing FileManager home page"
    exit 1
fi

#
# Check the FileManager admin page
echo ""
echo "Checking FileManager admin page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${FILEMANAGER_CONTEXT}/admin/index.jsp"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --url ${ASTROGRID_INTERNAL}/${FILEMANAGER_CONTEXT}/admin/index.jsp \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing FileManager admin page"
    exit 1
fi

#
# Generate the registration document.
# ** Change this to generate a v1.0 resource **
echo ""
echo "Generating service registration"
cat > ${ASTROGRID_HOME}/filemanager/resource.xml << EOF
<vor:VOResources
    xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"
    xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3"
    xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5"
    xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
    <vor:Resource xsi:type="vr:Service"  updated="2004-11-20T15:34:22Z" status="active">
        <title>FileManager demo service</title>
        <identifier>ivo://${ASTROGRID_AUTH}/filemanager</identifier>
        <curation>
            <publisher>AstroGrid</publisher>
            <contact>
                <name>${ASTROGRID_ADMIN}</name>
                <email>${ASTROGRID_EMAIL}</email>
            </contact>
        </curation>
        <content>
            <subject>FileManager</subject>
            <subject>VOStore</subject>
            <description>FileManager service</description>
            <referenceURL>http://www.astrogrid.org</referenceURL>
            <type>BasicData</type>
            <relationship>
            	<relationshipType>derived-from</relationshipType>
            	<relatedResource ivo-id="ivo://org.astrogrid/FileManagerKind">FileManager Kind</relatedResource>
            </relationship>  
        </content>
        <interface xsi:type="vs:WebService">
        	<accessURL use="full">${ASTROGRID_EXTERNAL}/${FILEMANAGER_CONTEXT}/services/FileManagerPort</accessURL>
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
echo "  XML  : file://${ASTROGRID_HOME}/filemanager/resource.xml"
if [ `curl -s -i \
     --url  ${REGISTRY_ENTRY} \
     --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
     --data "addFromURL=true" \
     --data "uploadFromURL=upload"  \
     --data "docurl=file://${ASTROGRID_HOME}/filemanager/resource.xml" \
     | head -n 10 | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Failed to register the service"
    exit 1
fi
