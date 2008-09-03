<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Command-line-application server - Self-testing</title>
<%@ include file="../inc/header.jsp" %>
<div id="bodyColumn">
  <div class="contentBox">
    <div class="section"><a name="Self-testing"></a>
      <h2>Self-testing</h2>
      <p> The CEC web-application can test its own installation and most of its configuration before
        it is registered. Please run through the following tests. </p>
      <div class="subsection"><a name="Testing_Axis"></a>
        <h3>Testing Axis</h3>
        <p> The CEC contains the Apache Axis technology for web services. If Axis is not working,
          then the CEC does not work. You should test Axis as soon as you have the CEC installed. </p>
        <p>Run the Axis tests using the "axis check" command from the left-hand side-bar. 
          The test checks for the presence of JARs of code. You need all the core libraries to be
          present; the optional libraries are not required. </p>
        <p> If Axis is not happy, there are three possibilities. </p>
        <ul>
          <li>The WAR for the web-application may be faulty. Please report a bug to AstroGrid.</li>
          <li>You may have Axis-related parts installed in your copy of Tomcat. Check the
            common directories of Tomcat for any JARs with similar names to those in the CEC
            web-application.</li>
          <li>Your copy of Java may be broken or may be inapproriate for the CEC. Please make sure 
            that you are using Java 1.4.x and not Java 1.5.x. Check for any JAR files added to the 
            Java installation that may be subverting the web-application.</li>
        </ul>
      </div>
      <div class="subsection"><a name="Testing_the_installations"></a>
        <h3>Testing the installations</h3>
        <p> When you have the CEC configured you can test the configuration using the <i>installation test</i> link in the left-hand side-bar. </p>
        <p> If you get a test-failure here then either your configuration is not quite right (likely)
          or the WAR that you downloaded from AstroGrid has a bug (less likely but worth considering).
          If you can't see a problem in the configuration then please contact AstroGrid for support. </p>
      </div>
      <div class="subsection"><a name="Test-running_applications"></a>
        <h3>Test-running applications</h3>
        <p> When all is configured, use the "test run application" link from the sidebar. This lets you
          invoke the configured applications locally. They should all work. However, any applications
          that produce big output-files won't work gracefully as they will return the output to the
          browser, not to VOSpace. </p>
        <p> If you get a test-failure here, then the problem could be in your configuration or could be
          a bug in the WAR you downloaded from AstroGrid. The problem could also be in the application
          itself. Try calling the application directly from the command line to see if it's working. </p>
      </div>
    </div>
  </div>
</div>
<%@ include file="../inc/footer.jsp" %>
