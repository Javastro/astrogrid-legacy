/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ivoa.VosiAvailabilityBean;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ivoa.VosiInternal;

/** Query a VOSI capability to produce annotations.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 8, 20085:54:15 PM
 */
public class VosiAnnotationSource extends DynamicAnnotationSource {

    private final VosiInternal vosi;
    
    /**
     * @param vosi
     */
    public VosiAnnotationSource(final VosiInternal vosi) {
        super(URI.create("ivo://vosi"),"Availability");
        this.vosi = vosi;

    }

    @Override
    public Annotation getAnnotationFor(final Resource r) {
        if (!(r instanceof Service)) { // not a suitable things to produce annotations in
            return null; 
        }
        try {
            final VosiAvailabilityBean b = vosi.checkAvailability(r.getId());
            final Annotation ann =new Annotation();
            ann.setResourceId(r.getId());
            ann.setSource(this);
            ann.setNote(vosi.makeTooltipFor(b));
         
            return ann;
        } catch (final InvalidArgumentException x) { // this service doesn't have an availability cap.            
            return null;
        }                    
    }

    @Override
    public boolean shouldCache() {
        return true;
    }

}
