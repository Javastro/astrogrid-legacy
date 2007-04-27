<?xml version='1.0'?>
<!-- Generate content from handler-registry.xml, for inclusion
     in ExtractNamespaces.java.in -->
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform"
            version="1.0"
            xmlns:x2s="http://ns.eurovotech.org/registry-metadata#"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">

  <output method='text'/>

  <key name='xns'
         match='xml-ns'
         use='@id'/>
  <key name='ons'
         match='ontology-ns'
         use='@id'/>

  <template match='/'>
    <apply-templates select="handler-registry"/>
  </template>

  <template match='handler-registry'>
    <apply-templates select='handler'/>
  </template>

  <template match='handler'>
    <choose>
      <when test='@from and @to'>
        nsinfo.put("<apply-templates select='key("xns", @from)'/>",
                   new NSInfo("<value-of select='@from'/>",
                              "<value-of select='@to'/>",
                              "<apply-templates select='key("ons", @to)'/>",
                              "<apply-templates select='xslt'/>"));
</when>
      <when test='@from'>
        nsinfo.put("<apply-templates select='key("xns", @from)'/>",
                   new NSInfo());
</when>
      <otherwise>
        // XXX found handler with no 'from' attribute!
      </otherwise>
    </choose>
  </template>

  <template match='xml-ns'>
    <apply-templates/>
  </template>

  <template match='ontology-ns'>
    <apply-templates/>
  </template>

</stylesheet>
