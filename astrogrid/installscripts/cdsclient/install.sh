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
# CDSClient settings.
APPLICATION_VERSION=2007.1.02
APPLICATION_WARFILE=astrogrid-cea-commandline-${APPLICATION_VERSION}.war
APPLICATION_CONTEXT=astrogrid-vizquery
APPLICATION_DATE=`date "+%Y-%m-%d"`
#
# Set the install date (used in registration documents).
INSTALL_DATE=`date "+%Y-%m-%dT%H:%M:%SZ%z"`

echo ""
echo "Installing CDSClient CEA application"

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
# Create CDSClient directories.
CDSCLIENT_HOME=${ASTROGRID_HOME}/vizquery
if [ -d ${CDSCLIENT_HOME} ]
then
    echo ""
    echo "CDSClient directory already exists"
    echo "  Path : ${CDSCLIENT_HOME}"
	exit 1
fi

echo ""
echo "Creating CDSClient directories"
echo "  Path : ${CDSCLIENT_HOME}"
mkdir ${CDSCLIENT_HOME}
mkdir ${CDSCLIENT_HOME}/temp
mkdir ${CDSCLIENT_HOME}/work
mkdir ${CDSCLIENT_HOME}/records
mkdir ${CDSCLIENT_HOME}/config

mkdir ${CDSCLIENT_HOME}/webapp
mkdir ${CDSCLIENT_HOME}/build

#
# Create the invocation script.
echo ""
echo "Creating invocation script"
cat > ${CDSCLIENT_HOME}/config/invoke.sh << EOF
#!/bin/sh
#
# Create the input file.
cat <<! > infile
-source=\$1
-c=\$2
-c.rm=\$3
-out.max=\$5
-out.add=_r
-out.form=mini
-sort=_r
!
#
# Run the application.
vizquery -mime=\$5 infile  > \$4
EOF
#
# Make it executable.
chmod a+x ${CDSCLIENT_HOME}/config/invoke.sh

#
# Create the application description.
echo ""
echo "Creating CDSClient application description"
cat > ${CDSCLIENT_HOME}/config/app-description.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
<CommandLineExecutionControllerConfig
    xmlns="http://www.astrogrid.org/schema/CEAImplementation/v1"
    xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"
    xmlns:agpd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.astrogrid.org/schema/CEAImplementation/v1 
        http://software.astrogrid.org/schema/cea/CEAImplementation/v1.0/CEAImplementation.xsd"
    >
    <!--+
        | The name here defines how the application is registered.
        +-->
    <Application name="${ASTROGRID_AUTH}/vizquery-application">
    <!--+
        | Define the parameters.
        +-->
    <ceab:Parameters>
        <!--+
            | Source dataset.
            +-->
        <CmdLineParameterDefn name="source" fileRef="false" type="text" commandPosition="1">
            <agpd:UI_Name>source</agpd:UI_Name>
            <agpd:UI_Description>The source dataset, see [http://vizier.u-strasbg.fr/viz-bin/vizHelp?cats/U.htx]</agpd:UI_Description>
            <agpd:DefaultValue>USNOB</agpd:DefaultValue>
        </CmdLineParameterDefn>
        <!--+
            | Target center.
            +-->
        <CmdLineParameterDefn name="target" fileRef="false" type="text" commandPosition="2">
            <agpd:UI_Name>target</agpd:UI_Name>
            <agpd:UI_Description>The target center, see [http://vizier.u-strasbg.fr/viz-bin/vizHelp?3.htx#target]</agpd:UI_Description>
            <agpd:DefaultValue>3C 273</agpd:DefaultValue>
        </CmdLineParameterDefn>
        <!--+
            | The radius.
            +-->
        <CmdLineParameterDefn name="radius" fileRef="false" type="text" commandPosition="3">
            <agpd:UI_Name>radius</agpd:UI_Name>
            <agpd:UI_Description>The radius around the target in arcminutes</agpd:UI_Description>
            <agpd:DefaultValue>1</agpd:DefaultValue>
        </CmdLineParameterDefn>
        <!--+
            | Results destination.
            +-->
        <CmdLineParameterDefn name="results" fileRef="true" type="text" commandPosition="4">
            <agpd:UI_Name>results</agpd:UI_Name>
            <agpd:UI_Description>Where to send the results</agpd:UI_Description>
        </CmdLineParameterDefn>
        <!--+
            | Output format.
            +-->
        <CmdLineParameterDefn name="format" fileRef="false" type="text" commandPosition="5">
            <agpd:UI_Name>format</agpd:UI_Name>
            <agpd:UI_Description>The output format</agpd:UI_Description>
            <agpd:DefaultValue>votable</agpd:DefaultValue>
            <agpd:OptionList>
                <agpd:OptionVal>votable</agpd:OptionVal>
                <agpd:OptionVal>html</agpd:OptionVal>
                <agpd:OptionVal>fits</agpd:OptionVal>
                <agpd:OptionVal>ascii</agpd:OptionVal>
                <agpd:OptionVal>csv</agpd:OptionVal>
                <agpd:OptionVal>tsv</agpd:OptionVal>
                <agpd:OptionVal>xml</agpd:OptionVal>
                <agpd:OptionVal>astrores</agpd:OptionVal>
            </agpd:OptionList>
        </CmdLineParameterDefn>
        <!--+
            | The maximum number of rows.
            +-->
        <CmdLineParameterDefn name="limit" fileRef="false" type="text" commandPosition="5">
            <agpd:UI_Name>limit</agpd:UI_Name>
            <agpd:UI_Description>The maximum number of rows</agpd:UI_Description>
            <agpd:DefaultValue>10000</agpd:DefaultValue>
        </CmdLineParameterDefn>

    </ceab:Parameters>
    <!--+
        | Define the interface.
        +-->
    <Interfaces xmlns="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1">
        <Interface name="vizquery">
            <input>
                <pref ref="source"/>
                <pref ref="target"/>
                <pref ref="radius"/>
                <pref ref="format"/>
                <pref ref="limit"/>
            </input>
            <output>
                <pref ref="results"/>
            </output>
        </Interface>
    </Interfaces>
    <!--+
        | The execution script.
        +-->
    <ExecutionPath>${CDSCLIENT_HOME}/config/invoke.sh</ExecutionPath>
    <!--+
        | Provide a textual description.
        +-->
    <LongName>vizquery</LongName>
    <Description>
        The CDSClient VizQuery tool to query VizieR
    </Description>
  </Application>
</CommandLineExecutionControllerConfig>
EOF

#
# Generate the registration template.
echo ""
echo "Creating CDSClient registration template"
cat > ${CDSCLIENT_HOME}/config/registration-template.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<vor:VOResources xmlns="http://www.ivoa.net/xml/RegistryInterface/v0.1" 
	xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.2" 
	xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1" 
	xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1" 
	xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10" 
	xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1" 
	xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.ivoa.net/xml/RegistryInterface/v0.1 ../../../../workflow-objects/schema/externalSchema/v10/RegistryInterface-v0.1.xsd http://www.ivoa.net/xml/CEAService/v0.2 ../../../../workflow-objects/schema/VOCEA-v0.2.xsd" >
	<vor:Resource xsi:type="cea:CeaApplicationType" xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
		<title>VizieR query tool</title>
		<shortName>vizquery</shortName>
		<identifier>ivo://${ASTROGRID_AUTH}/vizquery-application</identifier>
		<curation>
			<publisher>Astrogrid</publisher>
			<creator>
				<name>CDS</name>
				<logo>http://cdsweb.u-strasbg.fr/Icons/cds_icon.gif</logo>
			</creator>
			<date>${APPLICATION_DATE}</date>
			<version>1.0</version>
			<contact>
				<name>${ASTROGRID_ADMIN}</name>
				<email>${ASTROGRID_EMAIL}</email>
			</contact>
		</curation>
		<content>
			<subject>cdsclient vizquery vizier</subject>
			<description>
                CDSClient VizQuery tool to query VizieR
			</description>
			<referenceURL>http://vizier.u-strasbg.fr/doc/vizquery.htx</referenceURL>
			<type>Other</type>
		</content>
		<cea:ApplicationDefinition>
			<cea:Parameters/>
			<cea:Interfaces>
			   <ceab:Interface name="">
    			   <ceab:input/>
    			   <ceab:output/>
			   </ceab:Interface>
			</cea:Interfaces>
		</cea:ApplicationDefinition>
	</vor:Resource>
	<vor:Resource xsi:type="cea:CeaServiceType" xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
		<title>VizieR query tool</title>
		<shortName>vizquery</shortName>
		<identifier>ivo://${ASTROGRID_AUTH}/vizquery-service</identifier>
		<curation>
			<publisher>AstroGrid</publisher>
			<creator>
				<name>AstroGrid</name>
			</creator>
			<date>${APPLICATION_DATE}</date>
			<version>1.0</version>
			<contact>
				<name>${ASTROGRID_ADMIN}</name>
				<email>${ASTROGRID_EMAIL}</email>
			</contact>
		</curation>
		<content>
			<subject>cdsclient vizquery vizier</subject>
			<description>
                The CDSClient VizQuery tool to query VizieR
            </description>
			<referenceURL>http://www.astrogrid.org/maven/docs/head/applications/</referenceURL>
			<!-- Don't change this; the machines need this to be set to Other. -->
			<type>Other</type>
		</content>
		<interface xsi:type="vs:WebService">
			<vr:accessURL use="base">TemplateEntry</vr:accessURL>
		</interface>
		<cea:ManagedApplications>
			<cea:ApplicationReference>ivo://${ASTROGRID_AUTH}/vizquery-application</cea:ApplicationReference>
		</cea:ManagedApplications>
	</vor:Resource>
</vor:VOResources>
EOF

#
# Check temp directory
if [ ! -d ${ASTROGRID_TEMP} ]
then
	echo ""
    echo "Creating AstroGrid temp directory"
    echo "  Path : ${ASTROGRID_TEMP}"
    mkdir ${ASTROGRID_TEMP}
    chmod a+rwx ${ASTROGRID_TEMP}
fi

#
# Get the CEA war file.
if [ ! -f ${ASTROGRID_TEMP}/${APPLICATION_WARFILE} ]
then
	echo ""
	echo "Downloading CEA war file"
	wget -P ${ASTROGRID_TEMP} ${ASTROGRID_MAVEN}/org.astrogrid/wars/${APPLICATION_WARFILE}
fi

#
# Install the CEA war file.
echo ""
echo "Installing CEA war file"
cp ${ASTROGRID_TEMP}/${APPLICATION_WARFILE} ${CDSCLIENT_HOME}/webapp/

#
# Generate the webapp context.
echo ""
echo "Generating webapp context"
cat > ${CDSCLIENT_HOME}/webapp/context.xml << EOF
<?xml version='1.0' encoding='utf-8'?>
<Context
    displayName="AstroGrid experiment"
    docBase="${CDSCLIENT_HOME}/webapp/${APPLICATION_WARFILE}"
    path="/${APPLICATION_CONTEXT}"
    >
    <!-- Configure the base directory location -->
    <Environment
        description="The application base directory"
        name="cea.base.dir"
        type="java.lang.String"
        value="${CDSCLIENT_HOME}"
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
echo "Starting VizQuery webapp"
echo "  URL : ${ASTROGRID_INTERNAL}/manager/html/deploy"
echo "  CXT : /${APPLICATION_CONTEXT}"
echo "  XML : file://${CDSCLIENT_HOME}/webapp/context.xml"
if [ `curl -s \
      --url ${ASTROGRID_INTERNAL}/manager/html/deploy \
      --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
      --data "deployPath=/${APPLICATION_CONTEXT}" \
      --data "deployConfig=file://${CDSCLIENT_HOME}/webapp/context.xml" \
      | grep -c "OK - Deployed application at context path /${APPLICATION_CONTEXT}"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error starting CDSClient webapp"
    exit 1
fi

#
# Check the CDSClient home page.
echo ""
echo "Checking CEA home page"
echo "  URL  : ${ASTROGRID_INTERNAL}/${APPLICATION_CONTEXT}/"
if [ `curl -s --head \
      --url ${ASTROGRID_INTERNAL}/${APPLICATION_CONTEXT}/ \
      | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Error accessing CEA home page"
    exit 1
fi

#
# Register the service.
# ** No validation for this version of registry **
# ** Server does not return an error code if it fails **
echo ""
echo "Registering service"
echo "  URL  : ${REGISTRY_ENTRY}"
echo "  XML  : ${ASTROGRID_INTERNAL}/${APPLICATION_CONTEXT}/cec-http?method=returnRegistryEntry"
if [ `curl -s -i \
     --url  ${REGISTRY_ENTRY} \
     --user ${ASTROGRID_USER}:${ASTROGRID_PASS} \
     --data "addFromURL=true" \
     --data "uploadFromURL=upload"  \
     --data "docurl=${ASTROGRID_EXTERNAL}/${APPLICATION_CONTEXT}/cec-http?method=returnRegistryEntry" \
     | head -n 10 | grep -c "200 OK"` = 1 ]
then
	echo "  PASS"
else
	echo "  ERROR : Failed to register the service"
    exit 1
fi


