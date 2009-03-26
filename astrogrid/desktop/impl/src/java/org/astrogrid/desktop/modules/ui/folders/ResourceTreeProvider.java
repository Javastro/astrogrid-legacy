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
 *@TEST
 * @author   Noel Winstanley
 * @author   Mark Taylor
 * @since    6 Sep 2007
 */
public class ResourceTreeProvider extends PersistentTreeProvider {

    /**
     * previously used subscription location. unused.
     */
  //  private static final String OLD_SUBSCRIPTION_LOCATION = "http://wiki.astrogrid.org/pub/Astrogrid/VoExplorerSubscriptions/exampleResourceLists.xml";
    /** new subscription location - supplied by preference.
     * 
     */
    private final String examplesLocation;
    private static final String EXAMPLES_NODE_NAME = "Examples";

    /**
     * Constructor.
     *
     * @param    parent  context
     * @param    workdirPreference   directory for persistence
     * @param    persister   XML persistence implementation
     */
    public ResourceTreeProvider(UIContext parent, Preference workdirPreference, Preference examplesPreference,
                                XmlPersist persister) {
        super(parent, new File(new File(workdirPreference.getValue()),
                                        "resourceLists.xml"), persister);
        logger.info("Resource folders will be read from/written to " +
                    getStorageLocation());
        this.examplesLocation = examplesPreference.getValue();
        logger.info("Examples will be read from " +this.examplesLocation);
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
    @Override
    public DefaultMutableTreeNode load() throws IOException, ServiceException {
        DefaultMutableTreeNode root = super.load();
        if (root != null && root.getAllowsChildren()) {
            DefaultMutableTreeNode examplesNode = findExamples(root);
            if (examplesNode == null) { // no examples node
                root.insert(createExamplesNode(), 0);
            } else { // an obsolete examples note - either no subscription, or pointing to a different location than current preference.
                ResourceFolder examplesFolder = (ResourceFolder) examplesNode.getUserObject();                
                if (examplesFolder.getSubscription() == null || ! examplesLocation.equals(examplesFolder.getSubscription())) {
                root.remove(examplesNode);
                root.insert(createExamplesNode(),0);
                }
            }
        }
        return root;
    }

    @Override
    public DefaultMutableTreeNode getDefaultRoot() {
        DefaultMutableTreeNode root =
            new DefaultMutableTreeNode(new ResourceBranch("Resource Lists"), true);
        root.insert(createExamplesNode(), 0);
        return root;
    }

    /**
     * Generates a list of example folders.
     */
    private DefaultMutableTreeNode createExamplesNode() {
        ResourceBranch examplesBranch = new ResourceBranch(EXAMPLES_NODE_NAME);

        // Subscribe to central updating examples file
        examplesBranch.setSubscription(examplesLocation);
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
     * @return  the MutableTreeNode containing the examples resource folder, or null if not present
     */
    private DefaultMutableTreeNode findExamples(DefaultMutableTreeNode node) {
        if (node.getAllowsChildren()) {
            for (Enumeration en = node.children(); en.hasMoreElements();) {
                DefaultMutableTreeNode child =
                    (DefaultMutableTreeNode) en.nextElement();
                ResourceFolder folder = (ResourceFolder) child.getUserObject();
                if (folder instanceof ResourceBranch 
                    && EXAMPLES_NODE_NAME.equals(folder.getName())
                    ) {
                    return child;
                }
            }
        }
        return null;
    }

  
    /**
     * Returns an array of canned defaults for populating the tree.
     * the subscribed version will take precedence over these, if available.
     */
    private static ResourceFolder[] getExampleFolders() {
        ResourceFolder[] folders;
        try {
            folders = new ResourceFolder[] {
                    new XQueryList("Recent Changes",
                            "let $thresh := current-dateTime() - xs:dayTimeDuration('P10D')\n" // 10 days
                            + "let $dthresh := current-date() - xs:dayTimeDuration('P10D')\n" // 10 days
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
                            ,"ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/Object_catalogue_2dF_Galaxy_Redshift_Survey"
                                ,"ivo://mast.stsci/siap-cutout/goods.hst" //??
                              //  ,"ivo://wfau.roe.ac.uk/ukidssWorld-dsa/wsa"
                              // Richard's amendment  
                                ,"ivo://wfau.roe.ac.uk/ukidssDR1-dsa/wsa" // check
                                ,"ivo://wfau.roe.ac.uk/xmm_dsa/wsa"
                                ,"ivo://uk.ac.cam.ast/iphas-dsa-catalog/IDR"
                                ,"ivo://uk.ac.cam.ast/IPHAS/images/SIAP"
                                ,"ivo://stecf.euro-vo/siap/hst/preview"
                                ,"ivo://wfau.roe.ac.uk/schlegeldustmaps"    //?  
                                ,"ivo://nasa.heasarc/skyview/sdss"            
                                ,"ivo://wfau.roe.ac.uk/ssa-dsa/ssa"
                                ,"ivo://org.astrogrid/MERLINImager" 
                                ,"ivo://uk.ac.starlink/stilts"
                                ,"ivo://stecf.euro-vo/SSA/HST/FOS"//?
                                ,"ivo://nasa.heasarc/rc3"                                                        
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
                                ,"ivo://org.astrogrid/HDFImager"
                                ,"ivo://org.astrogrid/MERLINImager"
                    })
                    , new SmartList("Spectrum access examples","type = Spectral")
                    ,new SmartList("Remote applications","type = CeaApplication")
                    ,new StaticList("Queryable database examples",new String[]{
                            "ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/Object_catalogue_2dF_Galaxy_Redshift_Survey"
                                ,"ivo://wfau.roe.ac.uk/6df-dsa/wsa"
                                ,"ivo://wfau.roe.ac.uk/ukidssDR3-v1/wsa"
                                ,"ivo://wfau.roe.ac.uk/iras-dsa/wsa"
                                ,"ivo://wfau.roe.ac.uk/rosat-dsa/wsa"
                                ,"ivo://uk.ac.cam.ast/iphas-dsa-catalog/IDR"
                                ,"ivo://wfau.roe.ac.uk/sdssdr5-dsa/dsa"
                                ,"ivo://wfau.roe.ac.uk/ssa-dsa/ssa"
                                ,"ivo://wfau.roe.ac.uk/twomass-dsa/wsa"
                                ,"ivo://uk.ac.ucl.star/newhipparcos-dsa-catalog/HIPPARCOS_NEWLY_REDUCED"
                                ,"ivo://uk.ac.cam.ast/INT-WFS/observation-catalogue/INT_WFS_DQC"
                                ,"ivo://uk.ac.cam.ast/INT-WFS/merged-object-catalogue/INT_WFS_Merged_Object_catalogue"
                                ,"ivo://wfau.roe.ac.uk/glimpse-dsa/wsa"
                                ,"ivo://wfau.roe.ac.uk/first-dsa/wsa" 
                                ,"ivo://wfau.roe.ac.uk/xmm_dsa/wsa"
                                ,"ivo://wfau.roe.ac.uk/ukidssDR1-dsa/wsa"
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
