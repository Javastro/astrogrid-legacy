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
   xmlns:sia="http://www.ivoa.net/xml/SIA/v0.6">

   <xsl:output method="xml" />
   
    <xsl:template match="vr:Service">
        <xsl:element name="vr:Resource">
            <xsl:attribute name="xsi:type">
                <xsl:text>ServiceType</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="@updated"/>
            <xsl:apply-templates select="@created"/>
            <xsl:apply-templates select="@status"/>
            <xsl:apply-templates/>
        </xsl:element>
        <!-- Process the rest of the elements -->        
    </xsl:template>

    <xsl:template match="vs:SkyService">
        <xsl:element name="vr:Resource">
            <xsl:attribute name="xsi:type">
                <xsl:text>SkyServiceType</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="@updated"/>
            <xsl:apply-templates select="@created"/>
            <xsl:apply-templates select="@status"/>
            <xsl:apply-templates/>
        </xsl:element>
        <!-- Process the rest of the elements -->
    </xsl:template>

    <xsl:template match="vs:TabularSkyService">
        <xsl:element name="vr:Resource">
            <xsl:attribute name="xsi:type">
                <xsl:text>TabularSkyServiceType</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="@updated"/>
            <xsl:apply-templates select="@created"/>
            <xsl:apply-templates select="@status"/>
            <xsl:apply-templates/>
        </xsl:element>
        <!-- Process the rest of the elements -->
    </xsl:template>    

    <xsl:template match="vc:Organisation">
        <xsl:element name="vr:Resource">        
            <xsl:attribute name="xsi:type">
                <xsl:text>OrganisationType</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="@updated"/>
            <xsl:apply-templates select="@created"/>
            <xsl:apply-templates select="@status"/>
            <xsl:apply-templates/>
        </xsl:element>
        <!-- Process the rest of the elements -->
    </xsl:template>

    <xsl:template match="vs:DataCollection">
        <xsl:element name="vr:Resource">
            <xsl:attribute name="xsi:type">
                <xsl:text>DataCollectionType</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="@updated"/>
            <xsl:apply-templates select="@created"/>
            <xsl:apply-templates select="@status"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="vg:Authority">
        <xsl:element name="vr:Resource">
            <xsl:attribute name="xsi:type">
                <xsl:text>AuthorityType</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="@updated"/>
            <xsl:apply-templates select="@created"/>
            <xsl:apply-templates select="@status"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="vg:Registry">
        <xsl:element name="vr:Resource">
            <xsl:attribute name="xsi:type">
                <xsl:text>RegistryType</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates select="@updated"/>
            <xsl:apply-templates select="@created"/>
            <xsl:apply-templates select="@status"/>
            <xsl:apply-templates/>
        </xsl:element>
        <!-- Process the rest of the elements -->
    </xsl:template>


<!--    
    <xsl:template match="cs:ConeSearch">
        <xsl:element name="vr:Capability">
            <xsl:attribute name="xsi:type">
                <xsl:text>ConeSearchType</xsl:text>
            </xsl:attribute>
         <xsl:apply-templates/>            
        </xsl:element>
    </xsl:template>
-->
<!--
    <xsl:template match="vs:ParamHTTP">
        <xsl:element name="vr:Interface">
            <xsl:attribute name="xsi:type">
                <xsl:text>ParamHTTPType</xsl:text>
            </xsl:attribute>
         <xsl:apply-templates/>                        
        </xsl:element>
    </xsl:template>
-->
<!--    
    <xsl:template match="vs:AllSky">
        <xsl:element name="vs:Region">
            <xsl:attribute name="xsi:type">
                <xsl:text>RegionType</xsl:text>
            </xsl:attribute>
         <xsl:apply-templates/>                        
        </xsl:element>
    </xsl:template>
-->
<!--
    <xsl:template match="vs:CoordRange">
        <xsl:element name="vs:Region">
            <xsl:attribute name="xsi:type">
                <xsl:text>CoordRangeType</xsl:text>
            </xsl:attribute>
         <xsl:apply-templates/>                        
        </xsl:element>
    </xsl:template>
-->
<!--
    <xsl:template match="vs:CircleRegion">
        <xsl:element name="vs:Region">
            <xsl:attribute name="xsi:type">
                <xsl:text>CircleRegionType</xsl:text>
            </xsl:attribute>
         <xsl:apply-templates/>                        
        </xsl:element>
    </xsl:template>    
-->
<!--
    <xsl:template match="sia:SimpleImageAccessType">
        <xsl:element name="vr:Capability">
            <xsl:attribute name="xsi:type">
                <xsl:text>SimpleImageAccessType</xsl:text>
            </xsl:attribute>
         <xsl:apply-templates/>                        
        </xsl:element>
    </xsl:template>
-->
    
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>