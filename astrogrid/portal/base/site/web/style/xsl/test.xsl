<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.w3.org/1999/xhtml http://www.w3.org/2002/08/xhtml/xhtml1-transitional.xsd"
-->

  <xsl:output
      doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
      doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
      encoding="UTF-8"
      indent="yes"
      omit-xml-declaration="no"/>
      
  <xsl:param name="css-url"/>
  <xsl:param name="js-url"/>

  <xsl:template match="/">
<!--
    <xsl:element name="html">
      <xsl:attribute name="xsi:schemaLocation">http://www.w3.org/1999/xhtml http://www.w3.org/2002/08/xhtml/xhtml1-transitional.xsd</xsl:attribute>
-->
    <html>
      <head>
        <xsl:element name="link">
          <xsl:attribute name="href">
            <xsl:value-of select="$css-url"/>
          </xsl:attribute>
          <xsl:attribute name="rel">stylesheet</xsl:attribute>
          <xsl:attribute name="type">text/css</xsl:attribute>
        </xsl:element>
        <title>AstroGrid Portal</title>
        
        <xsl:element name="script">
          <xsl:attribute name="src">
            <xsl:value-of select="$js-url"/>
          </xsl:attribute>
          <xsl:attribute name="type">text/javascript</xsl:attribute>
        </xsl:element>
        
        <script type="text/javascript" src="/astrogrid-portal/sticky.js"/>
        
        <style type="text/css">
          #temp
          {
              position:     absolute;
              top:          0;
              left:         20%;
              color:        #ffffff;
              background:   #000000;
              padding:      10px;
              border:       none;
              z-index:      5;
          }
        </style>
      </head>
      
      <body onload="ag_content_panelbar_tab_click('ag-content-panelbar-content-1');init();">
        <div id="ag-main">
          <div id="ag-title-bar">
            <div id="img-logo">
              <img src="/astrogrid-portal/aglogo.png" alt="AstroGrid"/>
            </div>
            <h1 class="ag-main-header">Welcome to AstroGrid</h1>
            <p/>
            <h2 class="ag-main-header">The AstroGrid Portal Page</h2>
          </div>
          
          <p/>
          
          <!--
            Menu.
            -->
          <div>
          <xsl:apply-templates select="page/main-menu"/>
          
          <!--
            Main Content.
            -->
          <xsl:apply-templates select="page/main-content"/>
          </div>

          <div>
            <p/>
          </div>
    
          <div id="ag-footer-bar">
            &#169;AstroGrid 2003.
          </div>
        </div>
      </body>
    </html>
<!--
    </xsl:element>
-->
  </xsl:template>
  
  <xsl:template match="page">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="main-menu">
    <div id="ag-main-menu" style="position:absolute;top:150;">
      <div id="ag-main-menu-header">
        Menu
      </div>

      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="main-content">
    <div id="ag-content-main">
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:if test="not(starts-with(local-name(.), 'ag-'))">
      <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
    </xsl:if>
    <xsl:if test="starts-with(local-name(.), 'ag-')">
      <p>removed</p>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
