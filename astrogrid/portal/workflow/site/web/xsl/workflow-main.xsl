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
         <xsl:if test="$action = 'parameters-complete'">
            <xsl:call-template name="create_workflow"/>             
         </xsl:if>         
         <xsl:if test="$action = 'templates'">
            <xsl:call-template name="templates"/>             
         </xsl:if>
         <xsl:if test="$action = 'read-job-list'">
            <xsl:call-template name="monitor_jobs"/>             
         </xsl:if>                                                                                                                                     
      </xsl:template>

      <!--+
          | Main menu
          +-->              
      <xsl:template name="main_menu">                                
         <table id="ag-main">                                
            <tr>
               <td class="ag-content-tab-top" colspan="5" align="center">Design-Jobs</td>
               <!--
                 Spacing.
                 -->
               <td width="100%"></td>
               <td class="ag-content-tab-top">Monitor-Jobs</td>                  
            </tr>
            <tr>                
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmonitor.html?action=new">New</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmonitor.html?action=submit">Submit</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmonitor.html?action=view">View</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmonitor.html?action=delete">Delete</a></td>                  
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmonitor.html?action=templates">Templates</a></td>
               <!--
                 Spacing.
                 -->
               <td width="100%"></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="/astrogrid-portal/main/mount/workflow/agjobmanager-jes.html?action=read-job-list">Monitor</a></td>                  
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
      </xsl:template>

      <!--+
          | Display tools currently available
          +-->   
      <xsl:template name="list_tools">          
         <table>
            <tr>
               <td>Tools currently available for use:</td>
            </tr>
            <p />
            <xsl:for-each select="//toolsAvailable">
               <tr>
                  <xsl:if test="@tool-name != 'null'">
                     <td><xsl:value-of select="@tool-name"/></td>
                     <td><xsl:value-of select="@tool-description"/></td>
                  </xsl:if>
               </tr>
            </xsl:for-each>
         </table> 
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
         <table border="1" cellpadding="0" cellspacing="0">                                                           
            <tr>
               <td>Workflow Name:</td>
               <td><xsl:value-of select="@workflow-name" /></td>                                    
            </tr>
            <tr>
               <td>Workflow Description:</td>
               <td><xsl:value-of select="@workflow-description" /></td>                  
            </tr>
            <tr>
               <td>
                  <xsl:if test="@template = 'OneStepJob'"><img src="OneStepJob.gif" alt="OneStepJob" /></xsl:if>
                  <xsl:if test="@template = 'TwoSequentialJobsteps'"><img src="TwoStepSequence.gif" alt="TwoStepSequence" /></xsl:if>
                  <xsl:if test="@template = 'TwoParallelJobsteps'"><img src="TwoStepFlow.gif" alt="TwoStepFlow" /></xsl:if>
                  <xsl:if test="@template = 'TwoStepFlowAndMerge'"><img src="SequenceWithTwoStepFlow.gif" alt="SequenceWithTwoStepFlow" /></xsl:if>                
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
         <table border="1" cellpadding="0" cellspacing="0">                                                           
            <tr>
               <td>Workflow Name:</td>
               <td><xsl:value-of select="@workflow-name" /></td>
            </tr>
            <tr>
               <td>Workflow Description:</td>
               <td><xsl:value-of select="@workflow-description" /></td>                  
            </tr>
            <tr>
               <td>
                  <xsl:if test="@template = 'OneStepJob'"><img src="OneStepJob.gif" alt="OneStepJob" /></xsl:if>
                  <xsl:if test="@template = 'TwoSequentialJobsteps'"><img src="TwoStepSequence.gif" alt="TwoStepSequence" /></xsl:if>
                  <xsl:if test="@template = 'TwoParallelJobsteps'"><img src="TwoStepFlow.gif" alt="TwoStepFlow" /></xsl:if>
                  <xsl:if test="@template = 'TwoStepFlowAndMerge'"><img src="SequenceWithTwoStepFlow.gif" alt="SequenceWithTwoStepFlow" /></xsl:if>                
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
                           <table>
                              <tr><td><xsl:value-of select="@step-name"/></td></tr>
                              <tr><td>Desc: <xsl:value-of select="@step-description"/></td></tr>
                              <tr><td>Join: <xsl:value-of select="@joinCondition"/> </td></tr>
                           </table>
                        </td><!-- end of step column -->
                        <td><!-- start of tool column -->
                           <xsl:for-each select="tool">
                              <table>                                 
                                 <tr><td>Tool: <xsl:value-of select="@tool-name"/></td></tr>
                                 <tr><td>Documentation: <xsl:value-of select="@tool-documentation"/></td></tr>
                              </table>
                              <td><!-- start of input column -->
                                 <xsl:for-each select="inputParam">
                                    <table>
                                       <td>Name: <xsl:value-of select="@param-name"/></td>
                                       <td>Type: <xsl:value-of select="@param-type"/></td>
                                       <td>Location: <xsl:value-of select="@param-location"/></td>
                                    </table>
                                 </xsl:for-each>
                              </td><!-- end of input column -->
                              <td><!-- start of output column -->
                                 <xsl:for-each select="outputParam">
                                    <table>
                                       <td>Name: <xsl:value-of select="@param-name"/></td>
                                       <td>Type: <xsl:value-of select="@param-type"/></td>
                                       <td>Location: <xsl:value-of select="@param-location"/></td>
                                    </table>
                                 </xsl:for-each>                                                                                     
                               </td><!-- end of output column -->                                                               
                           </xsl:for-each><!-- end of each tool -->
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
               <td><img src="OneStepJob.gif" alt="OneStepJob" /></td>
               <td><img src="TwoStepSequence.gif" alt="TwoStepSequence" /></td>
               <td><img src="TwoStepFlow.gif" alt="TwoStepFlow" /></td>
               <td><img src="SequenceWithTwoStepFlow.gif" alt="SequenceWithTwoStepFlow" /></td>                                                      
            </tr>
         </table>
      </xsl:template>              
      
      <!--+
          | Monitor jobs
          +-->   
      <xsl:template name="monitor_jobs">
         <table border="1" cellpadding="0" cellspacing="0">
            <tr>
               <td>Name</td>
               <td>Description</td>
               <td>Time</td>
               <td>Status</td>
               <td>Job ID</td> 
            </tr>
            <xsl:for-each select="//job">
               <tr>    
                  <td><xsl:value-of select="@name"/></td>
                  <td><xsl:value-of select="@description"/></td>
                  <td><xsl:value-of select="@time"/></td>
                  <td><xsl:value-of select="@status"/></td>
                  <td><xsl:value-of select="@jobid"/></td>               
               </tr>
            </xsl:for-each>
         </table>
      </xsl:template>
    

      <!--+
          | Insert parameters into a tool
          +-->   
      <xsl:template name="insert_parameter">
<!--         <form method="get" name="ParameterInsertForm"> -->
            <table cellpadding="0" cellspacing="0" border="1">                                                           
               <tr>
                  <td>Workflow name:</td>
                  <td><xsl:value-of select="@workflow-name" /></td>                                    
               </tr>
               <tr>
                  <td>Workflow description:</td>
                  <td colspan="4"><xsl:value-of select="@workflow-description" /></td>                  
               </tr>
               <xsl:for-each select="//step">           
                   <xsl:if test="$activity-key = @key">
                  <xsl:for-each select="tool">                    
                     <tr>
                        <td>Tool name:</td>
                        <td><xsl:value-of select="@tool-name" /></td>
                     </tr>
                     <tr>
                        <td>Tool documentation:</td>
                        <td colspan="4"><xsl:value-of select="@tool-documentation" /></td>
                     </tr>
                     <tr>
                        <td colspan="5">
                           <table>
                              <tr>
                                 <td align="center" colspan="3">Input parameters:</td>
                              </tr>
                              <xsl:for-each select="inputParam">
                                 <xsl:call-template name="insert_input_parameter" />
                              </xsl:for-each><!-- end of inputParam -->  
                           </table>
                        </td>                  
                     </tr>               
                     <tr>
                        <td>
                           <table>
                              <tr>
                                 <td align="center" colspan="3"> Output parameters:</td>
                              </tr>
                              <xsl:for-each select="outputParam">  
                                 <xsl:call-template name="insert_output_parameter" />
                             </xsl:for-each><!-- end of outputParam -->  
                          </table>
                        </td>                  
                     </tr>
                  </xsl:for-each><!-- end of tool -->               
               </xsl:if>
               </xsl:for-each><!-- end of step -->                                        
<!--		    <input type="submit" value="Insert parameter" /> -->
		  </table>
<!-- This is temp - until parameter insert is working -->		  
<form method="get" name="ParameterInsertForm">		  
<input type="hidden" name="action" value="parameters-complete" />
<input type="submit" value="Done" />   
</form>
      </xsl:template>
    
      <!--+
          | Insert Input Parameter
          +-->   
      <xsl:template name="insert_input_parameter" method="get">
         <xsl:if test="@param-value = 'null'">
         <form name="input_query_tool_from" >               
            <table cellpadding="0" cellspacing="0" border="1">                      
               <tr> 
                  <td>Name: <xsl:value-of select="@param-name" /></td>
                  <td>Type: <xsl:value-of select="@param-type" /></td>
                  <td>Value: <input type="text" name="param-value" /><input type="submit" value="Submit" /></td>
                  <td>Cardinality (max): <xsl:value-of select="@param-cardinality-max" /></td>
                  <td>Cardinality (min): <xsl:value-of select="@param-cardinality-min" /></td>
               </tr>                                        
            </table> 
            <input type="hidden" name="action" value="insert-input-value" />
            <input type="hidden" name="param-name"><xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute></input>
            <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="$activity-key"/></xsl:attribute></input>              
         </form>
         </xsl:if>
         <xsl:if test="@param-value != 'null'">
            <table cellpadding="0" cellspacing="0">                      
               <tr> 
                  <td>Name: <xsl:value-of select="@param-name" /></td>
                  <td>Type: <xsl:value-of select="@param-type" /></td>
                  <td>Value: <xsl:value-of select="@param-value" /></td>
                  <td>Cardinality (max): <xsl:value-of select="@param-cardinality-max" /></td>
                  <td>Cardinality (min): <xsl:value-of select="@param-cardinality-min" /></td>
               </tr>                                        
            </table>             
         </xsl:if>
      </xsl:template>


      <!--+
          | Insert Output Parameter
          +-->   
      <xsl:template name="insert_output_parameter" method="get">
         <xsl:if test="@param-location = 'null'">
         <form name="input_query_tool_from" >               
            <table cellpadding="0" cellspacing="0">                      
               <tr> 
                  <td>Name: <xsl:value-of select="@param-name" /></td>
                  <td>Type: <xsl:value-of select="@param-type" /></td>
                  <td>Value: <input type="text" name="param-value" /><input type="submit" value="Submit" /></td>
                  <td>Cardinality (max): <xsl:value-of select="@param-cardinality-max" /></td>
                  <td>Cardinality (min): <xsl:value-of select="@param-cardinality-min" /></td>
               </tr>                                        
            </table> 
            <input type="hidden" name="action" value="insert-output-value" />
            <input type="hidden" name="param-name"><xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute></input>            
            <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="$activity-key"/></xsl:attribute></input>              
         </form>
         </xsl:if>
         <xsl:if test="@param-location != 'null'">
            <table cellpadding="0" cellspacing="0">                      
               <tr> 
                  <td>Name: <xsl:value-of select="@param-name" /></td>
                  <td>Type: <xsl:value-of select="@param-type" /></td>
                  <td>Value: <xsl:value-of select="@param-nalue" /></td>
                  <td>Cardinality (max): <xsl:value-of select="@param-cardinality-max" /></td>
                  <td>Cardinality (min): <xsl:value-of select="@param-cardinality-min" /></td>
               </tr>                                        
            </table>             
         </xsl:if>
      </xsl:template>
    
    
    
    
     <!-- Default, copy all and apply templates -->
     <xsl:template match="@*|node()">
       <xsl:copy>
         <xsl:apply-templates select="@*|node()" />
       </xsl:copy>
     </xsl:template>
   </xsl:stylesheet>
