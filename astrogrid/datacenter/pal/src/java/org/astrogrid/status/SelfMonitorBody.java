/*
 * $Id: SelfMonitorBody.java,v 1.1 2004/10/01 18:04:59 mch Exp $
 */

package org.astrogrid.status;

import java.io.StringWriter;

/**
 * Serves body-html (to include within the body html element) containing server
 * and tasks status. For example, include it in a JSP that has the correct
 * headers and footers for the application.
 *
 * @author mch
 */
public class SelfMonitorBody  {
   
   public static String getHtmlStatus(ServiceStatus status) {
      StringWriter out = new StringWriter();
      out.write(
         "<p>Started: "+status.getStarted()+"</p>"+
            "<h3>Memory</h3>"+
            "<p>"+
            "Free: "+Runtime.getRuntime().freeMemory()+"<br/>"+
            "Max:  "+Runtime.getRuntime().maxMemory()+"<br/>"+
            "Total:"+Runtime.getRuntime().totalMemory()+"<br/>"+
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
         String bg = "#FFFFFF"; //default to background white
         String fg = "#000000"; //default to foreground black
         if (qStatuses[i].getStage() == TaskStatus.ABORTED)  { bg = "#FF8800"; } //yellow/orange
         if (qStatuses[i].getStage() == TaskStatus.ERROR)    { bg = "#FF0000"; } //black on red
         if (qStatuses[i].getStage() == TaskStatus.COMPLETE) { bg = "#0000FF"; } //blue
         
         out.write("<tr bgcolor='"+bg+"' fgcolor='"+fg+"'>");
         out.write("<td><a href='queryStatus.jsp?ID="+qStatuses[i].getId()+"'>"+qStatuses[i].getId()+"</a></td>");
         out.write("<td>"+qStatuses[i].getStartTime()+"</td>");
         out.write("<td>"+qStatuses[i].getOwner()+"</td>");
         out.write("<td>"+qStatuses[i].getStage()+"</td>");
         out.write("</tr>");
      }
      
      out.write(
         "</table>"+
            "</p>"+
            "<h3>History</h3>"+
            "<p>"+
            "</p>");
      
      return out.toString();
   }
}
