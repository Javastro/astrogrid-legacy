/*
   Field.java

   Date        Author      Changes
   8 Oct 2002  M Hill      Created

   (c) Copyright...
*/
package org.astrogrid.tools.votable;

import org.astrogrid.log.Log;

/**
 * Each field (table column) in a VOTable is described by one of these.
 * It can also be
 * used to check if a table cell value is correct
 
 * @author M Hill
 */


public class Field
{
   private String id;
   private String name;
   private VotDatatype datatype;
   private int arraysize = NOT_ARRAY;    //0 = not an array, -1 = any size
   private String ucd;       //unified column descriptor - @see http://vizier.u-strasbg.fr/doc/UCD.htx
   private String description;
   
   public final static int NOT_ARRAY = 0; //for arraysize
   public final static int VARIABLE_ARRAY = -1; //for arraysize
   
   //can also add things like precision, min/max values, etc
   
   public Field(String anId, String aName, VotDatatype aDatatype,
                int anArraySize, String aUcd, String aDesc)
   {
      id = anId;
      name = aName;
      datatype = aDatatype;
      arraysize = anArraySize;
      ucd = aUcd;
      description = aDesc;
   }
   
   /** Property accessor - Id is the column identifier */
   public String getId()   { return id; }
   
   /** Property accessor - name is the column name */
   public String getName()    { return name; }
   
   /** Property accessor - datatype refers to the 'type' of the values in the column */
   public VotDatatype getDatatype()   { return datatype; }
   
   /** Property accessor - arraysize 0 = not array, -1 = variable array */
   public int getArraysize()  { return arraysize; }

   /** Property accessor - ucd - unified column descriptor */
   public String getUcd()  { return ucd; }

   /** Property accessor - column description */
   public String getDescription()   { return description; }
   
   /**
    * Throws an assertion error if the value does not correspond to the
    * data type and array size - ie if a string is being written for a numerical data
    * type.
    */
   public void assertValueValid(String aString)
   {
      if (arraysize == NOT_ARRAY)
      {
         assertSingleValueValid(aString);
      }
      else
      {
         //separate individual values out by spaces.  May be more than one space between
         String workString = aString;
         int gap = workString.indexOf(" ");
         int elementNum = 0;
         String value;
         while (gap>-1)
         {
            elementNum++;
            value =  workString.substring(0, gap).trim();
            workString = workString.substring(gap).trim();
            assertSingleValueValid(value);
            gap = workString.indexOf(" ");
         }
         
         if (arraysize != VARIABLE_ARRAY)
            Log.affirm(elementNum==arraysize, "Number of elements in value ["+aString+"] do not match arraysize "+arraysize+", field "+this);
         
      }
      
   }
   
   /**
    * Throws an assertion value if the individual value passed does not
    * correspond to the data type - ie, if a text string is being written
    * for a numerical data type column.0
    NOT FINISHED - not done BIT and various others
    */
   public void assertSingleValueValid(String aString)
   {
      String error = "Trying to write value "+aString+" to a "+datatype+" datatype";
      
      if (datatype == VotDatatype.LOGICAL)
      {
         Log.affirm("Ff0Tt1".indexOf(aString) != -1, error);
      }
      else if (datatype == VotDatatype.BYTE)
      {
         Byte.parseByte(aString);   //throws a number format error if a problem
      }
      else if ((datatype == VotDatatype.INT) || (datatype == VotDatatype.LONG) || (datatype == VotDatatype.SHORT))
      {
         Integer.parseInt(aString);
      }
      else if ((datatype == VotDatatype.FLOAT) || (datatype == VotDatatype.DFLOAT))
      {
         Float.parseFloat(aString);
      }
   }
   
   /** For debug & display purposes
    */
   public String toString()
   {
      return id+" ("+name+")";
   }
}

