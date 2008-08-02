DLM_PATH needs to be set to point to the directory containing acr_idl.so acr_idl.dlm
need also to set the system dynamic loadable library path includes the libar-capi.so

the connection to the ar needs to be established with ar_init

 print, ivoa_registry_getResource("ivo://org.astrogrid/Galaxev")
 crab=cds_sesame_resolve("crab")
 query=ivoa_siap_constructQuery("ivo://archive.eso.org/dss", crab.ra, crab.dec, 1.0D)
 results = ivoa_siap_execute(query)
 
 
 
 the types of the arguments are given in brackets for the xmlrpc interface
 
 The actual types of the IDL arguments are the same except for
 
 IVORN URI are mapped to string
 Date is mapped to a string - i.e. not yet handled as a string in 
 
 
 the maps from the xmlrpc interface are represented by (unnamed) structures in IDL - The AR can return arrays of differing map types - this is not allowed in IDL
 as all the stucts in an array must be of the same type - in this case the stuct returned for the whole array is the union struct - this means that individual members
 of the array might well have struct elements that are null.