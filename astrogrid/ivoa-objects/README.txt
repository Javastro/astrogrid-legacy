These project contains objects that are useful in interacting with an IVOA Registry

1. Initially generated using JAXB from the standard schema.
2. Then edited by hand to make 
      a) more comfortable to work with (remove some extraneous xml schema nasties...)
      b) Add a JPA mapping
      
Details of the JPA mapping


   1 embedded wherever the multiplicity was 1:1
   2 Added an ID field where necessary - all freestanding tables
   3 Used single table represent type hierarchy with a column (KIND) to distinguish the types
   4 Lists of enumerations have been converted to a comma separated set of values in a string.
   5 one to many relationships have been done by putting back reference into the many end so that can be annotated for the foreign key (would it b better to put this in the orm.xml file rather?)