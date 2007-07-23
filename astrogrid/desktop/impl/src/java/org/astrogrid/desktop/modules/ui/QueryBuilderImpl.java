/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.HeadlessException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContributionBuilder;
import org.astrogrid.desktop.modules.ui.actions.BuildQueryActivity;
import org.astrogrid.desktop.modules.ui.execution.ExecutionTracker;
import org.astrogrid.desktop.modules.ui.taskrunner.TaskRunnerImpl;

/** subclass of taskrunnner that takes care of query building.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 22, 20076:50:14 PM
 */
public class QueryBuilderImpl extends TaskRunnerImpl implements
        QueryBuilderInternal {



    public QueryBuilderImpl(UIContext context, ApplicationsInternal apps,
            ResourceChooserInternal rci, RegistryGoogle regChooser,
            ExecutionTracker tracker,
            UIContributionBuilder menuBuilder, TypesafeObjectBuilder builder
            ,Registry reg)
            throws HeadlessException {
        super(context, apps, rci, regChooser, tracker, menuBuilder, builder);
        this.reg = reg;
    }

    private final Registry reg;
    
    // overridden - to prefer adql apps first.
    public void buildForm(Resource r) {
        CeaApplication cea = null;
        if (r instanceof CeaApplication) {
            cea = (CeaApplication)r;
        } else {
            // try to find a 'synthetic' cea application.
            try {
                //@todo - should probably do this in a bg thread.
                cea = apps.getCeaApplication(r.getId());
            } catch (ACRException x) {
                logger.error("ServiceException",x);
                //@todo report a fault somewherte..
                return;
            }
        }
        String name = BuildQueryActivity.findNameOfFirstADQLInterface(cea);
        if (name != null) {
            pForm.buildForm(name,cea);
            pForm.getBottomPane().setCollapsed(false);
        } else { // show what we've got then - unlikely in this case
            pForm.buildForm(cea);
        }
        display(cea);
    }
    
    
// cea internal interface - need to harmonize this with taskrunner at some point    
    public void build(CeaApplication app) {
        buildForm(app);
    }

    public void build(DataCollection coll) {
        // find the related cea app, and go from there...
        String s = StringUtils.substringBeforeLast(coll.getId().toString(),"/");
        try {
            URI id = new URI(s + "/ceaApplication");
            Resource app = reg.getResource(id);
            if (app != null && app instanceof CeaApplication) {
                build((CeaApplication)app);
            } else {
                logger.error("not found");
            }
        } catch (Exception x) {
            logger.error("Failed to find associated app",x);
            //@todo report inability to show ui.
        }
    }

    public void edit(FileObject fo) {
        //@fixme implement
    }
    

}
