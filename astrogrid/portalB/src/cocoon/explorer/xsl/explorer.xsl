<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/cocoon/explorer/xsl/Attic/explorer.xsl,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/26 14:15:10 $</cvs:author>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    | $Log: explorer.xsl,v $
    | Revision 1.1  2003/06/26 14:15:10  dave
    | Added explorer pages and actions to Cocoon
    |
    | </cvs:log>
    | 
    +-->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<!--+
	    | The template parameters, initially set from the request parameters.
		| Eventually, these will be set from the Cocoon action.
		+-->
	<xsl:param name="paste"/>
	<xsl:param name="action"/>
	<xsl:param name="confirm"/>
	<xsl:param name="page">explorer.003</xsl:param>

	<!--+
	    | Match the top level page element.
		+-->
	<xsl:template match="/page">
		<page>
			<menu>
				<!-- Add our menu items -->
				<link type="menu" selected="true">
					<display>Explorer</display>
					<href>
						<base><xsl:value-of select="$page"/></base>
					</href>
					<!-- Add the new-view item -->
					<link type="menu">
						<!-- If this action is selected -->
						<xsl:if test="$action = 'create-view'">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<display>New view</display>
						<href>
							<base><xsl:value-of select="$page"/></base>
							<param name="action">create-view</param>
						</href>
					</link>
					<!-- Add our current views -->
					<xsl:call-template name="menu-views"/>
				</link>
			</menu>
			<content>
				<!-- Check the page action -->
				<xsl:choose>
					<!-- Display the hello message -->
					<xsl:when test="$action = 'hello'">
						<b>Hello !!</b>
					</xsl:when>
					<!-- Display the create view form -->
					<xsl:when test="$action = 'create-view'">
						<xsl:call-template name="create-view"/>
					</xsl:when>
					<!-- Display our view tree -->
					<xsl:otherwise>
						<xsl:apply-templates select="view"/>
					</xsl:otherwise>
				</xsl:choose>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate our list of views for our menu.
	    +-->
	<xsl:template name="menu-views">
		<!-- Match each of our session views -->
		<xsl:for-each select="session/views/view">
			<!-- Add a menu item for each view -->
			<xsl:element name="link">
				<xsl:attribute name="type">menu</xsl:attribute>
				<!-- If this the current view -->
				<xsl:if test="/page/view/@ident = @ident">
					<xsl:attribute name="selected">true</xsl:attribute>
				</xsl:if>
				<display>
					<xsl:value-of select="@ident"/>
				</display>
				<href>
					<base>
						<xsl:value-of select="$page"/>
					</base>
					<param name="AST-VIEW">
						<xsl:value-of select="@ident"/>
					</param>
				</href>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<!--+
	    | Process our current view.
	    +-->
	<xsl:template match="page/view">
		<table border="1">
			<tr>
				<td>View ident</td>
				<td>
					<xsl:value-of select="/page/view/@ident"/>
				</td>
				<td align="right">
					<link type="close">
						<display>Close</display>
						<href>
							<base><xsl:value-of select="$page"/></base>
							<param name="action">delete-view</param>
							<param name="confirm">true</param>
							<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
						</href>
					</link>
				</td>
			</tr>
			<tr>
				<td>Service</td>
				<td colspan="2">
					<xsl:value-of select="/page/view/@service"/>
				</td>
			</tr>
			<tr>
				<td>Explorer</td>
				<td colspan="2">
					<xsl:value-of select="/page/view/@path"/>
				</td>
			</tr>
			<tr>
				<td>Current</td>
				<td colspan="2">
					<xsl:value-of select="/page/view/current/@path"/>
				</td>
			</tr>
			<tr>
				<td>Selected</td>
				<td colspan="2">
					<xsl:value-of select="/page/view/selected/@path"/>
				</td>
			</tr>
			<tr>
				<td>Action</td>
				<td colspan="2">
					<xsl:value-of select="/page/view/selected/@action"/>
				</td>
			</tr>
		</table>
		<!-- Check the page action -->
		<xsl:choose>
			<!-- Display the delete item form -->
			<xsl:when test="$action = 'create-folder'">
				<xsl:call-template name="create-folder"/>
			</xsl:when>

			<!-- Display the delete item form -->
			<xsl:when test="$action = 'delete-item'">
				<xsl:call-template name="delete-item"/>
			</xsl:when>
			<!-- Display the rename item form -->
			<xsl:when test="$action = 'rename-item'">
				<xsl:call-template name="rename-item"/>
			</xsl:when>
			<!-- Display our view tree -->
			<xsl:otherwise>
				<!-- Process the tree nodes -->
				<table border="1">
					<xsl:apply-templates select="/page/view/tree/node"/>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!--+
	    | Process our tree nodes.
	    +-->
	<xsl:template match="/page/view/tree//node">
		<tr>
			<td>
				<!-- Add the indentation -->
				<xsl:call-template name="node-indent"/>
				<!-- Add the node link -->
				<xsl:choose>
					<!-- If this is a folder -->
					<xsl:when test="@type = '1'">
						<link type="folder">
							<!-- If this matches the explorer path -->
							<xsl:if test="@path = /page/view/@path">
								<xsl:attribute name="selected">true</xsl:attribute>
							</xsl:if>
							<display>
								<xsl:value-of select="@name"/>
							</display>
							<href>
								<base><xsl:value-of select="$page"/></base>
								<param name="action">explorer-path</param>
								<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
								<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
							</href>
						</link>
					</xsl:when>
					<!-- If this is an item -->
					<xsl:when test="@type = '2'">
						<link type="item">
							<!-- If this matches the current path -->
							<xsl:if test="@path = /page/view/current/@path">
								<xsl:attribute name="selected">true</xsl:attribute>
							</xsl:if>
							<display>
								<xsl:value-of select="@name"/>
							</display>
							<href>
								<base><xsl:value-of select="$page"/></base>
								<param name="action">current-path</param>
								<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
								<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
							</href>
						</link>
					</xsl:when>
				</xsl:choose>
			</td>
<!--
			<td>
				<xsl:value-of select="@ident"/>
			</td>
			<td>
				<xsl:value-of select="@type"/>
			</td>
			<td>
				<xsl:value-of select="@level"/>
			</td>
-->
			<td>
				<xsl:call-template name="node-actions"/>
			</td>
		</tr>
		<!-- Process any child nodes -->
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Generate the indentation for a tree node.
	    +-->
	<xsl:template name="node-indent">
		<!-- For each parent node in the tree -->
		<xsl:for-each select="ancestor::node">
			<xsl:call-template name="node-parent"/>
		</xsl:for-each>
		<!-- For this node -->
		<xsl:choose>
			<!-- If this node is not the last in its group -->
			<xsl:when test="@more = 'true'">
				<xsl:text>.+-</xsl:text>
			</xsl:when>
			<!-- If this node is the last in its group -->
			<xsl:otherwise>
				<xsl:text>.+-</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!--+
	    | Generate the indentation for a parent tree node.
	    +-->
	<xsl:template name="node-parent">
		<xsl:choose>
			<!-- If the PARENT node has more siblings -->
			<xsl:when test="@more = 'true'">
				<xsl:text>.|..</xsl:text>
			</xsl:when>
			<!-- If the PARENT node is the last in its group -->
			<xsl:otherwise>
				<xsl:text>....</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!--+
	    | Generate the actions for a tree node.
	    +-->
	<xsl:template name="node-actions">
		<xsl:choose>
			<!-- If this is an item -->
			<xsl:when test="@type = '2'">
				<!-- Add the cut-item action -->
				<xsl:text> </xsl:text>
				<link type="action">
					<display>
						<xsl:text>Cut</xsl:text>
					</display>
					<href>
						<base><xsl:value-of select="$page"/></base>
						<param name="action">cut-item</param>
						<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
						<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
					</href>
				</link>
				<!-- Add the copy-item action -->
				<xsl:text> </xsl:text>
				<link type="action">
					<display>
						<xsl:text>Copy</xsl:text>
					</display>
					<href>
						<base><xsl:value-of select="$page"/></base>
						<param name="action">copy-item</param>
						<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
						<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
					</href>
				</link>

				<!-- Add the rename-item action -->
				<xsl:text> </xsl:text>
				<link type="action">
					<display>
						<xsl:text>Rename</xsl:text>
					</display>
					<href>
						<base><xsl:value-of select="$page"/></base>
						<param name="action">rename-item</param>
						<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
						<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
					</href>
				</link>

				<!-- Add the rename-item action -->
				<xsl:text> </xsl:text>
				<link type="action">
					<display>
						<xsl:text>Delete</xsl:text>
					</display>
					<href>
						<base><xsl:value-of select="$page"/></base>
						<param name="action">delete-item</param>
						<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
						<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
					</href>
				</link>
			</xsl:when>
			<!-- If this is a container -->
			<xsl:when test="@type = '1'">
				<!-- If this is below the top two levels -->
				<xsl:if test="@level > 1">
					<!-- If we have a cut item to paste -->
					<xsl:if test="/page/view/selected/@action = 'cut-item'">
						<!-- Add the paste-item action -->
						<xsl:text> </xsl:text>
						<link type="action">
							<display>
								<xsl:text>Paste</xsl:text>
							</display>
							<href>
								<base><xsl:value-of select="$page"/></base>
								<param name="action">paste-item</param>
								<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
								<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
							</href>
						</link>
					</xsl:if>
					<!-- If we have a copy item to paste -->
					<xsl:if test="/page/view/selected/@action = 'copy-item'">
						<!-- Add the paste-item action -->
						<xsl:text> </xsl:text>
						<link type="action">
							<display>
								<xsl:text>Paste</xsl:text>
							</display>
							<href>
								<base><xsl:value-of select="$page"/></base>
								<param name="action">paste-item</param>
								<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
								<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
							</href>
						</link>
					</xsl:if>
					<!-- Add the new-folder action -->
					<xsl:text> </xsl:text>
					<link type="action">
						<display>
							<xsl:text>New folder</xsl:text>
						</display>
						<href>
							<base><xsl:value-of select="$page"/></base>
							<param name="action">create-folder</param>
							<param name="AST-VIEW"><xsl:value-of select="/page/view/@ident"/></param>
							<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
						</href>
					</link>
				</xsl:if>
				<!-- If this is below the top three levels -->
				<xsl:if test="@level > 2">
					<xsl:text> </xsl:text>
					<xsl:text>[Delete]</xsl:text>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!--+
	    | Generate the create view form.
	    +-->
	<xsl:template name="create-view">
		<form method="get">
			<input type="hidden" name="action"  value="create-view"/>
			<input type="hidden" name="confirm" value="true"/>
			<table border="1">
				<tr>
					<td>
						Path :
					</td>
					<td>
						<select name="AST-PATH">
							<xsl:for-each select="options/paths/path">
								<xsl:element name="option">
									<xsl:attribute name="value">
										<xsl:value-of select="@path"/>
									</xsl:attribute>
									<xsl:value-of select="@name"/>
								</xsl:element>
							</xsl:for-each>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						Service :
					</td>
					<td>
						<select name="AST-SERVICE">
							<xsl:for-each select="options/services/service">
								<xsl:element name="option">
									<xsl:attribute name="value">
										<xsl:value-of select="@url"/>
									</xsl:attribute>
									<xsl:value-of select="@name"/>
								</xsl:element>
							</xsl:for-each>
						</select>
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<input type="submit" value="Create"/>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>

	<!--+
	    | Generate the create folder form.
	    +-->
	<xsl:template name="create-folder">
		<form method="get">
			<input type="hidden" name="AST-VIEW">
				<xsl:attribute name="value">
					<xsl:value-of select="/page/view/@ident"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="AST-PATH">
				<xsl:attribute name="value">
					<xsl:value-of select="/page/view/current/@path"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="action"   value="create-folder"/>
			<input type="hidden" name="confirm"  value="true"/>
			<table border="1">
				<tr>
					<td>
						Name
					</td>
					<td>
						<input type="text" name="AST-NAME" value="New folder"/>
					</td>
				</tr>
				<tr>
					<td>
						Path
					</td>
					<td>
						<xsl:value-of select="/page/view/current/@path"/>
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<input type="submit" value="Create"/>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>

	<!--+
	    | Generate the rename item form.
	    +-->
	<xsl:template name="rename-item">
		<form method="get">
			<input type="hidden" name="AST-VIEW">
				<xsl:attribute name="value">
					<xsl:value-of select="/page/view/@ident"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="AST-PATH">
				<xsl:attribute name="value">
					<xsl:value-of select="/page/view/selected/@path"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="action"   value="rename-item"/>
			<input type="hidden" name="confirm"  value="true"/>
			<table border="1">
				<tr>
					<td>
						Name
					</td>
					<td>
						<input type="text" name="AST-NAME">
							<xsl:attribute name="value">
								<xsl:value-of select="/page/view/selected/@name"/>
							</xsl:attribute>
						</input>
					</td>
				</tr>
				<tr>
					<td>
						Path
					</td>
					<td>
						<xsl:value-of select="/page/view/selected/@path"/>
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<input type="submit" value="Rename"/>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>

	<!--+
	    | Generate the delete item form.
	    +-->
	<xsl:template name="delete-item">
		<form method="get">
			<input type="hidden" name="AST-VIEW">
				<xsl:attribute name="value">
					<xsl:value-of select="/page/view/@ident"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="AST-PATH">
				<xsl:attribute name="value">
					<xsl:value-of select="/page/view/selected/@path"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="action"   value="delete-item"/>
			<input type="hidden" name="confirm"  value="true"/>
			<table border="1">
				<tr>
					<td>
						Name
					</td>
					<td>
						<xsl:value-of select="/page/view/selected/@name"/>
					</td>
				</tr>
				<tr>
					<td>
						Path
					</td>
					<td>
						<xsl:value-of select="/page/view/selected/@path"/>
					</td>
				</tr>
				<tr>
					<td>
					</td>
					<td>
						<input type="submit" value="Delete"/>
					</td>
				</tr>
			</table>
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
