/*
   Parameter.java

   Date      Author      Changes
   $date$    M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.wrapper;

import org.astrogrid.tools.xml.XmlOutput;
import java.io.IOException;

/**
 * Parameter...
 *
 * @version %I%
 * @author M Hill
 */
   
public class SingleParameter extends Parameter
{
   private String value=null;
   
   /**
    * Constructor
    */
   public SingleParameter(String givenId, String givenValue)
   {
      super(givenId, null);
      this.value = givenValue;
   }
   
   /**
    * Constructor with units
    */
   public SingleParameter(String givenId, String givenValue, String givenUnits)
   {
      super(givenId, givenUnits);
      this.value = givenValue;
   }
   
   public String getValue()                     {  return value;   }
   public void   setValue(String givenValue)    {  value = givenValue;   }
   
   /** String representation for debugging/trace/user display */
   public String toString()
   {
      return "Parameter '"+id+"'="+value+units;
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
      out.writeIndentedLine(getIdXmlTag()+getValue()+"</"+getId()+">");
   }
   
}

