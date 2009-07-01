<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xd="http://maven.apache.org/XDOC/2.0">
  <xsl:output method="xml" omit-xml-declaration="yes"  ></xsl:output>
  <xsl:template match="/">
		<xsl:apply-templates select="xd:document/xd:body/*"/>
	</xsl:template>
   <xsl:template match="xd:section">
   <div class="section">
     <h2><xsl:value-of select="@name"/> </h2>
         <xsl:apply-templates/>
   </div>
   </xsl:template>
   <xsl:template match="xd:subsection">
   <div class="subsection">
     <h3><xsl:value-of select="@name"/> </h3>
         <xsl:apply-templates/>
   </div>
   </xsl:template>
   <xsl:template match="xd:subsubsection">
   <div class="subsubsection">
     <h4><xsl:value-of select="@name"/> </h4>
         <xsl:apply-templates/>
   </div>
   </xsl:template>
   <!-- change local http references to point to .jsp rather than .html + take into account that the doc directory is one level down on webapp-->
   <xsl:template match="xd:a/@href[not (starts-with(., 'http:') )]">
      <xsl:attribute name="href" select="replace(replace(., '\.html', '.jsp'),'^\./','../')"/>
   </xsl:template>
	<!-- standard copy template -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>	
</xsl:stylesheet>