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
               <xsl:apply-templates/>
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
         </xsl:if>
         <xsl:if test="$action = 'view-workflow'">
            <xsl:call-template name="view_workflow"/>             
         </xsl:if>
         <xsl:if test="$action = 'create-workflow'">
            <xsl:call-template name="create_workflow"/>             
         </xsl:if>
         <xsl:if test="$action = 'create-tool-for-step'">
            <xsl:call-template name="insert_tool"/>             
         </xsl:if>
         <xsl:if test="$action = 'insert-tool-into-step'">
            <xsl:call-template name="create_workflow"/>             
         </xsl:if>                                 
         <xsl:if test="$action = 'templates'">
            <xsl:call-template name="templates"/>             
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
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="?action=new">New</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="?action=submit">Submit</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="?action=view">View</a></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="?action=delete">Delete</a></td>                  
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="?action=templates">Templates</a></td>
               <!--
                 Spacing.
                 -->
               <td width="100%"></td>
               <td class="ag-content-tab-bottom"><a class="ag-content-tab-link" href="?action=monitor">Monitor</a></td>                  
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
            <xsl:for-each select="//tool">
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
         <form name="create_form" action="">               
            <table cellpadding="0" cellspacing="0">                                                           
               <tr>
                  <td>Job Name:</td>
                  <td><xsl:value-of select="@workflow-name" /></td>                                    
               </tr>
               <tr>
                  <td>Job Description:</td>
                  <td><xsl:value-of select="@workflow-description" /></td>                  
               </tr>
               <xsl:if test="@template = 'OneStepJob'">
                  <xsl:call-template name="OneStepJob" />
               </xsl:if>             
               <xsl:if test="@template = 'TwoSequentialJobsteps'">
                  <xsl:call-template name="TwoStepSequence" />
               </xsl:if>
               <xsl:if test="@template = 'TwoParallelJobsteps'">
                  <xsl:call-template name="TwoStepFlow" />
               </xsl:if>
               <xsl:if test="@template = 'TwoStepFlowAndMerge'">
                  <xsl:call-template name="SequenceWithTwoStepFlow" />
               </xsl:if>                                                                                            
            </table> 
         </form>     
      </xsl:template>
    
      <!--+
          | View workflow
          +-->   
      <xsl:template name="view_workflow">
         <form name="view_form" action="">               
            <table cellpadding="0" cellspacing="0">                      
               <p />                                     
               <tr>
                  <td>Job Name:</td>
                  <td><input type="text" name="workflow-name" disabled="true" /></td>
                  <td><input type="submit" value="View" /></td>
               </tr>                                                              
            </table> 
         </form>
      </xsl:template>
    
      <!--+
          | Delete workflow
          +-->   
      <xsl:template name="delete_workflow">
         <form name="delete_form" action="">               
            <table cellpadding="0" cellspacing="0">                      
               <p />                                     
               <tr>
                  <td>Job Name:</td>
                  <td><input type="text" name="workflow-name" disabled="true" /></td>
                  <td><input type="submit" value="Delete" /></td>
               </tr>                                                              
            </table> 
         </form>
      </xsl:template>
   
      <!--+
          | Submit workflow
          +-->   
      <xsl:template name="submit_workflow">
         <form name="submit_form" action="">               
            <table cellpadding="0" cellspacing="0">                      
               <p />                                     
               <tr>
                  <td>Job Name:</td>
                  <td><input type="text" name="workflow-name" disabled="true" /></td>
                  <td><input type="submit" value="Submit" /></td>
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
          | OneStepJob
          +-->   
      <xsl:template name="OneStepJob">
         <tr>
            <td>
               <img src="OneStepJob.gif" alt="OneStepJob" />
            </td>
            <xsl:for-each select="//step">
               <td>
                  Step 1: <xsl:call-template name="ToolSelectTemplate" />
               </td>
            </xsl:for-each>
         </tr>
      </xsl:template>
      
      <!--+
          | TwoStepSequence
          +-->   
      <xsl:template name="TwoStepSequence">
         <tr>
            <td>
               <img src="TwoStepSequence.gif" alt="TwoStepSequence" />
            </td>
            <td>
               <xsl:for-each select="//step">
                  <tr>
                     <td>
                        Step: <xsl:call-template name="ToolSelectTemplate" />
                     </td>
                  </tr>
               </xsl:for-each>
            </td>            
         </tr>
      </xsl:template>
      
      <!--+
          | TwoStepFlow
          +-->   
      <xsl:template name="TwoStepFlow">
         <tr>
            <td>
               <img src="TwoStepFlow.gif" alt="TwoStepFlow" />
            </td>
            <td>
               <xsl:for-each select="//step">
                  <tr>
                     <td>
                        Step: <xsl:call-template name="ToolSelectTemplate" />
                     </td>
                  </tr>
               </xsl:for-each>
            </td>            
         </tr>
      </xsl:template>
      
      <!--+
          | SequenceWithTwoStepFlow
          +-->   
      <xsl:template name="SequenceWithTwoStepFlow">
         <tr>
            <td> 
               <img src="SequenceWithTwoStepFlow.gif" alt="SequenceWithTwoStepFlow" />
            </td>
            <td>
               <xsl:for-each select="//step">
                  <tr>
                     <td>
                        Step: <xsl:call-template name="ToolSelectTemplate" />
                     </td>
                  </tr>
               </xsl:for-each>
            </td>            
         </tr>
      </xsl:template>
      
      <!--+
          | Monitor jobs
          +-->   
      <xsl:template name="monitor_jobs">
      </xsl:template>
      
    <!--+
	    | Tool selection template
	    +-->	
	<xsl:template name="ToolSelectTemplate">
       <form method="get" name="ToolSelectForm"> 
	   	  <select name="tool-name">
	         <option value="none">-- Select tool --</option>
             <xsl:for-each select="//tool">
                <xsl:element name="option">
                   <xsl:attribute name="value"><xsl:value-of select="@tool-name"/></xsl:attribute>
				   <xsl:value-of select="@tool-name"/>
				</xsl:element>
             </xsl:for-each>
		  </select>
		  <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input>
		  <input type="hidden" name="action" value="create-tool-for-step" />		                     	                 		                 		                 
		  <input type="submit" value="Select" />
	   </form>
	</xsl:template>                              

      <!--+
          | Insert tool into a step
          +-->   
      <xsl:template name="insert_tool">
         <form method="get" name="ToolInsertForm">               
            <table cellpadding="0" cellspacing="0">                                                           
               <tr>
                  <td>Job Name:</td>
                  <td><xsl:value-of select="@workflow-name" /></td>                                    
               </tr>
               <tr>
                  <td>Job Description:</td>
                  <td><xsl:value-of select="@workflow-description" /></td>                  
               </tr>
               <tr>
                  <td>
                     Tool name:
                     <td><xsl:value-of select="@toolObject-name" /></td>
                  </td>
               </tr>
               <tr>
                  <td>
                     Tool documentation:
                     <td><xsl:value-of select="@toolObject-documentation" /></td>
                  </td>
               </tr>                                     
               <tr>
                  <td>
                     Get all the parameters
                  </td>
               </tr>          
		    <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="$activity-key"/></xsl:attribute></input>
		    <input type="hidden" name="action" value="insert-tool-into-step" />		                     	                 		                 		                 
		    <input type="submit" value="Insert tool" />
		  </table>
	   </form>          
      </xsl:template>
    
     <!-- Default, copy all and apply templates -->
     <xsl:template match="@*|node()">
       <xsl:copy>
         <xsl:apply-templates select="@*|node()" />
       </xsl:copy>
     </xsl:template>
   </xsl:stylesheet>
