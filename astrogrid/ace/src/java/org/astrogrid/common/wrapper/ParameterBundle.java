
package org.astrogrid.common.wrapper;
/**
 * ParamterBundle.java
 *
 * A class used to wrap, or 'bundle', configuration parameters for application
 * services.
 *
 * @author M Hill
 */

import java.util.Hashtable;
import java.util.Vector;
import java.io.*;
import org.astrogrid.tools.xml.XmlOutput;

public class ParameterBundle extends Parameter
{
   Vector children = new Vector();        //this keeps the order of parameters correct
   Hashtable idindex = new Hashtable();   //for quick retrieval by id

   public ParameterBundle(String givenId, String givenUnits)
   {
      super(givenId, givenUnits);
   }
   
   public boolean hasChildren()
   {
      return (children.size() >0);
   }
   
   public void addChild(Parameter givenNode)
   {
      //remove existing one if there is
      if (idindex.get(givenNode.getId()) != null)
      {
         removeChild((Parameter) idindex.get(givenNode.getId()));
      }
      
      children.add(givenNode);
      idindex.put(givenNode.getId(), givenNode);
   }
   
   public void removeChild(Parameter givenNode)
   {
      children.remove(givenNode);
      idindex.remove(givenNode.getId());
   }
   
   public int getChildCount()
   {
      return children.size();
   }
   
   /**
    * Returns the child with the given id
    */
   public Parameter getChild(String id)
   {
      return (Parameter) idindex.get(id);
   }

   /**
    * Returns the child at the given index
    */
   public Parameter getChild(int index)
   {
      return (Parameter) children.get(index);
   }
   

   /**
    * Returns an array of all the children parameters
    */
   public Parameter[] getChildren()
   {
      return (Parameter[]) children.toArray(new Parameter[] {});
   }
   
   /**
    * Returns an array of all the dictionary keys
    */
   public String[] getIds()
   {
      return (String[]) idindex.keySet().toArray(new String[] {});
   }
   /**
    * Writes out the parameters in XML format, suitable for returning with
    * the results, and/or usable as a template in future.
    * This is a recursive method; we cannot be sure how far down the
    * tree this parameter is...
    */
   public void writeAsXml(XmlOutput out) throws IOException
   {
      XmlOutput childout = null;
      if (hasUnits())
      {
         childout = out.newTag(getId());
      }
      else
      {
         childout = out.newTag(getId(), "units='"+getUnits()+"'");
      }
      
      Parameter[] parameters = getChildren();
      for (int i=0;i<parameters.length;i++)
      {
         parameters[i].writeAsXml(childout);
         out.writeLine("");
      }
      childout.close();
   }
   
 

}

