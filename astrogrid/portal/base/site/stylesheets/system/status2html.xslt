<?xml version="1.0"?>

<!--+
    | Converts output of the StatusGenerator into HTML page
    | 
    | CVS $Id: status2html.xslt,v 1.6 2003/07/31 03:51:18 vgritsenko Exp $
    +-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:status="http://apache.org/cocoon/status/2.0">
 
  <xsl:param name="contextPath" select="string('/cocoon')"/>

  <xsl:template match="status:statusinfo">
    <html>
      <head>
        <title>Cocoon Status [<xsl:value-of select="@status:host"/>]</title>
        <link href="{$contextPath}/styles/main.css" type="text/css" rel="stylesheet"/>
        <script src="{$contextPath}/scripts/main.js" type="text/javascript"/>
      </head>

      <body>
        <h1><xsl:value-of select="@status:host"/> - <xsl:value-of select="@status:date"/></h1>
        <xsl:apply-templates/>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="status:group">
    <h2><xsl:value-of select="@status:name"/></h2>
    <ul><xsl:apply-templates select="status:value"/></ul>
    <xsl:apply-templates select="status:group"/>
  </xsl:template>

  <xsl:template match="status:value">
    <li>
      <span class="description"><xsl:value-of select="@status:name"/><xsl:text>: </xsl:text></span>
      <xsl:choose>
        <xsl:when test="contains(@status:name,'free') or contains(@status:name,'total')">
          <xsl:call-template name="suffix">
            <xsl:with-param name="bytes" select="number(.)"/>
          </xsl:call-template>
        </xsl:when>      
        <xsl:when test="count(status:line) &lt;= 1">
          <xsl:value-of select="status:line"/>
        </xsl:when>
        <xsl:otherwise>
          <span class="switch" id="{generate-id(.)}-switch" onclick="toggle('{generate-id(.)}')">[show]</span>
          <ul id="{generate-id(.)}" style="display: none">
             <xsl:apply-templates />
          </ul>
        </xsl:otherwise>
      </xsl:choose>
    </li>
  </xsl:template>

  <xsl:template match="status:line">
    <li><xsl:value-of select="."/></li>
  </xsl:template>

  <xsl:template name="suffix">
    <xsl:param name="bytes"/>
    <xsl:choose>
      <!-- More than 4 MB (=4194304) -->
      <xsl:when test="$bytes &gt;= 4194304">
        <xsl:value-of select="round($bytes div 10485.76) div 100"/> MB
      </xsl:when>
      <!-- More than 4 KB (=4096) -->
      <xsl:when test="$bytes &gt; 4096">
        <xsl:value-of select="round($bytes div 10.24) div 100"/> KB
      </xsl:when>
      <!-- Less -->
      <xsl:otherwise>
        <xsl:value-of select="$bytes"/> B
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
</xsl:stylesheet>
