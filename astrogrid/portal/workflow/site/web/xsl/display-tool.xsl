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
       | Also includes container_insert_form which simply holds value of selected id and index
       | that is used when user attempts to insert a sequence/flow/step
       +-->
    <xsl:template name="tool-details">
    <p />
        <div style="visibility: hidden" id="step_tool_details">
            <form name="properties_form">
                <table width="50%"  border="2" cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="50%" height="100%" valign="top">
                            <table width="100%" border="1" cellspacing="0" cellpadding="0">
                                <tr>                            
                                    <div style="color: blue; background-color: lightblue; text-align: center;">Step</div>
                                        <td width="30%">Name:</td>
                                        <td width="70%">
                                            <input type="text" width="40" name="step_name"></input>
                                            <a id="step_name_button">
                                                <input type="submit" name="action" value="add-step-name"/>
                                                <input type="hidden" name="activity_key"/>                                            
                                            </a>                                     
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="30%">Join:</td>
                                        <td>
                                            <input type="text" name="edit_condition"/>
                                            any <input type="radio" onClick="document.properties_form.edit_condition.value = 'any'; "/>
                                            true <input type="radio" onClick="document.properties_form.edit_condition.value = 'true'; " />
                                            false <input type="radio" onClick="document.properties_form.edit_condition.value = 'false';" />
                                            <input type="submit" name="action" value="edit-join-condition"/>
                                        </td>
                                    </tr>                                     
                                    <tr>
                                        <td>Description:</td>
                                        <td>
                                            <textarea name="step_description" cols="45" rows="2" >...</textarea>
                                            <input type="submit" name="action" value="add-step-description" />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td width="50%" height="100%" valign="top">
                                <table width="100%"  border="1" cellspacing="0" cellpadding="0">
                                    <tr>                         
                                        <div style="color: blue; background-color: lightblue; text-align: center;">Tool</div>
                                        <td width="30%">Name:</td>
                                        <td width="70%">
                                            <input type="text" width="40" name="tool_name"></input>
                                            <a>
                                                <input type="submit" name="action" value="insert-tool-into-step"/>
                                            </a>
                                        </td>
                                    </tr>                                                                      
                                    <tr>
                                        <td></td>
                                        <td>
                                            <div id="tool_select_dropdown">                                                
                                                <select name="tool_list" size="1" id="select_list" onClick=" document.properties_form.tool_name.value = document.properties_form.select_list.value;">
	                                                <option value="none">-- Select tool --</option>
                                                    <xsl:for-each select="toolsAvailable">
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
                                        <td><textarea name="tool_documentation" cols="45" rows="5">Choose relevant step...</textarea></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                    <input type="hidden" name="display_tool_values"><xsl:attribute name="value">true</xsl:attribute></input>                    
                </form>                
            </div> 
<!-- This is not right, and temporary! -->                
                <form action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" name="insert_sequence_form">
                   <input type="hidden" name="activity_key" id="activity_key"/>
                   <input type="hidden" name="activity_index_key"/>
                   <input type="hidden" name="activity_index_order"  id="activity_index_order_sequence"/>
                   <input type="hidden"  name="action" value="insert-sequence"/>                           
                </form>            
                <form action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" name="insert_flow_form">
                   <input type="hidden" name="activity_key" id="activity_key"/>
                   <input type="hidden" name="activity_index_key"/>
                   <input type="hidden" name="activity_index_order"  id="activity_index_order_flow"/>
                   <input type="hidden"  name="action" value="insert-flow"/>                       
                </form>                
                <form action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" name="insert_step_form">
                   <input type="hidden" name="activity_key" id="activity_key"/>
                   <input type="hidden" name="activity_index_key"/>
                   <input type="hidden" name="activity_index_order"  id="activity_index_order_step"/>
                   <input type="hidden"  name="action" value="insert-step"/>            
                </form>
                <form action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" name="remove_activity_form">
                   <input type="hidden" name="activity_key"/>
                   <input type="hidden" name="activity_index_key"/>
                   <input type="hidden"  name="action" value="remove_activity"/>            
                </form>                                                                                              
        </xsl:template>
        
</xsl:stylesheet>
