<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->
       
  <!--+
       | Display whileLoop details
       |
       | Display table containing details of whileLoop.
       |
       +-->
    <xsl:template match="whileObj" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <form name="whileLoop_form" id="whileLoop_form" method="post">
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
                <div style="color: blue; background-color: lightblue; text-align: center;">While loop:</div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>
                    The while element expresses a while loop.<br /> 
                    It has a required attribute <b>test</b> which must contain a script expression that evaluates to a boolean.
                    Its body is an activity (presently a sequence of activities) that will be executed for every time that the test evaluates to true.                    
                    <pre>
<!-- repeatedly execute a step until it returns at least 1 result value -->
   &lt;while test="${results == null || results.size() &lt; 1}"&gt;
      &lt;sequence&gt;
         &lt;step result-var="results"&gt;
            &lt;!-- omitted --&gt;
         &lt;/step&gt;
      &lt;/sequence&gt;
   &lt;/while&gt;
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
                  <xsl:when test="@while-test = 'null'">
                    <textarea name="while_test" cols="130" rows="6">
                      <xsl:attribute name="id">while_test<xsl:value-of select="@key"/></xsl:attribute>
                      <xsl:attribute name="onclick">document.getElementById('while_test<xsl:value-of select="@key"/>').value='';</xsl:attribute>
                      (the condition for the loop: an expression (in ${..}) that evaluates to a boolean...)
                    </textarea>                                    
                  </xsl:when>
                  <xsl:when test="@while-test = ''">
                    <textarea name="while-test" cols="130" rows="3">
                      <xsl:attribute name="id">while-test-empty<xsl:value-of select="@key"/></xsl:attribute>                        
                      <xsl:attribute name="onclick">document.getElementById('while-test-empty<xsl:value-of select="@key"/>').value='';
                                                    document.getElementById('while-test-empty<xsl:value-of select="@key"/>').onclick='';
                       </xsl:attribute>
                      ...
                    </textarea>                      
                  </xsl:when>                  
                  <xsl:otherwise>
                    <textarea name="while_test" cols="130" rows="6"><xsl:value-of select="@while-test"/></textarea>                
                  </xsl:otherwise>
                </xsl:choose>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue">
                  <input class="agActionButton" type="submit" name="action" value="update while loop details" />
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
       | Display whileLoop details for job status page
       |
       | Display table containing details of whileLoop.
       |
       +-->
    <xsl:template match="whileObj" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <form name="whileLoop_form" id="whileLoop_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>                
              <td colspan="2">
                <div style="color: blue; background-color: lightblue; text-align: center;">While loop:</div>
              </td>
            </tr>            
            <tr>
              <td>Test:</td>
              <td>
                <textarea name="while_test" cols="130" rows="6" readonly="true"><xsl:value-of select="@while-test"/></textarea>
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

