package org.astrogrid.common.creator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.log4j.Category;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class InstanceCreator {
  private Category logger = Category.getInstance(getClass());
  
  // Constants.
  private static final String DEFAULT_METHOD = "getInstance";

  public Object getInstance(String className) throws InstanceCreatorException {
    return getInstance(className, null, null);
  }

  public Object getInstance(String className, Object[] parameters) throws InstanceCreatorException {
    return getInstance(className, null, parameters);
  }
  
  public Object getInstance(String className, String methodName, Object[] parameters) throws InstanceCreatorException {
    Object result = null;
    
    if(methodName == null || methodName.length() < 1) {
      methodName = DEFAULT_METHOD;
    }
    
    try {
      Class clazz = Class.forName(className);
      
      Class[] classes = getParameterClasses(parameters);
      Method method = null;
      
      try {
        method = clazz.getMethod(methodName, classes);
      }
      catch(NoSuchMethodException e) {
        classes = convertPrimitives(classes);
        method = clazz.getMethod(methodName, classes);
      }
      
      int modifiers = method.getModifiers();
      if(Modifier.isStatic(modifiers)) {
        try {
          result = method.invoke(null, parameters);
        }
        catch(IllegalAccessException e) {
          throw new InstanceCreatorException(e);
        }
        catch(InvocationTargetException e) {
          throw new InstanceCreatorException(e);
        }
      }
    }
    catch(ClassNotFoundException e) {
      throw new InstanceCreatorException(e);
    }
    catch(NoSuchMethodException e) {
      throw new InstanceCreatorException(e);
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
