<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Nov 2004
       +-->

  <!--+
       |
       +-->
    <xsl:template name="query-intro">
      <div id="query-intro" style="display:">
        <table border="0" cellpadding="4">
          <tr>
            <td>
              <ul>
                <li>This page allows resource discovery, presently <b>tasks</b> and <b>catalogs</b> </li>
                <li>The search is case insensitive and uses 'contains'.</li>
                <li>The search will return a maximum of 100 entries.</li>
                <li>(Certain characters are not permitted when searching - these are: ' " &amp; &lt; )</li>
              </ul>
            </td>
          </tr>
        </table>
      </div>                                                                                                        
    </xsl:template>            
</xsl:stylesheet>