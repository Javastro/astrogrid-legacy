<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:ag="http://www.astrogrid.org"
>
  <xsl:output method="text" omit-xml-declaration="yes" indent="no" />
  

<xsl:template match="/ag:legacy-service">
package <xsl:value-of select="@target-package" />;
import org.astrogrid.datacenter.legacy.LegacyException;
<xsl:if test="count(//ag:parameter[@type='element']|//ag:result[@type='element']) > 0">
<xsl:text>
import org.w3c.dom.Element;
</xsl:text>
</xsl:if>

/** Automatically generated interface to access a legacy web service
*/
public interface <xsl:value-of select="@interface-name" /> {

  <xsl:for-each select="ag:method">
      /** call the legacy web service at <xsl:value-of select="ag:request/@endpoint" /> */
      public <xsl:apply-templates select="ag:result/@type"/><xsl:text> </xsl:text><xsl:value-of select="@name" />(<xsl:apply-templates select="ag:request" /> ) throws LegacyException;
  </xsl:for-each>
  
}
</xsl:template>

<!-- format each parameter tag as 'type name'. Filter out parameter tags that are fixed" -->
<xsl:template match="ag:request" >
    <xsl:for-each select="ag:parameter[not(@fixed) or @fixed != 'true']">
        <xsl:apply-templates select="./@type" /><xsl:text> </xsl:text>
        <xsl:value-of select="@name"/>
        <xsl:if test="position() != last()"><xsl:text>, </xsl:text></xsl:if>
     </xsl:for-each>
</xsl:template>

<!-- convert type attribute to Java type name -->
<xsl:template match="*/@type"  >
<xsl:choose>
    <xsl:when test="normalize-space(.)='string'">String</xsl:when>
    <xsl:when test="normalize-space(.)='int'">int</xsl:when>
    <xsl:when test="normalize-space(.)='boolean'">boolean</xsl:when>
    <xsl:when test="normalize-space(.)='flag'">boolean</xsl:when>    
    <xsl:when test="normalize-space(.)='element'">Element</xsl:when>
    <xsl:when test="normalize-space(.)='bytes'">byte[]</xsl:when>
    <xsl:when test="normalize-space(.)='float'">float</xsl:when>   
    <xsl:when test="normalize-space(.)='void'">void</xsl:when>    
</xsl:choose>
</xsl:template>



</xsl:stylesheet>
