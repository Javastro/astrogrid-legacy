<?xml version="1.0" encoding="ISO-8859-1"?>

  <xsl:stylesheet
    version="1.0"
    xmlns="http://www.astrogrid.org/portal"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"
    xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
    xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5"
    xmlns:vc="http://www.ivoa.net/xml/VORegistry/v0.3"
    xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.2"
    xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
    xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"     >

  <xsl:output 
    omit-xml-declaration="yes"
    method="xml"
    doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
	xalan:indent-amount="1"
	indent="no"
    encoding="iso-8859-1" />
      
    
<!-- ROOT -->
<xsl:template match="/" >	
  <html>
    <head>
      <style type="text/css">
	    cite{font-style:normal}
		.namespace{color: }
		.element{color:blue;}
		.body{font-family:sans-serif,monospaced;}   
      </style>            
    </head>
    <body>
      <xsl:apply-templates />
      <xsl:apply-templates mode="showxml" />
    </body>
  </html>
</xsl:template>


<!-- RESOURCE -->
<xsl:template match="vor:Resource">
  <div class="cite">                    
    <div class="element">
      <font color="black">Title: </font>
      <xsl:value-of select="vr:title"/> (<font color="black">Short name: </font><xsl:value-of select="vr:shortName"/>)
    </div>
    <div class="element">
      <font color="black">Identifier: </font>
      <xsl:value-of select="vr:identifier"/>
    </div>
    <center>--------------------------------</center>
    <xsl:apply-templates/>
  </div> 
</xsl:template>
            
<xsl:template match="vr:title"/>
<xsl:template match="vr:description"/>
<xsl:template match="vr:shortName"/>
<xsl:template match="vr:identifier"/>
<xsl:template match="vr:subject"/>
         
<!-- CONTENT -->
<xsl:template match="vr:content">
  <xsl:if test="vr:description != 'not set'">
    <div class="element">
      <font color="black">Description: </font>
      <xsl:value-of select="vr:description"/>
    </div>
  </xsl:if>  
  <div class="element">
    <xsl:if test="vr:referenceURL != ''">
      <font color="black">Reference URL: </font>
      <xsl:value-of select="vr:referenceURL"/>
    </xsl:if>
  </div>
  <div class="element">
    <font color="black">Type: </font>
    <xsl:value-of select="vr:type"/>
  </div>
  <xsl:if test="vr:subject != '???'">
    <div class="element">
      <font color="black">Type:</font>
      <xsl:value-of select="vr:subject"/>
    </div>            
  </xsl:if>
  <center>--------------------------------</center>
</xsl:template>


<!-- CURATION -->
<xsl:template match="vr:curation">
  <div class="element">
    <font color="black">Publisher: </font><xsl:value-of select="vr:publisher"/>
    <font color="black"> Creator: </font><xsl:value-of select="vr:creator"/>
  </div>
  <div class="element">
    <font color="black">Date: </font><xsl:value-of select="vr:date"/>
    <font color="black"> Version: </font><xsl:value-of select="vr:version"/>
  </div>
  <div class="element">
    <font color="black">Contact: </font><xsl:value-of select="vr:contact"/>
  </div>
  <center>--------------------------------</center>
</xsl:template>
     

<!-- APPLICATION DEFINITION -->
<xsl:template match="cea:ApplicationDefinition">          
  <font color="black">Parameters:</font>
  <xsl:apply-templates select="cea:Parameters/cea:ParameterDefinition" />
  <div class="element">
    <font color="black">Interfaces: </font>
    <xsl:apply-templates select="cea:Interfaces/ceab:Interface" />
  </div>    
    <center>--------------------------------</center>          
</xsl:template>                  

<!-- PARAMETER DEFINITION -->
<xsl:template match="cea:Parameters/cea:ParameterDefinition">        
  <div class="element">
    <font color="black">Name: </font><xsl:value-of select="ceapd:UI_Name"/>
    <font color="black"> Description: </font><xsl:value-of select="ceapd:UI_Description"/>
  </div>
</xsl:template>

<!-- INTERFACES -->      
<xsl:template match="cea:Interfaces/ceab:Interface">  
  <xsl:value-of select="@name"/>,
</xsl:template>      
            
<!-- XML TO HTML -->
  <!-- Default node handler, convert to HTML and apply templates -->
  <xsl:template match="*" name="xml-html" mode="showxml">
    <br></br> 
      <!--Indent by number of parent nodes -->
        <xsl:for-each select="ancestor::*">
          <xsl:text>....</xsl:text>
        </xsl:for-each>
        <!-- Open the element -->
        <xsl:text>&lt;</xsl:text>
        <!-- Add the element name -->
        <xsl:value-of select="name()"/>
        <!-- Copy all of the node attributes -->
        <xsl:for-each select="attribute::*">
          <xsl:text>.</xsl:text>
          <xsl:value-of select="name()"/>
          <xsl:text>=</xsl:text>
          <xsl:text>&quot;</xsl:text>
          <xsl:value-of select="."/>
          <xsl:text>&quot;</xsl:text>
        </xsl:for-each>
        <!-- If this node does not have any child nodes -->
        <xsl:if test="count(child::node()) + count(child::text()) + count(child::comment()) = 0">
          <!-- Close the element -->
          <xsl:text>/&gt;</xsl:text>                              
        </xsl:if>
        <!-- If this node does have some child nodes -->
        <xsl:if test="count(child::node()) + count(child::text()) + count(child::comment()) > 0">
          <!-- Close the element -->
          <xsl:text>&gt;</xsl:text>                       
          <!-- Add the element content -->
          <xsl:apply-templates mode="showxml"/>
          <br/> 
          <!--Indent by number of parent nodes -->
          <xsl:for-each select="ancestor::*">
          <xsl:text>....</xsl:text>
        </xsl:for-each>
        <!-- Open the element -->
        <xsl:text>&lt;</xsl:text>
        <xsl:text>/</xsl:text>
        <!-- Add the element name -->
        <xsl:value-of select="name()"/>
        <!-- Close the element -->
        <xsl:text>&gt;</xsl:text>
      </xsl:if>
    </xsl:template>    
             
</xsl:stylesheet>