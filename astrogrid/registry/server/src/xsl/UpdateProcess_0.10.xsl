<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
	version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"
	xmlns="http://www.ivoa.net/xml/VOResource/v0.10"
    xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
	xmlns:exist="http://exist.sourceforge.net/NS/exist">

	<xsl:output method="xml" />
	
    <xsl:template match="vr:Resource">
        <xsl:element name="vor:VOResources">
	        <xsl:element name="vor:Resource">
		        <xsl:call-template name="setResourceAttrs"/>
      			<xsl:apply-templates/>
		    </xsl:element>
        </xsl:element>
    </xsl:template>
	
	<xsl:template match="text()|processing-instruction()|comment()">
	  <xsl:value-of select="."/>
	</xsl:template>    

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

   <!--
     -  add record maintenance attributes to resource element 
     -->
   <xsl:template name="setResourceAttrs">
      <xsl:if test="@xsi:type">
         <xsl:attribute name="xsi:type">
            <xsl:value-of select="@xsi:type"/>
         </xsl:attribute>
      </xsl:if>   
      <xsl:if test="@created">
         <xsl:attribute name="created">
            <xsl:value-of select="@created"/>
         </xsl:attribute>
      </xsl:if>
      <xsl:if test="@updated">
         <xsl:attribute name="updated">
            <xsl:value-of select="@updated"/>
         </xsl:attribute>
      </xsl:if>
      <xsl:if test="@status">
         <xsl:attribute name="status">
            <xsl:value-of select="@status"/>
         </xsl:attribute>
      </xsl:if>
   </xsl:template>

</xsl:stylesheet>