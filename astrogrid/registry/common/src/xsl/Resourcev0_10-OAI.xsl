<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
   version="1.0" 
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
   xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10" 
   xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
   xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5" 
   xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3" 
   xmlns:vc="http://www.ivoa.net/xml/VORegistry/v0.3" 
   xmlns:vt="http://www.ivoa.net/xml/VOTable/v0.1" 
   xmlns:cs="http://www.ivoa.net/xml/ConeSearch/v0.3" 
   xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.1" 
   xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"  
   xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"   
   xmlns:sia="http://www.ivoa.net/xml/SIA/v0.7" 
   xmlns:oai="http://www.openarchives.org/OAI/2.0/"
   xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
   xmlns:dc="http://purl.org/dc/elements/1.1/"
   exclude-result-prefixes="vr vs vg vc vt cs cea ceapd ceab sia oai oai_dc dc vor">

   <xsl:output method="xml" />

    <xsl:template match="vor:Resource">
      <xsl:variable name="dtVar">
            <xsl:choose>
               <xsl:when test="@updated">
                  <xsl:value-of select="@updated"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="@created"/>
               </xsl:otherwise>
          </xsl:choose>
      </xsl:variable>
        <xsl:element name="OAIRecord">
         <xsl:element name="oai:record">
            <xsl:element name="oai:header">
               <xsl:element name="oai:identifier">
                  <xsl:value-of select="./vr:identifier"/>
               </xsl:element>
               <xsl:element name="oai:datestamp">
                  <xsl:choose>
                     <xsl:when test="string-length($dtVar) &gt;= 19">
                        <xsl:value-of select="concat(substring($dtVar,1,19),'Z')" />
                     </xsl:when>
                     <xsl:when test="string-length($dtVar) = 10">
                        <xsl:value-of select="concat(substring($dtVar,1,10),'T00:00:00Z')" />
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="$dtVar" />
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
                  <xsl:if test="./vr:title">
                     <xsl:element name="dc:title">
                        <xsl:value-of select="./vr:title"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:if test="./vr:curation/vr:publisher">
                     <xsl:element name="dc:creator">
                        <xsl:value-of select="./vr:curation/vr:publisher"/>
                     </xsl:element>                  
                  </xsl:if>                  
                  <xsl:if test="./vr:curation/vr:creator/vr:name">
                     <xsl:element name="dc:creator">
                        <xsl:value-of select="./vr:curation/vr:creator/vr:name"/>
                     </xsl:element>                  
                  </xsl:if>
                  <xsl:if test="./vr:curation/vr:contributer/vr:name">
                     <xsl:element name="dc:creator">
                        <xsl:value-of select="./vr:curation/vr:creator/vr:name"/>
                     </xsl:element>                  
                  </xsl:if>                  
                  <xsl:if test="./vr:content/vr:description">
                     <xsl:element name="dc:description">
                        <xsl:value-of select="./vr:content/vr:description"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:for-each select="./vr:content/vr:subject">
                     <xsl:element name="dc:subject">
                        <xsl:value-of select="."/>
                     </xsl:element>                  
                  </xsl:for-each>
                  <xsl:for-each select="./vr:type">
                     <xsl:element name="dc:type">
                        <xsl:value-of select="."/>
                     </xsl:element>
                  </xsl:for-each>
                  <xsl:element name="dc:date">
                     <xsl:choose>
                        <xsl:when test="string-length($dtVar) &gt;= 19">
                           <xsl:value-of select="concat(substring($dtVar,1,19),'Z')" />
                        </xsl:when>
                        <xsl:when test="string-length($dtVar) = 10">
                           <xsl:value-of select="concat(substring($dtVar,1,10),'T00:00:00Z')" />
                        </xsl:when>
                        <xsl:otherwise>
                           <xsl:value-of select="$dtVar" />
                        </xsl:otherwise>
                     </xsl:choose>
                  </xsl:element>
                  <xsl:element name="dc:identifier">
                     <xsl:value-of select="./vr:identifier"/>
                  </xsl:element>
                  <xsl:if test="./vr:content/vr:source/@format">
                     <xsl:element name="dc:format">
                        <xsl:value-of select="./vr:content/vr:source/@format"/>
                     </xsl:element>
                  </xsl:if>
                  <xsl:if test="./vr:content/vr:source">
                     <xsl:element name="dc:format">
                        <xsl:value-of select="./vr:content/vr:source"/>
                     </xsl:element>
                  </xsl:if>
               </xsl:element>
               <xsl:element name="vor:Resource" xmlns="http://www.ivoa.net/xml/VOResource/v0.10"
                        xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10" 
                        xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
                        xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5" 
                        xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3" 
                        xmlns:vc="http://www.ivoa.net/xml/VORegistry/v0.3" 
                        xmlns:vt="http://www.ivoa.net/xml/VOTable/v0.1" 
                        xmlns:cs="http://www.ivoa.net/xml/ConeSearch/v0.3" 
                        xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.1" 
                        xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"  
                        xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"   
                        xmlns:sia="http://www.ivoa.net/xml/SIA/v0.7">
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