<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>
	
	<xsl:param name="action" />		
	<xsl:param name="errormessage" />
	<xsl:param name="message" />
	<xsl:param name="secure_url" />	
	<xsl:param name="secure_connection" />
	<xsl:param name="admin">No Administrator found.</xsl:param>
	<xsl:param name="adminEmail">No Email found.</xsl:param>

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the login element.
		+-->
	<xsl:template match="login">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="login_form"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="login_form">
		<xsl:if test="$errormessage != ''">	
			<font color="red">
				<xsl:value-of select="$errormessage" />
			</font>	<br />
		</xsl:if>
		<xsl:if test="$message != ''">	
			<font color="blue">
				<xsl:value-of select="$message" />
			</font>	<br />
		</xsl:if>
		<xsl:if test="$secure_connection = 'on'">	
			<font color="blue">
			  This information will be sent securely over the internet, then redirect to a regular connection.
			</font>	<br />
		</xsl:if>
		
		
		<br /><br />
		    <center>
    	<p>
    	<h4>
    	 For access to this facility please consult the Astrogrid Administrator<br />
    	 Administrator for this community is <xsl:value-of select="$admin" /><br />
     	 Administrator's Email for this community is <xsl:value-of select="$adminEmail" /><br />
         </h4>
         </p>
		</center>
		<br />
		<center>
		<xsl:element name="form">
		<xsl:attribute name="method">Post</xsl:attribute>
			<xsl:attribute name="name">Login</xsl:attribute>
			<xsl:if test="$secure_url != ''">
				<xsl:attribute name="action" >
					<xsl:value-of select="$secure_url" />
				</xsl:attribute>
			</xsl:if>
			<input type="hidden" name="action" value="login" />
					      <table>
		        <tr>
		          <td align="right">Username:</td>
		          <td align="left"><input name="Username" type="text"/></td>
		        </tr>
		        <tr>
		          <td align="right">Password:</td>
		          <td align="left"><input name="Password" type="password"/></td>
		        </tr>
		        <tr> 
		          <td colspan="2" align="center">
		            <input type="submit" value="Login"/></td>
		        </tr>
		      </table>
		</xsl:element>
		</center>
		   <br />
		   If you are new to Astrogrid please sign in here click: <a href="agadministration.html?action=insertaccount">Register</a>
		                              <!-- End Real  pages go here -->
	
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