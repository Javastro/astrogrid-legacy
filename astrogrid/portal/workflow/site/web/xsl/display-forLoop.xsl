<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <!--+
       | Display forLoop details
       |
       | Display table containing details of forLoop.
       |
       +-->
    <xsl:template match="forObj" mode="activity-details">  
                
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <form name="forLoop_form" id="forLoop_form">
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
                <div style="color: blue; background-color: lightblue; text-align: center;">For loop:</div>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                  The for element expresses a for loop. <br/>
                  The structure of the for loop is similar to the for in Python (or for-each in Javascript) - it iterates over a sequence, rather than using an arithmetic expression like in Java / C / C++.
                  The for element has two required attributes: <b>items</b> which must evaluate to a list of items to iterate over; and <b>var</b> which provides the name of the loop variable to assign each element of the list to. 
                  The <b>body</b> of the for element is an activity (presently a sequence of activites) that will be executed for each item on the list. 
                  <pre>
&lt;!-- count up to 10 --&gt;
&lt;for var="x" items="${1...10}&gt; &lt;!-- start ... finish is groovy syntax for numeric ranges --&gt;
   &lt;script&gt;
      &lt;body&gt;
         print(x);
      &lt;/body&gt;
   &lt;/script&gt;
&lt;/for&gt;
                  </pre>
                </div>
              </td>
            </tr>                    
            <tr>
              <td>Var:</td>
              <td>
                <input type="text" size="80" name="for_get">
                  <xsl:choose>
                    <xsl:when test="@for-get = 'null'">
                      <xsl:attribute name="value"></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="value"><xsl:value-of select="@for-get"/></xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                </input>                                 
              </td>              
            </tr>
            <tr>
              <td>
                Items:
              </td>
              <td>
                <xsl:choose>
                  <xsl:when test="@for-item = 'null'">
                    <textarea name="for_item" cols="130" rows="4">
                      <xsl:attribute name="id">for_item<xsl:value-of select="@key"/></xsl:attribute>
                      <xsl:attribute name="onclick">document.getElementById('for_item<xsl:value-of select="@key"/>').value='';</xsl:attribute>
                      (A sequence or iterator of items - the loop variable will be assigned to each in turn, and then loop body executed...)                       
                    </textarea>                                    
                  </xsl:when>
                  <xsl:when test="@for-item = ''">
                    <textarea name="for-item" cols="130" rows="3">
                      <xsl:attribute name="id">for-item-empty<xsl:value-of select="@key"/></xsl:attribute>                        
                      <xsl:attribute name="onclick">document.getElementById('for-item-empty<xsl:value-of select="@key"/>').value='';
                                                    document.getElementById('for-item-empty<xsl:value-of select="@key"/>').onclick='';
                       </xsl:attribute>
                      ...
                    </textarea>                      
                  </xsl:when>                  
                  <xsl:otherwise>
                    <textarea name="for_item" cols="130" rows="2"><xsl:value-of select="@for-item"/></textarea>                
                  </xsl:otherwise>
                </xsl:choose>                  
              </td>
            </tr>            
            <tr>
              <td colspan="2">
                <div style="text-align: center; background-color: lightblue">
                  <input class="agActionButton" type="submit" name="action" value="update for loop details" />
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
       | Display forLoop details for job status page
       |
       | Display table containing details of forLoop.
       |
       +-->
    <xsl:template match="forObj" mode="job-status">  
                
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
        <form name="forLoop_form" id="forLoop_form">
          <table width="10%"  border="2" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="2">    
                <div style="color: blue; background-color: lightblue; text-align: center;">For loop:</div>
              </td>
            </tr>                    
            <tr>
              <td>Var:</td>
              <td>
                <textarea name="for_get" cols="130" rows="2" readonly="true"><xsl:value-of select="@for-get"/></textarea>                                                      
              </td>
            </tr>
            <tr>
              <td>Items:</td>
              <td>
                <textarea name="for_item" cols="130" rows="2" readonly="true"><xsl:value-of select="@for-item"/></textarea>
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
