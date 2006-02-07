<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:oai="http://www.openarchives.org/OAI/2.0/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                version="1.0">

   <!-- 
                   exclude-result-prefixes="vr2 vc2 vg2 vs2 cs2 sia2 vor cea2" 
     -  Stylesheet to convert VOResource-v0.10 to VOResource-v0.9
     -  Version 1.0 - Initial Revision
     -    Ramon Williamson, National Center for SuperComputing Applications
     -    July 1, 2004
     -->
   <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

   <xsl:variable name="cr"><xsl:text>
</xsl:text></xsl:variable>

   <!--
     -  the per-level indentation.  Set this to a sequence of spaces.
     -->
   <xsl:param name="indent">
      <xsl:for-each select="/*/*[2]">
         <xsl:call-template name="getindent"/>
      </xsl:for-each>
   </xsl:param>


   <!--
     -  determine the indentation preceding the context element
     -->
   <xsl:template name="getindent">
      <xsl:choose>
         <xsl:when test="contains(
                           preceding-sibling::text()[position()=last()],$cr)">
            <xsl:value-of 
              select="substring-after(
                           preceding-sibling::text()[position()=last()],$cr)"/>
         </xsl:when>
         <xsl:when test="preceding-sibling::text()[position()=last()]">
            <xsl:value-of 
              select="preceding-sibling::text()[position()=last()]"/>
         </xsl:when>
         <xsl:otherwise><xsl:text>    </xsl:text></xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <!--
     -  indent the given number of levels.  The amount of indentation will 
     -  be nlev times the value of the global $indent.
     -  @param nlev   the number of indentations to insert.
     -->
   <xsl:template name="doindent">
      <xsl:param name="nlev" select="2"/>
      <xsl:if test="$nlev &gt; 0">
         <xsl:value-of select="$indent"/>
         <xsl:if test="$nlev &gt; 1">
            <xsl:call-template name="doindent">
               <xsl:with-param name="nlev" select="$nlev - 1"/>
            </xsl:call-template>
         </xsl:if>
      </xsl:if>
   </xsl:template>


   <xsl:template match="oai:OAI-PMH">
     <vor:VOResources xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1">
      <xsl:apply-templates select="*/oai:record/oai:metadata"/>
     </vor:VOResources>

   </xsl:template>
   
   <xsl:template match="oai:metadata">
            <xsl:apply-templates />
   </xsl:template>
   
   <xsl:template match="oai:metadata/child::*">
         <vor:Resource xmlns="http://www.ivoa.net/xml/VOResource/v0.10"
                       xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
                       xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"                
                       xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"                
                       xmlns:vc="http://www.ivoa.net/xml/VOCommunity/v0.2" 
                       xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3" 
                       xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5" 
                       xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.2"
                       xmlns:cs="http://www.ivoa.net/xml/ConeSearch/v0.3" 
                       xmlns:sia="http://www.ivoa.net/xml/SIA/v0.7"
					   xmlns:sn="http://www.ivoa.net/xml/OpenSkyNode/v0.1">
         
            <xsl:call-template name="setResourceAttrs"/>
            <xsl:apply-templates select="@*|node()"/>   
          
          <!--
          <xsl:text>in the child</xsl:text>
          -->
         </vor:Resource>
      </xsl:template>   


<!--
   <xsl:template match="oai:metadata">
      <xsl:text>okay in oai:metadata</xsl:text>
   </xsl:template>



<xsl:template match="oai:metadata/ancestor::*">
         <xsl:apply-templates />
</xsl:template>

<xsl:template match="oai:metadata/child::*">
      <vor:Resource xmlns="http://www.ivoa.net/xml/VOResource/v0.10">         
         <xsl:call-template name="setResourceAttrs"/>
         <xsl:apply-templates select="@*|node()"/>         
      </vor:Resource>
   </xsl:template>   
-->      

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
      <xsl:if test="not(@status)">
         <xsl:attribute name="status">
            <xsl:text>active</xsl:text>
         </xsl:attribute>
      </xsl:if>
   </xsl:template>


</xsl:stylesheet>
