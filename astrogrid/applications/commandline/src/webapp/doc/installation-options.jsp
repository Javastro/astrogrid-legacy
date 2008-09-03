<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Command-line-application server - Installation options</title>
<%@ include file="../inc/header.jsp" %>
<div id="bodyColumn">
  <div class="contentBox">
    <div class="section"><a name="Installation_options"></a>
      <h2>Installation options</h2>
      <p>This document discusses alternatives to the the standard installation.</p>
      <div class="subsection"><a name="Alternative_versions_of_SUN_Java"></a>
        <h3>Alternative versions of SUN Java</h3>
        <p>Instead of SUN Java 5, you may use the JRE for SUN Java 1.4.2,
          provided that you also switch to Jakarta-Tomcat 5.0. Tomcat 5.0.28 is
          recommended.</p>
        <p>If you use Java 1.4.2, you lose the ability to handle large outputs
          robustly. With this Java version, each output file will be buffered
          entirely in memory before being sent to MySpace. If the output is
          larger than the available memory then your web-application will crash
          and you will need to restart Tomcat manually. The "HTTP chunking"
          feature that we use to avoid this problem is only available in Java 5
          and later. </p>
        <p>Versions of SUN Java older than 1.4.2 will not run the web-application
          properly in all respects.</p>
        <p>We expect SUN Java 6 to work as well as SUN Java 5 for this
          web-application. We have not tested this configuration yet.</p>
      </div>
      <div class="subsection"><a name="Alternative_implementations_of_Java"></a>
        <h3>Alternative implementations of Java</h3>
        <p>You may use another implementation of Java, provided that it
          is a complete implementation of the Java platform. Some Java 
          implementations omit some library classes and these are likely
          to cause serious problems. In particular, the <i>Kaffe</i> implementation of java, which is included in many Linux
          distributions, currently lacks some classes and cannot run this
          web-application.</p>
      </div>
      <div class="subsection"><a name="Alternative_versions_of_Tomcat"></a>
        <h3>Alternative versions of Tomcat</h3>
        <p>You may use Tomcat 5.0 in place of Tomcat 5.5 provided that you also
          use Java 1.4.2 as noted above. Please remember that this limits the
          web-application's ability to handle large outputs.</p>
        <p>Tomcat 3 and Tomcat 4 are not suitable for this web-application.
          At the time of writing we have not tested the web-application with
          Tomcat 6.</p>
      </div>
      <div class="subsection"><a name="Alternative_web_containers"></a>
        <h3>Alternative web containers</h3>
        <p>You might use a web container other than Tomcat. We have not
          tested this web-application in any other containers and cannot
          support such a configuration. However, you might find one that
          works well.</p>
      </div>
    </div>
  </div>
</div>
<%@ include file="../inc/footer.jsp" %>
