<?xml version='1.0'?>
<!-- Generate content from handler-registry.xml, for use by xschema2xslt.lx -->
<x:stylesheet xmlns:x="http://www.w3.org/1999/XSL/Transform"
              version="1.0">

  <x:output method='xml'/>

  <x:template match='/'>
    <namespace-nodes>
      <x:apply-templates select='/handler-registry/xml-ns'/>
      <x:apply-templates select='/handler-registry/ontology-ns'/>
    </namespace-nodes>
  </x:template>

  <x:template match="xml-ns|ontology-ns">
    <x:attribute name='xxxxx-{@id}'>
      <x:apply-templates/>
    </x:attribute>
  </x:template>
</x:stylesheet>
