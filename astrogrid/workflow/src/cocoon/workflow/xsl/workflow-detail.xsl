<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="action" />
    <xsl:param name="errormessage" />	
	
	<!--+
	    | Match the explorer element.
		+-->
	<xsl:template match="workflow">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="workflow-detail"/> 
				<xsl:call-template name="workflow-description"/>				 
				<xsl:call-template name="actions_menu"/>
			</content>
		</page>
	</xsl:template>

	<xsl:template name="workflow-detail">
	    <table border="0">
	        <xsl:if test="$errormessage != ''">	
	            <tr>
	                <td>
			            <font color="red">
				            <xsl:value-of select="$errormessage" />
			            </font>
			        </td>
			    </tr>
		    </xsl:if>
	        <tr>
	            <td>
	                Workflow name:   
	             </td>
	             <td>	                 
                    <xsl:element name="input">
                       <xsl:attribute name="name"><xsl:value-of select="workflow-name"/></xsl:attribute> 
                       <xsl:attribute name="type"><xsl:value-of select="text"/></xsl:attribute>
                       <xsl:attribute name="size"><xsl:value-of select="30"/></xsl:attribute>
                       <xsl:attribute name="value"><xsl:value-of select="@workflow-name"/></xsl:attribute>
                       <xsl:attribute name="tabindex"><xsl:value-of select="1"/></xsl:attribute>
                    </xsl:element>
	             </td>
	        </tr>
	        <tr>
	            <td>
           	        Workflow description:   
           	    </td>
           	    <td>
                    <xsl:element name="input">
                       <xsl:attribute name="name"><xsl:value-of select="workflow-description"/></xsl:attribute> 
                       <xsl:attribute name="type"><xsl:value-of select="text"/></xsl:attribute>
                       <xsl:attribute name="size"><xsl:value-of select="30"/></xsl:attribute>
                       <xsl:attribute name="value"><xsl:value-of select="@workflow-description"/></xsl:attribute>
                       <xsl:attribute name="tabindex"><xsl:value-of select="2"/></xsl:attribute>
                    </xsl:element>           	        
           	    </td>
           	</tr>
	    </table> 
	    <p></p>  	        
	</xsl:template>
	
	<xsl:template name="workflow-description">
      <xsl:choose>	    

	    <xsl:when test="@template-name = ''">
	       <img src="OneStepJob.gif" title="OneStepJob" alt="" style="border: 0px solid ; width: 200px; height: 250px;"/>
		</xsl:when>


	<!--+
	    | One Job Step
	    +-->
	    <xsl:when test="@template-name = 'OneStepJob'">
           <table border="0">
              <tr>
                 <td>
		            <img src="OneStepJob.gif" title="OneStepJob" alt="missing image" height="201" width="111" style="border: 0px solid ;"/>
		         </td>
		         <td>
		            <xsl:for-each select="//step">
		               <xsl:choose>
		                  <xsl:when test="@tool = 'NullTool_instance'">
                             <xsl:call-template name="SelectTemplate"/>
		                  </xsl:when>
		                  <xsl:otherwise>
		                     Query: <xsl:value-of select="@tool"/>
		                  </xsl:otherwise>		            
		               </xsl:choose>
	                </xsl:for-each>
	             </td>
	          </tr>
		   </table>
		</xsl:when>


	<!--+
	    | Two Parallel Job Steps
	    +-->
	    <xsl:when test="@template-name='TwoParallelJobsteps'">	        
           <table border="0">
              <tr>                 
                 <td rowspan="6">
			        <img src="TwoStepFlow.gif" title="TwoStepFlow" alt="missing image" height="260" width="200" style="border: 0px solid ;"/>
		         </td>
		         <td>
		            <xsl:for-each select="//step">
		               <xsl:choose>
		                  <xsl:when test="@tool = 'NullTool_instance'">
		                     <tr>
		                        <td> 
                                   <xsl:call-template name="SelectTemplate"/>
                                </td>
                             </tr>
		                  </xsl:when>		         
		                  <xsl:otherwise>
		                     <tr>
		                        <td> 
		                           Query: <xsl:value-of select="@tool"/>	            
		                        </td>
		                     </tr>
		                  </xsl:otherwise>	
		               </xsl:choose>
	                </xsl:for-each>
	             </td>
	          </tr>
		   </table>		 
        </xsl:when>


	<!--+
	    | Two Sequential Job Steps
	    +-->
	    <xsl:when test="@template-name = 'TwoSequentialJobsteps'">
           <table border="0">
              <tr>
                 <td rowspan="6">
			        <img src="TwoStepSequence.gif" title="TwoStepSequence" alt="missing image" height="260" width="111" style="border: 0px solid ;"/>
		         </td>
		         <td>
		         <xsl:for-each select="//step">		             
		            <xsl:choose>
		               <xsl:when test="@tool = 'NullTool_instance'">         
		                  <tr>
		                     <td valign="bottom">
		                        Select query:
		                     </td>
                             <td valign="bottom">
                                <xsl:call-template name="SelectTemplate"/>
                             </td>
                          </tr>
                          <tr>
                             <td valign="top">
                                Join Condition:                                 
                             </td>
                             <td valign="top">
                                <xsl:call-template name="JoinConditionTemplate"/> 
                             </td>
                          </tr>                      
                       </xsl:when>
                       <xsl:otherwise>
		                  <tr> 
		                     <td valign="bottom"> 
		                        Query:
		                     </td>
		                     <td valign="bottom">
		                        <xsl:value-of select="@tool"/> 
		                     </td>
		                  </tr>
		                  <tr>
		                     <td valign="top">
                                 Join Condition: 
                              </td>
                              <td valign="top">
                                 <xsl:call-template name="JoinConditionTemplate"/>
                              </td>
                           </tr>
		               </xsl:otherwise>           
		            </xsl:choose>    
		         </xsl:for-each>
		         </td>
		      </tr>
		   </table>
	   </xsl:when>

		
	   <xsl:otherwise>Unknown template: <xsl:value-of select="@template-name"/></xsl:otherwise>
	  </xsl:choose>
	</xsl:template>			
		
	<!--+
	    | Main menu
	    +-->	
	<xsl:template name="actions_menu">
    <!-- Design Jobs Menu -->  
      <table cellpadding="2" cellspacing="2" border="0">
        <tbody>
          <tr width="80%" align="center" valign="middle" style="color: rgb(51,51,255); background-color: rgb(204,204,204);">
            <td width="0" height="0" colspan="6" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              Create workflow
            </td>
          </tr>          
          <tr width="80%" align="center" valign="middle" style="color: rgb(51,51,255); background-color: rgb(204,204,204);">
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agjobmonitor.html?action=save-workflow">
                Save
              </a>
            </td>
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agjobmonitor.html">
                Clear
              </a>
            </td>
          </tr>      
        </tbody>
      </table> 
	</xsl:template>

    <!--+
	    | Query selection template
	    +-->	
	<xsl:template name="SelectTemplate">
       <form method="get" name="SelectForm"> 
	   	  <select name="query-name">
	         <option value="none">-- please choose --</option>
             <xsl:for-each select="//query">
                <xsl:element name="option">
                   <xsl:attribute name="value"><xsl:value-of select="@query-name"/></xsl:attribute>
				   <xsl:value-of select="@query-name"/>
				</xsl:element>
             </xsl:for-each>
		  </select>
		  <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input>		                     	                 		                 		                 
		  <input type="submit" name="action" value="choose-query" />
	   </form>
	</xsl:template>
	
    <!--+
	    | Query selection template
	    +-->	
	<xsl:template name="JoinConditionTemplate">
       <form method="get" name="SelectForm">
   	      <xsl:choose>
             <xsl:when test="@joinCondition = 'any'"> 
                <input type="radio" name="edit-condition" value="ANY" checked="true">any</input>
                <input type="radio" name="edit-condition" value="TRUE">true</input>
                <input type="radio" name="edit-condition" value="FALSE">false</input>
             </xsl:when>
   	         <xsl:when test="@joinCondition = 'true'"> 
                <input type="radio" name="edit-condition" value="ANY">any</input>
                <input type="radio" name="edit-condition" value="TRUE" checked="true">true</input>
                <input type="radio" name="edit-condition" value="FALSE">false</input>
             </xsl:when>
   	         <xsl:when test="@joinCondition = 'false'"> 
                <input type="radio" name="edit-condition" value="ANY">any</input>
                <input type="radio" name="edit-condition" value="TRUE">true</input>
                <input type="radio" name="edit-condition" value="FALSE" checked="true">false</input>
             </xsl:when>                        	      
          </xsl:choose>		                  
		  <input type="hidden" name="activity-key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input>          		                     	                 		                 		                    
	      <input type="submit" name="action" value="edit-join-condition" />
	   </form>
	</xsl:template>		
	
</xsl:stylesheet>
		
	