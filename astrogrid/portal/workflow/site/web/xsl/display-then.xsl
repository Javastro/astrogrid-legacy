<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <!--+
       | Display then details
       |
       | Display empty table...
       |
       +-->
    <xsl:template match="thenObj" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="then_form" id="then_form">
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
                <div style="color: blue; background-color: lightblue; text-align: center;"> Then: </div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                  The if element may have either or both a <b>then</b> and else child elements.<br /> 
                  Each contains an activity (or sequence of activities) that will be executed depending on the value of the test attribute
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
       | Display then details for job status page
       |
       | Display empty table...
       |
       +-->    
    <xsl:template match="thenObj" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="then_form" id="then_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>               
              <td colspan="2">
                <div style="color: blue; background-color: lightblue; text-align: center;"> Then: </div>
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
