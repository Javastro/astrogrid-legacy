<?xml version="1.0"?>

  <xsl:stylesheet
    version="1.0"
    xmlns="http://www.astrogrid.org/portal"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>
      
      <xsl:param name="mainelement" />
      <xsl:param name="parent_authId" />     

      <xsl:param name="resourceNameField" />
      <xsl:param name="publisherField" />
      <xsl:param name="titleField" />
      <xsl:param name="descriptionField" />
      <xsl:param name="authorityIdField" />
      <xsl:param name="returnDisplay" />

      
      <xsl:include href="microbrowserCatalog.xsl"/>
      <xsl:include href="microbrowserTask.xsl"/>

      <xsl:template match="resources">
        <ag-div>

          <table background="/astrogrid-portal/hudf_hst_strip.jpg" cellspacing="0" cellpadding="0" width="100%">
            <tr>
              <td align="left">
                <span style="color: yellow; font-size: 12pt; font-weight: bold;">
                  Resource Microbrowser
                </span>
              </td>
              <td align="right">
                <span style="background-color: black; cursor: pointer; color: yellow; font-variant: small-caps; font-size: 12pt; font-weight: bold; padding-right: 40px" onClick="javascript:window.close();">Close
                </span>
              </td>
            </tr>
          </table>
                                          
          <script language="javascript">
    /*
     * selectJoin()
     *
     * @param join         
     */
    function selectJoin(join)
    {     
        document.getElementById('andor1').value=join;
        document.getElementById('andor2').value=join;
        document.getElementById('andor3').value=join;	  
    } 
       
        </script>                                       
                          
        <table>
          <tr valign="top">
            <td>
              <table border="0" width="100">
                <tr align="center">
                  <xsl:if test="$mainelement = 'Task'">
                    <xsl:call-template name="queryMB-tasks" />
                  </xsl:if>
                  <xsl:if test="$mainelement = 'Catalog'">
                    <xsl:call-template name="queryMB-catalogs" />
                  </xsl:if>
                </tr>                                
              </table>
            </td>
          </tr>
        </table>               
        
        <div id="resourceIFrameMB">......</div>
        <p />

      <script language="javascript">
        var ifr = document.getElementById("resourceIFrameMB");        
        var url = '/astrogrid-portal/bare/mount/resources/resourceResultsMB.html' ;
        ifr.innerHTML = '<iframe name="workflow_iframeMB" id="workflow_iframeMB" src="'+url+'" border="0" frameborder="0" framespacing="5" height="600" width="100%" scrolling="auto" />'                
      </script>

      </ag-div>
      </xsl:template>                      
    
    
    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()" priority="-2">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="text()" priority="-1">
        <xsl:value-of select="."/>
    </xsl:template>
    
</xsl:stylesheet>
