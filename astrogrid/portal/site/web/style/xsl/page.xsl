<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="html"
    
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.w3.org/1999/xhtml http://www.w3.org/2002/08/xhtml/xhtml1-transitional.xsd">
  <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>

  <xsl:template match="/">
    <xsl:element name="html" namespace="http://www.w3.org/1999/xhtml">
      <xsl:attribute name="xsi:schemaLocation">http://www.w3.org/1999/xhtml http://www.w3.org/2002/08/xhtml/xhtml1-transitional.xsd</xsl:attribute>
      <head>
        <link rel="stylesheet" type="text/css" href="main.css"/>
        <title>Page</title>
      </head>
      <body>
        <xsl:apply-templates/>
      </body>
    </xsl:element>
  </xsl:template>
  
  <xsl:template match="page">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="main-menu">
    <div class="ag-main-menu">
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="html:main-content">
    <div id="ag-main-content">
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
