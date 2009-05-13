/*
 * $Id: ServerStatusHtmlRenderer.java,v 1.1.1.1 2009/05/13 13:20:37 gtr Exp $
 */

package org.astrogrid.monitor;

import java.io.IOException;
import java.io.Writer;
import org.astrogrid.status.ServiceStatus;
import org.astrogrid.status.TaskStatus;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Serves body-html (to include within the body html element) containing server
 * and tasks status. For example, include it in a JSP that has the correct
 * headers and footers for the application.
 *
 * @author mch
 */
public class ServerStatusHtmlRenderer   {
   
   public void writeHtmlStatus(Writer out, ServiceStatus status, TaskStatus[] persistedStatus) throws IOException {
      Date now = new Date();
      GregorianCalendar nowCal = new GregorianCalendar();
      nowCal.setTime(now);
      long currentTime = nowCal.getTimeInMillis();

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
               "<th>Queue time</th>"+
               "<th>Execution time</th>"+
               "<th>Total time</th>"+
               "<th>Owner</th>"+
               "<th>State</th>"+
               "<th>Size</th>"+
               "<th>Source</th>"+
               "</tr>\n");
         
         for (int i=0;i<tasks.length;i++) {
            writeTaskStatusRow(out, tasks[i],currentTime);
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
            writeTaskStatusRow(out, persistedStatus[i],currentTime);
         }
         
         out.write("</table></p>");
         
         out.flush();
      }
      out.flush();
   }
   
   /** Creates HTML for displaying a task in a row in a table */
   public void writeTaskStatusRow(Writer out, TaskStatus task, long currentTime) throws IOException {
         String bg = "#EEEEEE"; //default to background grey
         String fg = "#000000"; //default to foreground black
         if (task.getStage().equals(TaskStatus.ABORTED))  { bg = "#FFFFAA"; } //yellow/orange
         if (task.getStage().equals(TaskStatus.ERROR))    { bg = "#FFAAAA"; } //black on red
         if (task.getStage().equals(TaskStatus.RUNNING))  { bg = "#AAFFAA"; } //green
         if (task.getStage().equals(TaskStatus.COMPLETE)) { bg = "#AAAAFF"; } //blue
         
         
         out.write("<tr bgcolor='"+bg+"' fgcolor='"+fg+"'>");
         out.write("<td><a href='queryStatus.jsp?ID="+task.getId()+"'>"+task.getId()+"</a></td>");
         out.write("<td nowrap>"+task.getFirst().getTimestamp()+"</td>");

         /*
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
         */
         getTimeColumns(out, task,currentTime);
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

   private void getTimeColumns(Writer out, TaskStatus task, long nowTime) throws IOException
   {
      String html = "";
      if (task.getFirst() == task) {
         out.write("<td align=\"center\">-</td>");
         out.write("<td align=\"center\">-</td>");
         out.write("<td align=\"center\">-</td>");
      }
      else {
         long begunTime = task.getFirst().getTimestamp().getTime();
         long initialisedTime = 0;      /* INITIALISED */
         long queuedTime = 0;    /* PUT IN QUEUE */
         long runningTime = 0;   /* REMOVED FROM QUEUE AND STARTED */
         long completeTime = 0;

         while (task != null) {
            String stage = task.getStage();
            if (TaskStatus.COMPLETE.equals(stage)) {
               completeTime = task.getTimestamp().getTime();
            }
            else if (TaskStatus.RUNNING.equals(stage)) {
               runningTime = task.getTimestamp().getTime();
            }
            else if (TaskStatus.QUEUED.equals(stage)) {
               queuedTime = task.getTimestamp().getTime();
            }
            else if (TaskStatus.INITIALISED.equals(stage)) {
               initialisedTime = task.getTimestamp().getTime();
            }
            task = task.getPrevious();
         }
         /* Queue time = started - queued */
         if ((queuedTime != 0) && (runningTime != 0)) {
            long timeTaken = runningTime - queuedTime;
            int s = (int) timeTaken/1000;
            int m = (int) s/60;
            int h = (int) m/60;
            out.write("<td nowrap>"+h+":"+padZero(m % 60,2) +":" + padZero(s % 60,2)+"."+padZero(timeTaken % 1000,2) +"</td>");
         }
         else if (queuedTime != 0) {
            long timeTaken = nowTime - queuedTime;
            int s = (int) timeTaken/1000;
            int m = (int) s/60;
            int h = (int) m/60;
            out.write("<td nowrap>"+h+":"+padZero(m % 60,2) +":" + padZero(s % 60,2)+"."+padZero(timeTaken % 1000,2) +"</td>");
         }
         else {
            out.write("<td align=\"center\">-</td>");
         }
         /* Exec time = finished - started  */
         if ((runningTime != 0) && (completeTime != 0)) {
            long timeTaken = completeTime - runningTime;
            int s = (int) timeTaken/1000;
            int m = (int) s/60;
            int h = (int) m/60;
            out.write("<td nowrap>"+h+":"+padZero(m % 60,2) +":" + padZero(s % 60,2)+"."+padZero(timeTaken % 1000,2) +"</td>");
         }
         else if (runningTime != 0) {
            long timeTaken = nowTime - runningTime;
            int s = (int) timeTaken/1000;
            int m = (int) s/60;
            int h = (int) m/60;
            out.write("<td nowrap>"+h+":"+padZero(m % 60,2) +":" + padZero(s % 60,2)+"."+padZero(timeTaken % 1000,2) +"</td>");
         }
         else {
            out.write("<td align=\"center\">-</td>");
         }
         /* Total time = finished - begun  */
         if ((begunTime != 0) && (completeTime != 0)) {
            long timeTaken = completeTime - begunTime;
            int s = (int) timeTaken/1000;
            int m = (int) s/60;
            int h = (int) m/60;
            out.write("<td nowrap>"+h+":"+padZero(m % 60,2) +":" + padZero(s % 60,2)+"."+padZero(timeTaken % 1000,2) +"</td>");
         }
         else if (begunTime != 0) {
            long timeTaken = nowTime - begunTime;
            int s = (int) timeTaken/1000;
            int m = (int) s/60;
            int h = (int) m/60;
            out.write("<td nowrap>"+h+":"+padZero(m % 60,2) +":" + padZero(s % 60,2)+"."+padZero(timeTaken % 1000,2) +"</td>");
         }
         else {
            out.write("<td align=\"center\">-</td>");
         }
      }
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

