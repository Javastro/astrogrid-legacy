<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">



  <!--+
          | Display tool details
          +-->
    <xsl:template name="tool-details">
    <p />
    <div style="visibility: hidden" id="step_tool_details">
        <form name="properties_form">
            <table width="80%"  border="1" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="50%">
                        <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                            <div class="agWorkflow_table_header">Step</div>
                                <tr>
                                    <td width="30%">Name:</td>
                                    <td width="70%"><input type="text" name="step_name" size="30"></input><a style="visibility: hidden" id="step_name_button"><input name="" type="button" value="Submit " onClick="hide_select('tool_name_button');"/></a></td>
                                </tr>
                                <tr>
                                    <td width="30%">Join:</td>
                                    <td width="70%"><input type="text" name="join_condition" size="30"></input></td>
                                </tr>                                     
                                <tr>
                                    <td>Description:</td>
                                    <td><textarea name="step_desc" cols="50" rows="3" >Choose relevant step...</textarea></td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%">
                            <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                                <div class="agWorkflow_table_header">Tool</div>
                                    <tr>
                                        <td width="30%">Name:</td>
                                        <td width="70%"><input type="text" name="tool_name" size="30"></input></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>
                                            <div style="visibility: hidden" id="tool_select_dropdown">
<!-- TODOD: onClick call insert tool into step! -->                                             
                                                <select name="tool_list" size="1" id="select_list" onClick="hide_select('tool_select_dropdown'); hide_select('tool_select_button'); document.properties_form.tool_name.value = document.properties_form.select_list.value;">
                                                    <option value="6Df">6Df</option>
                                                    <option value="Data Federation">Data Federation</option>
                                                    <option value="Sextractor">Sextractor</option>
                                                    <option value="HyperZ">HyperZ</option>
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
