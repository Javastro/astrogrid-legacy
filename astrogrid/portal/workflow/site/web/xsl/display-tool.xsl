<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">



  <!--+
          | Display tool details
          |
          | Display table containing details of step and the tool that step contains.
          | Details of parameters etc are displayed elsewhere. 
          | If step contains no tool then tool can be inserted into step from here.
          | Join condition can be alterred from here.
          | Step name/description can be set from here.
          |
          +-->
    <xsl:template name="tool-details">
    <p />
    <div style="visibility: hidden" id="step_tool_details">
        <form name="properties_form">
            <table width="80%"  border="1" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="50%" valign="top">
                        <table width="100%"  border="1" cellspacing="0" cellpadding="0">
                            <tr>                            
                                <div class="agWorkflow_table_header">Step</div>
                                    <td width="30%">Name:</td>
                                    <td width="70%">
                                        <input type="text" name="step_name" size="34"></input>
                                        <a style="visibility: hidden" id="step_name_button">
                                            <input type="submit" value="Submit " action="add-step-description" onClick="hide_select('tool_name_button');"/>
                                            <input type="hidden" name="activity_key"></input>                                            
                                        </a>                                     
                                        </td>
                                </tr>
                                <tr>
                                    <td width="30%">Join:</td>
                                    <td><input type="text" name="join_condition" size="10" />
                                        any <input type="radio" onClick="document.properties_form.join_condition.value = 'any'; "/>
                                        true <input type="radio" onClick="document.properties_form.join_condition.value = 'true'; " />
                                        false <input type="radio" onClick="document.properties_form.join_condition.value = 'false';" />
                                        <input type="submit" value="change " action="edit-join-condition" />
                                    </td>
                                </tr>                                     
                                <tr>
                                    <td>Description:</td>
                                    <td><textarea name="step_description" cols="50" rows="2" >...</textarea>
                                    <input type="submit" value="Submit " action="add-step-description" /></td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" valign="top">
                            <table width="100%"  border="1" cellspacing="0" cellpadding="0">
                                 <tr>                         
                                     <div class="agWorkflow_table_header">Tool</div>
                                        <td width="30%">Name:</td>
                                        <td width="70%"><input type="text" name="tool_name" size="34"></input>
                                        <div style="display:none" id="tool_select_button"><input type="submit" value="Insert tool " action="insert-tool-into-step"  onclick="document.getElementById('tool_select_button').style.display='none';"/></div></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>
                                          <div style="display:none" id="tool_select_dropdown">                                                
                                            <select name="tool_list" size="1" id="select_list" onClick="document.getElementById('tool_select_dropdown').style.display='none'; document.getElementById('tool_select_button').style.display=''; document.properties_form.tool_name.value = document.properties_form.select_list.value;">
	                                          <option value="none">-- Select tool --</option>
                                                <xsl:for-each select="//toolsAvailable">
                                                  <xsl:element name="option">
                                                    <xsl:attribute name="value"><xsl:value-of select="@tool-name"/></xsl:attribute>
				                                    <xsl:value-of select="@tool-name"/>
				                                  </xsl:element>
                                                </xsl:for-each>
		                                      </select>                                                                                                                                                
                                            </div>                                         
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Description:</td>
                                        <td><textarea name="tool_documentation" cols="50" rows="3">Choose relevant step...</textarea></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>                    
                </form>
            </div>
        </xsl:template>
        
</xsl:stylesheet>
