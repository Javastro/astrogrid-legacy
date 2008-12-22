<?xml version="1.0"?>
<!-- 
Splits xmlized javadoc into separate files for each module, summarizing the service classes in each module.
Used to provide in-program api documentation within the workbench implementation.
-->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:redirect="http://xml.apache.org/xalan/redirect"
  extension-element-prefixes="redirect"
  >

  <xsl:output omit-xml-declaration="yes" indent="yes"/>
  <xsl:preserve-space elements="*" />
  <xsl:param name="outputDir"/>
<!-- compute a list of package names - will reuse this to iterate through later.. -->
<xsl:variable name="packages" select="/jel/jelclass[
	not(@package=preceding-sibling::jelclass/@package)
	]/@package[not (.='org.astrogrid.acr' or .='org.astrogrid.acr.opt' or .='org.astrogrid.util')]" />

<!-- list of service interfaces -->
<xsl:variable name="services" select="/jel/jelclass[@interface='true' 
		and comment/attribute[@name='@service']        
		]"/>
		
<!-- keep deprecated services.  		and not ( comment/attribute[@name='@deprecated']) -->


<!-- generate package descriptions -->
<xsl:template match="jel">
    <xsl:for-each select="$packages">
        <xsl:sort />
        <xsl:variable name="curr" select="." />
        <xsl:variable name="packageName">
            <xsl:call-template name="substring-after-last">
                <xsl:with-param name="input" select="." />
                <xsl:with-param name="marker" select="'.'" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:message>
            <xsl:value-of select="." />
        </xsl:message>
        <redirect:write
            file="{concat($outputDir,$packageName,'-descriptors.xml')}">
            <xsl:apply-templates select="$services[@package=$curr]">
                <xsl:sort select="@type" />
            </xsl:apply-templates>
        </redirect:write>
    </xsl:for-each>
</xsl:template>


<xsl:template match="jelclass">
	<xsl:variable name="id" select="comment/attribute[@name='@service']/description" />
    	<xsl:variable name="name">
 	<xsl:call-template name="substring-after-last">
 		<xsl:with-param name="input" select="$id" />
 		<xsl:with-param name="marker" select="'.'" />
 	</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="classDescription">
		<xsl:call-template name="fmtDescription">
			<xsl:with-param name="input" select="." />
		</xsl:call-template>
	</xsl:variable>
  <component interface-class="{@fulltype}"  name="{$name}" description="{$classDescription}">
	<xsl:if test="comment/attribute[@name='@exclude']">
                <xsl:attribute name="excluded"><xsl:text>true</xsl:text></xsl:attribute>
    </xsl:if>
    <xsl:variable name="inheritanceTypes" select="implements/interface/@fulltype" />
	<xsl:variable name="inheritanceStack" select=". | /jel/jelclass[@fulltype =$inheritanceTypes]" />
	<xsl:for-each select="$inheritanceStack/methods/method[@visibility='public' and not (contains(@name, 'Listener')) ]	">
	<xsl:variable name="methodDescription">
		<xsl:call-template name="fmtDescription">
			<xsl:with-param name="input" select="." />
		</xsl:call-template>
	</xsl:variable>
		<method name="{@name}" description="{$methodDescription}">
            <xsl:if test="comment/attribute[@name='@exclude']">
                <xsl:attribute name="excluded"><xsl:text>true</xsl:text></xsl:attribute>
            </xsl:if>
			<xsl:variable name="uitype">
				<xsl:call-template name="convert-type">
					<xsl:with-param name="p" select="." />		
		   		</xsl:call-template>
			</xsl:variable>
			<xsl:variable name="fulltype">
				<xsl:choose>
				<xsl:when test="@fulltype ='void'">java.lang.Void</xsl:when>
				<xsl:otherwise><xsl:value-of select="@fulltype"/></xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<return type="{$fulltype}" description="{@returncomment}" uitype="{$uitype}"/>		
	     		<xsl:for-each select="params/param">
				<xsl:variable name="uitype">
					<xsl:call-template name="convert-type">
						<xsl:with-param name="p" select="." />		
			   		</xsl:call-template>
				</xsl:variable>
				<param type="{@fulltype}" name="{@name}" description="{@comment}" uitype="{$uitype}"/>
			</xsl:for-each>		
		</method>
	</xsl:for-each>
   </component>
</xsl:template>


<!-- helper methods -->

<xsl:template name="convert-type">
<xsl:param name="p" />
<!-- 
<xsl:if test="contains($p/@fulltype,'[]')">
	<xsl:text>Array-of:</xsl:text>
</xsl:if>
 -->
<xsl:choose>
<!-- 
  <xsl:when test="contains($p/@type,'Information') or contains($p/@type,'Bean')">
  	key-value-map, See <xsl:value-of select='$p/@type'/> for structure.
  </xsl:when>
 -->
  <!-- 
  <xsl:when test="$p/@type = 'Map'">
  	<xsl:text>Key/Value-Map</xsl:text>
  </xsl:when>
  <xsl:when test="$p/@type = 'String'">
  	<xsl:text>string</xsl:text>
  </xsl:when>
   -->
  <xsl:when test="$p/@type = 'Document'">
  	<xsl:text>XML</xsl:text>
  </xsl:when>
  <!--
  <xsl:when test="$p/@type = 'URI'">
  	<xsl:text>URI-string</xsl:text>
  </xsl:when>
  <xsl:when test="$p/@type = 'void'">
  	<xsl:text>nothing</xsl:text>
  </xsl:when>
   -->
  <xsl:otherwise>
    <xsl:value-of select="$p/@type" />
  </xsl:otherwise>  
</xsl:choose>
<xsl:if test="contains($p/@fulltype,'[]')">
    <xsl:text>[]</xsl:text>
</xsl:if>
</xsl:template>

<xsl:template name="substring-after-last">
<xsl:param name="input" />
<xsl:param name="marker" />

<xsl:choose>
  <xsl:when test="contains($input,$marker)">
    <xsl:call-template name="substring-after-last">
      <xsl:with-param name="input" 
          select="substring-after($input,$marker)" />
      <xsl:with-param name="marker" select="$marker" />
    </xsl:call-template>
  </xsl:when>
  <xsl:otherwise>
   <xsl:value-of select="$input" />
  </xsl:otherwise>
 </xsl:choose>

</xsl:template>

<xsl:template name="fmtDescription">
    <!-- output tags from this template are all escaped, 
    to combine with the already-escaped tags from comment/description,
    this is also necssary because the result is going to be stored in
    an attribute in the result document. This, in turn, is necessary
    so that hivemind doesn't need to parse the documentation XHTML tags,
    but just slurps in the whole unparsed. 
    
    NB: following whitespace is all carefully arranged
    to produce nice plaintext output too, once html tags are stripped-->
	<xsl:param name="input" />
	<xsl:if test="comment/attribute[@name='@deprecated']">
		<xsl:text>&lt;b&gt;Deprecated: &lt;/b&gt;</xsl:text> 
        <xsl:value-of select="comment/attribute[@name='@deprecated']/description" />
        <xsl:text>&lt;p /&gt;
</xsl:text>
	</xsl:if>
		<xsl:value-of select="comment/description" /> 
        <xsl:if test="comment/attribute[@name='@equivalence' or @name='@note' or @name='@warning' or @name='@xmlrpc' or @name='@see']">
        <xsl:text>&lt;dl&gt;</xsl:text>
            <xsl:for-each select="comment/attribute">
                <xsl:sort select="@name" />                
                <xsl:choose>
                    <xsl:when test="@name='@equivalence'">
                        <xsl:text>
&lt;dt&gt;Equivalent To:&lt;/dt&gt;&lt;dd&gt;&lt;tt&gt;
   </xsl:text><xsl:value-of select="description" />
                        <xsl:text>&lt;/tt&gt;&lt;/dd&gt;</xsl:text>
                    </xsl:when>
                    <xsl:when test="@name='@note'">
                        <xsl:text>
&lt;dt&gt;Note:&lt;/dt&gt;&lt;dd&gt;
   </xsl:text><xsl:value-of select="description" />
                        <xsl:text>&lt;/dd&gt;</xsl:text>                  
                    </xsl:when>
                    <xsl:when test="@name='@warning'">
                        <xsl:text>
&lt;dt&gt;Warning:&lt;/dt&gt;&lt;dd&gt;
   </xsl:text><xsl:value-of select="description" />
                        <xsl:text>&lt;/dd&gt;</xsl:text>  
                    </xsl:when>       
                    <xsl:when test="@name='@xmlrpc'">
                        <xsl:text>
&lt;dt&gt;XMLRPC:&lt;/dt&gt;&lt;dd&gt;
   </xsl:text><xsl:value-of select="description" />
                        <xsl:text>&lt;/dd&gt;</xsl:text>                     
                    </xsl:when>                                                                  
                </xsl:choose>
            </xsl:for-each>
            <!--  finally - collate all 'see' together. -->
            <xsl:if test="comment/attribute[@name='@see']">
            <xsl:text>
&lt;dt&gt;See:&lt;/dt&gt;&lt;dd&gt;
   </xsl:text><xsl:for-each select="comment/attribute[@name='@see']">
                <xsl:value-of select="description"/><xsl:text>, </xsl:text>            
            </xsl:for-each>
            <xsl:text>&lt;/dd&gt;</xsl:text>
            </xsl:if>
        <xsl:text>&lt;/dl&gt;</xsl:text>
        </xsl:if>
    
</xsl:template>





</xsl:stylesheet>
