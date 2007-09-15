<?xml version="1.0" encoding="UTF-8"?>
<x:stylesheet xmlns:x="http://www.w3.org/1999/XSL/Transform"
              version="1.0"
              exclude-result-prefixes="h"
              xmlns:h="http://www.w3.org/1999/xhtml"
              xmlns:dc="http://purl.org/dc/elements/1.1/"
              xmlns:dcterms="http://purl.org/dc/terms/">
  <x:output method="html" encoding="UTF-8" version="1.0" omit-xml-declaration="yes"/>

  <x:namespace-alias stylesheet-prefix="h" result-prefix="#default"/>

  <x:param name="target"/>

  <x:param name="document-id">document-id</x:param>

  <x:template match="/">
    <x:choose>
      <x:when test="$target='aux'">
        <x:apply-templates select="//processing-instruction('bibliography')" mode="extract-aux"/>
      </x:when>
      <x:when test="$target">
        <x:message terminate="yes">Unrecognised target <x:value-of select="$target"/>
        </x:message>
      </x:when>
      <x:otherwise>
        <x:apply-templates/>
      </x:otherwise>
    </x:choose>
  </x:template>

  <x:template match="*">
    <x:copy>
      <x:copy-of select="@*"/>
      <x:apply-templates/>
    </x:copy>
  </x:template>

  <x:template match="@*">
    <x:copy-of select="."/>
  </x:template>

  <x:template xmlns:grddl="http://www.w3.org/2003/g/data-view#" match="h:html">
    <x:copy>
      <x:attribute name="grddl:transformation">http://ns.inria.fr/grddl/rdfa/2007/05/25/RDFa2RDFXML.xsl</x:attribute>
      <x:apply-templates/>
    </x:copy>
  </x:template>

  <x:template match="h:head">
    <x:copy>
      <x:apply-templates select="@*"/>
      <x:copy-of select="namespace::*[.='http://purl.org/dc/elements/1.1/']"/>
      <x:choose>
        <x:when test="h:link[@rel='stylesheet']">
          <x:comment>Using stylesheet from source file</x:comment>
          <x:apply-templates select="h:link[@rel='stylesheet']"/>
        </x:when>
        <x:otherwise>
          <h:link rel="stylesheet" type="text/css" href="/style/base.css"/>
        </x:otherwise>
      </x:choose>
      <x:apply-templates select="h:title|h:meta|h:style"/>
    </x:copy>
  </x:template>

  <x:template match="h:div[@class='section' or @class='section-nonum']">
    <x:variable name="id">
      <x:call-template name="make-section-id"/>
    </x:variable>
    <x:variable name="level">
      <x:choose>
        <x:when test="ancestor::h:div[@class='section']/ancestor::h:div[@class='section']">h4</x:when>
        <x:when test="ancestor::h:div[@class='section']">h3</x:when>
        <x:otherwise>h2</x:otherwise>
      </x:choose>
    </x:variable>
    <x:element name="{$level}">
      <h:a name="{$id}">
        <x:apply-templates select="." mode="make-section-name"/>
      </h:a>
    </x:element>
    <x:apply-templates/>
  </x:template>

  <x:template match="h:p[@class='title']"/>

  <x:template match="h:div[@class='appendices']">
    <h:h2>Appendices</h:h2>
    <x:apply-templates/>
  </x:template>

  <x:template match="h:div" mode="make-toc">
    <x:variable name="id">
      <x:call-template name="make-section-id"/>
    </x:variable>
    <h:li>
      <h:a href="#{$id}">
        <x:apply-templates select="." mode="make-section-name"/>
      </h:a>
      <x:if test="h:div[@class='section' or @class='section-nonum']">
        <h:ul>
          <x:apply-templates select="h:div[@class='section' or @class='section-nonum']" mode="make-toc"/>
        </h:ul>
      </x:if>
    </h:li>
  </x:template>

  <x:template match="processing-instruction('toc')">
    <h:div id="toc" class="toc">
      <h:ul>
        <x:apply-templates select="//h:body/h:div[@class='section' or @class='section-nonum']|//h:body/h:div[@class='appendices']/h:div" mode="make-toc"/>
      </h:ul>
    </h:div>
  </x:template>

  <x:template name="make-section-id">
    <x:choose>
      <x:when test="@id">
        <x:value-of select="@id"/>
      </x:when>
      <x:otherwise>
        <x:value-of select="generate-id()"/>
      </x:otherwise>
    </x:choose>
  </x:template>

  <x:template match="h:div" mode="make-section-name">
    <x:choose>
      <x:when test="ancestor-or-self::h:div[@class='section-nonum']"/>
      <x:when test="ancestor::h:div[@class='appendices']">
        <x:number count="h:div[@class='section']" level="multiple" format="A.1"/>
        <x:text> </x:text>
      </x:when>
      <x:otherwise>
        <x:number count="h:div[@class='section']" level="multiple" format="1.1"/>
        <x:text> </x:text>
      </x:otherwise>
    </x:choose>
    <x:apply-templates select="h:p[@class='title']/text()"/>
  </x:template>

  <x:template match="processing-instruction('bibliography')">
    <x:copy-of select="document(concat(substring-before($document-id, '.xml'),'.bbl'))"/>
  </x:template>

  <x:template match="h:span[@class='cite']">
    <h:em>
      <x:text>[</x:text>
      <h:a>
        <x:attribute name="href">#ref:<x:value-of select="."/></x:attribute>
        <x:value-of select="."/>
      </h:a>
      <x:text>]</x:text>
    </h:em>
  </x:template>

  <x:template match="h:span[@class='url']">
    <h:a>
      <x:attribute name='href'><x:value-of select='.'/></x:attribute>
      <h:span class='url'>
        <x:value-of select="."/>
      </h:span>
    </h:a>
  </x:template>

  <x:template match="processing-instruction('bibliography')" mode="extract-aux">
    <x:text>\relax
</x:text>
    <x:apply-templates select="//h:span[@class='cite']" mode="extract-aux"/>
    <x:if test="string-length(.) &gt; 0">
      <x:call-template name="make-tex-command">
        <x:with-param name="command">bibdata</x:with-param>
        <x:with-param name="content">
          <x:value-of select="normalize-space(.)"/>
        </x:with-param>
      </x:call-template>
    </x:if>
  </x:template>

  <x:template match="h:span[@class='cite']" mode="extract-aux">
    <x:call-template name="make-tex-command">
      <x:with-param name="command">citation</x:with-param>
      <x:with-param name="content">
        <x:copy-of select="."/>
      </x:with-param>
    </x:call-template>
  </x:template>

  <x:key name="xrefs" match="h:div" use="@id"/>

  <x:template match="h:span[@class='xref']">
    <x:variable name="id" select="."/>
    <h:a href="#{$id}">
      <x:apply-templates select="key('xrefs',$id)" mode="make-section-name"/>
    </h:a>
  </x:template>

  <x:template match="h:span[@class='rcsinfo']">
    <x:value-of select="substring-before(substring-after(.,': '),' $')"/>
  </x:template>

  <x:template match="h:h1">
    <h:h1 property="dc:title">
      <x:apply-templates select="@*"/>
      <x:apply-templates/>
    </h:h1>
  </x:template>

  <x:template xmlns:owl="http://www.w3.org/2002/07/owl#" match="h:meta">
    <x:choose>
      <x:when test="@name='DC.rights'">
        <h:link about="">
          <x:attribute name="rel">
            <x:value-of select="@name"/>
          </x:attribute>
          <x:attribute name="href">
            <x:value-of select="@content"/>
          </x:attribute>
        </h:link>
      </x:when>
      <x:when test="@name='rcsdate'">
        <h:meta property="dcterms:modified" about="">
          <x:attribute name="content">
            <x:value-of select="translate(substring(@content,8,10),'/','-')"/>
            <x:text>T</x:text>
            <x:value-of select="substring(@content,19,8)"/>
          </x:attribute>
        </h:meta>
      </x:when>
      <x:when test="@name='purl'">
        <h:link rel="dc:identifier" about="">
          <x:attribute name="href">
            <x:value-of select="@content"/>
          </x:attribute>
        </h:link>
        <h:link rel="owl:sameAs">
          <x:attribute name="href">
            <x:value-of select="@content"/>
          </x:attribute>
        </h:link>
      </x:when>
      <x:when test="starts-with(@name,'DC.')">
        <h:meta about="">
          <x:attribute name="property">dc:<x:value-of select="substring-after(@name, 'DC.')"/>
          </x:attribute>
          <x:attribute name="content">
            <x:value-of select="@content"/>
          </x:attribute>
        </h:meta>
      </x:when>
    </x:choose>
  </x:template>

  <x:template match="h:div[@class='signature']">
    <x:copy>
      <x:apply-templates select="@*"/>
      <x:choose>
        <x:when test="h:a/@href">
          <x:attribute name="rel">dc:creator</x:attribute>
          <x:attribute name="href">
            <x:value-of select="h:a/@href"/>
          </x:attribute>
        </x:when>
        <x:otherwise>
          <x:attribute name="property">dc:creator</x:attribute>
          <x:attribute name="content">
            <x:value-of select="h:a/text()"/>
          </x:attribute>
        </x:otherwise>
      </x:choose>
      <x:apply-templates/>
    </x:copy>
  </x:template>

  <x:template match="h:div[@class='abstract']">
    <x:copy>
      <x:apply-templates select="@*"/>
      <h:meta property="dcterms:abstract" about="">
        <x:attribute name="content">
          <x:apply-templates select="h:p[not(@class)]" mode="text-only"/>
        </x:attribute>
      </h:meta>
      <x:apply-templates/>
    </x:copy>
  </x:template>

  <x:template match="h:p" mode="text-only">
    <x:value-of select="normalize-space(.)"/>
    <x:text>  </x:text>
  </x:template>

  <x:template name="make-tex-command">
    <x:param name="command"/>
    <x:param name="content"/>
    <x:text>\</x:text>
    <x:value-of select="$command"/>
    <x:text>{</x:text>
    <x:value-of select="$content"/>
    <x:text>}
</x:text>
  </x:template>

</x:stylesheet>
