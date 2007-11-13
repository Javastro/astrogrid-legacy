package org.astrogrid.desktop.modules.ui.folders;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/**
 * Provides a persistent tree model representing a hierarchy of 
 * resource folders.  Every node in the tree is a 
 * {@.ink javax.swing.tree.DefaultMutableTreeNode} with a user object
 * which is a {@link org.astrogrid.desktop.modules.ui.folders.ResourceFolder}.
 *
 * @author   Noel Winstanley
 * @author   Mark Taylor
 * @since    6 Sep 2007
 */
public class ResourceTreeProvider extends PersistentTreeProvider {

    private static final String EXAMPLES_NODE_NAME = "Examples";

    /**
     * Constructor.
     *
     * @param    parent  context
     * @param    workdirPreference   directory for persistence
     * @param    persister   XML persistence implementation
     */
    public ResourceTreeProvider(UIContext parent, Preference workdirPreference,
                                XmlPersist persister) {
        super(parent, new File(new File(workdirPreference.getValue()),
                                        "resourceLists.xml"), persister);
        logger.info("Resource folders will be read from/written to " +
                    getStorageLocation());
        init(new ResourceTreeModel(parent, persister));
    }

    /**
     * Loads the tree representation from its persistent home on disk, and
     * doctors it if necessary to contain an Examples folder.  This will
     * normally only be necessary when loading a resources persisted by
     * an earlier version of this class.
     * Note however that it means the user cannot delete the Examples node
     * persistently.  I claim this is a Feature.
     */
    public DefaultMutableTreeNode load() throws IOException, ServiceException {
        DefaultMutableTreeNode root = super.load();
        if (root != null && root.getAllowsChildren()) {
            if (! hasExamples(root)) {
                root.insert(createExamplesNode(), 0);
            }
        }
        return root;
    }

    public DefaultMutableTreeNode getDefaultRoot() {
        DefaultMutableTreeNode root =
            new DefaultMutableTreeNode(new ResourceBranch("Resource Lists"), true);
        root.insert(createExamplesNode(), 0);
        return root;
    }

    /**
     * Generates a list of example folders.
     */
    private static DefaultMutableTreeNode createExamplesNode() {
        ResourceBranch examplesBranch = new ResourceBranch(EXAMPLES_NODE_NAME);

        // Subscribe to central updating examples file
        //@todo move this to a more stable location.
        examplesBranch.setSubscription("http://wiki.astrogrid.org/pub/Astrogrid/VoExplorerSubscriptions/exampleResourceLists.xml");
        examplesBranch.setIconName("myspace16.png");
        DefaultMutableTreeNode examplesNode =
            new DefaultMutableTreeNode(examplesBranch, true);
        ResourceFolder[] folders = getExampleFolders();
        for (int i = 0; i < folders.length; i++) {
            examplesNode.add(new DefaultMutableTreeNode(folders[i], false));
        }
        return examplesNode;
    }

    /**
     * Determines whether the given node has an Examples element as a child.
     *
     * @param  node  node to test
     * @return  true iff node contains an examples folder
     */
    private static boolean hasExamples(DefaultMutableTreeNode node) {
        if (node.getAllowsChildren()) {
            for (Enumeration en = node.children(); en.hasMoreElements();) {
                DefaultMutableTreeNode child =
                    (DefaultMutableTreeNode) en.nextElement();
                ResourceFolder folder = (ResourceFolder) child.getUserObject();
                if (folder instanceof ResourceBranch &&
                    EXAMPLES_NODE_NAME.equals(folder.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

   

    /**
     * Returns an array of canned defaults for populating the tree.
     */
    private static ResourceFolder[] getExampleFolders() {
        ResourceFolder[] folders;
        try {
            folders = new ResourceFolder[] {
                    new XQueryList("Recent Changes",
                            "let $thresh := current-dateTime() - xs:dayTimeDuration('P30D')\n" // month
                            + "let $dthresh := current-date() - xs:dayTimeDuration('P30D')\n" // month
                            + "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')]\n"
                            + "where  ($r/@updated castable as xs:dateTime and xs:dateTime($r/@updated) > $thresh)\n"
                            + "or ($r/@updated castable as xs:date and xs:date($r/@updated) > $dthresh)\n"
                            + "or ($r/@created castable as xs:dateTime and xs:dateTime($r/@created) > $thresh)\n"
                            + "or ($r/@created castable as xs:date and xs:date($r/@created) > $dthresh)\n"
                            + "return $r"
                    ) //@todo need to find a way to avoid caching - or to control the caching period of this entry.
                    // I suppose default cache is 3 days - that's not too bad.
                    , new StaticList("VO taster list", new String[]{
                            "ivo://irsa.ipac/2MASS-PSC"
                                ,"ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/ceaApplication"
                                ,"ivo://nasa.heasarc/skyview/dss2"
                                ,"ivo://mast.stsci/siap-cutout/goods.hst"
                                ,"ivo://stecf.euro-vo/SSA/HST/FOS"
                                ,"http://leda.univ-lyon1.fr" //URRRK?
                                ,"ivo://org.astrogrid/HyperZ"
                                ,"ivo://wfau.roe.ac.uk/sdssdr5-dsa/cone"
                                ,"ivo://wfau.roe.ac.uk/schlegeldustmaps"
                                ,"ivo://nasa.heasarc/skyview/sdss"
                                ,"ivo://org.astrogrid/Starburst99"
                                ,"ivo://wfau.roe.ac.uk/ssa-dsa/ceaApplication"
                                ,"ivo://nasa.heasarc/rc3"
                                ,"ivo://wfau.roe.ac.uk/ukidssDR2-dsa/ceaApplication"                              
                    })
                    // examples by service type
                    , new StaticList("Cone search examples", new String[]{
                                "ivo://irsa.ipac/2MASS-XSC"
                                , "ivo://irsa.ipac/2MASS-PSC"
                                , "ivo://wfau.roe.ac.uk/6df-dsa/cone"
                                , "ivo://wfau.roe.ac.uk/first-dsa/cone"
                                , "ivo://nasa.heasarc/iraspsc"
                                , "ivo://wfau.roe.ac.uk/rosat-dsa/cone"
                                , "ivo://wfau.roe.ac.uk/sdssdr5-dsa/cone"
                                , "ivo://wfau.roe.ac.uk/ssa-dsa/cone"
                                , "ivo://ned.ipac/Basic_Data_Near_Position"
                                , "ivo://nasa.heasarc/rc3"
                                , "ivo://fs.usno/cat/usnob"  
                    })
                    , new StaticList("Image access examples",new String[]{
                            "ivo://irsa.ipac/2MASS-ASKYW-AT"
                                ,"ivo://nasa.heasarc/skyview/dss2"
                                ,"ivo://nasa.heasarc/skyview/first"
                                ,"ivo://mast.stsci/siap-cutout/goods.hst"
                                ,"ivo://nasa.heasarc/skyview/halpha"
                                ,"ivo://uk.ac.cam.ast/IPHAS/images/SIAP"
                                ,"ivo://nasa.heasarc/skyview/nvss"
                                ,"ivo://nasa.heasarc/skyview/rass"
                                ,"ivo://nasa.heasarc/skyview/sdss"
                    })
                    , new SmartList("Spectrum access examples","type = spectrum")
                    ,new SmartList("Remote applications","type = CeaApplication")
                    ,new StaticList("Queryable database examples",new String[]{
                            "ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/ceaApplication"
                                ,"ivo://wfau.roe.ac.uk/6df-dsa/ceaApplication"
                                ,"ivo://dev2.star.le.ac.uk/mysql-first-roe/ceaApplication"
                                ,"ivo://wfau.roe.ac.uk/iras-dsa/ceaApplication"
                                ,"ivo://wfau.roe.ac.uk/rosat-dsa/ceaApplication"
                                ,"ivo://wfau.roe.ac.uk/sdssdr5-dsa/ceaApplication"
                                ,"ivo://uk.ac.cam.ast/SWIRE/Catalogue/ceaApplication"
                                ,"ivo://wfau.roe.ac.uk/ssa-dsa/ceaApplication"
                                ,"ivo://wfau.roe.ac.uk/twomass-dsa/ceaApplication"
                                ,"ivo://wfau.roe.ac.uk/ukidssDR2-dsa/ceaApplication"                        
                    })
                    
                    // examples by wavelength / field.
                    ,new SmartList("IR redshift","(ucd = REDSHIFT) AND (waveband = Infrared)")
                    ,new SmartList("Solar services","subject=solar")
                    , new StaticList("SWIFT follow up",new String[]{
                            "ivo://sdss.jhu/openskynode/PSCZ"
                                ,"ivo://nasa.heasarc/rassvars"
                                ,"ivo://nasa.heasarc/rassbsc"
                                ,"ivo://nasa.heasarc/rassfsc"
                                ,"ivo://wfau.roe.ac.uk/sdssdr5-dsa/TDB"
                                ,"ivo://wfau.roe.ac.uk/ssa-dsa/cone"
                                ,"ivo://CDS/VizieR/I/267/out"
                                ,"ivo://ned.ipac/Basic_Data_Near_Position"
                                ,"ivo://fs.usno/cat/usnob"
                                ,"ivo://nasa.heasarc/xmmssc"                        
                    })
                    , new SmartList("Radio images","(waveband = Radio) and (type = Image)")
                    ,new SmartList("Vizier AGN tables","(publisher = vizier) and (subject = agn)") 
                    ,new SmartList("VOEvent services","default=voevent")
                    
            };
        }
        catch (InvalidArgumentException e) {
            throw new RuntimeException("Programming error", e);
        }
        for (int i = 0; i < folders.length; i++) {
            folders[i].setFixed(true);
        }
        return folders;
    }
}
