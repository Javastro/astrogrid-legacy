/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.system.XmlPersist;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Handles the messy business of reading / writing anntotation files.
 * Keeps it separate from the rest of the implementation, 
 * and allows it to be switched around later.
 * 
 * at the moment only the reading-writing of single annotation files is properly developeed, tested and used.
 * loading/saving of the list of annotation sources is undeveloped at the moment.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20078:34:03 PM
 */
public class AnnotationIO {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(AnnotationIO.class);

	private final File workDir;
	private final AnnotationSource userSource;
	private final File annotationSourceList; // unused at the moment.
	private final File userAnnotationsFile;
	private final Set userAnnotationIds; // set of resource id's that contain user annotations.

    private final XmlPersist xml;

    private final AnnotationServiceImpl service;
		
	public AnnotationIO(final Preference workDirPref, IterableObjectBuilder srcBuilder,XmlPersist xml, AnnotationServiceImpl service) {
		super();
        this.xml = xml;
        this.service = service;
		userAnnotationIds = new HashSet();
		this.workDir = new File(workDirPref.toString());
		userAnnotationsFile =  new File(workDir,"user-annotations.xml");
		this.userSource = new AnnotationSource(userAnnotationsFile.toURI(),"User");
		this.userSource.setSortOrder(0); // most important.
		logger.info("Will load user annotations from " + userSource.getSource());
		
		
		this.annotationSourceList = new File(workDir,"annotation-sources.xml");
		//logger.info("Will load annotation sources from " + annotationSourceList);
		
		sources = new ArrayList();
		sources.add(userSource);
		for (Iterator i = srcBuilder.creationIterator(); i.hasNext();) {
			AnnotationSource src = (AnnotationSource) i.next();
			sources.add(src);
		}
				
	
	}

	//temporary hard-coded annotation source list
	//@todo make more editable.
	public List loadAnnotationSourceList() {
		return sources;
	}
	
	private final List sources ;
	
	// does nothing - unimplemented
	public void saveAnnotationSourceList(AnnotationSource[] list) {
	    //unimplemented
	}
	
	/** get the special 'user' annotation source */
	public AnnotationSource getUserSource() {
		return userSource;
	}
	
// management of annotations
	public Collection load(AnnotationSource source) {
		InputStream is = null;
			try {
				is = source.getSource().toURL().openStream();
				Collection anns = (Collection)xml.fromXml(is);
				if (anns != null) {
					logger.info("Read " + anns.size() + " from " + source);
					if (source.equals(userSource)) { // keep a list of the id's of the resoues which have a user annotation.
					    // this is used afterwards to locate these resources when persisting.
						userAnnotationIds.clear();						
						for(Iterator i = anns.iterator(); i.hasNext(); ) {
						    UserAnnotation  a = (UserAnnotation)i.next();
						    userAnnotationIds.add(a.getResourceId());
						}
					}
					return anns;
				} else {
					logger.info("Empty Source " + source);
				}
			} catch (MalformedURLException x1) {
				logger.warn(x1.getMessage());
			} catch (IOException x1) {
				logger.warn(x1.getMessage());					
			} catch (ServiceException x) {
                logger.warn(x.getMessage());
            } finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException x1) {
					    //meh
					}
				}				
			}
			// something's gone wrong - so just return an empty array.
			return Collections.EMPTY_LIST;
		}
	
	// user annotations.
	/** mark a user annotation as updated, and persist the list */
	public void updateUserAnnotation(UserAnnotation ann) {
	        userAnnotationIds.add(ann.getResourceId());
	        saveUserAnnotations();
	}
	/** mark a user annotation as removed, and persist the list */
	public void removeUserAnnotation(Resource r) {
           userAnnotationIds.remove(r.getId());
           saveUserAnnotations();
   }

	/**
	 * persist the user anotations. - finds the user annotations from the cache, and then saves then to disk.
	 */
	private void saveUserAnnotations() {
	    new BackgroundWorker(service.ui,"Saving user annotations") {
	        {
	            setTransient(true);
	        }

	        protected Object construct() throws Exception {
	            OutputStream fos = null;
	            try {
	                List persistList = new ArrayList(userAnnotationIds.size());
	                for (Iterator i = userAnnotationIds.iterator(); i.hasNext();) {
	                    URI id = (URI) i.next();
	                    UserAnnotation a = service.getUserAnnotation(id);
	                    if (a != null) {
	                        persistList.add(a);
	                    }
	                }
	                fos = new FileOutputStream(userAnnotationsFile);
	                xml.toXml(persistList,fos);
	            } finally {
	                if (fos != null) {
	                    try {
	                        fos.close();
	                    } catch (IOException x) {
	                        logger.error("Failed to save user annotations",x);
	                    }
	                }
	            }		
	            return null;
	        }
	    }.start();
	}

	}