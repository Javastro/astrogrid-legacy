/*
 * $Id: ServerStatusHtmlRenderer.java,v 1.2 2006/08/21 15:39:30 clq2 Exp $
 */

package org.astrogrid.monitor;

import java.io.IOException;
import java.io.Writer;
import org.astrogrid.status.ServiceStatus;
import org.astrogrid.status.TaskStatus;

/**
 * Serves body-html (to include within the body html element) containing server
 * and tasks status. For example, include it in a JSP that has the correct
 * headers and footers for the application.
 *
 * @author mch
 */
public class ServerStatusHtmlRenderer   {
   
   public void writeHtmlStatus(Writer out, ServiceStatus status, TaskStatus[] persistedStatus) throws IOException {
      out.write(
         "<p>Started: "+status.getStarted()+"</p>"+
            "<h2>Memory</h2>"+
            "<p><table>"+
            "<tr><td align='right'>Free<td><td align='right'>"+status.getFreeMemory()+"</td></tr>"+
            "<tr><td align='right'>Max<td><td align='right'>"+status.getMaxMemory()+"</td></tr>"+
            "<tr><td align='right'>Total<td><td align='right'>"+status.getTotalMemory()+"</td></tr>"+
            "</table></p>");

      TaskStatus[] tasks = status.getTasks();
   
      out.write(
            "<h2>Tasks ("+(tasks.length)+")</h2>\n"+
            "<table>");
      
      if (tasks.length>0) {
   
         out.write(
               "<tr>"+
               "<th>Query</th>"+
               "<th>Started</th>"+
               "<th>Time</th>"+
               "<th>Owner</th>"+
               "<th>State</th>"+
               "<th>Size</th>"+
               "<th>Source</th>"+
               "</tr>\n");
         
         for (int i=0;i<tasks.length;i++) {
            writeTaskStatusRow(out, tasks[i]);
         }
         
         out.write(
            "</table></p>");
      }
   
      if ((persistedStatus != null) && (persistedStatus.length>0)) {
         //historical - persisted IDs.  These may not have much information
         out.write(
               "<h2>History</h2>\n"+
               "<table>"+
               "<tr>"+
               "<th>Query</th>"+
               "<th>Started</th>"+
               "<th>Time</th>"+
               "<th>Owner</th>"+
               "<th>State</th>"+
               "<th>Size</th>"+
               "<th>Source</th>"+
               "</tr>\n");
   
         //go backwards so we get latest first
         for (int i = persistedStatus.length-1; i >=0 ; i--)
         {
            writeTaskStatusRow(out, persistedStatus[i]);
         }
         
         out.write("</table></p>");
         
         out.flush();
      }
   }
   
   /** Creates HTML for displaying a task in a row in a table */
   public void writeTaskStatusRow(Writer out, TaskStatus task) throws IOException {
         String bg = "#FFFFFF"; //default to background white
         String fg = "#000000"; //default to foreground black
         if (task.getStage().equals(TaskStatus.ABORTED))  { bg = "#FFFFAA"; } //yellow/orange
         if (task.getStage().equals(TaskStatus.ERROR))    { bg = "#FFAAAA"; } //black on red
         if (task.getStage().equals(TaskStatus.RUNNING))  { bg = "#AAFFAA"; } //green
         if (task.getStage().equals(TaskStatus.COMPLETE)) { bg = "#AAAAFF"; } //blue
         
         
         out.write("<tr bgcolor='"+bg+"' fgcolor='"+fg+"'>");
         out.write("<td><a href='queryStatus.jsp?ID="+task.getId()+"'>"+task.getId()+"</a></td>");
         out.write("<td nowrap>"+task.getFirst().getTimestamp()+"</td>");
         if (task.getFirst() == task) {
            out.write("<td>Unknown</td>");
         }
         else {
            long timeTaken = task.getTimestamp().getTime()-task.getFirst().getTimestamp().getTime();
            int s = (int) timeTaken/1000;
            int m = (int) s/60;
            int h = (int) m/60;
            out.write("<td nowrap>"+h+":"+padZero(m % 60,2) +":" + padZero(s % 60,2)+"."+padZero(timeTaken % 1000,3) +"</td>");
         }
         out.write("<td nowrap>"+task.getOwner().getName()+"</td>");
         out.write("<td>"+task.getStage()+"</td>");
         
         //number of results
         if (task.getProgressMax()>0) {
            out.write("<td>"+task.getProgressMax()+"</td>");
         }
         else {
            out.write("<td></td>");
         }
         
         //source
         out.write("<td nowrap>"+emptyNuller(task.getSource())+"</td>");

         //add abort button if it's still running
         if (!task.isFinished()) {
            out.write("<td class='button'><a href='./AttemptAbort?ID="+task.getId()+"'>Abort</a></td>");
         }
         out.write("</tr>\n");
   }
   
   private String padZero(long i, int digits) {
      String s = "00000000000"+i;
      return s.substring(s.length()-digits);
   }

   private String emptyNuller(String s) {
      if (s==null) {
         return "";
      }
      else {
         return s;
      }
   }
   
}

