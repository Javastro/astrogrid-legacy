/*
   IndexedParameter.java

   Date      Author      Changes
   $date$    M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.wrapper;

import org.astrogrid.tools.xml.XmlOutput;
import java.io.IOException;

/**
 * IndexedParameter...
 *
 * @version %I%
 * @author M Hill
 */
   
public class IndexedParameter extends Parameter
{
   private String[] values=null;
   
   /**
    * Constructor
    */
   public IndexedParameter(String givenId, String[] givenValues, String givenUnits)
   {
      super(givenId, givenUnits);
      this.values = givenValues;
   }
   
   public String[] getValues()
   {
      return values;
   }
   
   /** String representation for debugging/trace/user display */
   public String toString()
   {
      return "Parameter '"+getId()+"'="+values+getUnits();
   }
   
   public boolean hasChildren()                       { return false; }
   public void addChild(Parameter givenNode)      { throw new UnsupportedOperationException(); }
   public void removeChild(Parameter givenNode)   { throw new UnsupportedOperationException(); }
   public Parameter[] getChildren()               { return null; }
   public int getChildCount()                         { return 0; }
   
   /**
    * Writes out the parameters in XML format, suitable for returning with
    * the results, and/or usable as a template in future.
    * This is a recursive method; we cannot be sure how far down the
    * tree this parameter is...
    */
   public void writeAsXml(XmlOutput out) throws IOException
   {
      out.writeLine(getIdXmlTag());
      for (int i=0;i<values.length;i++)
      {
         out.writeIndentedLine("<arg>"+values[i]+"</arg>");
      }
      out.writeLine("</"+getId()+">");
      
   }
      
   
}

