/*
 ParameterExtractor.java

 Date       Author      Changes
 1 Oct 2002 M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.common.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.astrogrid.common.wrapper.ContextFactory;
import org.astrogrid.common.wrapper.IndexedParameter;
import org.astrogrid.common.wrapper.Parameter;
import org.astrogrid.common.wrapper.ParameterBundle;
import org.astrogrid.common.wrapper.SingleParameter;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Creates parameter bundles from XML DOM nodes, using a particular
 * context.
 *
 * for each element, it looks for the corresponding property in the ParameterBundle
 * (eg setName(String), setTel(String) ).  If it can't find it, it uses the
 * ParameterBundle.addParameter(String, String) method.
 *
 * @see org.astrogrid.wrapper.ParameterBundle
 *
 * The context is given by a ContextFactory implementation; this defines what
 * subclass of ParameterBundle might be used, unit and ucd dictionaries, etc
 *
 * @see org.astrogrid.wrapper.ContextFactory
 *
 *  @author           : M Hill
 */
public class ParameterExtractor
{

   //   static final String nameSpace =
   //      "http://www.astrogrid.org/namespace/SExtractor_2_2_2_CONFIG";

   static final String UNIT_ATTR = "Units";
   static final String INDEXED_ELEMENT_TAG = "arg";

   private ContextFactory contextFactory = null;

   public ParameterExtractor(ContextFactory givenContextFactory)
   {
      this.contextFactory = givenContextFactory;
   }

   /**
    * Public method called to load the bundle from the top level node of a DOM
    * tree containing the configuration parameters.
    * Returns the 'parsed' bundle of parameters.
    */
   public ParameterBundle loadRootBundle(Element configNode)
   {
      //recursive load from nodes to xmlBundle
      ParameterBundle xmlBundle = loadBundleElement("Root", configNode); //we know the root one is a bundle

      //convert xml bundle from ucds, etc to local terms
      ParameterBundle localBundle = xmlBundle; //for now

      //return parameter bundle
      return localBundle;

   }

   /**
    * For the given DOM node (element), creates a matching parameter.
    */
   protected Parameter loadElement(Element element)
   {
      // See if this is as multi-argument parameter
      //            NodeList args = element.getElementsByTagNameNS(myNameSpace,"arg");
      NodeList args = element.getElementsByTagName(INDEXED_ELEMENT_TAG);

      //validate the tag against the dictionary, and convert to a local term
      //if need be
      String id = element.getNodeName();
      if (!contextFactory.getLocalTermDictionary().isInDictionary(id))
      {
         org.astrogrid.log.Log.logWarning("Term/Tag '"+id+"' is not in the local dictionary");
      }

      //convert to a local term if it is a ucd
//      if (contextFactory.getUcdDictionary().isUcdValid(id))
//      {
 //        id = contextFactory.getLocalTermDictionary().getLocalTermFor(id);
//      }

      //check what kind of parameter it will be
      if (args.getLength() > 1)
      {
         // Multiple arguments
         String[] values = new String[args.getLength()];
         for (int j = 0; j < args.getLength(); j++)
         {
            values[j] = args.item(j).getFirstChild().getNodeValue().trim();
         }
         return new IndexedParameter(id,
                                     values,
                                     element.getAttribute(UNIT_ATTR)
                                    );
      }

      //this line handles the doc when all tags, even single value parameters,
      //have [arg] tags
      Element valueElement = (Element) args.item(0);

      //is it a bundle?
      // ie are there any more elements in its children?
      for (int i=0; i<valueElement.getChildNodes().getLength(); i++)
      {
         if (element.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
         {
            return loadBundleElement(id, element);
         }
      }


      // single parameter element
      return new SingleParameter(id,
                                 valueElement.getFirstChild().getNodeValue().trim(),
                                 element.getAttribute(UNIT_ATTR)
                                );
   }

   /**
    * For the given element that contains a number of sub-elements corresponding
    * to more parameters, loads a bundle of parameters and all sub-elements.
    * A recursive method for loading trees of bundles.
    */
   protected ParameterBundle loadBundleElement(String id, Element element)
   {
      ParameterBundle bundle = contextFactory.newParameterBundle(
         id,
         element.getAttribute(UNIT_ATTR)
      );

      NodeList nodes = element.getChildNodes();

      for (int i = 0; i < nodes.getLength(); i++)
      {
         if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
         {
            Parameter p = loadElement( (Element)(nodes.item(i)) );

            //look first to see if there is a special method on bundle that
            //expects this element.  This is of the form setXXXX where
            //XXX is the parameter id.

            //look through all methods.  This algorithm allows us
            //to look case insensitively, and perhaps later to ignore underscores,
            // etc.
            //          Method[] methods = bundle.getClass().getMethods();//"set"+p.getId(), new Class[] {Parameter.class});
            //          Method setMethod = null;
            //          for (int j=0;j<methods.length;j++)
            //          {
            //             if (methods[j].getName() == "set"+p.getId())
            //             {
            //                setMethod = methods[j];
            //             }
            //          }

            //use the introspection library to find the method.  This algorithm
            //is quicker but less flexible; case and arguments must be exact.

            //it's also a bit messy, as we first try using the Parameter itelf as
            //an argument.  If that fails we try a string, but this is only
            //suitable if the parameter is a SingleParameter with a value. Other
            //wise we add the parameter as a child.
            //If there is a failure in invoking a method, we report the root
            //cause (eg IllegalArgumentException)
            Method method = null;
            Object[] args = null;
            try
            {
               //first try with Parameter as argument
               method = bundle.getClass().getMethod("set"+p.getId(), new Class[] {p.getClass()});
               args = new Object[] { p };
            }
            catch (NoSuchMethodException nsme)
            {
               if (p instanceof SingleParameter)
               {
                  try
                  {
                     //now try with ordinary string as argument
                     method = bundle.getClass().getMethod("set"+p.getId(), new Class[] {String.class});
                     args = new Object[] { ((SingleParameter) p).getValue() };
                  }
                  catch (NoSuchMethodException nsme2) {} //ignore
               }
            }

            if (method == null)
            {
               bundle.addChild(p);
            }
            else
            {
               try
               {
                  method.invoke(bundle, args);
               }
               catch (InvocationTargetException ite)  //Problem running method
               {
                  //Problem calling method - eg illegal argument
                  //Log.logError("Error setting bundle property "+p.getId(),ite.getCause()); //java 1.4 - preferable as cause is useful
                  Log.logError("Error setting bundle property "+p.getId(),ite);  //java 1.3
               }
               catch (IllegalAccessException iae)  //Problem running method
               {
                  //Nasty
                  Log.logError("Error setting bundle property "+p.getId(),iae);
               }
            }
         }
      }

      return bundle;
   }

   /**
    * Test harness - would normally leave commented out as its application
    * specific.  Loads parameters from test.xml and prints them out to
    * screen.
    */
   public static void main(String [] cmdargs) throws IOException
   {
      Element domNode = org.astrogrid.test.ConfigElementLoader.loadElement("test.xml");

      ParameterExtractor extractor = new ParameterExtractor(org.astrogrid.ace.service.AceContext.getInstance());

      ParameterBundle aBundle = (ParameterBundle) extractor.loadRootBundle(domNode);

      if (aBundle != null)
      {
         try
         {
            ParameterXmlWriter.writeXmlConfigFile(aBundle, System.out);
            org.astrogrid.ace.sextractor.SexNativeFileWriter.writeNativeConfigFile((org.astrogrid.ace.service.AceParameterBundle) aBundle, new org.astrogrid.tools.ascii.AsciiOutputStream(System.out));
         }
         catch (IOException ioe)
         {
            ioe.printStackTrace(System.out);
         }
      }
   }
   /**/
}

