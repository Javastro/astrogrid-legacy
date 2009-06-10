<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:cap="urn:astrogrid:schema:Capabilities"
  xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0">
  
  <xsl:template match="/cap:capabilities">
    <html>
      <head>
        <title>Service capabilities</title>
      </head>
      <body>
        <h1>Service capabilities</h1>
        <xsl:apply-templates/>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="capability">
    <h2><xsl:value-of select="@standardID"/></h2>
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="interface">
    <h3>Interface</h3>
    <dl>
      <dt>Type</dt>
      <dd><xsl:value-of select="@xsi:type"/></dd>
      <dt>Version</dt>
      <dd><xsl:value-of select="@version"/></dd>
      <dt>Role</dt>
      <dd><xsl:value-of select="@role"/></dd>
      <xsl:for-each select="accessURL">
        <dt>Access URL</dt>
        <dd><xsl:value-of select="."/></dd>
      </xsl:for-each>
      <xsl:for-each select="securityMethod">
        <dt>Security method</dt>
        <dd><xsl:value-of select="@standardID"/></dd>
      </xsl:for-each>
      <xsl:for-each select="wsdlURL">
        <dt>WSDL URL</dt>
        <dd><xsl:value-of select="."/></dd>
      </xsl:for-each>
      <dt>HTTP verb</dt>
      <dd><xsl:value-of select="queryType"/></dd>
      <dt>MIME type of HTTP response</dt>
      <dd><xsl:value-of select="resultType"/></dd>
    </dl>
    
  </xsl:template>

</xsl:stylesheet>
