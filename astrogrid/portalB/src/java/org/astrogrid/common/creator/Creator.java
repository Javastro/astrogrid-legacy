package org.astrogrid.common.creator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.apache.log4j.Category;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class Creator {
  private Category logger = Category.getInstance(getClass());

  public Object newInstance(String className) throws CreatorException {
    return newInstance(className, null);
  }
  
  public Object newInstance(String className, Object[] parameters) throws CreatorException {
    Object result = null;
    
    try {
      Class clazz = Class.forName(className);

      Class[] classes = getParameterClasses(parameters);
      Constructor constructor = null;
      try {
        constructor = clazz.getConstructor(classes);
      }
      catch(NoSuchMethodException e) {
        classes = convertPrimitives(classes);
        constructor = clazz.getConstructor(classes);
      }

      try {
        result = constructor.newInstance(parameters);
      }
      catch(InstantiationException e) {
        throw new CreatorException(e);
      }
      catch(IllegalAccessException e) {
        throw new CreatorException(e);
      }
      catch(InvocationTargetException e) {
        throw new CreatorException(e);
      }
    }
    catch(ClassNotFoundException e) {
      throw new CreatorException(e);
    }
    catch(NoSuchMethodException e) {
      throw new CreatorException(e);
    }
    
    return result;
  }
  
  private Class[] getParameterClasses(Object[] parameters) {
    Class[] result = null;
    
    if(parameters != null && parameters.length > 0) {
      result = new Class[parameters.length];
      for(int paramIndex = 0; paramIndex < parameters.length; paramIndex++) {
        result[paramIndex] = parameters[paramIndex].getClass();
      }
    }    
    
    return result;
  }
  
  private Class[] convertPrimitives(Class[] classes) {
    Class[] result = null;
    
    if(classes != null && classes.length > 0) {
      result = new Class[classes.length];
      for(int classIndex = 0; classIndex < classes.length; classIndex++) {
        result[classIndex] = convertPrimitive(classes[classIndex]);
      }
    }    
    
    return result;
  }
  
  private Class convertPrimitive(Class clazz) {
    Class result = clazz;
    
    logger.debug("class: " + clazz.getName());
    
    try {
      Field field = clazz.getField("TYPE");
      int modifiers = field.getModifiers();
      if(Modifier.isStatic(modifiers)) {
        Class fieldClazz = field.getType();

        if(fieldClazz.isAssignableFrom(Class.class)){
          result = (Class) field.get(null);
        }
      }
    }
    catch(NoSuchFieldException e) {
      logger.debug("no TYPE for class: " + result.getName());
    }
    catch(IllegalAccessException e) {
      logger.debug("illegal access: " + result.getName());
    }
    
    logger.debug("result: " + result.getName());
    logger.debug("Integer.TYPE: " + Integer.TYPE.getName());

    return result;
  }

}
