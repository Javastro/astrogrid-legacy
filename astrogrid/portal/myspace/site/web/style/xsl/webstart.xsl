<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml"/>
  
  <xsl:param name="myspace-url"/>
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="application-desc">
    <application-desc>
      <xsl:attribute name="main-class"><xsl:value-of select="@main-class"></xsl:value-of></xsl:attribute>
      <argument><xsl:value-of select="$myspace-url"/></argument>
    </application-desc>
  </xsl:template>

  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
