<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->
       
  <!--+
       | Display unset details
       |
       | Display table containing details of unset...
       |
       +-->
    <xsl:template match="unsetObj" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="unset_form" id="unset_form" method="post">
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
                <div style="color: blue; background-color: lightblue; text-align: center;">Unset:</div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>
                    The <b>unset</b> element is an activity that deletes a workflow variable.<br /> 
                    It has one required attribute - <b>var</b> - the name of the workflow variable to delete.<br />
                    If a workflow variable is referenced after it has been deleted, null is returned. 
                </div>
              </td>
            </tr>            
            <tr>
              <td>
                Name:
              </td>
              <td>
                <input type="text" size="80" name="unset-var">
                  <xsl:choose>
                    <xsl:when test="@unset-var = 'null'">
                      <xsl:attribute name="value"></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="value"><xsl:value-of select="@unset-var"/></xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                </input>                                 
              </td>              
            </tr>
            <tr>
              <td></td>
              <td><textarea name="empty" cols="130" rows="3" readonly="true">...</textarea>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue;">
                  <input class="agActionButton" type="submit" name="action" value="update unset details" />
                </div>
              </td>
            </tr>            
          </table>
          <input type="hidden" name="activity_key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input> <!-- Note: do NOT set id as id=activity_key is used elsewhere and not needed here -->
        </form>
      </div>
    <xsl:apply-templates select="*" mode="activity-details"/>                                                                                                  
    </xsl:template>
    
  <!--+
       | Display unset details for job status page
       |
       | Display table containing details of unset...
       |
       +-->
    <xsl:template match="unsetObj" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="unset_form" id="unset_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="2">       
                <div style="color: blue; background-color: lightblue; text-align: center;">Unset:</div>
              </td>
            </tr>            
            <tr>
              <td>
                Name:
              </td>
              <td>
                <textarea name="unset-var" cols="130" rows="6" readonly="true"><xsl:value-of select="@unset-var"/></textarea>                
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue;">
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
