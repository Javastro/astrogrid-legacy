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
	<!--
	<xsl:template match="vr:Resource">
		<xsl:text>hellovr</xsl:text>
		<xsl:apply-templates/>
	</xsl:template>
	-->
	
    <xsl:template match="@xsi:type">
	    <xsl:choose>
	    	<xsl:when test=". = 'AuthorityType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.registry.AuthorityType</xsl:text>
			    </xsl:attribute>    		
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'RegistryType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.registry.RegistryType</xsl:text>
			    </xsl:attribute>    		
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'DataCollectionType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.dataservice.DataCollectionType</xsl:text>
			    </xsl:attribute>    		
	    	</xsl:when>
	    	<xsl:when test=". = 'ServiceType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.ServiceType</xsl:text>
			    </xsl:attribute>    		
	    	</xsl:when>
	    	<xsl:when test=". = 'ResourceType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.ResourceType</xsl:text>
			    </xsl:attribute>    		
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'SkyServiceType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.dataservice.SkyServiceType</xsl:text>
			    </xsl:attribute>    		
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'TabularSkyServiceType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.dataservice.TabularSkyServiceType</xsl:text>
			    </xsl:attribute>    		
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'OrganisationType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.community.OrganisationType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>
	    	<xsl:when test=". = 'ConeSearchType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.conesearch.ConeSearchType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>
	    	<xsl:when test=". = 'CircleRegionType'">
    			<xsl:attribute name="xsi:type">
					<xsl:text>java:org.astrogrid.registry.beans.resource.dataservice.CircleRegionType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'CoordRangeType'">
    			<xsl:attribute name="xsi:type">
					<xsl:text>java:org.astrogrid.registry.beans.resource.dataservice.CoordRangeType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'RegionType'">
    			<xsl:attribute name="xsi:type">
					<xsl:text>java:org.astrogrid.registry.beans.resource.dataservice.RegionType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'SimpleImageAccessType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.sia.SimpleImageAccessType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'ParamHTTPType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.dataservice.ParamHTTPType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'CeaApplicationType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.cea.CeaApplicationType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'CeaServiceType'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.resource.cea.CeaServiceType</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>
	    	<xsl:when test=". = 'DataCenterParameterDefinition'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.cea.base.DataCenterParameterDefinition</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>	    	
	    	<xsl:when test=". = 'WebServiceParameterDefinition'">
    			<xsl:attribute name="xsi:type">
	    			<xsl:text>java:org.astrogrid.registry.beans.cea.base.WebServiceParameterDefinition</xsl:text>
			    </xsl:attribute>
	    	</xsl:when>
	    	<xsl:otherwise>
	   			<xsl:attribute name="xsi:type">
		    	  <xsl:value-of select="."/>
			    </xsl:attribute>
	    	</xsl:otherwise>
	    </xsl:choose>
    	<!--
    	  <xsl:value-of select="."/>
		-->
    </xsl:template>
    
    <xsl:template match="ceab:CmdLineParameterDefn">
		<xsl:attribute name="xsi:type">
   			<xsl:text>java:org.astrogrid.registry.beans.cea.base.CmdLineParameterDefn</xsl:text>
	    </xsl:attribute>
    </xsl:template>
    
	<xsl:template match="text()|processing-instruction()|comment()">
	  <xsl:value-of select="."/>
	</xsl:template>    
    
    
    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    

</xsl:stylesheet>