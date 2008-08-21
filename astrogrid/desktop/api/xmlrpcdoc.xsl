<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  >

  <xsl:preserve-space elements="*" />

<!-- compute a list of package names - will reuse this to iterate through later.. -->
<xsl:variable name="packages" select="/jel/jelclass[
	not(@package=preceding-sibling::jelclass/@package)
	]/@package[not (.='org.astrogrid.acr' or .='org.astrogrid.acr.opt' or .='org.astrogrid.acr.builtin')]" />

<!-- list of service interfaces -->
<xsl:variable name="services" select="/jel/jelclass[@interface='true' 
		and comment/attribute[@name='@service']
		]"/>
		
<!-- list of bean classes - various heuristics should capture most
	- any class ending in 'Information' or 'Bean'
	- any class in package 'org.astrogrid.acr.ivoa.resource', 
	- or which extends or implements a class in that package
, bean classes that don't match these rules need to be tagged with '@bean'-->
<xsl:variable name="beans" 
	select="/jel/jelclass[contains(@type,'Information') 
		or contains(@type,'Bean')
		or comment/attribute[@name='@bean']
		or contains(@fulltype,'org.astrogrid.acr.ivoa.resource')
		or contains(implements/interface/@fulltype,'org.astrogrid.acr.ivoa.resource')
		or contains(@superclassfulltype,'org.astrogrid.acr.ivoa.resource')
		]" />


<xsl:template match="jel">
<document>
<properties>
	<author email="noel.winstanley@manchester.ac.uk">Noel Winstanley</author>
	<title>Astro Runtime - XML-RPC Interface</title>
</properties>
<body>
<section name="Astro Runtime XML-RPC Interface"><p>
This document lists the functions available on the Astro Runtime via XMLRPC. It presents the 
methods in an abridged, language-independent form. This document is generated from the 
<a href="apidocs/index.html">AR javadoc</a> - refer there for full details.</p>

<subsection name="Service List">
<p>
In the AR, related functions are grouped into <i>Services</i>. Related services are in turn grouped into 
<i>Modules</i>. This section summarizes the available modules and services.
</p>
<ul>     
    <xsl:for-each select="$packages">
    	<xsl:sort />
       <xsl:variable name="curr" select="." />
       <xsl:variable name="module">
       	<xsl:call-template name="substring-after-last">
 		<xsl:with-param name="input" select="." />
 		<xsl:with-param name="marker" select="'.'" />
 	  </xsl:call-template>
	</xsl:variable>
    	<li>Module: <a href="#{$module}"><tt><xsl:value-of select="$module"/></tt></a>
 	
         <ul>
	   <xsl:for-each select="$services[@package=$curr]">
		<xsl:sort select="@type" />
		<xsl:variable name="service" select="comment/attribute[@name='@service']/description"/>
		<li>
		<a href="#{$service}"><tt><xsl:value-of select="$service"/></tt></a>
		   - <xsl:choose>
		  	<xsl:when test="comment/attribute[@name='@deprecated']">
		  		<xsl:text>Deprecated: </xsl:text>
		  		<xsl:value-of select="comment/attribute[@name='@deprecated']" />
		  	</xsl:when>
		   	<xsl:when test="contains(comment/description,'&#xA;')">
   				<xsl:value-of select="substring-before(comment/description,'&#xA;')" disable-output-escaping="yes" />			
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="comment/description" disable-output-escaping="yes"/>
			</xsl:otherwise>
		     </xsl:choose>		
		</li>
	    </xsl:for-each>
	 </ul>
       </li>
    </xsl:for-each>
</ul>

</subsection>

<subsection name="Map Structures">
<p>
Some AR functions return structured data - which are represented in XML-RPC as <i>maps</i>. Different 
programming languages have differing terms for a map - it may be known as a dictionary / dict / hash / associative array.
All refer to the same thing - a datastructure that provides mapping between <i>keys</i> and
<i>values</i>
This section describes the different kinds of map structure used by the AR.
<ul>
	<xsl:for-each select="$beans">
		<xsl:sort select="@type" />
		<li><a href="#{@type}"><tt><xsl:value-of select="@type"/></tt></a> - 
		      <xsl:choose>
		  	<xsl:when test="comment/attribute[@name='@deprecated']">
		  		<xsl:text>Deprecated: </xsl:text>
		  		<xsl:value-of select="comment/attribute[@name='@deprecated']" />
		  	</xsl:when>		      
		   	<xsl:when test="contains(comment/description,'&#xA;')">
   				<xsl:value-of select="substring-before(comment/description,'&#xA;')" disable-output-escaping="yes" />			
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="comment/description" disable-output-escaping="yes"/>
			</xsl:otherwise>
		     </xsl:choose>		
		</li>
	</xsl:for-each>
</ul>

</p>
</subsection>

</section>

<!-- generate package descriptions -->

  <xsl:for-each select="$packages">
    	<xsl:sort />
       <xsl:variable name="curr" select="." />
    	<xsl:variable name="packageName">
 	<xsl:call-template name="substring-after-last">
 		<xsl:with-param name="input" select="." />
 		<xsl:with-param name="marker" select="'.'" />
 	</xsl:call-template>
	</xsl:variable>
        <section name="{$packageName}">
	<p>
	<a name="{$packageName}"/>
	</p>
	   <xsl:apply-templates select="$services[@package=$curr]">
		<xsl:sort select="@type" />
	    </xsl:apply-templates>
	 </section>	 
  </xsl:for-each>

  <section name="Map Structures">
	<xsl:for-each select="$beans">
		<xsl:sort select="@type" />	
		<xsl:variable name="bean" select="." />
		<subsection name="{@type}">
		<p>
		  <a name="{@type}"/>
		  <xsl:if test="@superclass != 'Object'">
		  Inherits attributes defined in <a href="#{@superclass}"><xsl:value-of select="@superclass" /></a><br />
		  </xsl:if>
		  <xsl:for-each select="implements/interface[@type != 'Serializable']">
	  		Inherits attributes defined in <a href="#{@type}"><xsl:value-of select="@type" /></a><br />			  
		  </xsl:for-each>
		  	<xsl:if test="comment/attribute[@name='@deprecated']">
		  		<b>Deprecated: </b>
		  		<xsl:value-of select="comment/attribute[@name='@deprecated']" />
		  		<br />
		  	</xsl:if>			  
		  <xsl:value-of select="comment/description" disable-output-escaping="yes"/>
		</p>
		<dl>
		<xsl:for-each select="methods/method[@visibility='public' and contains(@name,'get')]">
			<xsl:variable name="tmp" select="substring-after(@name,'get')" />
			<!-- convert method to javabean style field name. -->
			<xsl:variable name="fieldname" 
			select="concat(
				translate(substring($tmp,1,1),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')
				,substring($tmp,2)
				)" />
		 <dt><tt><xsl:value-of select="$fieldname"/></tt></dt>
		 <dd>
		  	<xsl:if test="comment/attribute[@name='@deprecated']">
		  		<b>Deprecated: </b>
		  		<xsl:value-of select="comment/attribute[@name='@deprecated']" />
		  		<br />
		  	</xsl:if>		 
		   <xsl:value-of select="comment/description" disable-output-escaping="yes" />
		    <i> (<xsl:call-template name="convert-type">
			 	<xsl:with-param name="p" select="."/>
			 </xsl:call-template>)</i>
		 </dd>
		</xsl:for-each>
		</dl>
		</subsection>
	</xsl:for-each>  
  </section>

</body>
</document>
</xsl:template>



<xsl:template match="jelclass">
  	<xsl:variable name="service-name" 
		select="comment/attribute[@name='@service']/description" />	
 <subsection name="{$service-name}">
 	<p>
 	<a name="{$service-name}"/>	
 		  	<xsl:if test="comment/attribute[@name='@deprecated']">
		  		<b>Deprecated: </b>
		  		<xsl:value-of select="comment/attribute[@name='@deprecated']" />
		  		<br />
		  	</xsl:if>	
 	<xsl:value-of select="comment/description" disable-output-escaping="yes" />	
	<xsl:if test="comment/attribute[@name='@see']">
		<br/>
		<xsl:text>See: </xsl:text>
	<xsl:for-each select="comment/attribute[@name='@see']">
		<xsl:value-of select="." disable-output-escaping="yes"/>
		<xsl:text> </xsl:text>
	</xsl:for-each>	
	</xsl:if>
	</p>
	<dl>	
	<xsl:variable name="inheritanceTypes" select="implements/interface/@fulltype" />
	<xsl:variable name="inheritanceStack" select=". | /jel/jelclass[@fulltype =$inheritanceTypes]" />	<xsl:for-each select="$inheritanceStack/methods/method[@visibility='public' 
			and not ( comment/attribute[@name='@deprecated'] 
				or contains(@name, 'Listener')) ]">
	 <dt><br /><tt><xsl:value-of select="$service-name"
	 	/>.<xsl:value-of select="@name"
		/>
		<xsl:choose>
			<xsl:when test="params/param">		
			<xsl:text>(</xsl:text>
			<xsl:for-each select="params/param">
		   		<i><xsl:value-of select="@name" /></i>
		   		<xsl:if test="position() != last()">
		   			<xsl:text>, </xsl:text>
		   		</xsl:if>
			</xsl:for-each>
			<xsl:text>)</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>()</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		</tt></dt>
	 <dd>
	 		  	<xsl:if test="comment/attribute[@name='@deprecated']">
		  		<b>Deprecated: </b>
		  		<xsl:value-of select="comment/attribute[@name='@deprecated']" />
		  		<br />
		  	</xsl:if>	 
	 <xsl:value-of select="comment/description" disable-output-escaping="yes"/>	     
	     	<xsl:for-each select="params/param">
			<br/><i><tt><xsl:value-of select="@name"/></tt></i> 
			- <xsl:value-of select="@comment" disable-output-escaping="yes"/>
			 <i> (<xsl:call-template name="convert-type">
			 	<xsl:with-param name="p" select="."/>
			 </xsl:call-template>)</i>			 
		</xsl:for-each>
		<br/><i>Return</i> - <xsl:value-of select="@returncomment" disable-output-escaping="yes"/>
			<i> (<xsl:call-template name="convert-type">
				<xsl:with-param name="p" select="." />		
			   </xsl:call-template>)</i>
	 </dd>
	</xsl:for-each>
	</dl>
 </subsection>
</xsl:template>


<!-- helper methods -->

<xsl:template name="convert-type">
<xsl:param name="p" />
<xsl:if test="contains($p/@fulltype,'[]')">
	<xsl:text>List of : </xsl:text>
</xsl:if>
<xsl:choose>
  <xsl:when test="$p/@type = 'Map'">
  	<xsl:text>key-value map</xsl:text>
  </xsl:when>
  <xsl:when test="$p/@type = 'String'">
  	<xsl:text>string</xsl:text>
  </xsl:when>
  <xsl:when test="$p/@type = 'Document'">
  	<xsl:text>string containing XML</xsl:text>
  </xsl:when>
  <xsl:when test="$p/@type = 'URI'">
  	<xsl:text>Ivorn or other URI</xsl:text>
  </xsl:when>
  <xsl:when test="$p/@type = 'void'">
  	<xsl:text>nothing</xsl:text>
  </xsl:when>
  <xsl:when test="contains($p/@fulltype,'java') 
  	or $p/@fulltype = 'boolean' 
  	or $p/@fulltype = 'int'
  	or $p/@fulltype = 'byte'
  	or $p/@fulltype = 'float'
  	or $p/@fulltype = 'double'  	  	
  	"> <!--  standard lib or prim type. -->
  	<xsl:value-of select="$p/@type"/>
  </xsl:when>
  <xsl:otherwise><!-- try and hyperlink to it -->
  	<a href="#{$p/@type}"><xsl:value-of select='$p/@type'/></a>    
  </xsl:otherwise>
</xsl:choose>
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


</xsl:stylesheet>
