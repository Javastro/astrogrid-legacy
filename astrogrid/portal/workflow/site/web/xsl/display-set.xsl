<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <!--+
       | Display set details
       |
       | Display table containing details of set...
       |
       +-->
    <xsl:template match="setObj" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="set_form" id="set_form" method="post">
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
                <div style="color: blue; background-color: lightblue; text-align: center;">Set:</div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                    The <b>set</b> element is an activity that defines a new workflow variable, or updates the value of an existing variable. <br/>
                    It has two attributes: <b>var</b> - the name of the workflow variable (required); <b>value</b> - the value to set this variable to (optional).<br />
                    If no value is provided, the variable is created and set to null.<br />
                    The value attribute may be a straight literal string, or a script expression 
                </div>
              </td>
            </tr>            
            <tr>
              <td>
                Name:
              </td>
              <td>
                <input type="text" size="80" name="set-var">
                  <xsl:choose>
                    <xsl:when test="@set-var = 'null'">
                      <xsl:attribute name="value"></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="value"><xsl:value-of select="@set-var"/></xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                </input>                                 
              </td>
            </tr>
            <tr>
              <td>
                Value:
              </td>
              <td>
                <xsl:choose>
                  <xsl:when test="@set-value = 'null'">
                    <textarea name="set-value" cols="130" rows="3">
                      <xsl:attribute name="id">set-value<xsl:value-of select="@key"/></xsl:attribute>
                      <xsl:attribute name="onclick">document.getElementById('set-value<xsl:value-of select="@key"/>').value='';
                                                    document.getElementById('set-value<xsl:value-of select="@key"/>').onclick='';
                      </xsl:attribute>
                      (Value to set variable to. May contain embedded expressions in ${..}, which will be evaluated...)
                    </textarea>                                    
                  </xsl:when>
                  <xsl:when test="@set-value = ''">
                    <textarea name="set-value" cols="130" rows="3">
                      <xsl:attribute name="id">set-value-empty<xsl:value-of select="@key"/></xsl:attribute>                        
                      <xsl:attribute name="onclick">document.getElementById('set-value-empty<xsl:value-of select="@key"/>').value='';
                                                    document.getElementById('set-value-empty<xsl:value-of select="@key"/>').onclick='';
                       </xsl:attribute>
                      ...
                    </textarea>                      
                  </xsl:when>                  
                  <xsl:otherwise>
                    <textarea name="set-value" cols="130" rows="3"><xsl:value-of select="@set-value"/></textarea>                
                  </xsl:otherwise>
                </xsl:choose>                  
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue;">
                  <input class="agActionButton" type="submit" name="action" value="update set details" />
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
       | Display set details for job status page
       |
       | Display table containing details of set...
       |
       +-->
    <xsl:template match="setObj" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="set_form" id="set_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="2">       
                <div style="color: blue; background-color: lightblue; text-align: center;">Set:</div>
              </td>
            </tr>            
            <tr>
              <td>Name:</td>
              <td>
                <textarea name="set-var" cols="130" rows="2" readonly="true"><xsl:value-of select="@set-var"/></textarea>                
              </td>
            </tr>
            <tr>
              <td>Value:</td>
              <td>
                <xsl:choose>
                  <xsl:when test="@set-value = ''">
                    <textarea name="set-value" cols="130" rows="2" readonly="true">...</textarea>                       
                  </xsl:when>                  
                  <xsl:otherwise>
                    <textarea name="set-value" cols="130" rows="2" readonly="true"><xsl:value-of select="@set-value"/></textarea>           
                  </xsl:otherwise>
                </xsl:choose>                                                 
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
