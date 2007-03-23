<?xml version='1.0'?>
<x:stylesheet xmlns:x="http://www.w3.org/1999/XSL/Transform"
              version="1.0"
              xmlns:x2s="http://ns.eurovotech.org/registry-metadata#"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">

  <x:output method='text'/>

  <x:key name='xns'
         match='xml-ns'
         use='@id'/>
  <x:key name='ons'
         match='ontology-ns'
         use='@id'/>

  <x:template match='/'>
    <x:apply-templates select="handler-registry"/>
  </x:template>

  <x:template match='handler-registry'>
    <x:apply-templates select='handler'/>
  </x:template>

  <x:template match='handler'>
        nsinfo.put("<x:apply-templates select='key("xns", @from)'/>",
                   new NSInfo("<x:apply-templates select='xslt'/>",
                              "<x:value-of select='@from'/>",
                              "<x:value-of select='@to'/>",
                              "<x:apply-templates select='key("ons", @to)'/>"));
</x:template>

  <x:template match='xml-ns'>
    <x:apply-templates/>
  </x:template>

  <x:template match='ontology-ns'>
    <x:apply-templates/>
  </x:template>

</x:stylesheet>
