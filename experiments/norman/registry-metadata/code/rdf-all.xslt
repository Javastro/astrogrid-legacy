<?xml version='1.0'?>
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform"
            xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0"
            xmlns:x2s="http://ns.eurovotech.org/registry-metadata#"
            version="1.0">

  <!-- This processes v1.0 RegistryInterface Resources.  Swap the ri namespace to
   !   xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v0.1"
   !   to handle v0.1 Resources.
   ! -->

  <import href="fallback.xslt"/>
  <import href="rdf-VOResource-0.10.xslt"/>
  <import href="rdf-VOResource-1.0.xslt"/>
  <import href="rdf-RegistryInterface-0.1.xslt"/>
  <import href="rdf-RegistryInterface-1.0.xslt"/>
  <import href="rdf-TabularDB-v0.3.xslt"/>
  <import href="rdf-SIA-0.7.xslt"/>
  <import href="rdf-SIA-1.0.xslt"/>
  <import href="rdf-VORegistry-1.0.xslt"/>
  <import href="rdf-VODataService-v0.5.xslt"/>
  <import href="rdf-VODataService-1.0.xslt"/>

  <template match="/">
    <rdf:RDF xmlns:rio="urn:example.org/DUMMY"
             xmlns:voro="http://www.ivoa.net/xml/VOResource/v1.0#"
             xmlns:x2s="http://ns.eurovotech.org/registry-metadata#">
      <apply-templates select="//ri:Resource"/>
    </rdf:RDF>
  </template>

  <!-- This template is basically <call-template name='@xsi:type'/>,
       but the name attribute there can only be a QName, hence this mess.
       Yuk. -->
  <template name="resolve-xsitype">
    <variable name='l'>
      <choose>
        <when test="contains(@xsi:type,':')">
          <value-of select="substring-after(@xsi:type,':')"/>
        </when>
        <otherwise>
          <value-of select='@xsi:type'/>
        </otherwise>
      </choose>
    </variable>
    <!-- if @xsi:type has no colon, then p is empty string -->
    <variable name='p' select="substring-before(@xsi:type,':')"/>
    <variable name='n' select="namespace::*[name()=$p]"/>
    <choose>
      <when test="$n='http://www.ivoa.net/xml/VODataService/v0.5'">
        <call-template xmlns:ds05='http://www.ivoa.net/xml/VODataService/v0.5'
                       name="ds05:resolve-xsitype">
          <with-param name="type"><value-of select="$l"/></with-param>
        </call-template>
      </when>
      <when test="$n='http://www.ivoa.net/xml/VODataService/v1.0'">
        <call-template xmlns:ds10='http://www.ivoa.net/xml/VODataService/v1.0'
                       name="ds10:resolve-xsitype">
          <with-param name="type"><value-of select="$l"/></with-param>
        </call-template>
      </when>
      <when test="$n='http://www.ivoa.net/xml/SIA/v0.7'">
        <call-template xmlns:sia07="http://www.ivoa.net/xml/SIA/v0.7"
                       name="sia07:resolve-xsitype">
          <with-param name='type'><value-of select='$l'/></with-param>
        </call-template>
      </when>
      <when test="$n='http://www.ivoa.net/xml/SIA/v1.0'">
        <call-template xmlns:sia10="http://www.ivoa.net/xml/SIA/v1.0"
                       name="sia10:resolve-xsitype">
          <with-param name='type'><value-of select='$l'/></with-param>
        </call-template>
      </when>
      <when test="$n='http://www.ivoa.net/xml/VOResource/v0.10'">
        <call-template xmlns:vor01="http://www.ivoa.net/xml/VOResource/v0.10"
                       name="vor01:resolve-xsitype">
          <with-param name='type'><value-of select='$l'/></with-param>
        </call-template>
      </when>
      <when test="$n='http://www.ivoa.net/xml/VOResource/v1.0'">
        <call-template xmlns:vor10="http://www.ivoa.net/xml/VOResource/v1.0"
                       name="vor10:resolve-xsitype">
          <with-param name='type'><value-of select='$l'/></with-param>
        </call-template>
      </when>
      <when test="$n='http://www.ivoa.net/xml/VORegistry/v1.0'">
        <call-template xmlns:voreg="http://www.ivoa.net/xml/VORegistry/v1.0"
                       name="voreg:resolve-xsitype">
          <with-param name='type'><value-of select='$l'/></with-param>
        </call-template>
      </when>
      <when test="$n='urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3'">
        <call-template xmlns:tdb="urn:astrogrid:schema:vo-resource-types:TabularDB:v0.3"
                       name="tdb:resolve-xsitype">
          <with-param name='type'><value-of select='$l'/></with-param>
        </call-template>
      </when>
      <otherwise>
        <x2s:UNKNOWN>
          <text>rdf-all.xslt: Unable to resolve xsi:type=</text>
          <value-of select='$n'/>#<value-of select='$l'/>
        </x2s:UNKNOWN>
      </otherwise>
    </choose>
  </template>

  <!--
      no use...?
  <template name="emit-rdf-type">
    <param name="type"/>
    <variable name='prefix'
              select="substring-before($type,':')"/>
    <rdf:type rdf:resource="{namespace::*[name()=$prefix]}#{substring-after($type,':')}"/>
  </template>
-->

</stylesheet>
