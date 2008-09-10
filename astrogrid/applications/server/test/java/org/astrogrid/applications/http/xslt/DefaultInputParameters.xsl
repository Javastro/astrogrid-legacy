<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	                          xmlns:ceas="http://www.ivoa.net/xml/CEAService/v0.1">
	<xsl:output method="xml" indent="yes" />
	<xsl:template match="/">
		<xsl:message>matched root</xsl:message>
		<root>
		<xsl:apply-templates/>
		</root>
	</xsl:template>
	<xsl:template match="http-app">
		<ha>
		<xsl:message>matched http-app</xsl:message>
		<xsl:apply-templates/>
		</ha>
	</xsl:template>
	<xsl:template match="ceas:CeaHttpApplicationType">
		<xsl:message>matched CeaHttpApplicationType</xsl:message>
		<chat>
		<xsl:apply-templates/>
		</chat>
	</xsl:template>
	<xsl:template match="CeaHttpAdapterSetup">
		<setup>
		<xsl:message>matched CeaHttpAdapterSetup</xsl:message>
		<!--xsl:copy-of/-->
		</setup>
	</xsl:template>	
	<xsl:template match="URL">
		<URL>
		<xsl:message>matched URL</xsl:message>
		<xsl:apply-templates/>
		</URL>
	</xsl:template>		
</xsl:stylesheet>