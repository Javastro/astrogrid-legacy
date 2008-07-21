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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceTable;

import ca.odell.glazedlists.EventList;

/** 
 * implementation of Filemodel based on a swing transfer handler.
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
    public FileModelTransferHandler(Filemodel filemodel) {
        this.filemodel = filemodel;
    }
        protected Transferable createTransferable(JComponent c) {
             Transferable t = filemodel.getSelectionTransferable();
             if (logger.isDebugEnabled()) {
                 DataFlavor[] flavs = t.getTransferDataFlavors();
                 for (int i = 0; i < flavs.length; i++) {
                     logger.debug(flavs[i]);
                 }
             }
             return t;
        }
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            for (int i = 0; i < transferFlavors.length; i++) {
                if (ArrayUtils.contains(inputFlavors,transferFlavors[i])) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean importData(JComponent comp, Transferable t) {
            if (logger.isDebugEnabled()) {
                DataFlavor[] flavs = t.getTransferDataFlavors();
                for (int i = 0; i < flavs.length; i++) {
                    logger.debug(flavs[i]);
                }
            }
            if (canImport(comp,t.getTransferDataFlavors())) {
                try {
                    List objects = null;
                    if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT)) {
                        logger.debug("local fileobject");
                        objects = Collections.singletonList(t.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT));
                    } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY)) {
                        logger.debug("local fileobject array");
                         FileObject[] arr = (FileObject[])t.getTransferData(VoDataFlavour.LOCAL_FILEOBJECT_ARRAY);
                         objects = Arrays.asList(arr);
                    } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI)) {
                        logger.debug("local uri");
                        objects = Collections.singletonList(t.getTransferData(VoDataFlavour.LOCAL_URI));
                    } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URI_ARRAY)) {
                        logger.debug("local uri array");
                        URI[] arr = (URI[])t.getTransferData(VoDataFlavour.LOCAL_URI_ARRAY);
                        objects = Arrays.asList(arr);                        
                    } else if (t.isDataFlavorSupported(VoDataFlavour.LOCAL_URL)) {
                        logger.debug("local url");
                        objects = Collections.singletonList(t.getTransferData(VoDataFlavour.LOCAL_URL));                        
                    } else if (t.isDataFlavorSupported(VoDataFlavour.URI_LIST)) {
                        logger.debug("external uri list");
                        BufferedReader r = null;
                        objects = new ArrayList();
                        try {
                            InputStream is = (InputStream)t.getTransferData(VoDataFlavour.URI_LIST);
                            r = new BufferedReader(new InputStreamReader(is));
                            String line;
                            while ((line = r.readLine()) != null) {
                                // see if we can at least make a URL from it.
                                URL u = VoDataFlavour.mkJavanese(new URL(line.trim()));                                
                                objects.add(u);
                            }                        
                        } finally {
                            if (r != null) {
                                try { r.close(); } catch (IOException e) { /* ignored*/ }
                            }
                        }     
                    } else if (t.isDataFlavorSupported(VoDataFlavour.URI_LIST_STRING)) {
                        logger.debug("external uri list as string");
                        objects = new ArrayList();
                        StringTokenizer tok = new StringTokenizer((String)t.getTransferData(VoDataFlavour.URI_LIST_STRING));
                        while (tok.hasMoreElements()) {
                            String s= tok.nextToken();
                            URL u = VoDataFlavour.mkJavanese(new URL(s));
                            objects.add(u);
                        }
                    } else if (t.isDataFlavorSupported(VoDataFlavour.URL)) { // seems to be used for a multiple selection, but only returns first - so demote after uri-list
                        logger.debug("external url");
                        URL u = (URL) t.getTransferData(VoDataFlavour.URL);                        
                        objects = Collections.singletonList(VoDataFlavour.mkJavanese(u));                        
                    } else {
                        logger.error("Unknown type of transferable " + t);   
                        return false;
                    }
                    logger.debug(objects);
                     if (objects != null && ! objects.isEmpty()) {
                         // see if we've got anything that is read-only.
                         boolean moveAllowed =true;
                         for (Iterator i = objects.iterator(); i
                                .hasNext();) {
                           Object o = i.next();
                           if (!( o instanceof FileObject)) {
                               // something else, so best not to move it.
                               moveAllowed = false;
                               break;
                           }
                           FileObject fo = (FileObject)o;
                            if (AstroscopeFileObject.isDelegateOrAstroscopeFileObject(fo) || ! fo.isWriteable()) {
                                moveAllowed = false;
                                break;
                            }
                        }
                         logger.debug("can move: " + moveAllowed);
                         if (moveAllowed) {
                             promptUserForSaveOrMove(comp,objects);
                         } else {
                             filemodel.ops.copyToCurrent(objects);
                         }
                         return true;
                     }
                } catch (IOException e) {
                    logger.error("Failed to import data",e);
                } catch (UnsupportedFlavorException e) {
                    logger.error("Failed to import data",e);
                }                
            }
            return false;
        }
        
        
        /** show a popup to the user. 
         * and perform a save or a move according to his selection.
         */
        private void promptUserForSaveOrMove(JComponent comp, final List fileObjects) {
            JPopupMenu m = new JPopupMenu(); // create a fresh one each time, as the model might be shared
            int sz = fileObjects.size();
            m.add("Copy " + sz + " items here").addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    filemodel.ops.copyToCurrent(fileObjects);
                }
            });
            m.add("Move " + sz + " items here").addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    filemodel.ops.moveToCurrent(fileObjects);      
                }
            });
            
                // center it in the component
                // would like to get drop position, but not possible using a TransferHandler.
                Dimension size = comp.getSize();
                Point  location = new Point(size.width/2,size.height/2);
            m.show(comp,location.x,location.y);
            
        }
        public Icon getVisualRepresentation(Transferable t) {
            EventList selected = filemodel.selection.getSelected();            
            if (selected.size() > 1) {
                return ResourceTable.RESOURCES_ICON;
            } else {
                return filemodel.icons.find((FileObject)selected.get(0));
            }
        }
    



}
