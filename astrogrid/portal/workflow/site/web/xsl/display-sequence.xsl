<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <!--+
       | Display sequence details
       |
       | Display empty table...
       |
       +-->
    <xsl:template match="sequence" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="sequence_form" id="sequence_form">
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
                <div style="color: blue; background-color: lightblue; text-align: center;"> Sequence: </div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                  The <b>sequence</b> element composes a set of activities into a linear structure - they will be executed sequentially in order.<br />
                  <i>Select activities to insert into this sequence using the <b>Edit</b> menu.</i>
                  <pre>
&lt;!-- execute some activities sequentially --&gt;
&lt;sequence&gt;
   &lt;step name="a"&gt;
      &lt;!-- omitted --&gt;
   &lt;/step&gt;
   &lt;script&gt;
      &lt;!-- omitted --&gt;
   &lt;/script&gt;
   &lt;step name="b"&gt;
      &lt;!-- omitted --&gt;
   &lt;/step&gt;
&lt;/sequence&gt;                    
                  </pre>
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
       | Display sequence details for job status page
       |
       | Display empty table...
       |
       +-->
    <xsl:template match="sequence" mode="job-status">
      <div style="display:none "><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <xsl:choose>        
          <xsl:when test="@key = '/sequence'"> 
            <form name="sequence_form" id="sequence_form">            
              <table border="2" cellspacing="0" cellpadding="0">
                <tr>
                  <td colspan="2"> 
                    <div style="color: blue; background-color: lightblue; text-align: left;"><b>Workflow: Overall execution record</b></div>
                  </td>
                  <td>
                    <div style="color: blue; background-color: lightblue; text-align: left;"><b>Name:</b> <xsl:value-of select="../@workflow-name"/></div>
                  </td>
                  <td colspan="2">
                    <div style="color: blue; background-color: lightblue; text-align: left;"><b>Desc:</b> <xsl:value-of select="../@workflow-description"/></div>
                  </td>              
                </tr>
                <xsl:for-each select="//workflowExecutionRecord">        
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
                <tr>
                  <td colspan="4">
                    <div style="text-align: center; background-color: lightblue;">...
                    </div>
                  </td>
                </tr>                        
              </table>           
            </form> 
          </xsl:when>
          <xsl:otherwise>
            <form name="sequence_form" id="sequence_form">
              <table width="10%"  border="2" cellspacing="0" cellpadding="0">
                <tr>                
                  <td colspan="2">
                    <div style="color: blue; background-color: lightblue; text-align: center;"> Sequence: </div>
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
          </xsl:otherwise>
        </xsl:choose>               
      </div>
    <xsl:apply-templates select="*" mode="job-status"/>      
    </xsl:template>            
                        
</xsl:stylesheet>
