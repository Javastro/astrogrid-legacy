package org.astrogrid.desktop.modules.dialogs.file;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.UIImpl;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;

import uk.ac.starlink.connect.Branch;
import uk.ac.starlink.connect.BranchComboBox;
import uk.ac.starlink.connect.ConnectorAction;
import uk.ac.starlink.connect.FileBranch;
import uk.ac.starlink.connect.Leaf;
import uk.ac.starlink.connect.Node;
import uk.ac.starlink.connect.NodeComparator;

/** COPIED from topcat source - and then loosened up, to make easier to 
 * extend.
 * 
 * NWW:
 * @todo fix drive / folder icons shown in dropdown.
 * @todo add progress / status / background manager for potentially long-running network calls.
 * @todo cleanup on logout - move view back to local files if in myspace at the time.
 * @future hide files when in folder-choose usage
 * Original Doc:
 * Generalised file browser which can browse virtual remote filesystems
 * as well as the local filesystem.  The objects it holds are instances
 * of the {@link Node} interface.
 *
 * <p>Though written from scratch, this class is effectively a generalisation 
 * of {@link javax.swing.JFileChooser}. JFileChooser looks like it ought
 * to be generalisable by providing alternative 
 * <tt>FileSystemView</tt> implementations, but
 * I've tried it, and that way lies misery.
 * 
 * @author   Mark Taylor (Starlink)
 * @since    18 Feb 2005
 */
public abstract class FilestoreChooser extends JPanel {

    final BranchComboBox branchSelector_;
    final JList nodeList_;
    private final JScrollPane scroller_;
    protected final JTextField nameField_;
    protected final JTextField resourceUriField; 
    private final JButton logButton_;
    private final JComponent logBox_;
    private final JLabel logLabel_;
    private final PropertyChangeListener connectorWatcher_;
    private final Action upAction_;
    private final Action okAction_;
    private final Component[] activeComponents_;
    private Branch lastBranch_;
    private ConnectorAction connectorAction_;
    private List connectorActions_;
    private static Log logger = 
        LogFactory.getLog( FilestoreChooser.class );

    /**
     * Constructor.
     */
    public FilestoreChooser() {
        super( new BorderLayout() );

        /* Basic setup. */
        JPanel main = new JPanel( new BorderLayout() );
        add( main, BorderLayout.CENTER );
        Border gapBorder = BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
        Border etchBorder = BorderFactory.createEtchedBorder();
        List activeList = new ArrayList();

        /* Construct and place a chooser for the current directory. */
        branchSelector_ = new BranchComboBox();
        branchSelector_.setToolTipText("Select local or remote folder");
        activeList.add( branchSelector_ );
        Box branchBox = Box.createHorizontalBox();
        branchBox.add( new JLabel( "Location: " ) );
        branchBox.add( branchSelector_ );
        branchBox.setBorder( gapBorder );
        add( branchBox, BorderLayout.NORTH );

        /* Define and add a button for moving up a directory. */
        Icon upIcon =  IconHelper.loadIcon("up.png");
        		// found on mac to look same as 'new folder' - "FileChooser.upFolderIcon" );
        upAction_ = new AbstractAction( null, upIcon ) {
        	{
        		putValue(Action.SHORT_DESCRIPTION,"Move up to parent directory");
        	}
            public void actionPerformed( ActionEvent evt ) {
                Branch parent = getBranch().getParent();
                if ( parent != null ) {
                    setBranch( parent );
                }
            }
        };
        JButton upButton = new JButton( upAction_ );
        branchBox.add( Box.createHorizontalStrut( 5 ) );
        branchBox.add( upButton );

        /* Define and add a button for moving to home directory. */
        Icon homeIcon = UIManager.getIcon( "FileChooser.homeFolderIcon" );
        final Branch homedir = getHomeBranch();
        Action homeAction = new AbstractAction( null, homeIcon ) {
        	{
        		putValue(Action.SHORT_DESCRIPTION,"Move to local home directory");
        	}
            public void actionPerformed( ActionEvent evt ) {
                if ( homedir != null ) {
                    setBranch( homedir );
                }
            }
        };
        homeAction.setEnabled( homedir != null );
        JButton homeButton = new JButton( homeAction );
        activeList.add( homeButton );
        branchBox.add( Box.createHorizontalStrut( 5 ) );
        branchBox.add( homeButton );

        Icon newIcon = UIManager.getIcon("FileChooser.newFolderIcon");
        Action newAction = new AbstractAction(null, newIcon) {
        	{
        		putValue(Action.SHORT_DESCRIPTION,"Create a new directory");
        	}
        	public void actionPerformed(ActionEvent e) {
        		Branch curr = getBranch();
        		if (! (curr instanceof MyspaceBranch || curr instanceof FileBranch)) {
        			// don't know how to proces it.
        			return;
        		}
        		String name = JOptionPane.showInputDialog(FilestoreChooser.this, "Name of new directory:", "New Folder",JOptionPane.PLAIN_MESSAGE);
        		if (name != null) {
        			Node[] children = curr.getChildren();
        			for (int i = 0; i < children.length; i++) {
						if (children[i].getName().equals(name)) {
							JOptionPane.showMessageDialog(FilestoreChooser.this, "An object named '" + name + "' already exists","Can't create",JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
        			// harrumph - there's no method on the node to create a new folder. Need to inspect the type of the node, and do some dodgy hacks.
        			// fragile to new node types.
        			if (curr instanceof FileBranch) {
        				FileBranch fb = (FileBranch)curr;
        				File f=  fb.getFile();
        				File newF = new File(f,name);
        				if (! newF.exists()) {
        					newF.mkdirs();
        				}
        			} else if (curr instanceof MyspaceBranch) {
        				MyspaceBranch mb = (MyspaceBranch)curr;
        				try {
        					// not so good - happens on event thread. can't think of a meaningful alternative at the moment.
							mb.getNode().addFolder(name);
						} catch (IOException x) {
							UIImpl.showError(FilestoreChooser.this, "Failed to create: '" + name, x);
						} 
        			}
    				//   refresh the list.
        			refreshList();
    				//@future select the newly created folder.
        		}
        	}
        };
        JButton newButton = new JButton(newAction);
        activeList.add(newButton);
        branchBox.add(Box.createHorizontalStrut(5));
        branchBox.add(newButton);
        
        /* Button for login/logout.  This will only be visible if the current
         * branch represents a remote filesystem.
         * @future remove this - not used in present context. */
        logBox_ = Box.createHorizontalBox();
        logButton_ = new JButton();
        logLabel_ = new JLabel();
        logBox_.add( logLabel_ );
        logBox_.add( Box.createHorizontalStrut( 5 ) );
        logBox_.add( logButton_ );
        logBox_.add( Box.createHorizontalGlue() );
        logBox_.setBorder( gapBorder );
        setConnectorAction( null );
        main.add( logBox_, BorderLayout.NORTH );

        /* Main JList containing nodes in the current branch. */
        nodeList_ = new JList();
        nodeList_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nodeList_.setCellRenderer( new NodeRenderer() );
        scroller_ = new JScrollPane( nodeList_ );
        scroller_.setBorder( BorderFactory.createCompoundBorder( gapBorder,
                                                                 etchBorder ) );
        scroller_.setPreferredSize( new Dimension( 450, 300 ) );
        main.add( scroller_, BorderLayout.CENTER );

        /* Text entry field for typing in the name of a file or directory. */
        nameField_ = new JTextField();
        nameField_.setToolTipText("Enter new file name");
        activeList.add( nameField_ );
        Box nameBox = Box.createHorizontalBox();
        nameBox.add( new JLabel( "File Name: " ) );
        nameBox.add( nameField_ );
        nameBox.setBorder( gapBorder );

        /* Text entry field to type in entire URI */
        resourceUriField = new JTextField();
        resourceUriField.setToolTipText("Enter new URI: http://, ftp://, file://, ivo:// ...");
        activeList.add(resourceUriField);
        Box uriBox = Box.createHorizontalBox();
        uriBox.add(new JLabel("URI: "));
        uriBox.add(resourceUriField);
        
        // add both of these to the display.
        Box bottomBox = Box.createVerticalBox();
        bottomBox.add(nameBox);
        bottomBox.add(uriBox);
        add( bottomBox, BorderLayout.SOUTH );
        
        
        
        /* Make sure we update state when a new branch selection is made
         * from the branch selector combo box. */
        branchSelector_.addItemListener( new ItemListener() {
            public void itemStateChanged( ItemEvent evt ) {
                if ( evt.getStateChange() == ItemEvent.SELECTED ) {
                    setBranch( (Branch) evt.getItem() );
                }
            }
        } );

        /* Ensure that double-clicking or hitting return on one of the items
         * in the list, as well as hitting return in the text entry field,
         * count as indicating a selection. */
        okAction_ = new AbstractAction( "OK" ) {
            public void actionPerformed( ActionEvent evt ) {
                ok();
            }
        };
        nameField_.addActionListener( okAction_ );
        resourceUriField.addActionListener(okAction_);
        nodeList_.addMouseListener( new MouseAdapter() {
            public void mouseClicked( MouseEvent evt ) {
                if ( evt.getClickCount() == 2 ) {
              //      ok();
               // } else  {
                	Node node = getSelectedNode();
					if (node instanceof Branch) {
						setBranch( (Branch) node);
						nameField_.setText("");
						resourceUriField.setText("");
					}
                }
            }
        } );
       
        nodeList_.getInputMap()
                 .put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ),
                       "select" );
        nodeList_.getActionMap().put( "select", okAction_ );

        /* Keep the text in the selection box up to date with the current
         * selected node, and vice versa. */
        nodeList_.addListSelectionListener( new ListSelectionListener() {
            public void valueChanged( ListSelectionEvent evt ) {
                Object[] selected = nodeList_.getSelectedValues();
                String text = null;
                if ( selected.length == 1 ) {
                    final Node node = (Node) selected[ 0 ];
                    if ( (enableFileSelection && node instanceof Leaf)
                    	|| (enableDirectorySelection && node instanceof Branch)) {
                    	final URI u = mkURI(node);
                    	if (u != null) {
                    		text = (node).getName();
                    		nameField_.setText( text );
                    		resourceUriField.setText(u.toString());
                    	}
                    }
                }
            }
        } );
        
        nameField_.addFocusListener(new FocusListener() {
            public void focusGained( FocusEvent evt ) {
                nodeList_.clearSelection();
                resourceUriField.setText("");
            }
            public void focusLost( FocusEvent evt ) {
            	// intentionally empty
            }
        } );
        resourceUriField.addFocusListener(new FocusListener() {
            public void focusGained( FocusEvent evt ) {
                nodeList_.clearSelection();
                nameField_.setText("");
            }
            public void focusLost( FocusEvent evt ) {
            	// intentionally empty            	
            }
        });

        /* Set up an object which can make sure the list is refreshed
         * when a connector logs in or out. */
        connectorWatcher_ = new PropertyChangeListener() {
            public void propertyChange( PropertyChangeEvent evt ) {
                if ( evt.getSource() instanceof ConnectorAction ) {
                    ConnectorAction connAct = (ConnectorAction) evt.getSource();
                    if ( branchSelector_.getConnectorAction() == connAct &&
                         evt.getPropertyName()
                            .equals( connAct.CONNECTION_PROPERTY ) ) {
                        refreshList();
                    }
                }
            }
        };
        activeComponents_ = (Component[]) 
                            activeList.toArray( new Component[ 0 ] );
    }

    protected abstract URI mkURI(Node n);
    
    /**
     * Constructs a new chooser pointing to a given branch.
     *
     * @param  branch  initial branch
     */
    public FilestoreChooser( Branch branch ) {
        this();
        setBranch( branch );
    }


    /**
     * Returns the action which is equivalent to hitting an OK button,
     * that is performing a selection.
     *
     * @return  OK action
     */
    public Action getOkAction() {
        return okAction_;
    }

    public void setEnabled( boolean enabled ) {
        if ( enabled != isEnabled() ) {
            okAction_.setEnabled( enabled );
            for ( int i = 0; i < activeComponents_.length; i++ ) {
                activeComponents_[ i ].setEnabled( enabled );
            }
            upAction_.setEnabled( enabled && lastBranch_.getParent() != null );
        }
        super.setEnabled( enabled );
    }

   

    /**
     * Sets the current selected branch.  This may or may not add a new
     * branch to the selector.
     * 
     * @param  branch  branch
     */
    public void setBranch( Branch branch ) {
        if ( branch != branchSelector_.getSelectedBranch() ) {
            branchSelector_.setSelectedBranch( branch );
        }
        if ( branch != lastBranch_ ) {
            lastBranch_ = branch;
            BoundedRangeModel scrollModel = 
                scroller_.getVerticalScrollBar().getModel();
            scrollModel.setValue( scrollModel.getMinimum() );
            refreshList();

            /* Add a login/logout button if it represents remote filespace. */
            setConnectorAction( branchSelector_.getConnectorAction() );
        }
        upAction_.setEnabled( branch.getParent() != null );
    }

    /** adds a branch, without selecting it. */
    public void addBranch(Branch branch) {
    	branchSelector_.addBranch(branch);
    }
    
    /**
     * Ensures that the list contains the correct children for the
     * currently selected branch.
     */
    public void refreshList() {
        Branch branch = getBranch();
        Node[] children = branch == null ? new Node[ 0 ]
                                         : branch.getChildren();
        Arrays.sort( children, NodeComparator.getInstance() );
        nodeList_.setListData( children );
    }

    /**
     * Returns the currently selected branch.
     *
     * @return   current branch
     */
    public Branch getBranch() {
        return branchSelector_.getSelectedBranch();
    }

    /**
     * Adds a new branch representing a connection to a remote service to
     * this chooser.
     *
     * @param  connAct connector action
     */
    public void addConnection( ConnectorAction connAct ) {
        branchSelector_.addConnection( connAct );
    }

    /**
     * Returns the array of all nodes currently selected.
     *
     * @return  array of selected nodes
     */
    public Node[] getSelectedNodes() {
        return (Node[]) Arrays.asList( nodeList_.getSelectedValues() )
                       .toArray( new Node[ 0 ] );
    }

    /**
     * Returns the single selected node.  If more than one is selected,
     * null is returned.  A node is considered selected if its name is
     * currently entered in the text field (as well as if it's been 
     * selected in the list in the usual way).
     *
     * @param  unique selected node, or null
     */
    public Node getSelectedNode() {
        Node[] nodes = getSelectedNodes();
        if ( nodes.length == 1 ) {
            return nodes[ 0 ];
        }
        else if ( nodes.length == 0 ) {
            String name = nameField_.getText();
            if ( name != null && name.trim().length() > 0 ) {
                return getBranch().createNode( name );
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    /**
     * Invoked if the user hits the OK button or equivalent.
     * If the selected node is a branch, this is interpreted as a 
     * request to move to that branch.  If it's a leaf, then it 
     * counts as a final selection.
     */
    public void ok() {
    	if ( okAction_.isEnabled() ) {
    		Node node = getSelectedNode();
    		if (node != null &&  enableFileSelection && node instanceof Leaf ) {
    			uriSelected(  mkURI(node) );
    		} else if (node != null &&  enableDirectorySelection &&  node instanceof Branch ) {
    			uriSelected(  mkURI(node) );
    		} else {
    			if (resourceUriField.getText() != null && resourceUriField.getText().length() > 0) {
    				try {
    					uriSelected(new URI(resourceUriField.getText()));
    				} catch (URISyntaxException x) {
    					JOptionPane.showConfirmDialog(this,"Not a valid URI");
    				} 
    			}
    		}
    	}
    }
    
    private boolean enableFileSelection = true;
    private boolean enableDirectorySelection = true;
    
    public void setEnableFileSelection(boolean enabled) {
    	this.enableFileSelection = enabled;
    }
    
    public void setEnableDirectorySelection(boolean enabled) {
    	this.enableDirectorySelection = enabled;
    }

    /**
     * Configures the ConnectorAction for the currently selected branch
     * (it will only be non-null if the currently selected branch 
     * represents a connector).
     *
     * @param  connectorAction   new connector action (possibly null)
     */
    private void setConnectorAction( ConnectorAction connectorAction ) {
        if ( connectorAction != connectorAction_ ) {
            if ( connectorAction_ != null ) {
                connectorAction_
               .removePropertyChangeListener( connectorWatcher_ );
            }
            connectorAction_ = connectorAction;
            if ( connectorAction != null ) {
                logLabel_.setText( connectorAction.getConnector().getName() );
                logButton_.setAction( connectorAction );
                connectorAction
               .addPropertyChangeListener( connectorWatcher_ );
            }
        }

        /* Only display the logIn button if the action is non-null. */
        logBox_.setVisible( connectorAction != null );
    }

    /**
     * Returns an object which contains the state of this chooser.
     * The object is the ComboBoxModel which defines the state of the
     * selector at the top of the window which selects the current
     * branch.
     *
     * @return   data model for this chooser
     */
    public ComboBoxModel getModel() {
        return branchSelector_.getModel();
    }

    /**
     * Sets the model which contains the state of this chooser.
     * The object is the ComboBoxModel which defines the state of the
     * selector at the top of the window which selects the current branch.
     * Note you can't just bung any old CombBoxModel in here; it must
     * be one obtained from a {@link #getModel} call on another
     * <tt>FilestoreChooser</tt>.
     *
     * @param   data model to use
     */
    public void setModel( ComboBoxModel model ) {
        branchSelector_.setModel( model );
    }


    
    protected void uriSelected(URI u) {
    	// intentionally empty    	
    }

    /**
     * Returns the user's home directory.
     *
     * @return  readable user.home directory, or null
     */
    private static Branch getHomeBranch() {
        try {
            String home = System.getProperty( "user.home" );
            if ( home != null ) {
                File homedir = new File( home );
                if ( homedir.isDirectory() && homedir.canRead() ) {
                    return new FileBranch( homedir );
                }
            }
            return null;
        }
        catch ( SecurityException e ) {
            return null;
        }
    }

    /**
     * Renderer for list items.
     */
    static class NodeRenderer extends DefaultListCellRenderer {
        private Icon branchIcon = UIManager.getIcon("FileView.directoryIcon"); //UIManager.getIcon( "Tree.closedIcon" );
        private Icon leafIcon = UIManager.getIcon("FileView.fileIcon");//UIManager.getIcon( "Tree.leafIcon" );
        public Component getListCellRendererComponent( JList list, Object value,
                                                       int index, 
                                                       boolean isSelected,
                                                       boolean hasFocus ) {
            Icon icon = null;
            if ( value instanceof Branch ) {
                value = ((Branch) value).getName();
                icon = branchIcon;
            }
            else if ( value instanceof Leaf ) {
                value = ((Leaf) value).getName();
                icon = leafIcon;
            }
            Component comp = 
                super.getListCellRendererComponent( list, value, index, 
                                                    isSelected, hasFocus );
            if ( comp instanceof JLabel ) {
                ((JLabel) comp).setIcon( icon );
            }
            return comp;
        }
    }

}
