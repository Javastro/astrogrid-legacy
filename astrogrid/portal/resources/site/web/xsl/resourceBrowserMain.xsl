<?xml version="1.0"?>

  <xsl:stylesheet
    version="1.0"
    xmlns="http://www.astrogrid.org/portal"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>

      <xsl:param name="resourceNameField" />
      <xsl:param name="publisherField" />
      <xsl:param name="titleField" />
      <xsl:param name="descriptionField" />
      <xsl:param name="authorityIdField" />
      <xsl:param name="returnDisplay" />

      
      <xsl:include href="query-catalogs.xsl"/>
      <xsl:include href="query-tasks.xsl"/>
      <xsl:include href="query-filestores.xsl"/>
      <xsl:include href="query-intro.xsl"/>

      <xsl:template match="resources">
        <ag-div>
                                    
          <span class="agCoolishTitle">Resource browser</span>
                            
          <body style="font-size: 90%"/> 
                                          
          <script language="javascript">
    /*
     * showSearch()
     *
     * Changes display setting of div
     *
     * @param id        id of div         
     */
	var previously_selected_search = 'query-intro';
    function showSearch(id) 
    {     
        if ( document.getElementById(previously_selected_search) )
        {
		    document.getElementById(previously_selected_search).style.display="none"; 
		}
        document.getElementById(id).style.display="";
		previously_selected_search=id;	          
    }   

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

    /*
     * toggle()
     *
     * @param id         
     */
    function toggle(id) 
    {             
        var element = document.getElementById(id);
        with (element.style) 
        {
            if ( display == "none" )
            {
                display = ""
            } 
            else
            {
                display = "none"
            }
        }
    }
       
        </script>
                          
        <table>
          <tr valign="top">
            <td>
              <table border="0" width="100">
                <tr>
                  <td align="center" style="color: blue; cursor: pointer; font-weight: bold" >                      
                    Search for:
                  </td>
                </tr>
                <tr>
                  <td nowrap="true">
                    <div title="&#10;Search registry for catalogs&#10;" class="agActionButton" onclick="showSearch('query-catalogs')">Catalog Search</div>
                  </td>
                </tr>
                <tr>
                  <td nowrap="true">
                    <div title="&#10;Search registry for tasks&#10;" class="agActionButton" onclick="showSearch('query-tasks')">Task Search</div>
                  </td>
                </tr>
                <tr>
                  <td nowrap="true">
                    <div title="&#10;Browse registry for filestores&#10;" class="agActionButton" onclick="showSearch('query-filestores')">Browse filestores</div>
                  </td>
                </tr>                                
              </table>
            </td>
            <td>
              <table>
                <tr>
                  <td>
                    <xsl:call-template name="query-catalogs" />
                    <xsl:call-template name="query-tasks" />
                    <xsl:call-template name="query-filestores" />
                    <xsl:call-template name="query-intro" />
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>               
        
        <div id="resourceIFrame">......</div>
        <p />

      <script language="javascript">
        var ifr = document.getElementById("resourceIFrame");        
        var url = '/astrogrid-portal/bare/mount/resources/resourceResults.html' ;
        ifr.innerHTML = '<iframe name="workflow_iframe" id="workflow_iframe" src="'+url+'" border="0" frameborder="0" framespacing="5" height="500" width="100%" scrolling="auto" />'                
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
