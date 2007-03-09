#!/bin/sh
#
# <meta:header>
#   <meta:title>
#     AstroGrid install script.
#   </meta:title>
# </meta:header>
#

#
# DSA settings.
# astrogrid-dsa-catalog-2007.1.01pl.war
#DSAFIRST_VERSION=2007.1.01pl
#DSAFIRST_WARFILE=astrogrid-dsa-catalog-${DSAFIRST_VERSION}.war

DSAFIRST_VERSION=200702281028
DSAFIRST_WARFILE=dsa-test-${DSAFIRST_VERSION}.war

DSAFIRST_CONTEXT=astrogrid-first
DSAFIRST_RESOURCE=first-catalogue

DSAFIRST_CONFIG=first.properties
DSAFIRST_METADOC=first.metadoc.xml

DSAFIRST_LOGIN=jdbc
DSAFIRST_PASS=23rt87p
DSAFIRST_HOST=localhost
DSAFIRST_DATABASE=first
DSAFIRST_TABLE=catalogue
DSAFIRST_RA=POS_EQ_RA
DSAFIRST_DEC=POS_EQ_DEC

MYSQL_VERSION=5.0.4
MYSQL_MIRROR=http://mirrors.dedipower.com/www.mysql.com/Downloads/Connector-J
MYSQL_ZIPFILE=mysql-connector-java-${MYSQL_VERSION}.zip
MYSQL_JARFILE=mysql-connector-java-${MYSQL_VERSION}-bin.jar

#
# Set the install date (used in registration documents).
INSTALL_DATE=`date "+%Y-%m-%dT%H:%M:%SZ%z"`

echo ""
echo "Installing FIRST DSA"

echo ""
echo "  DSAFIRST_VERSION  : ${DSAFIRST_VERSION:?"undefined"}"
echo "  DSAFIRST_CONTEXT  : ${DSAFIRST_CONTEXT:?"undefined"}"
echo "  DSAFIRST_RESOURCE : ${DSAFIRST_RESOURCE:?"undefined"}"

echo "  DSAFIRST_CONFIG   : ${DSAFIRST_CONFIG:?"undefined"}"
echo "  DSAFIRST_METADOC  : ${DSAFIRST_METADOC:?"undefined"}"

echo "  DSAFIRST_LOGIN    : ${DSAFIRST_LOGIN:?"undefined"}"
echo "  DSAFIRST_PASS     : ${DSAFIRST_PASS:?"undefined"}"
echo "  DSAFIRST_HOST     : ${DSAFIRST_HOST:?"undefined"}"
echo "  DSAFIRST_DATABASE : ${DSAFIRST_DATABASE:?"undefined"}"
echo "  DSAFIRST_TABLE    : ${DSAFIRST_TABLE:?"undefined"}"

echo ""
echo "  MYSQL_VERSION     : ${MYSQL_VERSION:?"undefined"}"
echo "  MYSQL_MIRROR      : ${MYSQL_MIRROR:?"undefined"}"
echo "  MYSQL_ZIPFILE     : ${MYSQL_ZIPFILE:?"undefined"}"
echo "  MYSQL_JARFILE     : ${MYSQL_JARFILE:?"undefined"}"

echo ""
echo "  JAVA_HOME         : ${JAVA_HOME:?"undefined"}"
echo "  CATALINA_HOME     : ${CATALINA_HOME:?"undefined"}"

echo ""
echo "  ASTROGRID_HOME    : ${ASTROGRID_HOME:?"undefined"}"
echo "  ASTROGRID_USER    : ${ASTROGRID_USER:?"undefined"}"
echo "  ASTROGRID_PASS    : ${ASTROGRID_PASS:?"undefined"}"

echo "  ASTROGRID_HOST    : ${ASTROGRID_HOST:?"undefined"}"
echo "  ASTROGRID_PORT    : ${ASTROGRID_PORT:?"undefined"}"

echo "  ASTROGRID_AUTH    : ${ASTROGRID_AUTH:?"undefined"}"
echo "  ASTROGRID_EMAIL   : ${ASTROGRID_EMAIL:?"undefined"}"
echo "  ASTROGRID_ADMIN   : ${ASTROGRID_ADMIN:?"undefined"}"

echo "  REGISTRY_ADMIN    : ${REGISTRY_ADMIN:?"undefined"}"
echo "  REGISTRY_QUERY    : ${REGISTRY_QUERY:?"undefined"}"
echo "  REGISTRY_ENTRY    : ${REGISTRY_ENTRY:?"undefined"}"

echo "  INTERNAL_URL      : ${ASTROGRID_INTERNAL:?"undefined"}/${DSAFIRST_CONTEXT:?"undefined"}/"
echo "  EXTERNAL_URL      : ${ASTROGRID_EXTERNAL:?"undefined"}/${DSAFIRST_CONTEXT:?"undefined"}/"

#
# Create the FIRST DSA directories.
if [ -d ${ASTROGRID_HOME}/dsa-first ]
then
    echo ""
    echo "FIRST DSA directory already exists"
    echo "  Path : ${ASTROGRID_HOME}/dsa-first"
	exit 1
fi

echo ""
echo "Creating FIRST DSA directories"
echo "  Path : ${ASTROGRID_HOME}/dsa-first"
mkdir ${ASTROGRID_HOME}/dsa-first
mkdir ${ASTROGRID_HOME}/dsa-first/driver
mkdir ${ASTROGRID_HOME}/dsa-first/webapp

#
# Download the mysql driver.
# http://mirrors.dedipower.com/www.mysql.com/Downloads/Connector-J/mysql-connector-java-5.0.4.tar.gz
pushd ${ASTROGRID_HOME}/dsa-first/driver
    echo ""
    echo "Stopping Tomcat"
    ${CATALINA_HOME}/bin/shutdown.sh
    echo ""
    echo "Downloading MySql database driver"
    echo "  URL : ${MYSQL_MIRROR}/${MYSQL_ZIPFILE}"
	wget ${MYSQL_MIRROR}/${MYSQL_ZIPFILE}
    echo ""
    echo "Extracting MySql database driver"
    echo "  ZIP : ${MYSQL_ZIPFILE}"
    echo "  JAR : ${MYSQL_JARFILE}"
    unzip -j -d ${CATALINA_HOME}/common/lib \
        ${MYSQL_ZIPFILE} \
        mysql-connector-java-${MYSQL_VERSION}/${MYSQL_JARFILE}
    echo ""
    echo "Starting Tomcat"
    ${CATALINA_HOME}/bin/startup.sh
popd

#
# Install the mysql driver.
#

#
# Get the DSA war file.
echo ""
echo "Downloading DSA war file"
pushd ${ASTROGRID_HOME}/dsa-first/webapp
	#wget ${ASTROGRID_MAVEN}/org.astrogrid/wars/${DSAFIRST_WARFILE}
    wget http://ag03.ast.cam.ac.uk:8080/download/${DSAFIRST_WARFILE}
popd

#
# Extract the default properties file.
echo ""
echo "Extracting default properties"
unzip -j -d ${ASTROGRID_HOME}/dsa-first/webapp \
	${ASTROGRID_HOME}/dsa-first/webapp/${DSAFIRST_WARFILE} \
	WEB-INF/classes/default.properties
cp ${ASTROGRID_HOME}/dsa-first/webapp/default.properties ${ASTROGRID_HOME}/dsa-first/webapp/${DSAFIRST_CONFIG}
#
# Patch the properties file.
echo ""
echo "Configuring FIRST DSA properties"
patch -b -l ${ASTROGRID_HOME}/dsa-first/webapp/${DSAFIRST_CONFIG} << EOF
85c85
< datacenter.url=http://localhost:8080/astrogrid-dsa-catalog/
---
> !datacenter.url=http://localhost:8080/astrogrid-dsa-catalog/
90c90
< !datacenter.url=http://{your_full_host_address}:{your_port_number}/{path_to_your_webapp}/
---
> datacenter.url=${ASTROGRID_EXTERNAL}/${DSAFIRST_CONTEXT}/
102c102
< datacenter.metadoc.file=samplestars.metadoc.xml
---
> !datacenter.metadoc.file=samplestars.metadoc.xml
132c132
< !datacenter.metadoc.file={/full/path/to/your.metadoc.xml}
---
> datacenter.metadoc.file=${ASTROGRID_HOME}/dsa-first/webapp/${DSAFIRST_METADOC}
170c170
< datacenter.querier.plugin=org.astrogrid.tableserver.test.SampleStarsPlugin
---
> !datacenter.querier.plugin=org.astrogrid.tableserver.test.SampleStarsPlugin
191c191
< !datacenter.querier.plugin=org.astrogrid.tableserver.jdbc.JdbcPlugin
---
> datacenter.querier.plugin=org.astrogrid.tableserver.jdbc.JdbcPlugin
199c199
< !db.trigfuncs.in.radians=true
---
> db.trigfuncs.in.radians=true
209,210c209,210
< !datacenter.plugin.jdbc.user={jdbc_username}
< !datacenter.plugin.jdbc.password={jdbc_password}
---
> datacenter.plugin.jdbc.user=${DSAFIRST_LOGIN}
> datacenter.plugin.jdbc.password=${DSAFIRST_PASS}
231,232c231,232
< !datacenter.plugin.jdbc.drivers=com.mysql.jdbc.Driver
< !datacenter.plugin.jdbc.url=jdbc:mysql://{hostname}/{databasename}"
---
> datacenter.plugin.jdbc.drivers=com.mysql.jdbc.Driver
> datacenter.plugin.jdbc.url=jdbc:mysql://${DSAFIRST_HOST}/${DSAFIRST_DATABASE}
274c274
< !datacenter.sqlmaker.xslt=MYSQL-4.1.16.xsl
---
> datacenter.sqlmaker.xslt=MYSQL-4.1.16.xsl
338,340c338,340
< datacenter.self-test.table=SampleStars
< datacenter.self-test.column1=RA
< datacenter.self-test.column2=DEC
---
> !datacenter.self-test.table=SampleStars
> !datacenter.self-test.column1=RA
> !datacenter.self-test.column2=DEC
350,352c350,352
< !datacenter.self-test.table={a_table_in_your_database}
< !datacenter.self-test.column1={a_floating_point_column_in_that_table}
< !datacenter.self-test.column2={another_floating_point_column_in_that_table}
---
> datacenter.self-test.table=${DSAFIRST_TABLE}
> datacenter.self-test.column1=${DSAFIRST_RA}
> datacenter.self-test.column2=${DSAFIRST_DEC}
372c372
< default.table=SampleStars
---
> !default.table=SampleStars
375c375
< !default.table={a_table_in_your_database}
---
> default.table=${DSAFIRST_TABLE}
419,422c419,422
< conesearch.table=SampleStars
< conesearch.ra.column=RA
< conesearch.dec.column=DEC
< conesearch.columns.units=deg
---
> !conesearch.table=SampleStars
> !conesearch.ra.column=RA
> !conesearch.dec.column=DEC
> !conesearch.columns.units=deg
429,432c429,432
< !conesearch.table={a_table_in_your_database}
< !conesearch.ra.column={RA_column_in_that_table}
< !conesearch.dec.column={Dec_column_in_that_table}
< !conesearch.columns.units={your_RA_Dec_units}
---
> conesearch.table=${DSAFIRST_TABLE}
> conesearch.ra.column=${DSAFIRST_RA}
> conesearch.dec.column=${DSAFIRST_DEC}
> conesearch.columns.units=deg
438c438
< conesearch.radius.limit=0
---
> conesearch.radius.limit=5.0
455,456c455,456
< datacenter.name={AstroGrid DSA/catalog running test dataset}
< datacenter.description={This is a default (unconfigured) DSA/catalog installation.  It accesses a small HSQLDB database containing fictitious tables of stars and galaxies for testing and demonstration purposes.}
---
> datacenter.name=FIRST object catalogue
> datacenter.description=Test installation of FIRST object catalogue
474,475c474,475
< datacenter.authorityId=astrogrid.org
< datacenter.resourceKey=test-dsa-catalog
---
> !datacenter.authorityId=astrogrid.org
> !datacenter.resourceKey=test-dsa-catalog
479,480c479,480
< !datacenter.authorityId={please.specify.me}
< !datacenter.resourceKey={SpecifyMe}
---
> datacenter.authorityId=${ASTROGRID_AUTH}
> datacenter.resourceKey=${DSAFIRST_RESOURCE}
485,487c485,487
< datacenter.publisher={Organisation making this DSA/catalog available}
< datacenter.contact.name={Name of DSA/catalog administrator}
< datacenter.contact.email={Email address of DSA/catalog administrator}
---
> datacenter.publisher=AstroGrid
> datacenter.contact.name=${ASTROGRID_ADMIN}
> datacenter.contact.email=${ASTROGRID_EMAIL}
492,493c492,493
< datacenter.data.creator.name={Name of organisation/person who created the data}
< datacenter.data.creator.logo={URL of a logo image for the data creator}
---
> datacenter.data.creator.name=The VLA FIRST Survey
> datacenter.data.creator.logo=http://sundog.stsci.edu/icons/first_logo.gif
502c502
< datacenter.reference.url=
---
> datacenter.reference.url=http://sundog.stsci.edu/
557c557
< org.astrogrid.registry.admin.endpoint=http://galahad.star.le.ac.uk:8080/astrogrid-registry/services/RegistryUpdate
---
> org.astrogrid.registry.admin.endpoint=${REGISTRY_ADMIN}
564c564
< org.astrogrid.registry.query.endpoint=http://galahad.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery
---
> org.astrogrid.registry.query.endpoint=${REGISTRY_QUERY}
568c568
< org.astrogrid.registry.query.altendpoint=http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/RegistryQuery
---
> org.astrogrid.registry.query.altendpoint=${REGISTRY_QUERY}
EOF

#
# Download the metadoc file.
echo ""
echo "Downloading FIRST metadoc file"
wget -O ${ASTROGRID_HOME}/dsa-first/webapp/${DSAFIRST_METADOC} http://ag03.ast.cam.ac.uk:8080/download/first-dsa-metadoc.xml

#
# Generate the webapp context.
echo ""
echo "Generating webapp context"
cat > ${ASTROGRID_HOME}/dsa-first/webapp/context.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
<Context
    displayName="AstroGrid FileStore"
    docBase="${ASTROGRID_HOME}/dsa-first/webapp/${DSAFIRST_WARFILE}"
    path="/${DSAFIRST_CONTEXT}"
    >
    <!-- Configure the DSA properties file location -->
    <Environment
        description="Configuration filename"
        name="org.astrogrid.config.filename"
        override="false"
        type="java.lang.String"
        value="${ASTROGRID_HOME}/dsa-first/webapp/${DSAFIRST_CONFIG}"
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
echo "Starting FIRST DSA webapp"
echo "  URL : ${ASTROGRID_INTERNAL}/manager/html/deploy"
echo "  CXT : /${DSAFIRST_CONTEXT}"
echo "  XML : file://${ASTROGRID_HOME}/dsa-first/webapp/context.xml"
if [ `curl -s \
      --url ${ASTROGRID_INTERNAL}/manager/html/deploy \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --data "deployPath=/${DSAFIRST_CONTEXT}" \
      --data "deployConfig=file://${ASTROGRID_HOME}/dsa-first/webapp/context.xml" \
      | grep -c "OK - Deployed application at context path /${DSAFIRST_CONTEXT}"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error starting FIRST DSA webapp"
    exit 1
fi

#
# Check the FIRST DSA home page.
echo ""
echo "Checking FIRST DSA home page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${DSAFIRST_CONTEXT}/"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${DSAFIRST_CONTEXT}/ \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing FIRST DSA home page"
    exit 1
fi

#
# Check the FIRST DSA admin page
echo ""
echo "Checking FIRST DSA admin page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${DSAFIRST_CONTEXT}/admin/index.jsp"
echo "  name : ${ASTROGRID_USER}"
echo "  pass : ${ASTROGRID_PASS}"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${DSAFIRST_CONTEXT}/admin/index.jsp \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing FIRST DSA admin page"
    exit 1
fi

#
# Register the service.
# ** No validation for this version of registry **
# ** Server does not return an error code if it fails **
echo ""
echo "Registering service"
echo "  URL  : ${REGISTRY_ENTRY}"
echo "  XML  : ${ASTROGRID_INTERNAL}/${DSAFIRST_CONTEXT}/GetMetadata"
if [ `curl -s -i \
     --url  ${REGISTRY_ENTRY} \
     --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
     --data "addFromURL=true" \
     --data "uploadFromURL=upload"  \
     --data "docurl=${ASTROGRID_INTERNAL}/${DSAFIRST_CONTEXT}/GetMetadata" \
     | head -n 10 | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Failed to register the service"
    exit 1
fi

######
# Execute as root ?

#
# Install the mysql database driver.

#
# Install the mysql database server.

#
# Download the FIRST database dump.

#
# Install the FIRST database.
