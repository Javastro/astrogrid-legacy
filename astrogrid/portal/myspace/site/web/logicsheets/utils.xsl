<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsp="http://apache.org/xsp"
    xmlns:xsp-request="http://apache.org/xsp/request/2.0"
    xmlns:xsp-session="http://apache.org/xsp/session/2.0"
    xmlns:agp-utils="http://astrogrid.org/xsp/utils/1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output
      method="xml"
      exclude-prefixes="xsp xsp-request xsp-session agp-utils"/>

  <xsl:template match="xsp:page">
    <xsp:page>
      <xsl:apply-templates select="@*"/>
      <xsp:logic>
        private String agp_utils_returnAnyParameter(String paramName, String defaultValue) {
          String result = null;
          
          Session session = null;
          if(request != null) {
            result = request.getParameter(paramName);
            if(result == null || result.length() == 0) {
              session = request.getSession(true);
            }
            else {
              return result;
            }
          }
          
          if(session != null) {
            result = (String) session.getAttribute(paramName);
    
            if(result != null &amp;&amp; result.length() > 0) {
              return result;
            }
          }
          
          return defaultValue;
        }
      </xsp:logic>
      <xsl:apply-templates/>
    </xsp:page>
  </xsl:template>
      
  <xsl:template match="agp-utils:test">
    LogicSheet is Working
  </xsl:template>
  
  <xsl:template match="agp-utils:get-parameter" name="get-parameter">
    <xsp:expr>agp_utils_returnAnyParameter("<xsl:value-of select="@name"/>", "<xsl:value-of select="@default"/>")</xsp:expr>
  </xsl:template>
  
  <xsl:template match="agp-utils:option">
    <xsl:param name="compare-field" select="@compare-field"/>
    <xsl:param name="value" select="@value"/>
    <xsl:param name="label" select="@label"/>
    <xsp:logic> {
      String agp_utils_compare_value = agp_utils_returnAnyParameter("<xsl:value-of select="$compare-field"/>", "");
      String agp_utils_value = "<xsl:value-of select="$value"/>";
      
      if(agp_utils_value != null &amp;&amp; agp_utils_value.length() > 0) {
        if(agp_utils_compare_value != null &amp;&amp;
           agp_utils_compare_value.length() > 0 &amp;&amp;
           agp_utils_value.equals(agp_utils_compare_value)) {
          <xsp:content>
            <xsp:attribute name="selected">true</xsp:attribute>
          </xsp:content>
        }
        <xsp:content>
          <xsp:attribute name="value"><xsp:expr>agp_utils_value</xsp:expr></xsp:attribute>
        </xsp:content>
      }
    } </xsp:logic>
    <option>
      <xsl:value-of select="$label"/>
    </option>
  </xsl:template>
  
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="text()">
    <xsl:value-of select="."/>
  </xsl:template>
</xsl:stylesheet>

