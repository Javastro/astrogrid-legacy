<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->
       
  <!--+
       | Display script details
       |
       | Display table containing details of script...
       |       
       +-->
    <xsl:template match="script" mode="activity-details" >
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="script_form" id="script_form" method="post">
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
                <div style="color: blue; background-color: lightblue; text-align: center;">Script:</div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                    The <b>script</b> element is an activity that executes some inline scripting code. This is a very versatile activity.<br />
                    The script element contains an optional <b>description</b> element; a mandatory <b>body</b> element that contains the script text, and a step-execution-record which records the execution of the script.
                    A script may reference workflow variables, reading and storing data in them. The changes to the workflow variables are visible further on in the workflow. <br />
                    A script may also define local variables, functions, etc. However, these are only available to the script itself - they are not visible to subsequent scripts or script expressions.
                    Hence any result that is to be accessed later should be stored in a previously-defined workflow variable. 
                    <pre>
&lt;!-- in the variable 'a' store the value 'hello world' (java.lang.String) --&gt;
&lt;set var="a" value="hello world" /&gt;
&lt;!-- in the variable 'b', store the value '1' (java.lang.String) --&gt;
&lt;set var="b" value="1" /&gt;
&lt;!-- in the variable 'c', store the value 1 (java.lang.Integer) --&gt;
&lt;set var="c" value="${1}" /&gt;
&lt;!-- in the variable 'd' store the value '1 + 1' (java.lang.String) --&gt;
&lt;set var="d" value="${c} + 1" /&gt;
&lt;!-- in the variable 'e' store the value 2 (java.lang.Integer) --&gt;
&lt;set var="e" value="${c + 1}" /&gt;
&lt;!-- in the variable 'now' store the current date (java.lang.Date) --&gt;
&lt;set var="now" value="${new java.util.Date()}" /&gt;
&lt;!-- in the variable 'nowString' store the current date (java.lang.String) - note trailing space --&gt;
&lt;set var="nowString value="${new java.util.Date()} " /&gt;
&lt;!-- in the variable 'f' store the value '1 - hello world and goodbye." --&gt;
&lt;set var="f" value="${c} - ${a} and goodbye. "/&gt;
&lt;!-- in the variable 'u' store the value 'http://www.astrogrid.org' (java.net.URL) --&gt;
&lt;set var="u" value="${new java.net.URL('http://www.astrogrid.org')} /&gt;
&lt;!-- in the variable 'scheme' store the value 'http' (java.lang.String) --&gt;
&lt;set var="scheme" value="${u.getScheme()}" /&gt;                        
                    </pre>
                </div>
              </td>
            </tr>             
            <tr>
              <td>
                Description:
              </td>
              <td>
                <input type="text" size="80" name="script_description">
                  <xsl:choose>
                    <xsl:when test="@script-desc = 'null'">
                      <xsl:attribute name="value"></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="value"><xsl:value-of select="@script-desc"/></xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                </input>                  
              </td>
            </tr>
            <tr>
              <td>
                Body:
              </td>
              <td>
                <xsl:choose>
                  <xsl:when test="@script-body = 'null'">
                    <textarea name="script_body" cols="120" rows="5">
                      <xsl:attribute name="id">script_body<xsl:value-of select="@key"/></xsl:attribute>
                      <xsl:attribute name="onclick">document.getElementById('script_body<xsl:value-of select="@key"/>').value='';
                                                    document.getElementById('script_body<xsl:value-of select="@key"/>').onclick='';
                      </xsl:attribute>
                      ( script statements to execute....)
                    </textarea>                                    
                  </xsl:when>
                  <xsl:otherwise>
                    <textarea name="script_body" cols="120" rows="5"><xsl:value-of select="@script-body"/></textarea>                
                  </xsl:otherwise>
                </xsl:choose>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue;">
                  <input class="agActionButton" type="submit" name="action" value="update script details" />
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
       |
       | Display script details for job status page
       |
       +-->
    <xsl:template match="script" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute> 
        <form name="script_form" id="script_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>                
              <td colspan="4">
                <div style="color: blue; background-color: lightblue; text-align: center;">Script:</div>
              </td>
            </tr>             
            <tr>
              <td>
                Description:
              </td>
              <td colspan="3"><xsl:value-of select="@script-desc"/></td>
            </tr>
            <tr>
              <td>Body:</td>
              <td colspan="3"><textarea name="script_body" cols="120" rows="2"><xsl:value-of select="@script-body"/></textarea></td>
            </tr>
            <xsl:call-template name="executionRecord"/>
            <tr>
              <td colspan="4">
                <div style="text-align: center; background-color: lightblue;">...</div>
              </td>
            </tr>            
          </table>
        </form>
      </div> 
      <xsl:apply-templates select="*" mode="job-status"/>                                                                                                 
    </xsl:template>    
        
</xsl:stylesheet>
