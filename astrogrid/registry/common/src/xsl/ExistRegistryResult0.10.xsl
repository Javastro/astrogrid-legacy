<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
   version="1.0"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"
   xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
   xmlns:exist="http://exist.sourceforge.net/NS/exist">

   <xsl:output method="xml" />
   
    <xsl:template match="exist:result">
        <xsl:element name="vor:VOResources">
         <xsl:attribute name="xsi:schemaLocation">
               <xsl:text>http://www.ivoa.net/xml/VOResource/v0.10</xsl:text>
               <xsl:text> http://www.ivoa.net/xml/VOResource/v0.10</xsl:text>
               <xsl:if test="contains(*/@xsi:type,'Organisation')">
                  <xsl:text> http://www.ivoa.net/xml/VOCommunity/v0.3</xsl:text>
                  <xsl:text> http://www.ivoa.net/xml/VOCommunity/v0.3</xsl:text>
               </xsl:if>
               <xsl:if test="contains(*/@xsi:type,'Registry') or 
                             contains(*/@xsi:type,'Authority')">
                  <xsl:text> http://www.ivoa.net/xml/VORegistry/v0.3</xsl:text>
                  <xsl:text> http://www.ivoa.net/xml/VORegistry/v0.3</xsl:text>
               </xsl:if>
               <xsl:if test="contains(*/@xsi:type,'Service') or 
                             contains(*/@xsi:type,'DataCollection')">
                <xsl:text> http://www.ivoa.net/xml/VODataService/v0.5</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VODataService/v0.5</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VOTable/v1.0</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VOTable/v1.0</xsl:text>
               </xsl:if>
               <xsl:if test="contains(*/@xsi:type,'ConeSearch')">
                <xsl:text> http://www.ivoa.net/xml/VODataService/v0.5</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VODataService/v0.5</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VOTable/v1.0</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VOTable/v1.0</xsl:text>
                  <xsl:text> http://www.ivoa.net/xml/ConeSearch/v0.3</xsl:text>
                  <xsl:text> http://www.ivoa.net/xml/ConeSearch/v0.3</xsl:text>
               </xsl:if>
               <xsl:if test="contains(*/@xsi:type,'SimpleImageAccess')">
                <xsl:text> http://www.ivoa.net/xml/VODataService/v0.5</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VODataService/v0.5</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VOTable/v1.0</xsl:text>
                <xsl:text> http://www.ivoa.net/xml/VOTable/v1.0</xsl:text>
                  <xsl:text> http://www.ivoa.net/xml/SIA/v0.7</xsl:text>
                  <xsl:text> http://www.ivoa.net/xml/SIA/v0.7</xsl:text>
               </xsl:if>
         </xsl:attribute>
         <xsl:apply-templates/>
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
</xsl:stylesheet>