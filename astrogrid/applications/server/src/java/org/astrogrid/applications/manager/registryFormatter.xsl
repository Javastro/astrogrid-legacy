<?xml version="1.0" encoding="UTF-8"?>
<!--
	This stylesheet takes the default registry entry created by the castor marshalling and makes sure that it is in a fomat suitable for the registry.
	-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.1" xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1" xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1" xmlns:vm="http://www.ivoa.net/xml/VOMetadata/v0.1" xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9" xmlns:vt="http://www.ivoa.net/xml/VOTable/v1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="xml" indent="yes" />
	<!-- get rid of the namespace in the xsi:type - do not do this as this is the correct way to declare this 
	<xsl:template match="vr:Resource">
		<xsl:copy>
			<xsl:copy-of select="@*[not(name()='xsi:type')]"/>
			<xsl:variable name="atval" select="@xsi:type"/>
			<xsl:choose>
				<xsl:when test="contains($atval,':')">
					<xsl:attribute name="xsi:type"><xsl:value-of select="substring-after($atval, ':')"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="xsi:type"><xsl:value-of select="$atval"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	-->
	
	<!--get rid of these if they are empty-->
	<xsl:template match="@accept-encodings[string-length(.)=0]"/>
	<xsl:template match="@ sub-type[string-length(.)=0]"/>

	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="node()|@*">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
