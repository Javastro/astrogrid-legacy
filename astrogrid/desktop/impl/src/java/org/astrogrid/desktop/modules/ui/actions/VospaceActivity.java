/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.VospaceService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.FileManagerInternal;

/** Activity that offers to open vospace in a fileexplorer
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 6, 20094:48:11 PM
 */
public class VospaceActivity extends AbstractResourceActivity {

    private final FileManagerInternal fm;
    
    
    /**
     * @param fm
     */
    public VospaceActivity(final FileManagerInternal fm) {        
        this.fm = fm;
        setHelpID("activity.vospace");
        setText("Open VOSpace");
        setIcon(IconHelper.loadIcon("anystorage16.png"));
        setToolTipText("Open this VOSpace in a new File Explorer window");
    }


    @Override
    protected boolean invokable(final Resource r) {
        return r instanceof VospaceService;
    }
    
    @Override
    public void someSelected(final Resource[] list) {
        noneSelected();
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final List<Resource> l = computeInvokable();
        final VospaceService vos = (VospaceService)l.get(0);
        fm.show(
                vos.findVospaceCapability().getVospaceRoot()
                );
    }

}
