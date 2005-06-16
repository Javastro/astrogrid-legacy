<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->
       
 <!--+
          | Display tool details
          |
          | Display table containing details of input and output parameters.
          | Allows inserting of parameter values.
          | Pop-up for micro-browser also available
          |
          +-->
    <xsl:template name="parameter-details">
      <ag-script type="text/javascript" src="/astrogrid-portal/extras.js"/>
      <script language="javascript">    
        function toggleHelp(id)
        {
	  	  if(document.getElementById(id).style.display == "none")
	  	  {
		    document.getElementById(id).style.display = "";
		  } 
		  else 
		  {
			document.getElementById(id).style.display = "none";
		  }        
	    }
	    
        function showHelp(id)
        {	       	  
		  document.getElementById(id).style.display = "";
	    }	            
      </script>                    
        
      <table border="2" cellpadding="0" cellspacing="0">
        <form name="parameter_form" id="parameter_form" action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" target="workflowOuterFrame" method="post" >
        <tr>
          <td align="center" colspan="7">
            <div style="color: blue; background-color: lightblue; text-align: center;">
              Parameters for step:
              <xsl:choose>
                <xsl:when test="@step-name != 'null'"> 
                  <b> <xsl:value-of select="@step-name"/></b>;
                </xsl:when>
                <xsl:otherwise>
                  --
                </xsl:otherwise>
              </xsl:choose>
              task: 
              <xsl:choose>
                <xsl:when test="./tool/@tool-name != 'null'"> 
                  <b> <xsl:value-of select="./tool/@tool-name"/></b>.
                </xsl:when>
                <xsl:otherwise>
                  --
                </xsl:otherwise>
              </xsl:choose>             
            </div>
          </td>
        </tr>
        <xsl:choose>
          <xsl:when test="./tool/@tool-name != 'null'">                                    
            <xsl:if test="./tool/inputParam" >
              <tr><td colspan="5">
                <div style="color: blue; background-color: lightblue; text-align: center;">( input parameters for this task: )</div>
              </td></tr> 
              <xsl:for-each select="./tool/inputParam">
                <xsl:sort select="@param-name"/> 
                <xsl:call-template name="parameter">                                     
                  <xsl:with-param name="direction">input</xsl:with-param>
                </xsl:call-template>                              
              </xsl:for-each>
            </xsl:if>

              <xsl:if test="./tool/outputParam" >  <!-- Email tool has no output params, so don't display -->      
                <tr><td colspan="5">
                  <div style="color: blue; background-color: lightblue; text-align: center;">( output parameters for this task: )</div>
                </td></tr>                
                <xsl:for-each select="./tool/outputParam">  
                  <xsl:call-template name="parameter">
                    <xsl:with-param name="direction">output</xsl:with-param>
                  </xsl:call-template>
                </xsl:for-each>
              </xsl:if>
              <tr>
                <td colspan="6">
                  <div style="display: none; color: blue;">
                    <xsl:attribute name="id">multiParamDiv<xsl:value-of select="@key"/></xsl:attribute> 
                    * task may contain more than one occurence of this parameter. <br />
                      Please enter each value separated by ++.
                  </div>
                </td>                        
              </tr>              
              <tr>
                <td colspan="6">
                  <div style="color: blue; background-color: lightblue; text-align: center;">
                    <input class="agActionButton" type="submit" value="Update parameter values" />                    
                  </div>
                </td>                        
              </tr>                          
            <input type="hidden" name="action" value="insert-parameter-value" />
            <input type="hidden" name="display_parameter_values"><xsl:attribute name="value">true</xsl:attribute></input> 
            <input type="hidden" name="input_param_count"><xsl:attribute name="value"><xsl:value-of select="count(./tool/inputParam)"/></xsl:attribute></input>
            <input type="hidden" name="output_param_count"><xsl:attribute name="value"><xsl:value-of select="count(./tool/outputParam)"/></xsl:attribute></input>
            <input type="hidden" name="activity_key"><xsl:attribute name="value"><xsl:value-of select="@key"/></xsl:attribute></input>                                                     
          </xsl:when>
          <xsl:otherwise>
            <tr>
              <td colspan="6" > There is currently no task associated with this step </td>
              <td style="cursor: help; text-align: center">                                     
                <xsl:element name="img">
                  <xsl:attribute name="src">/astrogrid-portal/mount/workflow/Help3.png</xsl:attribute>
                  <xsl:attribute name="alt">info</xsl:attribute>
                  <xsl:attribute name="onClick">showHelp('helpDiv<xsl:value-of select="@key"/>');</xsl:attribute>
                </xsl:element>                                    
              </td>              
            </tr>
            <tr>
              <td colspan="7">
                <div style="display: none; color: blue;  text-align: left;">
                  <xsl:attribute name="id">helpDiv<xsl:value-of select="@key"/></xsl:attribute>                    
                    Select a <b>task</b> from the dropdown list (below). <br />
                    Once inserted the parameters will be displayed <br />
                    when the step is clicked, and values can be entered. 
                </div>
              </td>
            </tr>                
          </xsl:otherwise>
        </xsl:choose> 
        </form>                       
      </table>
    </xsl:template>


    <!--+
          | Match the parameter element.
          +-->
    <xsl:template name="parameter">
      <xsl:param name="direction"/>      
          <tr>
            <td>
              <xsl:attribute name="style">cursor: help</xsl:attribute>                                    
              <xsl:attribute name="href">javascript:void(0);</xsl:attribute>
              <xsl:attribute name="onMouseOver">this.T_TITLE='Parameter: <xsl:value-of select="@param-name" /> '; this.T_WIDTH=250; this.T_DELAY=500; this.T_STICKY=true; return escape('' +
                                 ' &lt;b&gt;Description: &lt;/b&gt; <xsl:value-of select="@param-UI-description" /> ' +
                                 ' &lt;br/&gt; &lt;b&gt;Type:&lt;/b&gt; <xsl:value-of select="@param-type" /> ' +
                                 ' &lt;br/&gt; &lt;b&gt;Subtype:&lt;/b&gt; <xsl:value-of select="@param-subtype"/> ' +
                                 ' &lt;br/&gt; &lt;b&gt;Units:&lt;/b&gt; <xsl:value-of select="@param-units"/> ' +
                                 ' &lt;br/&gt; &lt;b&gt;UCD:&lt;/b&gt; <xsl:value-of select="@param-ucd"/> ' +
                                 ' &lt;br/&gt; &lt;b&gt;Default:&lt;/b&gt; <xsl:value-of select="@param-defaultValue"/> ') ;
<!--       ' &lt;br/&gt; &lt;b&gt;Indirect?:&lt;/b&gt; <xsl:value-of select="@param-indirect"/> ' +   
                                 ' &lt;br/&gt; Cardinality max: <xsl:value-of select="@param-cardinality-max"/> ' +
                                 ' &lt;br/&gt; Cardinality min: <xsl:value-of select="@param-cardinality-min"/> ' ) -->
              </xsl:attribute>
              <xsl:choose>
                <xsl:when test="@param-UI-name != ''">
                  <xsl:value-of select="@param-UI-name"/> 
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="@param-name" />
                </xsl:otherwise>
              </xsl:choose>                       
            <xsl:if test="@param-cardinality-max='0'">
              <b> *</b>
              <ag-onload>             
                <xsl:attribute name="function">showHelp('multiParamDiv<xsl:value-of select="./../../@key"/>');</xsl:attribute>                 	
              </ag-onload>                
            </xsl:if>                                                    
          </td>
          <xsl:variable name="ivorn-id"><xsl:value-of select="count(preceding-sibling::*)"/>ivorn</xsl:variable>
          <xsl:variable name="agsl-id"><xsl:value-of select="count(preceding-sibling::*)"/>agsl</xsl:variable>
          <xsl:variable name="indirect-id"><xsl:value-of select="count(preceding-sibling::*)"/>param</xsl:variable>
          <td>
            <input type="text" size="50">
              <xsl:attribute name="name">param-value#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>                
              <xsl:attribute name="id"><xsl:value-of select="$agsl-id"/></xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
              <xsl:if test="$direction = 'output'">
                <xsl:attribute name="onFocus">                
                               javascript:document.getElementById('<xsl:value-of select="$indirect-id"/>').checked = 'true';
                </xsl:attribute> 
              </xsl:if>             
            </input>                                                                    
            <input type="hidden">
              <xsl:attribute name="name">original-param-value#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
            </input>
            <input type="hidden">
              <xsl:attribute name="name">ivorn-value#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
              <xsl:attribute name="id"><xsl:value-of select="$ivorn-id"/></xsl:attribute>
            </input>
            <input type="hidden">
              <xsl:attribute name="name">original-indirect-value#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="@param-indirect" /></xsl:attribute>
            </input>                                                                                                            
          </td>                            
          <td align="center">
            <input class="agActionButton" name="myspace-name" type="button" value="Browse...">
              <!--xsl:attribute name="onClick">                
                             javascript:document.getElementById('<xsl:value-of select="$indirect-id"/>').checked = 'true'; 
                             popupBrowser('/astrogrid-portal/lean/mount/myspace/myspace-micro?ivorn=<xsl:value-of select="$ivorn-id"/>&amp;agsl=<xsl:value-of select="$agsl-id"/>');
              </xsl:attribute-->
<!--jeff's changes-->
              <xsl:attribute name="onClick">                
                             javascript:document.getElementById('<xsl:value-of select="$indirect-id"/>').checked = 'true'; 
                             void(window.open('/astrogrid-portal/bare/mount/myspace/myspace-micro?ivorn=<xsl:value-of select="$ivorn-id"/>&amp;agsl=<xsl:value-of select="$agsl-id"/>&amp;requested-mode=choose-file-for-workflow', 'mySpaceMicro', 'toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=650, height=300'));
              </xsl:attribute>
            </input>
          </td>
          <td nowrap="true">
            <xsl:attribute name="style">cursor: help</xsl:attribute>                                    
            <xsl:attribute name="href">javascript:void(0);</xsl:attribute>
            <xsl:attribute name="onMouseOver">this.T_TITLE='Indirect flag: '; this.T_WIDTH=250; this.T_DELAY=500; return escape('' +                                                            
                                 'The indirect attribute alters how the value of the parameter is interpreted by CEA.&lt;br/&gt;' +
                                 'If set to &lt;b&gt;true&lt;/b&gt;, the parameter value is expected to be a URI that points to a resource that contains the actual value for this parameter.&lt;br/&gt;' +
                                 'If set to &lt;b&gt;false&lt;/b&gt; (the default), then the parameter value is expected inline.' +
                                 'Finally the indirect attribute alters how the value of the parameter is interpreted by CEA.&lt;br/&gt;' +
                                 'Browsing myspace sets the indirect flag to true. ');
              </xsl:attribute>                                             
            <input type="checkbox">
              <xsl:attribute name="name">param_indirect#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>                
              <xsl:attribute name="id"><xsl:value-of select="$indirect-id"/></xsl:attribute>
              <xsl:if test="@param-indirect = 'true'">
                <xsl:attribute name="checked">true</xsl:attribute>
              </xsl:if>              
            </input>                        
          </td>

          <td nowrap="true">
            <xsl:if test="@param-cardinality-min='0'">
              <xsl:attribute name="style">cursor: help</xsl:attribute>                                    
              <xsl:attribute name="href">javascript:void(0);</xsl:attribute>
              <xsl:attribute name="onMouseOver">this.T_TITLE='Delete: '; this.T_WIDTH=250; this.T_DELAY=500; return escape('' +                                                            
                                 'Check this box to delete this parameter.  Leaving it blank passes an empty '+
                                 'value which may have a different effect from no parameter at all.');
              </xsl:attribute>                                             
              <input type="checkbox">
                <xsl:attribute name="name">param_delete#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
              </input>
            </xsl:if>
          </td>

        </tr>
        <input type="hidden">
          <xsl:attribute name="name">param-name#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute>
        </input>
        <input type="hidden">
          <xsl:attribute name="name">activity_key#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="../../@key"/></xsl:attribute>
        </input>            
        <input type="hidden">
          <xsl:attribute name="name">direction#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/>></xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="$direction"/></xsl:attribute>
        </input>                                        
        <input type="hidden">
            <xsl:attribute name="name">param_indirect_changed#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
            <xsl:attribute name="id">param_indirect_changed#<xsl:value-of select="$direction"/>#<xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
        </input>                               
    </xsl:template>



 <!--+
          | Display tool details for job status page
          |
          |
          +-->
    <xsl:template name="parameter-details-status">
      <table border="2" cellpadding="0" cellspacing="0">
        <tr>
          <td align="center" colspan="2">
            <div style="color: blue; background-color: lightblue; text-align: center;">
              Parameters for step:
              <xsl:choose>
                <xsl:when test="@step-name != 'null'"> 
                  <b> <xsl:value-of select="@step-name"/></b>;
                </xsl:when>
                <xsl:otherwise>
                  --
                </xsl:otherwise>
              </xsl:choose>
              task: 
              <xsl:choose>
                <xsl:when test="./tool/@tool-name != 'null'"> 
                  <b> <xsl:value-of select="./tool/@tool-name"/></b>.
                </xsl:when>
                <xsl:otherwise>
                  --
                </xsl:otherwise>
              </xsl:choose>              
            </div>
          </td>
        </tr>
        <xsl:choose>
          <xsl:when test="@step-tool != 'null'">            
            <xsl:for-each select="./inputParam">
              <xsl:call-template name="parameter_status">
              </xsl:call-template>
            </xsl:for-each>
            <xsl:if test="/outputParam" >              
              <xsl:for-each select="./outputParam">  <!-- Email tool has no output params, so don't display -->
                <xsl:call-template name="parameter_status">
                </xsl:call-template>
              </xsl:for-each>
            </xsl:if>            
          </xsl:when>
          <xsl:otherwise>
            <tr>
              <td colspan="2" >There is currently no tool associated with this step</td>
            </tr>    
          </xsl:otherwise>
        </xsl:choose>                        
      </table>
    </xsl:template>


    <!--+
          | Match the parameter element.
          +-->
    <xsl:template name="parameter_status">

        <form name="parameter_form" id="parameter_form">
          <tr>
            <td><xsl:value-of select="@param-name"/></td>                                                       
            <td><xsl:value-of select="@param-value" /></td>                            
        </tr>                             
      </form>
    </xsl:template>

</xsl:stylesheet>
