package astrogrid.registry.harvest.daemon;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


import org.astrogrid.config.Config;
import org.astrogrid.registry.server.harvest.RegistryHarvestService;
import org.astrogrid.registry.RegistryException;
//import org.apache.axis.AxisFault;


public class HarvestDaemon extends HttpServlet //implements Runnable
{
   //Thread myThread;
   int myCounter;
   int harvestInterval;
   Date lastHarvestTime;
   Date servletInitTime;
   RegistryHarvestService rhs;
   boolean harvestOnLoad = false;
   boolean valuesSet = false;
   public static boolean harvestStarted;
   
   private ServletContext context = null;   

   public static final String INTERVAL_HOURS_PROPERTY =
       "reg.custom.harvest.interval_hours";
   public static Config conf = null;
   private Timer timer = null;
   private Timer scheduleTimer = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         harvestStarted = false;
      }
   }
   

   public void destroy() {
       super.destroy();
       System.out.println("exiting HarvestDaemon; trying to destroy timers");
       if(timer != null) {
           timer.cancel();
       }//if
       if(scheduleTimer != null) {
           scheduleTimer.cancel();
       }
   }
   
   public void init(ServletConfig config)
                throws ServletException
   {
	    super.init(config);
      
       System.out.println("initialized HarvestDaemon");
       
       servletInitTime = new Date();
       rhs = new RegistryHarvestService();
       if(scheduleTimer != null) {
           //hmmm just in case the servlet container decides to re-initialize
           //this servlet.
           scheduleTimer.cancel();
           scheduleTimer = null;
       }
       scheduleTimer = new Timer();
       boolean harvestEnabled = conf.getBoolean("reg.amend.harvest",false);
       if(harvestEnabled) {
          if(!valuesSet) {
              System.out.println("harvest is enabled");
              valuesSet = true;
              harvestInterval = conf.getInt(INTERVAL_HOURS_PROPERTY);
              //lets not start a harvest off the bat, wait till next cycle.
              harvestOnLoad = false;
              if(harvestInterval <= 0) {
                  System.out.println("ERROR CANNOT HAVE A HARVESTINTERVAL LESS THAN 1; DEFAULTING TO 2");
                  harvestInterval = 2;
              }
          }
          System.out.println("in init of harvestDaemon and starting thread.");
          
          scheduleTimer.scheduleAtFixedRate(new HarvestTimer("HarvestNow"),
                       harvestInterval*3600*1000,
                       harvestInterval*3600*1000);          
          //Thread myThread = new Thread(this);
          //myThread.start();
       }else {
           System.out.println("harvest not enabled.");
           //if harvest is not enabled then just leave.
           return;
       }//else
      
   }
   

   public void doGet(HttpServletRequest req, HttpServletResponse res)
                throws IOException, ServletException
   {
	  String nowParam = req.getParameter("HarvestNow");
     if(timer != null) {
         //hmmm just in case the servlet container decides to re-initialize
         //this servlet.
         timer.cancel();
         timer = null;
     }
     timer = new Timer();
     
	  if( nowParam != null && !harvestStarted ) {
        //start the harvest in 5 seconds
        timer.schedule(new HarvestTimer("HarvestNow"),5000);
        harvestStarted = true;
      }
     String replicate = req.getParameter("ReplicateNow");
      if( replicate != null && !harvestStarted ) {
         //start a replication in 5 seconds
         timer.schedule(new HarvestTimer("ReplicateNow"),5000);
         harvestStarted = true;         
       }
      
      
      ServletOutputStream out = res.getOutputStream();
      res.setContentType("text/html");
      out.println("<html><head><title>Astrogrid Registry Harvest</title></head>");
      out.println("<body><a href=\"http://www.astrogrid.org\">");
      out.println("<img src=\"http://www.astrogrid.org/image/AGlogo\"" +
                  "align=right alt=\"AG logo\"> </a>");
      out.println("<h1>Astrogrid Registry Harvest Control Servlet</h1>" +
                  "<br><h2>Servlet initialized " + servletInitTime +
                  "<br>Harvest interval = " + harvestInterval + " hours" +
                  "<br>Number of Harvests initiated = " + myCounter +
                  "<br>Last harvest time = " + lastHarvestTime +
                  "<form method=\"get\"><input type=\"submit\" " +
                  " name=\"HarvestNow\" value=\"Harvest now!\">" +
                  "<br /><strong>Replication can be dangerous replicates all resources not by date</strong><br />" +
                  "<input type=\"submit\" name=\"ReplicateNow\" value=\"Replicate Now\" /></form>");
                  if(harvestStarted) {
                      out.println("<br /><i>A Harvest/Replicate has began or still running.</i>");
                  }
                  out.println("</h2></body></html>");
   }

   class HarvestTimer extends TimerTask {
       private String task = null;
       
       public HarvestTimer(String harvestTask) {
           task = harvestTask;
       }
       
       public void run() {

           try {
               if("HarvestNow".equals(task)) {
                   System.out.println("Immediate harvesting will be commenced!");
                   try {
                       rhs.harvestAll(true,true);
                   }catch(RegistryException e) {
                        e.printStackTrace();
                   }
               }
               if("ReplicateNow".equals(task)) {
                   System.out.println("Immediate replicate is beginning!");
                   try {
                      rhs.harvestAll(true,false);
                   }catch(RegistryException e) {
                      e.printStackTrace();
                   }
               }//if
           } finally {
               //turn off
               HarvestDaemon.harvestStarted = false;
           }
       }//run       
   }

   
   
}