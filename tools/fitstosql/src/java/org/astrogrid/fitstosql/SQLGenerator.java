package org.astrogrid.fitstosql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import nom.tam.fits.Fits;
import nom.tam.fits.Header;
import nom.tam.fits.BasicHDU;
import nom.tam.fits.HeaderCard;

// End of Import

public  class  SQLGenerator {

   public  boolean  checkForExtensions   ;
   private  HashMap  colMap   ;
   private  static  File  fileIndexDir   ;

   public   HashMap makeIndexSnippet(URL fitsUrl) throws java.io.IOException  ,nom.tam.fits.FitsException
   {
       if(fitsUrl == null) {
       	System.out.println("Error no URL found to process");
       	System.exit(1);
       }
       System.out.println("Examining file " + fitsUrl + "....");
       Fits fitsRead = new Fits(fitsUrl);
       return this.makeIndexSnippet(fitsRead,fitsUrl.toString());

   }

   public   HashMap makeIndexSnippet(Fits reader,String filename) throws java.io.IOException  ,nom.tam.fits.FitsException
   {

        StringBuffer JDecGenerated2=new StringBuffer();
        StringBuffer  snippet=JDecGenerated2;
        System.out.println("entered makeIndexNippet(fitsreader,file)");
        StringBuffer JDecGenerated21=new StringBuffer();
        System.out.println("Number of hdus = " + reader.getNumberOfHDUs());
        StringBuffer JDecGenerated49=new StringBuffer();
        
        //System.out.println("Number of BAsicHDus again from read = " + reader.read().length);
        HashMap  result=null;
        HashMap JDecGenerated78=new HashMap();
        HashMap  sqlCommands=JDecGenerated78;
        int i=0;
        /*
        while(i < reader.getNumberOfHDUs()) {
          Header header=reader.getHDU(i).getHeader();
          result = this.makeIndexSnippet(header,filename);
          if(sqlCommands.containsKey(filename)!=false) {
            HashMap  prevResult=(HashMap)sqlCommands.get(filename);
            result.putAll(prevResult);
		  }
          sqlCommands.put(filename,result);
          i=i+1;
        }
       
        BasicHDU bh;
        while((bh = reader.readHDU()) != null) {
          System.out.println("reading a hdu");
          Header header= bh.getHeader();
          result = this.makeIndexSnippet(header,filename);
          if(sqlCommands.containsKey(filename)!=false) {
            HashMap  prevResult=(HashMap)sqlCommands.get(filename);
            result.putAll(prevResult);
		  }
          sqlCommands.put(filename,result);
     
        }  
         */      
        System.out.println("lets try to do a read to get the hdu's");
        BasicHDU []bh = reader.read();
        System.out.println("success and there are these many hdu's = " + bh.length);
        for(int j = 0;j < bh.length;j++) {
          System.out.println("reading a hdu index = " + j);
          Header header= bh[j].getHeader();
          result = this.makeIndexSnippet(header,filename);
          if(sqlCommands.containsKey(filename)!=false) {
            HashMap  prevResult=(HashMap)sqlCommands.get(filename);
            result.putAll(prevResult);
		  }
          sqlCommands.put(filename,result);
        }//for
        System.out.println("Made mapping of columns the size = " + sqlCommands.size() + " if 0 then most likely skipped and no data to write for that file.");
        return sqlCommands;
   }
   
   private String translateUpper(String val) {
	   if(props.getProperty("nameuppercasecolumns").equals("true")) {
	          return val.toUpperCase();
       }
	   return val;
   }
   
   private String translateKey(String val) {
		  if(props.getProperty("column." + val + ".translate") != null) {
	          	return (props.getProperty("column." + val + ".translate"));
	      }
		  return val;
		  
	   
   }

   public   HashMap makeIndexSnippet(Header header,String fileLocation) throws java.io.IOException  ,nom.tam.fits.FitsException
   {
        System.out.println("entered makeIndexSnippet(header,filelocation)");
        if(header== null)
        {
          HashMap JDecGenerated14=new HashMap();
          return JDecGenerated14;
        }
        System.out.println("header not null");
        HashMap  sqlColsAndVals=  new HashMap();
        int i=0;
        String keyProp = null;
        String value = null;
        String key = null;
        SimpleDateFormat indexDateFormat = null;
        Date dateVal = null;
        

        /*
        while(i < header.getNumberOfCards())
        {
        		  key=header.getKey(i).trim();
        */
        for ( Iterator it = header.iterator(); it.hasNext(); ) {
        	key = null;
            value = null;

            HeaderCard card = (HeaderCard) it.next();
            key = card.getKey().trim();
            value = card.getValue();
		  if(key != null && key.trim().length() > 0) {
          if(props.getProperty("column." + key + ".ignore") == null) {
	          
	          keyProp = props.getProperty("column." + key + ".datatype");
	                    
	          if(keyProp == null || keyProp.toLowerCase().indexOf("varchar") != -1 || 
	             keyProp.toLowerCase().indexOf("char") != -1) {
	          	 //value=header.getStringValue(key);
	          	if(value != null)
		          	sqlColsAndVals.put(translateKey(translateUpper(key)),"'" + value + "'");
		        else
			        sqlColsAndVals.put(translateKey(translateUpper(key)),null);
	          }else if(keyProp.toLowerCase().indexOf("float") != -1) {
				//value = String.valueOf(header.getFloatValue(key));          
				if(value.equals("0.0") && "true".equals(props.getProperty("column." + key + ".numval0nullable")))
					sqlColsAndVals.put(translateKey(translateUpper(key)),null);
				else
	    	      	sqlColsAndVals.put(key,value);
	          }else if(keyProp.toLowerCase().indexOf("boolean") != -1) {
	          	//value = String.valueOf(header.getBooleanValue(key));
				if(value.equals("0.0") && "true".equals(props.getProperty("column." + key + ".numval0nullable")))
					sqlColsAndVals.put(translateKey(translateUpper(key)),null);
				else
	    	      	sqlColsAndVals.put(translateKey(translateUpper(key)),value);          	
	          }else if(keyProp.toLowerCase().indexOf("int") != -1) {
		          	//value = String.valueOf(header.getIntValue(key));
					if(value.equals("0") && "true".equals(props.getProperty("column." + key + ".numval0nullable")))
						sqlColsAndVals.put(translateKey(translateUpper(key)),null);
					else
		    	      	sqlColsAndVals.put(translateKey(translateUpper(key)),value);	        	  
	          }
	          else if(keyProp.toLowerCase().indexOf("double") != -1) {
	          	//value = String.valueOf(header.getDoubleValue(key));
				if(value.equals("0.0") && "true".equals(props.getProperty("column." + key + ".numval0nullable")))
					sqlColsAndVals.put(translateKey(translateUpper(key)),null);
				else
	    	      	sqlColsAndVals.put(translateKey(translateUpper(key)),value);
	          }else if(keyProp.toLowerCase().indexOf("datetime") != -1) {
	            if(value != null) {
		           try
		            {
		              indexDateFormat = new SimpleDateFormat(props.getProperty("datetime.syntax"));
		              dateVal = indexDateFormat.parse(value);
		              value = indexDateFormat.format(dateVal);
		          	sqlColsAndVals.put(translateKey(translateUpper(key)),"'" + value + "'");
		            }
		            catch(ParseException  e)
		            {
		
		            }
	            }else
	  	          	sqlColsAndVals.put(translateKey(translateUpper(key)),null);
	          }else if(keyProp.toLowerCase().indexOf("date") != -1) {
	            if(value != null) {
		           try
		            {
		              indexDateFormat = new SimpleDateFormat(props.getProperty("date.syntax"));
		              dateVal = indexDateFormat.parse(value);
		              value = indexDateFormat.format(dateVal);
		              	          	sqlColsAndVals.put(translateKey(translateUpper(key)),"'" + value + "'");
		            }
		            catch(ParseException  e)
		            {
		
		            }
	            }else {
	           		sqlColsAndVals.put(translateKey(translateUpper(key)),null);
	            }
	          }else {
	 	          	 value=header.getStringValue(key);
	 	          	if(value != null)
	 		          	sqlColsAndVals.put(translateKey(translateUpper(key)),"'" + value + "'");
	 		        else
	 			        sqlColsAndVals.put(translateKey(translateUpper(key)),null);
	          }
	        
	            if(!this.colMap.containsKey(translateKey(translateUpper(key))))
	            {
	              this.colMap.put(translateKey(translateUpper(key)),null);
	            }
	    	}
		  }
	        i=i+1;
        }
        
        sqlColsAndVals.put("FitsLocation","'" + fileLocation + "'");
        return sqlColsAndVals;

   }

  

   public   File generateIndex(InputStream urlsIn,String tableName) throws java.io.IOException  ,nom.tam.fits.FitsException
   {
        System.out.println("entered generateIndex(inputstream)");
        BufferedReader in = new BufferedReader(new InputStreamReader(urlsIn));
        String  line=null;
        while((line = in.readLine()) != null)
        {
        	  URL inputURL = new URL(line);
	          this.writeInsert(this.makeIndexSnippet(inputURL),tableName);

        }
        return this.writeCreate(tableName);
   }


   public   File writeInsert(HashMap sqlCommands,String tableName) throws java.io.IOException, nom.tam.fits.FitsException
   {
        System.out.println("entered generateIndex(url)");
        File  sqlFile= new File(tableName + "_insert.sql");
        FileWriter  fw=null;
        PrintWriter  pw=null;
        String  xmlSnippet=null;
        String  insertCommand=null;
        String  cols="";
        String  vals="";
        boolean dateObsFound=false;
        try
        {
          String  fileName=null;
          fw = new FileWriter(sqlFile,true);
          pw =new PrintWriter(fw);
          Iterator  iter = sqlCommands.keySet().iterator();
          insertCommand = "Insert into " + tableName;
          while(iter.hasNext())
          {
            String  key = (String)iter.next();
            HashMap  result = (HashMap)sqlCommands.get(key);
            Iterator  iter2=result.keySet().iterator();
            cols = "FitsLocation";
            vals = (String)result.get("FitsLocation");
            while(iter2.hasNext())
            {
              String  key2=(String)iter2.next();
              String  val2=(String)result.get(key2);
              if(!key2.equals("FitsLocation"))
              {
                cols = cols + ", " + key2;
                
                if(val2 == null)
                  vals = vals + "," + "NULL";
                else
                  vals = vals + ", " + val2;

              }
            }
            pw.println(insertCommand + "(" + cols + ") VALUES (" + vals + ");");
          }
          pw.flush();
          fw.close();
          pw.close();
        }
        catch(IOException  ioe)
        {

        }
        sqlCommands.clear();
        sqlCommands = null;
        return null;
   }

   public   File writeCreate(String tableName) throws java.io.IOException  ,nom.tam.fits.FitsException
   {
        System.out.println("entered generateIndex(url)");
        File  sqlFile=new File(tableName + "_create.sql");
        FileWriter  fw=null;
        PrintWriter  pw=null;
        String  cols="FitsLocation VARCHAR(" + props.getProperty("fitslocation.size") + "), ";
        try
        {
          fw = new FileWriter(sqlFile,true);
          pw = new PrintWriter(fw);
          Iterator  iter=this.colMap.keySet().iterator();
          String  keyTemp=null;
          String primaryKey = props.getProperty("primary.key");
          String keys = props.getProperty("indexed.keys");
          String keysql = "";
          if(keys != null) {
        	  keysql = ", KEY(" + keys + ")";
	      }//if
          while(iter.hasNext())
          {
            keyTemp =(String)iter.next();
            if(!"FitsLocation".equals(keyTemp))
            {
              StringBuffer JDecGenerated246=new StringBuffer();
              cols += keyTemp;
              if(props.getProperty("column." + keyTemp + ".datatype") == null) {
              	  cols += " VARCHAR(" + props.getProperty("default.varcharsize") + "), ";
              	}else { 
              	  cols += " " + props.getProperty("column." + keyTemp + ".datatype") + ", ";
              	}
            }//if
          }//while
          pw.println("CREATE table " + tableName + "(" + cols + " PRIMARY KEY ( " + primaryKey + ") " + keysql + " );");
          pw.flush();
          fw.close();
          pw.close();
        }
        catch(IOException  ioe)
        {

        }
        return null;
   }


   public  static   void main(String [] args) {
	   //System.out.println("args length = " + args.length);
	   //System.out.println("processing urls in file = " + args[0] + " for table name = " + args[1]);
        if((args==null) || (args.length != 2)  )
        {
          System.out.println("java SQLGenerator <filename of urls (one url per line)> <tablename>");
          return;
        }
        SQLGenerator  generator=new SQLGenerator();
        try {
          System.out.println("processing urls in file = " + args[0] + " for table name = " + args[1]);
          FileInputStream JDecGenerated55 = new FileInputStream(args[0]);
          generator.generateIndex(JDecGenerated55,args[1]);
        }catch(java.io.IOException ioe) {
        	ioe.printStackTrace();
        }catch(nom.tam.fits.FitsException fe) {
        	fe.printStackTrace();
        }catch(Exception e) {
        	e.printStackTrace();
        }
      
   }

	Properties props = null;

   public SQLGenerator( )
   {
        this.colMap = new HashMap();
        props = new Properties();
        try {
        	System.out.println("prop = sqlgen.props");
        	props.load(new java.io.FileInputStream("sqlgen.props"));
        }catch(Exception e) {
          System.out.println("error loading properties = " + e.getMessage());
          System.exit(1);
        }
   }
}