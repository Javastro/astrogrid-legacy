<xsl:stylesheet
    version="1.0"
    xmlns="http://www.astrogrid.org/portal"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <!--
  -->
  <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>

  <xsl:param name="menu-name"/>
  
  <xsl:template match="/">
<!--
    <menu>
      <xsl:attribute name="name">fred</xsl:attribute>
      <xsl:apply-templates/>
    </menu>
-->
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="menu">
    <menu>
      <xsl:attribute name="name">bob</xsl:attribute>
      <xsl:apply-templates/>
    </menu>
  </xsl:template>
  
  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
