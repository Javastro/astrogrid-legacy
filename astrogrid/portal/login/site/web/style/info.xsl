<?xml version="1.0"?>
<!--+
    |  $Id: info.xsl,v 1.2 2004/03/19 12:40:09 jdt Exp $
		|  Transforms pages which simply display a message
		|
		+-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="page">
  	<DIV>
		<xsl:apply-templates/>
	</DIV>
  </xsl:template>
    <xsl:template match="title">
      <DIV style="color: lightblue; background-color: blue">
  				<xsl:apply-templates/>
      </DIV>
  </xsl:template>
    <xsl:template match="content">
  		<DIV>
  		    <xsl:apply-templates/>
  		</DIV>
  </xsl:template>
  
	<xsl:template match="@*|node()">
		<xsl:copy>
		   <xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
  </xsl:template>
	<xsl:template match="text()">
		   <xsl:value-of select="."/>
  </xsl:template>
</xsl:stylesheet>		
		
<!--+
    | $log:$
		+-->