<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="view=source" />
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
	                Workflow name:<xsl:value-of select="@workflow-name"/>
	             </td>
	        </tr>
	        <tr>
	            <td>
           	        Workflow description:<xsl:value-of select="@workflow-description"/>
           	    </td>
           	</tr>
	    </table> 
	    <p></p>  	        
	</xsl:template>
	
	<xsl:template name="workflow-description">
      <xsl:choose>	    

	    <xsl:when test="@template-name = ''">
		    <a href="http://www.astrogrid.org">
			    <img src="OneStepJob.gif" title="OneStepJob" alt="" style="border: 0px solid ; width: 200px; height: 250px;"/>
		    </a>
		</xsl:when>


	<!--+
	    | One Job Step
	    +-->
	    <xsl:when test="@template-name = 'OneStepJob'">
           <table border="0">
              <tr>
                 <td>
		            <a href="http://www.astrogrid.org">
		               <img src="OneStepJob.gif" title="OneStepJob" alt="" style="border: 0px solid ;"/>
		            </a>
		         </td>
		         <td>
		            <xsl:for-each select="//step">
		               <xsl:choose>
		                  <xsl:when test="@step-name = 'StepOne'">
                             <xsl:call-template name="SelectTemplate"/>
		                  </xsl:when>
		                  <xsl:otherwise>Query: <xsl:value-of select="@step-name"/></xsl:otherwise>		            
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
		            <a href="http://www.astrogrid.org">
			           <img src="TwoStepFlow.gif" title="TwoStepFlow" alt="" style="border: 0px solid ;"/>
		            </a>
		         </td>
		      </tr>
		      <xsl:for-each select="//step">
		      <xsl:choose>
		         <xsl:when test="@step-name = 'StepOne'">
                    <xsl:call-template name="SelectTemplate"/>
		         </xsl:when>
		         <xsl:when test="@step-name = 'StepTwo'">
                    <tr><xsl:call-template name="SelectTemplate"/></tr>
		         </xsl:when>		         
		         <xsl:otherwise>Query: <xsl:value-of select="@step-name"/></xsl:otherwise>		            
		      </xsl:choose>
	       </xsl:for-each>
		</table>		 
       </xsl:when>


	<!--+
	    | Two Sequential Job Steps
	    +-->
	    <xsl:when test="@template-name = 'TwoSequentialJobsteps'">
           <table border="0">
              <tr>
                 <td rowspan="6">
		            <a href="http://www.astrogrid.org">
			           <img src="TwoStepSequence.gif" title="TwoStepSequence" alt="" style="border: 0px solid ;"/>
		            </a>
		         </td>
		         <td>
		            <xsl:for-each select="//step">
		               <xsl:choose>
		                  <xsl:when test="@step-name = 'StepOne'">
                             <tr><xsl:call-template name="SelectTemplate"/></tr>
		                  </xsl:when>
		                  <xsl:when test="@step-name = 'StepTwo'">
                             <tr><xsl:call-template name="SelectTemplate"/></tr>
		                  </xsl:when>		            
		                  <xsl:otherwise>
		                     Query: <xsl:value-of select="@step-name"/>
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
              <a href="agworkflow-administer.html?action=save-workflow">
                Save
              </a>
            </td>
            <td width="0" height="0" colspan="1" rowspan="1" style=" font-family: arial,helvetica,sans-serif; font-size-adjust: 2; font-weight: bold; height: 30px; width: 200px;">
              <a href="agworkflow-admin.html">
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
		  <td>
   	         Select query: 
	   	     <select name="query-name">
	   	     <option value="none">-- please choose --</option>
                <xsl:for-each select="//query/options/name">
				   <xsl:element name="option">
                      <xsl:attribute name="value">
               		     <xsl:value-of select="@val"/>
				      </xsl:attribute>
                	  <xsl:value-of select="@val"/>
				   </xsl:element>
				</xsl:for-each>
		     </select>
		     <input type="hidden" name="activity-key">
		        <xsl:attribute name="value">
                   <xsl:value-of select="@key"/>
                </xsl:attribute>
		     </input>		                     
		  </td>
		  <td>	                 		                 		                 
		     <input type="submit" name="action" value="choose-query" />
		  </td>		                 
	   </form>
	</xsl:template>	
	
</xsl:stylesheet>
		
	