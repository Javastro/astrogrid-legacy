/*$Id: VospaceBrowserImpl.java,v 1.2 2005/04/13 12:59:18 nw Exp $
 * Created on 22-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ag.Community;
import org.astrogrid.desktop.modules.ag.UserLoginEvent;
import org.astrogrid.desktop.modules.ag.UserLoginListener;
import org.astrogrid.desktop.modules.ag.Vospace;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeMetadata;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.store.Ivorn;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;

import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;

import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JList;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Mar-2005
 *
 */
public class VospaceBrowserImpl extends UIComponent implements VospaceBrowser, UserLoginListener{

	private JSplitPane jSplitPane = null;
	private JTree nodeTree = null;
	private JMenuBar jJMenuBar = null;
	private JToolBar jToolBar = null;
    private DefaultTreeModel model = null;
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */    
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(new JScrollPane(getNodeTree()));
			jSplitPane.setRightComponent(new JScrollPane(getActionPane()));
			jSplitPane.setDividerSize(5);
            jSplitPane.setDividerLocation(300);
		}
		return jSplitPane;
	}
    
    
	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */    
	private JTree getNodeTree() {
		if (nodeTree == null) {
			nodeTree = new JTree() {
                public String convertValueToText(Object value, boolean a, boolean b, boolean c, int i, boolean d) {
                    if (value instanceof FileManagerNode) {
                        return ((FileManagerNode)value).getName();
                    } else {
                        return super.convertValueToText(value,a,b,c,i,d);
                    }
                }
            };
            //model = new FolderTreeModel(null);
            model = new DefaultTreeModel(null);
            nodeTree.setModel(model);          
            nodeTree.setCellEditor(null);                           
			nodeTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() { 
				public void valueChanged(javax.swing.event.TreeSelectionEvent e) {    
                    FileManagerNode n = (FileManagerNode)nodeTree.getLastSelectedPathComponent();
                    displayInformation(n);
                }
			});
		}
		return nodeTree;
	}
    private DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM,SimpleDateFormat.MEDIUM);
    
    private void displayInformation(FileManagerNode n) {
        StringBuffer buff = new StringBuffer();
        NodeMetadata m= n.getMetadata();
        buff.append("<html>")
          .append("<b>")
          .append(n.getName())
          .append("</b><dl>")
          .append("<dt>Created<dd>")
          .append(df.format(m.getCreateDate().getTime()))
        .append("<dt>Modified<dd>")
        .append(df.format(m.getModifyDate().getTime()))       
         .append("<dt>Node Ivorn<dd>")
         .append(m.getNodeIvorn());
        if (n.isFile()) {
            buff.append("<dt>Size<dd>")
            .append(Math.round(m.getSize().longValue() /  1024))
            .append(" Kb")
            .append("<dt>Store<dd>")
           .append(m.getContentLocation());
        } else {
            buff.append("<dt>Children<dd>")
                .append(n.getChildCount());
        }
         buff.append("</html>");
        information.setText(buff.toString());
        buff = new StringBuffer();
        buff.append("<html><dl>");
        for (Iterator i = m.getAttributes().entrySet().iterator(); i.hasNext();) {
            Map.Entry e= (Map.Entry)i.next();
            buff.append("<dt>")
                .append(e.getKey())
                .append("<dd>")
                .append(e.getValue());
        }
        buff.append("</dl></html>");
        furtherInformation.setText(buff.toString());
    }
    
    
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
		}
		return jJMenuBar;
	}
	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */    
	private JToolBar getJToolBar() {
		if (jToolBar == null) {
			jToolBar = new JToolBar();
		}
		return jToolBar;
	}

    private JTaskPane getActionPane() {
        if (actionPane == null) {
            actionPane = new JTaskPane();
            JTaskPaneGroup infoGroup = new JTaskPaneGroup();
            infoGroup.setText("Properties");
            infoGroup.setIcon(IconHelper.loadIcon("info_obj.gif"));
            infoGroup.setSpecial(true);
            JTaskPaneGroup furtherGroup = new JTaskPaneGroup();
            furtherGroup.setText("Advanced");
            furtherGroup.setIcon(IconHelper.loadIcon("read_obj.gif"));
            furtherGroup.setExpanded(false);
            actionPane.add(infoGroup);
            actionPane.add(furtherGroup);
            information = new JLabel();
            infoGroup.add(information);
            furtherInformation = new JLabel();
            furtherGroup.add(furtherInformation);
        }
        return actionPane;
    }

	/**
	 * This is the default constructor
	 * @throws CommunityException
	 * @throws RegistryException
	 * @throws URISyntaxException
	 * @throws RemoteException
	 * @throws NodeNotFoundFault
	 * @throws FileManagerFault
	 */
	public VospaceBrowserImpl() {
        this.vos = null;
		initialize();
	}
    
    public VospaceBrowserImpl(Vospace vos, Community comm) {
        this.vos = vos;
        comm.addUserLoginListener(this);
        initialize();
    }
    protected final Vospace vos;
    private JTaskPane actionPane;
    private JLabel information;
    private JLabel furtherInformation;
	/*
	 * This method initializes this
	 * 
	 * @return void
	 * @throws CommunityException
	 * @throws RegistryException
	 * @throws URISyntaxException
	 * @throws RemoteException
	 * @throws NodeNotFoundFault
	 * @throws FileManagerFault
	 */
	private void initialize(){
        this.setJMenuBar(getJJMenuBar());
		this.setName("vospaceBrowser");
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(700,600);
        JPanel pane = getJContentPane();
        pane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
        pane.add(getJToolBar(), java.awt.BorderLayout.NORTH);        
		this.setContentPane(pane);
		this.setTitle("VoSpace Browser");
        readRoot();
	}
    /**
     * @throws URISyntaxException
     * @throws CommunityException
     * @throws RegistryException
     * @throws RemoteException
     * @throws NodeNotFoundFault
     * @throws FileManagerFault
     * @see org.astrogrid.desktop.modules.ui.VospaceBrowser#open(org.astrogrid.store.Ivorn)
     */
    public void open(Ivorn ivo) {
        //@todo implement.
    }
    
    private void readRoot() {
        (new BackgroundOperation("Reading Vospace Root") {

            protected Object construct() throws Exception {
                return vos.node(new Ivorn(vos.home().toString()));
            }
            protected void doFinished(Object result) {
                model.setRoot((FileManagerNode)result);
            }
        }).start();
        
    }
    /**
     * @see org.astrogrid.desktop.modules.ag.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
        readRoot();
    }
    /**
     * @see org.astrogrid.desktop.modules.ag.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
        getNodeTree().setModel(null);
    }

    
    
}


/* 
$Log: VospaceBrowserImpl.java,v $
Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/13 12:23:27  nw
refactored a common base class for ui components

Revision 1.1.2.1  2005/04/04 16:43:48  nw
made frames remember their previous positions.
synchronized guiLogin, so only one login box ever comes up.
made refresh action in jobmonitor more robust

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.
 
*/