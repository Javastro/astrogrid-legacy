<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"

    exclude-result-prefixes="agp"
    xmlns:agp="http://www.astrogrid.org/portal">

  <!--
    Output indented XML, version 1.0
    -->
  <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>
      
  <!--
    Create a wrapping block.
    -->
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <!--
    Display the menu, its items and any submenus, in a block.
    -->
  <xsl:template match="agp:menu">
    <div class="ag-menu-content">
      <xsl:call-template name="display-item"/>
  
      <xsl:apply-templates select="agp:item"/>
      <xsl:apply-templates select="agp:menu"/>
    </div>
  </xsl:template>
  
  <!--
    Display an item in an inline element.
    -->
  <xsl:template match="agp:item">
    <div class="ag-menu-item">
      <xsl:call-template name="display-item"/>
    </div>
  </xsl:template>
  
  <!--
    Display text or hyperlink depending on the availability of the <link/>.
    -->
  <xsl:template name="display-item">
    <div class="ag-menu-display">
      <xsl:if test="current()/agp:link">
        <xsl:element name="a">
          <xsl:attribute name="class">ag-menu-link</xsl:attribute>
          
          <xsl:attribute name="href">
            <xsl:value-of select="current()/agp:link"/>
          </xsl:attribute>
  
          <xsl:value-of select="current()/@display"/>
        </xsl:element>
        <br/>
      </xsl:if>

      <xsl:if test="not(current()/agp:link)">
        <xsl:value-of select="current()/@display"/>
        <br/>
      </xsl:if>
    </div>
  </xsl:template>
  
  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
