package org.astrogrid.common.registry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.astrogrid.common.creator.Creator;
import org.astrogrid.common.creator.CreatorException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class Registry {
  // Members.
  private Properties registry;
  private Creator creator;
  
  public Registry(Properties registry) {
    this(registry, new Creator());
  }
  
  public Registry(String filename) throws FileNotFoundException, IOException {
    this(new FileInputStream(filename));
  }

  public Registry(InputStream inStream) throws IOException {
    this(new Properties());
    registry.load(inStream);
  }

  public Registry(Properties registry, Creator creator) {
    this.registry = registry;
    this.creator = creator;
  }
  
  public Object newInstance(String property) throws CreatorException {
    return newInstance(property, (Object[]) null);
  }
  
  public Object newInstance(String property, Object[] parameters) throws CreatorException {
    Object result = null;
    
    String className = registry.getProperty(property);
    if(className != null && className.length() > 0) {
      result = creator.newInstance(className, parameters);
    }
        
    return result;
  }
  
  public Object newInstance(String property, Class clazz) throws CreatorException, ClassCastException {
    return newInstance(property, null, clazz);
  }

  public Object newInstance(String property, Object[] parameters, Class clazz) throws CreatorException, ClassCastException {
    Object result = newInstance(property, parameters);
    
    if(!clazz.isInstance(result)) {
      throw new ClassCastException();
    }
    
    return result;
  }
  
}
