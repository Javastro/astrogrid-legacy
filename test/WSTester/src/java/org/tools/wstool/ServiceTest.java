package org.tools.wstool;

import java.util.ArrayList;


public class ServiceTest {

	private String name;
	private String fullClassName;
	private String threadStyle = "sequential";
	
	private ArrayList serviceTestCase = new ArrayList();
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFullClassName(){
		return fullClassName;
	}
	
	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}
	
	public ArrayList getServiceTestCases() {
		return this.serviceTestCase;
	}
	
	public void addTestCase(ServiceTestCase stc){
		serviceTestCase.add(stc);
	}
	
	public String getThreadStyle() {
		return this.threadStyle;
	}
	
	public void setThreadStyle(String threadStyle) {
		this.threadStyle = threadStyle;
	}
	
	
	public String toString() {
		String s = "";
		s += "Name = " + name + "\n" + " FullClassName = " + fullClassName + "\n" + " Size of TestCaseArray = " + serviceTestCase.size() + "\n";
		for(int i = 0;i < serviceTestCase.size();i++) {
			s += ((ServiceTestCase)serviceTestCase.get(i)).toString();	
		}
		return s;
		
	}
}

class ServiceTestCase {
	private String name;
	private int loop;
	private ArrayList threads = new ArrayList();
	private String threadStyle = "sequential";

	public ServiceTestCase(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLoop(int loop) {
		this.loop = loop;
	}
	
	public int getLoop() {
		return this.loop;
	}
	
	public ArrayList getTestCaseThreads() {
		return threads;
	}
	
	public void addThread(TestCaseThreads tct) {
		threads.add(tct);
	}
	
	public String getThreadStyle() {
		return this.threadStyle;
	}
	
	public void setThreadStyle(String threadStyle) {
		this.threadStyle = threadStyle;
	}
	
	
	public String toString() {
		String s = "";
		s += " Name = " + this.name + "\n" + " Threads size = " + threads.size() + "\n";
		for(int i = 0;i < threads.size();i++) {
			s += ((TestCaseThreads)threads.get(i)).toString();
		}
		return s;
	}
	
}

class TestCaseThreads {
	private int conCurrentThreads;
	private int loop;
	private String threadStyle = "sequential";
	
	public int getConCurrentThreads() {
		return this.conCurrentThreads;
	}
	
	public void setConCurrentThreads(int conCurrentThreads) {
		this.conCurrentThreads = conCurrentThreads;
	}

	public int getLoop() {
		return this.loop;
	}
	
	public void setLoop(int loop) {
		this.loop = loop;
	}
	
	public String getThreadStyle() {
		return this.threadStyle;
	}
	
	public void setThreadStyle(String threadStyle) {
		this.threadStyle = threadStyle;
	}
	
	
	public String toString() {
		String s = "";
		s += " ConCurrentThreads " + this.conCurrentThreads + "\n" + " Loop = " + this.loop + "\n";
		return s;
	}
}