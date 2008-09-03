<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: CEA_UpdateConfigTo1.0.xsl,v 1.2 2008/09/03 14:18:52 pah Exp $
Stylesheet to transform CEA implementation configuration instances from v.10 to CEA implementation v2.0
Paul Harrison pharriso@eso.org.
 N.B this is not yet exhaustively tested - works for instances from the wrapping produced by a v.10 Astrogrid registry only.
 -->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" 
                xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.1" 
                xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0" 
                xmlns:ceab="http://www.ivoa.net/xml/CEA/base/v1.1"               
                xmlns:vr.old="http://www.ivoa.net/xml/VOResource/v0.10" 
		        xmlns:cea.old="http://www.ivoa.net/xml/CEAService/v0.2"
                xmlns:ceab.old="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1" 
                xmlns:ceapd.old="http://www.astrogrid.org/schema/AGParameterDefinition/v1" 
                xmlns:ceaimp="http://www.astrogrid.org/schema/CEAImplementation/v2.0"
                xmlns:impl.old="http://www.astrogrid.org/schema/CEAImplementation/v1"
                xmlns:ri.old="http://www.ivoa.net/xml/RegistryInterface/v0.1"
                exclude-result-prefixes="cea.old ceab.old ceapd.old impl.old ri.old xs vr.old" 
                >
   <!--   
     -  Stylesheet to convert implementation CEA to CEA 1.0
     $ID:$
     
    -->
   <xsl:output method="xml" encoding="UTF-8" indent="yes" />
   <xsl:strip-space elements="*"/>
  <xsl:param name="regtemplate">registration-template.xml</xsl:param>
 
 
   <xsl:template match="/" >
      <ceaimp:CECConfig>
      <xsl:attribute name="xsi:schemaLocation">http://www.astrogrid.org/schema/CEAImplementation/v2.0 ../../../contracts/src/schema/cea/CEAImplementation/v2.0/CEAImplementation.xsd</xsl:attribute>
           <xsl:apply-templates select="//impl.old:Application" />        
      </ceaimp:CECConfig>
   </xsl:template>
   
   
   <xsl:template match="impl.old:Application">
       <xsl:element name="ceaimp:CeaApplication">
         <xsl:apply-templates select="@*[not(local-name() = ('name', 'instanceClass', 'version'))] "/>
         <xsl:if test="not(@created)">
         <xsl:attribute name="created">1999-01-01T00:00:00</xsl:attribute>
         </xsl:if>
         <xsl:attribute name="status">active</xsl:attribute>
         <xsl:attribute name="updated"><xsl:value-of select="current-dateTime()"></xsl:value-of></xsl:attribute>
         <title><xsl:value-of select="impl.old:LongName"/></title>
         <shortName><xsl:value-of select="impl.old:LongName"/></shortName>
         <identifier><xsl:value-of select="concat('ivo://',@name)"/></identifier>
         <!--TODO  curation should come from the registry template.. -->
          <xsl:call-template name="replaceversion" >
           <xsl:with-param name="cur">
                    <xsl:apply-templates select="document($regtemplate)//ri.old:Resource[ends-with(@xsi:type,'CeaApplicationType')]/vr.old:curation">
         </xsl:apply-templates>
           
            </xsl:with-param>
         </xsl:call-template>
        <content>
            <subject>???</subject>
            <description><xsl:value-of select="impl.old:Description"/></description>
            <referenceURL><xsl:value-of select="impl.old:ReferenceURL"/></referenceURL>
            <type>Other</type><!-- needs changing -->
        </content>
         <xsl:element name="applicationDefinition">
         <xsl:attribute name="xsi:type">ceaimp:CeaCmdLineApplicationDefinition</xsl:attribute>
         <xsl:if test="@instanceClass">
            <xsl:copy-of select="@instanceClass" />
         </xsl:if>
         <applicationType>processing</applicationType>
         
         <xsl:apply-templates select="ceab.old:Parameters|ceab.old:Interfaces"/>
         <xsl:apply-templates select="impl.old:ExecutionPath"/>
         </xsl:element>
         
      </xsl:element> 
      
   </xsl:template>

   <xsl:template name="replaceversion">
   <xsl:param name="cur"></xsl:param>
   <xsl:message>in version</xsl:message>
     <curation>
       <xsl:copy-of select="$cur/curation/version/preceding-sibling::*"/>
       <version>
         <xsl:choose>
           <xsl:when test="@version">
              <xsl:value-of select="@version"/>
           </xsl:when>
           <xsl:otherwise><xsl:value-of select="'0.0001'"/></xsl:otherwise>
         </xsl:choose>
       </version>
       <xsl:copy-of select="$cur/curation/version/following-sibling::*"/>
     </curation>
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
     
  <xsl:template match="impl.old:CmdLineParameterDefn">
      <parameterDefinition>
       <xsl:attribute name="id" >
         <xsl:value-of select="@name"/>
      </xsl:attribute>
      <xsl:attribute name="xsi:type">ceaimp:CommandLineParameterDefinition</xsl:attribute>
      <xsl:apply-templates select="@*[local-name() != 'name']"/>
      <!-- need to bring the units forward -->
      <xsl:apply-templates select="ceapd.old:UI_Name"/>
      <xsl:apply-templates select="ceapd.old:UI_Description"/>
      <xsl:apply-templates select="ceapd.old:Units"/>
      <xsl:call-template name="ucdproc">
         <xsl:with-param name="ucdval" select="ceapd.old:UCD"></xsl:with-param>
      </xsl:call-template>
      <xsl:apply-templates select="node()[not(local-name() = ('UI_Name','UI_Description','Units','UCD' ))]"/>
      
     </parameterDefinition>
  </xsl:template>
  <xsl:template match="impl.old:CmdLineParameterDefn/@type">
     <xsl:attribute name="type">
        <xsl:choose>
           <xsl:when test=". = 'double'">
              <xsl:value-of select="'real'" />
           </xsl:when>
           <xsl:when test=". = 'RA'">
              <xsl:value-of select="'angle'" />
           </xsl:when>
           <xsl:when test=". = 'Dec'">
              <xsl:value-of select="'angle'" />
           </xsl:when>
           <xsl:otherwise>
              <xsl:value-of select="." />
           </xsl:otherwise>
        </xsl:choose>
     </xsl:attribute>
  </xsl:template>
  <xsl:template match="@fileRef">
     <xsl:attribute name="fileRef">
        <xsl:choose>
           <xsl:when test=". = 'true'">
              <xsl:value-of select="'file'" />
           </xsl:when>
           <xsl:otherwise>
              <xsl:value-of select="'no'" />
           </xsl:otherwise>
        </xsl:choose>
     </xsl:attribute>
  </xsl:template>
  <xsl:template match="ceapd.old:UI_Name">
      <xsl:element name="name" namespace="">
         <xsl:apply-templates/>
      </xsl:element>
   </xsl:template>
  <xsl:template match="ceapd.old:Units">
      <xsl:element name="unit" namespace="">
         <xsl:apply-templates/>
      </xsl:element>
   </xsl:template>
    <xsl:template match="ceapd.old:UI_Description">
      <xsl:element name="description" namespace="">
         <xsl:apply-templates/>
      </xsl:element>
    </xsl:template>
   <xsl:template match="cea.old:ParameterDefinition/ceapd.old:Units">
   <!-- do nothing becasue units dealt with elsewher -->
   </xsl:template>
   
   <xsl:template name="ucdproc">
      <xsl:param name="ucdval"></xsl:param>
      <xsl:element name="ucd" namespace="">
        <xsl:choose>
           <xsl:when test="@type = 'RA'">
              <xsl:value-of select="'pos.eq.ra'" />
           </xsl:when>
           <xsl:when test="@type = 'Dec'">
              <xsl:value-of select="'pos.eq.dec'" />
           </xsl:when>
           <xsl:otherwise>
              <xsl:value-of select="$ucdval" />
           </xsl:otherwise>
        </xsl:choose>
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
 $Log: CEA_UpdateConfigTo1.0.xsl,v $
 Revision 1.2  2008/09/03 14:18:52  pah
 result of merge of pah_cea_1611 branch

 Revision 1.1.2.2  2008/09/03 12:11:58  pah
 commit before merge

 Revision 1.1.2.1  2008/05/17 16:40:19  pah
 using latest namespaces

 Revision 1.1.2.3  2008/05/15 13:44:57  pah
 get the curation from the registration template

 Revision 1.1.2.2  2008/03/26 17:29:51  pah
 Unit tests pass

 Revision 1.1.2.1  2008/03/12 18:20:19  pah
 updater for config - does not read information from the template yet


 -->
</xsl:stylesheet>
