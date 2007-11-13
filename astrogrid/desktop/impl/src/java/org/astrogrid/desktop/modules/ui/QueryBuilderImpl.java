/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.HeadlessException;
import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
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



    public QueryBuilderImpl(UIContext context, ApplicationsInternal apps,
            RemoteProcessManagerInternal rpmi
            ,ResourceChooserInternal rci, RegistryGoogle regChooser,
             TypesafeObjectBuilder builder
            , VoMonInternal vomon
            ,Registry reg)
            throws HeadlessException {
        super(context, apps, rpmi,rci, regChooser, builder,vomon);
        this.reg = reg;
    }

    private final Registry reg;
    
    // overridden - to prefer adql apps first.
    protected void selectStartingInterface(CeaApplication cea) {
        // now that's working in the background, work out what we should be building a form for.
        String name = BuildQueryActivity.findNameOfFirstADQLInterface(cea);
        if (name != null) {
            pForm.buildForm(name,cea);
            pForm.setExpanded(true);
        } else { // show what we've got then
            pForm.buildForm(cea);
        }
    }
    
// cea internal interface - need to harmonize this with taskrunner at some point    
    public void build(CeaApplication app) {
        buildForm(app);
    }

    public void build(DataCollection coll) {
        // find the related cea app, and go from there...
        final String s = StringUtils.substringBeforeLast(coll.getId().toString(),"/");
        // do the query to find a related cea app on a bg thread.
        (new BackgroundOperation("Finding application definition for this collection") {

            protected Object construct() throws Exception {
                URI id = new URI(s + "/ceaApplication");
                Resource app =  reg.getResource(id);
                if (app != null && app instanceof CeaApplication) {
                    return app;
                } else {
                    throw new ACRException("Failed to find an application associated with this collection");
                }
            }
            protected void doFinished(Object result) {
                build((CeaApplication)result);
            }
        }).start();

    }

    public void edit(FileObject fo) {
       super.edit(fo);
    }
    

}
