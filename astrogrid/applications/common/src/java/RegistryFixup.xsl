<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: RegistryFixup.xsl,v 1.1 2008/12/16 19:27:45 pah Exp $
Stylesheet to transform any CEA implementation dependent information out of 1.0 registry instances.
These style of instances are created
Paul Harrison pah@jb.man.ac.uk
 -->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" 
                xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0" 
                xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0" 
                xmlns:ceab="http://www.ivoa.net/xml/CEA/base/v1.1"
                xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0"               
                xmlns:ceaimpl="http://www.astrogrid.org/schema/CEAImplementation/v2.0"               
                exclude-result-prefixes="ceaimpl">
   <xsl:output method="xml" encoding="UTF-8" indent="yes" />
   <xsl:strip-space elements="*"/>
   
   <xsl:template match="/" >
    <xsl:apply-templates />
   </xsl:template>
   
   <xsl:template match="ri:Resource">
      <xsl:copy><xsl:attribute name="schemaLocation" namespace="http://www.w3.org/2001/XMLSchema-instance" >http://www.ivoa.net/xml/CEA/v1.0
http://software.astrogrid.org/schema/vo-resource-types/VOCEA/v1.0/VOCEA.xsd</xsl:attribute>
     <xsl:apply-templates select="@*|node()"/>
</xsl:copy>
   </xsl:template>
   
   <xsl:template match="@xsi:type[.='ceaimpl:CeaCmdLineApplicationDefinition']">
 	<!--Unfortunately this is dependent on the namespace prefixes - do not see a simple way round this at the moment  - the template document is fairly static though that this is used on - could use local name-->
   <!-- just drop -->
  </xsl:template>
   <!-- drop the extra attributes from parameter definition -->
    <xsl:template match="applicationDefinition/@instanceClass"/>
    <xsl:template match="parameterDefinition/@xsi:type|parameterDefinition/@localFileName|parameterDefinition/@commandPosition|parameterDefinition/@commandSwitch|parameterDefinition/@fileRef|parameterDefinition/@switchType|parameterDefinition/@isStreamable"/>
    <!-- drop the extra elements -->
    <xsl:template match="executionPath"/>
 
   <!-- copy-all attributes template  -->
   <xsl:template match="@*|node()" >
      <xsl:copy> 
         <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
   </xsl:template>
   
<!--
 $Log: RegistryFixup.xsl,v $
 Revision 1.1  2008/12/16 19:27:45  pah
 RESOLVED - bug 2875: Application registration lacks schemaLocation
 http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2875

 Revision 1.2  2008/09/03 14:19:05  pah
 result of merge of pah_cea_1611 branch

 Revision 1.1.2.3  2008/08/29 07:28:30  pah
 moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

 Revision 1.1.2.2  2008/04/01 13:50:08  pah
 http service also passes unit tests with new jaxb metadata config

 Revision 1.1.2.1  2008/03/27 13:34:36  pah
 now producing correct registry documents


 -->
</xsl:stylesheet>
