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

  <xsl:template match="/">
    <html>
      <head>
        <link rel="stylesheet" type="text/css" href="main.css"/>
        <title>AstroGrid Portal</title>
        
        <script type="text/javascript" src="paneltab.js"/>
      </head>
      
      <body onload="ag_content_panelbar_tab_click('ag-content-panelbar-content-1');">
        <div id="ag-main">
          <div id="ag-title-bar">
            <div id="img-logo">
              <img src="../images/aglogo.png" alt="AstroGrid"/>
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
            
            <div id="ag-content-panelbar">
              <div id="ag-content-panelbar-header">
                PanelBar Header
              </div>
              
              <div class="ag-content-panelbar-separator"><hr/></div>
              
              <div class="ag-content-panelbar-tabs">
                <span class="ag-content-panelbar-tab" onclick="ag_content_panelbar_tab_click('ag-content-panelbar-content-1');">
                  tab-1
                </span>
                <span class="ag-content-panelbar-tab" onclick="ag_content_panelbar_tab_click('ag-content-panelbar-content-2');">
                  tab-2
                </span>
                <span class="ag-content-panelbar-tab" onclick="ag_content_panelbar_tab_click('ag-content-panelbar-content-3');">
                  tab-3
                </span>
              </div>
    
              <div id="ag-content-panelbar-content">
                <div id="ag-content-panelbar-content-1" class="ag-content-panelbar-content">
                  PanelBar Content<br/>
                  PanelBar Content<br/>
                  PanelBar Content<br/>
                  PanelBar Content<br/>
                  PanelBar Content<br/>
                  PanelBar Content<br/>
                </div>
                
                <div id="ag-content-panelbar-content-2" class="ag-content-panelbar-content">
                  <select>
                    <option>One</option>
                    <option>Two</option>
                    <option>Three</option>
                  </select>
                  <input type="button" value="insert"/>
               </div>
                
                <div id="ag-content-panelbar-content-3" class="ag-content-panelbar-content">
                  PanelBar Content
                </div>
              </div>
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
