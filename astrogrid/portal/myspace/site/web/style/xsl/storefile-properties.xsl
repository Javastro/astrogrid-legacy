<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output
      method="xml"
      omit-xml-declaration="yes"/>   
    
    
    <xsl:template match="/">      
	  <ag-div>
	     <!-- Add our page content -->
		 <content>
		   <agPUBMessage>
             File Properties
           </agPUBMessage> 
                      
        <xsl:apply-templates select="properties" />
    
    	 </content>
	  </ag-div>
  </xsl:template>
  
  
  <xsl:template match="properties">
  
          <table>
          <tr>
            <td>Name:&#160;</td>
            <td><xsl:value-of select="@name"/></td>
          </tr>
          <tr>
            <td>Path:&#160;</td>
            <td>
                 <xsl:element name="A">
                     <xsl:attribute name="href"><xsl:value-of select="@url"/></xsl:attribute>
                     <xsl:value-of select="@path"/>
                 </xsl:element>
            </td>
          </tr>
          <tr>
            <td>Created:&#160;</td>
            <td><xsl:value-of select="@created"/></td>
          </tr>
          <tr>
            <td>Modified:&#160;</td>
            <td><xsl:value-of select="@modified"/></td>
          </tr>
          <tr>
            <td>Owner:&#160;</td>
            <td><xsl:value-of select="@owner"/></td>
          </tr>
          <tr>
            <td>Size:&#160;</td>
            <td><xsl:value-of select="@size"/></td>
          </tr>
          <tr>
            <td>MIME type:&#160;</td>
            <td><xsl:value-of select="@mime-type"/></td>
          </tr>       
        </table>
  </xsl:template>
  
  

  
  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
