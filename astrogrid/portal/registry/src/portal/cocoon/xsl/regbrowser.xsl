<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:param name="mainelement" />
  <xsl:param name="authId" select="authid"/>
  <xsl:param name="resourceKey" select="resourcekey"/>
  <xsl:param name="action" select="action"  />
  <xsl:param name="errormessage" />
  <xsl:param name="queryresult" />
  <xsl:param name="resultlist" />  

  <!--+
      | Match the root element.
    +-->
	<xsl:template match="/">
	  <ag-div>	      
	    <!-- Add our page content -->
		<content>
		  <agPUBMessage>
            Query the Registry Microbrowser - <xsl:value-of
                                                select="$mainelement"/>
          </agPUBMessage> 
		  <ag-script type="text/javascript" src="/astrogrid-portal/extras.js"/>
		  <xsl:apply-templates/>
		</content>
	  </ag-div>
	</xsl:template>


	<!--+
	    | Match the admin element.
		+-->
	<xsl:template match="BrowserQuery">		    
	  <xsl:apply-templates/>
	</xsl:template>

  <xsl:template match="BrowserBody">
    <xsl:if test="$resultlist=''" >
      <xsl:call-template name="browser_form"/>
    </xsl:if>
    <xsl:if test="string-length($resultlist)&gt;0" >
      <xsl:call-template name="results_list"/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="ResultsList">
    <page>
      <!-- Add our page content -->
      <content>
        <xsl:call-template name="results_list"/>
      </content>
    </page>
  </xsl:template>

  <xsl:template match="Description">
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="ResourceKey">
     <em><xsl:apply-templates/></em>
  </xsl:template>

  <!--+
      | Match the Error message.
    +-->
  <xsl:template match="Error">
     <span style="color:red">
        <strong>Error: <xsl:value-of select="$errormessage"/></strong>
     </span>
  </xsl:template>

  <!--+
      | and any debug message.
    +-->
  <xsl:template match="Debug">
     <div style="color:green">
        <em><xsl:apply-templates/></em>
     </div>
  </xsl:template>

  <!--+
      | Generate the query form.
      +-->
  <xsl:template name="browser_form">
    <form method="post" action="registrybrowser.html"
                        name="RegistryBrowser">
      <input type="hidden" name="action" value="queryregistry" />
      <input type="hidden" name="mainelement">
        <xsl:attribute name="value"><xsl:value-of
                       select="$mainelement"/></xsl:attribute>
      </input>      
      <br/>

      <label>Select a <xsl:value-of select="$mainelement"/>
             with the ANY or NONE of the following criteria:
      </label>
      <br/>
      <p>
      <table>
      <tr><td> Authority: </td>
          <td> contains </td>
          <td> <input type="text" name="authId"/> </td>
      </tr>
      <tr><td> Resource: </td>
          <td> contains </td>
          <td> <input type="text" name="resourceKey"/> </td>
      </tr>
      <tr><td> Description: </td>
          <td> contains </td>
          <td> <input type="text" name="title"/> </td>
      </tr>
      <xsl:if test="$mainelement='Catalog'" >
        <tr><td> Table Name: </td>
            <td> equals </td>
            <td> <input type="text" name="tabname"/> </td>
        </tr>
        <tr><td> Column Name: </td>
            <td> equals </td>
            <td> <input type="text" name="colname"/> </td>
        </tr>
        <tr><td> Column UCD: </td>
            <td> equals </td>
            <td> <input type="text" name="colucds"/> </td>
        </tr>
        <tr><td> Column Units: </td>
            <td> equals </td>
            <td> <input type="text" name="colunits"/> </td>
        </tr>
        <tr><td> Column Description: </td>
            <td> contains </td>
            <td> <input type="text" name="coldescr"/> </td>
        </tr>
        <!--tr><td> Number of rows in Catalogue: </td>
            <td> equals </td>
            <td> <input disabled="true" type="text" name="nrows"/> </td>
        </tr>
        <tr><td> Number of variables in Catalogue: </td>
            <td> equals </td>
            <td> <input disabled="true" type="text" name="nvar"/> </td>
        </tr-->
      </xsl:if>
      </table>
      </p>
      <p>    
        <input class="agActionButton" type="submit" name="queryregistry"
               value="Search" />
        <xsl:text>          </xsl:text>
        <input class="agActionButton" type="reset" value="Clear"/>
        <xsl:text>          </xsl:text>
        <input class="agActionButton" type="button" value="Close"
               onclick="window.close()"/>
        <xsl:text>          </xsl:text>
        <input class="agActionButton" type="button" value="Help"
               onclick="window.close()"/>
      </p>
    </form>
  </xsl:template>

  <xsl:template name="results_list">
    <xsl:choose>
      <xsl:when test="$mainelement = 'Catalog'">   <!-- CATALOG --> 
        <p>
            <form method="post" action="registrybrowser.html"
                  name="RegistryBrowser" id="RegistryBrowser">
          <!-- form method="post"
       action="/astrogrid-portal/bare/mount/datacenter/variablesFromMB.html"
                name="RegistryBrowser" id="RegistryBrowser"-->
            <input type="hidden" name="mainelement">
              <xsl:attribute name="value">
                <xsl:value-of select="$mainelement"/>
              </xsl:attribute>
            </input>
            Select a <xsl:value-of select="$mainelement"/> from the following:
            <br/>
              <table>                    
              <xsl:for-each
                    select="//BrowserQuery/BrowserBody/ResultsList/result">
                <tr>
                  <td>
                     <strong> <xsl:value-of select="@key"/> </strong>
                  </td>
                  <td>
                     <em> : <xsl:value-of select="@title"/></em>
                  </td>
                </tr>
                <xsl:for-each select="@table">                      
                   <tr>
                     <td>
                     </td>
                     <td>                                           
                       <input type="radio" name="selektion">
                         <xsl:attribute name="value">
                         <xsl:value-of select="../@identifier"/>/<xsl:value-of
                                                                    select="."/>
                         </xsl:attribute>
                       </input>
                       <xsl:value-of select="."/>
                     </td>
                   </tr>
                </xsl:for-each>
              </xsl:for-each>
           </table>
            <input class="agActionButton" name="queryregistry" type="button"
                   value="Select..." onclick="findSelection()"/>
            <xsl:text>          </xsl:text>
            <input class="agActionButton" type="submit" name="queryregistry"
                     value="Restart" />
            <xsl:text>          </xsl:text>
            <input class="agActionButton" type="button" value="Cancel"
                   onclick="window.close()"/>
            <xsl:text>          </xsl:text>
            <input class="agActionButton" type="button" value="Help"
                   onclick="window.close()"/>
          </form>
        </p> 
        </xsl:when>
    
        <xsl:otherwise>           <!-- TOOL -->
          <p>
            <form method="post" action="registrybrowser.html"
                  name="RegistryBrowser">
              <input type="hidden" name="action" value="selectentry" />
              <input type="hidden" name="authId">
                <xsl:attribute name="value"><xsl:value-of
                               select="$authId"/></xsl:attribute>
              </input>
              <input type="hidden" name="resourceKey">
                 <xsl:attribute name="value">
                   <xsl:value-of select="$resourceKey"/>
                 </xsl:attribute>
              </input>
              <input type="hidden" name="mainelement">
                <xsl:attribute name="value">
                  <xsl:value-of select="$mainelement"/>
                </xsl:attribute>
              </input>
              Select a <xsl:value-of select="$mainelement"/> from the following:
              <br/>      
                <xsl:for-each
                   select="//BrowserQuery/BrowserBody/ResultsList/result">
                  <input type="radio" name="selektion">
                    <xsl:attribute name="value">
                      <xsl:value-of select="@identifier"/>
                    </xsl:attribute>
                  </input>             
                  <strong> <xsl:value-of select="@key"/> </strong>
                  <em> : <xsl:value-of select="@title"/></em>
                  <br/> 
                </xsl:for-each>
              <input class="agActionButton" type="button" value="Select">
                <xsl:attribute name="onClick">
                   getSelectionId('<xsl:value-of select="$authId"/>',
                                  '<xsl:value-of select="$resourceKey"/>');
                </xsl:attribute>                              
              </input>                                             
              <xsl:text>          </xsl:text>
              <input class="agActionButton" type="submit" name="queryregistry"
                     value="Restart" />
              <xsl:text>          </xsl:text>
              <input class="agActionButton" type="button" value="Cancel"
                     onclick="window.close()"/>
              <xsl:text>          </xsl:text>
              <input class="agActionButton" type="button" value="Help"
                     onclick="window.close()"/>
            </form>
          </p>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:template>    


  <!--+
      | Default template, copy all and apply templates.
      +-->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
