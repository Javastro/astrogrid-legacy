/*
 VotTag.java

 Date        Author      Changes
 28 Oct 2002 M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.tools.votable;

import java.io.*;
import org.astrogrid.tools.xml.*;
import java.util.Vector;

/**
 * Xml Tag for writing tables using the VOTable XML schema - see the NVO
 * website and its description of VOTable for details, but it goes something
 * like this:
 * <pre>
 * headers
 * VOTABLE
 *     DESCRIPTION
 *     DEFINITIONS xN
 *     RESOURCE
 *        PARAM  xN <-- the criteria used for the search
 *        TABLE
 *           DESCRIPTION
 *           FIELD xN
 *        DATA
 *           TABLEDATA
 *              TR xN
 *                 TD xN
 * </pre>
 * @see http://us-vo.org/metadata/conesearch/ for summary of format required
 * and
 * @see http://vizier.u-strasbg.fr/doc/VOTable/votable-1-0.htx for general
 * VOTable format
 *
 * VotRootTag is the root votable tag, allowing only the following tags as
 * children:
 *    - Descriptions
 *    - Definitions
 *    - Resources
 *
 * @version %I%
 * @author M Hill
 */

public class VotTag extends AbstractVotTag
{
   public final static String VOT_ROOT = "VOTABLE";

   public VotTag(XmlOutput xmlOut) throws IOException
   {
      super(xmlOut.newTag(new XmlTag(VOT_ROOT,"version=\"1.0\"",xmlOut)));
   }

   public void writeDescription(String description) throws IOException
   {
      xmlOut.writeTag("DESCRIPTION",description);
   }

   public DefinitionTag newDefinitionTag() throws IOException
   {
      return (DefinitionTag) newTag(new DefinitionTag(this));
   }

   public ResourceTag newResourceTag(String attrs) throws IOException
   {
      return (ResourceTag) newTag(new ResourceTag(attrs, this));
   }

   //switching this on will slow the conversion down (slightly) as each value
   //is converted from a string to a primitive, checked against the datatype,
   //then back to a string to write out to the VOTable.
   boolean validateValues = false;

   Vector fields = new Vector(); //stores descriptions of the fields (table columns)

   public final static int NOT_ARRAY = 0; //for arraysize
   public final static int VARIABLE_ARRAY = -1; //for arraysize

   /** Called whenever a field tag (ie, table column header) is added.  This
    * can then be used by the writeValue() of the table block to validate the
    * value being written.  Not implemented yet
    */
   public void addFieldDescriptor(Field field)
   {
      fields.add(field);
   }

   /** Returns the number of fields specified in the table so far
    */
   public int getNumFields()
   {
      return fields.size();
   }

   /**Set the validation on/off
    */
   public void setValidateValues(boolean v)
   {
      validateValues = v;
   }


   /** Inner class.  Special 'Definition' tag - allows co-ord sys to be given */
   public class DefinitionTag extends AbstractVotTag
   {
      public DefinitionTag(VotTag parent) throws IOException
      {
         super(new XmlTag("DEFINITIONS","",parent.xmlOut));
      }

      public void writeCooSys(String id, String system, String equinox, String epoch) throws IOException
      {
         xmlOut.writeLine("<COOSYS ID=\""+id+"\" system=\""+system+"\" equinox=\""+equinox+"\" epoch=\""+epoch+"\"/>");
      }
   }

   /** Resource tag block - allows parameters and data table to be given */
   public class ResourceTag extends AbstractVotTag
   {
      public ResourceTag(String attrs, VotTag parent) throws IOException
      {
         super(new XmlTag("RESOURCE",attrs,parent.xmlOut));
      }

      public void writeDescription(String description) throws IOException
      {
         xmlOut.writeTag("DESCRIPTION",description);
      }

      public void writeParam(String id, String datatype, String value) throws IOException
      {
         xmlOut.writeLine("<PARAM ID=\""+id+"\" datatype=\""+datatype+"\" value=\""+value+"\"/>");
      }
      public void writeParam(String id, String datatype, String arraysize, String value) throws IOException
      {
         xmlOut.writeLine("<PARAM ID=\""+id+"\" datatype=\""+datatype+"\" arraysize=\""+arraysize+"\" value=\""+value+"\"/>");
      }
      public TableTag newTableTag() throws IOException
      {
         return (TableTag) newTag(new TableTag(this));
      }
   }

   /** Table tag - allows fields (headers) and data (cell values) to be written*/
   public class TableTag extends AbstractVotTag
   {
      public TableTag(ResourceTag parent) throws IOException
      {
         super(new XmlTag("TABLE","",parent.xmlOut));
      }

      public void writeDescription(String description) throws IOException
      {
         xmlOut.writeTag("DESCRIPTION",description);
      }

      /** Writes out the column header information */
      public void writeField(Field fieldDesc) throws IOException
      {
         String line = "<FIELD ID=\""+fieldDesc.getId()+ "\""
            + " name=\""+fieldDesc.getName()+"\""
            + " datatype=\""+fieldDesc.getDatatype()+"\"";

         if (fieldDesc.getUcd() != null)
         {
            line = line + " ucd=\""+fieldDesc.getUcd()+"\"";
         }

         if (fieldDesc.getArraysize() != NOT_ARRAY)
            if (fieldDesc.getArraysize() == VARIABLE_ARRAY)
               line = line + " arraysize=\"*\"";
            else
               line = line + " arraysize=\""+fieldDesc.getArraysize()+"\"";

         xmlOut.writeLine(line + ">");

         xmlOut.writeIndentedLine(fieldDesc.getDescription());

         xmlOut.writeLine("</FIELD>");

         addFieldDescriptor(fieldDesc); //so we can validate table cell values
      }

      /** Makes the table data/value tag */
      public TableDataTag newTableDataTag() throws IOException
      {
         return (TableDataTag) newTag(new TableDataTag( this));
      }
   }

   /** Table data tag - really a double-layer tag <DATA> then <TABLEDATA> */
   public class TableDataTag extends AbstractVotTag
   {
      private XmlTag rowTag = null;
      private int col = 0;
      private XmlTag tableDataTag = null;

      public TableDataTag(TableTag parent) throws IOException
      {
         super(new XmlTag("DATA","",parent.xmlOut));
         tableDataTag = xmlOut.newTag(new XmlTag("TABLEDATA","", this.xmlOut));
      }

      public void startNewRow() throws IOException
      {
         col = 0;
         rowTag = tableDataTag.newTag("TR","");
      }

      public void writeValue(String value) throws IOException
      {
         if (validateValues)
         {
            ((Field) fields.elementAt(col)).assertValueValid(value.trim());
         }

         rowTag.writeTag("TD",value);
         col++;
      }
   }


      /** Override close to check that valid fields have been created.  Certain
       * UCDs must have been specified once only (@see
       * http://us-vo.org/metadata/conesearch/)
       * - nope - specific to NVO...
       public void close() throws IOException
       {
       super.close();

       for
       }
       */



}

