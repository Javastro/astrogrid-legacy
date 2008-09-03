<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: CEA_UpdateTo1.0.xsl,v 1.2 2008/09/03 14:18:52 pah Exp $
Stylesheet to transform CEA related registry instances from v.10 to v1.0
Paul Harrison pharriso@eso.org.
 N.B this is not yet exhaustively tested - works for instances from the wrapping produced by a v.10 Astrogrid registry only.
 -->
<xsl:stylesheet  
                xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0"
                xmlns:ri.old="http://www.ivoa.net/xml/RegistryInterface/v0.1"
                xmlns:vg="http://www.ivoa.net/xml/VORegistry/v1.0" 
                xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" 
                xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0" 
                xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0" 
                xmlns:ceab="http://www.ivoa.net/xml/CEA/base/v1.1"               
                xmlns:vg.old="http://www.ivoa.net/xml/VORegistry/v0.3" 
                xmlns:vr.old="http://www.ivoa.net/xml/VOResource/v0.10" 
                xmlns:vs.old="http://www.ivoa.net/xml/VODataService/v0.5" 
		        xmlns:cea.old="http://www.ivoa.net/xml/CEAService/v0.2"
                xmlns:ceab.old="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1" 
                xmlns:ceapd.old="http://www.astrogrid.org/schema/AGParameterDefinition/v1" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:impl="http://www.astrogrid.org/schema/CEAImplementation/v2.0"
                exclude-result-prefixes="vr.old  vg.old vs.old cea.old ceab.old ceapd.old xs ri.old" 
                version="2.0">
   <!--   
     -  Stylesheet to convert CEA to CEA 1.0
     $ID:$
     
    -->
   <xsl:output method="xml" encoding="UTF-8" indent="yes" />
   <xsl:strip-space elements="*"/>

   <!--
     -  The prefix to prepend to schema files listed in the xsi:schemaLocation
     -  (if used).  The value should include a trailing slash as necessary.
     -  The default is an empty string, which indicates the current working 
     -  directory (where output is used).  Note that the xsi:schemaLocation 
     -  is only set if it is set on the input.
     -->
   <xsl:param name="schemaLocationPrefix"/>
   <!--
     -  If set, the updated atribute will be set to this value
     -->
   <xsl:param name="today"/>
   <!--
     -  Set to 1 if the xsi:schemaLocation should be set or zero if it should
     -  not be.  If not set at all (default), xsi:schemaLocation is only set 
     -  if it is set on the input.
     -->
   <xsl:param name="setSchemaLocation"/>

   <xsl:variable name="setSL">
      <xsl:choose>
         <xsl:when test="$setSchemaLocation=''">
            <xsl:choose>
               <xsl:when test="/*/@xsi:schemaLocation">
                  <xsl:copy-of select="1"/>
               </xsl:when>
               <xsl:otherwise><xsl:copy-of select="0"/></xsl:otherwise>
            </xsl:choose>
         </xsl:when>
         <xsl:otherwise>
            <xsl:copy-of select="number($setSchemaLocation)"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:variable>

 
   <xsl:template match="/" >
      <ri:VOResources>
        <xsl:attribute name="xsi:schemaLocation">http://www.ivoa.net/xml/RegistryInterface/v1.0 http://www.ivoa.net/xml/RegistryInterface/RegistryInterface-v1.0.xsd</xsl:attribute>
	<xsl:attribute name="from">1</xsl:attribute>
	<xsl:attribute name="more">false</xsl:attribute>
        <xsl:attribute name="numberReturned"><xsl:value-of select="count(/*/ri.old:Resource)"/></xsl:attribute>

           <xsl:apply-templates select="./*/ri.old:Resource|//vr.old:Resource" />        
      </ri:VOResources>
   </xsl:template>
   
   
   <xsl:template match="ri.old:Resource">
       <xsl:element name="ri:Resource">
         <xsl:apply-templates select="@* "/>
         <xsl:if test="not(@created)">
         <xsl:attribute name="created">1999-01-01T00:00:00</xsl:attribute>
         </xsl:if>
      
         <xsl:attribute name="updated">2008-04-30T15:00:00</xsl:attribute><!--<xsl:value-of select="$today"/>-->
       
         
         <xsl:apply-templates select="node()"/>
      </xsl:element> 
      
   </xsl:template>

   <xsl:template match="ri.old:Resource/@xsi:type[.='CeaApplicationType' or 
                                 substring-after(.,':')='CeaApplicationType']">
       <xsl:attribute name="type" namespace="http://www.w3.org/2001/XMLSchema-instance">cea:CeaApplication</xsl:attribute>
   </xsl:template>

   <xsl:template match="ri.old:Resource/@xsi:type[.='CeaServiceType' or 
                                 substring-after(.,':')='CeaServiceType']">
       <xsl:attribute name="type" namespace="http://www.w3.org/2001/XMLSchema-instance">vr:Service</xsl:attribute>
   </xsl:template>
   <xsl:template match="ceab.old:Interface"><!-- check this in original -->
       <xsl:element name="interfaceDefinition" namespace="">
         <xsl:apply-templates select="@* | node()"/>
      </xsl:element>
     
   </xsl:template>
   
     <xsl:template match="ceab.old:Interface/@name">
      <xsl:attribute name="id" >
         <xsl:value-of select="."/>
      </xsl:attribute>
   </xsl:template>
     
  <xsl:template match="cea.old:ParameterDefinition">
      <parameterDefinition>
       <xsl:attribute name="id" >
         <xsl:value-of select="@name"/>
      </xsl:attribute>
      <xsl:apply-templates select="@*[local-name() != 'name']"/>
      <!-- need to bring the units forward -->
      <xsl:apply-templates select="node()"/>
     </parameterDefinition>
    </xsl:template>
  <xsl:template match="cea.old:ParameterDefinition/@type">
     <xsl:attribute name="type">
        <xsl:choose>
           <xsl:when test=". = 'double'">
              <xsl:value-of select="'real'" />
           </xsl:when>
           <xsl:otherwise>
              <xsl:value-of select="." />
           </xsl:otherwise>
        </xsl:choose>
     </xsl:attribute>
  </xsl:template>
  <xsl:template match="cea.old:ParameterDefinition/ceapd.old:UI_Name">
      <xsl:element name="name" namespace="">
         <xsl:apply-templates/>
      </xsl:element>
   </xsl:template>
   <xsl:template match="cea.old:ParameterDefinition/ceapd.old:UI_Description">
      <xsl:element name="description" namespace="">
         <xsl:apply-templates/>
      </xsl:element>
      <!--FIXME reposition units here... what to do if description does not exist? need a better solution really -->
      <xsl:element name="unit">
        <xsl:value-of select="./ceapd.old:Units" />
      </xsl:element>
   </xsl:template>
   <xsl:template match="cea.old:ParameterDefinition/ceapd.old:Units">
   <!-- do nothing becasue units dealt with elsewher -->
   </xsl:template>
   
   <xsl:template match="cea.old:ParameterDefinition/ceapd.old:UCD">
      <xsl:element name="ucd" namespace="">
         <xsl:apply-templates/>
      </xsl:element>
   </xsl:template>
    <xsl:template match="@minoccurs">
      <xsl:attribute name="minOccurs" >
        <xsl:value-of select="."></xsl:value-of>
      </xsl:attribute>
   </xsl:template>
   <xsl:template match="@maxoccurs">
      <xsl:attribute name="maxOccurs">
        <xsl:value-of select="."></xsl:value-of>
      </xsl:attribute>
   </xsl:template>
   <!-- CEA service does not exist as a separate type - a CEA capability is the V1.0 methos of showing this-->
 <xsl:template match="ri.old:Resource[@xsi:type ='cea:CeaServiceType']/vr.old:interface">
   <capability xsi:type="cea:CeaCapability" standardID="ivo://org.astrogrid/std/CEA/v1.0">
     <interface role="std" xsi:type="cea:CECInterface" version="1.0">
       <accessURL>
        <xsl:value-of select="vr.old:accessURL"/>
        </accessURL>
     </interface>
     <managedApplications>
        <xsl:apply-templates select="following-sibling::cea.old:ManagedApplications/cea.old:ApplicationReference" />
     </managedApplications>
   </capability>
  </xsl:template>
   <xsl:template match="ri.old:Resource[@xsi:type ='cea:CeaServiceType']/cea.old:ManagedApplications">
   <!-- ignore because already dealt with in the capability element -->
  </xsl:template>
   
   <!-- here are impl specific changes (not directly relevant to the VOResouce) -->
      <xsl:template match="ExecutionPath"><!-- check this in original -->
       <xsl:element name="executionPath" namespace="">
         <xsl:apply-templates select="@* | node()"/>
      </xsl:element>
    </xsl:template>
      <xsl:template match="impl:CmdLineParameterDefn"><!-- check this in original -->
       <xsl:element name="cmdLineParameterDefn" namespace="">
         <xsl:apply-templates select="@* | node()"/>
      </xsl:element>
    </xsl:template>
      <xsl:template match="impl:LongName"><!-- check this in original -->
       <xsl:element name="longName" namespace="">
         <xsl:apply-templates select="@* | node()"/>
      </xsl:element>
    </xsl:template>
     <xsl:template match="impl:CommandLineExecutionControllerConfig"><!-- check this in original -->
       <xsl:element name="commandLineExecutionControllerConfig" namespace="">
         <xsl:apply-templates select="@* | node()"/>
      </xsl:element>
    </xsl:template>
     <xsl:template match="impl:Application"><!-- check this in original -->
       <xsl:element name="application" namespace="">
         <xsl:apply-templates select="@* | node()"/>
      </xsl:element>
    </xsl:template>
     <xsl:template match="impl:referenceURL"><!-- check this in original -->
       <xsl:element name="ReferenceURL" namespace="">
         <xsl:apply-templates select="@* | node()"/>
      </xsl:element>
    </xsl:template>
     <xsl:template match="impl:LongName"><!-- check this in original -->
       <xsl:element name="longName" namespace="">
         <xsl:apply-templates select="@* | node()"/>
      </xsl:element>
    </xsl:template>
   <!--
     -  Default complex element template.  This converts the copies the source 
     -  element into the output except that it places it into the default
     -  (no name) namespace.
     -->
   <xsl:template match="*">
      <xsl:variable name="elname">
         <xsl:call-template name="uncapitalize">
            <xsl:with-param name="in" select="local-name()"/>
         </xsl:call-template>
      </xsl:variable>
               <xsl:message>
                  <xsl:text>INFO: el=</xsl:text>                
                  <xsl:value-of select="name()"/>
               </xsl:message>
      <xsl:element name="{$elname}">
        <xsl:apply-templates select="@* | node()" />
      </xsl:element>
  </xsl:template>

   
   <!-- copy-all attributes template  -->
   <xsl:template match="@*|comment()|processing-instruction()" >
      <xsl:copy> 
      </xsl:copy>
   </xsl:template>
   <!-- ==========================================================
     -  Utility templates
     -  ========================================================== -->

   <!--
     -  convert the first character to a lower case
     -  @param in  the string to convert
     -->
   <xsl:template name="uncapitalize">
      <xsl:param name="in"/>
      <xsl:value-of select="translate(substring($in,1,1),
                                      'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
                                      'abcdefghijklmnopqrstuvwxyz')"/>
      <xsl:value-of select="substring($in,2)"/>
   </xsl:template>
   
      <!--
     -  convert the first character to an upper case
     -  @param in  the string to convert
     -->
   <xsl:template name="capitalize">
      <xsl:param name="in"/>
      <xsl:value-of select="translate(substring($in,1,1),
                                      'abcdefghijklmnopqrstuvwxyz',
                                      'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
      <xsl:value-of select="substring($in,2)"/>
   </xsl:template>


   
<!--
 $Log: CEA_UpdateTo1.0.xsl,v $
 Revision 1.2  2008/09/03 14:18:52  pah
 result of merge of pah_cea_1611 branch

 Revision 1.1.2.1  2008/05/17 16:40:19  pah
 using latest namespaces

 Revision 1.1.2.4  2008/05/01 15:20:51  pah
 fixed some namespace problems

 Revision 1.1.2.3  2008/04/01 13:50:07  pah
 http service also passes unit tests with new jaxb metadata config

 Revision 1.1.2.2  2008/03/26 17:15:40  pah
 Unit tests pass

 Revision 1.1.2.1  2008/03/19 23:07:53  pah
 script to change registry instance documents to v1.1

 Revision 1.6  2008/02/27 11:22:22  pharriso
 keeping the schema namespace as AG specific

 Revision 1.5  2007/04/23 10:00:03  pharriso
 this is a reasonably complete version that will take v.10 cea instances from AG registry and convert to v1.0

 Revision 1.4  2006/07/21 10:35:08  pharriso
 cleanup of cea and new vospace

 Revision 1.3  2006/07/20 10:58:31  pharriso
 various cleaning up of CEA + everything seemed to get local changes

 Revision 1.2  2006/06/30 08:07:49  pharriso
 updating a few comments

 Revision 1.1  2006/06/14 06:26:02  pharriso
 beginning of a CEA update script...

 -->
</xsl:stylesheet>
