<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="view=source" />

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
	      <strong>Maintain Workflow</strong>
	      <p>
	          Workflow name:<xsl:value-of select="@workflow-name"/>
	      </p>
	      <p>
	          Workflow description:<xsl:value-of select="@workflow-description"/>	          
	      </p>
	</xsl:template>
	
	<xsl:template name="workflow-description">
		<a href="http://www.astrogrid.org">
			<img src="OneStepJob.gif" title="AstroGrid Home" alt="" style="border: 0px solid ; width: 200px; height: 250px;"/>
		</a>
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
	
	
</xsl:stylesheet>
		
	