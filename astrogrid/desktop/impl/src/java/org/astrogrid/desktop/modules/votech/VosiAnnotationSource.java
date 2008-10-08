/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.net.URI;
import java.text.DateFormat;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ivoa.Vosi;
import org.astrogrid.acr.ivoa.VosiAvailabilityBean;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;

/**Annotation source that produces annotations from VOSI availability.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 8, 20085:54:15 PM
 */
public class VosiAnnotationSource extends DynamicAnnotationSource {

    private final Vosi vosi;
    
    /**
     * @param vosi
     */
    public VosiAnnotationSource(final Vosi vosi) {
        super(URI.create("ivo://vosi"),"Availability");
        this.vosi = vosi;
        df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
    }
    private final DateFormat df;
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
            final StringBuilder sb = new StringBuilder();
            if (b.isAvailable()) {
                if (b.getDownAt() != null) {
                    sb.append("OK until " + df.format(b.getDownAt()));
                } else {
                    sb.append("Service OK");
                }
                
            } else {                      
                if (b.getBackAt() != null) {
                    sb.append("Down until " + df.format(b.getBackAt()));
                } else {
                    sb.append("Service Down");
                }
            }
            if (b.getUpSince()!= null) {
                sb.append("<br>Up since " +df.format(b.getUpSince()));
            }
            final String[] notes = b.getNotes();
            if (notes != null && notes.length > 0) {
                sb.append("<p>");
                for (int i = 0; i < notes.length; i++) {
                    sb.append("<br>")
                        .append(notes[i]);
                }                 
            }            
            ann.setNote(sb.toString());
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
