<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 
  
      <!--+
          | Match the root element.
          +-->
      <xsl:template match="/">
         <ag-div>
            <agComponentTitle>Job list</agComponentTitle>
            <ag-script type="text/javascript" src="/astrogrid-portal/mount/workflow/workflow-functions.js"/>                                         
            <xsl:apply-templates/>               
         </ag-div>
      </xsl:template>
  
      <!--+
          | Match the workflow element.
          +-->
      <xsl:template match="workflow">
      <p />          
         <table border="2" cellpadding="0" cellspacing="0">
            <tr>
               <td nowrap="true" style="color: blue; background-color: lightblue; text-align: center;">Name</td>
               <td nowrap="true" style="color: blue; background-color: lightblue; text-align: center;">Description</td>
               <td nowrap="true" style="color: blue; background-color: lightblue; text-align: center;">Time submitted</td>
               <td nowrap="true" style="color: blue; background-color: lightblue; text-align: center;">Status</td>
               <td nowrap="true" style="color: blue; background-color: lightblue; text-align: center;">
                  <div class="jobIdColumn" style="display: none;">
                     <xsl:attribute name="id">full_column_heading</xsl:attribute>               
                     <xsl:element name="a">  
                        <xsl:attribute name="onClick">toggleColumn('jobIdColumn');</xsl:attribute>                
                        <xsl:attribute name="href">javascript:void(0);</xsl:attribute> 
                        <xsl:attribute name="class">jobIdColumn</xsl:attribute>
                        <xsl:attribute name="NOWRAP">true</xsl:attribute>
                     </xsl:element>
                     <font>
                        <small><b>[less]</b></small>
                     </font>
                     <xsl:element name="/a"></xsl:element> 
                  </div>
                  <div class="jobIdColumn">
                     <xsl:attribute name="id">short_column_heading</xsl:attribute>              
                     <xsl:element name="a">  
                        <xsl:attribute name="onClick">toggleColumn('jobIdColumn');</xsl:attribute>                
                        <xsl:attribute name="href">javascript:void(0);</xsl:attribute>
                        <xsl:attribute name="class">jobIdColumn</xsl:attribute>
                        <xsl:attribute name="NOWRAP">true</xsl:attribute>
                     </xsl:element>
                     <font>
                        <small><b>[more]</b></small>
                     </font>
                     <xsl:element name="/a"></xsl:element> 
                  </div>                                      
                  Job ID   
               </td>
               <td style="color: blue; background-color: lightblue; text-align: center;">Delete/Cancel?</td> 
            </tr>
            <xsl:for-each select="//job">
               <tr>    
                  <td nowrap="true"><xsl:value-of select="@name"/></td>
                  <td nowrap="true"><xsl:value-of select="@description"/></td>
                  <td nowrap="true"><xsl:value-of select="@time"/></td>
                  <td nowrap="true"><xsl:value-of select="@status"/></td>
                  <td nowrap="true">
                     <xsl:choose> 
                        <xsl:when test="@status = 'n/a'">  <!--  EMPLTY LIST -->
                           n/a
                        </xsl:when>
                        <xsl:otherwise>
                           <div class="jobIdColumn" style="display: none;">                      
                              <xsl:attribute name="id">full_<xsl:value-of select="@jobid"/></xsl:attribute> 
                              <xsl:element name="a">
                                 <xsl:attribute name="href">/astrogrid-portal/main/mount/workflow/agjobmanager-job-status.html?action=read-job&amp;jobURN=<xsl:value-of select="@jobid"/></xsl:attribute>
                              </xsl:element>
                              <xsl:value-of select="@jobid"/>
                              <xsl:element name="/a"></xsl:element>
                           </div>
                           <div class="jobIdColumn">
                           <xsl:attribute name="id">short_<xsl:value-of select="@jobid"/></xsl:attribute>
                           <xsl:element name="a">                  
                              <xsl:attribute name="href">/astrogrid-portal/main/mount/workflow/agjobmanager-job-status.html?action=read-job&amp;jobURN=<xsl:value-of select="@jobid"/></xsl:attribute>
                              <xsl:attribute name="class">jobIdColumn</xsl:attribute>              
                           </xsl:element>
                              ......<xsl:value-of select="@jobid-short"/>
                           <xsl:element name="/a"></xsl:element>                                                       
                        </div>                           
                        </xsl:otherwise>                                                                 
                     </xsl:choose>                     
                     </td>
                     <td align="center">
                       <xsl:choose>                                      
                          <xsl:when test="@status = 'ERROR'">  <!--  ERROR -->
                              <form action="/astrogrid-portal/main/mount/workflow/agjobmanager-jes.html" name="job_form">
                                  <xsl:element name="input">                              
                                      <xsl:attribute name="type">hidden</xsl:attribute>
                                      <xsl:attribute name="name">jobURN</xsl:attribute>
                                      <xsl:attribute name="value"><xsl:value-of select="@jobid" /></xsl:attribute>
                                  </xsl:element>                                                                                                        
                                  <input type="submit" name="action" value="delete-job"/>                          
                              </form>
                          </xsl:when>
                          <xsl:when test="@status = 'COMPLETED'">  <!--  COMPLETED -->
                              <form action="/astrogrid-portal/main/mount/workflow/agjobmanager-jes.html" name="job_form">
                                  <xsl:element name="input">                              
                                      <xsl:attribute name="type">hidden</xsl:attribute>
                                      <xsl:attribute name="name">jobURN</xsl:attribute>
                                      <xsl:attribute name="value"><xsl:value-of select="@jobid" /></xsl:attribute>
                                  </xsl:element>                                                                                                        
                                  <input type="submit" name="action" value="delete-job"/>                          
                              </form>
                          </xsl:when> 
                          <xsl:when test="@status = 'n/a'">  <!--  EMPLTY LIST -->
                              ----
                          </xsl:when>                                                                                                       
                          <xsl:otherwise>
                              <form action="/astrogrid-portal/main/mount/workflow/agjobmanager-jes.html" name="job_form">
                                  <xsl:element name="input">                              
                                      <xsl:attribute name="type">hidden</xsl:attribute>
                                      <xsl:attribute name="name">jobURN</xsl:attribute>
                                      <xsl:attribute name="value"><xsl:value-of select="@jobid" /></xsl:attribute>
                                  </xsl:element>                                                                                                        
                                  <input type="submit" name="action" value="cancel-job"/>                          
                              </form>                        
                          </xsl:otherwise>
                      </xsl:choose>                                                                                                                                                                                      
                  </td>                 
               </tr>
            </xsl:for-each>
         </table>                                                                                                                
      </xsl:template>
    
    
     <!-- Default, copy all and apply templates -->
     <xsl:template match="@*|node()">
       <xsl:copy>
         <xsl:apply-templates select="@*|node()" />
       </xsl:copy>
     </xsl:template>
   </xsl:stylesheet>
