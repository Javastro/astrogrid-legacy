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
	   harvestInterval = conf.getInt(INTERVAL_HOURS_PROPERTY);
	   System.out.println("Servlet starting - harvest interval = " + harvestInterval + "hours");
	   servletInitTime = new Date();
	   rhs = new RegistryHarvestService();

	   Thread myThread = new Thread(this);
	   myThread.start();
   }

   public void doGet(HttpServletRequest req, HttpServletResponse res)
                throws IOException, ServletException
   {
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
                  "</h1></body></html>");
   }

   public void run()
   {
	  while(true) {
         myCounter++;
         lastHarvestTime = new Date();

         //try {
            
            /*
	     }
         
	     catch (AxisFault e)
	     {
			 e.printStackTrace();
	     }
         */

 		 try{
            rhs.harvestAll(false,true);
//            myThread.sleep(10000);
            myThread.sleep(harvestInterval*3600*1000);
	     }
	     catch(InterruptedException e)
	     {
			 e.printStackTrace();
	     }catch(RegistryException re) {
	         re.printStackTrace();
        }
         //System.out.println("Still looping in run() method! ...waiting 10 secs");
     }
   }

}


