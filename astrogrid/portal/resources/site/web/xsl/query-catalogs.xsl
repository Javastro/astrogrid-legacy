<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Nov 2004
       +-->

    <xsl:template name="query-catalogs">       
      <div id="query-catalogs" style="display: none ">
        <form name="catalog_form" 
              id="catalog_form" 
              action="/astrogrid-portal/bare/mount/resources/resourceResults.html" 
              method="post" 
              target="resourcesResultsFrame">
          <table border="1" cellpadding="5">
            <tr valign="top">
              <td>
                <table border="0">
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
                  <tr valign="top">
                    <td nowrap="true">Resource name</td>
                    <td>
                      <input title="eg: vocone - (AuthorityID, ResourceKey)" type="text" name="resourceNameField" id="resourceNameField" onblur="defocusit(this)" onfocus="focusit(this)" >
                      </input>
                    </td>
                    <td><span>string</span></td>
                  </tr>
                  <tr valign="top">
                    <td nowrap="true">Resource publisher</td>
                    <td>
                      <input title="eg: MSSL/UCL Mullard Space Science Laboratory" type="text" name="publisherField" id="publisherField" onblur="defocusit(this)" onfocus="focusit(this)" >
                      </input>
                    </td>
                    <td><span>string</span></td>
                  </tr>
                  <tr>
                    <td nowrap="true">Resource title</td>
                    <td>
                      <input title="eg: NOAA SGAS Energetic Events" type="text" name="titleField" id="titleField" onblur="defocusit(this)" onfocus="focusit(this)" >
                      </input>
                    </td>
                    <td><span>string</span></td>
                  </tr>
                  <tr valign="top">
                    <td>Description</td>
                    <td>
                      <input title="eg: This is the Solar Event Catalogue" type="text" name="descriptionField" id="descriptionField" onblur="defocusit(this)" onfocus="focusit(this)" >
                      </input>                      
                    </td>
                    <td><span>string</span></td>
                  </tr>                  
                  <tr>
                    <td align="center" colspan="3">
                      and<input type="radio" name="ConstraintAndOr" value="AND" checked="true"/>
                      or<input type="radio" name="ConstraintAndOr" value="OR"/>
                    </td>
                  </tr>
                </table>
              </td>          

           <td>
             <table border="0" width="100%">
               <tbody>
                 <tr>
                   <td valign="center">
                     <input type="text" size="4" id="andor1" readonly="true" value="AND"/>
                   </td>    
                   <td align="center">
                     Wavelength<br />
                     <select size="7" multiple="multiple" type="multiple" name="wavelength" id="wavelengthSelect">
                       <xsl:for-each select="//Wavelength">
                         <xsl:sort select="." />
                         <xsl:element name="option">
                           <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
				           <xsl:value-of select="."/>
				         </xsl:element>
                       </xsl:for-each>
                     </select>
                     <br />
                     and<input type="radio" 
                               name="WavelengthAndOr" 
                               checked="true" 
                               value="AND"
                               title="Multiple selections above: search will be for xxx AND yyy" 
                               onblur="defocusit(this)" 
                               onfocus="focusit(this)"/>
                     or <input type="radio" 
                               name="WavelengthAndOr"
                               value="OR"
                               title="Multiple selections above: search will be for xxx OR yyy" 
                               onblur="defocusit(this)" 
                               onfocus="focusit(this)"/>                               
                   </td>
                   <td valign="center">
                     <input type="text" size="4" id="andor2" readonly="true" value="AND" />
                   </td>
                   <td align="center">
                     Mission<br />
                     <select size="7" multiple="multiple" type="multiple" name="mission">
                       <xsl:for-each select="//Facility">
                         <xsl:sort select="." />
                         <xsl:element name="option">
                           <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
				           <xsl:value-of select="."/>
				         </xsl:element>
                       </xsl:for-each> 
                     </select>
                     <br />
                     and<input type="radio" 
                               name="MissionAndOr" 
                               checked="true" 
                               value="AND"
                               title="Multiple selections above: search will be for xxx AND yyy" 
                               onblur="defocusit(this)" 
                               onfocus="focusit(this)"/>
                     or <input type="radio" 
                               name="MissionAndOr"
                               value="OR"
                               title="Multiple selections above: search will be for xxx OR yyy" 
                               onblur="defocusit(this)" 
                               onfocus="focusit(this)"/>                     
                   </td>
                   <td valign="center">
                     <input type="text" size="4" id="andor3" readonly="true" value="AND"/>
                   </td>                   
                   <td align="center">
                     Keyword<br />
                     <select size="7" multiple="multiple" type="multiple" name="keyword">
                       <xsl:for-each select="//Subject">
                         <xsl:sort select="." />
                         <xsl:element name="option">
                           <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
				           <xsl:value-of select="."/>
				         </xsl:element>
                       </xsl:for-each> 
                     </select>
                     <br />
                     and<input type="radio" 
                               name="KeywordAndOr" 
                               checked="true"
                               value="AND" 
                               title="Multiple selections above: search will be for xxx AND yyy" 
                               onblur="defocusit(this)" 
                               onfocus="focusit(this)"/>
                     or <input type="radio" 
                               name="KeywordAndOr"
                               value="OR"
                               title="Multiple selections above: search will be for xxx OR yyy" 
                               onblur="defocusit(this)" 
                               onfocus="focusit(this)"/>
                   </td>                             
                 </tr>
               </tbody>
             </table>                      
           </td>
         </tr>
         <tr>
           <td colspan="2">
             <table width="100%">
               <tr>
                 <td rowspan="2" valign="center">Search by:</td>                
                 <td><input type="radio" name="finalAndOr" checked="true" value="AND" onclick="selectJoin('AND')"/>constrains AND wavelength AND mission AND keyword</td>
                 <td rowspan="2" valign="center">
                   <input class="agActionButton" type="submit" name="action" value="search for catalogs" />
                   <input class="agActionButton" value="reset" type="reset" />
                   <input type="hidden" name="returnDisplay" value="query-catalogs" />
                 </td>
               </tr>
               <tr>
                 <td><input type="radio" name="finalAndOr" value="OR" onclick="selectJoin('OR')"/>constraints OR wavelength OR mission OR keyword</td>
               </tr>               
             </table>
           </td>
         </tr>
         </table>
         </form>
       </div>
    </xsl:template>            
</xsl:stylesheet>
