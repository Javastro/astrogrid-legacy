<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    
    xmlns:agp="http://www.astrogrid.org/portal/it05">
    
    <xsl:output
        omit-xml-declaration="yes"/>
        
    <xsl:strip-space elements="agp:menu agp:item" />
    
  <xsl:template match="/agp:menu">
  domMenu_data.setItem(
      '<xsl:value-of select="@name"/>',
      new domMenu_Hash(
      <xsl:apply-templates/>));
  </xsl:template>
  
  <xsl:template match="agp:menu">
    <xsl:if test="position() != 1">,</xsl:if>
    <xsl:value-of select="position()"/>, new domMenu_Hash(
        'contents', '<xsl:value-of select="@contents"/>',
        'contentsHover', '<xsl:value-of select="@contents-hover"/>',
        'icon', '<xsl:value-of select="@icon"/>',
        'uri', '<xsl:value-of select="@uri"/>',
        'target', '<xsl:value-of select="@target"/>',
        'statusText', '<xsl:value-of select="@status-text"/>',
    <xsl:apply-templates/>)
  </xsl:template>
  
  <xsl:template match="agp:item">
    <xsl:if test="position() != 1">,</xsl:if>
    <xsl:value-of select="position()"/>, new domMenu_Hash(
        'contents', '<xsl:value-of select="@contents"/>',
        'contentsHover', '<xsl:value-of select="@contents-hover"/>',
        'icon', '<xsl:value-of select="@icon"/>',
        'uri', '<xsl:value-of select="@uri"/>',
        'target', '<xsl:value-of select="@target"/>',
        'statusText', '<xsl:value-of select="@status-text"/>')
  </xsl:template>
</xsl:stylesheet>
