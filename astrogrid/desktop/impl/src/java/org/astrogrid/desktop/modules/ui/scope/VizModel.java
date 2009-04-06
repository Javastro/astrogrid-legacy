package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Font;
import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.AstroScopeLauncherImpl;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectListTransferable;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectTransferable;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.fileexplorer.IconFinder;

import edu.berkeley.guir.prefuse.focus.DefaultFocusSet;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/** The shared model of the vizualization - a tree of results, plus selections.
 * model is shared between all vizualizations.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 26-Jan-2006
 */
public  final class VizModel {
    private final TreeNode rootNode;
    private final FocusSet selectionFocusSet;
    private final NodeSizingMap nodeSizingMap;
    private final Tree tree;
    private final DalProtocolManager protocols;
    private final AstroScopeLauncherImpl parent;    
    private FileSystem resultsFS;
    final FileSystemManager vfs;
    private final QueryResultCollector summarizer;
    public final IconFinder iconFinder;
    public VizModel(final AstroScopeLauncherImpl parent,final DalProtocolManager protocols, final QueryResultCollector summarizer
    		, final FileSystemManager vfs, final IconFinder iconFinder) {
        this.parent = parent;
        this.protocols = protocols;
        this.summarizer = summarizer;        
        this.vfs = vfs;
        this.iconFinder = iconFinder;
        this.nodeSizingMap = new NodeSizingMap();
        this.selectionFocusSet = new DefaultFocusSet();       
        rootNode = new ImageTreeNode(IconHelper.loadIcon("scope16.png").getImage());
        rootNode.setAttribute(AbstractRetriever.LABEL_ATTRIBUTE,"Search Results");
        final Font ft = new Font(null,Font.BOLD,14);
        rootNode.setAttribute(AbstractRetriever.FONT_ATTRIBUTE,ft.toString());
        
        createResultsFilesystem();
                
        for (final Iterator i = protocols.iterator(); i.hasNext(); ) {
            final DalProtocol p = (DalProtocol)i.next();
            p.setVizModel(this);
            final TreeNode primary = p.getPrimaryNode();
            final DefaultEdge primaryEdge = new DefaultEdge(rootNode,primary);
            primaryEdge.setAttribute(AbstractRetriever.WEIGHT_ATTRIBUTE,"3");
            rootNode.addChild(primaryEdge);            
        }
 
        tree = new DefaultTree(rootNode);   
             
    }

    /**
     * @param vfs
     */
    private void createResultsFilesystem() {
        try {

            // Use a different scheme for each new go at the results vfs.
            // This is a reliable (though perhaps not optimally efficient)
            // way to ensure that the new vfs does not pick up results from 
            // old ones.
            final String scheme = "astroscope" + (++vfsSeq);
            this.resultsFS = vfs.createVirtualFileSystem(scheme+"://").getFileSystem();
        } catch (final FileSystemException x) {
            throw new RuntimeException("Not expected to fail",x);
        }
    }
    int vfsSeq = 0;

    /** clears the graph, plus all selections */
    public void clear() {
        getNodeSizingMap().clear();
        getSelectionFocusSet().clear();
        summarizer.clear();
        for (final Iterator i = protocols.iterator(); i.hasNext(); ) {
            final DalProtocol p = (DalProtocol)i.next();
            p.getPrimaryNode().removeAllChildren();
        }         
        //just for sanity go make sure the remaining nodes which are really just
        //siaNode, rootNode, coneNode are set to false for the selected attribute.
        for (final Iterator i = getTree().getNodes(); i.hasNext(); ) {
            final Node n = (Node)i.next();
            n.setAttribute("selected","false");
        }    

        // seems like I can't remove these junctions - nuciance.
        // just need to delete the whole fs.
 
        // Note (mbt 2008-02-25): closeFileSystem does not itself appear to 
        // delete the files.  FileObject.delete doesn't work here because the
        // delegate vfs doesn't support deletion.
        // Hack: createResultsFileSystem makes sure it uses a new namespace
        // each time.
        vfs.closeFileSystem(resultsFS);
        createResultsFilesystem();
        
    }        

    /** focus set used to maintain list of nodes selected for download.
     * focus set is shared between vizualizaitons- so changes in one will be seen in others.
     */
    public FocusSet getSelectionFocusSet() {
        return selectionFocusSet;
    }

    /**access the shared data model */
    public Tree getTree() {
        return tree;
    }

    public DalProtocolManager getProtocols() {
        return this.protocols;
    }
    
    public NodeSizingMap getNodeSizingMap() {
        return nodeSizingMap;
    }

	/** Build a transferable that represents the interesting parts of the selection
	 * as file objects.
	 * @future reimplement so this is a single object, repopulated on each selection change.
	 */
	public Transferable getSelectionTransferable() {
	    final List<FileObjectView> l = new ArrayList<FileObjectView>();
        // scan through the selection, looking for nodes who  offer a FileObject.
        for (final Iterator i = getSelectionFocusSet().iterator(); i.hasNext();) {
            final TreeNode tn = (TreeNode) i.next();
            if (tn instanceof FileProducingTreeNode) {
                final FileObjectView fo = ((FileProducingTreeNode)tn).getFileObject();
                if (fo != null) {
                    l.add(fo);
                }
            }
        }
        switch (l.size()) {
        case 0:
            return null; 
        case 1:
            return new FileObjectTransferable(l.get(0),false);
        default:
            return new FileObjectListTransferable(l); 
        }
	}

	/** add a result into the results direcotry. 
	 * bz 2665 - was getting concurrent modification exceptions here, so made this method synchronized.
	 * @param abstractRetriever the retriever to add a result for
	 * @param name the name of the result
	 * @param result the file object that contains the contents of the result.
	 * @param the tree node to cache the freshly created file object in.
	 */
	public synchronized void addResultFor(final Retriever abstractRetriever,final String name,final AstroscopeFileObject result, final FileProducingTreeNode node) throws FileSystemException {
	        final String retDir = mkFileName(abstractRetriever);
	        int renameCount = 0;	        
	        final String prefix = StringUtils.substringBeforeLast(name,".");
	        final String suffix = StringUtils.substringAfterLast(name,".");
	            String fullName;
	            FileObject candidate; 	        
	            do { // make a unique name
	                if (renameCount == 0) {
	                    fullName = retDir + "/" + name;
	                } else {
	                    fullName = retDir + "/" + prefix + "-" + renameCount + "." + suffix;
	                }
	               // synchronized(resultsFS) {
	                    candidate = resultsFS.resolveFile(fullName);
	                //}
	                renameCount++;
	            } while (candidate != null && candidate.exists());
	            // ok, found a candidate that doesn't yet exist.
	            resultsFS.addJunction(fullName,result);
	            // now find the image, based on the filename.
	             final Image image = iconFinder.find(candidate).getImage();
	             //tie both together.
                final FileObjectView fov = new FileObjectView(candidate,iconFinder);
                node.setFileObject(fov,image);
                result.setNode(node);
	}
	
	/**
	 *  Create an astroscope file object - a lazy wrapper around a 
	 *  real url file result.
	 * @param url the real data
	 * @param size the data size - mustbe correct, else data will not be all read.
	 * @param date last modification date
	 * @param type the content-type of the data
	 * @return
	 * @throws FileSystemException
	 */
	public AstroscopeFileObject createFileObject(final URL url,final long size,final long date,final String type) throws FileSystemException {
	    final FileObject core = vfs.resolveFile(url.toString());
	    return new AstroscopeFileObject(core,size,date,type);
	}
	
	/** create a results directory for this retriever.
	 * @param abstractRetriever
	 * @return
	 * @throws FileSystemException 
	 */
	public FileObject createResultsDirectory(final Retriever abstractRetriever) throws FileSystemException {
	    final String name = mkFileName(abstractRetriever);
	    final FileObject fo= resultsFS.resolveFile(name);	    
	    return fo;
     
	}
	
	/** compute a legal and fairly pretty filename from a service id */
	private String mkFileName(final Retriever r) {
        String name = r.getService().getId().getSchemeSpecificPart();
        name += "_" + r.getServiceType();
        final String subName = r.getSubName();
        if (subName != null && subName.length()>0) {
            name += "_" + subName;
        }
	    
	    return "/" + removeLineNoise(name);
	}
	

    public final AstroScopeLauncherImpl getParent() {
        return this.parent;
    }

    public final QueryResultCollector getSummarizer() {
        return this.summarizer;
    }

    private static final String FORBIDDEN = "/:;?=&\\$+!*\"'()@{}|[]^~<>#` ";
    private static final String REPLACE = StringUtils.repeat("_",FORBIDDEN.length());
    
    /** cponverts all line noise to underscore */ 
    public static String removeLineNoise(final String name) {
        final String munged = StringUtils.replaceChars(name,FORBIDDEN,REPLACE); // convert  / to _, strip : and any other odd symbols
        return URLEncoder.encode(munged);
    }
    
}
