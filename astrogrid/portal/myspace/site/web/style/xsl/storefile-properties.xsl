<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10" >
    
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
           
        <div id="queryfilestores" style="display: ">                 
        <form name="filestore_form" 
              id="filestore_form" 
              action="/astrogrid-portal/bare/mount/myspace/myspace-relocate-file-action" 
              method="post" 
              enctype="multipart/form-data" > 
              
              <input id="myspace-action" name="myspace-action" type="hidden" value="myspace-relocate-file"/>          
             
          <table border="1" cellpadding="5" width="100%">
            <tr valign="top">
              <td>                      
                  <xsl:apply-templates select="properties/file-properties" />
              </td>
            </tr>
            <tr valign="top">
              <td>
                 <xsl:apply-templates select="properties/file-stores" />
              </td>
            </tr>
          </table>
        </form>
        </div>
    
    	 </content>
	  </ag-div>
  </xsl:template>
  
  <xsl:template match="file-properties">
  
          <table border="0" class="compact" >
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
                 <input id="myspace-source-path" name="myspace-source-path" type="hidden">
                     <xsl:attribute name="value"><xsl:value-of select="@path"/></xsl:attribute>     
                 </input>             
                  
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
         <tr>
            <td>Held in filestore:&#160;</td>
            <td><xsl:value-of select="substring-after(@content-location,'ivo://')"/></td>
          </tr>   
        </table>
  </xsl:template>
  
  <xsl:template match="file-stores">
          <br/>
          <table border="0" class="compact">
              <tr>
                  <td valign="top" align="left">To relocate to a different filestore, select from the available filestores below and press the relocate button.<br/><br/></td>
              </tr>
              <tr>    
                   <td valign="top" align="left">
                     <select size="7" id="myspace-filestore" name="myspace-filestore">
                       <xsl:for-each select="vr:Resource">
                         <xsl:sort select="Identifier/AuthorityID" />
                         <xsl:sort select="Identifier/ResourceKey" />
                         <xsl:element name="option">
                           <xsl:value-of select="Identifier/AuthorityID"/>/<xsl:value-of select="Identifier/ResourceKey"/>
				         </xsl:element>
                       </xsl:for-each> 
                     </select>
                     <br/><br/>
                   </td>                                              
               </tr>
               <tr>
                  <td valign="center" align="left"><input class="agActionButton" type="submit" name="actionButton" value="relocate" /></td>
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
