<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   May 2005
       +--> 
       
  
      <!--+
          | Match the root element.
          +-->
      <xsl:template match="/">
         <ag-div>
            <agComponentTitle>Parameterized Workflows</agComponentTitle>
            <xsl:apply-templates/>               
         </ag-div>
      </xsl:template>
      
      <xsl:template match="paramWf">      
        <table>
          <tr>
            <td nowrap="true" style="color: blue; background-color: lightblue; text-align: center; font-weight: bold">Name</td>
            <td style="color: blue; background-color: lightblue; text-align: center; font-weight: bold">Description</td>          
            <td style="color: blue; background-color: lightblue; text-align: center;">Select</td>
          </tr>
          <xsl:for-each select="//list"> <!-- if list of parameterized workflows grows to more than ~ 20 will need to add frame for list--> 
            <xsl:sort select="@name" />
            <form name="paramWf_form" action="/astrogrid-portal/main/mount/workflow/agparameterizedWf.html?action=create_paramWf" target="_top" method="post" >
              <tr>
                <td><xsl:value-of select="@name" /></td>
                <td><xsl:value-of select="@desc" /></td>
                <td><input class="agActionButton" type="submit" value="select" /></td>
              </tr>
              <input type="hidden" name="paramWf_name"><xsl:attribute name="value"><xsl:value-of select="@name"/></xsl:attribute></input>
            </form>
          </xsl:for-each>
        </table> 
        <p/>
        <hr/>       
        <p/>                                        
        <xsl:apply-templates/>     
      </xsl:template> 
   
      <xsl:template match="paramWfTemplate"> 
        <table border="1">
          <form name="update_paramWf_form" id="update_paramWf_form" action="/astrogrid-portal/main/mount/workflow/agparameterizedWf.html" target="_top" method="post" >
            <tr>
              <td style="color: blue; background-color: lightblue; font-weight: bold" colspan="2">Name: <xsl:value-of select="@name"/></td>
              <td style="color: blue; background-color: lightblue; font-weight: bold" colspan="4">Desc: <xsl:value-of select="@desc"/></td>
            </tr>
            <tr>
              <td style="color: blue; background-color: lightblue;" align="center">Name: </td>
              <td style="color: blue; background-color: lightblue;" align="center">Desc: </td>
              <td style="color: blue; background-color: lightblue;" align="center">Value: </td>
              <td style="color: blue; background-color: lightblue;" align="center">Type: </td>
              <td style="color: blue; background-color: lightblue;" align="center">UCD: </td>
              <td style="color: blue; background-color: lightblue;" align="center">Units: </td>
            </tr>          
            <xsl:apply-templates/>
            <tr>
              <td style="color: blue; background-color: lightblue; font-weight: bold" align="center" valign="top" colspan="6">
                <input class="agActionButton" type="button" value="Execute">                                
                  <xsl:attribute name="onClick">                
                    if(document.getElementById('executeAndSave').checked) 
                    {
                      void(window.open('/astrogrid-portal/bare/mount/myspace/myspace-micro?ivorn=paramWf_save_ivorn_tag&amp;form_name=update_paramWf_form&amp;field_name=action&amp;field_value=execute_paramWf&amp;requested-mode=save-workflow-file', 'mySpaceMicro', 'toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=650, height=300'));
                    }
                    else
                    {
                      document.getElementById('action').value = "execute_paramWf";
                      document.getElementById('update_paramWf_form').submit();
                    }
                  </xsl:attribute>                
                </input>               
                ( save copy of workflow created
                <input type="checkbox" id="executeAndSave" name="executeAndSave"/>               
                  )
              </td>
            </tr>
          <input type="hidden" name="paramWf_name"><xsl:attribute name="value"><xsl:value-of select="@name"/></xsl:attribute></input>
          <input type="hidden" name="action" id="action" />
          <input type="hidden" name="paramWf_save_ivorn_tag" id="paramWf_save_ivorn_tag" />          
          </form>
        </table>                
      </xsl:template>  
           
 
      <xsl:template match="InputParams">         
        <tr>
          <td><xsl:value-of select="@UIName"/></td>
          <td><xsl:value-of select="@UIDesc"/></td>
          <td>
            <input title="Value of this input parameter" 
                   type="text"  
                   onblur="defocusit(this)" 
                   onfocus="focusit(this)" >
                   <xsl:attribute name="name">inputParam_<xsl:value-of select="@Name"/></xsl:attribute>
            </input>
          </td>
          <td><xsl:value-of select="@Type"/></td>
          <td><xsl:value-of select="@UCD"/></td>
          <td><xsl:value-of select="@Units"/></td>
        </tr>      
        <xsl:apply-templates/>
      </xsl:template>          
 
 
      <xsl:template match="OutputParams">         
        <tr>
          <td><xsl:value-of select="@UIName"/></td>
          <td><xsl:value-of select="@UIDesc"/></td>
          <td>
            <input title="Value of this output parameter" 
                   type="text"  
                   onblur="defocusit(this)" 
                   onfocus="focusit(this)" >
                   <xsl:attribute name="name">outputParam_<xsl:value-of select="@Name"/></xsl:attribute>
            </input>
          </td>
          <td><xsl:value-of select="@Type"/></td>
          <td><xsl:value-of select="@UCD"/></td>
          <td><xsl:value-of select="@Units"/></td>
        </tr>      
        <xsl:apply-templates/>
      </xsl:template>    
      
      <xsl:template match="messages">
        <table>
          <tr>
            <xsl:if test="@paramWf_submit_message != ''"> 
            <xsl:if test="@paramWf_submit_message != 'null'">                                                  
              <td>
                <span style="color:green">
                  Parameterized workflow successfully submitted to JES with a Job ID of: <xsl:value-of select="@paramWf_submit_message"/>  ;you can view its status in the <a href="/astrogrid-portal/main/mount/workflow/agjobmanager-jes.html?action=read-job-list">job monitor</a> page.      
                </span>
              </td>
            </xsl:if>
            </xsl:if>
            <xsl:if test="@paramWf_error_message != ''">
            <xsl:if test="@paramWf_submit_message != 'null'">                                                   
              <td>
                <span style="color:red">
                  Error:
                </span>
              </td>
            </xsl:if>
            </xsl:if>
          </tr>
        </table>      
      </xsl:template>             
  
        
     <!-- Default, copy all and apply templates -->
     <xsl:template match="@*|node()">
       <xsl:copy>
         <xsl:apply-templates select="@*|node()" />
       </xsl:copy>
     </xsl:template>
   </xsl:stylesheet>
