<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: chiba-unmassage.xsl,v 1.1 2008/10/09 11:40:10 pah Exp $
Cope with the fact that chiba does not seem to like xsi:type at the moment 
Put xsi:type back into application defintion documents.
-->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0"
                xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" 
                xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0" 
                xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0" 
                xmlns:ceab="http://www.ivoa.net/xml/CEA/base/v1.1"               
                xmlns:ceaimp="http://www.astrogrid.org/schema/CEAImplementation/v2.0"
                >
   <xsl:output method="xml" encoding="UTF-8" indent="yes" />
   <xsl:strip-space elements="*"/>
   
   <xsl:template match="/" >
           <xsl:apply-templates />        
   </xsl:template>
   
   <xsl:template match="applicationDefinition">
    <xsl:copy>
     <xsl:attribute namespace="http://www.w3.org/2001/XMLSchema-instance" name="type" >
     <xsl:value-of select="'ceaimp:CeaCmdLineApplicationDefinition'"/>
     </xsl:attribute>
        <xsl:apply-templates select="@*|node()|comment()|processing-instruction()"/>
    </xsl:copy>   
   </xsl:template>
   
    <xsl:template match="parameterDefinition">
    <xsl:copy>
     <xsl:attribute namespace="http://www.w3.org/2001/XMLSchema-instance" name="type" >
     <xsl:value-of select="'ceaimp:CommandLineParameterDefinition'"/>
     </xsl:attribute>
        <xsl:apply-templates select="@*|node()|comment()|processing-instruction()" />
    </xsl:copy>   
   </xsl:template>
   
   <!-- copy-all  template  -->
   <xsl:template match="@*|node()|comment()|processing-instruction()" >
      <xsl:copy> 
         <xsl:apply-templates select="@*|node()|comment()|processing-instruction()"  /> 
      </xsl:copy>
   </xsl:template>
    
 
   
</xsl:stylesheet>
<!--
 $Log: chiba-unmassage.xsl,v $
 Revision 1.1  2008/10/09 11:40:10  pah
 NEW - bug 2847: simple xform application definition
 http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2847

 Revision 1.2  2008/09/03 14:18:34  pah
 result of merge of pah_cea_1611 branch

 Revision 1.1.2.1  2008/05/13 16:02:47  pah
 uws with full app running UI is working

 Revision 1.1.2.2  2008/03/26 17:29:51  pah
 Unit tests pass

 Revision 1.1.2.1  2008/03/12 18:20:19  pah
 updater for config - does not read information from the template yet


 -->
