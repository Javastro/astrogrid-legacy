<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
        xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.4"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >


  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
