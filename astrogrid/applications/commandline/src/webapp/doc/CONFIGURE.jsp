<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Command-line-application server - Configuration</title>
<jsp:include page="../inc/header.jsp" flush="true" />
<!-- body -->
<div id="bodyColumn">

  <div class="contentBox">
  
    <div class="section"><a name="Configuration"></a>
      <h2>Configuration</h2>
      <p> You must configure the CEC so that it works with your chosen applications and in the
        environment of your site. Please follow this sequence: </p>
      <ol>
        <li>Edit the CEC's "environment" to tell it where to find things.</li>
        <li>Edit the application-description document.</li>
        <li>Edit the "registration-template" document.</li>
        <li>Preview the registration document.</li>
        <li>Restart the web application to pick up the changed configuration.</li>
      </ol>
      <p> Instructions for each step are gven below. </p>
    </div>
    <div class="section"><a name="Edit_the_CEC_s_environment"></a>
      <h2>Edit the CEC's environment</h2>
      <p> The CEC needs to be told where to find things in its 
        operating environment.
        You do this by editing the "environment entries" (i.e. named properties)
        using the environment-editor page. There is a link to the editor in the
        sidebar. </p>
      <p> The editor displays descriptions of the uses and proper
        values of the environment entries. More detail is available in the
        reference manual for the CEC. </p>
      <p> When you save changes in the editor, it first saves them into a file
        in the CEC's own configuration directory. This makes a safe copy of the
        environment but doesn't apply the changes. The editor then applies the
        environment by copying it into Tomcat's configuration directory. At this
        point, Tomcat should detect the change and restart the web application with
        the new environment. </p>
      <div class="subsection"><a name="The_CEC_s_working_directories_and_configuration_files"></a>
        <h3>The CEC's working directories and configuration files</h3>
        <p> The CEC uses a fixed directory-structure for its configuration and 
          working files. </p>
        <pre>
				(base)/
				(base)/temp
				(base)/records
				(base)/config
				(base)/config/app-description.xml
				(base)/config/registration-template.xml
				(base)/config/(context-name).xml
			</pre>
        <p> In this structure, <i>(base)</i> is the directory you specify by setting the environment
          entry <i>cea.base.dir</i>. By changing this entry, you can move the directory structure
          anywhere you like. However, best thing is usually to leave the structure at the location
          initially suggested by the web-application. That way, the web-application can find it again when
          recovering from a software upgrade. Usually, Tomcat and the web-application conspire to
          put <i>cea.base.dir</i> inside the Tomcat installation. </p>
        <p> There are two cases where you'd need to change <i>cea.base.dir</i>. </p>
        <ol>
          <li>If the default location is inside Tomcat and you have Tomcat's directories write-locked.</li>
          <li>If the default location turns out to be in <i>/tmp</i> and you are running Solaris.</li>
        </ol>
        <p> In the latter case, <i>/tmp</i> is no good as it gets wiped when the server reboots. You need
          the CEC's files to survive server restarts. </p>
        <p> The file <i>(base)/config/(context-name).xml</i> (where <i>(context-name)</i> is the name of the 
          web-application context; e.g. CEC-1) is where the environment editor stores the environment. </p>
      </div>
    </div>
    <div class="section"><a name="Edit_the_application-description_document"></a>
      <h2>Edit the application-description document</h2>
      <p> You must produce an XML file describing to the CL-CEC how to run the applications
        of choice. The XML vocabulary for this file is defined by CEA. </p>
      <p> The application-description file is always called <i>app-description.xml</i>.
        To find it, use the <i>show current configuration</i> link in the sidebar. You need to replace the copy at that location with a
        customized version. </p>
      <p> Please see the reference manual for a description of the
        application-description language. </p>
      <p> If you are providing applications that have already run in CEA at another
        site then you can re-use that site's application-description; the parameters
        and interfaces will be the same. However, you will need to change the <i>ExecutionPath</i> elements
        to point to the locations where your applications are installed. </p>
    </div>
    <div class="section"><a name="Edit_the_registration_template"></a>
      <h2>Edit the registration template</h2>
      <p> The CEC uses an XML document called a registration template to generate the 
        registration documents that it submits to the IVO resource-registry. You need
        to edit this template. </p>
      <p> To find the registration template, use the <i>show current configuration</i> link in the sidebar. You need to replace the copy at that location with a
        customized version. </p>
      <p> The template initialized by the CEC has extensive comments to tell you what to set;
        please read them. Note that there are two main sections: one for the applications
        and another, below, for the service. </p>
    </div>
    <div class="section"><a name="Preview_the_registration_document"></a>
      <h2>Preview the registration document</h2>
      <p> When you have the environment set, including the entry that leads the URL
        to your edited copy of the registration template, you should check that the
        registration documents are coming out satisfactorily. Use the
        "show registration document" link in the side-bar. </p>
      <p> The registration document is what will eventually be sent to the IVO
        resource registry to identify your CEC and its applications. These documents
        are public and are read both by human users of your service and by machines.
        Therefore, they need to be both scientifically useful and technically correct.
        You need to check them at this stage before going on to registration. </p>
      <p> The most important thing to check is the <i>identifier</i> element in the service
        description. This is the formal name for your service in the registry, and you
        can't change it once you've registered (changing the name creates a second
        registration; it doesn't rename the original one). Almost anything else can be
        patched up by registry updates, but you have to get the identifier right first time.
        Things to check: </p>
      <ul>
        <li>Is the authority ID part of the identifier (e.g. the <i>foo.bar</i> in <i>ivo://foo.bar/baz</i> right for the registry where you're publishing?
          A given registry accepts registrations only under a limited, pre-agreed
          set of authorities.</li>
        <li>Is the resource-key part (e.g. the <i>baz</i> in <i>ivo://foo.bar/baz</i> unique within the naming authority?</li>
        <li>Is the resource key a sensible and memorable name for the service?</li>
      </ul>
    </div>
    <div class="section"><a name="Preview_the_registration_document"></a>
      <h2>Preview the registration document</h2>
      <p> When you have the environment set, including the entry that leads the URL
        to your edited copy of the registration template, you should check that the
        registration documents are coming out satisfactorily. Use the
        "show registration document" link in the side-bar. </p>
      <p> The registration document is what will eventually be sent to the IVO
        resource registry to identify your CEC and its applications. These documents
        are public and are read both by human users of your service and by machines.
        Therefore, they need to be both scientifically useful and technically correct.
        You need to check them at this stage before going on to registration. </p>
      <p> The most important thing to check is the <i>identifier</i> element in the service
        description. This is the formal name for your service in the registry, and you
        can't change it once you've registered (changing the name creates a second
        registration; it doesn't rename the original one). Almost anything else can be
        patched up by registry updates, but you have to get the identifier right first time.
        Things to check: </p>
      <ul>
        <li>Is the authority ID part of the identifier (e.g. the <i>foo.bar</i> in <i>ivo://foo.bar/baz</i> right for the registry where you're publishing?
          A given registry accepts registrations only under a limited, pre-agreed
          set of authorities.</li>
        <li>Is the resource-key part (e.g. the <i>baz</i> in <i>ivo://foo.bar/baz</i> unique within the naming authority?</li>
        <li>Is the resource key a sensible and memorable name for the service?</li>
      </ul>
    </div>
    <div class="section"><a name="Restart_the_web_application"></a>
      <h2>Restart the web application</h2>
      <p> The CEC web-service needs to be restarted to pick up the changed configuration.
        This means that you have to restart the whole web-application. Either restart
        Tomcat itself, or use the Tomcat Manager application to restart the current
        web-application. </p>
    </div>
  </div>
</div>
<jsp:include page="../inc/footer.jsp" flush="true" />