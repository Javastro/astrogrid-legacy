<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
	version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
    xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
	xmlns:exist="http://exist.sourceforge.net/NS/exist">

	<xsl:output method="xml" />
	
    <xsl:template match="exist:result">
        <xsl:element name="vor:VOResources">
      		<xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
	
	<xsl:template match="text()|processing-instruction()|comment()">
	  <xsl:value-of select="."/>
	</xsl:template>    

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>