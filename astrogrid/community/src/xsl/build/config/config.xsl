<?xml version="1.0"?>
<!--+
	| <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/xsl/build/config/Attic/config.xsl,v $</cvs:source>
	| <cvs:author>$Author: dave $</cvs:author>
	| <cvs:date>$Date: 2003/09/17 19:47:21 $</cvs:date>
	| <cvs:version>$Revision: 1.2 $</cvs:version>
	| <cvs:log>
	|   $Log: config.xsl,v $
	|   Revision 1.2  2003/09/17 19:47:21  dave
	|   1) Fixed classnotfound problems in the build.
	|   2) Added the JUnit task to add the initial accounts and groups.
	|   3) Got the build to work together with the portal.
	|   4) Fixed some bugs in the Account handling.
	|
	|   Revision 1.1  2003/09/13 02:18:52  dave
	|   Extended the jConfig configuration code.
	|
	| </cvs:log>
	|
	+-->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:wsdd="http://xml.apache.org/axis/wsdd/"
	>
	<!-- Config path from the Ant build -->
	<xsl:param name="category">category</xsl:param>
	<xsl:param name="community.name"></xsl:param>
	<xsl:param name="database.name"></xsl:param>
	<xsl:param name="database.config"></xsl:param>
	<xsl:param name="database.mapping"></xsl:param>

	<!-- Match the community.name property -->
	<xsl:template match="/properties/category[@name=$category]/property[@name='community.name']">
		<xsl:copy>
			<xsl:attribute name="name">
				<xsl:text>community.name</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="$community.name"/>
			</xsl:attribute>
		</xsl:copy>
	</xsl:template>

	<!-- Match the database.name property -->
	<xsl:template match="/properties/category[@name=$category]/property[@name='database.name']">
		<xsl:copy>
			<xsl:attribute name="name">
				<xsl:text>database.name</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="$database.name"/>
			</xsl:attribute>
		</xsl:copy>
	</xsl:template>

	<!-- Match the database.config property -->
	<xsl:template match="/properties/category[@name=$category]/property[@name='database.config']">
		<xsl:copy>
			<xsl:attribute name="name">
				<xsl:text>database.config</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="$database.config"/>
			</xsl:attribute>
		</xsl:copy>
	</xsl:template>

	<!-- Match the database.mapping property -->
	<xsl:template match="/properties/category[@name=$category]/property[@name='database.mapping']">
		<xsl:copy>
			<xsl:attribute name="name">
				<xsl:text>database.mapping</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="value">
				<xsl:value-of select="$database.mapping"/>
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
