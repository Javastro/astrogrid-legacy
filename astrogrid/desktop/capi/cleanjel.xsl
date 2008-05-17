<?xml version="1.0"?>
<!-- $id$
   a stylesheet to leanthe jel description...note this is using xslt 2.0 features. -->
<!-- issues
  chage the name of the curation date type
  
  get rid of one of the siap executes
   
   ExecutionMessage and ModuleDescriptor need to be beans
-->
<xsl:stylesheet version="2.0"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   xmlns:xs="http://www.w3.org/2001/XMLSchema"
   xmlns:lfn="urn:acrclocalfunctions"
>
   <xsl:preserve-space elements="*" />
   <xsl:output method="xml" />
   <xsl:template match="/">
      <xsl:apply-templates />
   </xsl:template>
   <xsl:template match="*[@fulltype='org.astrogrid.acr.ivoa.resource.Date' and @type='Date']">
        <xsl:copy-of select="."><!-- not good enough - need to preserve the possible [] just do a string replace... -->
           <xsl:attribute name='fulltype'>org.astrogrid.acr.ivoa.resource.Date</xsl:attribute>
          <xsl:attribute name='type'>Date</xsl:attribute>
        </xsl:copy-of>
   </xsl:template>
   <!-- default template -->
   <xsl:template match="node()|@*">
      <xsl:copy-of select="." />
   </xsl:template>
</xsl:stylesheet>
