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
        .title{text-align:center; text-decoration:underline}
		.element{color:blue;}
		.body{font-family:sans-serif,monospaced;}
		.solid{border-style:solid;}
      </style>            
    </head>
    <body>
      <xsl:apply-templates />
      <!-- div class="title">XML registry entry:</div -->  
      <!-- xsl:apply-templates mode="showxml" / -->
    </body>
  </html>
</xsl:template>


<!-- RESOURCE -->
<xsl:template match="vor:Resource">
  <div class="title">Resource:</div>        
  <div class="element">
    <font color="black">Title: </font><xsl:value-of select="vr:title"/> 
    <br/>
    <font color="black">Short name: </font><xsl:value-of select="vr:shortName"/>
    <br/>
    <font color="black">Identifier: </font><xsl:value-of select="vr:identifier"/>
  </div>  
  <hr width="90%" align="center"></hr>
  <xsl:apply-templates/> 
</xsl:template>
         
<!-- CONTENT -->
<xsl:template match="vr:content">
  <div class="title">Content:</div>  
  <div class="element">      
    <xsl:if test="vr:description != 'not set'">    
      <font color="black">Description: </font><xsl:value-of select="vr:description"/>
      <br/>
    </xsl:if>    
    <xsl:if test="vr:referenceURL != ''">
      <font color="black">Reference URL: </font><xsl:value-of select="vr:referenceURL"/>
      <br/>
    </xsl:if>  
    <xsl:if test="vr:type != ''">    
      <font color="black">Type: </font><xsl:apply-templates select="vr:type" /> 
      <br/>
    </xsl:if>
    <xsl:if test="vr:subject != '???'">
      <font color="black">Type:</font><xsl:value-of select="vr:subject"/>
      <br/>           
    </xsl:if>
    <xsl:if test="vr:contentLevel != ''">    
      <font color="black">Level: </font><xsl:apply-templates select="vr:contentLevel" /> 
      <br/>
    </xsl:if>
  </div> 
  <hr width="90%" align="center"></hr>
</xsl:template>

<xsl:template match="vr:type">  
  <xsl:value-of select="."/><xsl:text>, </xsl:text>
</xsl:template>

<xsl:template match="vr:contentLevel">  
  <xsl:value-of select="."/><xsl:text>, </xsl:text>
</xsl:template>


<!-- COVERAGE -->
<xsl:template match="vs:coverage">
  <div class="title">Coverage:</div>        
  <div class="element">          
    <font color="black">Coverage:</font>
    <font color="black">All sky:</font>
    <xsl:apply-templates select="vr:spatial/vs:allSky" /> 
    <font color="black">region of regard:</font>         
    <xsl:apply-templates select="vr:spatial/vs:regionOfRegard" /> 
    <font color="black">Spectral:</font>         
    <xsl:apply-templates select="vs:spectral/vs:waveband" />
  </div>
  <hr width="90%" align="center"></hr>
</xsl:template>

<xsl:template match="vs:spectral/vs:waveband">  
  <xsl:value-of select="."/>,
</xsl:template>
      
<xsl:template match="vr:spatial/vs:allSky">
  <xsl:value-of select="."/><br /> 
</xsl:template>      

<xsl:template match="vr:spatial/vs:regionOfRegard">  
  <xsl:value-of select="."/><br />
</xsl:template>


<!-- TABLE -->
<xsl:template match="vs:table">
  <div class="element">
    <font color="black">Table name:</font>
    <xsl:value-of select="vs:name"/>
    <!-- COLUMN DETAILS -->      
    <div class="element">
      <xsl:apply-templates select="vs:column" />
    </div>
  </div>
  <hr width="90%" align="center"></hr>
</xsl:template>


<!-- COLUMN -->      
<xsl:template match="vs:column">
  <nobr>
  <font color="black"> Column: </font>
  <xsl:if test="vs:name != ''">
    <xsl:value-of select="vs:name"/>
  </xsl:if>  
  <xsl:if test="vs:description != ''">
    <font color="black"> Desc: </font>  
    <xsl:value-of select="vs:description"/>
  </xsl:if>
  <xsl:if test="vs:dataType != ''">
    <font color="black"> Type: </font>
    <xsl:value-of select="vs:dataType"/>
  </xsl:if>
  <xsl:if test="vs:unit != ''">
    <font color="black"> Units: </font>
    <xsl:value-of select="vs:unit"/>
  </xsl:if>
  <xsl:if test="vs:UCD != ''">
    <font color="black"> UCD: </font>
    <xsl:value-of select="vs:UCD"/>
  </xsl:if>
  </nobr>
  <br/>
</xsl:template>

<!-- CURATION -->
<xsl:template match="vr:curation">
  <div class="title">Curation:</div>  
  <div class="element">        
    <xsl:if test="vr:publisher != ''">
      <font color="black">Publisher: </font><xsl:value-of select="vr:publisher"/> 
      <br/>     
    </xsl:if>
    <xsl:if test="vr:creator != ''">    
      <font color="black">Creator: </font><xsl:value-of select="normalize-space(vr:creator)"/>      
      <br/>
    </xsl:if>
    <xsl:if test="vr:date != ''">
      <font color="black">Date: </font><xsl:value-of select="vr:date"/><xsl:text>,  </xsl:text>      
    </xsl:if>
    <xsl:if test="vr:version != ''">
      <font color="black">Version: </font><xsl:value-of select="vr:version"/>      
      <br/>
    </xsl:if>
    <xsl:if test="vr:contact/vr:name != ''">
      <font color="black">Contact: </font><xsl:value-of select="vr:contact/vr:name"/>
    </xsl:if>
    <xsl:if test="vr:contact/vr:email != ''">
      <font color="black">, Email: </font><xsl:value-of select="vr:contact/vr:email"/>
    </xsl:if>
  </div>
  <hr width="90%" align="center"></hr>
</xsl:template>
     

<!-- APPLICATION DEFINITION -->
<xsl:template match="cea:ApplicationDefinition">          
  <div class="title">Parameters:</div>
  <xsl:apply-templates select="cea:Parameters/cea:ParameterDefinition" />
  <div class="element">
    <font color="black">Interfaces: </font>
    <xsl:apply-templates select="cea:Interfaces/ceab:Interface" />
  </div>    
  <hr width="90%" align="center"></hr>
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
  <xsl:value-of select="@name"/><xsl:text>,  </xsl:text>
</xsl:template>

    
    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()" priority="-2"/>
    <xsl:template match="text()" priority="-1"/>
             
</xsl:stylesheet>