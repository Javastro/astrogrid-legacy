<?xml version="1.0" encoding="UTF-8"?>
<!--
    Support a mildly enhanced HTML.

    <div class='section' id='foo'><p class='title'>Title</p> ...</div>
        Support sectioning and cross references.  These <div> elements
        can be nested to give subsections.

    <div class='appendices'> ... </div>
        Contains all the <div> elements marked as appendices

    <span class='xref'>div-id</span>
        Generates a cross-reference to a <div class='section' id='div-id'>
        element.

    <?toc?>
        Generate a table of contents from the <div class='section'> elements

    <?bibliography?>
        Include a bibliography, generated externally.

    <span class='cite'>foo</span>
        Make a link to a bibliography entry.  If the stylesheet is
        invoked with the 'target' parameter being "aux", then instead
        of generating HTML, generate a LaTeX .aux file, which can be
        separately processed into a .bbl file, which is what the
        <?bibliography?> PI incorporates.

    <span class='rcsinfo' >$Foo: xxx yyy $</span>
        Expands to just "xxx yyy".

    Apart from these constructions, the input is copied to the output.
-->

<x:stylesheet xmlns:x="http://www.w3.org/1999/XSL/Transform"
              version="1.0"
              exclude-result-prefixes="h"
              xmlns:h="http://www.w3.org/1999/xhtml"
              xmlns="http://www.w3.org/1999/xhtml">

  <x:output method="xml"
            encoding="UTF-8"
            version="1.0"
            omit-xml-declaration="yes"/>

  <!-- The following is redundant in general, but if it's not here, then
   !   the XSLT processor within the JDK (versions?), which is what is
   !   used by Ant, can end up generating excess xmlns:h declarations within
   !   some output elements (specifically the h:ul of the <?toc?>),
   !   which obviously confuse HTML browsers. -->
  <x:namespace-alias stylesheet-prefix="h" result-prefix="#default"/>

<!--
    The following aren't terribly useful, so might as well be skipped.
    Also, they cause xsltproc to produce a not-as-helpful-as-it-thinks
    http-equiv meta element setting the character encoding.
            doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
            doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
-->

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
      <a name="{$id}">
        <x:apply-templates select='.'
                           mode='make-section-name'/>
      </a>
    </x:element>
    <x:apply-templates/>
  </x:template>

  <!-- Title paragraphs are handled in make-section-name mode, and must
   !   be skipped when we come across them normally. -->
  <x:template match="h:p[@class='title']"/>

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
      <h:a href="#{$id}"><x:apply-templates select='.' mode='make-section-name'/></h:a>
      <x:if test="h:div[@class='section' or @class='section-nonum']">
        <h:ul>
          <x:apply-templates select="h:div[@class='section' or @class='section-nonum']" mode="make-toc"/>
        </h:ul>
      </x:if>
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
  <x:template match='h:div' mode='make-section-name'>
    <x:choose>
      <x:when test="ancestor-or-self::h:div[@class='section-nonum']">
        <!-- nothing, and no text elements -->
      </x:when>
      <x:when test="ancestor::h:div[@class='appendices']">
        <x:number count="h:div[@class='section']"
                  level='multiple'
                  format='A.1'/>
        <x:text>–</x:text>
      </x:when>
      <x:otherwise>
        <x:number count="h:div[@class='section']"
                  level='multiple'
                  format='1.1'/>
        <x:text>–</x:text>
      </x:otherwise>
    </x:choose>
    <x:apply-templates select="h:p[@class='title']/text()"/>
    <!--<x:apply-templates select="h:p[1]/text()"/>-->
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

  <!-- Process all <span class='cite'>foo</span> elements into a link to
       a bibliography entry which has a ref:foo target.  This bibliography
       is externally generated, and pulled in by the <?bibliography?> PI. -->
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

  <!-- In the extract-aux mode, create a list of \citation{blah} commands
       from any <span class='cite'>blah</span> elements. -->
  <x:template match="h:span[@class='cite']" mode="extract-aux">
    <x:call-template name="make-tex-command">
      <x:with-param name="command">citation</x:with-param>
      <x:with-param name="content">
        <x:copy-of select="."/>
      </x:with-param>
    </x:call-template>
  </x:template>

  <!-- Set up cross references to div elements containing id attributes.
           <span class='xref'>id-value</span>
       (where id-value is a div/@id attribute).
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

  <!-- Extract CVS/RCS information, "xxx" contained within
       <span class='rcsinfo' >$blah: xxx $</span> -->
  <x:template match="h:span[@class='rcsinfo']">
    <x:value-of select='substring-before(substring-after(.,": ")," $")'/>
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
