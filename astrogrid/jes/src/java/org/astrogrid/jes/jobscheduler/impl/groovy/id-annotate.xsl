<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns="http://www.astrogrid.org/schema/AGWorkflow/v1"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:wf="http://www.astrogrid.org/schema/AGWorkflow/v1"
	xmlns:cred="http://www.astrogrid.org/schema/Credentials/v1"
	xmlns:agpd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"	
	>
<!-- stylesheet that preserves document, but adds 'id' attribute to each activity node -->

<xsl:template match="node()">
	<xsl:copy>
	<xsl:if test="name() = 'workflow' or name() = 'sequence' or name() = 'flow' or name() = 'step' or name() = 'script' or name() = 'while' or name() = 'for' or name() = 'parfor' or name()  = 'if' or name() = 'Activity'">
	  <xsl:attribute name="id"><xsl:value-of select="generate-id()"/></xsl:attribute>
	</xsl:if>
	<xsl:apply-templates select="@* | node()"/>	
	</xsl:copy>
</xsl:template>

<xsl:template match="@*">
	<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
</xsl:template>


</xsl:stylesheet>
