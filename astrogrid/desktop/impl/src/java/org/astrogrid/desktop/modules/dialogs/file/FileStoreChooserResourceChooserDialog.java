/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URI;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.AbstractResourceChooserDialog;

import uk.ac.starlink.connect.FileBranch;
import uk.ac.starlink.connect.FileNode;
import uk.ac.starlink.connect.Node;

/** @future enabble dialogue to be positioned at a particular folder / resource.
 * @todo replace with VFS based solution.
 * @author Noel Winstanley
 * @since Nov 6, 20063:37:22 PM 
 */
public class FileStoreChooserResourceChooserDialog extends
		AbstractResourceChooserDialog{

	static final Log logger = LogFactory.getLog(FileStoreChooserResourceChooserDialog.class);
	
	class AcrFilestoreChooser extends FilestoreChooser {
		{
			addDefaultBranches(); 
		}

		  /** COPIED AND HACKED FROM PARENT IMPL.
	     * Populate this browser with a default set of branches.
	     * This includes the current directory and possibly some 
	     * connectors for remote filestores.
	     * The selection is also set to a sensible initial value 
	     * (probably the current directory).
	     */
	    public void addDefaultBranches() {

	        /* Note: there is a problem with listRoots on Windows 2000 - it
	         * pops up a dialogue about empty removable drives (floppy, cd-rom).
	         * See Java bug id #4711632.  There may be workarounds but I tried
	         * for a bit and didn't manage (hard without a local win2000 machine),
	         * so I've given up for now since there's probably not that many
	         * win2000 users. */
	        File[] fileRoots = File.listRoots();

	        /* Add branches for local filesystems. */
	        for ( int i = 0; i < fileRoots.length; i++ ) {
	            File fileRoot = fileRoots[ i ];
	            if ( fileRoot.isDirectory() && fileRoot.canRead() ) {
	                setBranch( new FileBranch( fileRoot ) );
	            }
	            else {
	                logger.warn( "Local filesystem root " + fileRoot + 
	                                 " is not a readable directory" );
	            }
	        }
	        try {
	        addBranch(new MyspaceRootNode(ms,comm));
	        } catch (Exception e) {
	        	// oh well.
	        	logger.warn("Unable to add myspace branch",e);
	        }

	        /* Try to set the current selection to something sensible. */
	        File dir = new File( "." );
	        try {
	            dir = new File( System.getProperty( "user.dir" ) );
	        }
	        catch ( SecurityException e ) {
	            logger.warn( "Can't get current directory" );
	        }
	        if ( dir.isDirectory() ) {
	            setBranch( new FileBranch( dir ) );
	        }
	        else {
	            logger.warn( "Can't read current directory" );
	        }
	    }
	    
	    protected void uriSelected(URI arg0) {
	    	optionPane.setValue(arg0);
	    }
	    
	    protected URI mkURI(Node n) {
	    	if (n instanceof FileNode) {
	    		return ((FileNode)n).getFile().toURI();
	    	} else if (n instanceof MyspaceNode) {
	    		try {
					return((MyspaceNode)n).getURI() ;
				} catch (Exception x) {
					logger.error("URISyntaxException - unlikely",x);
					return null;
				}
	    	} else {
	    		return null;
	    	}
	    }	    
	} // end of inner class.
	
	public FileStoreChooserResourceChooserDialog(MyspaceInternal ms, Community comm) {
		setModal(true);
	        this.setSize(250,300);
		this.ms = ms;
		this.comm = comm;
		this.fst = new AcrFilestoreChooser();
		

		
		optionPane = new JOptionPane(fst,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
        	public void propertyChange(PropertyChangeEvent e) {
        		String prop = e.getPropertyName();
        		if (isVisible()  && (e.getSource() == optionPane)
        				&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||
        						JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
        			Object value = optionPane.getValue();
        			if (value == JOptionPane.UNINITIALIZED_VALUE || value == null) {
        				return; //ignore reset
        			}
        			optionPane.setValue(
        					JOptionPane.UNINITIALIZED_VALUE);
        			
        			if (value instanceof Integer && JOptionPane.OK_OPTION == ((Integer)value).intValue()) {
        				// ok button been clicked - now go an actually fetch the value.
        				fst.ok();
        			} else if (value instanceof URI) {
        				setUri((URI)value);
        					resetAndHide();
        			} else { //user closed dialog or clicked cancel
        				setUri(null);
        				resetAndHide();
        			}
        		}        
        		
        	}
        });
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);     
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent we) {
        		optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
        	}
        });        
        this.setContentPane(optionPane);
        resetAndHide(); // configures it into default state.
	}
	
	final MyspaceInternal ms;
	final Community comm;
	final AcrFilestoreChooser fst;
	final JOptionPane optionPane;
	
	private URI uri;
	
	public void setVisible(boolean b) {
		if (b) {
			fst.refreshList();
		} 
		super.setVisible(b);
	}
	
	
	public URI getUri() {
		return this.uri;
	}

    public void resetAndHide() {        
        setVisible(false);
        setEnableLocalFilePanel(true);
        setEnableMySpacePanel(true);
        setEnableURIPanel(true);
        setChooseDirectories(false);
        
        // clear previously entered data.
        this.fst.resourceUriField.setText("");
        this.fst.nameField_.setText("");
    }
    private boolean enableLocal = true;
    private boolean enableMyspace = true;
	public void setEnableLocalFilePanel(boolean enableLocalFilePanel) {
		//@implement setEnableLocalFilePanel properly
		this.enableLocal = enableLocalFilePanel;
	}
	public void setEnableMySpacePanel(boolean enableMySpacePanel) {
		//@implement setEnableMyspacePanel
		this.enableMyspace = enableMySpacePanel;
	}

	public void setEnableURIPanel(boolean enableURIPanel) {
		this.fst.resourceUriField.setEditable(enableURIPanel);
		this.fst.resourceUriField.setEnabled(enableURIPanel);
	}

	public void setChooseDirectories(boolean enableDirectorySelection) {
		// implies directory only.
		this.fst.setEnableDirectorySelection(enableDirectorySelection);
		this.fst.setEnableFileSelection(!enableDirectorySelection);
		this.fst.nameField_.setEditable(! enableDirectorySelection);
		this.fst.nameField_.setEnabled(! enableDirectorySelection);
	}

    public void setUri(URI uri) {
        this.uri = uri;
    }

}
