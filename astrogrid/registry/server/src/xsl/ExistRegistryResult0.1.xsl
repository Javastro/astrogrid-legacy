<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
   version="1.0"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"
   xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
   xmlns:exist="http://exist.sourceforge.net/NS/exist">

    <xsl:output method="xml" />
    <xsl:strip-space elements="vr:identifier vr:accessURL"/>

    <xsl:param name="schemaLocationBase"/>

	<xsl:template match="/">
        <xsl:element name="vor:VOResources">
         <xsl:attribute name="xsi:schemaLocation">
               <xsl:text>http://www.ivoa.net/xml/VOResource/v0.10 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VOResource/v0.10/VOResource.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/RegistryInterface/v0.1 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>registry/RegistryInterface/v0.1/RegistryInterface.xsd </xsl:text>
         </xsl:attribute>
         <xsl:apply-templates />
        </xsl:element>
	</xsl:template>

    <xsl:template match="AstrogridResource">
        <xsl:element name="vor:VOResources">
         <xsl:attribute name="xsi:schemaLocation">
               <xsl:text>http://www.ivoa.net/xml/VOResource/v0.10 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VOResource/v0.10/VOResource.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/RegistryInterface/v0.1 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>registry/RegistryInterface/v0.1/RegistryInterface.xsd </xsl:text>
         </xsl:attribute>
		<!--
         <xsl:apply-templates select="child::node()" />
		-->
			<xsl:apply-templates />
        </xsl:element>
    </xsl:template>
   

    <xsl:template match="exist:match">
         <xsl:apply-templates />
    </xsl:template>


	<!-- Skip this one because the match on the root element will put the elements correct. -->

    <xsl:template match="exist:result">
			<xsl:apply-templates />
	</xsl:template>


	<xsl:template match="@xsi:schemaLocation">
		<xsl:apply-templates select="@*|node()"/>
   	</xsl:template>
   
	<xsl:template match="@xsi:schemalocation">
		<xsl:apply-templates select="@*|node()"/>
	</xsl:template>

   <xsl:template match="@xsi:type">
	<xsl:attribute name="xsi:type">
         <xsl:value-of select="." />
	</xsl:attribute>
	<xsl:attribute name="xsi:schemaLocation">
	<xsl:if test="contains(.,'Service') or 
                  contains(.,'DataCollection') or
                  contains(.,'TabularSkyService') or 
                  contains(.,'SkyService')">	
			<xsl:text>http://www.ivoa.net/xml/VODataService/v0.5 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VODataService/v0.5/VODataService.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/VOTable/v1.0 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-formats/VOTable/v1.0/VOTable.xsd </xsl:text>
	</xsl:if>
	<xsl:if test="contains(.,'Registry') or 
                  contains(.,'Authority')">
		<xsl:text> http://www.ivoa.net/xml/VORegistry/v0.3 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VORegistry/v0.3/VORegistry.xsd </xsl:text>
	</xsl:if>
	<xsl:if test="contains(.,'ConeSearch')">
		<xsl:text>http://www.ivoa.net/xml/VODataService/v0.5 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VODataService/v0.5/VODataService.xsd </xsl:text><xsl:text> http://www.ivoa.net/xml/VOTable/v1.0 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-formats/VOTable/v1.0/VOTable.xsd </xsl:text><xsl:text> http://www.ivoa.net/xml/ConeSearch/v0.3 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/ConeSearch/v0.3/ConeSearch.xsd </xsl:text>
	</xsl:if>
	<xsl:if test="contains(.,'SimpleImageAccess')">
		<xsl:text>http://www.ivoa.net/xml/VODataService/v0.5 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VODataService/v0.5/VODataService.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/VOTable/v1.0 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-formats/VOTable/v1.0/VOTable.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/SIA/v0.7 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/SIA/v0.7/SIA.xsd </xsl:text>
	</xsl:if>
	<xsl:if test="contains(.,'TabularDB')">
		<xsl:text>http://www.ivoa.net/xml/VODataService/v0.5 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VODataService/v0.5/VODataService.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/VOTable/v1.0 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-formats/VOTable/v1.0/VOTable.xsd </xsl:text><xsl:text>urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/TabularDB/v0.3/TabularDB.xsd </xsl:text>
	</xsl:if>
	<xsl:if test="contains(.,'OpenSkyNode')">
		<xsl:text>http://www.ivoa.net/xml/VODataService/v0.5 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VODataService/v0.5/VODataService.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/VOTable/v1.0 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-formats/VOTable/v1.0/VOTable.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/OpenSkyNode/v0.1 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/OpenSkyNode/v0.1/OpenSkyNode.xsd </xsl:text>
	</xsl:if>
	<xsl:if test="contains(.,'CeaService') or
                  contains(.,'CeaHttp') or
                  contains(.,'CeaApp')">
		<xsl:text>http://www.ivoa.net/xml/VODataService/v0.5 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/VODataService/v0.5/VODataService.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/VOTable/v1.0 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-formats/VOTable/v1.0/VOTable.xsd </xsl:text><xsl:text>http://www.ivoa.net/xml/CEAService/v0.2 </xsl:text><xsl:value-of select="$schemaLocationBase"/><xsl:text>vo-resource-types/CEAService/v0.2/CEAService.xsd </xsl:text>
	</xsl:if>
	</xsl:attribute>
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