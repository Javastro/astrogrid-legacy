/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.MultiConeInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

/** Open multicone on a resource or votable.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 15, 20097:43:28 PM
 */
public class MultiConeActivity extends AbstractFileOrResourceActivity {

    private final MultiConeInternal multi;
    
    /**
     * @param multi
     */
    public MultiConeActivity(final MultiConeInternal multi) {        
        this.multi = multi;
        setHelpID("activity.multicone");
        setText("Multi Query");
        setIcon(IconHelper.loadIcon("multicone16.png"));
        setToolTipText("Query the selected service using a file containing multiple positions and concatenate the output");
    }

    @Override
    protected boolean invokable(final FileObjectView f) {
        return f.getType().hasContent()
                && VoDataFlavour.VOTABLE.equals(f.getContentType());
    }

    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof ConeService;
        // can't accept siap, etc because muticone cea task won't accept it.
    }
    @Override
    public void manySelected(final FileObjectView[] l) {
        noneSelected(); // can't operate on more than one file.
    }

    @Override
    public void someSelected(final Resource[] l) {
        noneSelected(); // can't operate on more than one resource;
    }

    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final List<Resource> resources = computeInvokableResources();
        switch(resources.size()) {
            case 0:
                break;
            case 1:
                final ConeService r = (ConeService) resources.get(0);
                multi.multiCone(r);
                break;
            default:
                //can'\t edit more than one.           
        }
        final List<FileObjectView> files= computeInvokableFiles();
        switch(files.size()) {
            case 0:
                break;
            case 1:
                final FileObjectView fo = files.get(0);
              (new BackgroundWorker<URI>(uiParent.get(),"Opening in Multi-Cone") {

                @Override
                protected URI construct() throws Exception {
                    // resolve to innermost reference.
                    final FileObject f = AstroscopeFileObject.findAstroscopeOrInnermostFileObject(fo.getFileObject());
                    final URL u = f.getURL();
                    return u.toURI();
                }
                @Override
                protected void doFinished(final URI u) {                    
                    multi.multiCone(u);
                }
              }).start();
 
                break;
            default:
               // can't edit multiple
        }
    }

}
