package org.astrogrid.desktop.modules.ui.folders;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.system.ProgrammerError;
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
    private static final String SANDBOX_NODE_NAME = "Sandpit";
    private static final String RECENT_NODE_NAME ="Recent Changes";

    /**
     * Constructor.
     *
     * @param    parent  context
     * @param    workdirPreference   directory for persistence
     * @param    persister   XML persistence implementation
     */
    public ResourceTreeProvider(final UIContext parent, final Preference workdirPreference, final Preference examplesPreference,
                                final XmlPersist persister) {
        super(parent, new File(new File(workdirPreference.getValue()),
                                        "resourceLists.xml"), persister);
        logger.info("Resource folders will be read from/written to " +
                    getStorageLocation());
        this.examplesLocation = examplesPreference.getValue();
        logger.info("Examples will be read from " +this.examplesLocation);
        init(new ResourceTreeModel(parent, persister));
        // listen to the examples preference..
        examplesPreference.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent evt) {
                refreshPreCannedLists((DefaultMutableTreeNode)getDefaultTreeModel().getRoot());
            }
        });
    }

    /**
     * Loads the tree representation from its persistent home on disk, and
     * doctors it if necessary to contain the precanned folders.  This will
     * normally only be necessary when loading a resources persisted by
     * an earlier version of this class.
     * Note however that it means the user cannot delete the precanned nodes
     * permenently.  I claim this is a Feature.
     */
    @Override
    public DefaultMutableTreeNode load() throws IOException, ServiceException {
        final DefaultMutableTreeNode root = super.load();
        if (root != null && root.getAllowsChildren()) {
            refreshPreCannedLists(root); 
        }
        return root;
    }
    /** overridden to prepopulate with examples, sandbox, and recent changes */
    @Override
    public DefaultMutableTreeNode getDefaultRoot() {
        final DefaultMutableTreeNode root =
            new DefaultMutableTreeNode(new ResourceBranch("Resource Lists"), true);
        root.insert(createRecentNode(),0);
        root.insert(createExamplesNode(), 1); 
        root.insert(createSandboxNode(),2);
        return root;
    }

    /** adjusts tree to remove any obselete examples nodes,
     * and ensures that a current exammples, sandbox, recent changes node is present.
     * @param root
     */
    private void refreshPreCannedLists(final DefaultMutableTreeNode root) {
        final DefaultMutableTreeNode recent = findResourceFolder(root, XQueryList.class, RECENT_NODE_NAME);
        if (recent == null || ! root.equals(recent.getParent())) { // no changes
            root.insert(createRecentNode(),0);
        }
        final DefaultMutableTreeNode examplesNode = findResourceFolder(root, ResourceBranch.class, EXAMPLES_NODE_NAME);
        if (examplesNode == null || ! root.equals(examplesNode.getParent())) { // no examples node
            root.insert(createExamplesNode(), 1);
        } else { // an obsolete examples note - either no subscription, or pointing to a different location than current preference.
            final ResourceFolder examplesFolder = (ResourceFolder) examplesNode.getUserObject();                
            if (examplesFolder.getSubscription() == null || ! examplesLocation.equals(examplesFolder.getSubscription())) {
            root.remove(examplesNode);
            root.insert(createExamplesNode(),1);
            }
        }

        final DefaultMutableTreeNode sandbox = findResourceFolder(root, ResourceBranch.class,SANDBOX_NODE_NAME);
        if (sandbox == null || ! root.equals(sandbox.getParent())) {
            root.insert(createSandboxNode(),2);
        }
    }
    
    // above code handles all required changes apart from changing contents of an existing examples folder
    // but this should update when we edit the subscription.



    /** seatch for a particular folder
     * @param searchRoot tree node to search from
     * @param searchClass the class of folder to look for.
     * @param searchName the name of the folder to look for
     * @return the first matching folder, or null.
     */
    private DefaultMutableTreeNode findResourceFolder(
            final DefaultMutableTreeNode searchRoot,
            final Class<? extends ResourceFolder> searchClass,
            final String searchName) {
        if (searchRoot.getAllowsChildren()) {
            for (final Enumeration<DefaultMutableTreeNode> en = searchRoot.children(); en.hasMoreElements();) {
                final DefaultMutableTreeNode child = en.nextElement();
                final ResourceFolder folder = (ResourceFolder) child.getUserObject();
                if ( searchClass.isInstance(folder) 
                    && searchName.equals(folder.getName())
                    ) {
                    return child;
                }
            }
        }
        return null;
    }
    
    // create the pre-canned nodes.
    /**
     * Generates the list of example folders. - acts as a backup for the examples subscription - these appear if the subscription is not available.
     */
    private DefaultMutableTreeNode createExamplesNode() {
        final ResourceBranch examplesBranch = new ResourceBranch(EXAMPLES_NODE_NAME);

        // Subscribe to central updating examples file
        examplesBranch.setSubscription(examplesLocation);
        examplesBranch.setIconName("myspace16.png");
        examplesBranch.setFixed(true); // indicates that these cannot be edited.
        examplesBranch.setDescription("Example lists - not editable");
        final DefaultMutableTreeNode examplesNode =new DefaultMutableTreeNode(examplesBranch, true);
        final ResourceFolder[] folders = exampleTemplate();
        for (int i = 0; i < folders.length; i++) {
            examplesNode.add(new DefaultMutableTreeNode(folders[i], false));
        }
        return examplesNode;
    }
    
    /** crearte the recent changes list */
    private DefaultMutableTreeNode createRecentNode() {
        final XQueryList list = new XQueryList("Recent Changes",
                "let $thresh := current-dateTime() - xs:dayTimeDuration('P10D')\n" // 10 days
                + "let $dthresh := current-date() - xs:dayTimeDuration('P10D')\n" // 10 days
                + "for $r in //vor:Resource[not (@status='inactive' or @status='deleted')]\n"
                + "where  ($r/@updated castable as xs:dateTime and xs:dateTime($r/@updated) > $thresh)\n"
                + "or ($r/@updated castable as xs:date and xs:date($r/@updated) > $dthresh)\n"
                + "or ($r/@created castable as xs:dateTime and xs:dateTime($r/@created) > $thresh)\n"
                + "or ($r/@created castable as xs:date and xs:date($r/@created) > $dthresh)\n"
                + "return $r"
        ); //@todo need to find a way to avoid caching - or to control the caching period of this entry.
        // I suppose `efault cache is 3 days - that's not too bad.
         list.setFixed(true);
         list.setDescription("Recent changes to the registry. May be slow.");
         list.setIconName("latest16.png");
         
         return new DefaultMutableTreeNode(list,false);
         
    }
    /** create the sandbox node */
    private DefaultMutableTreeNode createSandboxNode() {
        final ResourceBranch b = new ResourceBranch(SANDBOX_NODE_NAME);
        b.setDescription("Editable examples");
        b.setFixed(true); // cannot edit this folder, but can edit items below it.
        b.setIconName("annotate16.png");
        final DefaultMutableTreeNode sandboxNode =new DefaultMutableTreeNode(b, true);
        final ResourceFolder[] folders = sandboxTemplate();
        for (int i = 0; i < folders.length; i++) {
            sandboxNode.add(new DefaultMutableTreeNode(folders[i], false));
        }
        return sandboxNode;        
    }
    
    
    /////////////////////////////////////////////////////////
    // templates only below here.


    
    private static ResourceFolder[] sandboxTemplate() {
        final ResourceFolder[] folders;
        try {
            folders = new ResourceFolder[] {
                    new SmartList("IR redshift","(ucd = REDSHIFT) AND (waveband = Infrared)")

                    , new StaticList("SWIFT follow up",new String[]{
                            "ivo://fs.usno/cat/usnob"//
                            ,"ivo://nasa.heasarc/rassvars"//
                            ,"ivo://nasa.heasarc/rassbsc"//
                            ,"ivo://nasa.heasarc/iraspsc"//
                            ,"ivo://nasa.heasarc/rassfsc"//
                            ,"ivo://sdss.jhu/services/DR5CONE"//
                            ,"ivo://ned.ipac/Basic_Data_Near_Position"//
                            ,"ivo://nasa.heasarc/xmmssc"//
                            ,"ivo://wfau.roe.ac.uk/ssa-dsa" //
                    })

                    , new SmartList("Radio images","(waveband = Radio) and (capability = Image)")

                    , new SmartList("Vizier AGN tables","(publisher = CDS) and (subject = agn) and (subject = not magnetic")                    
                    
                    , new StaticList("Blazar selection", new String[] {
                            "ivo://nasa.heasarc/iraspsc",
                                "ivo://CDS.VizieR/VIII/71",
                                "ivo://nasa.heasarc/xmmssc",
                                "ivo://nrao.archive/nvsscatalog",
                                "ivo://CDS.VizieR/J/MNRAS/351/83",
                                "ivo://wfau.roe.ac.uk/ukidssDR3-dsa/wsa",
                                "ivo://wfau.roe.ac.uk/ssa-dsa",
                                "ivo://irsa.ipac/2MASS-PSC",
                                "ivo://CDS.VizieR/J/ApJS/175/97",
                                "ivo://wfau.roe.ac.uk/sss-siap",
                                "ivo://CDS.VizieR/J/ApJ/657/706",
                                "ivo://CDS.VizieR/J/A+A/436/799",
                                "ivo://nasa.heasarc/skyview/first",
                                "ivo://nasa.heasarc/crates",
                                "ivo://nasa.heasarc/rassfsc",
                                "ivo://nasa.heasarc/fermilbsl",
                                "ivo://nasa.heasarc/skyview/nvss",
                                "ivo://CDS.VizieR/J/AJ/129/2542",
                                "ivo://CDS.VizieR/J/PAZh/25/893",
                                "ivo://CDS.VizieR/III/157",
                               // "ivo://edit.me",
                                "ivo://CDS.VizieR/J/AJ/133/1947"                         
                    })
            };
        } catch (final InvalidArgumentException e) {
            throw new ProgrammerError(e);
        }
        return folders;
    }
   
    /**
     * Returns an array of canned defaults for populating the tree.
     * the subscribed version will take precedence over these, if available.
     */
    private static ResourceFolder[] exampleTemplate() {
        ResourceFolder[] folders;
        try {
            folders = new ResourceFolder[] {
                    new StaticList("VO taster list", new String[]{
                            "ivo://org.astrogrid/MERLINImager" //
                            ,"ivo://irsa.ipac/2MASS-PSC" //
                            ,"ivo://nasa.heasarc/fermilbsl" //
                            ,"ivo://wfau.roe.ac.uk/xmm_dsa/wsa"//
                            ,"ivo://stecf.euro-vo/SSA/HST/FOS" //
                            ,"ivo://uk.ac.cam.ast/2dFGRS/object-catalogue/Object_catalogue_2dF_Galaxy_Redshift_Survey"//
                            ,"ivo://stecf.euro-vo/siap/hst/preview" //
                            ,"ivo://uk.ac.cam.ast/iphas-dsa-catalog/IDR" //
                            ,"ivo://uk.ac.starlink/stilts" //
                          //  ,"ivo://sdss.jhu/services/DR5CONE"
                            ,"ivo://nasa.heasarc/rc3" //
                            ,"ivo://wfau.roe.ac.uk/ssa-dsa"
                            ,"ivo://uk.ac.cam.ast/IPHAS/images/SIAP" //
                            ,"ivo://mast.stsci/siap-cutout/goods.hst"                            
                            ,"ivo://wfau.roe.ac.uk/sdssdr7-dsa/dsa"
                            ,"ivo://wfau.roe.ac.uk/ukidssDR3-dsa/wsa"
                    })
                    // examples by service type
                    , new StaticList("Cone search examples", new String[]{
                            "ivo://fs.usno/cat/usnob"//
                            , "ivo://irsa.ipac/2MASS-PSC"//
                            , "ivo://nasa.heasarc/first" //
                            , "ivo://sdss.jhu/services/DR5CONE"//
                            , "ivo://nasa.heasarc/iraspsc"//
                            , "ivo://irsa.ipac/2MASS-XSC"//
                            , "ivo://wfau.roe.ac.uk/ssa-dsa" //
                            , "ivo://nasa.heasarc/rc3"//
                            , "ivo://wfau.roe.ac.uk/rosat-dsa/wsa"//
                            ,"ivo://nasa.heasarc/xmmssc"//
                            ,"ivo://wfau.roe.ac.uk/ukidssDR3-dsa/wsa"//
                            
                            ,
                    })
                    
                    , new StaticList("Image access examples",new String[]{
                            "ivo://wfau.roe.ac.uk/sss-siap"//
                            ,"ivo://nasa.heasarc/skyview/rass"//
                            ,"ivo://mast.stsci/siap/vla-first"//
                         //   ,"ivo://cadc.nrc.ca/siap/jcmt"
                            ,"ivo://cadc.nrc.ca/siap/hst"//
                            ,"ivo://org.astrogrid/HDFImager"//
                            ,"ivo://irsa.ipac/2MASS-ASKYW-AT"//
                            ,"ivo://nasa.heasarc/skyview/nvss"//
                            ,"ivo://uk.ac.cam.ast/IPHAS/images/SIAP" //
                            ,"ivo://mast.stsci/siap-cutout/goods.hst"//
                            ,"ivo://wfau.roe.ac.uk/ukidssdr2-siap"//
                    })
                    
                    , new SmartList("Spectrum access examples","capability = Spectral")
                    
                    , new SmartList("Remote applications","resourcetype = CeaApplication")
                    
                    , new SmartList("Table query services","capability = /TAP")

                    , new SmartList("Solar services","subject=solar")
                    
                    , new SmartList("VOEvent services","default=voevent")

            };
        }
        catch (final InvalidArgumentException e) {
            throw new ProgrammerError( e);
        }
        for (int i = 0; i < folders.length; i++) {
            folders[i].setFixed(true);
        }
        return folders;
    }
}
