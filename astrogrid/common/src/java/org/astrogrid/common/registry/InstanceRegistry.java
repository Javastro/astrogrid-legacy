package org.astrogrid.common.registry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.astrogrid.common.creator.InstanceCreator;
import org.astrogrid.common.creator.InstanceCreatorException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class InstanceRegistry {
  // Members.
  private Properties registry;
  private InstanceCreator creator;
  
  public InstanceRegistry(Properties registry) {
    this(registry, new InstanceCreator());
  }
  
  public InstanceRegistry(String filename) throws FileNotFoundException, IOException {
    this(new FileInputStream(filename));
  }

  public InstanceRegistry(InputStream inStream) throws IOException {
    this(new Properties());
    registry.load(inStream);
  }

  public InstanceRegistry(Properties registry, InstanceCreator creator) {
    this.registry = registry;
    this.creator = creator;
  }
  
  public Object getInstance(String property) throws InstanceCreatorException {
    return getInstance(property, (String) null, (Object[]) null);
  }
  
  public Object getInstance(String property, String methodName, Object[] parameters) throws InstanceCreatorException {
    Object result = null;
    
    String className = registry.getProperty(property);
    if(className != null && className.length() > 0) {
      result = creator.getInstance(className, methodName, parameters);
    }
        
    return result;
  }
  
  public Object getInstance(String property, Class clazz) throws InstanceCreatorException, ClassCastException {
    return getInstance(property, (String) null, (Object[]) null, clazz);
  }

  public Object getInstance(String property, String methodName, Object[] parameters, Class clazz) throws InstanceCreatorException, ClassCastException {
    Object result = getInstance(property, methodName, parameters);
    
    if(!clazz.isInstance(result)) {
      throw new ClassCastException();
    }
    
    return result;
  }
  
}
