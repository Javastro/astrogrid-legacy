<?xml version="1.0"?>

<xsl:stylesheet
   version="1.0"
   xmlns="http://www.astrogrid.org/portal"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>

      <xsl:param name="action" />
      <xsl:param name="errormessage" />
      <xsl:param name="activity-key" />  
  
      <!--+
          | Match the root element.
          +-->
      <xsl:template match="/">
         <html>
            <head>
               <link rel="stylesheet" type="text/css" href="main.css" />              
               <title>AstroGrid Portal</title>
            </head>
            <body onload="verifyName(); ">
               <div> <!-- div encloses content to be displayed in main portal pages -->          
                  <xsl:apply-templates/>
               </div> <!-- End div encloses content to be displayed in main portal pages -->
            </body>
         </html>
      </xsl:template>
  
      <!--+
          | Match the workflow element.
          +-->
      <xsl:template match="workflow">
         <xsl:call-template name="main_menu"/>
         <p />
         <xsl:if test="$action = ''">
            <xsl:call-template name="list_workflow" />
            <xsl:call-template name="list_query" />
            <xsl:call-template name="list_tools" />             
         </xsl:if>             
         <xsl:if test="$action = 'new'">
            <xsl:call-template name="new_workflow" />             
         </xsl:if>
         <xsl:if test="$action = 'submit'">
            <xsl:call-template name="list_workflow"/>
            <xsl:call-template name="submit_workflow" />             
         </xsl:if>              
         <xsl:if test="$action = 'view'">
            <xsl:call-template name="list_workflow"/>
            <xsl:call-template name="view_workflow" />             
         </xsl:if>   
         <xsl:if test="$action = 'delete'">
            <xsl:call-template name="list_workflow"/>
            <xsl:call-template name="delete_workflow" />             
         </xsl:if>
         <xsl:if test="$action = 'delete-workflow'">
            <xsl:call-template name="list_workflow"/>             
         </xsl:if>
         <xsl:if test="$action = 'submit-workflow'">
            <xsl:call-template name="list_workflow"/>             
         </xsl:if>
         <xsl:if test="$action = 'save-workflow'">
            <xsl:call-template name="list_workflow"/>
            <xsl:call-template name="list_query" />
            <xsl:call-template name="list_tools" />                         
         </xsl:if>
         <xsl:if test="$action = 'read-workflow'">
            <xsl:call-template name="view_workflow_details"/>             
         </xsl:if>
         <xsl:if test="$action = 'create-workflow'">
            <xsl:call-template name="create_workflow"/>             
         </xsl:if>
         <xsl:if test="$action = 'insert-tool-into-step'">
<!--             <xsl:call-template name="create_workflow"/> -->
            <xsl:call-template name="insert_parameter"/> 
         </xsl:if>
         <xsl:if test="$action = 'insert-input-value'">
            <xsl:call-template name="insert_parameter"/>             
         </xsl:if>
         <xsl:if test="$action = 'insert-output-value'">
            <xsl:call-template name="insert_parameter"/>             
         </xsl:if>   
         <xsl:if test="$action = 'reset-parameter'">
            <xsl:call-template name="insert_parameter"/>             
         </xsl:if>                        
         <xsl:if test="$action = 'parameters-complete'">
            <xsl:call-template name="create_workflow"/>             
         </xsl:if>         
         <xsl:if test="$action = 'templates'">
            <xsl:call-template name="templates"/>             
         </xsl:if>
         <p />                                                                                                                                     
      </xsl:template>

      <!--+
          | Main menu
          +-->              
      <xsl:template name="main_menu">                                
         <table id="ag-main">                                
            <tr>
               <td class="ag-content-tab-top" colspan="5" align="center">Design-Jobs</td>                 
            </tr>
            <tr>                
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmanager.html?action=new">New</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmanager.html?action=submit">Submit</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmanager.html?action=view">View</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmanager.html?action=delete">Delete</a></td>                  
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmanager.html?action=templates">Templates</a></td>
            </tr>
         </table>
      </xsl:template>  
  
      <!--+
          | Display workflows currently stored in MySpace
          +-->   
      <xsl:template name="list_workflow">
         <table>
            <tr>
               <td>Workflows currently stored in your mySpace:</td>
            </tr>
            <p />
            <xsl:for-each select="//workflow">
               <tr>
                  <xsl:if test="@workflow-name != 'null'">
                     <td><xsl:value-of select="@workflow-name"/></td>
                     <td><xsl:value-of select="@workflow-description"/></td>
                  </xsl:if>
               </tr>
            </xsl:for-each>
         </table>
         <p /> 
      </xsl:template> 
      
      <!--+
          | Display queries currently stored in MySpace
          +-->   
      <xsl:template name="list_query">          
         <table>
            <tr>
               <td>Queries currently stored in your mySpace:</td>
            </tr>
            <p />
            <xsl:for-each select="//query">
               <tr>
                  <xsl:if test="@query-name != 'null'">
                     <td><xsl:value-of select="@query-name"/></td>
                     <td><xsl:value-of select="@query-description"/></td>
                  </xsl:if>
               </tr>
            </xsl:for-each>
         </table>
         <p /> 
      </xsl:template>

      <!--+
          | Display tools currently available
          +-->   
      <xsl:template name="list_tools">          
         <tr>
            <td>Tools currently available for use:</td>
         </tr>
         <p />      
         <select name="tool-name" size="5">
            <xsl:for-each select="//toolsAvailable">
               <xsl:element name="option">
                  <xsl:attribute name="value"><xsl:value-of select="@tool-name"/></xsl:attribute>
                  <xsl:value-of select="@tool-name"/>
		       </xsl:element>
            </xsl:for-each>
		 </select>
      </xsl:template>
    
      <!--+
          | New workflow
          +-->          
      <xsl:template name="new_workflow">
         <form name="new_form" method="get">               
            <table cellpadding="0" cellspacing="0">                                                           
               <tr>
                  <td>Job Name:</td>
                  <td><input type="text" name="workflow-name" /></td>
               </tr>
               <tr>
                  <td>Job Description:</td>
                  <td><input type="text" name="workflow-description" /></td>
               </tr>
               <tr>
                  <td>
                     <ul>
                        <li><input type="radio" name="template" value="OneStepJob" checked="true" />one step sequence</li>
                        <li><input type="radio" name="template" value="TwoSequentialJobsteps" />two step sequence</li>
                        <li><input type="radio" name="template" value="TwoParallelJobsteps" />two step flow</li>
                        <li><input type="radio" name="template" value="TwoStepFlowAndMerge" />sequence with two step flow</li>
                        <li><input type="radio" name="template" value="ComplexWorkflow" />complex flow</li>
                     </ul>
                  </td>
               </tr>
               <tr>
                  <td><input type="submit" value="Create" /></td>
                  <input type="hidden" name="action" value="create-workflow" />
               </tr>                                                              
            </table> 
         </form>     
      </xsl:template>
  
      <!--+
          | Create workflow
          +-->          
      <xsl:template name="create_workflow">               
         <table border="0" cellpadding="0" cellspacing="0">                                                           
            <tr>
               <td>Workflow Name:</td>
               <td><xsl:value-of select="@workflow-name" /></td>                                    
            </tr>
            <tr>
               <td>Workflow Description:</td>
               <td><xsl:value-of select="@workflow-description" /></td>                  
            </tr>
         </table>
         <p />
         <table border="0" cellpadding="0" cellspacing="0">
            <tr>
               <td>
                  <xsl:if test="@template = 'OneStepJob'"><img src="/astrogrid-portal/mount/workflow/OneStepJob.gif" width="110" height="200" alt="OneStepJob" /></xsl:if>
                  <xsl:if test="@template = 'TwoSequentialJobsteps'"><img src="/astrogrid-portal/mount/workflow/TwoStepSequence.gif" width="110" height="260" alt="TwoStepSequence" /></xsl:if>
                  <xsl:if test="@template = 'TwoParallelJobsteps'"><img src="/astrogrid-portal/mount/workflow/TwoStepFlow.gif" width="200" height="260" alt="TwoStepFlow" /></xsl:if>
                  <xsl:if test="@template = 'TwoStepFlowAndMerge'"><img src="/astrogrid-portal/mount/workflow/SequenceWithTwoStepFlow.gif" width="200" height="260" alt="SequenceWithTwoStepFlow" /></xsl:if>                
                  <xsl:if test="@template = 'ComplexWorkflow'"><img src="/astrogrid-portal/mount/workflow/ComplexWorkflow.gif" width="200" height="315" alt="ComplexWorkflow" /></xsl:if> 
               </td>               
               <td>
                  <xsl:for-each select="//step">
                     <form method="get" name="ToolSelectForm">                          
                        <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input>
		                <input type="hidden" name="action" value="insert-tool-into-step" />		                     	                 		                 		                 
                        Step: (<xsl:value-of select="@step-name" />)                           
                        <xsl:for-each select="tool">
                           <xsl:if test="@tool-name = 'null'">
	   	                      <select name="tool-name">
	                             <option value="none">-- Select tool --</option>
                                 <xsl:for-each select="//toolsAvailable">
                                    <xsl:element name="option">
                                       <xsl:attribute name="value"><xsl:value-of select="@tool-name"/></xsl:attribute>
				                       <xsl:value-of select="@tool-name"/>
				                    </xsl:element>
                                 </xsl:for-each>
		                      </select>
                           <input type="submit" value="Select" />		                         
                           </xsl:if>
                           <xsl:if test="@tool-name != 'null'">
                              <xsl:value-of select="@tool-name" />
                           </xsl:if>
                        </xsl:for-each><!-- end of tool -->
                     </form>
                  </xsl:for-each><!-- end of step -->                                            
               </td>
            </tr>
            <form name="create_form">
               <tr>
                  <td>
                     <input type="hidden" name="action"  value="save-workflow" />
                     <input type="submit" value="Save workflow" />
                  </td>
               </tr>
            </form>                                                                                                                
         </table> 
      </xsl:template>


      <!--+
          | View workflow details
          +-->          
      <xsl:template name="view_workflow_details">               
         <table border="0" cellpadding="0" cellspacing="0">
            <tr>
               <td>Workflow Name:</td>
               <td><xsl:value-of select="@workflow-name" /></td>
            </tr>
            <tr>
               <td>Workflow Description:</td>
               <td><xsl:value-of select="@workflow-description" /></td>                  
            </tr>
         </table>
         <p />
         <table border="1" cellpadding="0" cellspacing="0">
            <tr>
               <td>
                  <xsl:if test="@template = 'OneStepJob'"><img src="/astrogrid-portal/mount/workflow/OneStepJob.gif" width="110" height="200" alt="OneStepJob" /></xsl:if>
                  <xsl:if test="@template = 'TwoSequentialJobsteps'"><img src="/astrogrid-portal/mount/workflow/TwoStepSequence.gif" width="110" height="260" alt="TwoStepSequence" /></xsl:if>
                  <xsl:if test="@template = 'TwoParallelJobsteps'"><img src="/astrogrid-portal/mount/workflow/TwoStepFlow.gif" width="200" height="260" alt="TwoStepFlow" /></xsl:if>
                  <xsl:if test="@template = 'TwoStepFlowAndMerge'"><img src="/astrogrid-portal/mount/workflow/SequenceWithTwoStepFlow.gif" width="200" height="260" alt="SequenceWithTwoStepFlow" /></xsl:if>
                  <xsl:if test="@template = 'ComplexWorkflow'"><img src="/astrogrid-portal/mount/workflow/ComplexWorkflow.gif" width="200" height="315" alt="ComplexWorkflow" /></xsl:if>                
               </td>
               <td>
                  <table border="1" cellpadding="0" cellspacing="0">
                     <tr>
                        <td align="center">Step:</td>
                        <td align="center">Tool:</td>
                        <td align="center">Input parameters</td>   
                        <td align="center">Output parameters</td>
                     </tr>
                     <xsl:for-each select="//step">                         
                        <td><!--  start of step column -->
                           <tr>
                              <td>                        
                                 <table>
                                    <tr><td>Name: </td><td><xsl:value-of select="@step-name"/></td></tr>
                                    <tr><td>Desc: </td><td><xsl:value-of select="@step-description"/></td></tr>
                                    <tr><td>Join: </td><td><xsl:value-of select="@joinCondition"/> </td></tr>
                                 </table>
                              </td><!-- end of step column -->
                              <td><!-- start of tool column -->
                                 <xsl:for-each select="tool">
                                    <table>                                 
                                       <tr><td>Tool: </td><td><xsl:value-of select="@tool-name"/></td></tr>
                                       <tr><td>Doc: </td><td><xsl:value-of select="@tool-documentation"/></td></tr>
                                    </table>
                                    <td><!-- start of input column -->
                                       <xsl:for-each select="inputParam">
                                          <xsl:if test="@param-value != ''">
                                             <table border="0" cellpadding="0" cellspacing="0">
                                                <tr><td>Name: </td><td><xsl:value-of select="@param-name"/></td></tr>
                                                <tr><td>Type: </td><td><xsl:value-of select="@param-type"/></td></tr>
                                                <tr><td>Value: </td><td><xsl:value-of select="@param-value"/></td></tr>
                                             </table>
                                             <p />
                                          </xsl:if>
                                       </xsl:for-each>
                                    </td><!-- end of input column -->
                                    <td><!-- start of output column -->
                                       <xsl:for-each select="outputParam">
                                          <table border="0" cellpadding="0" cellspacing="0">
                                             <tr><td>Name: </td><td><xsl:value-of select="@param-name"/></td></tr>
                                             <tr><td>Type: </td><td><xsl:value-of select="@param-type"/></td></tr>
                                             <tr><td>Location: </td><td><xsl:value-of select="@param-location"/></td></tr>
                                          </table>
                                       </xsl:for-each>                                                                                     
                                     </td><!-- end of output column -->                                                               
                                 </xsl:for-each><!-- end of each tool -->
                              </td>
                           </tr>                           
                        </td><!-- end of tool column -->     
                     </xsl:for-each><!-- end of each step -->
                  </table>
               </td>
            </tr>
         </table> 
      </xsl:template>
    
      <!--+
          | View workflow
          +-->   
      <xsl:template name="view_workflow">
         <form name="view_form" method="get">               
            <table cellpadding="0" cellspacing="0">                      
               <p />                                     
               <tr>
                  <td>Job Name:</td>
                  <td><input type="text" name="workflow-name" /></td>
                  <td><input type="submit" value="View" /></td>
                  <input type="hidden" name="action" value="read-workflow" />
               </tr>                                                              
            </table> 
         </form>
      </xsl:template>
    
      <!--+
          | Delete workflow
          +-->   
      <xsl:template name="delete_workflow">
         <form name="delete_form" method="get">               
            <table cellpadding="0" cellspacing="0">                      
               <p />                                     
               <tr>
                  <td>Job Name:</td>
                  <td><input type="text" name="workflow-name" /></td>
                  <td><input type="submit" value="Delete" /></td>
                  <input type="hidden" name="action" value="delete-workflow" />
               </tr>                                                              
            </table> 
         </form>
      </xsl:template>
   
      <!--+
          | Submit workflow
          +-->   
      <xsl:template name="submit_workflow" method="get">
         <form name="submit_form" action="">               
            <table cellpadding="0" cellspacing="0">                      
               <p />                                     
               <tr>
                  <td>Job Name:</td>
                  <td><input type="text" name="workflow-name" /></td>
                  <td><input type="submit" value="Submit" /></td>
                  <input type="hidden" name="action" value="submit-workflow"/>
               </tr>                                                              
            </table> 
         </form>
      </xsl:template>
   
      <!--+
          | Templates
          +-->   
      <xsl:template name="templates">
         <table cellpadding="0" cellspacing="0">
            <tr>
               <td><img src="/astrogrid-portal/mount/workflow/OneStepJob.gif" width="110" height="200" alt="OneStepJob" /></td>
               <td><img src="/astrogrid-portal/mount/workflow/TwoStepSequence.gif" width="110" height="260" alt="TwoStepSequence" /></td>
               <td><img src="/astrogrid-portal/mount/workflow/TwoStepFlow.gif" width="200" height="260" alt="TwoStepFlow" /></td>
               <td><img src="/astrogrid-portal/mount/workflow/SequenceWithTwoStepFlow.gif" width="200" height="260" alt="SequenceWithTwoStepFlow" /></td>                                   
               <td><img src="/astrogrid-portal/mount/workflow/ComplexWorkflow.gif" width="200" height="315" alt="ComplexWorkflow" /></td>
            </tr>
         </table>
      </xsl:template>              

      <!--+
          | Insert parameters into a tool
          +-->   
      <xsl:template name="insert_parameter">
            <table cellpadding="0" cellspacing="0" border="0">
               <tr>
                  <td>Workflow name: </td>
                  <td><xsl:value-of select="@workflow-name" /></td>                                    
               </tr>
               <tr>
                  <td>Workflow description: </td>
                  <td colspan="6"><xsl:value-of select="@workflow-description" /></td>                  
               </tr>
            </table>
            <p />
            <table cellpadding="0" cellspacing="0" border="0">   
               <xsl:for-each select="//step">           
                  <xsl:if test="$activity-key = @key">
                  <xsl:for-each select="tool">
                     <table cellpadding="0" cellspacing="0" border="0">   
                        <tr>
                           <td>Tool name: </td>
                           <td><xsl:value-of select="@tool-name" /></td>
                        </tr>
                        <tr>
                           <td>Tool documentation: </td>
                           <td colspan="6"><xsl:value-of select="@tool-documentation" /></td>
                        </tr>
                     </table>
                     <p />                     
                     <tr>
                        <td>Input parameters:</td>
                     </tr>
                     <tr>
                        <td>
                           <table cellpadding="0" cellspacing="0" border="1">
                              <xsl:for-each select="inputParam">
                                 <xsl:call-template name="display_input_parameter" />
                              </xsl:for-each><!-- end of inputParam -->
                           </table>                                  
                        </td>                  
                     </tr>
                     <p />
                     <tr>
                        <td>Output parameters:</td>
                     </tr>
                     <tr>
                        <td>
                           <table cellpadding="0" cellspacing="0" border="1">
                              <xsl:for-each select="outputParam">  
                                 <xsl:call-template name="display_output_parameter" />
                              </xsl:for-each><!-- end of outputParam -->  
                           </table>
                        </td>                  
                     </tr>
                  </xsl:for-each><!-- end of tool -->               
               </xsl:if>
               </xsl:for-each><!-- end of step -->                                        
		  </table>
          <form method="get" name="ParameterInsertForm">		  
             <input type="hidden" name="action" value="parameters-complete" />
             <input type="submit" value="Done" />   
         </form>
      </xsl:template>
    
      <!--+
          | Display Input Parameter
          +-->   
      <xsl:template name="display_input_parameter" method="get">
            <xsl:if test="@param-value = ''">
               <form name="input_parameter_form" >               
                  <tr> 
                     <td>Name: <xsl:value-of select="@param-name" /></td>
                     <td>Type: <xsl:value-of select="@param-type" /></td>
                     <xsl:if test="@param-type != 'boolean'">
                        <td>Value: <input type="text" size="25" name="param-value" /></td>
                        <td><input type="submit" value="Submit" /></td>
                     </xsl:if>
                     <xsl:if test="@param-type = 'boolean'">
                        <td>
                           Value:  
                            true <input type="radio" name="param-value" value="true" />
                            false <input type="radio" name="param-value" value="false" />
                         </td>
                         <td>
                           <input type="submit" value="Submit" />
                        </td>
                     </xsl:if>
                     <td>Cardinality (max): <xsl:value-of select="@param-cardinality-max" /></td>
                     <td>(min): <xsl:value-of select="@param-cardinality-min" /></td>
                  </tr>                                        
                  <input type="hidden" name="action" value="insert-input-value" />
                  <input type="hidden" name="param-name"><xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute></input>
                  <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="$activity-key"/></xsl:attribute></input>              
               </form>                  
            </xsl:if>
            <xsl:if test="@param-value != ''">
               <form name="reset_parameter_form">
                  <tr> 
                     <td>Name: <xsl:value-of select="@param-name" /></td>
                     <td>Type: <xsl:value-of select="@param-type" /></td>
                     <td>Value:
                        <xsl:element name="input">
                           <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
                           <xsl:attribute name="type">text</xsl:attribute>
                           <xsl:attribute name="size">25</xsl:attribute>
                           <xsl:attribute name="readonly">readonly</xsl:attribute>
                        </xsl:element>
                     </td>
                     <td>
                        <input type="submit" value="Delete" />
                     </td>
                     <td>Cardinality (max): <xsl:value-of select="@param-cardinality-max" /></td>
                     <td>(min): <xsl:value-of select="@param-cardinality-min" /></td>
                  </tr>                  
                  <input type="hidden" name="action" value="reset-parameter" />
                  <input type="hidden" name="direction" value="input" />
                  <input type="hidden" name="param-name"><xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute></input>
                  <input type="hidden" name="param-value"><xsl:attribute name="value"><xsl:value-of select="@param-value"/></xsl:attribute></input>
                  <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="$activity-key"/></xsl:attribute></input>              
               </form>                      
            </xsl:if>
      </xsl:template>


      <!--+
          | Display Output Parameter
          +-->   
      <xsl:template name="display_output_parameter" method="get">
         <xsl:if test="@param-location = ''">
            <form name="output_parameter_form" >                
               <tr> 
                  <td>Name: <xsl:value-of select="@param-name" /></td>
                  <td>Type: <xsl:value-of select="@param-type" /></td>
                  <td>Value: <input type="text" size="25" name="param-value" /></td>
                  <td><input type="submit" value="Submit" /></td>
                  <td>Cardinality (max): <xsl:value-of select="@param-cardinality-max" /></td>
                  <td>(min): <xsl:value-of select="@param-cardinality-min" /></td>
               </tr>                                        
               <input type="hidden" name="action" value="insert-output-value" />
               <input type="hidden" name="param-name"><xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute></input>            
               <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="$activity-key"/></xsl:attribute></input>              
            </form>
         </xsl:if>
         <xsl:if test="@param-location != ''">
            <form name="reset_parameter_form" > 
               <tr> 
                  <td>Name: <xsl:value-of select="@param-name" /></td>
                  <td>Type: <xsl:value-of select="@param-type" /></td>
                  <td>Value:
                     <xsl:element name="input">
                        <xsl:attribute name="value"><xsl:value-of select="@param-location" /></xsl:attribute>
                        <xsl:attribute name="type">text</xsl:attribute>
                        <xsl:attribute name="size">25</xsl:attribute>
                        <xsl:attribute name="readonly">readonly</xsl:attribute>
                     </xsl:element>
                  </td>
                  <td><input type="submit" value="Delete" /></td>
                  <td>Cardinality (max): <xsl:value-of select="@param-cardinality-max" /></td>
                  <td>(min): <xsl:value-of select="@param-cardinality-min" /></td>
               </tr>                         
                  <input type="hidden" name="action" value="reset-parameter" />
                  <input type="hidden" name="direction" value="output" />
                  <input type="hidden" name="param-name"><xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute></input>
                  <input type="hidden" name="param-value"><xsl:attribute name="value"><xsl:value-of select="@param-location"/></xsl:attribute></input>
                  <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="$activity-key"/></xsl:attribute></input>              
            </form>               
         </xsl:if>
      </xsl:template>
    
    
     <!-- Default, copy all and apply templates -->
     <xsl:template match="@*|node()">
       <xsl:copy>
         <xsl:apply-templates select="@*|node()" />
       </xsl:copy>
     </xsl:template>
   </xsl:stylesheet>
