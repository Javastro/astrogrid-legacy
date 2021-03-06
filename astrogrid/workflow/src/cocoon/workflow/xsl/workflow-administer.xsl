<?xml version="1.0"?>
<xsl:stylesheet   version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
      
   <xsl:param name="action" />
   <xsl:param name="errormessage" />         

   <!--+
       | Match the root element.
      +-->
   <xsl:template match="/">
      <xsl:apply-templates/>
   </xsl:template>

   <!--+
       | Match the workflow element.
      +-->
   <xsl:template match="workflow">
      <page>
         <!-- Add our page content -->
         <content>
            <xsl:call-template name="main_menu"/>
            <xsl:if test="$action = ''">
               <xsl:call-template name="list_workflow" />
               <xsl:call-template name="list_query" />             
            </xsl:if>             
            <xsl:if test="$action = 'new'">
               <xsl:call-template name="create_worklow" />             
            </xsl:if>
            <xsl:if test="$action = 'submit'">
              <xsl:call-template name="list_workflow"/>
               <xsl:call-template name="submit_worklow" />             
            </xsl:if>              
            <xsl:if test="$action = 'edit'">
              <xsl:call-template name="list_workflow"/>
               <xsl:call-template name="edit_worklow" />             
            </xsl:if>   
            <xsl:if test="$action = 'copy'">
              <xsl:call-template name="list_workflow"/>
               <xsl:call-template name="copy_worklow" />             
            </xsl:if>
            <xsl:if test="$action = 'delete'">
              <xsl:call-template name="list_workflow"/>
               <xsl:call-template name="delete_worklow" />             
            </xsl:if>
            <xsl:if test="$action = 'send'">
              <xsl:call-template name="list_workflow"/>
               <xsl:call-template name="send_worklow" />             
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
            <xsl:if test="$action = 'read-workflow'">
              <xsl:call-template name="edit_workflow"/>             
            </xsl:if>                                                                                                             
         </content>
      </page>
   </xsl:template>
   
   <!--+
       | Main menu
       +-->   
   <xsl:template name="main_menu">
      <style type="text/css">
        .topmenu { color: rgb(51,51,255);
                   background-color: rgb(204,204,204);
                   font-family: arial,helvetica,sans-serif;
                   font-size-adjust: 2;
                   font-weight: bold;
                   height: 30px;
                   width: 200px;
                 }   
      </style>
    <!-- Design Jobs Menu -->  
      <table cellpadding="2" cellspacing="2" border="0">
         <tbody>
           <tr width="100%" align="center" valign="middle" class="topmenu">
             <td width="80%" height="0" colspan="6" rowspan="1" class="topmenu">
               Design Jobs
             </td>
             <td width="20%" height="0" colspan="1" rowspan="1" class="topmenu">
               Submitted Jobs
             </td>
           </tr>          
           <tr width="80%" align="center" valign="middle" class="topmenu">
             <td width="0" height="0" colspan="1" rowspan="1" class="topmenu">
               <a href="agjobmonitor.html?action=new">
                 New
               </a>
             </td>
             <td width="0" height="0" colspan="1" rowspan="1" class="topmenu">
               <a href="agjobmonitor.html?action=submit">
                 Submit
               </a>
             </td>
             <td width="0" height="0" colspan="1" rowspan="1" class="topmenu">
               <a href="agjobmonitor.html?action=edit">
                 Edit
               </a>
             </td>
             <td width="0" height="0" colspan="1" rowspan="1" class="topmenu">
               <a href="agjobmonitor.html?action=copy">
                 Copy
               </a>
             </td>
             <td width="0" height="0" colspan="1" rowspan="1" class="topmenu">
               <a href="agjobmonitor.html?action=delete">
                 Delete
               </a>
             </td>
             <td width="0" height="0" colspan="1" rowspan="1" class="topmenu">
               <a href="agjobmonitor.html?action=send">
                 Send
               </a>
             </td>
             <td width="0" height="0" colspan="1" rowspan="1" class="topmenu">
               <a href="jobentry/job-list.xsp?action=read-job-list">
                 List
               </a>
             </td>
           </tr>
           <xsl:if test="$errormessage != ''">   
              <tr>
                 <td>
                   <font color="red">
                      <xsl:value-of select="$errormessage" />
                   </font>
                 </td>
              </tr>
          </xsl:if>                
        </tbody>
      </table>
   </xsl:template>

   <!--+
       | List workflows currently stored in MySpace
       +-->   
   <xsl:template name="list_workflow">
      <table border="0">
         <tr>
            <td>
                <b>Workflows currently stored in your mySpace:</b>
            </td>
         </tr>
         <tr>
            <td>
            </td>
         </tr>
         <xsl:for-each select="//workflow">
         <tr>
            <td>
               <xsl:if test="@workflow-name != 'null'">
                  <xsl:value-of select="@workflow-name"/>
               </xsl:if>
            </td>
            <td>
               <xsl:value-of select="@workflow-description"/>
            </td>
         </tr>
         </xsl:for-each>
     </table>       
   </xsl:template>
   
   <!--+
       | List queries currently stored in MySpace
       +-->   
   <xsl:template name="list_query">
       <table border="0">
           <tr>
               <td>
               </td>
           </tr>
           <tr>
           <td>
                   <b>Queries currently stored in your mySpace:</b>
               </td>
           </tr>
           <tr>
              <td>
              </td>
           </tr>           
           <xsl:for-each select="//query">
           <tr>
               <td>
                  <xsl:if test="@query-name != 'null'">
                     <xsl:value-of select="@query-name"/>
                  </xsl:if>
               </td>
               <td>
                   <xsl:value-of select="@query-description"/>
               </td>
           </tr>
           </xsl:for-each>
       </table>
   </xsl:template>   

   <!--+
       | Create workflow
       +-->          
   <xsl:template name="create_worklow">
       <form name="createWorkflow" method="get"  action="agworkflow-administer.html">
          <table cellpadding="2" cellspacing="2" border="0">
            <tbody>
              <tr width="80%" align="center" valign="middle" class="topmenu">
                <td width="0" height="0" colspan="6" rowspan="1" class="topmenu">
                  Create new workflow
                </td>
                <p/>
              </tr>
              <tr>
                <td>
                  Workflow Name:
                </td>
              <td>
                <input type="text" size="40" name="workflow-name"/>
              </td>
             </tr>
              <tr>
                <td>
                  Workflow Description:
                </td>
              <td>
                <input type="text" size="40" name="workflow-description"/>
              </td>
             </tr>             
             <tr>
               <td>     
                 Template:
               </td>
               <td>
                 <table>
                   <tr> <td>
                      <input type="radio" name="template"
                             value="OneStepJob" checked="true">
                             one step sequence
                      </input>
                   </td> </tr>
                   <tr> <td>
                     <input type="radio" name="template"
                            value="TwoParallelJobsteps">
                             two step flow
                     </input>
                   </td> </tr>
                   <tr> <td>
                     <input type="radio" name="template"
                            value="TwoSequentialJobsteps">
                             two step sequence
                     </input>
                   </td> </tr>
                 </table>
               </td>
             </tr>
             <tr>
               <td>
                 <input type="submit" name="action" value="create-workflow"/>
               </td>
             </tr>
          </tbody>
        </table>
      </form>        
   </xsl:template>   
   
   <!--+
       | Edit workflow
       +-->   
   <xsl:template name="edit_worklow">
       <form name="editWorkflow" method="get"
             action="agworkflow-administer.html">
          <table cellpadding="2" cellspacing="2" border="0">
            <tbody>
              <tr width="80%" align="center" valign="middle"
                  class="topmenu">
                <td width="0" height="0" colspan="6" rowspan="1" class="topmenu">
                  Edit workflow
                </td>
              </tr>
              <tr>
                <td>
                  Job Name:
                </td>
              <td>
                <input type="text" size="40" name="workflow-name"/>
              </td>
             </tr>
             <tr>
               <td>
                 <input type="submit" name="action" value="read-workflow"/>
               </td>
             </tr>
          </tbody>
        </table>
      </form>       
   </xsl:template>   
   
   <!--+
       | Delete workflow
       +-->   
   <xsl:template name="delete_worklow">
       <form name="deleteWorkflow" method="get" action="agjobmonitor.html">
          <table cellpadding="2" cellspacing="2" border="0">
            <tbody>
              <tr width="80%" align="center" valign="middle" class="topmenu">
                <td width="0" height="0" colspan="6" rowspan="1" class="topmenu">
                  Delete workflow
                </td>
              </tr>
              <tr>
                <td>
                  Job Name:
                </td>
              <td>
                <input type="text" size="40" name="workflow-name"></input>
              </td>
             </tr>
             <tr>
               <td>
                 <input type="submit" name="action" value="delete-workflow"/>
               </td>
             </tr>
          </tbody>
        </table>
      </form>       
   </xsl:template>      

   <!--+
       | Submit workflow
       +-->   
   <xsl:template name="submit_worklow">
       <form name="submitWorkflow" method="get" action="agjobmonitor.html">
          <table cellpadding="2" cellspacing="2" border="0">
            <tbody>
              <tr width="80%" align="center" valign="middle" class="topmenu">
                <td width="0" height="0" colspan="6" rowspan="1" class="topmenu">
                  Submit workflow
                </td>
              </tr>
              <tr>
                <td>
                  Job Name:
                </td>
              <td>
                <input type="text" size="40" name="workflow-name"/>
              </td>
             </tr>
             <tr>
               <td>
                 <input type="submit" name="action" value="submit-workflow"/>
               </td>
             </tr>
          </tbody>
        </table>
      </form>       
   </xsl:template>

   <!--+
       | Copy workflow
       +-->   
   <xsl:template name="copy_worklow">
       <p>
       <strong>Further functionality to follow</strong>
       </p>       
   </xsl:template>
   
   <!--+
       | Send workflow
       +-->   
   <xsl:template name="send_worklow">
       <p>
       <strong>Further functionality to follow</strong>
       </p>       
   </xsl:template>   

</xsl:stylesheet>
      
   
      
   
