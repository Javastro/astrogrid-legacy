<?xml version="1.0"?>
<!--+
	| <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/xsl/build/policy/Attic/wsdd.xsl,v $</cvs:source>
	| <cvs:author>$Author: dave $</cvs:author>
	| <cvs:date>$Date: 2003/09/13 02:18:52 $</cvs:date>
	| <cvs:version>$Revision: 1.2 $</cvs:version>
	| <cvs:log>
	|   $Log: wsdd.xsl,v $
	|   Revision 1.2  2003/09/13 02:18:52  dave
	|   Extended the jConfig configuration code.
	|
	|   Revision 1.1  2003/09/03 06:39:13  dave
	|   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
	|
	| </cvs:log>
	| 
	+-->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:wsdd="http://xml.apache.org/axis/wsdd/"
	>
	<!-- Implementation class from the Ant build -->
	<xsl:param name="impl">missing impl class</xsl:param>
	<xsl:param name="service">missing service name</xsl:param>

	<!-- Match the service classname parameter -->
	<xsl:template match="wsdd:deployment/wsdd:service[@name=$service]/wsdd:parameter[@name='className']">
		<xsl:copy>
			<xsl:attribute name="name">
				<xsl:text>className</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="$impl"/>
			</xsl:attribute>
		</xsl:copy>
	</xsl:template>

	<!-- Default, copy all and apply templates -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
