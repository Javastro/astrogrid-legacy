<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <!--+
       | Display parallel forLoop details
       |
       | Display table containing details of parallel forLoop.
       |
       +-->
    <xsl:template match="parForObj" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <form name="parForLoop_form" id="parForLoop_form" method="post">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>
              <td style="cursor: help; text-align: center; background-color: lightblue">                                     
                <xsl:element name="img">
                  <xsl:attribute name="src">/astrogrid-portal/mount/workflow/Help3.png</xsl:attribute>
                  <xsl:attribute name="alt">info</xsl:attribute>
                  <xsl:attribute name="onClick">toggleHelp('helpDiv<xsl:value-of select="@key"/>');</xsl:attribute>
                </xsl:element>                                    
              </td>                
              <td>
                <div style="color: blue; background-color: lightblue; text-align: center;">Parallel For loop:</div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                    The parfor element expresses a parallel for loop.<br /> 
                    It has the same structure as the for loop, but executes it's loop body simultaneously for each item in the values list.
                    This construct is useful for starting many CEA application exections in parallel.
                </div>
              </td>
            </tr>            
            <tr>
              <td>
                Var:
              </td>
              <td>
                <xsl:choose>
                  <xsl:when test="@parFor-var = 'null'">
                    <textarea name="for_get" cols="130" rows="2">
                      <xsl:attribute name="id">while_var<xsl:value-of select="@key"/></xsl:attribute>
                      <xsl:attribute name="onclick">document.getElementById('while_var<xsl:value-of select="@key"/>').value='';</xsl:attribute>
                      (the name of the loop variable...)
                    </textarea>                                    
                  </xsl:when>
                  <xsl:otherwise>
                    <textarea name="for_get" cols="130" rows="2"><xsl:value-of select="@parFor-var"/></textarea>                
                  </xsl:otherwise>
                </xsl:choose>                  
              </td>
            </tr>
            <tr>
              <td>Items:</td>
              <td>
                <xsl:choose>
                  <xsl:when test="@parFor-item = 'null'">
                    <textarea name="for_item" cols="130" rows="2">
                      <xsl:attribute name="id">while_item<xsl:value-of select="@key"/></xsl:attribute>
                      <xsl:attribute name="onclick">document.getElementById('while_item<xsl:value-of select="@key"/>').value='';</xsl:attribute>
                      ( A sequence or iterator of items - the loop variable will be assigned to each in turn, and then loop body executed...)
                    </textarea>                                    
                  </xsl:when>
                  <xsl:otherwise>
                    <textarea name="for_item" cols="130" rows="2"><xsl:value-of select="@parFor-item"/></textarea>                
                  </xsl:otherwise>
                </xsl:choose>                  
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue">
                  <input class="agActionButton" type="submit" name="action" value="update parallel for loop details" />
                </div>
              </td>
            </tr>
          </table>
          <!-- Note: do NOT set id as id=activity_key is used elsewhere and not needed here -->
          <input type="hidden" name="activity_key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input> 
        </form>
      </div> 
    <xsl:apply-templates select="*" mode="activity-details"/>                                                                                                      
    </xsl:template>
    
  <!--+
       | Display parallel forLoop details for job status page
       |
       | Display table containing details of parallel forLoop.
       |
       +-->
    <xsl:template match="parForObj" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <form name="parForLoop_form" id="parForLoop_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="2">
                <div style="color: blue; background-color: lightblue; text-align: center;">Parallel For loop:</div>
              </td>
            </tr>
            <tr>
              <td>
                Var:
              </td>
              <td>
                <textarea cols="130" rows="2" readonly="true"><xsl:value-of select="@parFor-var"/></textarea>
              </td>
            </tr>
            <tr>
              <td>Items:</td>
              <td>
                <textarea cols="130" rows="2" readonly="true"><xsl:value-of select="@parFor-item"/></textarea>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue">
                  ...
                </div>
              </td>
            </tr>
          </table>
        </form>
      </div> 
    <xsl:apply-templates select="*" mode="job-status"/> 
    </xsl:template>    
        
</xsl:stylesheet>
