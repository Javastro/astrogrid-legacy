/*
 ParameterNode.java

 Date      Author      Changes
 $date$    M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.common.wrapper;

import java.io.*;
import org.astrogrid.tools.xml.XmlOutput;

/**
 * ParameterNode...
 *
 * @version %I%
 * @author M Hill
 */

public abstract class Parameter
{
   protected String id = null;     //this might be a UCD, or some local term
   protected String units = null;
   
   /**
    * Constructor
    */
   public Parameter(String givenId, String givenUnits)
   {
      this.id = givenId;
      if (givenUnits != "") //so we always have null or something
      {
         this.units = givenUnits;
      }
   }
   
   public String getUnits()
   {
      return units;
   }
   
   public String getId()
   {
      return id;
   }
   
   public String getIdXmlTag()
   {
      String tag = "<"+getId();
      if (getUnits() != null)
      {
         tag = tag + " units=\""+getUnits()+"\"";
      }
      tag = tag + ">";
      
      return tag;
   }
   
   public boolean hasUnits()
   {
      return units != null;
   }
   
   public abstract boolean hasChildren();
   public abstract void addChild(Parameter givenNode);
   public abstract void removeChild(Parameter givenNode);
   public abstract Parameter[] getChildren();
   
   public abstract void writeAsXml(XmlOutput out) throws IOException;
   
}

