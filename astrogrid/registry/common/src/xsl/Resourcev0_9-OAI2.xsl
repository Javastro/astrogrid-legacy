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
   xmlns:dc="http://purl.org/dc/elements/1.1/">

   <xsl:output method="xml" />

    <xsl:template match="vr:Resource">
        <xsl:element name="OAIRecord">
         <xsl:element name="record" namespace="oai">
            <xsl:element name="header" namespace="oai">
               <xsl:element name="identifier" namespace="oai">
                  <xsl:text>oai:</xsl:text>
                  <xsl:value-of select="./vr:Identifier/vr:AuthorityID"/>
                  <xsl:text>/</xsl:text>
                  <xsl:value-of select="./vr:Identifier/vr:ResourceKey"/>
               </xsl:element>
               <xsl:element name="datestamp" namespace="oai">
                  <xsl:choose>
                     <xsl:when test="@updated">
                        <xsl:value-of select="@updated"/>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="@created"/>
                     </xsl:otherwise>
                  </xsl:choose>
               </xsl:element>
               <xsl:element name="setSpec" namespace="oai">
                  <xsl:text>I</xsl:text>
               </xsl:element>
               <xsl:if test="@status = 'deleted'">
                  <xsl:element name="status" namespace="oai">
                     <xsl:text>deleted</xsl:text>
                  </xsl:element>                  
               </xsl:if>
            </xsl:element>
            <xsl:element name="metadata" namespace="oai">
               <xsl:element name="dc" namespace="oai_dc">
                  <xsl:if test="./Title">
                     <xsl:element name="title" namespace="dc">
                        <xsl:value-of select="./vr:Title"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:if test="./vr:Curation/vr:Publisher/vr:Title">
                     <xsl:element name="creator" namespace="dc">
                        <xsl:value-of select="./vr:Curation/vr:Publisher/vr:Title"/>
                     </xsl:element>                  
                  </xsl:if>                  
                  <xsl:if test="./vr:Curation/vr:Creator/vr:Name">
                     <xsl:element name="creator" namespace="dc">
                        <xsl:value-of select="./vr:Curation/vr:Creator/vr:Name"/>
                     </xsl:element>                  
                  </xsl:if>
                  <xsl:if test="./vr:Curation/vr:Contributer/vr:Name">
                     <xsl:element name="creator" namespace="dc">
                        <xsl:value-of select="./vr:Curation/vr:Creator/vr:Name"/>
                     </xsl:element>                  
                  </xsl:if>                  
                  <xsl:if test="./vr:Summary/vr:Description">
                     <xsl:element name="description" namespace="dc">
                        <xsl:value-of select="./vr:Summary/vr:Description"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:for-each select="./vr:Subject">
                     <xsl:element name="subject" namespace="dc">
                        <xsl:value-of select="."/>
                     </xsl:element>                  
                  </xsl:for-each>
                  <xsl:for-each select="./vr:Type">
                     <xsl:element name="type" namespace="dc">
                        <xsl:value-of select="."/>
                     </xsl:element>
                  </xsl:for-each>
                  <xsl:choose>
                     <xsl:when test="@updated">
                        <xsl:element name="date" namespace="dc">                     
                          <xsl:value-of select="@updated"/>
                        </xsl:element>
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:element name="date" namespace="dc">
                          <xsl:value-of select="@created"/>
                        </xsl:element>
                     </xsl:otherwise>
                  </xsl:choose>                  
                  <xsl:element name="identifier" namespace="dc">
                     <xsl:text>ivo://</xsl:text>
                     <xsl:value-of select="./vr:Identifier/vr:AuthorityID"/>
                     <xsl:text>/</xsl:text>
                     <xsl:value-of select="./vr:Identifier/vr:ResourceKey"/>
                  </xsl:element>
                  <xsl:if test="./vr:Summary/vr:Source/@format">
                     <xsl:element name="format" namespace="dc">
                        <xsl:value-of select="./vr:Summary/vr:Source/@format"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:if test="./vr:Summary/vr:Source">
                     <xsl:element name="format" namespace="dc">
                        <xsl:value-of select="./vr:Summary/vr:Source"/>
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