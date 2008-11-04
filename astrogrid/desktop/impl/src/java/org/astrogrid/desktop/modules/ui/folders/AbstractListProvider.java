/**
 * 
 */
package org.astrogrid.desktop.modules.ui.folders;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;

/** Abstract class for a persistent list of folders
 * provides a persistent model to which multiple views can connect.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 200711:25:20 PM
 */
public abstract class AbstractListProvider 
implements ListEventListener,  ListProvider, ExceptionListener{

    /**
     * Logger for this class
     */
    protected static final Log logger = LogFactory
    .getLog(ListProvider.class);
    /**
     *  create a new persisten list.
     *  
     */
    public AbstractListProvider(final UIContext parent, final File storage, final XmlPersist xml) {
        this.parent = parent;
        this.storage = storage;
        this.xml = xml;
        this.theList = createList();			
        load(storage, theList);
        if (theList.size() == 0) {
            initializeFolderList(); 
            save(storage,theList);
        }
        theList.addListEventListener(this);			
    }

    private final UIContext parent;
    private final File storage;
    private final XmlPersist xml;
    private final EventList theList;

    // called when a recoverable exception is thrown by the decoder.
    //@todo remove once we're done with backward compatability
    public void exceptionThrown(final Exception e) {
        logger.warn("Exception whilst reading file: '" + e.getMessage() + "' - continuing");
        logger.debug(e);
    }
    public final EventList getList() {
        return theList;
    }
    public final File getStorageLocation() {
        return storage;
    }

    /** folder list has changed - write it back to disk */
    public void listChanged(final ListEvent arg0) {
        save(storage,theList);
    }


    public final void load(final File f, final List l) {
        if (! f.exists()) {
            return;
        }
        logger.info("Loading list from " + f);
        InputStream fis = null;
        try { 
            fis = new FileInputStream(f);
            final List rs = (List)xml.fromXml(fis);
            if (rs != null) {
                l.addAll(rs);
                logger.info("Loaded " + rs.size() + " items");
            } else {
                logger.info("File is empty");
            }
        } catch (final FileNotFoundException ex) {
            logger.error(f.toString(),ex);        
        } catch (final ServiceException x) {
            logger.info("Failed to read file");
            logger.debug("Failed to read file",x);
            // trying to fall back to old technique..
            loadOldXmlDecoder(f,l);
            save(f,l); // and if it loaded, save it again.
        }finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (final IOException x1) {
                    logger.error("IOException",x1);
                }
            }
        }
    }

    public final void save(final File file, final List content) {
        (new BackgroundWorker(parent,"Saving list") {
            {
                setTransient(true);
            }
            protected Object construct() throws Exception{
                reportProgress("Saving list to " + file);
                OutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    final List output = new ArrayList(content); // necessary to copy, as glazedList doesn't serialize
                    xml.toXml(output,fos);
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (final IOException x) {
                            logger.error("Failed to save list.",x);
                        }
                    }
                }
                return null;
            }
        }).start();
    }   



    /** ceate the list object. override to use a different implementation
     * @return
     */
    protected EventList createList() {
        return new BasicEventList();
    }

    /** subclasses should extend this to pre-populate the list */
    protected abstract void initializeFolderList() ;

    // legacy methods - keep these around for a little while for backwards compatability..
    //@fixme - delete.
    private void loadOldXmlDecoder(final File f, final java.util.List target) {
        if (! f.exists()) {
            return;
        }
        logger.info("Loading folder list from " + f);
        InputStream fis = null;
        XMLDecoder x = null;
        try { 
            fis = new FileInputStream(f);
            x = new XMLDecoder(fis,this,this);
            final Folder[] rs = (Folder[])x.readObject();
            if (rs != null) {
                target.addAll(Arrays.asList(rs));
                logger.info("Loaded " + rs.length + " items");
            } else {
                logger.info("File is empty");
            }
        } catch (final FileNotFoundException ex) {
            logger.error(storage.toString(),ex);
        } catch (final ArrayIndexOutOfBoundsException ex) {
            // thrown when the file contains no data - a bit crap - no way to check this.
            // fail gracefully.
            logger.info("File is empty");
        } catch (final NoSuchElementException ex) {
            // thrown when the file contains no data - a bit crap - no way to check this.
            // fail gracefully.
            logger.info("File is empty");			
        }finally {
            if (x != null) {
                x.close();
            } 
            if (fis != null) {
                try {
                    fis.close();
                } catch (final IOException x1) {
                    logger.error("IOException",x1);
                }
            }
        }
    }
//    private void saveOldXmlDecoder(final File file, final List source) {
//        (new BackgroundWorker(parent,"Saving resource lists") {
//            protected Object construct() throws Exception {
//                logger.info("Saving list to " + file);
//                OutputStream fos = null;
//                XMLEncoder a = null;
//                try {
//                    fos = new FileOutputStream(file);
//                    a = new XMLEncoder(fos);
//                    Folder[] rf = (Folder[])source.toArray(new Folder[theList.size()]);
//                    a.writeObject(rf);
//                } catch (FileNotFoundException x) {
//                    logger.error(file.toString(),x);
//                } finally {
//                    if (a != null) {
//                        a.close();
//                    } 
//                    if (fos != null) {
//                        try {
//                            fos.close();
//                        } catch (IOException x) {
//                            logger.error("Failed to save folder list.",x);
//                        }
//                    }
//                }
//                return null;
//            }
//        }).start();
//    }

}