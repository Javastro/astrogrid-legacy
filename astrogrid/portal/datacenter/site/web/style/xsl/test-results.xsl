<xsl:stylesheet
    version="1.0"
    xmlns:xsp-request="http://apache.org/cocoon/request/2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="xsp-request">
    
  <xsl:output
      encoding="UTF-8"
      indent="yes"
      omit-xml-declaration="no"/>
      
  <xsl:param name="css-url"/>
  
  <xsl:template match="/">
    <ag-div>
      <ag-link href="/astrogrid-portal/mount/datacenter/test-results.css" rel="stylesheet" type="text/css"/>
      
      <div>
        <xsl:apply-templates select="//xsp-request:attribute"/>
      </div>
    </ag-div>
  </xsl:template>
  
  <xsl:template match="xsp-request:attribute">
    <p>
      <span>name:&#160;<xsl:value-of select="@name"/></span><br/>

      <xsl:if test="not(./xsp-request:value/DatacenterResults)">
        <span>value:&#160;<xsl:value-of select="./xsp-request:value"/></span>
      </xsl:if>
      
      <xsl:if test="./xsp-request:value/DatacenterResults">
        <textarea id="ag-data-results" rows="10" cols="80" readonly="readonly">
          <xsl:copy-of select="./xsp-request:value/DatacenterResults"/>
        </textarea>
      </xsl:if>
      </p>
<!--
      <span>value:&#160; "<xsl:value-of select="./xsp-request:value"/>" </span>
-->
  </xsl:template>
  
  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:if test="not(namespace-uri() = 'http://apache.org/cocoon/request/2.0')">
      <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>

