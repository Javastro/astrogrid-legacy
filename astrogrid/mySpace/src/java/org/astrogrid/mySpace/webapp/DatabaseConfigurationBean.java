/* $Id: DatabaseConfigurationBean.java,v 1.1 2004/02/16 12:37:23 jdt Exp $
 * Created on 13-Feb-2004 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.mySpace.webapp;

import java.util.Vector;

import org.astrogrid.mySpace.mySpaceManager.RegistryManager;
import org.astrogrid.mySpace.mySpaceManager.ServerDetails;

/**
 * @author jdt
 *
 *  Bean for use in JSP pages to simplify creation of the mySpace database
 */
public class DatabaseConfigurationBean {
	private String pathToRegistry="";
	private String tomcatRoot="";
	private String host="";
	private String folderName="mySpaceConfig";
	private String serverName="serv1";
	private int expiry=30;
	
	public String getURL() {
		return host+"/" + folderName+"/";
	}
	public String getDirectory() {
		return tomcatRoot+"/webapps/ROOT/"+folderName+"/";
	}
	
	public void createRegistry() {
		ServerDetails server = new ServerDetails(serverName,expiry, getURL(), getDirectory());
		Vector servers = new Vector();
		servers.add(server);
		new RegistryManager(pathToRegistry, servers);
	}
	/**
	 * @return Returns the expiry.
	 */
	public int getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry The expiry to set.
	 */
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return Returns the pathToRegistry.
	 */
	public String getPathToRegistry() {
		return pathToRegistry;
	}

	/**
	 * @param pathToRegistry The pathToRegistry to set.
	 */
	public void setPathToRegistry(String pathToRegistry) {
		this.pathToRegistry = pathToRegistry;
	}

	/**
	 * @return Returns the serverName.
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName The serverName to set.
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return Returns the tomcatRoot.
	 */
	public String getTomcatRoot() {
		return tomcatRoot;
	}

	/**
	 * @param tomcatRoot The tomcatRoot to set.
	 */
	public void setTomcatRoot(String tomcatRoot) {
		this.tomcatRoot = tomcatRoot;
	}

	/**
	 * @return Returns the folderName.
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * @param folderName The folderName to set.
	 */
	public void setFolderName(String webAppName) {
		this.folderName = webAppName;
	}

}
