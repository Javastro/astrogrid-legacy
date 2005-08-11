<?xml version="1.0" encoding="iso-8859-1"?>
<!-- Copyright © 2002, 2003 Matthew West        
	modified Noel Winstanley
	  -->
<!-- $Id: xml2xhtml.xsl,v 1.1 2005/08/11 10:15:00 nw Exp $ -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xalan="http://xml.apache.org/xslt"
		xmlns="http://www.w3.org/1999/xhtml"
                version="1.0">
 <xsl:output omit-xml-declaration="no"
             method="xml"
             doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
             doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
	     xalan:indent-amount="1"
	     indent="no"
             encoding="iso-8859-1" />
 <xsl:template match="/">
  <html>
   <head>
	<!-- see http://retrovirus.com/brunch/2003/04/html-support-in-jeditorpane/ for explanation of quirky markup - editorPane doesn't support span.. -->
	 <style type="text/css">
		cite{font-style:normal}
		.namespace{color: }
		.element{color:blue;}
		.attribute{color:green;}
		.text{color:black;}
		.newelement{margin-left:5px;}
		.body{font-family:sans-serif,monospaced}
	</style>
   </head> 
   <body>
    <pre>
     <xsl:apply-templates select="comment()" mode="prolog" />
     <xsl:apply-templates select="* | processing-instruction()" />
    </pre> 
   </body>
  </html> 
 </xsl:template>

 <xsl:template match="*">
	<div class="newelement">
  <xsl:text>&lt;</xsl:text>
  <xsl:call-template name="showName">
   <xsl:with-param name="name" select="name()" />
  </xsl:call-template> 
  <xsl:apply-templates select="@*" />
  <xsl:choose>
   <xsl:when test="node()">
    <xsl:text>&gt;</xsl:text>
    <xsl:apply-templates />
    <xsl:text>&lt;</xsl:text> 
    <xsl:text>/</xsl:text>
    <xsl:call-template name="showName">
     <xsl:with-param name="name" select="name()" />
    </xsl:call-template> 
   </xsl:when>
   <xsl:otherwise> 
    <xsl:text>/</xsl:text>
   </xsl:otherwise>
  </xsl:choose> 
  <xsl:text>&gt;</xsl:text>
  </div>
 </xsl:template>

 <xsl:template name="showName">
  <xsl:param name="name" />
  <xsl:choose>
   <xsl:when test="substring-before($name, ':')">
    <cite class="namespace">
     <xsl:value-of select="substring-before($name, ':')" />
    </cite>
    <xsl:text>:</xsl:text>
    <cite class="element">
     <xsl:value-of select="substring-after($name, ':')" />
    </cite>
   </xsl:when>
   <xsl:otherwise>
    <cite class="element">
     <xsl:value-of select="$name" />
    </cite>
   </xsl:otherwise>
  </xsl:choose>
 </xsl:template>       
 
 <xsl:template match="processing-instruction()">
  <cite class="process">
   <xsl:text>&lt;?</xsl:text>
   <xsl:value-of select="name()" />
  </cite> 
  <xsl:text> </xsl:text>
  <cite class="text"> 
   <xsl:value-of select="." />
  </cite> 
  <xsl:text>?&gt;</xsl:text>
 </xsl:template>

 <xsl:template match="@*">
  <xsl:text> </xsl:text>
  <cite class="attribute">
   <xsl:value-of select="name()" />
   <xsl:text>=</xsl:text>
  </cite>
  <cite class="text"> 
   <xsl:text>"</xsl:text>
   <xsl:value-of select="." />
   <xsl:text>"</xsl:text>
  </cite> 
 </xsl:template>

 <xsl:template match="text()">
  <cite class="text"> 
   <xsl:value-of select="." />
  </cite> 
 </xsl:template>

 <xsl:template match="comment()">
  <cite class="comment">
   <xsl:text>&lt;!--</xsl:text>
   <xsl:value-of select="." />
   <xsl:text>--&gt;</xsl:text>
  </cite>
 </xsl:template>

 <xsl:template match="comment()" mode="prolog">
  <cite class="comment">
   <xsl:text>&lt;!--</xsl:text>
   <xsl:value-of select="." />
   <xsl:text>--&gt;</xsl:text>
  </cite><xsl:text>
</xsl:text>
 </xsl:template>

</xsl:stylesheet>
