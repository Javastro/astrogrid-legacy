<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->
       
  <xsl:param name="image_path">/astrogrid-portal/mount/workflow/</xsl:param>  <!-- path to images -->
  
    <xsl:include href="display-else.xsl"/> 
    <xsl:include href="display-executionRecord.xsl"/>    
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
                
        <table border="1" cellpadding="0" cellspacing="0">
          <tr>
            <td>Name:</td>
            <td><xsl:value-of select="@workflow-name"/></td>
            <td nowrap="true">Overall status: </td>
            <td align="center">
              <xsl:choose>
                <xsl:when test="@workflow-status = 'RUNNING'">
                  <font color="green" size="-1"><xsl:value-of select="@workflow-status"/></font>
                </xsl:when>
                <xsl:when test="@workflow-status = 'ERROR'">
                  <font color="red" size="-1"><xsl:value-of select="@workflow-status"/></font>
                </xsl:when>                    
                <xsl:otherwise>
                  <font color="blue" size="-1"><xsl:value-of select="@workflow-status"/></font>                        
                </xsl:otherwise>
              </xsl:choose>
            </td>
            <td rowspan="3">
              <xsl:call-template name="workflow-map"/>
            </td>            
            <td rowspan="3" valign="bottom">
              <form action="/astrogrid-portal/main/mount/workflow/agjobmanager-printer-friendly.html" name="printer_form" id="printer_form">
                <input class="agActionButton" type="submit" name="action" value="Workflow transcript"/>
                <input type="hidden" name="jobURN" id="jobURN"/>
              </form>
            </td>
          </tr>            
          <tr>
            <td>Description:</td>
            <td colspan="3"><xsl:value-of select="@workflow-description"/></td>
          </tr>
          <tr>
            <td>Start time:</td>
            <td><xsl:value-of select="@workflow-start-time"/></td>
            <td>End time:</td>
            <td><xsl:value-of select="@workflow-end-time"/></td>
          </tr>                                                                     
        </table>                     
        <div id="iframeStatus">......</div>
        <p />
        <xsl:apply-templates select="*" mode="job-status"/>        
      </ag-div>

      <script language="javascript">
        var ifr = document.getElementById("iframeStatus");
        ifr.innerHTML = '<iframe name="workflow_iframe" id="workflow_iframe" src="/astrogrid-portal/bare/mount/workflow/statusInnerFrame.html" border="0" frameborder="0" framespacing="10" height="350" width="95%" scrolling="auto" />'        
      </script>    
    </xsl:template>

    <xsl:template match="tool"/>

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
