<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:param name="user-param" select="user-param"/>
	<xsl:param name="name" select="name"/>
	<xsl:param name="community" select="community"/>	
	<xsl:param name="action" select="action"/>
   <xsl:param name="errormessage" select="errormassage"/>			

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
      <agtemplate title="Job List" help="aghelp.html">
         Action is <xsl:value-of select="$action" /> <br/>
         User Param is <xsl:value-of select="$user-param" /> <br/>
         Name is <xsl:value-of select="$name" /> <br/>
         Community is <xsl:value-of select="$community" /> <br/>
         Error Message is <xsl:value-of select="$errormessage" />
		   <xsl:apply-templates/>
      </agtemplate>
	</xsl:template>

	<!--+
	    | Match the workflow element.
		+-->
	<xsl:template match="job-list">
		<page>
			<!-- Add our page content -->
			<content>
             Action is <xsl:value-of select="$action" />
			    <xsl:call-template name="main_menu"/>
			    <xsl:if test="$action='read-job-list'">
		          <xsl:call-template name="list_jobs" />
		       </xsl:if>			    
			</content>
		</page>
	</xsl:template>
	
	<!--+
	    | Main menu
	    +-->	
	<xsl:template name="main_menu">
    <!-- Design Jobs Menu -->  
      <style type="text/css">
       .error { color : red }
       tr.menu { width : 80%;
                 align : center;
                 valign : middle;
                 color: rgb(51,51,255);
                 background-color: rgb(204,204,204) }
       td.menu { width : 0; 
                 height : 0;
                 colspan : 1;
                 rowspan : 1;
                 font-family: arial,helvetica,sans-serif;
                 font-size-adjust: 2;
                 font-weight: bold;
                 height: 30px;
                 width: 200px }
       td.details { font-weight: bold }
      </style>
      <table cellpadding="2" cellspacing="2" border="0">
        <tbody>
          <tr class="menu" >
            <td class="menu" colspan="6" align="center">
              Submitted Jobs List
            </td>
          </tr>          
	        <xsl:if test="$errormessage != ''">	
	          <tr>
	              <td class="error">
				         <xsl:value-of select="$errormessage" />
			        </td>
			    </tr>
		    </xsl:if>                
        </tbody>
      </table>
	</xsl:template>

	<!--+
	    | List Jobs
	    +-->	
	<xsl:template name="list_jobs">
       <table border="0">
           <xsl:for-each select="//job">
           <tr>
               <td class="details">
                   <xsl:value-of select="@name"/>
               </td>
               <td class="details">
                   <xsl:value-of select="@description"/>
               </td>
               <td class="details">
                   <xsl:value-of select="@status"/>
               </td>
           </tr>
           </xsl:for-each>
       </table>	    
	</xsl:template>
	
</xsl:stylesheet>
		
	
		
	