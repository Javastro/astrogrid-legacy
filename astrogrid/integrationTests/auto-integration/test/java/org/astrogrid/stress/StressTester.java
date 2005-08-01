package org.astrogrid.stress;

import javax.xml.parsers.*;

import org.astrogrid.community.User;
import org.astrogrid.store.Ivorn;
import org.xml.sax.*;
import java.util.ArrayList;
import junit.framework.*;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.Timer;
import java.io.FileOutputStream;
import java.io.File;
import net.sourceforge.groboutils.junit.v1.IntegrationTestCase;
import java.util.Vector;
import org.astrogrid.integration.AbstractTestForIntegration;
import java.io.InputStream;

public class StressTester extends AbstractTestForIntegration {

	private String configFile = null;
	private ConfigFileReader cfr = new ConfigFileReader();
	private FileOutputStream fos = null;
	private final String SYNCHRONEOUS_STYLE = "synchroneous";
	private final String SEQUENTIAL_STYLE = "sequential";
   public static Vector resultInfo = null;
   public static int threadsgoing;
	   
	public StressTester(String arg0, String configFile) {
      super(arg0);
		this.configFile = configFile;
      System.out.println("the configFile = " + configFile);
      if(resultInfo == null) {
          resultInfo = new Vector();
      }//if
      resultInfo.clear();
      threadsgoing = 0;
	}
    
   protected void setUp() throws Exception {
       super.setUp();
       System.out.println("enter setup of StressTester");
       setupAndParseXML(configFile);
       setupThreadsAndSchedule();
       System.out.println("exit setup of StressTester");
   }
	
   /*
    * Parse the xml into our Handler which creates the necessary object arrays for looping
    * through later to start the threads.
    */
	private void setupAndParseXML(String configFile) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		//spf.setValidating(true);
		XMLReader xmlReader = null;
		try {
			SAXParser saxParser = spf.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(cfr);
			xmlReader.setErrorHandler(new XMLErrorHandler());
			xmlReader.parse(new InputSource(this.getClass().getResourceAsStream(configFile)));
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
    /*
     * Okay this is the main big thread that loops through all the 
     * Tests, and there various TestCases and Threads kicking off threads to execute the
     * junit tests.
     */
	private void setupThreadsAndSchedule() {
      System.out.println("enter setupThreadsAndSchedule");
		ArrayList webServices = cfr.getWebServices();
		//System.out.println("The size of webservices = " + webServices.size());
		int iActiveThreads = 0;
		Timer timer;

      //okay set a time for 5 seconds in the future.
		Calendar timerTime = Calendar.getInstance();
		timerTime.add(Calendar.SECOND,5);
      //get WebServiceTest known as ServiceTest object.
		for(int i = 0;i < webServices.size();i++){
			ServiceTest st = (ServiceTest)webServices.get(i);
			//System.out.println("The toString = " + st.toString());			
			ArrayList stcList = st.getServiceTestCases();
         //get its test cases.
			for(int j = 0;j < stcList.size();j++) {
				ServiceTestCase stc = (ServiceTestCase)stcList.get(j);
				ArrayList threadList = stc.getTestCaseThreads();
            //now call the threads as many times as defined for a loop of the
            //TestCase element.
				for(int r = 0;r < stc.getLoop();r++) {
               //go through all the Thread elements for this TestCase.
					for(int k = 0;k < threadList.size();k++) {
						TestCaseThreads tct = (TestCaseThreads)threadList.get(k);
						//System.out.println("The before activecount = " + Thread.activeCount());
                  //this is the number of threads to run. (the number attribute)
						for(int m = 0; m < tct.getConCurrentThreads();m++) {
                        //increase the thread count.
                        threadsgoing++;
                        //hmmm probably could move this instantiation above, oh well leave it for now.
								timer = new Timer();
                        //Now schedule the execution.
                        //All of it should happen roughly 5 seconds in the future.
                        //that gives plenty of time for this for loop to go through and 
                        //schedule the amount of threads.
								timer.schedule(new RunWebServiceTask(st.getName(),
                                        st.getFullClassName(),
                                        stc.getName(),
                                        stc.getLoop(),
                                        tct.getLoop(),
                                        tct.getConCurrentThreads()),
                                        timerTime.getTime());
								//System.out.println("schedule number = " + m);
						}//for
                        
                  //Okay was this threadstyle sequential if so then sleep till all threads are finished.
						if(tct.getThreadStyle().equals(SEQUENTIAL_STYLE)) {
							//while( (iActiveThreads = Thread.activeCount()) > normalThreadCount) {
                      while(threadsgoing > 0) {
								try {
                           System.out.println("a. the threads currently going (Will sleep for 5 seconds if there are more than 0) = " + threadsgoing);
									Thread.sleep(5000);
								}catch(Exception e1){System.out.println(e1.toString());}
							}//while
                     //Okay everything done for this round set a new timer for 5 seconds in the future.
							timerTime = Calendar.getInstance();
							timerTime.add(Calendar.SECOND,5);
						}//if
					}//for
               //check if this Servicetest (TestCase) element is sequential
               //if so sleep for 5 more seconds. This is in case there are many
               //TestCases running at the same time stop at a sequential till everything is complete.
					if(stc.getThreadStyle().equals(SEQUENTIAL_STYLE)) {
						//while( (iActiveThreads = Thread.activeCount()) > normalThreadCount) {
                  while(threadsgoing > 0) {                        
							try {
                        System.out.println("b. the threads currently going (Will sleep for 5 seconds if there are more than 0) = " + threadsgoing);
								Thread.sleep(5000);                        
							}catch(Exception e2){System.out.println(e2.toString());}
						}//while
                  //okay everything is complete so set the timer for 5 more seconds into the future.
						timerTime = Calendar.getInstance();
						timerTime.add(Calendar.SECOND,5);
					}//if
				}//for				
			}//for
         //check if this whole web service test is a sequential if so then 
         //stop till everything is complete.
			if(st.getThreadStyle().equals(SEQUENTIAL_STYLE)) {
				//while( (iActiveThreads = Thread.activeCount()) > normalThreadCount) {
             while(threadsgoing > 0) {                
					try {
						//System.out.println("the active threadcount = " + iActiveThreads);
                  System.out.println("c. the threads currently going (Will sleep for 5 seconds if there are more than 0) = " + threadsgoing);
						Thread.sleep(5000);
					}catch(Exception e2){System.out.println(e2.toString());}
				}//while
				timerTime = Calendar.getInstance();
				timerTime.add(Calendar.SECOND,5);
			}				
		}//for
      System.out.println("writing vector info size = " + resultInfo.size());
      writeVector();
      System.out.println("exist setupThreadsAndSchedule");
	}
    
  private void writeVector() {
      System.out.println("Remember the time is how long it took to run the whole Junit Test class and all there test method(s).  NOT individual test methods.");
      for(int i = 0;i < resultInfo.size();i++) {
          System.out.println(resultInfo.get(i));
      }//for
  }//writeVector
    
   
	
	public static void main(String []args) {
		if(args.length > 0) {
			new StressTester("dummy", args[0]);			
		}else {			
			System.out.println("Error -- Must provide config file.");
		}
	}
	
}

class RunWebServiceTask extends TimerTask {
	
	private String testName = null;
	private String fullClassName = null;
	private String methodTestName = null;
	int loopCount;
	int threadLoopCount;
	int threadNumber;
	FileOutputStream fos;
	String fileName;
	
	public RunWebServiceTask(String testName, 
                            String fullClassName,
                            String methodTestName,
                            int loopCount,
                            int threadLoopCount,
                            int threadNumber) {
		this.testName = testName;
		this.fullClassName = fullClassName;
		this.methodTestName = methodTestName;
		this.loopCount = loopCount;
		this.threadLoopCount = threadLoopCount;
		this.threadNumber = threadNumber;
	}
	
	public void run() {
		TestSuite su = new TestSuite();
		long start = 0;
		long end = 0;
		try {
			Class testClass = Class.forName(fullClassName);
			su.addTestSuite(testClass);
			start = System.currentTimeMillis();
			for(int i = 0;i < this.threadLoopCount;i++) {
				junit.textui.TestRunner.run(su);
			}
			end = System.currentTimeMillis();
			writeEntry((end-start));
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	private synchronized void writeEntry(long duration) {
		String entry = testName + "-" + methodTestName + "-" + threadNumber + "-" + loopCount+",";
      StressTester.resultInfo.add(entry + " " + String.valueOf(duration) + "ms");
      StressTester.threadsgoing--;
	}
	
	public String toString() {
		String s = "";
		s += "Test Name = " + this.testName + "\n" + "FullClassName = " + this.fullClassName + "MethodTestName" + this.methodTestName + "\nLoopCount = " + this.loopCount + "\n";
		return s;
	}
}