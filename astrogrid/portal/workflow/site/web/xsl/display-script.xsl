<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+
       | Display script details
       |
       | Display table containing details of script.
       |
       +-->
    <xsl:template name="script-details">
      <p /> 
      <div style="visibility: hidden" id="script_details">
        <form name="script_form" id="script_form">
          <table width="95%"  border="2" cellspacing="0" cellpadding="0">
            <tr>
              <td>
                <div style="color: blue; background-color: lightblue; text-align: center;">Script:</div>
              </td>
            </tr>
            <tr>
              <td>
                Description: <input type="text" size="60" name="script_description"/>
                <input type="submit" name="action" value="insert-script-description" />
              </td>
            </tr>
            <tr>
              <td>
                <textarea name="script_body" cols="45" rows="4">...</textarea>
                <input type="submit" name="action" value="insert-script-body" />
              </td>
            </tr>
          </table>
          <input type="hidden" name="activity_key"/>
        </form>
      </div>                                                                                                  
    </xsl:template>
        
</xsl:stylesheet>
