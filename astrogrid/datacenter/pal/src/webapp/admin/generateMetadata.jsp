<%@ page import="org.astrogrid.datacenter.metadata.MetadataGenerator"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%= MetadataGenerator.generateMetadata() %>
