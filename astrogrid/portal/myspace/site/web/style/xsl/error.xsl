<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="xml"/>
  
  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template name="set-errors">
    <ag-onload>
      <xsl:attribute name="function">agSetError('<xsl:value-of select="."/>')</xsl:attribute>
    </ag-onload>
  </xsl:template>
  
  <xsl:template match="node()[local-name() = 'ag-errors']">
    <ag-link href="/astrogrid-portal/mount/myspace/error.css" rel="stylesheet" type="text/css"/>
    <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/error.js"/>
    <xsl:for-each select="//node()[local-name() = 'ag-error']/@field">
      <xsl:call-template name="set-errors"/>
    </xsl:for-each>
    
    <div class="ag-errors">
      <span class="ag-errors-heading">Errors:</span>
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="node()[local-name() = 'ag-error']">
    <div class="ag-error">
      <span class="ag-error-heading">Error:</span><br/><br/>
      
      <xsl:if test="@field">
        <span class="ag-error-field"><a>
          <xsl:attribute name="href">#<xsl:value-of select="@field"/></xsl:attribute>
          field: <xsl:value-of select="@field"/><br/>
        </a></span>
      </xsl:if>
      
      <xsl:if test="@anchor">
        <span class="ag-error-anchor"><a>
          <xsl:attribute name="href">#<xsl:value-of select="@anchor"/></xsl:attribute>
          Go!
        </a></span>
      </xsl:if>
      
      <span class="ag-error-message">message: <xsl:value-of select="normalize-space(./node()[local-name() = 'ag-message'])"/></span>
    </div>
  </xsl:template>
  
  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
