<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output
      doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
      doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
      encoding="UTF-8"
      indent="yes"
      omit-xml-declaration="no"/>
      
<!--
  <xsl:param name="header"/>
  <xsl:param name="footer"/>
  <xsl:param name="menu"/>
-->

  <xsl:template match="/">
    <xsl:element name="html">
      <xsl:attribute name="xsi:schemaLocation">http://www.w3.org/1999/xhtml http://www.w3.org/2002/08/xhtml/xhtml1-transitional.xsd</xsl:attribute>
      
      <head>
      </head>
      
      <body>
<!--
        <xsl:apply-templates>
          <xsl:value-of select="$header"/>
        </xsl:apply-templates>

        <xsl:apply-templates>
          <xsl:value-of select="$menu"/>
        </xsl:apply-templates>
-->

        <xsl:apply-templates/>

<!--
        <xsl:apply-templates>
          <xsl:value-of select="$footer"/>
        </xsl:apply-templates>
-->
      </body>
    </xsl:element>
  </xsl:template>

  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
