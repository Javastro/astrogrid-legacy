<%@ page import="org.astrogrid.datacenter.metadata.MetadataInitialiser, org.astrogrid.util.DomHelper"
   
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%= DomHelper.DocumentToString(MetadataInitialiser.generateMetadata()) %>
