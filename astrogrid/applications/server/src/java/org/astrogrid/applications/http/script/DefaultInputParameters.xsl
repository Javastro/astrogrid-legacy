<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
							  xmlns:ns1="http://www.astrogrid.org/schema/AGWorkflow/v1"
							  xmlns:ns2="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
							  xmlns:ns3="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
							  xmlns:ns4="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
							  xmlns="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1">
	<xsl:output method="xml" indent="yes" />
	<xsl:template match="/">
		<xsl:message>matched root</xsl:message>
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="http-app">
		<xsl:message>matched http-app</xsl:message>
		<http-get>
			<xsl:attribute name="url"><xsl:value-of select="WebHttpApplication/URL"/></xsl:attribute>
			<xsl:apply-templates/>
		</http-get>
	</xsl:template>
	<xsl:template match="node()[local-name() = 'tool']">
		<xsl:message>matched tool/etc</xsl:message>
		<xsl:message>namespace-uri: <xsl:value-of select="namespace-uri()"/></xsl:message>
		<xsl:message>copy: <xsl:copy-of select="."/></xsl:message>
		<xsl:message>name: <xsl:copy-of select="name()"/></xsl:message>
		<xsl:message>local-name: <xsl:copy-of select="local-name()"/></xsl:message>
		<parameter>
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:value-of select="value"/>
		</parameter>
	</xsl:template>
</xsl:stylesheet>