<%@ page import="java.io.*,
       org.astrogrid.util.DomHelper,
       org.astrogrid.datacenter.metadata.MetadataGenerator"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><% MetadataGenerator.writeMetadata(out); %>
