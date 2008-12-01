/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTable;

import ca.odell.glazedlists.EventList;

/** 
 *Filemodel based on a swing transfer handler.
 * 
 * 
 * issues - popup not appearing in right place.
 *  - doesn't seem to be possible - system takes over and I don't get a position back.
 *      - instead, need to respect the copy/move conventions.
 *      - but these don't seem to be working very well.
 *      
 *      - ok. all I can do is just center on the screen, and leave at that.

 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 30, 20073:47:38 PM
 */
public class FileModelTransferHandler extends TransferHandler{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(FileModelTransferHandler.class);


    private final static DataFlavor[] inputFlavors = new DataFlavor[] {
        // other file objects - treat as a copy / move.
        VoDataFlavour.LOCAL_FILEOBJECT
        ,VoDataFlavour.LOCAL_FILEOBJECT_ARRAY
        // another url / uri reference from somewhere.
        ,VoDataFlavour.LOCAL_URI
        ,VoDataFlavour.LOCAL_URI_ARRAY
        ,VoDataFlavour.LOCAL_URL
        // external urls.
        ,VoDataFlavour.URI_LIST
        ,VoDataFlavour.URI_LIST_STRING
        ,VoDataFlavour.URL
        // should I have string or plain here?
        // and if so, do I treat it as a reference, or data content?
    };


    private final Filemodel filemodel;
    
        /**
     * @param filemodel
     */
    public FileModelTransferHandler(final Filemodel filemodel) {
        this.filemodel = filemodel;
    } 
        @Override
        protected Transferable createTransferable(final JComponent c) {
             final Transferable t = filemodel.getSelectionTransferable();
             if (logger.isDebugEnabled()) {
                 final DataFlavor[] flavs = t.getTransferDataFlavors();
                 for (int i = 0; i < flavs.length; i++) {
                     logger.debug(flavs[i]);
                 }
             }
             return t;
        }
        @Override
        public int getSourceActions(final JComponent c) {
            return COPY_OR_MOVE;
        }
        @Override
        public boolean canImport(final JComponent comp, final DataFlavor[] transferFlavors) {
            for (int i = 0; i < transferFlavors.length; i++) {
                if (ArrayUtils.contains(inputFlavors,transferFlavors[i])) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean importData(final JComponent dest, final Transferable t) {
            if (logger.isDebugEnabled()) {
                final DataFlavor[] flavs = t.getTransferDataFlavors();
                for (int i = 0; i < flavs.length; i++) {
                    logger.debug(flavs[i]);
                }
            }
            
            if (canImport(dest,t.getTransferDataFlavors())) {
                try {
                    List objects = null;
                    if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
                        logger.debug("local fileobject");
                        objects = Collections.singletonList(t.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT));
                    } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY)) {
                        logger.debug("local fileobject array");
                         final FileObject[] arr = (FileObject[])t.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY);
                         objects = Arrays.asList(arr);
                    } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI)) {
                        logger.debug("local uri");
                        objects = Collections.singletonList(t.getTransferData(VoDataFlavour.LOCAL_URI));
                    } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY)) {
                        logger.debug("local uri array");
                        final URI[] arr = (URI[])t.getTransferData(VoDataFlavour.LOCAL_URI_ARRAY);
                        objects = Arrays.asList(arr);                        
                    } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URL)) {
                        logger.debug("local url");
                        objects = Collections.singletonList(t.getTransferData(VoDataFlavour.LOCAL_URL));                        
                    } else if (t.isDataFlavorSupported(VoDataFlavour.URI_LIST)) {
                        logger.debug("external uri list");
                        LineIterator it = null;
                        objects = new ArrayList();
                        try {
                            final InputStream is = (InputStream)t.getTransferData(VoDataFlavour.URI_LIST);
                            it = IOUtils.lineIterator(new InputStreamReader(is));
                            while (it.hasNext()) {
                                final String line = it.nextLine();
                                final URL u = VoDataFlavour.mkJavanese(new URL(line.trim()));                                
                                objects.add(u);
                            }                        
                        } finally {
                            LineIterator.closeQuietly(it);
                        }     
                    } else if (t.isDataFlavorSupported(VoDataFlavour.URI_LIST_STRING)) {
                        logger.debug("external uri list as string");
                        objects = new ArrayList();
                        final StringTokenizer tok = new StringTokenizer((String)t.getTransferData(VoDataFlavour.URI_LIST_STRING));
                        while (tok.hasMoreElements()) {
                            final String s= tok.nextToken();
                            final URL u = VoDataFlavour.mkJavanese(new URL(s));
                            objects.add(u);
                        }
                    } else if (t.isDataFlavorSupported(VoDataFlavour.URL)) { // seems to be used for a multiple selection, but only returns first - so demote after uri-list
                        logger.debug("external url");
                        final URL u = (URL) t.getTransferData(VoDataFlavour.URL);                        
                        objects = Collections.singletonList(VoDataFlavour.mkJavanese(u));                        
                    } else {
                        logger.error("Unknown type of transferable " + t);   
                        return false;
                    }
                    logger.debug(objects);
                     if (objects != null && ! objects.isEmpty()) {
                         // see if we've got anything that is read-only.
                         boolean moveAllowed =true;
                         for (final Iterator i = objects.iterator(); i
                                .hasNext();) {
                           final Object o = i.next();
                           if (!( o instanceof FileObject)) {
                               // something else, so best not to move it.
                               moveAllowed = false;
                               break;
                           }
                           final FileObject fo = (FileObject)o;
                           
                           // check whether it's a writable kind of file object
                            if (AstroscopeFileObject.isDelegateOrAstroscopeFileObject(fo) || ! fo.isWriteable()) {
                                moveAllowed = false;
                                break;
                            }
                        }
                         logger.debug("can move: " + moveAllowed);
                         if (moveAllowed) {
                             promptUserForSaveOrMove(dest,objects);
                         } else {
                             filemodel.ops.copyToCurrent(objects);
                         }
                         return true;
                     }
                } catch (final IOException e) {
                    logger.error("Failed to import data",e);
                } catch (final UnsupportedFlavorException e) {
                    logger.error("Failed to import data",e);
                }                
            }
            return false;
        }
        
        
        /** show a popup to the user. 
         * and perform a save or a move according to his selection.
         */
        private void promptUserForSaveOrMove(final JComponent comp, final List fileObjects) {
            final JPopupMenu m = new JPopupMenu(); // create a fresh one each time, as the model might be shared
            final int sz = fileObjects.size();
            m.add("Copy " + sz + " items here").addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    filemodel.ops.copyToCurrent(fileObjects);
                }
            });
            m.add("Move " + sz + " items here").addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    filemodel.ops.moveToCurrent(fileObjects);      
                }
            });
            
                // center it in the component
                // would like to get drop position, but not possible using a TransferHandler.
                final Dimension size = comp.getSize();
                final Point  location = new Point(size.width/2,size.height/2);
            m.show(comp,location.x,location.y);
            
        }
        @Override
        public Icon getVisualRepresentation(final Transferable t) {
            final EventList selected = filemodel.selection.getSelected();            
            if (selected.size() > 1) {
                return ResourceTable.RESOURCES_ICON;
            } else {
                return filemodel.icons.find((FileObject)selected.get(0));
            }
        }
    



}
