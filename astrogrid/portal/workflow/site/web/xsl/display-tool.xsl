<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <!--+  
       | Display tool details
       |
       | Display table containing details of step and the task that the step contains.
       | Details of parameters etc are displayed elsewhere (display-parameters.xsl). 
       | If step contains no tool then tool can be inserted into step from here.
       | Join condition can be alterred from here. - Removed as new logic available via groovy.
       |                      
       +-->
    <xsl:template match="step" mode="activity-details">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>          
        <form name="properties_form" id="properties_form">
          <table border="2" cellspacing="0" cellpadding="0">
            <tr>
              <td style="cursor: help; text-align: center; background-color: lightblue">                                     
                <xsl:element name="img">
                  <xsl:attribute name="src">/astrogrid-portal/mount/workflow/Help3.png</xsl:attribute>
                  <xsl:attribute name="alt">info</xsl:attribute>
                  <xsl:attribute name="onClick">toggleHelp('helpDiv<xsl:value-of select="@key"/>');</xsl:attribute>
                </xsl:element>              
              </td>
              <td> 
                <div style="color: blue; background-color: lightblue; text-align: center;">Step:</div>
              </td>
              <td style="cursor: help; text-align: center; background-color: lightblue">                                     
                <xsl:element name="img">
                  <xsl:attribute name="src">/astrogrid-portal/mount/workflow/Help3.png</xsl:attribute>
                  <xsl:attribute name="alt">info</xsl:attribute>
                  <xsl:attribute name="onClick">toggleHelp('taskDiv<xsl:value-of select="@key"/>');</xsl:attribute>
                </xsl:element>              
              </td>              
              <td>
                <div style="color: blue; background-color: lightblue; text-align: center;">Task:</div>
              </td>              
            </tr>
            <tr>
              <td colspan="4">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                    The <b>step</b> element is an activity that performs a call to a CEA application. This element has a required <b>name</b> attribute, <br />
                    and optional <b>variable name</b>(allows output of a step is to be used elsewhere in the workflow) and <b>description</b> child elements.<br />
                    <i>Enter a step name and optional variable name and description</i><br />
                    The step element contains a <b>tool</b> element, which specifies: the name of the CEA application to execute; which interface of the application to call; <br />
                    and the input and output <b>parameter</b> values to the tool call.<br />
                    <i>Select the tool that you wish this step to contain from the drop down list. A step <b>must</b> contain a tool.</i><br />
                    Parameters to a call are defined by <b>parameter</b> elements. The <b>name</b> attribute defines the name of the parameter, while the content of the <b>value</b> child element<br /> defines the value for this parameter.<br />
                    <i>Once you have inserted a tool into a step and updated the step details clicking on the step will show it's associated parameters and allow you to fill in their values.</i>
                </div>
              </td>
            </tr>
            <tr>
              <td colspan="4">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">taskDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                    The dropdown list contains a selection of 15 <b>tasks</b> that can be inserted into this <b>step</b>. <br />
                    Further tasks can be chosen by browsing the registry. As other tasks are selected they will be included as one <br />
                    of the 15 tasks in the dropdown so you do not have to keep browsing into the registry for the tasks you use most frequently. <br />
                    A task will have a number of parameters associated with it, clicking on the step itself will bring up these parameters and you <br />
                    can enter the desired values. 
                </div>
              </td>
            </tr>                        
            <tr>
              <td>Step name:</td>
              <td>
                <input type="text" size="50" name="step_name">
                <xsl:attribute name="id">step_name<xsl:value-of select="@key"/></xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:choose>
                    <xsl:when test="@step-name = 'null'"></xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="@step-name"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:attribute>
                </input>
              </td>
              <td rowspan="2">Task name:</td>
              <td>
                <input type="text" size="50" name="tool_name">
                <xsl:attribute name="id">tool_name<xsl:value-of select="@key"/></xsl:attribute>
                <xsl:attribute name="value">
                  <xsl:choose>
                    <xsl:when test="tool/@tool-name = 'null'">
                      ( Please select a tool... )       
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="tool/@tool-name"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:attribute>
                <xsl:value-of select="tool/@tool-title"/> : <xsl:value-of select="tool/@tool-interface"/>
                </input>
              </td>                                      
            </tr>
            <tr>
              <td>Var. name:</td>
              <td>
                <input type="text" size="50" name="step_var">
                  <xsl:choose>
                    <xsl:when test="@step-var = 'null'">
                      <xsl:attribute name="value"></xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:attribute name="value"><xsl:value-of select="@step-var"/></xsl:attribute>
                    </xsl:otherwise>
                  </xsl:choose>
                </input>                  
              </td>
              <td>
                <div>                                                
                  <select size="1">
                    <xsl:attribute name="id">select<xsl:value-of select="@key"/></xsl:attribute>
                    <xsl:attribute name="onChange">if(document.getElementById('select<xsl:value-of select="@key"/>').value=='browse') javascript:void(window.open('/astrogrid-portal/lean/mount/registry/registrybrowser.html?mainelement=Tool&amp;parent_authId=tool_name<xsl:value-of select="@key"/>', 'RegistryMicro', 'toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=450, height=500')); 
                                                   else if (document.getElementById('select<xsl:value-of select="@key"/>').value=='none') alert('Please select....');
                                                   else document.getElementById('tool_name<xsl:value-of select="@key"/>').value = document.getElementById('select<xsl:value-of select="@key"/>').value;
                    </xsl:attribute>
	                <option value="none" selected="true">-- Select task --</option>
	                <option value="browse">-- Browse registry --</option>
                    <xsl:for-each select="//interface">
                      <xsl:element name="option">
                        <xsl:attribute name="value"><xsl:value-of select="../@tool-name"/>#<xsl:value-of select="@int-name" /></xsl:attribute>
				        <xsl:value-of select="../@tool-name"/> : <xsl:value-of select="@int-name" /> : <xsl:value-of select="../@tool-UIname"/>
				      </xsl:element>
                    </xsl:for-each>
		          </select>                                                                                                                                                
                </div>                                         
              </td>                                        
            </tr>
            <tr>
              <td>Description:</td>
              <td>
                <textarea name="step_description" cols="40" rows="3"><xsl:value-of select="normalize-space(@step-description)"/></textarea>
              </td> 
              <td>Description:</td>
              <td>
                <textarea name="tool_documentation" cols="75" rows="3" readonly="true"><xsl:value-of select="tool/@tool-documentation"/>...</textarea>
              </td>
            </tr>                                    
            <tr>
              <td colspan="4">
                <div style="text-align: center; background-color: lightblue;">
                  <input class="agActionButton" type="submit" name="action" value="update step details" />
                </div>
              </td>
            </tr>                        
          </table>
          <input type="hidden" name="display_tool_values"><xsl:attribute name="value">true</xsl:attribute></input>
          <input type="hidden" name="activity_key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input>            
        </form>                
      </div>
    <xsl:apply-templates select="*" mode="activity-details"/>      
    </xsl:template>


  <!--+
       |
       | Display tool details for job status page
       | 
       +-->
    <xsl:template match="step" mode="job-status">
      <div style="display: none"><xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>          
        <form name="properties_form" id="properties_form">
          <table border="2" cellspacing="0" cellpadding="0">
            <tr>
              <td colspan="2"> 
                <div style="color: blue; background-color: lightblue; text-align: left;"><b>Step:</b> <xsl:value-of select="normalize-space(@step-name)"/></div>
              </td>
              <td>
                <div style="color: blue; background-color: lightblue; text-align: left;"><b>Task:</b> <xsl:value-of select="normalize-space(@step-tool)"/></div>
              </td>
              <td colspan="2">
                <div style="color: blue; background-color: lightblue; text-align: left;"><b>Desc:</b> <xsl:value-of select="normalize-space(@step-description)"/></div>
              </td>              
            </tr>
            <xsl:choose>
              <xsl:when test="@step-status = 'Not submitted'">
                <tr>
                  <td colspan="4">
                    This step was not submitted to JES...
                  </td>
                </tr>
              </xsl:when>    
              <xsl:otherwise>
                <xsl:call-template name="executionRecord"/>                       
              </xsl:otherwise>
            </xsl:choose>             
            <tr>
              <td colspan="4">
                <div style="text-align: center; background-color: lightblue;">...
                </div>
              </td>
            </tr>                        
          </table>           
        </form>                
      </div>
    <xsl:apply-templates select="*" mode="job-status"/>      
    </xsl:template>

            
</xsl:stylesheet>
