<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>
	
	<xsl:param name="action" />		
	<xsl:param name="ErrorMessage" />	

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the admin element.
		+-->
	<xsl:template match="admin">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="admin_form"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="admin_form">
		<xsl:if test="$ErrorMessage != ''">	
			<font color="red">
				<xsl:value-of select="$ErrorMessage" />
			</font>	
		</xsl:if>
		<form method="get" name="AdminTaskSelect">
			<select name="action">
					<xsl:for-each select="//admin/options/actions/action">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:if test="$action = @val">
								<xsl:attribute name="selected">
									<xsl:text>true</xsl:text>
								</xsl:attribute>
							</xsl:if>							
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>			
			</select>
			<input type="submit" name="admintaskselect" value="Go" />
		</form>
		<xsl:if test="$action = 'insertresource'">
			<form method="get" name="AdminInsertResource">
				<input type="hidden" name="processaction" value="insertresource" />			
				<strong>Resource Name: </strong> <input type="text" name="ident" />
				<br />
				<strong>Description: </strong> <input type="text" name="description" />
				<br />
				<input type="submit" name="insertresource" value="Insert Resource" />
			</form>
		</xsl:if>
		<xsl:if test="$action = 'removeresource'">
			<form method="get" name="AdminRemoveResource">
				<input type="hidden" name="processaction" value="removeresource" />
				<strong>Resources: </strong>
				<select name="ident">
					<xsl:for-each select="//admin/options/resources/resource">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<input type="submit" name="removeresource" value="Remove Resource" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'insertgroup'">			
			<form method="get" name="AdminInsertGroup">
				<input type="hidden" name="processaction" value="insertgroup" />
				*<i>You can only insert MULTI user groups, single type's are automatically created with inserting an Account</i>
				<br />
				<strong>Group Name: </strong> <input type="text" name="ident" />
				<br />
				<strong>Description: </strong> <input type="text" name="description" />
				<br />
				*<strong><i>You will become owner of this group, go to "ChangeGroupOwner" for assiging the group to another user</i></strong>
				<br />
				<input type="submit" name="insertgroup" value="Insert Group" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'removegroup'">			
			<form method="get" name="AdminRemoveGroup">
				*<i>You can only remove MULTI user groups, single type's are automatically removed with removing of an Account</i>
				<br />
				<input type="hidden" name="processaction" value="removegroup" />
				<strong>Group Name: </strong> 
				<select name="ident">
					<xsl:for-each select="//admin/options/groups/group">
						<xsl:if test="@type = 'MULTI'" >
							<xsl:element name="option">
								<xsl:attribute name="value">
									<xsl:value-of select="@val"/>
								</xsl:attribute>
								<xsl:value-of select="@name"/>
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
				</select>
				<br />
				<input type="submit" name="removegroup" value="Remove Group" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'insertmember'">			
			<form method="get" name="InsertMember">
				<input type="hidden" name="processaction" value="insertmember" />
				<strong>Group Name: </strong> 
				<select name="group">
					<xsl:for-each select="//admin/options/groups/group">
						<xsl:if test="@type = 'MULTI'" >
							<xsl:element name="option">
								<xsl:attribute name="value">
									<xsl:value-of select="@val"/>
								</xsl:attribute>
								<xsl:value-of select="@name"/>
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
				</select>
				<br />
				<strong>User: </strong>
				<select name="ident">
					<xsl:for-each select="//admin/options/accounts/account">
							<xsl:element name="option">
								<xsl:attribute name="value">
									<xsl:value-of select="@val"/>
								</xsl:attribute>
								<xsl:value-of select="@name"/>
							</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<input type="submit" name="insertmermber" value="Insert Member" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'removemember'">			
			<form method="get" name="RemoveMember">
				<input type="hidden" name="processaction" value="removemember" />
				<strong>Group Name: </strong> 
				<select name="group">
					<xsl:for-each select="//admin/options/groups/group">
						<xsl:if test="@type = 'MULTI'" >
							<xsl:element name="option">
								<xsl:attribute name="value">
									<xsl:value-of select="@val"/>
								</xsl:attribute>
								<xsl:value-of select="@name"/>
							</xsl:element>
						</xsl:if>
					</xsl:for-each>
				</select>
				<br />
				<strong>User: </strong>
				<select name="ident">
					<xsl:for-each select="//admin/options/accounts/account">
							<xsl:element name="option">
								<xsl:attribute name="value">
									<xsl:value-of select="@val"/>
								</xsl:attribute>
								<xsl:value-of select="@name"/>
							</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<input type="submit" name="removemember" value="Remove Member" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'insertpermission'">			
			<form method="get" name="InsertPermission">
				<strong>Group Name: </strong> 
				<select name="group">
					<xsl:for-each select="//admin/options/resources/resource">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<strong>User: </strong>
				<select name="group">
					<xsl:for-each select="//admin/options/singlegroups/group">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>			
					<xsl:for-each select="//admin/options/multigroups/group">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<strong>policy/action:</strong><input type="text" name="policy" />
				<br />
				<input type="submit" name="insertpermission" value="Insert Permission" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'removepermission'">			
			<form method="get" name="RemovePermission">
				<strong>Group Name: </strong> 
				<select name="group">
					<xsl:for-each select="//admin/options/resources/resource">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<strong>User: </strong>
				<select name="group">
					<xsl:for-each select="//admin/options/singlegroups/group">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>
					<xsl:for-each select="//admin/options/multigroups/group">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<strong>policy/action:</strong> <input type="text" name="policy" />
				<br />
				<input type="submit" name="removepermission" value="Remove Permission" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'insertaccount'">			
			<form method="get" name="InsertAccount">
				<input type="hidden" name="processaction" value="insertaccount" />			
				<strong>User: </strong> <input type="text" name="ident" />
				<br />
				<strong>Password: </strong> <input type="password" name="password" />
				<br />
				<strong>Description:</strong> <input type="text" name="description" />
				<br />
				<input type="submit" name="insertaccount" value="Insert Account" />
			</form>
		</xsl:if>			
		
		<xsl:if test="$action = 'removeaccount'">			
			<form method="get" name="RemoveAccount">
				<input type="hidden" name="processaction" value="removeaccount" />			
				<select name="ident">
					<xsl:for-each select="//admin/options/accounts/account">
							<xsl:element name="option">
								<xsl:attribute name="value">
									<xsl:value-of select="@val"/>
								</xsl:attribute>
								<xsl:value-of select="@name"/>
							</xsl:element>
					</xsl:for-each>
				</select>
				<input type="submit" name="removeaccount" value="Remove Account" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'insertcommunity'">			
			<form method="get" name="InsertCommunity">
				<input type="hidden" name="processaction" value="insertcommunity" />			
				<strong>Community Name (Domain Name): </strong> <input type="text" name="ident" />
				<br />
				<input type="submit" name="insertcommunity" value="Insert Community" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'removecommunity'">			
			<form method="get" name="RemoveCommunity">
				<input type="hidden" name="processaction" value="removecommunity" />
				<select name="ident">
					<xsl:for-each select="//admin/options/communities/community">
							<xsl:element name="option">
								<xsl:attribute name="value">
									<xsl:value-of select="@val"/>
								</xsl:attribute>
								<xsl:value-of select="@name"/>
							</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<input type="submit" name="removecommunity" value="Remove Community" />
			</form>
		</xsl:if>
		<xsl:if test="$action = 'changeofpassword'">			
			<form method="get" name="ChangeOfPassword">
				<input type="hidden" name="processaction" value="changeofpassword" />
				<strong>Current Password: </strong> <input type="password" name="currentpassword" /><br />
				<strong>New Password: </strong> <input type="password" name="newpassword" /><br />
				<strong>Verify Password: </strong> <input type="password" name="verifypassword" /><br />						
				<input type="submit" name="changepassword" value="Change Password" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'viewgroups'">			
			<strong>Groups: </strong>
			<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td>
						<strong>Group:</strong>
					</td>
					<td>
						<strong>Type:</strong>
					</td>
					<td>
						<strong>Description:</strong>
					</td>
				</tr>
				<xsl:for-each select="//admin/options/groups/group">
					<tr>
						<td>
							<xsl:value-of select="@val"/>
						</td>
						<td>
							MSSL
						</td>
						<td>
							<xsl:value-of select="@val"/>
						</td>
						<td>
							<xsl:value-of select="@desc"/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</xsl:if>
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
