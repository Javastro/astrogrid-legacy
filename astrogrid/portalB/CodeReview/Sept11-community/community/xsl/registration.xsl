<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>
	
	<xsl:param name="ErrorMessage" />	

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the register element.
		+-->
	<xsl:template match="register">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="register_form"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="register_form">
		<xsl:if test="$ErrorMessage != ''">	
			<font color="red">
				<xsl:value-of select="$ErrorMessage" />
			</font>	
		</xsl:if>
		<strong>Astrogrid Registration</strong> <br />		
		<form method="get" name="RegisterForm">
			<strong>User Name: </strong> <input type="text" name="username" />
			<br />
			<strong>Password: </strong> <input type="password" name="password" />
			<br />
			<strong>Community: </strong>
			<select name="community">
				<xsl:for-each select="//register/options/communities/community">
					<xsl:element name="option">
						<xsl:attribute name="value">
							<xsl:value-of select="@val"/>
						</xsl:attribute>
						<xsl:value-of select="@val"/>
					</xsl:element>
				</xsl:for-each>
			</select>
			<br />
			<strong>First Name: </strong> <input type="text" name="firstname" />
			<strong>Last Name: </strong> <input type="text" name="lastname" />
			<input type="submit" name="register" value="Register" />
		</form>
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
