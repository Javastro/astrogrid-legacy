package org.tools.wstool;

import javax.xml.parsers.*;
import org.xml.sax.*;
import java.util.ArrayList;
import junit.framework.*;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.Timer;
import java.io.FileOutputStream;
import java.io.File;



public class WSTester {

	private String configFile = null;
	private ConfigFileReader cfr = new ConfigFileReader();
	private FileOutputStream fos = null;
	private final String SYNCHRONEOUS_STYLE = "synchroneous";
	private final String SEQUENTIAL_STYLE = "sequential";
	private String fileName = null;	
	
	public WSTester(String configFile) {
		this.configFile = configFile;
		String fileRef = "WSTest-" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".txt";
		try {
			File fi = new File(fileRef);
			fi.createNewFile();
			this.fileName = fileRef;
		}catch(Exception e){
			e.printStackTrace();
		}
		setupAndParseXML(configFile);
		setupThreadsAndSchedule();
	}
	
	private void setupAndParseXML(String configFile) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setValidating(true);
		XMLReader xmlReader = null;
		try {
			SAXParser saxParser = spf.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(cfr);
			xmlReader.setErrorHandler(new XMLErrorHandler());
			xmlReader.parse(configFile);
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	private void setupThreadsAndSchedule() {
		ArrayList webServices = cfr.getWebServices();
		//System.out.println("The size of webservices = " + webServices.size());
		int iActiveThreads = 0;
		Timer timer;

		Calendar timerTime = Calendar.getInstance();
		timerTime.add(Calendar.SECOND,5);
		for(int i = 0;i < webServices.size();i++){
			ServiceTest st = (ServiceTest)webServices.get(i);
			//System.out.println("The toString = " + st.toString());			
			ArrayList stcList = st.getServiceTestCases();
			for(int j = 0;j < stcList.size();j++) {
				ServiceTestCase stc = (ServiceTestCase)stcList.get(j);
				ArrayList threadList = stc.getTestCaseThreads();
				for(int r = 0;r < stc.getLoop();r++) {
					for(int k = 0;k < threadList.size();k++) {
						TestCaseThreads tct = (TestCaseThreads)threadList.get(k);
						//System.out.println("The before activecount = " + Thread.activeCount());
						for(int m = 0; m < tct.getConCurrentThreads();m++) {
								timer = new Timer();
								timer.schedule(new RunWebServiceTask(this.fileName,st.getName(),st.getFullClassName(),stc.getName(),stc.getLoop(),tct.getLoop(),tct.getConCurrentThreads()),timerTime.getTime());
								//System.out.println("schedule number = " + m);
						}//for
						//System.out.println("The after activecount = " + Thread.activeCount());
						if(tct.getThreadStyle().equals(SEQUENTIAL_STYLE)) {
							while( (iActiveThreads = Thread.activeCount()) > 2) {
								try {
									//System.out.println("the active threadcount = " + iActiveThreads);
									Thread.sleep(5000);
								}catch(Exception e1){System.out.println(e1.toString());}
							}//while
							timerTime = Calendar.getInstance();
							timerTime.add(Calendar.SECOND,5);
						}//if
					}//for
					if(stc.getThreadStyle().equals(SEQUENTIAL_STYLE)) {
						while( (iActiveThreads = Thread.activeCount()) > 2) {
							try {
								//System.out.println("the active threadcount = " + iActiveThreads);
								Thread.sleep(5000);
							}catch(Exception e2){System.out.println(e2.toString());}
						}//while
						timerTime = Calendar.getInstance();
						timerTime.add(Calendar.SECOND,5);
					}//if
				}//for				
			}//for
			if(st.getThreadStyle().equals(SEQUENTIAL_STYLE)) {
				while( (iActiveThreads = Thread.activeCount()) > 2) {
					try {
						//System.out.println("the active threadcount = " + iActiveThreads);
						Thread.sleep(5000);
					}catch(Exception e2){System.out.println(e2.toString());}
				}//while
				timerTime = Calendar.getInstance();
				timerTime.add(Calendar.SECOND,5);
			}				
		}//for
	}
	
	public static void main(String []args) {
		if(args.length > 0) {
			new WSTester(args[0]);			
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
	
	public RunWebServiceTask(String fileName,String testName, String fullClassName,String methodTestName,int loopCount,int threadLoopCount,int threadNumber) {
		this.testName = testName;
		this.fullClassName = fullClassName;
		this.methodTestName = methodTestName;
		this.loopCount = loopCount;
		this.threadLoopCount = threadLoopCount;
		this.threadNumber = threadNumber;
		this.fileName = fileName;
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
		try {
			FileOutputStream fos = new FileOutputStream(fileName,true);
			fos.write(entry.getBytes());
			entry = String.valueOf(duration) + "\n";
			fos.write(entry.getBytes());
			fos.flush();
			fos.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		String s = "";
		s += "Test Name = " + this.testName + "\n" + "FullClassName = " + this.fullClassName + "MethodTestName" + this.methodTestName + "\nLoopCount = " + this.loopCount + "\n";
		return s;
	}
}