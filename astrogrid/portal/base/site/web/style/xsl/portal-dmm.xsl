<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    
    xmlns:agp="http://www.astrogrid.org/portal"
    xmlns:xhtml="http://www.w3.org/1999/xhtml"

    exclude-result-prefixes="agp xhtml">
    
  <xsl:preserve-space elements="*"/>
    
  <xsl:output
      encoding="UTF-8"
      indent="yes"
      omit-xml-declaration="yes"/>
      
  <xsl:param name="ag-css-url" value="default.css"/>
  <xsl:param name="ag-title" value="AstroGrid"/>

  <xsl:template match="/">
    <html>
      <head>
        <title><xsl:value-of select="$ag-title"/></title>
        
        <!--
          Main stylesheet.
          -->
        <xsl:element name="link">
          <xsl:attribute name="href"><xsl:value-of select="$ag-css-url"/></xsl:attribute>
          <xsl:attribute name="rel">stylesheet</xsl:attribute>
          <xsl:attribute name="type">text/css</xsl:attribute>
        </xsl:element>
        
        <!--
          Menu stylesheet.
          -->
        <link>
          <xsl:attribute name="href">/astrogrid-portal/domMenu.css</xsl:attribute>
          <xsl:attribute name="rel">stylesheet</xsl:attribute>
          <xsl:attribute name="type">text/css</xsl:attribute>
        </link>
        
        <script src="/astrogrid-portal/domMenu.js" type="text/javascript">
          null;
        </script>

        <script src="/astrogrid-portal/menu.xml" type="text/javascript">
          null;
        </script>

        <!-- Process <menu/> elements -->
        <xsl:for-each select="//node()[local-name() = 'ag-menu']">
          <xsl:call-template name="ag-menu"/>
        </xsl:for-each>

        <!-- Process <script/> elements -->
        <xsl:for-each select="//node()[local-name() = 'ag-script']">
          <xsl:call-template name="ag-script"/>
        </xsl:for-each>
        
        <script type="text/javascript">
          function ag_onload()
          {
            <xsl:for-each select="//node()[local-name() = 'ag-onload']">
              <xsl:call-template name="ag-onload"/>
            </xsl:for-each>
          }
        </script>

        <!-- Process <link/> elements -->
        <xsl:for-each select="//node()[local-name() = 'ag-link']">
          <xsl:call-template name="ag-link"/>
        </xsl:for-each>
      </head>
      
      <!-- Process onload functions -->
      <body onload="ag_onload();">
        <div id="ag-header">
          <xsl:apply-templates select="//ag-header//ag-div/*"/>
        </div>
        
        <div id="ag-body">
          <div id="main-menu">
            <script type="text/javascript">
              domMenu_activate('main-menu');
            </script>
          </div>

          <xsl:apply-templates select="//ag-body/*"/>
        </div>
        
        <div id="ag-footer">
          <xsl:apply-templates select="//ag-footer//ag-div/*"/>
        </div>
      </body>
    </html>
  </xsl:template>
  
  <!-- Add <menu/> element -->
  <xsl:template name="ag-menu">
    <xsl:element name="script">
      <xsl:attribute name="src">/astrogrid-portal/<xsl:value-of select="@name"/>.dmm</xsl:attribute>
      <xsl:attribute name="type">text/javascript</xsl:attribute>
      null;
    </xsl:element>
  </xsl:template>
  
  <!-- Add <link/> element -->
  <xsl:template name="ag-link">
    <xsl:element name="link">
      <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
      <xsl:attribute name="rel"><xsl:value-of select="@rel"/></xsl:attribute>
      <xsl:attribute name="type"><xsl:value-of select="@type"/></xsl:attribute>
    </xsl:element>
  </xsl:template>

  <!-- Add <script/> element -->
  <xsl:template name="ag-script">
    <xsl:element name="script">
      <xsl:attribute name="type"><xsl:value-of select="@type"/></xsl:attribute>
      <xsl:attribute name="src"><xsl:value-of select="@src"/></xsl:attribute>
      <xsl:apply-templates/>
      null;
    </xsl:element>
  </xsl:template>

  <!-- Add onbody JavaScript calls -->
  <xsl:template name="ag-onload">
    <xsl:value-of select="@function"/>;
  </xsl:template>
  
  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="@*|node()">
    <!-- Output local names for XHTML nodes -->
    <xsl:if test="namespace-uri() = 'http://www.w3.org/1999/xhtml'">
      <xsl:element name="{local-name()}">
        <xsl:apply-templates select="@*|node()"/>
      </xsl:element>
    </xsl:if>
  
    <xsl:if test="namespace-uri() != 'http://www.w3.org/1999/xhtml'">
      <!-- Copy all nodes which do not begin 'ag-' -->
      <xsl:if test="not(starts-with(local-name(), 'ag-'))">
        <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
      </xsl:if>
  
      <!--+
          | Process all subnodes of those begining 'ag-'
          +-->
          
      <!-- Output nothing... already processed -->
      <xsl:if test="local-name() = 'ag-link'">
        <xsl:apply-templates select="node()"/>
      </xsl:if>
  
      <!-- Output nothing... already processed -->
      <xsl:if test="local-name() = 'ag-script'">
        <xsl:apply-templates select="node()"/>
      </xsl:if>
  
      <!-- Replace with standard 'div' tags -->
      <xsl:if test="local-name() = 'ag-div'">
        <div>
          <xsl:apply-templates/>
        </div>
      </xsl:if>
  
      <!-- Activate menu -->
      <xsl:if test="local-name() = 'ag-menu'">
        <div>
          <xsl:attribute name="id"><xsl:value-of select="@name"/></xsl:attribute>
          <script type="text/javascript">
            domMenu_activate('<xsl:value-of select="@name"/>');
          </script>
        </div>
      </xsl:if>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>

