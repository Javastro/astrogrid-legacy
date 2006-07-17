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
        
        System.out.println("Number of BAsicHDus again from read = " + reader.read().length);
        HashMap  result=null;
        HashMap JDecGenerated78=new HashMap();
        HashMap  sqlCommands=JDecGenerated78;
        int i=0;
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
        return sqlCommands;
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
        while(i < header.getNumberOfCards())
        {
		  value = null;

		  key=header.getKey(i).trim();

		  if(props.getProperty("nameuppercasecolumns").equals("true")) {
	          key = key.toUpperCase();
          }  
		  if(props.getProperty("column." + key + ".translate") != null) {
	          	key = props.getProperty("column." + key + ".translate");
	      }
          if(props.getProperty("column." + key + ".ignore") == null) {
	          
	          keyProp = props.getProperty("column." + key + ".datatype");
	                    
	          if(keyProp == null || keyProp.toLowerCase().indexOf("varchar") != -1 || 
	             keyProp.toLowerCase().indexOf("char") != -1) {
	          	 value=header.getStringValue(key);
	          	if(value != null)
		          	sqlColsAndVals.put(key,"'" + value + "'");
		        else
			        sqlColsAndVals.put(key,null);
	          }else if(keyProp.toLowerCase().indexOf("float") != -1) {
				value = String.valueOf(header.getFloatValue(key));          
				if(value.equals("0.0") && "true".equals(props.getProperty("column." + key + ".numval0nullable")))
					sqlColsAndVals.put(key,null);
				else
	    	      	sqlColsAndVals.put(key,value);
	          }else if(keyProp.toLowerCase().indexOf("boolean") != -1) {
	          	value = String.valueOf(header.getBooleanValue(key));
				if(value.equals("0.0") && "true".equals(props.getProperty("column." + key + ".numval0nullable")))
					sqlColsAndVals.put(key,null);
				else
	    	      	sqlColsAndVals.put(key,value);          	
	          }else if(keyProp.toLowerCase().indexOf("int") != -1) {
		          	value = String.valueOf(header.getIntValue(key));
					if(value.equals("0") && "true".equals(props.getProperty("column." + key + ".numval0nullable")))
						sqlColsAndVals.put(key,null);
					else
		    	      	sqlColsAndVals.put(key,value);	        	  
	          }
	          else if(keyProp.toLowerCase().indexOf("double") != -1) {
	          	value = String.valueOf(header.getDoubleValue(key));
				if(value.equals("0.0") && "true".equals(props.getProperty("column." + key + ".numval0nullable")))
					sqlColsAndVals.put(key,null);
				else
	    	      	sqlColsAndVals.put(key,value);
	          }else if(keyProp.toLowerCase().indexOf("datetime") != -1) {
	            if(value != null) {
		           try
		            {
		              indexDateFormat = new SimpleDateFormat(props.getProperty("datetime.syntax"));
		              dateVal = indexDateFormat.parse(value);
		              value = indexDateFormat.format(dateVal);
		          	sqlColsAndVals.put(key,"'" + value + "'");
		            }
		            catch(ParseException  e)
		            {
		
		            }
	            }else
	  	          	sqlColsAndVals.put(key,null);
	          }else if(keyProp.toLowerCase().indexOf("date") != -1) {
	            if(value != null) {
		           try
		            {
		              indexDateFormat = new SimpleDateFormat(props.getProperty("date.syntax"));
		              dateVal = indexDateFormat.parse(value);
		              value = indexDateFormat.format(dateVal);
		              	          	sqlColsAndVals.put(key,"'" + value + "'");
		            }
		            catch(ParseException  e)
		            {
		
		            }
	            }else {
	           		sqlColsAndVals.put(key,null);
	            }
	          }else {
	 	          	 value=header.getStringValue(key);
	 	          	if(value != null)
	 		          	sqlColsAndVals.put(key,"'" + value + "'");
	 		        else
	 			        sqlColsAndVals.put(key,null);
	          }
	        
	            if(!this.colMap.containsKey(key))
	            {
	              this.colMap.put(key,null);
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
          URL JDecGenerated34 = new URL(line);
          this.writeInsert(this.makeIndexSnippet(JDecGenerated34),tableName);
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
        	  keysql += ", ";
	          String []keysArray = keys.split(",");
	          for(int j = 0;j < keysArray.length;j++) {
	           keysql += " KEYS(" + keysArray[j] + ")";
	           if(j != (keysArray.length - 1))
	        	   keysql += ", ";
	          }//for
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


   public  static   void main(String [] args) throws java.io.IOException  ,java.net.MalformedURLException  ,nom.tam.fits.FitsException  ,java.lang.Exception
   {

       
        if((args==null) || (args.length != 3)  )
        {
          System.out.println("java SQLGenerator -file <filename of urls (one url per line)> <tablename>");
          return;
        }
        SQLGenerator  generator=new SQLGenerator();
        
        if("-file".equals(args[0]))
        {
          FileInputStream JDecGenerated55 = new FileInputStream(args[1]);
          generator.generateIndex(JDecGenerated55,args[2]);
          return;
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