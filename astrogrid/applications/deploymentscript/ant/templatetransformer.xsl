<?xml version="1.0" encoding="ISO-8859-1" ?>
<!-- this sccript will replace some values in the default templace
-->
<xsl:stylesheet version="1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"	
     xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/"
        xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
        xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
        xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10" 
	xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1" 
	xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5"
	xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.2"
	xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1" 
	xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"
	>
<xsl:param name="REGAUTHORITY">org.astrogrid.localhost</xsl:param>
<xsl:param name="CECNAME">defaultCEC</xsl:param>
<xsl:output method="xml" indent="yes" />
<xsl:template match="/">
<xsl:apply-templates />
</xsl:template>
<xsl:template match="//vor:Resource[@xsi:type='cea:CeaServiceType']/vr:identifier/text()">ivo://<xsl:value-of select="$REGAUTHORITY"/>/<xsl:value-of select="$CECNAME"/></xsl:template>
<!-- copy everything else -->
<xsl:template match="node()|@*" >
        <xsl:copy> 
                <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
</xsl:template>

</xsl:stylesheet>
