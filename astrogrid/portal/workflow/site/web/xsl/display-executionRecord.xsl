<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+
       | Display execution record
       |
       | Display execution records and messages associated with certain activities.
       | Only used with Step and Script activities at present as other activities do not have execution records associated with them
       +-->
    <xsl:template name="executionRecord">
      <xsl:for-each select="./executionRecord">        
        <tr>                  
          <td nowrap="true"><font size="-1"><b>Execution Record: </b></font></td>
          <td><font size="-1"><b>Status: </b></font>
            <xsl:choose>
              <xsl:when test="@execution-status = 'RUNNING'">
                <font color="green" size="-1"><xsl:value-of select="@execution-status"/></font>
              </xsl:when>
              <xsl:when test="@execution-status = 'ERROR'">
                <font color="red" size="-1"><xsl:value-of select="@execution-status"/></font>
              </xsl:when>                    
              <xsl:otherwise>
                <font color="blue" size="-1"><xsl:value-of select="@execution-status"/></font>                        
              </xsl:otherwise>
            </xsl:choose>
          </td>                
          <td><font size="-1"><b>Start: </b> <xsl:value-of select="@execution-start"/></font></td>
          <td><font size="-1"><b>Finish: </b> <xsl:value-of select="@execution-finish"/></font></td>              
        </tr>
        <xsl:for-each select="./message">
          <tr>
            <td rowspan="2"><font size="-1"><b>message: </b></font></td>
            <td nowrap="true"><font size="-1"><b>Phase: </b></font>
              <xsl:choose>
                <xsl:when test="@message-phase = 'RUNNING'">
                  <font color="green" size="-1"><xsl:value-of select="@message-phase"/></font>
                </xsl:when>
                <xsl:when test="@message-phase = 'ERROR'">
                  <font color="red" size="-1"><xsl:value-of select="@message-phase"/></font>
                </xsl:when>                    
                <xsl:otherwise>
                  <font color="blue" size="-1"><xsl:value-of select="@message-phase"/></font>                        
                </xsl:otherwise>
              </xsl:choose>                  
            </td>
            <td><font size="-1"><b>Time: </b><xsl:value-of select="@message-timestamp"/></font></td>
            <td><font size="-1"><b>Source: </b><xsl:value-of select="@message-source"/></font></td>              
          </tr>
          <tr>
            <td colspan="3"><font size="-1"><i><pre><xsl:value-of select="@message-content"/></pre></i></font></td>
          </tr> 
        </xsl:for-each>
      </xsl:for-each>            
    </xsl:template>
  </xsl:stylesheet>