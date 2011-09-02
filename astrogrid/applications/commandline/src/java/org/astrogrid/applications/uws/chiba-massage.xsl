<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: chiba-massage.xsl,v 1.4 2011/09/02 21:55:53 pah Exp $
Cope with the fact that chiba does not seem to like xsi:type at the moment -->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0"
                xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" 
                xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0" 
                xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0" 
                xmlns:ceab="http://www.ivoa.net/xml/CEA/base/v1.1"               
                xmlns:impl="http://www.astrogrid.org/schema/CEAImplementation/v2.1"
                >
   <xsl:output method="xml" encoding="UTF-8" indent="yes" />
   <xsl:strip-space elements="*"/>
   
   <xsl:template match="/" >
           <xsl:apply-templates />        
   </xsl:template>
   <xsl:template match="ri:Resource">
      <Resource>
        <xsl:apply-templates  />        
      </Resource>
   </xsl:template>
   
   <xsl:template match="@xsi:type">
   <!-- just drop it... -->
   </xsl:template>
   
   
   <!-- copy-all  template  -->
   <xsl:template match="@*|node()|comment()|processing-instruction()" >
      <xsl:copy> 
         <xsl:apply-templates select="@*|node()|comment()|processing-instruction()"  /> 
      </xsl:copy>
   </xsl:template>
    
 
   
</xsl:stylesheet>
<!--
 $Log: chiba-massage.xsl,v $
 Revision 1.4  2011/09/02 21:55:53  pah
 result of merging the 2931 branch

 Revision 1.3.4.1  2009/11/08 12:16:56  pah
 update namespaces

 Revision 1.3  2008/10/09 11:40:10  pah
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
