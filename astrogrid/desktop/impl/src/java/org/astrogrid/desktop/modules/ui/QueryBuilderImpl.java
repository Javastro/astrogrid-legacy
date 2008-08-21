/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.HeadlessException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaServerCapability;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.Relationship;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.ResourceName;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.dialogs.RegistryGoogleInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl;
import org.astrogrid.desktop.modules.votech.VoMonInternal;

/** subclass of taskrunnner that takes care of query building.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 22, 20076:50:14 PM
 */
public class QueryBuilderImpl extends TaskRunnerImpl implements
        QueryBuilderInternal {



    public QueryBuilderImpl(final UIContext context, final ApplicationsInternal apps,
            final RemoteProcessManagerInternal rpmi
            ,final ResourceChooserInternal rci, final RegistryGoogleInternal regChooser,
             final TypesafeObjectBuilder builder
            , final VoMonInternal vomon
            ,final Registry reg)
            throws HeadlessException {
        super(context, apps, rpmi,rci, regChooser, builder,vomon);
        this.reg = reg;
    }

    private final Registry reg;
    
    // overridden - to prefer adql apps first.
    @Override
    protected void selectStartingInterface(final CeaApplication cea) {
        // now that's working in the background, work out what we should be building a form for.
        final String name = BuildQueryActivity.findNameOfFirstADQLInterface(cea);
        if (name != null) {
            pForm.buildForm(name,cea);
            pForm.setExpanded(true);
        } else { // show what we've got then
            pForm.buildForm(cea);
        }
    }
    
// cea internal interface - need to harmonize this with taskrunner at some point    
    public void build(final CeaApplication app) {
        buildForm(app);
        super.showHideFullEditor.setEnabled(true);
    }

    public void build(final CatalogService coll) {
        // find the related cea app, and go from there...

        URI id = null;
        //
        // first - try to find a relationship, and extract an id from this.       
        final Relationship[] relationships = coll.getContent().getRelationships();      
        if (relationships != null) {
            for (int i = 0; i < relationships.length; i++) {
                final Relationship r = relationships[i];
                if ("service-for".equalsIgnoreCase(r.getRelationshipType()) && r.getRelatedResources().length > 0) {
                    final ResourceName rn = r.getRelatedResources()[0];
                    if (rn.getId() != null) {
                        id = rn.getId(); // id attribute isn't provided at the moment.
                    } else {
                        try {
                            id = new URI(rn.getValue());
                        } catch (final URISyntaxException e) {
                        }
                    }                        
                }
            }
        }
        //
        // If we fail, look for a suitable capability...
        if( id == null ) {
            final Capability[] capabilities = coll.getCapabilities() ;
            if( capabilities != null ) {
                for( int i=0; i<capabilities.length; i++ ) {
                    final Capability c = capabilities[i] ;
                    if( c instanceof CeaServerCapability  ) {
                        final CeaServerCapability ceac = (CeaServerCapability)c ;
                        final URI[] apps = ceac.getManagedApplications() ;
                        if( apps != null ) {
                            if( apps.length != 0 ) {
                                if( apps.length == 1 ) {
                                    id = apps[0] ;
                                }
                                else {
                                    id = apps[ new Random().nextInt( apps.length ) ] ;
                                }
                            }
                        }
                    }
                }
            }
        }
        //
        // No point in going further if we've found nothing suitable so far...
        if( id == null ) {
            showError("Failed to find an application associated with this catalog service");
            return ;
        }
//        if (id == null) { // fallback to string-mangling
//            String s = StringUtils.substringBeforeLast(coll.getId().toString(),"/");
//            try {
//                id = new URI(s + "/ceaApplication");
//            } catch (URISyntaxException x) {
//                // oh, I give up!!
//                showError("Failed to find an application associated with this catalog service");                
//            }
//        }
        
        final URI ceaAppId = id; // pesky finals
        
        // do the query to find a related cea app on a bg thread.
        (new BackgroundOperation("Finding application definition for this catalog",Thread.MAX_PRIORITY) {

            @Override
            protected Object construct() throws Exception {
                
                final Resource app =  reg.getResource(ceaAppId);
                if (app != null && app instanceof CeaApplication) {
                    return app;
                } else {
                    throw new ACRException("Failed to find an application associated with this catalog service");
                }
            }
            @Override
            protected void doFinished(final Object result) {
                build((CeaApplication)result);
            }
        }).start();

    }

    @Override
    public void edit(final FileObject fo) {
       super.edit(fo);
    }
    

}
