/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.util.Collection;
import java.util.List;

import org.astrogrid.acr.ivoa.resource.Resource;

/**
 * Encapsulates the messy business of reading / writing anntotation files.
 * Keeps it separate from the rest of the implementation, 
 * and allows it to be switched around later.
 * 
 * at the moment only the reading-writing of single annotation files is properly developeed, tested and used.
 * loading/saving of the list of annotation sources is undeveloped at the moment.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 22, 20089:55:41 PM
 */
public interface AnnotationIO {

    /** list of sources that annotations are being loaded from 
     * returns an unmodifiable collection*/
    public List<AnnotationSource> getSourcesList();

    // does nothing - unimplemented
    public void saveAnnotationSourceList(AnnotationSource[] list);

    /** get the special 'user' annotation source */
    public AnnotationSource getUserSource();

    /** load the set of annotations from disk */
    public Collection load(AnnotationSource source);

    // user annotations.
    /** mark a user annotation as updated, and persist the list */
    public void updateUserAnnotation(UserAnnotation ann);

    /** mark a user annotation as removed, and persist the list */
    public void removeUserAnnotation(Resource r);

}