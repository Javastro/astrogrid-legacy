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
			  <option value="insertresource">Insert Resource</option>
			  <option value="removeresource">Remove Resource</option>
			  <option value="changeresourceowner">Change Resource Owner</option>
			  <option value="insertgroup">Insert Group</option>
			  <option value="removegroup">Remove Group</option>
			  <option value="changegroupowner">Change Group Owner</option>
			  <option value="insertmember">Insert Member</option>
			  <option value="removemember">Remove Member</option>
			  <option value="insertpermission">Insert Permission</option>
			  <option value="removepermission">Remove Permission</option>
			  <option value="insertcommunity">Insert Community</option>
			  <option value="removecommunity">Remove Community</option>
			  <option value="changeofpassword">Change Of Password</option>
			  <option value="insertaccount">Insert Account</option>			  
			  <option value="removeaccount">Remove Account</option>
			  <option value="viewgroups">View Groups</option>
			</select>
			<input type="submit" name="admintaskselect" value="Go" />
		</form>
		<xsl:if test="$action = 'insertresource'">
			<form method="get" name="AdminInsertResource">
				<strong>Resource Name: </strong> <input type="text" name="resourcername" />
				<br />
				<strong>Description: </strong> <input type="text" name="description" />
				<br />
				*<strong><i>You will become owner of this resource, go to "ChangeResourceOwner" for assiging administration to a user group or a single user</i></strong>
				<br />
				<input type="submit" name="insertresource" value="Insert Resource" />
			</form>
		</xsl:if>
		<xsl:if test="$action = 'removeresource'">
			<form method="get" name="AdminRemoveResource">
				<strong>Resources you can administrate: </strong>
				<select name="resource">
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
		<xsl:if test="$action = 'changeresourceowner'">
			<form method="get" name="AdminChangeResourceOwner">
				<strong>Resources you can administrate: </strong>
				<select name="resource">
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
				<strong>Change Owner to: </strong>
				<select name="group">
					<xsl:for-each select="//admin/options/singlegroups/group">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<input type="submit" name="changeresourceowner" value="Changer Owner" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'insertgroup'">			
			<form method="get" name="AdminInsertGroup">
				*<i>You can only insert MULTI user groups, single type's are automatically created with inserting an Account</i>
				<br />
				<strong>Group Name: </strong> <input type="text" name="groupname" />
				<br />
				<strong>Community: </strong> <input type="text" name="community" />
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
				<strong>Group Name: </strong> 
				<select name="group">
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
				<input type="submit" name="removegroup" value="Remove Group" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'changegroupowner'">			
			<form method="get" name="ChangeGroupOwner">
				*<i>You can only remove MULTI user groups ownership.</i>
				<br />
				<strong>Group Name: </strong> 
				<select name="group">
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
				<i>User must belong to this local community.</i>
				<select name="group">
					<xsl:for-each select="//admin/options/singlelocalgroup/group">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
							</xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:for-each>
				</select>
				<br />
				<input type="submit" name="removegroup" value="Remove Group" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'insertmember'">			
			<form method="get" name="InsertMember">
				<strong>Group Name: </strong> 
				<select name="group">
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
				</select>
				<br />
				<input type="submit" name="insertmermber" value="Insert Member" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'removemember'">			
			<form method="get" name="RemoveMember">
				<strong>Group Name: </strong> 
				<select name="group">
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
				<strong>User: </strong> <input type="text" name="name" />
				<br />
				<strong>Password: </strong> <input type="text" name="password" />
				<br />
				<strong>Community: </strong> <input type="text" name="community" />
				<br />
				<input type="submit" name="insertaccount" value="Insert Account" />
			</form>
		</xsl:if>			
		
		<xsl:if test="$action = 'removeaccount'">			
			<form method="get" name="RemoveAccount">
				<strong>User: </strong> <input type="text" name="name" />
				<br />
				<strong>Community: </strong> <input type="text" name="community" />
				<br />
				<input type="submit" name="removeaccount" value="Remove Account" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'insertcommunity'">			
			<form method="get" name="InsertCommunity">
				<strong>Community Name: </strong> <input type="text" name="name" />
				<br />
				<strong>Admin Service URL: </strong> <input type="text" name="url" />
				<br />
				<input type="submit" name="insertcommunity" value="Insert Community" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'removecommunity'">			
			<form method="get" name="RemoveCommunity">
				<strong>Community Name: </strong> <input type="text" name="name" />
				<br />
				<input type="submit" name="removecommunity" value="Remove Community" />
			</form>
		</xsl:if>			
		<xsl:if test="$action = 'changeofpassword'">			
			<form method="get" name="ChangeOfPassword">
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
						<strong>Name:</strong>
					</td>
					<td>
						<strong>Community:</strong>
					</td>
					<td>
						<strong>Type:</strong>
					</td>
					<td>
						<strong>Description:</strong>
					</td>
				</tr>
				<xsl:for-each select="//admin/options/multigroups/group">
					<tr>
						<td>
							<xsl:value-of select="@val"/>
						</td>
						<td>
							MSSL
						</td>
						<td>
							MULTI
						</td>
						<td>
							info
						</td>
					</tr>
				</xsl:for-each>
				<xsl:for-each select="//admin/options/singlegroups/group">
					<tr>
						<td>
							<xsl:value-of select="@val"/>
						</td>
						<td>
							MSSL
						</td>
						<td>
							SINGLE
						</td>
						<td>
							info
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
