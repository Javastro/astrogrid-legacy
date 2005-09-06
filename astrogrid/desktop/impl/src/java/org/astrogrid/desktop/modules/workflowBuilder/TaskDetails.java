/* TaskDetails.java
 * Created on 02-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder;

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TaskDetails {
  	String uiName;
  	String taskName;
  	String interfaceName;
  	/**
  	 * 
  	 * @param taskName
  	 * @param uiName
  	 * @param interfaceName
  	 */
  	public TaskDetails(String taskName, String uiName, String interfaceName){
  		this.uiName = uiName;
  		this.taskName = taskName;
  		this.interfaceName = interfaceName;
  	}
  	public String toString() {
  		return uiName + " (" + interfaceName +")" ;
  	}
  	public String getTaskName() {
  		return taskName;
  	}
  	public String getUIName() {
  		return uiName;
  	}      	
  	public String getInterfaceName() {
  		return interfaceName;
  	} 

}
