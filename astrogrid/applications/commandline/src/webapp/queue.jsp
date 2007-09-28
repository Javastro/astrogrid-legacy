<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Iterator,
                org.astrogrid.applications.Application,
                org.astrogrid.applications.component.CEAComponentManagerFactory,
                org.astrogrid.applications.commandline.QueuedJobList"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Queue of CEA jobs</title>
    </head>
    <body>

    <h1>Queue of CEA jobs</h1>
    <table border="1" >
      <thead>
        <tr>
          <th>Job name</th>
          <th>Application name</th>
          <th>State</th>
          <th>Start</th>
          <th>End</th>
          <th>Deadline</th>
          <th/>
         </tr> 
      </thead>
      <tbody>
        <%
        QueuedJobList qjl =(QueuedJobList) CEAComponentManagerFactory.getInstance().getExecutionController();
        Iterator i = qjl.getQueueAsList().iterator();
        while (i.hasNext()) {
	      Application app = (Application)i.next();
	%>
	<tr>
          <td><%=app.getID()%></td>
          <td><%=app.getApplicationDescription().getName()%></td>
	  <td><%=app.getStatus()%></td>
          <td><%=app.getStartInstant()%></td>
          <td><%=app.getEndInstant()%></td>
          <td><%=app.getDeadline()%></td>
          <td>
            <form action="abort" method="post">
              <input type="hidden" name="job" value="<%=app.getID()%>"/>
              <input type="submit" value="Abort"/>
            </form>
          </td>
        </tr>
	<%}%>
      </tbody>
    </table>
    </body>
</html>