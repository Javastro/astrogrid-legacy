<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
   version="1.0" 
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
   xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9" 
   xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.4" 
   xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.2" 
   xmlns:vc="http://www.ivoa.net/xml/VOCommunity/v0.2" 
   xmlns:vt="http://www.ivoa.net/xml/VOTable/v0.1" 
   xmlns:cs="http://www.ivoa.net/xml/ConeSearch/v0.2" 
   xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.1" 
   xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"  
   xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"   
   xmlns:sia="http://www.ivoa.net/xml/SIA/v0.6" 
   xmlns:oai="http://www.openarchives.org/OAI/2.0/"
   xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
   xmlns:dc="http://purl.org/dc/elements/1.1/"
   exclude-result-prefixes="vr vs vg vc vt cs cea ceapd ceab sia oai oai_dc dc">

   <xsl:output method="xml" />

    <xsl:template match="vr:Resource">
        <xsl:element name="OAIRecord">
         <xsl:element name="oai:record">
            <xsl:element name="oai:header">
               <xsl:element name="oai:identifier">
                  <xsl:text>ivo_vor://</xsl:text>
                  <xsl:value-of select="./vr:Identifier/vr:AuthorityID"/>
                  <xsl:text>/</xsl:text>
                  <xsl:value-of select="./vr:Identifier/vr:ResourceKey"/>
               </xsl:element>
               <xsl:element name="oai:datestamp">
                  <xsl:choose>
                     <xsl:when test="@updated">
                        <xsl:value-of select="@updated"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="@created"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:element>
               <xsl:element name="oai:setSpec">
                  <xsl:text>I</xsl:text>
               </xsl:element>
               <xsl:if test="@status = 'deleted'">
                  <xsl:element name="oai:status">
                     <xsl:text>deleted</xsl:text>
                  </xsl:element>                  
               </xsl:if>
            </xsl:element>
            <xsl:element name="oai:metadata">
               <xsl:element name="oai_dc:dc">
                  <xsl:if test="./Title">
                     <xsl:element name="dc:title">
                        <xsl:value-of select="./Title"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:if test="./Curation/Publisher/Title">
                     <xsl:element name="dc:creator">
                        <xsl:value-of select="./Curation/Publisher/Title"/>
                     </xsl:element>                  
                  </xsl:if>                  
                  <xsl:if test="./Curation/Creator/Name">
                     <xsl:element name="dc:creator">
                        <xsl:value-of select="./Curation/Creator/Name"/>
                     </xsl:element>                  
                  </xsl:if>
                  <xsl:if test="./Curation/Contributer/Name">
                     <xsl:element name="dc:creator">
                        <xsl:value-of select="./Curation/Creator/Name"/>
                     </xsl:element>                  
                  </xsl:if>                  
                  <xsl:if test="./Summary/Description">
                     <xsl:element name="dc:description">
                        <xsl:value-of select="./Summary/Description"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:for-each select="./Subject">
                     <xsl:element name="dc:subject">
                        <xsl:value-of select="."/>
                     </xsl:element>                  
                  </xsl:for-each>
                  <xsl:for-each select="./Type">
                     <xsl:element name="dc:type">
                        <xsl:value-of select="."/>
                     </xsl:element>
                  </xsl:for-each>
                  <xsl:choose>
                     <xsl:when test="@updated">
                        <xsl:element name="dc:date">
                          <xsl:value-of select="@updated"/>
                        </xsl:element>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:element name="dc:date">
                          <xsl:value-of select="@created"/>
                        </xsl:element>
                     </xsl:otherwise>
                  </xsl:choose>                  
                  <xsl:element name="dc:identifier">
                     <xsl:text>ivo_vor://</xsl:text>
                     <xsl:value-of select="./vr:Identifier/vr:AuthorityID"/>
                     <xsl:text>/</xsl:text>
                     <xsl:value-of select="./vr:Identifier/vr:ResourceKey"/>
                  </xsl:element>
                  <xsl:if test="./Summary/Source/@format">
                     <xsl:element name="dc:format">
                        <xsl:value-of select="./Summary/Source/@format"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:if test="./Summary/Source">
                     <xsl:element name="dc:format">
                        <xsl:value-of select="./Summary/Source"/>
                     </xsl:element>
                  </xsl:if>
               </xsl:element>
               <xsl:element name="vr:Resource">
                  <xsl:apply-templates select="@*|node()"/>
               </xsl:element>
            </xsl:element>
         </xsl:element>
        </xsl:element>
    </xsl:template>    
        
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>