<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    
    exclude-result-prefixes="agp"
    xmlns:agp="http://www.astrogrid.org/portal">
    
  <xsl:output
      encoding="UTF-8"
      indent="yes"
      omit-xml-declaration="no"/>
      
  <xsl:param name="ag-css-url"/>
  <xsl:param name="ag-title"/>

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

        <!-- Process <link/> elements -->
        <xsl:for-each select="//ag-link">
          <xsl:call-template name="ag-link"/>
        </xsl:for-each>

        <!-- Process <script/> elements -->
        <xsl:for-each select="//ag-script">
          <xsl:call-template name="ag-script"/>
        </xsl:for-each>
        
        <script type="text/javascript">
          function ag_onload()
          {
            <xsl:for-each select="//ag-onload">
              <xsl:call-template name="ag-onload"/>
            </xsl:for-each>
            
            null;
          }
        </script>
      </head>
      
      <!-- Process onload functions -->
      <body onload="ag_onload();">
        <div id="ag-header">
          <xsl:apply-templates select="//ag-header//ag-div/*"/>
        </div>
        
        <div id="ag-main">
          <div id="ag-menu">
            <div id="ag-menu-title">
              Menu
            </div>

            <xsl:apply-templates select="//ag-menu"/>
          </div>
          
          <div id="ag-body">
            <xsl:apply-templates select="//ag-body"/>
          </div>
        </div>
        
        <div id="ag-footer">
          <xsl:apply-templates select="//ag-footer//ag-div/*"/>
        </div>
      </body>
    </html>
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
    <!-- Copy all nodes which do not begin 'ag-' -->
    <xsl:if test="not(starts-with(local-name(), 'ag-'))">
      <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
    </xsl:if>
    <!-- Process all subnodes of those begining 'ag-' -->
    <xsl:if test="starts-with(local-name(), 'ag-')">
      <xsl:apply-templates select="node()"/>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>

