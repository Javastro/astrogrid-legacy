package org.astrogrid.ace.sextractor;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import org.astrogrid.ace.service.AceParameterBundle;
import org.astrogrid.common.wrapper.ProgramExecutor;
import org.astrogrid.common.wrapper.SingleParameter;
import org.astrogrid.io.Piper;
import org.astrogrid.log.Log;
import org.astrogrid.mySpace.delegate.MySpaceClient;

/**
 * Executes the sextractor program
 *
 * @author M Hill
 */
public class SExtractorExecutor extends ProgramExecutor
{
   private AceParameterBundle bundle = null;

   private String votResultsFilename = null;

   private SExtractorOutputMonitor outputMonitor = new SExtractorOutputMonitor();

   private String outputPath = null; //prefix to be used for converted program output
   private String sexOutputFile = null;   //where the *native* program output will go
   private String commonDir = null; //where to find common config files

   /**
    * Constructor
    */
   public SExtractorExecutor(String givenProgramPath, String instanceWorkingDir,
                             String givenOutputPath, String givenCommonDir)
   {
      super(givenProgramPath, instanceWorkingDir);
      setOutputPath(givenOutputPath);
      commonDir = givenCommonDir;
   }

   public void setBundle(AceParameterBundle givenBundle)
   {
      this.bundle = givenBundle;
   }

   public String getVotResultsFilename()
   {
      return votResultsFilename;
   }

   public SExtractorOutputMonitor getMonitor()
   {
      return outputMonitor;
   }

   /**
    * Starts the program SExtractor
    */
   public Process startProgram() throws IOException
   {
      String configFile = getWorkingDirectory()+"sexConfig.cfg";
      String paramFile = getWorkingDirectory()+"sexParam.cfg";
      sexOutputFile = getWorkingDirectory()+"sexResults.txt";

      //should really have been trapped earlier
      Log.affirm(bundle.getImageToMeasure() != null,
                 "No image to measure - have you given <ImageToMeasure>?");

      //resolve access to files
      String imageToMeasure = resolveFile(bundle.getImageToMeasure());
      SingleParameter param = (SingleParameter) bundle.getChild("FILTER_NAME");
      if (param != null) param.setValue(resolveCommonFile(param.getValue()));
      param = (SingleParameter) bundle.getChild("STARNNW_NAME");
      if (param != null) param.setValue(resolveCommonFile(param.getValue()));

      //set 'hardwired' parameters
      bundle.setParamFile(paramFile);
      bundle.setOutputFile(sexOutputFile);
      bundle.setOutputFormat(bundle.ASCII);

      //debug
      //      SexNativeFileWriter.writeNativeConfigFile( bundle, new ascii.AsciiOutputStream(System.out));

      //write out native configuration files.
      SexNativeFileWriter.writeNativeFiles(bundle, configFile, paramFile);

      //ensure output is sent to log for trace
      getProgramOutput().addStream(new net.mchill.log.LogOutputStream());

      //also trap output for errors, etc
      getProgramOutput().addStream(outputMonitor);

      //NB - Windows may need quotes around arguments if there are spaces in the directory names...
      //setArgs(new String[] { "\""+imageToMeasure, "\" -c \""+configFile+"\"" });
      setArgs(new String[] { imageToMeasure, " -c "+configFile });

      //start program - if it fails, add information from the output monitor
      try
      {
         return super.startProgram();
      }
      catch (IOException ioe)
      {
         if (outputMonitor.getLastError() != null)
         {
            IOException ioe2 = new IOException(ioe+" ("+outputMonitor.getLastError()+")");
            //ioe2.setStackTrace(ioe.getStackTrace());  //java v1.4+
            ioe2.fillInStackTrace(); //java v1.3
            throw ioe2;
         }
         else
         {
            throw ioe;
         }
      }


   }

   /**
    * waits for program - traps exceptions and adds info
    */
   public void waitForProgram() throws IOException
   {
      //start program - if it fails, add information from the output monitor
      try
      {
         super.waitForProgram();
      }
      catch (IOException ioe)
      {
         if (outputMonitor.getLastError() != null)
         {
            IOException ioe2 = new IOException(ioe+" ("+outputMonitor.getLastError()+")");
            //ioe2.setStackTrace(ioe.getStackTrace());  //java v1.4+
            ioe2.fillInStackTrace(); //java v1.3
            throw ioe2;
         }
         else
         {
            throw ioe;
         }
      }


   }
   /**
    * When the program ends, the output is converted to
    * votable format
    */
   public void endProgram() throws IOException
   {
      super.endProgram();
      convertResultsToVot();
   }

   /**
    * Convert the output to VOTable fromat
    */
   public void convertResultsToVot() throws IOException
   {
      //convert results to VOTable
      org.astrogrid.log.Log.trace("(SextractorExecutor) Converting output to VOTable...");
      votResultsFilename = getOutputPath()+"sexResults.vot.xml";
      SexAscii2Vot.convert(sexOutputFile, votResultsFilename,bundle);
      org.astrogrid.log.Log.trace("...done convert");
   };

   /** Used to prefix output files - which may last longer than an instance
    * of this class, and may specify some area in myspace, but must be given
    * here as a file.
    */
   protected String getOutputPath() {      return outputPath;  }

   /** Used to prefix output files - which may last longer than an instance
    * of this class, and may specify some area in myspace, but must be given
    * here as a file.
    */
   protected void setOutputPath(String givenPrefix)
   {
      outputPath = givenPrefix;
   }

   /**
    * Checks the file specified, and if it's not local copies it in (as
    * SExtractor can only cope with local files).  Returns path to file.
    * fileId might be a url or a file path.  This should become part of the
    * myspace stuff
    */
   public String resolveFile(String fileId) throws IOException
   {
      if (fileId.indexOf(':') == -1)
      {
         //no protocol given, it should be just a path name
         return fileId;
      }
      if (fileId.indexOf(':') == 1)
      {
         //one character - a windows machine, the first letter being a drive
         return fileId;
      }
      else
      {
         //some kind of protocol given...
         URL imageUrl = new URL(fileId);
         if (imageUrl.getProtocol().equalsIgnoreCase("file"))
         {
            //ordinary file - NB, while this may be locally available, this
            // might be difficult to resolve in practice...
            //for now, remove protocol and return
            String localName = "//"+imageUrl.getHost()+"/"+imageUrl.getPath();
            if (new File(localName).exists()) return localName;
         }

         //remote protocol, therefore we have to copy it in from somewhere
         String localFilename = getWorkingDirectory()+"/"+fileId;
         Piper.pipe(imageUrl.openStream(), new FileOutputStream(localFilename));

         return localFilename;
      }
   }

   /**
    * Checks the file specified, and if it's given without a path,
    * looks for it in the common directory, if not there then looks
    * for it on http://www.astro.ku.dk/software/SExtractor/sex2.1.0/config/
    * and copy in.  Used for .conv and .nnw files.
    */
   public String resolveCommonFile(String fileId) throws IOException
   {
      Log.trace("Resolving "+fileId+"...");

      if ((fileId.indexOf(':') > -1) || (fileId.indexOf('/') > -1) ||
          (fileId.indexOf('\\') > -1))
      {
         //some kind of path or protocol given, so leave as is
         Log.trace("...file path is already specified");
         return fileId;
      }

      //is the file in the common directory?
      File cf = new File(commonDir+fileId);

      if (cf.exists())
      {
         Log.trace("...file found in common directory "+cf);
         return ""+cf;
      }

      //copy in from site
      URL url = new URL("http://www.astro.ku.dk/software/SExtractor/sex2.1.0/config/"+fileId);
      String localFilename = commonDir+"/"+fileId;
      Piper.pipe(url.openStream(), new FileOutputStream(localFilename));
      
      return localFilename;
   }
   /**
    * Test harness for MCH's machine
    *
   public static void main(String[] args) throws IOException
   {
      org.w3c.dom.Element domNode = org.astrogrid.test.ConfigElementLoader.loadElement(
//         "/astrogrid/sex/test.xml"
         "/fannich/mch/sex/konastest.xml"
      );

      //get the parameters out of the given configuration xml element
      org.astrogrid.common.service.ParameterExtractor extractor =
         new org.astrogrid.common.service.ParameterExtractor(AceContext.getInstance());

      AceParameterBundle aBundle = (AceParameterBundle) extractor.loadRootBundle(domNode);
      aBundle.setImageToMeasure("1_sexInImage.fits");

      SExtractorExecutor wrapper = new SExtractorExecutor(
         //    "\\winnt\\system32\\ipconfig",
         //    "\\astrogrid\\sex\\",
         //    "\\home\\mch\\sex\\tmp_"
         "/fannich/mch/sextractor2.2.2/sex",
         "/fannich/mch/sex/",
         "/fannich/mch/sex/tmp/",
         "/fannich/mch/sex/"
      );

      wrapper.setBundle(aBundle);

      wrapper.runProgram();

    }
    /**/
}

