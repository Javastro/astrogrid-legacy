#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# Community settings.
#COMMUNITY_VERSION=2006.3.04ct
COMMUNITY_VERSION=2007.1ct
COMMUNITY_WARFILE=astrogrid-community-${COMMUNITY_VERSION}.war
COMMUNITY_CONTEXT=astrogrid-community

#
# Set the install date (used in registration documents).
INSTALL_DATE=`date "+%Y-%m-%dT%H:%M:%SZ%z"`

echo ""
echo "Installing AstroGrid Community"

echo ""
echo "  COMMUNITY_VERSION : ${COMMUNITY_VERSION:?"undefined"}"
echo "  COMMUNITY_CONTEXT : ${COMMUNITY_CONTEXT:?"undefined"}"

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

echo "  INTERNAL_URL    : ${ASTROGRID_INTERNAL:?"undefined"}/${COMMUNITY_CONTEXT:?"undefined"}/"
echo "  EXTERNAL_URL    : ${ASTROGRID_EXTERNAL:?"undefined"}/${COMMUNITY_CONTEXT:?"undefined"}/"


# ####
# ** EXTRA STEP required to add HSQLDB jar to Tomcat **
# Install HSQLDB database driver.
#

#
# Install the HSQLDB jar.
if [ ! -f ${CATALINA_HOME}/common/lib/hsqldb-1.7.1.jar ]
then
    #
    # Stop Tomcat ...
    echo ""
    echo "Stopping Tomcat"
    ${CATALINA_HOME}/bin/shutdown.sh
    #
    # Download and install the HSQLDB jars.
    echo ""
    echo "Installing HSQLDB jar"
    wget -P ${CATALINA_HOME}/common/lib ${ASTROGRID_MAVEN}/hsqldb/jars/hsqldb-1.7.1.jar
    #
    # Start Tomcat ...
    echo ""
    echo "Starting Tomcat"
    ${CATALINA_HOME}/bin/startup.sh
fi

#
# Create Community directories.
if [ -d ${ASTROGRID_HOME}/community ]
then
    echo ""
    echo "Community directory already exists"
    echo "  Path : ${ASTROGRID_HOME}/community"
	exit 1
fi

echo ""
echo "Creating Community directories"
echo "  Path : ${ASTROGRID_HOME}/community"
mkdir ${ASTROGRID_HOME}/community
mkdir ${ASTROGRID_HOME}/community/data
mkdir ${ASTROGRID_HOME}/community/webapp

#
# Get the Community war file.
echo ""
echo "Downloading Community war file"
pushd ${ASTROGRID_HOME}/community/webapp
	wget ${ASTROGRID_MAVEN}/org.astrogrid/wars/${COMMUNITY_WARFILE}
popd

#
# Generate the webapp context.
echo ""
echo "Generating webapp context"
cat > ${ASTROGRID_HOME}/community/webapp/context.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
<Context
    displayName="AstroGrid Community"
    docBase="${ASTROGRID_HOME}/community/webapp/${COMMUNITY_WARFILE}"
    path="/${COMMUNITY_CONTEXT}"
    >
    <!-- Configure the community identifier -->
    <Environment
        description="The Community identifier"
        name="org.astrogrid.community.ident"
        type="java.lang.String"
        value="${ASTROGRID_AUTH}"
        />
    <!-- Configure the default FileManager -->
    <Environment
        description="The default FileManager"
        name="org.astrogrid.community.default.vospace"
        type="java.lang.String"
        value="ivo://${ASTROGRID_AUTH}/filemanager"
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
    <!-- Configure the database connector -->
    <Resource
        auth="Container"
        description="Database connection"
        name="jdbc/org.astrogrid.community.database"
        type="javax.sql.DataSource"
        driverClassName="org.hsqldb.jdbcDriver"
        url="jdbc:hsqldb:${ASTROGRID_HOME}/community/data/database"
        username="sa"
        password=""
        />
</Context>
EOF

#
# Ask Tomcat to load the webapp.
echo ""
echo "Starting Community webapp"
echo "  URL : ${ASTROGRID_INTERNAL}/manager/html/deploy"
echo "  CXT : /${COMMUNITY_CONTEXT}"
echo "  XML : file://${ASTROGRID_HOME}/community/webapp/context.xml"
if [ `curl -s \
      --url ${ASTROGRID_INTERNAL}/manager/html/deploy \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --data "deployPath=/${COMMUNITY_CONTEXT}" \
      --data "deployConfig=file://${ASTROGRID_HOME}/community/webapp/context.xml" \
      | grep -c "OK - Deployed application at context path /${COMMUNITY_CONTEXT}"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error starting Community webapp"
    exit 1
fi

#
# Check the Community home page.
echo ""
echo "Checking Community home page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}/"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}/ \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing Community home page"
    exit 1
fi

#
# Check the Community admin page
echo ""
echo "Checking Community admin page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}/admin/index.jsp"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --url ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}/admin/index.jsp \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing Community admin page"
    exit 1
fi

#
# Initialise the database.
echo ""
echo "Initialising database"
echo "  URL  : ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}admin/ResetDB.jsp"
if [ `curl -s -i \
     --url  ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}/admin/ResetDB.jsp \
     --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
     --data "resetdb=true" \
     --data "resetdbsubmit=Initialize DB"  \
     | grep -c "Database \"health\" is OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Failed to initialise the database"
    exit 1
fi

#
# Generate the registration document.
# ** Change this to generate a v1.0 resource **
echo ""
echo "Generating service registration"
cat > ${ASTROGRID_HOME}/community/resource.xml << EOF
<vor:VOResources
    xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"
    xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3"
    xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5"
    xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
	<vor:Resource xsi:type="vr:Service"  updated="${INSTALL_DATE}" status="active">
		<title>Policy manager</title>
		<identifier>ivo://${ASTROGRID_AUTH}/org.astrogrid.community.common.policy.manager.PolicyManager</identifier>
		<curation>
			<publisher>AstroGrid</publisher>
			<contact>
                <name>${ASTROGRID_ADMIN}</name>
                <email>${ASTROGRID_EMAIL}</email>
			</contact>
		</curation>
		<content>
			<subject>PolicyManager</subject>
			<description>AstroGrid policy manager</description>
			<referenceURL>http://www.astrogrid.org</referenceURL>
			<type>BasicData</type>
			<relationship>
				<relationshipType>derived-from</relationshipType>
				<relatedResource ivo-id="ivo://org.astrogrid/CommunityServerKind">Community Main Relation</relatedResource>
			</relationship>
		</content>
		<interface xsi:type="vs:WebService">
			<accessURL use="full">${ASTROGRID_EXTERNAL}/${COMMUNITY_CONTEXT}/services/PolicyManager</accessURL>
		</interface> 
	</vor:Resource>
	<vor:Resource xsi:type="vr:Service"  updated="${INSTALL_DATE}" status="active">
		<title>Policy service</title>
		<identifier>ivo://${ASTROGRID_AUTH}/org.astrogrid.community.common.policy.service.PolicyService</identifier>
		<curation>
			<publisher>AstroGrid</publisher>
			<contact>
                <name>${ASTROGRID_ADMIN}</name>
                <email>${ASTROGRID_EMAIL}</email>
			</contact>
		</curation>
		<content>
			<subject>PolicyService</subject>
			<description>AstroGrid policy service</description>
			<referenceURL>http://www.astrogrid.org</referenceURL>
			<type>BasicData</type>
		</content>
		<interface xsi:type="vs:WebService">
			<accessURL use="full">${ASTROGRID_EXTERNAL}/${COMMUNITY_CONTEXT}/services/PolicyService</accessURL>
		</interface> 
	</vor:Resource>
	<vor:Resource xsi:type="vr:Service"  updated="${INSTALL_DATE}" status="active">
		<title>Security manager</title>
		<identifier>ivo://${ASTROGRID_AUTH}/org.astrogrid.community.common.security.manager.SecurityManager</identifier>
		<curation>
			<publisher>AstroGrid</publisher>
			<contact>
                <name>${ASTROGRID_ADMIN}</name>
                <email>${ASTROGRID_EMAIL}</email>
			</contact>
		</curation>
		<content>
			<subject>SecurityManager</subject>
			<description>AstroGrid security manager</description>
			<referenceURL>http://www.astrogrid.org</referenceURL>
			<type>BasicData</type>
		</content>
		<interface xsi:type="vs:WebService">
			<accessURL use="full">${ASTROGRID_EXTERNAL}/${COMMUNITY_CONTEXT}/services/SecurityManager</accessURL>
		</interface> 
	</vor:Resource>
	<vor:Resource xsi:type="vr:Service"  updated="${INSTALL_DATE}" status="active">
		<title>Security service</title>
		<identifier>ivo://${ASTROGRID_AUTH}/org.astrogrid.community.common.security.service.SecurityService</identifier>
		<curation>
			<publisher>AstroGrid</publisher>
			<contact>
                <name>${ASTROGRID_ADMIN}</name>
                <email>${ASTROGRID_EMAIL}</email>
			</contact>
		</curation>
		<content>
			<subject>SecurityService</subject>
			<description>AstroGrid security service</description>
			<referenceURL>http://www.astrogrid.org</referenceURL>
			<type>BasicData</type>
		</content>
		<interface xsi:type="vs:WebService">
			<accessURL use="full">${ASTROGRID_EXTERNAL}/${COMMUNITY_CONTEXT}/services/SecurityService</accessURL>
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
echo "  XML  : file://${ASTROGRID_HOME}/community/resource.xml"
if [ `curl -s -i \
     --url  ${REGISTRY_ENTRY} \
     --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
     --data "addFromURL=true" \
     --data "uploadFromURL=upload"  \
     --data "docurl=file://${ASTROGRID_HOME}/community/resource.xml" \
     | head -n 10 | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Failed to register the service"
    exit 1
fi

#
# Add test user account.
echo ""
echo "Add test user (Y/n) ?"
read RESPONSE
if [ ${RESPONSE:-y} = "y" ]
then
    TEST_NAME=frog
    TEST_PASS=qwerty
    #
    # Create the user account
    echo ""
    echo "Adding test account"
    echo "  URL  : ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}/admin/AccountAdmin.jsp"
    echo "  Name : ${TEST_NAME}"
    echo "  Pass : ${TEST_PASS}"
    #
    # Create the user account
    if [ `curl -s -i \
        --url  ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}/admin/AccountAdmin.jsp \
        --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
        --data "userLoginName=${TEST_NAME}" \
        --data "userCommonName=Test account" \
        --data "userPassword=${TEST_PASS}" \
        --data "description=Test account" \
        --data "email=${TEST_NAME}@test.astrogrid.org" \
        --data "AddAccount=true" \
        --data "AddAccountSubmit=Add" \
         | grep -c "ivo://${ASTROGRID_AUTH}/${TEST_NAME}"` -ge 1 ]
    then
    	echo "  PASS"
    else
    	echo "  ERROR : Failed to allocate user account"
        #exit 1
    fi
    #
    # Check the user homespace
    if [ `curl -s -i \
        --url  ${ASTROGRID_INTERNAL}/${COMMUNITY_CONTEXT}/admin/AccountAdmin.jsp \
        --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
         | grep -c "ivo://${ASTROGRID_AUTH}/filemanager"` -ge 1 ]
    then
    	echo "  PASS"
    else
    	echo "  ERROR : Failed to allocate user homespace"
        #exit 1
    fi
fi


