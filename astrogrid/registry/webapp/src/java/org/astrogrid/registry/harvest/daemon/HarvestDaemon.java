package astrogrid.registry.harvest.daemon;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


import org.astrogrid.config.Config;
import org.astrogrid.registry.server.harvest.RegistryHarvestService;
import org.astrogrid.registry.RegistryException;
//import org.apache.axis.AxisFault;


public class HarvestDaemon extends HttpServlet implements Runnable
{
   Thread myThread;
   int myCounter;
   int harvestInterval;
   Date lastHarvestTime;
   Date servletInitTime;
   RegistryHarvestService rhs;
   boolean harvestOnLoad = false;
   boolean valuesSet = false;
   
   private ServletContext context = null;   

   public static final String INTERVAL_HOURS_PROPERTY =
       "org.astrogrid.registry.harvest.daemon.interval-hours";
   public static Config conf = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }
   }
   
   public void init(ServletConfig config)
                throws ServletException
   {
	   super.init(config);
      
      /*
       System.out.println("in init of yielding before sleept");
      try {
          Thread.sleep(60 * 1000);          
      }catch(InterruptedException e) {
          e.printStackTrace();
      }
      Thread.yield();
      System.out.println("finished yielding in init");      
      */
      
       System.out.println("in init of harvestDaemon and staring thread.");
       servletInitTime = new Date();
       rhs = new RegistryHarvestService();
       Thread myThread = new Thread(this);
       myThread.start();
      
      
/*      
      boolean harvestEnabled = conf.getBoolean("registry.harvest.enabled",false);
      harvestOnLoad = conf.getBoolean("registry.harvest.onload",false);
      if(harvestEnabled) {
    	   harvestInterval = conf.getInt(INTERVAL_HOURS_PROPERTY);
         if(harvestInterval <= 0) {
             System.out.println("ERROR CANNOT HAVE A HARVESTINTERVAL LESS THAN 1; DEFAULTING TO 1");
             harvestInterval = 1;
         }
    	   System.out.println("Servlet starting - harvest interval = " + harvestInterval + "hours");
    	   servletInitTime = new Date();
    	   rhs = new RegistryHarvestService();
    
    	   Thread myThread = new Thread(this);
    	   myThread.start();
      }//if
*/
       
   }

   public void doGet(HttpServletRequest req, HttpServletResponse res)
                throws IOException, ServletException
   {
	  String nowParam = req.getParameter("HarvestNow");
	  if( nowParam != null ) {
	     System.out.println("Immediate harvesting will be commenced!");
	     try {
	        rhs.harvestAll(true,true);
	     }
	     catch(RegistryException e)
	     {
		    e.printStackTrace();
         }
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
                  " name=\"HarvestNow\" value=\"Harvest now!\"></form>" +
                  "</h1></body></html>");
   }

   public void run()
   {
       
       boolean harvestEnabled = conf.getBoolean("registry.harvest.enabled",false);
       if(harvestEnabled) {
          if(!valuesSet) {
              System.out.println("harvest is enabled");
              valuesSet = true;
              harvestInterval = conf.getInt(INTERVAL_HOURS_PROPERTY);
              harvestOnLoad = conf.getBoolean("registry.harvest.onload",false);          
              if(harvestInterval <= 0) {
                  System.out.println("ERROR CANNOT HAVE A HARVESTINTERVAL LESS THAN 1; DEFAULTING TO 1");
                  harvestInterval = 1;
              }
          }
       }else {
           System.out.println("harvest not enabled.");
           //if harvest is not enabled then just leave.
           return;
       }
            
	  while(true) {
         
         lastHarvestTime = new Date();

         if(harvestOnLoad) {
           try {
                rhs.harvestAll(true,true);
                myCounter++;
    	     }
           catch (RegistryException e)
           {
               e.printStackTrace();
           }
         }//if
         harvestOnLoad = true;

 		 try{
            myThread.sleep(harvestInterval*3600*1000);
	     }
	     catch(InterruptedException e)
	     {
			 e.printStackTrace();
	     }
         //System.out.println("Still looping in run() method! ...waiting 10 secs");
     }//while
   }

}


