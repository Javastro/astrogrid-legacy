<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="agp xsi html"
    
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns:agp="http://www.astrogrid.org/portal"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.w3.org/1999/xhtml http://www.w3.org/2002/08/xhtml/xhtml1-transitional.xsd">
  <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>
      
  <xsl:param name="css-url"/>
  <xsl:param name="js-url"/>

  <xsl:template match="/">
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
      </head>
      
      <body onload="ag_content_panelbar_tab_click('ag-content-panelbar-content-1');">
        <div id="ag-main">
          <div id="ag-title-bar">
            <div id="img-logo">
              <img src="/astrogrid-portal/aglogo.png" alt="AstroGrid"/>
            </div>
            <h1 class="ag-main-header">Welcome to AstroGrid</h1>
            <p/>
            <h2 class="ag-main-header">The AstroGrid Portal Page</h2>
          </div>
          
          <!--
            Menu.
            -->
          <xsl:apply-templates select="page/main-menu"/>
          
          <div id="ag-content">
            <div id="ag-content-tabs-top">
              <span class="ag-content-tab-top"><a class="ag-content-tab-link" href="">Welcome</a></span>
              <span class="ag-content-tab-top"><a class="ag-content-tab-link" href="">Help</a></span>
            </div>
            
            <!--
              Main Content.
              -->
            <xsl:apply-templates select="page/html:main-content"/>

            <div id="ag-content-tabs-bottom">
              <span class="ag-content-tab-bottom">
                <a class="ag-content-tab-link" href="">Welcome</a>
              </span>
              <span class="ag-content-tab-bottom">
                <a class="ag-content-tab-link" href="">Help</a>
              </span>
            </div>
          </div>

          <p/>
    
          <div id="ag-footer-bar">
            &#169;AstroGrid 2003.
          </div>
        </div>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="page">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="main-menu">
    <div id="ag-main-menu">
      <div id="ag-main-menu-header">
        Menu
      </div>

      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="html:main-content">
    <div id="ag-content-main">
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
