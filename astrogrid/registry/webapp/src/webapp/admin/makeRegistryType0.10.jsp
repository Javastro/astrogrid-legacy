<vor:VOResources
   xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
   xmlns:xs="http://www.w3.org/2001/XMLSchema" 
   xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10" 
   xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3" 
   xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5"
   xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
<vor:Resource xsi:type="vg:Registry"  updated="2004-11-20T15:34:22Z" status="active">
 <title><%= request.getParameter("Title") %></title>
 <identifier>ivo://<%= request.getParameter("AuthorityID") %>/org.astrogrid.registry.RegistryService</identifier>
 <curation>
  <publisher><%= request.getParameter("Publisher") %></publisher>
  <contact>
   <name><%= request.getParameter("ContactName") %></name>
   <email><%= request.getParameter("ContactEmail") %></email>
  </contact>
 </curation>
 <content>
  <subject>registry</subject>
  <description>Main Astrogrid Registry for the Full Registry.</description>
  <referenceURL><%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %></referenceURL>
  <type>Archive</type>
 </content>
    <interface xsi:type="vs:WebService">
      <accessURL use="full"><%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/services/RegistryHarvest</accessURL>
    </interface> 
   <vg:managedAuthority><%= request.getParameter("AuthorityID") %></vg:managedAuthority>
</vor:Resource>
</vor:VOResources>