package org.astrogrid.tools.WebServiceDelegate;


public class Policy {

	private boolean canAccess = false;
	private boolean canRead = false;
	private boolean canWrite = false;
	private boolean canExecute = false;

	public Policy() {
		
	}
	
	public void setRead(boolean canRead) {
		this.canRead = canRead;
	}
	
	public void setAccess(boolean canAccess) {
		this.canAccess = canAccess;
	}
	
	public void setWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}
	
	public void setExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}
	
	public boolean canRead() {
		return this.canRead;
	}
		
	public boolean canWrite() {
		return this.canWrite;
	}
		
	public boolean canExecute() {
		return this.canExecute;
	}
	
	public boolean canAccess() {
		return this.canAccess;
	}
	
	public String toString() {
		return  "Access=" +  String.valueOf(canAccess()) + 
				" Read=" +   String.valueOf(canRead()) + 
                " Write" +   String.valueOf(canWrite()) + 
                " Execute" + String.valueOf(canExecute()); 
	}
}