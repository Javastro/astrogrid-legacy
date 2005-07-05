<?xml version="1.0" encoding="ISO-8859-1" ?>
<!-- This script will convert the xsi:type elements from the true xml type to the java:org.astrogrid.registry.beans.resource.ServiceType class name that castor uses to unmarshall with the correct type.
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

<xsl:output method="xml" indent="yes" />
<xsl:template match="/">
<xsl:apply-templates />
</xsl:template>
<xsl:template match="@xsi:type">
<xsl:attribute name="type" namespace="http://www.w3.org/2001/XMLSchema-instance">
	<!--Unfortunately this is dependent on the namespace prefixes - do not see a simple way round this at the moment - the template document is fairly static though that this is used on-->
<xsl:choose>
<xsl:when test=". ='cea:CeaApplicationType'">java:org.astrogrid.registry.beans.v10.cea.CeaApplicationType</xsl:when>
<xsl:when test=". ='cea:CeaServiceType'">java:org.astrogrid.registry.beans.v10.cea.CeaServiceType</xsl:when>
<xsl:when test=". ='vs:WebService'">java:org.astrogrid.registry.beans.v10.resource.dataservice.WebService</xsl:when>
		<xsl:otherwise><xsl:value-of select="."/></xsl:otherwise>
</xsl:choose>
</xsl:attribute>
</xsl:template>
<!-- copy everything else -->
<xsl:template match="node()|@*" >
        <xsl:copy> 
                <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
</xsl:template>

</xsl:stylesheet>
