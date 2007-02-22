<?xml version='1.0'?>

<!--
! Extract UType declarations from HTML.
!
! I'm not quite sure yet what is the best way to announce this.  The
! most obvious thing is to use GRDDL, define the profile
! "http://www.w3.org/2003/g/data-view" in the <head>, and link to a
! transformation, as in, for example:
!
!   <head profile="http://www.w3.org/2003/g/data-view">
!     <title>Namespace description document</title>
!     <link rel="transformation"
!           href="http://www.ivoa.net/utypes/grok-utypes.xslt"/>
!   </head>
!
! However I don't want to support full GRDDL yet, so I'm not sure
! whether the best thing is to define my own profile and forget the
! GRDDL thing, or whether it's best to make it look as if it's GRDDL,
! but in fact ignore the transformation link.
!
! The namespace of the document is specified by the first of the following
! which is found:
!
!     * an element in the document with class "namespace"
!     * a base element in the <head>
!     * a html:base element in the <html:head>
!     * or else the namespace is the URI of the current document
!       (that is, `@prefix x: <>', in N3 terms).
!
! See <http://www.w3.org/TR/html4/>.  There is no support at present 
! for xml:base. <http://www.w3.org/TR/xmlbase/>
!
! The structures which this recognises are as follows:
!
! <foo class='utype' id='u1'>...</foo>
!     Declare a UType class 'u1' (element and contents don't matter)
! 
! <foo class='utype' id='u2'>...<a rel='subclassof' href='#u1'>...</a></foo>
!     Declare Utype 'u2' to be a subclass of 'u1'.
!-->

<x:stylesheet xmlns:x="http://www.w3.org/1999/XSL/Transform"
              xmlns:h="http://www.w3.org/1999/xhtml"
              version="1.0">

  <x:output method='text'
            encoding='UTF-8'/>

  <x:template match='/'>
    <x:text>@prefix rdfs: &lt;http://www.w3.org/2000/01/rdf-schema#&gt;.
@prefix u: &lt;http://example.ivoa.org/utypes#&gt;.
</x:text>
    <x:choose>
      <x:when test='//*[@class="namespace"]'>
        <x:apply-templates select='//*[@class="namespace"]'/>
      </x:when>
      <x:when test="/h:html/h:head/h:base">
        <x:apply-templates select="/h:html/h:head/h:base"/>
      </x:when>
      <x:when test="/html/head/base">
        <x:apply-templates select="/html/head/base"/>
      </x:when>
      <x:otherwise>
        <x:text>@prefix : &lt;#&gt;.

</x:text>
      </x:otherwise>
    </x:choose>
    <x:apply-templates select='//*[@class="utype" and @id]'/>
    <x:apply-templates select='//*[@rel="subclassof" and @href]'/>
  </x:template>

  <!-- various ways of defining the namespace -->
  <x:template match='*[@class="namespace"]'>
    <x:text>@prefix : &lt;</x:text>
    <x:apply-templates/>
    <x:text>#&gt;.

</x:text>
  </x:template>
  <x:template match='h:base'>
    <x:text>@prefix : &lt;</x:text>
    <x:apply-templates select='@href'/>
    <x:text>#&gt;.

</x:text>
  </x:template>
  <x:template match='base'>
    <x:text>@prefix : &lt;</x:text>
    <x:apply-templates select='@href'/>
    <x:text>#&gt;.

</x:text>
  </x:template>

  <!-- class declaration -->
  <x:template match='*[@class="utype"]'>
    <x:apply-templates select='.' mode='genref'/>
    <x:text> a u:UType.
</x:text>
    <!--
    <x:text> a rdfs:Class.
</x:text>
    -->
  </x:template>

  <!-- generate a reference to a class, based on the @id of
   !   the referenced object -->
  <x:template match='*' mode='genref'>
    <x:choose>
      <x:when test='@id'>:<x:value-of select='@id'/></x:when>
      <!-- The 'otherwise' should never happen, but generate some
           syntactically OK content nevertheless. -->
      <x:otherwise>_:<x:value-of select='generate-id()'/></x:otherwise>
    </x:choose>
  </x:template>

  <!-- the subClassOf relation -->
  <x:template match='*[@rel="subclassof" and @href]'>
    <x:apply-templates select='ancestor::*[@class="utype"]' mode='genref'/>
    <x:text> rdfs:subClassOf </x:text>
    <x:choose>
      <!-- It appears that we could use genref mode plus a <key @id> here.
       !   We almost can, but it appears that some processors, in
       !   particular _one_ of the processors within javax.xml,
       !   gets confused by this and emits wrong text, in a way which
       !   I can't reduce, or work around.  The following isn't really
       !   too bad... -->
      <x:when test='starts-with(@href,"#")'
              >:<x:value-of select='substring(@href,2)'/></x:when>
      <x:otherwise>&lt;<x:value-of select='@href'/>&gt;</x:otherwise>
    </x:choose>
    <x:text>.
</x:text>
  </x:template>
</x:stylesheet>
