<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Feb 2005
       +-->

  <!--+
       |
       +-->
    <xsl:template name="queryMB-tasks">
        
      <script language="javascript">
        /*
         * validateEntries()
         * Prevent characters that may cause xml queries to
         * fail from being entered into search (eg: ' " &lt; &amp;)
         *
         * @param id         
         */
        function validateEntries()
        {
          var name = document.getElementById('taskNameField').value;
          var title = document.getElementById('taskTitleField').value;
          var desc = document.getElementById('taskDescriptionField').value;                              
          var objRegExp = /['"\074\046]/;

          if (objRegExp.test(name) || objRegExp.test(pub) || objRegExp.test(title) || objRegExp.test(desc))
          {
          alert('Search criteria contain characters that are not allowed in when searching..');
            return false;
          }
          else
          {
            return true;
          }
        }
      </script>
              
      <div id="query-tasks" style="display: ">
        <form name="query_form" 
              id="query_form" 
              method="post"
              action="/astrogrid-portal/bare/mount/resources/resourceResultsMB.html" 
              target="workflow_iframeMB"
              onsubmit = "return validateEntries();">
          <table border="1" cellpadding="5">
            <tr valign="top">
              <td rowspan="3">
                <table>
                  <tr>
                    <td colspan="3" 
                        align="center" 
                        style="cursor: help"
                        title="Search uses 'contains' eg: where title contains Energetic, and is case insensitive." 
                        onblur="defocusit(this)" 
                        onfocus="focusit(this)">
                      General Constraints
                    </td>
                  </tr>
                  <tr>
                    <td> Task name: </td>
                    <td> contains </td>
                    <td>
                      <input title="eg: org.astrogrid.localhost - (AuthorityID, ResourceKey)"  
                             name="taskNameField" 
                             id="taskNameField" 
                             type="text" 
                             onblur="defocusit(this)" 
                             onfocus="focusit(this)">
                      </input>
                    </td>
                  </tr>
                  <tr>
                    <td> Task title: </td>
                    <td> contains </td>
                    <td>
                      <input title="eg: Trace Instrument Fits Catalogue"  
                             name="titleField" 
                             id="taskTitleField" 
                             type="text" 
                             onblur="defocusit(this)" 
                             onfocus="focusit(this)">
                      </input>
                    </td>
                  </tr>
                  <tr>
                    <td> Description: </td>
                    <td> contains </td>
                    <td>
                      <input title="eg: part of the astrogrid CEA" 
                             name="descriptionField" 
                             id="taskDescriptionField" 
                             type="text" 
                             onblur="defocusit(this)" 
                             onfocus="focusit(this)">
                      </input>
                    </td>
                  </tr>
                  <tr>
                    <td align="center" colspan="3">
                      and<input type="radio" name="TaskAndOr" value="AND" checked="true"/>
                      or<input type="radio" name="TaskAndOr" value="OR"/>
                    </td>
                  </tr>                                    
                </table>
              </td>          
            </tr>
            <tr>
              <td align="center">
                <input class="agActionButton" type="submit" name="action" value="search for tasks" /><br />
                <input class="agActionButton" value="reset" type="reset" /><br />
                <input class="agActionButton" type="button" value="close" onclick="window.close()" />
                <input type="hidden" name="returnDisplay" value="query-tasks" />
                <input type="hidden" name="parent_authId"><xsl:attribute name="value"><xsl:value-of select="$parent_authId"/></xsl:attribute></input>
                <input type="hidden" name="mainelement"><xsl:attribute name="value"><xsl:value-of select="$mainelement"/></xsl:attribute></input>
              </td>
            </tr>
          </table>
        </form>
      </div>                                                                                                
    </xsl:template>            
</xsl:stylesheet>
