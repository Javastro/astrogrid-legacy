/*$Id: Desktop.java,v 1.1 2005/02/21 11:25:07 nw Exp $
 * Created on 01-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.ui;

import org.astrogrid.desktop.service.Community;
import org.astrogrid.desktop.service.Services;
import org.astrogrid.ui.script.ScriptEnvironment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.picocontainer.Startable;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Feb-2005
 *
 */
public class Desktop extends JFrame implements Startable, Observer {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Desktop.class);

	private javax.swing.JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;
	private JMenu jMenu = null;
	private JMenuItem loginMenuItem = null;
	private JMenu jMenu2 = null;
	private JMenuItem logoutMenuItem = null;
	private JMenuItem jMenuItem2 = null;
	private JMenuItem jMenuItem3 = null;
	private JMenuItem jMenuItem4 = null;
	private JMenuItem jMenuItem5 = null;
	private JLabel status = null;
	private JMenuItem jMenuItem6 = null;
	private JLabel jLabel = null;
	/**
	 * This is the default constructor - testing only.
     * @deprecated - only for use by the gui builder.
	 */
	public Desktop() {
		super();
        this.browser = null;
        this.community = null;
		initialize();
	}
    
    /** this is the production constructor */
    public Desktop(BrowserControl browser,Community community) {
        this.browser = browser;
        this.community = community;
        community.addObserver(this);
        initialize();
    }
    protected final Community community;
    protected final BrowserControl browser;
    
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setJMenuBar(getJJMenuBar());
		this.setSize(151, 199);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			status = new JLabel();
			jLabel = new JLabel();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			status.setText("Not Logged In.");
			status.setForeground(java.awt.SystemColor.activeCaption);
			jLabel.setText("");
			jLabel.setIcon(new ImageIcon(getClass().getResource("/org/astrogrid/AGlogo.png")));
			jContentPane.add(status, java.awt.BorderLayout.SOUTH);
			jContentPane.add(jLabel, java.awt.BorderLayout.WEST);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenu());
			jJMenuBar.add(getJMenu2());
		}
		return jJMenuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setText("Connect");
			jMenu.add(getLoginMenuItem());
			jMenu.add(getLogoutMenuItem());
			jMenu.add(getJMenuItem5());
			jMenu.add(getJMenuItem6());
		}
		return jMenu;
	}
	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getLoginMenuItem() {
		if (loginMenuItem == null) {
			loginMenuItem = new JMenuItem();
			loginMenuItem.setText("Login...");
			loginMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                        community.guiLogin();
				}
			});
		}
		return loginMenuItem;
	}
	/**
	 * This method initializes jMenu2	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getJMenu2() {
		if (jMenu2 == null) {
			jMenu2 = new JMenu();
			jMenu2.setName("");
			jMenu2.setText("Help");
			jMenu2.add(getJMenuItem2());
			jMenu2.add(getJMenuItem4());
			jMenu2.add(getJMenuItem3());
		}
		return jMenu2;
	}
	/**
	 * This method initializes jMenuItem1	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getLogoutMenuItem() {
		if (logoutMenuItem == null) {
			logoutMenuItem = new JMenuItem();
			logoutMenuItem.setText("Logout");
			logoutMenuItem.setEnabled(false);
			logoutMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					community.logout();
				}
			});
		}
		return logoutMenuItem;
	}
	/**
	 * This method initializes jMenuItem2	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getJMenuItem2() {
		if (jMenuItem2 == null) {
			jMenuItem2 = new JMenuItem();
			jMenuItem2.setText("Overview...");
			jMenuItem2.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {                        
                        browser.openURL("http://www.astrogrid.org");
                    } catch (Exception ex) {
                        logger.error(e);
                    }
				}
			});
		}
		return jMenuItem2;
	}
	/**
	 * This method initializes jMenuItem3	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getJMenuItem3() {
		if (jMenuItem3 == null) {
			jMenuItem3 = new JMenuItem();
			jMenuItem3.setText("About Astrogrid Desktop...");
			jMenuItem3.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    JOptionPane.showMessageDialog(Desktop.this,"Astrogrid Desktop v0.1","About Astrogrid Desktop",JOptionPane.INFORMATION_MESSAGE);
				}
			});
		}
		return jMenuItem3;
	}
	/**
	 * This method initializes jMenuItem4	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getJMenuItem4() {
		if (jMenuItem4 == null) {
			jMenuItem4 = new JMenuItem();
			jMenuItem4.setText("Services...");
			jMenuItem4.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					try {
                        browser.openRelative(""); // open the services root.
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
				}
			});
		}
		return jMenuItem4;
	}
	/**
	 * This method initializes jMenuItem5	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getJMenuItem5() {
		if (jMenuItem5 == null) {
			jMenuItem5 = new JMenuItem();
			jMenuItem5.setText("Settings...");
			jMenuItem5.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        browser.openRelative("configuration/");
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
				}
			});
		}
		return jMenuItem5;
	}
	/**
	 * This method initializes jMenuItem6	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */    
	private JMenuItem getJMenuItem6() {
		if (jMenuItem6 == null) {
			jMenuItem6 = new JMenuItem();
			jMenuItem6.setText("Exit");
			jMenuItem6.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {   
                    System.exit(0);
				}
			});
		}
		return jMenuItem6;
	}

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        this.show();
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }

    /**
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        // notification received that login status has changed
        loginMenuItem.setEnabled(!community.isLoggedIn());
        logoutMenuItem.setEnabled(community.isLoggedIn());
        if (community.isLoggedIn()) {
            ScriptEnvironment env = community.getEnv();
            status.setText("Logged in " + env.getUserIvorn());
        } else {
            status.setText("Not Logged in");
        }
    }
  }  //  @jve:decl-index=0:visual-constraint="10,11"


/* 
$Log: Desktop.java,v $
Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/