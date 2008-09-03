<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Command-line-application server - Reference guide to the environment entries for the CL-CEC</title>
<%@ include file="../inc/header.jsp"%>
<div id="bodyColumn">
  <div class="contentBox">
    <div class="section"><a name="Environment_entries"></a>
      <h2>Environment entries</h2>
      <dl>
        <dt>cea.local.apps.config</dt>
        <dd>The URL for the file that describes the local applications.
          The CL-CEC needs to read this file when it starts up to determine what
          it may run. The URL normally points to a local file but may point to
          a public web-server. </dd>
        <dt>org.astrogrid.registry.query.endpoint</dt>
        <dd> The service endpoint (or access URL) for querying a registry web-service. The CL-CEC
          will use this registry to locate servers in VOSpace. Therefore, if this
          entry is not set correctly you will find that the CL-CEC will not be able to
          complete jobs that read or write files in VOSpace. </dd>
        <dd> The default value points to AstroGrid's central reference registry. This
          value will most probably work satisfactorily for your CL-CEC. However,
          you may be able to get better performance by changing to a different
          registry, e.g. one with lower load or closer on the network to the CL-CEC.
          You will definitely have to change to change reference registry if your
          VOSpace assets are on servers not known to AstroGrid. </dd>
        <dt>org.astrogrid.registry.update</dt>
        <dd> The service endpoint (or access URL) for updating a registry via its 
          web service. The CL-CEC will use this registry to register itself and its
          applications. Therefore, if you do not complete this entry the CL-CEC will
          not be able to register by itself. (You could then register it manually,
          by writing the registration documents yourself.) </dd>
        <dd> AstroGrid strongly recommends that you run your own publishing registry and
          register your CL-CEC (and other services) in that. The main reference
          registries in the VO then harvest the registrations from your publishing
          registry. This gives you tight control over the registry metadata for
          your services and makes it easier for you to manage your own resources
          (finding all your own registrations in a large, reference registry can
          be trying). </dd>
        <dd> You can, currently,
          register in the central AstroGrid reference registry. However, in later
          versions of the AstroGrid system this will no longer be allowed and you
          will have to use your own registry. </dd>
        <dd> The default value for this environment entry specifies the server "localhost"
          and the port number 8080. This <em>won't</em> work in general; you have to
          set this entry. The default is only there to show you the URL pattern for
          an AstroGrid registry. </dd>
        <dt>cea.job.records</dt>
        <dd> The absolute path (not a URL) to the directory where the CL-CEC can store 
          records of its jobs. The CL-CEC keeps these files indefinitely as a record of
          the jobs it has run. The files are not large. </dd>
        <dt>cea.job.temp.files</dt>
        <dd> The absolute path (not a URL) to a directory where the CL-CEC can store
          temporary files associated with the jobs its runs. The temporary files are
          data going to or from the application. They can be any size, possibly very
          large. </dd>
        <dt>cea.component.manager.class</dt>
        <dd> The name of the Java class that manages the CL-CEC at the top level.
          This is the class that defines the CEC as a CL-CEC and draws in the code
          for dealing with command-line software. </dd>
        <dd> You should not change this value during configuration. </dd>
      </dl>
    </div>
  </div>
</div>
<%@ include file="../inc/footer.jsp" %>