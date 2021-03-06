<?xml version="1.0" encoding="UTF-8"?>
<x:stylesheet xmlns:x="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                exclude-result-prefixes="h"
                xmlns:h="http://www.w3.org/1999/xhtml">

  <x:output method="xml"
            version="1.0"
            doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
            doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
            omit-xml-declaration="yes"/>

  <x:param name="target"/>
  <x:param name="document-id">document-id</x:param>

  <x:template match="/">
    <x:choose>
      <x:when test='$target="aux"'>
        <x:apply-templates select='//processing-instruction("bibliography")'
                           mode='extract-aux'/>
      </x:when>
      <x:when test='$target'>
        <x:message terminate='yes'>
          Unrecognised target <x:value-of select="$target"/>
        </x:message>
      </x:when>
      <x:otherwise> <!-- normal case -->
        <x:apply-templates/>
      </x:otherwise>
    </x:choose>
  </x:template>

  <!-- Default identity transformation -->
  <x:template match="*|@*">
    <x:copy>
      <x:apply-templates select="@*|node()"/>
    </x:copy>
  </x:template>

  <!-- Handle <div class='section'> -->
  <x:template match="h:div[@class='section' or @class='section-nonum']">
    <x:variable name="id">
      <x:call-template name='make-section-id'/>
    </x:variable>
    <x:variable name="level">
      <x:choose>
        <x:when test="ancestor::h:div[@class='section']/ancestor::h:div[@class='section']">h4</x:when>
        <x:when test="ancestor::h:div[@class='section']">h3</x:when>
        <x:otherwise>h2</x:otherwise>
      </x:choose>
    </x:variable>
    <x:element name="{$level}">
      <!--<a name="{$id}"><x:call-template name='make-section-name'/></a>-->
      <a name="{$id}">
        <x:apply-templates select='.'
                           mode='make-section-name'/>
      </a>
    </x:element>
    <x:apply-templates/>
  </x:template>

  <!-- The first paragraph under a section division is the title.  That's
       handled above, so we must skip it here. -->
  <x:template match="h:div[@class='section' or @class='section-nonum']/h:p[1]"/>

  <x:template match="h:div[@class='appendices']">
    <h:hr/>
    <h:h2>Appendices</h:h2>
    <x:apply-templates/>
  </x:template>

  <!-- Alternative handling for h:div elements when we're making the ToC.
       We don't have to check the @class attribute, as this is only called
       on the correct type of divs. -->
  <x:template match='h:div' mode='make-toc'>
    <x:variable name="id">
      <x:call-template name='make-section-id'/>
    </x:variable>
    <h:li>
      <!--<h:a href="#{$id}"><x:call-template name='make-section-name'/></h:a>-->
      <h:a href="#{$id}"><x:apply-templates select='.' mode='make-section-name'/></h:a>
      <h:ul>
        <x:apply-templates select="h:div[@class='section' or @class='section-nonum']" mode="make-toc"/>
      </h:ul>
    </h:li>
  </x:template>

  <!-- The processing-instructions which actually generates the ToC -->
  <x:template match='processing-instruction("toc")'>
    <h:div id="toc" class='toc'>
      <h:ul>
        <x:apply-templates select="//h:body/h:div[@class='section' or @class='section-nonum']|//h:body/h:div[@class='appendices']/h:div" mode="make-toc"/>
      </h:ul>
    </h:div>
  </x:template>

  <!-- named template to extract an id for a section -->
  <x:template name='make-section-id'>
    <x:choose>
      <x:when test="@id">
        <x:value-of select="@id"/>
      </x:when>
      <x:otherwise>
        <x:value-of select="generate-id()"/>
      </x:otherwise>
    </x:choose>
  </x:template>

  <!-- named template to extract the numbered section name -->
  <!--<x:template name='make-section-name'>-->
  <x:template match='h:div' mode='make-section-name'>
    <x:choose>
      <x:when test="ancestor-or-self::h:div[@class='section-nonum']">
        <!-- nothing, and no text elements -->
      </x:when>
      <x:when test="ancestor::h:div[@class='appendices']">
        <x:number count="h:div[@class='section']"
                  level='multiple'
                  format='A.1'/>
        <x:text>???</x:text>
      </x:when>
      <x:otherwise>
        <x:number count="h:div[@class='section']"
                  level='multiple'
                  format='1.1'/>
        <x:text>???</x:text>
      </x:otherwise>
    </x:choose>
    <x:apply-templates select="h:p[1]/text()"/>
  </x:template>

  <!--
  <x:template match="ng:rcsdate">
    <x:variable name="fmt" select="@format"/>
    <x:choose>
      <x:when test="$fmt='standard'">
        <x:call-template name="long_date">
          <x:with-param name="date" select="substring(., 8, 10)"/>
        </x:call-template>
      </x:when>
    </x:choose>
  </x:template>
  -->

  <x:template match="processing-instruction('bibliography')">
    <x:copy-of select="document(concat(substring-before($document-id, '.xml'),'.bbl'))"/>
  </x:template>

  <x:template match="h:span[@class='cite']">
    <em>
      <x:text>[</x:text>
      <h:a>
        <x:attribute name="href">#ref:<x:value-of select="."/></x:attribute>
        <x:value-of select="."/>
      </h:a>
      <x:text>]</x:text>
    </em>
  </x:template>

  <x:template match="processing-instruction('bibliography')" mode="extract-aux">
    <x:text>\relax
</x:text>
    <x:apply-templates select="//h:span[@class='cite']" mode="extract-aux"/>
    <!--
    <x:if test="@style">
      <x:call-template name="make-tex-command">
        <x:with-param name="command">bibstyle</x:with-param>
        <x:with-param name="content" select="@style"/>
      </x:call-template>
    </x:if>
    <x:if test="@data">
      <x:call-template name="make-tex-command">
        <x:with-param name="command">bibdata</x:with-param>
        <x:with-param name="content" select="@data"/>
      </x:call-template>
    </x:if>
    -->
  </x:template>

  <x:template match="h:span[@class='cite']" mode="extract-aux">
    <x:call-template name="make-tex-command">
      <x:with-param name="command">citation</x:with-param>
      <x:with-param name="content">
        <x:copy-of select="."/>
      </x:with-param>
    </x:call-template>
  </x:template>

  <!-- Set up cross references to div elements containing id attributes.
       We could do this with ID attributes, but this removes dependence
       on there being a DTD to hand.  Thanks to the XSL FAQ for the hint.
       The div target of the link has an id attribute, and we make the
       link with an element <span class='xref'>id-of-target</span> . -->
  <x:key name='xrefs' match='h:div' use='@id'/>
  <x:template match="h:span[@class='xref']">
    <x:variable name='id' select='.'/>
    <h:a href="#{$id}">
      <x:apply-templates select="key('xrefs',$id)"
                         mode='make-section-name'/>
    </h:a>
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
