<?xml version="1.0"?>
<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:log="http://www.astrogrid.org/registry">
  
  <xsl:output indent="yes" method="xml" omit-xml-declaration="yes"/>
  
  <xsl:template match="/log:log-message">
    <key>
      <xsl:apply-templates select="log:sourceAddress"/>
      <xsl:apply-templates select="log:destinationAddress"/>
      <xsl:apply-templates select="log:replyAddress"/>
      <xsl:apply-templates select="log:category"/>
      <xsl:apply-templates select="log:subject"/>
    </key>
  </xsl:template>
  <xsl:template match="log:sourceAddress">
    <sourceAddress><xsl:value-of select="."/></sourceAddress>
  </xsl:template>
  <xsl:template match="log:destinationAddress">
    <destinationAddress><xsl:value-of select="."/></destinationAddress>
  </xsl:template>
  <xsl:template match="log:replyAddress">
    <replyAddress><xsl:value-of select="."/></replyAddress>
  </xsl:template>
  <xsl:template match="log:timestamp"/>
  <xsl:template match="priority"/>
  <xsl:template match="log:category">
    <category><xsl:value-of select="."/></category>
  </xsl:template>
  <xsl:template match="log:subject">
    <subject><xsl:value-of select="."/></subject>
  </xsl:template>
  <xsl:template match="log:paragraph"/>

</xsl:stylesheet>
