<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <!--+
       | Display scope details
       |
       | Display empty table...
       |
       +-->
    <xsl:template match="scopeObj" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="scopeObj_form" id="scopeObj_form" method="post">
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
                <div style="color: blue; background-color: lightblue; text-align: center;"> Scope: </div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                 The <b>scope</b> element is a container for activities that introduces a new nested scope for workflow variables.<br /> 
                 Any workflow variables defined within a nested scope are not visible outside that scope.<br /> 
                 Any attempts to reference such variables returns null
                </div>
              </td>
            </tr>            
            <tr>
              <td></td>                  
              <td>
                <textarea name="empty_area" cols="130" rows="6" readonly="true">...</textarea>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue">...</div>
              </td>
            </tr>
          </table>
          <input type="hidden" name="activity_key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input> <!-- Note: do NOT set id as id=activity_key is used elsewhere and not needed here -->
        </form>
      </div>
      <xsl:apply-templates select="*" mode="activity-details"/>                                                                                                  
    </xsl:template>
    
  <!--+
       | Display scope details
       |
       | Display empty table...
       |
       +-->
    <xsl:template match="scopeObj" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="scopeObj_form" id="scopeObj_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>                
              <td colspan="2">
                <div style="color: blue; background-color: lightblue; text-align: center;"> Scope: </div>
              </td>
            </tr>            
            <tr>
              <td></td>                  
              <td>
                <textarea name="empty_area" cols="130" rows="6" readonly="true">...</textarea>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue">...</div>
              </td>
            </tr>
          </table>
        </form>
      </div>
      <xsl:apply-templates select="*" mode="job-status"/>                                                                                                  
    </xsl:template>    
        
</xsl:stylesheet>
