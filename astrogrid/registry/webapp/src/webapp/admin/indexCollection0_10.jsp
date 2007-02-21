<collection xmlns="http://exist-db.org/collection-config/1.0">
   <index xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10"  xmlns:xsi="http://www.w3c.org/2001/XMLSchema-instance">
       <fulltext default="all" attributes="true" alphanum="true"/>
       <create path="//@xsi:type" type="xs:string"/>
       <create path="//@status" type="xs:string"/>       
       <create path="//vr:title" type="xs:string"/>
       <create path="//vr:shortName" type="xs:string"/>
       <create path="//vr:identifier" type="xs:string"/>
       <create path="//vr:curation/vr:publisher" type="xs:string"/>
       <create path="//vr:curation/vr:creator/vr:name" type="xs:string"/>
       <create path="//vr:curation/vr:contributor" type="xs:string"/>
       <create path="//vr:curation/vr:contact/vr:name" type="xs:string"/>
       <create path="//vr:content/vr:subject" type="xs:string"/>
       <create path="//vr:content/vr:description" type="xs:string"/>
       <create path="//vr:content/vr:source" type="xs:string"/>
       <create path="//vr:content/vr:referenceURL" type="xs:string"/>
       <create path="//vr:content/vr:type" type="xs:string"/>
       <create path="//vr:content/vr:contentLevel" type="xs:string"/>
   </index>
</collection> 