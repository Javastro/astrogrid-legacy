/*
 * $Id: SelfMonitorBody.java,v 1.4 2004/10/05 18:05:02 mch Exp $
 */

package org.astrogrid.status;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Serves body-html (to include within the body html element) containing server
 * and tasks status. For example, include it in a JSP that has the correct
 * headers and footers for the application.
 *
 * @author mch
 */
public class SelfMonitorBody  {
   
   public static void writeHtmlStatus(Writer out, ServiceStatus status) throws IOException {
      out.write(
         "<p>Started: "+status.getStarted()+"</p>"+
            "<h3>Memory</h3>"+
            "<p>"+
            "Free: "+status.getFreeMemory()+"<br/>"+
            "Max:  "+status.getMaxMemory()+"<br/>"+
            "Total:"+status.getTotalMemory()+"<br/>"+
            "</p>"+
            "<h3>Tasks</h3>"+
            "<table>"+
            "<tr>"+
            "<th>Query</th>"+
            "<th>Started</th>"+
            "<th>Owner</th>"+
            "<th>State</th>"+
            "</tr>");
      
      TaskStatus[] qStatuses = status.getTasks();
      
      out.write("<p>Queries: "+ qStatuses.length+"</p>");
      
      for (int i=0;i<qStatuses.length;i++) {
         writeTaskStatusRow(out, qStatuses[i]);
      }
      
      out.write(
         "</table>"+
            "</p>"+
            "<h3>History</h3>"+
            "<p>"+
            "</p>");
      
   }
   
   /** Creates HTML for displaying a task in a row in a table */
   public static void writeTaskStatusRow(Writer out, TaskStatus task) throws IOException {
         String bg = "#FFFFFF"; //default to background white
         String fg = "#000000"; //default to foreground black
         if (task.getStage() == TaskStatus.ABORTED)  { bg = "#FFFFAA"; } //yellow/orange
         if (task.getStage() == TaskStatus.ERROR)    { bg = "#FFAAAA"; } //black on red
         if (task.getStage() == TaskStatus.RUNNING)  { bg = "#AAFFAA"; } //green
         if (task.getStage() == TaskStatus.COMPLETE) { bg = "#AAAAFF"; } //blue
         
         out.write("<tr bgcolor='"+bg+"' fgcolor='"+fg+"'>");
         out.write("<td><a href='queryStatus.jsp?ID="+task.getId()+"'>"+task.getId()+"</a></td>");
         out.write("<td>"+task.getStartTime()+"</td>");
         out.write("<td>"+task.getOwner()+"</td>");
         out.write("<td>"+task.getStage()+"</td>");
         out.write("</tr>");
   }
}

