<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="image_path">/astrogrid-portal/mount/workflow/</xsl:param>  <!-- path to images -->
    <xsl:param name="activity_key" />
    <xsl:param name="display_parameter_values" />
    <xsl:param name="display_tool_values" />

    <xsl:include href="display-else.xsl"/> 
    <xsl:include href="display-forLoop.xsl"/>
    <xsl:include href="display-flow.xsl"/> 
    <xsl:include href="display-if.xsl"/>     
    <xsl:include href="display-parameters.xsl"/>
    <xsl:include href="display-parForLoop.xsl"/>    
    <xsl:include href="display-scope.xsl"/>    
    <xsl:include href="display-script.xsl"/>
    <xsl:include href="display-set.xsl"/>
    <xsl:include href="display-sequence.xsl"/>
    <xsl:include href="display-then.xsl"/>    
    <xsl:include href="display-tool.xsl"/>    
    <xsl:include href="display-unset.xsl"/>    
    <xsl:include href="display-whileLoop.xsl"/>        
    <xsl:include href="display-workflow-map.xsl"/> 
    
    <xsl:template match="workflow">
      <ag-div>       
                 
        <agComponentTitle>Workflow</agComponentTitle>             
<!--       <ag-script type="text/javascript" src="/astrogrid-portal/mount/workflow/workflow-functions.js"/> -->
<!--       <xsl:if test="$activity_key != ''">
         <xsl:if test="$display_parameter_values = 'true'">
           <ag-onload>
             <xsl:attribute name="function">toggle('parameters:<xsl:value-of select="$activity_key"/>');</xsl:attribute>
           </ag-onload>
         </xsl:if>
       </xsl:if>
-->                
        <table border="1">
          <form action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" name="workflow_form" id="workflow_form">
            <tr>
              <td colspan="5">
                <ag-menu name="workflow-menu"/>
              </td>
            </tr>
            <tr>
              <td width="30">Name:</td>
              <td>
                <input type="text" size="80" name="workflow-name">                        
                  <xsl:attribute name="value"><xsl:value-of select="@workflow-name"/></xsl:attribute>
                </input>                        
              </td>
              <td rowspan="2">
                <input class="agActionButton" name="action" type="submit" value="update workflow details"/>                    
              </td>
              <td rowspan="2">
                <xsl:call-template name="workflow-map"/>
              </td>
              <td rowspan="2">
                <table>
                  <tr>
                    <td nowrap="true">
                      <font color="YELLOW" size="-2"><img src="/astrogrid-portal/mount/workflow/yellow.gif"/> Sequence</font><xsl:text>   </xsl:text>
                      <font color="RED" size="-2"><img src="/astrogrid-portal/mount/workflow/black.gif"/> Flow</font><xsl:text>   </xsl:text>
                      <font color="GREY" size="-2"><img src="/astrogrid-portal/mount/workflow/grey.gif"/> Step</font><br/>
                      <font color="GREEN" size="-2"><img src="/astrogrid-portal/mount/workflow/green.gif"/> Logic(if/scope/script/set/unset)</font><br/>
                      <font color="BLUE" size="-2"><img src="/astrogrid-portal/mount/workflow/blue.gif"/> Loops(for/parallel for/while)</font><br/>
                      <font color="BLACK" size="-2"><img src="/astrogrid-portal/mount/workflow/red.gif"/> Error handling(try/catch)</font><br/>                                                
                    </td>
                  </tr>
                </table>
              </td>                    
            <input type="hidden" name="open-workflow-ivorn" id="open-workflow-ivorn"/>
            <input type="hidden" name="open-workflow-agsl" id="open-workflow-agsl"/>                        
            </tr>
            <tr>
              <td width="30">Description:</td>
              <td>
                <input type="text" size="80" name="workflow-description">
                  <xsl:attribute name="value"><xsl:value-of select="@workflow-description"/></xsl:attribute>
                </input>
              </td>                            
              <input type="hidden" name="save-workflow-ivorn" id="save-workflow-ivorn"/>
              <input type="hidden" name="save-workflow-agsl" id="save-workflow-agsl"/>
              <input type="hidden" name="action" id="workflow_action"/>                 
            </tr>
          </form>

      <form action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" name="insert_activity_form" id="insert_activity_form">
        <input type="hidden" name="activity_key" id="activity_key"/>
        <input type="hidden" name="activity_index_key"/>
        <input type="hidden" name="activity_index_order"  id="activity_index_order"/>
        <input type="hidden" name="parent_activity_key"/>
        <input type="hidden" name="activity_type" id="activity_type"/>
        <input type="hidden" name="insert_activity_type" id="insert_activity_type"/>                   
        <input type="hidden" name="action" value="insert_activity"/> 
        <input type="hidden" name="copy_activity_key" id="copy_activity_key"/>           
      </form>                                            
      <form action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" name="remove_activity_form" id="remove_activity_form">
        <input type="hidden" name="activity_key"/>
        <input type="hidden" name="parent_activity_key"/>
        <input type="hidden" name="activity_index_key"/>                   
        <input type="hidden" name="action" value="remove_activity"/>            
      </form>

        </table>
        <div id="iframeHolder">......</div>
        <p />
        <xsl:apply-templates select="*" mode="activity-details"/>                                                   
      </ag-div>

      <script language="javascript">
        var ifr = document.getElementById("iframeHolder");
        ifr.innerHTML = '<iframe name="workflow_iframe" id="workflow_iframe" src="/astrogrid-portal/bare/mount/workflow/innerFrame.html" border="0" frameborder="0" framespacing="10" height="400" width="95%" scrolling="auto" />'        
      </script>
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
