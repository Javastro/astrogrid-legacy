<?xml version="1.0"?>
<xsl:stylesheet	version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:param name="user-param" />
	<xsl:param name="name" />
	<xsl:param name="community" />	
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
			</content>
		</page>
	</xsl:template>
	
	<!--+
	    | Main menu
	    +-->	
	<xsl:template name="main_menu">
    <!-- Design Jobs Menu -->  
      <table cellpadding="2" cellspacing="2" border="0">
        <tbody>
          <tr width="80%" align="center" valign="middle" style="color: rgb(51,51,255); background-color: rgb(204,204,204);">
            <td width="0" height="0" colspan="6" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              Design Jobs
            </td>
          </tr>          
          <tr width="80%" align="center" valign="middle" style="color: rgb(51,51,255); background-color: rgb(204,204,204);">
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agworkflow-admin.html?action=new">
                New
              </a>
            </td>
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agworkflow-admin.html?action=submit">
                Submit
              </a>
            </td>
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agworkflow-admin.html?action=edit">
                Edit
              </a>
            </td>
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agworkflow-admin.html?action=copy">
                Copy
              </a>
            </td>
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agworkflow-admin.html?action=delete">
                Delete
              </a>
            </td>
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agworkflow-admin.html?action=send">
                Send
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
                   Workflows currently strored in your mySpace:
               </td>
           </tr>
           <xsl:for-each select="//workflow">
           <tr>
               <td>
                   <xsl:value-of select="@workflow-name"/>
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
                   Queries currently stored in your mySpace:
               </td>
           </tr>
           <xsl:for-each select="//query">
           <tr>
               <td>
                   <xsl:value-of select="@query-name"/>
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
              <tr width="80%" align="center" valign="middle" style="color: rgb(51,51,255); background-color: rgb(204,204,204);">
                <td width="0" height="0" colspan="6" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
                  Create new workflow
                </td>
                <p/>
              </tr>
<!--              <tr>
                <td>
                  UserId:
                </td>
                <td>
                  <xsl:value-of select="$user-param"/>
                </td>
              </tr>
              <tr>
                <td>
                  Community:
                </td>
                <td>
                  <xsl:value-of select="$community"/>
                </td>
              </tr>
-->              <tr>
                <td>
                  Workflow Name:
                </td>
              <td>
                <input type="text" size="40" name="workflow-name"></input>
              </td>
             </tr>
              <tr>
                <td>
                  Workflow Description:
                </td>
              <td>
                <input type="text" size="40" name="workflow-description"></input>
              </td>
             </tr>             
             <tr>
               <td>     
                 Template:
               </td>
               <td>
<!--                 <br><input type="radio" name="template" value="none_selected" checked="true">None</input></br>  -->
                 <table>
                   <tr><td><input type="radio" name="template" value="OneStepJob">one step sequence</input></td></tr>
                   <tr><td><input type="radio" name="template" value="TwoParallelJobsteps">two step flow</input></td></tr>
                   <tr><td><input type="radio" name="template" value="TwoSequentialJobsteps">two step sequence</input></td></tr>
                 </table>
               </td>
             </tr>
             <tr>
               <td>
                 <input type="submit" name="action" value="create-workflow"></input>
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
	    <form name="editWorkflow" method="get" action="agworkflow-administer.html">
          <table cellpadding="2" cellspacing="2" border="0">
            <tbody>
              <tr width="80%" align="center" valign="middle" style="color: rgb(51,51,255); background-color: rgb(204,204,204);">
                <td width="0" height="0" colspan="6" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
                  Edit workflow
                </td>
              </tr>
<!--              <tr>
                <td>
                  UserId:
                </td>
                <td>
                  <xsl:value-of select="$user-param"/>
                </td>
              </tr>
              <tr>
                <td>
                  Community:
                </td>
                <td>
                  <xsl:value-of select="$community"/>
                </td>
              </tr>
-->              <tr>
                <td>
                  Job Name:
                </td>
              <td>
                <input type="text" size="40" name="workflow-name"></input>
              </td>
             </tr>
             <tr>
               <td>
                 <input type="submit" name="action" value="read-workflow"></input>
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
	    <form name="deleteWorkflow" method="get" action="agworkflow-administer.html">
          <table cellpadding="2" cellspacing="2" border="0">
            <tbody>
              <tr width="80%" align="center" valign="middle" style="color: rgb(51,51,255); background-color: rgb(204,204,204);">
                <td width="0" height="0" colspan="6" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
                  Delete workflow
                </td>
              </tr>
<!--              <tr>
                <td>
                  UserId:
                </td>
                <td>
                  <xsl:value-of select="$user-param"/>
                </td>
              </tr>
              <tr>
                <td>
                  Community:
                </td>
                <td>
                  <xsl:value-of select="$community"/>
                </td>
              </tr>
-->              <tr>
                <td>
                  Job Name:
                </td>
              <td>
                <input type="text" size="40" name="workflow-name"></input>
              </td>
             </tr>
             <tr>
               <td>
                 <input type="submit" name="action" value="delete-workflow"></input>
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
	    <p>
	    <strong>To do</strong>
	    </p>	    
	</xsl:template>

	<!--+
	    | Copy workflow
	    +-->	
	<xsl:template name="copy_worklow">
	    <p>
	    <strong>To do</strong>
	    </p>	    
	</xsl:template>
	
	<!--+
	    | Send workflow
	    +-->	
	<xsl:template name="send_worklow">
	    <p>
	    <strong>To do</strong>
	    </p>	    
	</xsl:template>	

</xsl:stylesheet>
		
	
		
	