<?xml version='1.0'?>
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform"
            xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
            version="1.0">

<!--
            xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v0.1"
            xmlns:vor="http://www.ivoa.net/xml/VOResource/v0.10"
-->

  <import href="fallback.xslt"/>
  <import href="rdf-RegistryInterface-0.1.xslt"/>
  <import href="rdf-VOResource-0.10.xslt"/>

  <template match="/">
    <rdf:RDF xmlns:rio="urn:example.org/DUMMY"
             xmlns:voro="http://www.ivoa.net/xml/VOResource/v1.0#"
             xmlns:x2s="http://ns.eurovotech.org/registry-metadata">
      <apply-templates/>
    </rdf:RDF>
  </template>
</stylesheet>
