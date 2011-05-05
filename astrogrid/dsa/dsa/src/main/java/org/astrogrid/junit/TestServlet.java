package org.astrogrid.junit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * A Servlet to run the JUnit tests in a user-specified class and to display
 * the results in HTML.
 *
 * @author Guy Rixon
 */
public class TestServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    response.setContentType("text/html");

    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("  <head>");
    out.println("    <title>Results of self-test</title>");
    out.println("    <style type='text/css'>");
    out.println("      .pass {background: #9f9;}");
    out.println("      .fail {background: #f99;}");
    out.println("    </style>");
    out.println("  </head>");
    out.println("  <body>");
    out.println("    <h1>Results of self-tests</h1>");

    try {
      String testClassName = request.getParameter("class");
      Class<?> testClass = Class.forName(testClassName);
      out.println("    <p>Test class is " + testClassName + "</p>");
      JUnitCore junit = new JUnitCore();
      Result result = junit.run(testClass);
      if (result.wasSuccessful()) {
        out.println("    <p class='pass'>Overall: PASS</p>");
      }
      else {
        out.println("    <p class='fail'>Overall: FAIL</p>");
        out.println("    <p>Failed tests:</p>");
        out.println("    <dl>");
        List<Failure> failures = result.getFailures();
        for (Failure f : failures) {
          out.println("      <dt class='fail'>" + f.getDescription() + "</dt>");
          out.println("      <dd>" + f.getMessage() + "</dd>");
          out.println("      <dd><pre>" + f.getTrace() + "</pre></dd>");
        }
        out.println("    </dl>");
      }
    }
    catch(Exception e) {
      out.println("    <pre>");
      out.println(e);
      out.println("    </pre>");
    }

    out.println("  </body>");
    out.println("</html>");
    out.close();
  }

}
