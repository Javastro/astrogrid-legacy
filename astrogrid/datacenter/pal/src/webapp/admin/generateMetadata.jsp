<%@ page import="org.astrogrid.datacenter.metadata.MetadataInitialiser"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%= MetadataInitialiser.generateMetadata() %>
