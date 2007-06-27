/**
 * 
 */
package org.astrogrid.desktop.modules.votech;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.hivemind.IterableObjectBuilder;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.folders.Folder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.awt.Color;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/** Handles the messy business of reading / writing anntotation files.
 * Keeps it separate from the rest of the implementation, 
 * and allows it to be switched around later.
 * 
 * @todo reimplement - find a way to avoid keeping list of userAnnotations in memory
 * 	- but still be able to write all out to a file.
 * random access file of some kind?
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 18, 20078:34:03 PM
 */
public class AnnotationIO implements ExceptionListener{
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(AnnotationIO.class);

	private final File workDir;
	private final AnnotationSource userSource;
	private final File annotationSourceList;
	private final File userAnnotationsFile;
	private final List userAnnotations;
	
	private final XStream xstream;
	
	/** converter for java.net.URI */
	public static class URIConverter extends AbstractSingleValueConverter{

		public boolean canConvert(Class arg0) {
			return arg0.equals(URI.class);
		}

		public Object fromString(String arg0) {
			return URI.create(arg0);
		} 
	}
	
	/** convertor for java.awt.Color */
	public static class ColorConverter implements Converter {

		public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
			Color c = (Color)arg0;
			int i = c.getRGB();
			arg1.setValue("#" + Integer.toHexString(i).substring(2,8)); // omit the alpha chanel, otherwise can't parse back in. odd.
		}

		public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
			try {
				return Color.decode(arg0.getValue());
			} catch (NumberFormatException e) {
				return Color.WHITE;
			}
		}

		public boolean canConvert(Class arg0) {
			return arg0.equals(Color.class);
		}
	}
	
	
	public AnnotationIO(final Preference workDirPref, IterableObjectBuilder srcBuilder) {
		super();
		userAnnotations = new ArrayList();
		this.workDir = new File(workDirPref.toString());
		userAnnotationsFile =  new File(workDir,"user-annotations.xstream");
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
				
		xstream = new XStream(new PureJavaReflectionProvider()); // @todo configure to use stax too?
		xstream.alias("annotation",UserAnnotation.class);
		xstream.registerConverter(new URIConverter());
		xstream.registerConverter(new ColorConverter());
		xstream.omitField(Annotation.class,"source");
		xstream.addImmutableType(Color.class);
	}

	//temporary hard-coded annotation source list
	//@fixme make more editable.
	List loadAnnotationSourceList() {
		return sources;
	}
	
	private final List sources ;
	
	// does nothing
	void saveAnnotationSourceList(AnnotationSource[] list) {
	}
	
	
	/** get the special 'user' annotation source */
	public AnnotationSource getUserSource() {
		return userSource;
	}
	
	
	
// management of annotations
	//@todo replace with XStream.
	Annotation[] load(AnnotationSource source) {
		InputStream is = null;

			//@todo open streams in a more robust way (allowing for ivo://) later.
			try {
				is = source.getSource().toURL().openStream();
				Annotation[] anns = (Annotation[])xstream.fromXML(is);
				if (anns != null) {
					logger.info("Read " + anns.length + " from " + source);
					if (source.equals(userSource)) {
						userAnnotations.addAll(Arrays.asList(anns));
					}
					return anns;
				} else {
					logger.info("Empty Source " + source);
				}
			} catch (MalformedURLException x1) {
				logger.warn(x1.getMessage());
			} catch (IOException x1) {
				logger.warn(x1.getMessage());					
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException x1) {
					}
				}				
			}
			// something's gone wrong - so just return an empty array.
			return new Annotation[0];		
		}
	/** load a list of annotations from an annotation source */
	Annotation[] loadSunXMLPersistence(AnnotationSource source) {
		InputStream is = null;
		XMLDecoder x = null;

			//@todo open streams in a more robust way (allowing for ivo://) later.
			try {
				is = source.getSource().toURL().openStream();
				x = new XMLDecoder(is, this,this);
				Annotation[] anns = (Annotation[])x.readObject();
				if (anns != null) {
					logger.info("Read " + anns.length + " from " + source);
					return anns;
				} else {
					logger.info("Empty Source " + source);
				}
			} catch (MalformedURLException x1) {
				logger.error("MalformedURLException",x1);
			} catch (IOException x1) {
				logger.error("IOException",x1);
			} catch (ArrayIndexOutOfBoundsException ex) {
				// thrown when the file contains no data - a bit crap - no way to check this.
				// fail gracefully.
				logger.warn("Empty Source " + source);
			} catch (NoSuchElementException ex) {
				// thrown when the file contains no data - a bit crap - no way to check this.
				// fail gracefully.
				logger.warn("Empty Source" + source);						
			} finally {
				if (x != null) {
					x.close();
				} 
				if (is != null) {
					try {
						is.close();
					} catch (IOException x1) {
						logger.error("IOException",x1);
					}
				}				
			}
			// something's gone wrong - so just return an empty array.
			return new Annotation[0];		
		}

	// user annotations.
	/** add a new user annotation, and persist the whole lot */
	void addUserAnnotation(UserAnnotation ann) {
		userAnnotations.add(ann);
		saveUserAnnotations();
	}
	
	/** update a user annotation, and persist the whole lot */
	void updateUserAnnotation(UserAnnotation ann) {
		int ix = 0;
		for (Iterator i = userAnnotations.iterator(); i.hasNext();ix++) {
			UserAnnotation ua = (UserAnnotation) i.next();
			if (ua.getResourceId().equals(ann.getResourceId())) {
				userAnnotations.set(ix,ann);
			}
		}
		saveUserAnnotations();
	}
	
	/** update a user annotation, and persist the whole lot */
	void removeUserAnnotation(UserAnnotation ann) {
		userAnnotations.remove(ann);
		saveUserAnnotations();
	}

	
	/**
	 * persist the user anotations.
	 */
	private void saveUserAnnotations() {
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(userAnnotationsFile);
			UserAnnotation[] arr = (UserAnnotation[])userAnnotations.toArray(new UserAnnotation[userAnnotations.size()]);
			xstream.toXML(arr,fos);
		} catch (FileNotFoundException x) {
			logger.error("Failed to save user annotations",x);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException x) {
					logger.error("Failed to save user annotations",x);
				}
			}
		}		
	}
	private void saveUserAnnotationsSeriaization() {
		ObjectOutputStream fos = null;
		try {
			fos = new ObjectOutputStream(new FileOutputStream(userAnnotationsFile));
			UserAnnotation[] arr = (UserAnnotation[])userAnnotations.toArray(new UserAnnotation[userAnnotations.size()]);
			fos.writeObject(arr);
		} catch (FileNotFoundException x) {
			logger.error("Failed to save user annotations",x);
		} catch (IOException x) {
			logger.error("IOException",x);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException x) {
					logger.error("Failed to save user annotations",x);
				}
			}
		}		
	}

	/** exception handler */
	public void exceptionThrown(Exception e) {
		logger.warn("Failed to read source - " + e.getMessage());
		logger.debug(e);
	}
	}