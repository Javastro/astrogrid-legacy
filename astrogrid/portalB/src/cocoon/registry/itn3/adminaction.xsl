<?xml version="1.0"?>
<xsl:stylesheet 
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


<xsl:output method="html"/>
   <xsl:include href="../../common/xsl/registrytemplate.xsl"/>
   <xsl:template match="registryAdminQueryResponse">
      <p><xsl:value-of select="current()"/></p>
   </xsl:template>


 <!-- xmlns:xsp-request="http://msslxy.mssl.ucl.ac.uk:8080/cocoon/astrogrid/registry/add.xsp"> -->


</xsl:stylesheet> 



