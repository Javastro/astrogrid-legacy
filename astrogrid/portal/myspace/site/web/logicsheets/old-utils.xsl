<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsp="http://apache.org/xsp"
    xmlns:xsp-request="http://apache.org/xsp/request/2.0"
    xmlns:xsp-session="http://apache.org/xsp/session/2.0"
    xmlns:agp-utils="http://astrogrid.org/xsp/utils/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="agp-utils:test">
    LogicSheet is Working
  </xsl:template>
  
  <xsl:template match="agp-utils:input">
    <xsl:variable name="param-value">
      <xsl:call-template name="get-parameter">
        <xsl:with-param name="param-name"><xsl:value-of select="@name"/></xsl:with-param>
        <xsl:with-param name="default-value"><xsl:value-of select="@default"/></xsl:with-param>
      </xsl:call-template>
    </xsl:variable>
    <input>
      <xsl:copy-of select="@*[not(name()='value')]"/>
      <xsl:attribute name="value"><xsl:copy-of select="$param-value"/></xsl:attribute>
    </input>
  </xsl:template>
  
  <xsl:template match="agp-utils:get-parameter" name="get-parameter">
    <xsl:param name="param-name" select="@name"/>
    <xsl:param name="default-value" select="@default"/>
    <xsl:variable name="request-value">
      <xsp-request:get-parameter>
        <xsp-request:name><xsl:value-of select="$param-name"/></xsp-request:name>
      </xsp-request:get-parameter>
    </xsl:variable>
    <xsl:variable name="session-value">
      <xsp-session:get-attribute>
        <xsp-session:name><xsl:value-of select="$param-name"/></xsp-session:name>
      </xsp-session:get-attribute>
    </xsl:variable>
    <xsl:variable name="param-value">
      <xsl:choose>
        <xsl:when test="$request-value != ''">
          <xsl:copy-of select="$request-value"/>
        </xsl:when>
        <xsl:when test="$session-value != ''">
          <xsl:copy-of select="$session-value"/>
        </xsl:when>
        <xsl:when test="$default-value != ''">
          <xsl:copy-of select="$default-value"/>
        </xsl:when>
        <xsl:otherwise>none</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:copy-of select="$param-value"/>
  </xsl:template>
  
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
