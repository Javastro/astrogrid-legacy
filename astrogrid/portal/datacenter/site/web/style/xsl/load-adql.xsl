<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output
      encoding="UTF-8"
      indent="yes"
      omit-xml-declaration="no"/>
      
  <xsl:param name="adql-document"/>
  <xsl:param name="adql-document-errors"/>
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="textarea[@id='ag-data-query']">
    <xsl:if test="$adql-document-errors">
      <div class="ag-adql-errors">
        <xsl:value-of select="adql-document-errors"/>
      </div>
    </xsl:if>
    
    <xsl:element name="textarea">
      <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
      <xsl:attribute name="cols"><xsl:value-of select="@cols"/></xsl:attribute>
      <xsl:attribute name="rows"><xsl:value-of select="@rows"/></xsl:attribute>
      <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
      
      <xsl:if test="string-length($adql-document) &lt; 1">*** DOCUMENT CONTAINS NO DATA ***</xsl:if>
      <xsl:if test="string-length($adql-document) &gt; 0"><xsl:value-of select="$adql-document"/></xsl:if>
    </xsl:element>
  </xsl:template>

  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
