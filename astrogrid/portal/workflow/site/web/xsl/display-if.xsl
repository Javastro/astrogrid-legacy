<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <!--+
       | Display if details
       |
       | Display table containing details of if activity.
       |
       +-->
    <xsl:template match="ifObj" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <form name="if_form" id="if_form" method="post">
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
                <div style="color: blue; background-color: lightblue; text-align: center;">If:</div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                    The <b>if</b> element allows conditional execution. It has a required attribute test, which must contain a script expression that evaluates to a boolean.<br />
                    The if element will have a <b>then</b> and may also include <b>else</b> child elements.
                    Each contains an activity (or sequence of activities) that will be executed depending on the value of the test attribute.<br />
                    <i>An if activity will include a <b>then</b>, if you wish to add an <b>else</b> highlight the if activity and select insert else from the <b>Edit</b> menu.</i>
                    <pre>
&lt;set var="x" value="${1}" /&gt;
&lt;if test="${x > 0}"&gt;
  &lt;then&gt;
    &lt;sequence&gt;
   &lt;!-- some activities to do here --&gt;
    &lt;/sequence&gt;
  &lt;/then&gt;
  &lt;else&gt;
    &lt;script&gt;
   &lt;body&gt;
   print('test was false');
   &lt;/body&gt;
    &lt;/script&gt;
  &lt;/else&gt;
&lt;/if&gt;                        
                    </pre> 
                </div>
              </td>
            </tr>            
            <tr>
              <td>
                Test:
              </td>
              <td>
                <xsl:choose>
                  <xsl:when test="@if-test = 'null'">
                    <textarea name="if_test" cols="130" rows="6">
                      <xsl:attribute name="id">if_test<xsl:value-of select="@key"/></xsl:attribute>
                      <xsl:attribute name="onclick">document.getElementById('if_test<xsl:value-of select="@key"/>').value='';</xsl:attribute>
                      ( the condition for the if: an expression (in ${..}) that evaluates to a boolean...)
                    </textarea>                                    
                  </xsl:when>
                  <xsl:otherwise>
                    <textarea name="if_test" cols="130" rows="6"><xsl:value-of select="@if-test"/></textarea>
                  </xsl:otherwise>
                </xsl:choose>              
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue">
                  <input class="agActionButton" type="submit" name="action" value="update if details" />
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
       |
       | Display if details for job status page
       |
       +-->
    <xsl:template match="ifObj" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <form name="if_form" id="if_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>                
              <td colspan="2">
                <div style="color: blue; background-color: lightblue; text-align: center;">If:</div>
              </td>
            </tr>           
            <tr>
              <td>
                Test:
              </td>
              <td><textarea name="if_test" cols="130" rows="6" readonly="true"><xsl:value-of select="@if-test"/></textarea></td>
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

