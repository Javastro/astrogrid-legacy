<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:strip-space elements="*" />

  <xsl:template match="javadoc">
<document>
<xsl:comment>Warning - this is a machine-generated document. Edits to this document will be lost</xsl:comment>
  <properties>
    <title>JEScript Object Model</title>
    <author email="Noel.Winstanley@manchester.ac.uk">Noel Winstanley</author>
  </properties>
  <head>
<script type="text/javascript">
cc=0
function show(id)
{
x = document.getElementById(id)
if (x.cc == null || x.cc ==0) 
{
x.cc=1
x.style.display='block'
}
else
{
x.cc=0
x.style.display='none'
}
}
</script>		
</head>
  <body>

<section name="What's available?">
	<p> From groovy scripts it is possible to import any class available on the 
		classpath, and then instantiate and use it in the normal manner. </p>
      <p>In addition, the JES Server provides an environment for scripts to execute  that
      is configured with some pre-initialized <strong>system objects</strong> - we call scripts for this environment JEScripts.
	   The system  objects  are preconfigured objects
		that are always available under fixed names. They can be used togain information and interfact with the current state of the
      workflow, the Jes Server, and other Astrogrid components.</p>
	<p>
		<em>link to list of jars bundled with JES - so user can see what's 
			available</em>
	</p>
	<subsection name="System Objects">
	<p> The following objects are available to 
		JEScripts and script expressions: </p>
	<dl>

		<dt><code>astrogrid</code></dt>
		<dd>An object that represents the entire Astrogrid system. 
			Implemented by class <xsl:call-template name="maybe-link"><xsl:with-param name="type">org.astrogrid.scripting.Toolbox</xsl:with-param></xsl:call-template>.
			Provides
			<ul>
				<li>configured delegates to access other astrogrid services (vospace, registry, cea, jes)</li>
				<li>libraries for constructing workflows</li>
				<li>libraries for parsing and constructing tables</li>
				<li>libraries for input / output</li>	
				<li>access to system config</li>
				</ul>
			</dd>
			
		<dt><code>jes</code></dt>
		<dd>An object that represents the JES server that is executing the current workflow. 
			Implemented by class          <xsl:call-template name="maybe-link"><xsl:with-param name="type">org.astrogrid.jes.jobscheduler.impl.groovy.JesInterface</xsl:with-param></xsl:call-template>.
			Provides 
			<ul>
				<li>logging</li>
				<li>access to the document model of the current workflow</li>
				<li>execution control</li>
			</ul>			
		</dd>
		
		<dt><code>account</code></dt>
		<dd>object that represents the identity of the user under which this workflow is being executed. An instance of
			<xsl:call-template name="maybe-link"><xsl:with-param name="type">org.astrogrid.community.beans.v1.Account</xsl:with-param></xsl:call-template></dd>
		
		<dt><code>user</code></dt>
		<dd>another (deprecated) representation of the current user. An instance of
			<xsl:call-template name="maybe-link"><xsl:with-param name="type">org.astrogrid.community.User</xsl:with-param></xsl:call-template></dd>
		
	
		<dt><code>userIvorn</code></dt>
		<dd>Ivorn representing the current user. An instance of 
			<xsl:call-template name="maybe-link"><xsl:with-param name="type">org.astrogrid.store.Ivorn</xsl:with-param></xsl:call-template></dd>
		
		<dt><code>homeIvorn</code></dt>
		<dd>Ivorn pointing to the homespace of the current user. An instance of 
			<xsl:call-template name="maybe-link"><xsl:with-param name="type">org.astrogrid.store.Ivorn</xsl:with-param></xsl:call-template></dd>
	
	</dl>
	</subsection>
</section>   


   <section name="Toolbox" >
        <xsl:call-template name="toc">
                <xsl:with-param name="class" select="class[@public='true' and not(contains(@name,'.') or contains(@name,'VoSpace')) and  (starts-with(@qualified-name,'org.astrogrid.scripting') or @name='Config')]" />
        </xsl:call-template>
        <p>The main class for interacting with the astrogrid is <a href="#org.astrogrid.scripting.Toolbox">Toolbox</a>.
        An instance of this class, named <code>astrogrid</code> is available in the JEScript environment. Other subsidiary
        helper objects and delegates are accessible by calling the methods on <code>Toolbox</code>
        </p>
        <xsl:apply-templates select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.scripting') and not(contains(@name,'.') or contains(@name,'VoSpace'))]"/>
        <xsl:apply-templates select="class[@public='true' and @name='Config']"/>
   </section>



   <section name="VoSpace">
        <xsl:call-template name="toc">
                <xsl:with-param name="class" select="class[@public='true' and (starts-with(@qualified-name,'org.astrogrid.store') or starts-with(@qualified-name,'org.astrogrid.applications.parameter.protocol') or contains(@name,'VoSpace'))]" />
        </xsl:call-template>
     <p>There's three interfaces for interacting with VoSpace - the vospace client, the tree, and external values</p>
       <xsl:apply-templates select="class[@public='true' and (starts-with(@qualified-name,'org.astrogrid.store') or starts-with(@qualified-name,'org.astrogrid.applications.parameter.protocol') or contains(@name,'VoSpace'))]"/>
   </section>

   <section name="JES Server" >
        <xsl:call-template name="toc">
                <xsl:with-param name="class" select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.jes')]" />
        </xsl:call-template>
        <p>The <code>JesInterface</code> class gives access into some of the internals of the current JES server. An
        instance of this class named <code>jes</code> is available in the JEScript environment. The <code>JesInterface</code>
        class in turn extends <code>WorkflowLogger</code>, and so inherits it's methods too.
        </p>
        <xsl:apply-templates select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.jes')]"/>
   </section>

   <section name="Workflow Delegate">
        <xsl:call-template name="toc">
                <xsl:with-param name="class" select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.portal.workflow')]" />
        </xsl:call-template>
        <p>These classes provide a high-level interface to the Astrogrid workflow system. They provide
        methods for building workflows, loading and saving to VOSpace, querying registry for cea applications,
        and submitting workflows to JES for execution.
        </p>
       <xsl:apply-templates select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.portal.workflow')]"/>
   </section>

   <section name="Registry Delegate">
        <xsl:call-template name="toc">
                <xsl:with-param name="class" select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.registry')]" />
        </xsl:call-template>
        <p>Delegates to query and adminsister a registry</p>
          <xsl:apply-templates select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.registry')]"/>
   </section>


 <section name="CEA Delegate">
        <xsl:call-template name="toc">
                <xsl:with-param name="class" select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.applications.delegate')]" />
        </xsl:call-template>
        <p>These classes provide a low-level interface to interact with CEA servers.
        </p>
          <xsl:apply-templates select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.applications.delegate')]"/>
   </section>

	<section name="User Objects">
		<xsl:call-template name="toc">
			<xsl:with-param name="class" select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.community')]" />
			</xsl:call-template>
			<p>Representations of user and account details</p>
			<xsl:apply-templates select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.community')]">
				<xsl:with-param name="concise" select="true()"/>
				</xsl:apply-templates>
		</section>
   <section name="Workflow Document Model">
        <xsl:call-template name="toc">
                <xsl:with-param name="class" select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.workflow.beans')]" />
        </xsl:call-template>
        <p>Workflow Documents are represented using the following objects</p>
          <xsl:apply-templates select="class[@public='true' and starts-with(@qualified-name,'org.astrogrid.workflow.beans')]">
                <xsl:with-param name="concise" select="true()" />
          </xsl:apply-templates>
   </section>
  </body>
</document>
  </xsl:template>

  <xsl:template match="class">
      <xsl:param name="concise" select="false()" />
      <a name="{@qualified-name}" />
    <subsection name="{@name}"><p>
      <xsl:for-each select="super-class[not(@name='java.lang.Object')]">	
		<xsl:choose>
			<xsl:when test="position() = 1">Extends&#160;</xsl:when>
			<xsl:otherwise>,&#160;</xsl:otherwise>
		</xsl:choose>
			<xsl:call-template name="maybe-link"><xsl:with-param name="type" select="@name"/></xsl:call-template>
      </xsl:for-each>
	  </p>
      <xsl:choose>
      <xsl:when test="tag[@name='@script-doc']">
        <p><xsl:copy-of select="tag[@name='@script-doc']/text" /><span style="color:blue;cursor:hand;" onclick="show('{generate-id()}')"> more..</span></p>
      </xsl:when>
      <xsl:otherwise>
        <p><xsl:copy-of select="comment"/><span style="color:blue;cursor:hand;" onclick="show('{generate-id()}')"> more..</span></p>
      </xsl:otherwise>
      </xsl:choose>	 
	  <div style="display:none" id="{generate-id()}">
      <table>
        <xsl:apply-templates
         select="method[@public='true' and not (overridden-class='java.lang.Object') and not (tag[@name='@script-doc-omit']) ]">
                <xsl:with-param name="concise" select="$concise" />
        </xsl:apply-templates>
      </table>
	  </div>
    </subsection>
  </xsl:template>

  <xsl:template match="method">
        <xsl:param name="concise" select="false()" />
    <tr><td><code><nobr>
         <xsl:call-template name="maybe-link">
          <xsl:with-param name="type" select="@return-type" />
         </xsl:call-template>
        </nobr><nobr>
    <xsl:text>&#160;</xsl:text>
    <strong><xsl:value-of select="@name" /></strong>
    <xsl:choose>
      <xsl:when test="parameter">
        <xsl:text>(</xsl:text>
        <xsl:for-each select="parameter">
          <xsl:if test="position() != 1">,&#160;</xsl:if>
          <xsl:call-template name="maybe-link">
                <xsl:with-param name="type" select="@type" />
          </xsl:call-template>
          <xsl:text>&#160;</xsl:text>
          <xsl:value-of select="@name" />
        </xsl:for-each>
        <xsl:text>)</xsl:text>
      </xsl:when>
      <xsl:otherwise>()</xsl:otherwise>
    </xsl:choose>
    </nobr></code>
    </td></tr>

    <xsl:if test="$concise = false()" >
    <tr><td><p>
    <xsl:choose>
        <xsl:when test="tag[@name='@script-doc']">
                <xsl:value-of select="tag[@name='@script-doc']/text" />
        </xsl:when>
        <xsl:otherwise>
                <xsl:value-of select="normalize-space(comment)" />
        </xsl:otherwise>
    </xsl:choose>
        </p>
    <xsl:if test="tag[@name='@param' and string-length(normalize-space(text)) > 4]">
        <p><i>Params</i><ul>
        <xsl:for-each select="tag[@name='@param']">
                <li><code><xsl:value-of select="substring-before(normalize-space(text),' ')" /></code>
                <xsl:text>&#160;</xsl:text>
                <xsl:value-of select="substring-after(normalize-space(text),' ')" /></li>
        </xsl:for-each>
        </ul></p>
    </xsl:if>

    <xsl:if test="tag[@name='@return' and string-length(normalize-space(text)) > 2]">
        <p><i>Returns</i>&#160;<xsl:value-of select="normalize-space(tag[@name='@return']/text)"/></p>
    </xsl:if>

    <xsl:if test="tag[@name='@throws']">
        <p><i>Throws</i><ul>
        <xsl:for-each select="tag[@name='@throws']">
                <li><xsl:value-of select="normalize-space(text)" /></li>
        </xsl:for-each>
        </ul></p>
    </xsl:if>

    </td></tr>
    </xsl:if>
  </xsl:template>

  <xsl:template name="maybe-link">
        <xsl:param name="type" />
        <xsl:choose>
                <xsl:when test="starts-with($type,'org.astrogrid')"><a href="#{$type}"><xsl:value-of select="$type" /></a></xsl:when>
                <xsl:otherwise><xsl:value-of select="$type" /></xsl:otherwise>
        </xsl:choose>
  </xsl:template>


   <xsl:template name="toc">
        <xsl:param name="class" />
     <table>
       <xsl:for-each select="$class">
        <xsl:sort select="@qualified-name" />
        <tr>
                <td><xsl:call-template name="maybe-link"><xsl:with-param name="type" select="@qualified-name"/></xsl:call-template></td>
                <xsl:choose>
                  <xsl:when test="tag[@name='@script-summary']">
                        <td><xsl:value-of select="normalize-space(tag[@name='@script-summary']/text)" /></td>
                  </xsl:when>
                  <xsl:otherwise>
                        <td><xsl:value-of select="normalize-space(summary)" /></td>
                  </xsl:otherwise>
                </xsl:choose>
        </tr>
       </xsl:for-each>
     </table>
   </xsl:template>

</xsl:stylesheet>