<?xml version="1.0" encoding="utf-8"?>
<!--
	Author: John Taylor
	File: 
	Date: 
	Purpose: Converts a Context.xml into a stylesheet to update a web.xml Huh?
    OK - previously the deployment scripts stuffed a customised context.xml into the war
    that overrode the entries in the web.xml.  To make the wars work with Tomcat 4.1- instead
    we're using a stylesheet to modify the original web.xml.  Not need for a context.xml and
    Bob's your uncle.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" encoding="UTF-8"/>
 	
	<xsl:template match="/Context">
		<xsl:element namespace="http://www.w3.org/1999/XSL/Transform" name="stylesheet">
			<xsl:attribute name="version">1.0</xsl:attribute>
			<xsl:apply-templates/>
			<xsl:element  name="template" namespace="http://www.w3.org/1999/XSL/Transform">
				<xsl:attribute name="match">@*|node()</xsl:attribute>
				<xsl:element name="copy" namespace="http://www.w3.org/1999/XSL/Transform">
					<xsl:element name="apply-templates" namespace="http://www.w3.org/1999/XSL/Transform">
						<xsl:attribute name="select">@*|node()</xsl:attribute>
					</xsl:element>
				</xsl:element>
			</xsl:element>	
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="Environment">
		<xsl:element name="template"  namespace="http://www.w3.org/1999/XSL/Transform">
			<xsl:attribute name="match">env-entry[env-entry-name='<xsl:value-of select="@name"/>']/env-entry-value/text()</xsl:attribute>
			<xsl:value-of select="@value"/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
