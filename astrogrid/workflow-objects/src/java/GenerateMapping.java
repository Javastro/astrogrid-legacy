import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.tools.MappingTool;

import org.astrogrid.registry.beans.cea.CeaApplicationType;

/*
 * $Id: GenerateMapping.java,v 1.1 2004/03/16 13:51:04 pah Exp $
 * 
 * Created on 15-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

/**
 * Small utility to generate a mapping file for the generated classes.
 * @author Paul Harrison (pah@jb.man.ac.uk) 15-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class GenerateMapping {

   MappingTool maptool;
   private File outfile;
   /**
    * 
    */
   public GenerateMapping(String filename) throws MappingException {
      
      outfile=new File(filename);
      maptool = new MappingTool();
      maptool.setForceIntrospection(false); // use the descriptor classes
      //add the class back references
      maptool.addClass(CeaApplicationType.class, false);
      
   }
   public void writeMap()
   {
      Writer w;
      try {
         w = new FileWriter(outfile);
         maptool.write(w);
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (MappingException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      
   }

   /**
    * @param args The first argument is the name of the output file to be generated
    */
   public static void main(String[] args) {
      try {
         GenerateMapping mapper = new GenerateMapping(args[0]);
         mapper.writeMap();
      }
      catch (MappingException e) {
 
        e.printStackTrace();
      }
      
   }
   
}
