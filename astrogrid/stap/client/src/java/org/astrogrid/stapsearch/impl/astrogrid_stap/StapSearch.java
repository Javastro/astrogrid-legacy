package org.astrogrid.stapsearch.impl.astrogrid_stap;

import org.astrogrid.stapsearch.IStapSearch;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.BufferedWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Set;

import uk.ac.starlink.table.ColumnInfo;
import java.sql.*;

import org.astrogrid.tools.votable.stap.v0_1.STAPMaker;
import org.astrogrid.config.Config;

import java.io.IOException;

public class StapSearch implements IStapSearch {
    
    /**
     * conf - Config variable to access the configuration for the server normally
     * jndi to a config file.
     * @see org.astrogrid.config.Config
     */   
    public static Config conf = null;
    
    private HashMap noDuplMap = null;
    
    private static String STAP_DEFAULT_FORMAT;   
    
    private final int numCols;
    
    private STAPMaker stapMaker;
    
    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
       if(conf == null) {
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
          STAP_DEFAULT_FORMAT = conf.getString("format.default","TIME_SERIES");
       }
    }       
    
    public StapSearch() {
        noDuplMap = new HashMap();
        this.stapMaker = createStapMaker();
        numCols = stapMaker.getNumberOfColumns();
    }
    
    private void printMap(Map info) {
        Set st = info.keySet();
        Object []keyarray = st.toArray();
        for(int i = 0;i < keyarray.length;i++) {
            System.out.println("key = " + keyarray[i] + " val = " + ((String [])info.get(keyarray[i]))[0]  );
        }//for
    }
    
    private STAPMaker createStapMaker() {
    	String moreCols = null;
    	int i = 0;
    	while( (moreCols = conf.getString("results.more." + String.valueOf(i),null)) != null) {
    		i++;
    	}
    	
    	ColumnInfo []defValues;
        defValues = new ColumnInfo[(9+i)];
        defValues[0] = new ColumnInfo("ACCESS_URL",String.class,"Url pointing to data file");
        defValues[0].setUCD("VOX:AccessReference");
        defValues[1] = new ColumnInfo("PROVIDER",String.class,"The archive (STAP service) providing the data");
        defValues[1].setUCD("meta.curation");
        defValues[2] = new ColumnInfo("TIME_START",String.class,"Start time of data in the file  ");
        defValues[2].setUCD("time.obs.start");
        defValues[2].setUnitString("iso8601");
        defValues[3] = new ColumnInfo("TIME_END",String.class,"End time of data in the file  ");
        defValues[3].setUCD("time.obs.end");
        defValues[3].setUnitString("iso8601");
        defValues[4] = new ColumnInfo("DATA_ID",String.class,"Short description of the measurement");
        defValues[4].setUCD("meta.title");        
        defValues[5] = new ColumnInfo("INSTRUMENT_ID",String.class,"A string specifiying the mission and instrument");
        defValues[5].setUCD("INST_ID");        
        defValues[6] = new ColumnInfo("DESCRIPTION",String.class,"A string containing supplemental information on the data file");
        defValues[6].setUCD("meta");
        defValues[7] = new ColumnInfo("DESCRIPTION_URL",String.class,"A URL pointing to information on the data product");
        defValues[7].setUCD("meta.ref.url");
        defValues[8] = new ColumnInfo("FORMAT",String.class,"Format");
        defValues[8].setUCD("VOX:Format");  
        String []newCols;
        
        for(int j = 0;j < i;j++) {
        	newCols = conf.getString("results.more." + String.valueOf(j)).split("\\|");
        	//System.out.println("the newcols 0 = " + newCols[0] + " 1 = " + newCols[1] + " 2 " + newCols[2] + " 3 " + newCols[3] + " 4" + newCols[4] + " 5 " + newCols[5]);
        	defValues[j+9] = new ColumnInfo(newCols[0]);
        	if(newCols[1] != null && newCols[1].trim().length() > 0) {
        		try {
        			defValues[j+9].setContentClass(Class.forName("java.lang." + newCols[1]));
        		}catch(ClassNotFoundException cfe) {
        			cfe.printStackTrace();
        		}
        	}
        	if(newCols[2] != null && newCols[2].trim().length() > 0) {
        		defValues[j+9].setDescription(newCols[2]);
        	}
        	if(newCols[3] != null && newCols[3].trim().length() > 0) {
        		defValues[j+9].setUnitString(newCols[3]);
        	}
        	if(newCols[4] != null && newCols[4].trim().length() > 0) {
        		defValues[j+9].setUCD(newCols[4]);
        	}
        }
        STAPMaker stapMaker = new STAPMaker(defValues);
        return stapMaker;
    }
    
    
    public void execute(Calendar startTime, Calendar endTime, Map info, PrintWriter output) throws IOException {

        STAPMaker stapMaker = createStapMaker();
    	String formatReq = null;
        if(info.containsKey("FORMAT")) {
            formatReq = ((String [])info.get("FORMAT"))[0];
        }
        
        String ra = null;
        String dec = null;
        String size = null;
        String pos = null;
        if(info.containsKey("POS")) {
            pos = ((String [])info.get("POS"))[0];
            ra = pos.split(",")[0];
            dec = pos.split(",")[1];
        }
        if(info.containsKey("SIZE")) {
            size = ((String [])info.get("SIZE"))[0];
        }
        
        String convertFormat = conf.getString("convert.time.format","yyyy-MM-dd HH:mm:ss");
        String endTimeSQL = null;
        String startTimeSQL = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat();

        if(convertFormat.equals("milliseconds")) {
        	startTimeSQL = String.valueOf(startTime.getTimeInMillis());
        	endTimeSQL = String.valueOf(endTime.getTimeInMillis());
        }else {
          dateFormat = new SimpleDateFormat(convertFormat);
          //dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
          startTimeSQL = dateFormat.format(startTime.getTime());
          endTimeSQL = dateFormat.format(endTime.getTime());
        }
        
        SimpleDateFormat stapDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        stapDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        

        String []resultsData = new String[numCols];
        String []queryCols = new String[numCols];
        String cols = "";
        
        resultsData[0] = conf.getString("results.dataid",null);
        resultsData[1] = conf.getString("results.start",null);
        resultsData[2] = conf.getString("results.end",null);
        resultsData[3] = conf.getString("results.accessref",null);
        resultsData[4] = conf.getString("results.provider",null);
        resultsData[5] = conf.getString("results.description",null);
        resultsData[6] = conf.getString("results.descriptionURL",null);
        resultsData[7] = conf.getString("results.instrumentID",null);
        resultsData[8] = STAP_DEFAULT_FORMAT;
        
        
        for(int j = 0;j < (numCols - 9);j++) {
        	String []newCols = (conf.getString("results.more." + String.valueOf(j))).split("\\|");
        	resultsData[9 + j] = newCols[5];
        }
        
        String accRefPrePend = conf.getString("accessref.prepend","");
        
        for(int i = 0;i < resultsData.length;i++) {
        	if(resultsData[i] != null && resultsData[i].startsWith("col.")) {
        		queryCols[i] = new String(resultsData[i].substring(4));
        		cols += resultsData[i].substring(4) + ",";
        		resultsData[i] = null;
        	}
        }
        cols = cols.substring(0,cols.length() - 1);
        
        //lets query database.
        Connection conn = null;
        String jdbcString = conf.getString("jdbc.driver",null);
        String jdbcURL = conf.getString("jdbc.url",null);
        String user = conf.getString("jdbc.user","");
        String passwd = conf.getString("jdbc.password","");
        BufferedWriter out =
            new BufferedWriter( output );
        try { 

            // Step 1: Load the JDBC driver. 
            Class.forName(jdbcString);
            // Step 2: Establish the connection to the database.  
            conn = DriverManager.getConnection(jdbcURL,user,passwd);

        } catch (Exception e) { 
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
        }
        Statement stmt = null;
        try {
        stmt = conn.createStatement();
        //String query = conf.getString("full.sql.syntax");
        //String sql = query.replaceAll("__start__", startTimeSQL).replaceAll("__end__", endTimeSQL);  
        
        String query = null;
        String sql = null;
        if(ra != null && size != null) {
           query = conf.getString("full.sql.syntax.pos");
           sql = query.replaceAll("__ra__", ra).replaceAll("__dec__", dec).replaceAll("__size__", size).replaceAll("__start__",
        startTimeSQL).replaceAll("__end__", endTimeSQL);
        } else {
           query = conf.getString("full.sql.syntax");
           sql = query.replaceAll("__start__", startTimeSQL).replaceAll("__end__", endTimeSQL);       }
        
        System.out.println("Query to be Executed by Stap -- " + sql);
        ResultSet rs = stmt.executeQuery(sql);
       
        stapMaker.writeBeginVOTable(out,conf.getString("results.name","DSA"));        
        Calendar startTimeCal = Calendar.getInstance();
        Calendar endTimeCal = Calendar.getInstance();
        
        Calendar startTemp = Calendar.getInstance();
        Calendar endTemp = Calendar.getInstance();
        
        String tempRS = null;
        while(rs.next()){
         //System.out.println("the title = " + views[i].getTitle() + " id = " + views[i].getId());
        	if(queryCols[0] != null)
        		stapMaker.setDataID(rs.getString(queryCols[0]));        	
        	if(queryCols[1] != null) {
        		tempRS = rs.getString(queryCols[1]);
        		if(convertFormat.equals("milliseconds")) {
        			startTemp.setTimeInMillis(Long.valueOf(tempRS).longValue());
        		}else {
        			startTemp.setTime(dateFormat.parse(rs.getString(queryCols[1])));
        		}
        		stapMaker.setTimeStart(stapDateFormat.format(startTemp.getTime()));
        	}
        	if(queryCols[2] != null) {
        		tempRS = rs.getString(queryCols[1]);
        		if(convertFormat.equals("milliseconds")) {
        			endTemp.setTimeInMillis(Long.valueOf(tempRS).longValue());
        		}else {
        			endTemp.setTime(dateFormat.parse(rs.getString(queryCols[2])));
        		}
        		stapMaker.setTimeEnd(stapDateFormat.format(endTemp.getTime()));
        	}
        	stapMaker.setFormat(STAP_DEFAULT_FORMAT);        		
        	if(queryCols[3] != null) {
        		tempRS = rs.getString(queryCols[3]);
        		stapMaker.setAccessReference(accRefPrePend + tempRS);
        		stapMaker.setFormat(getFormat(tempRS));
        	}
        	if(queryCols[4] != null)
        		stapMaker.setProvider(rs.getString(queryCols[4]));
        	if(queryCols[5] != null)
        		stapMaker.setDescription(rs.getString(queryCols[5]));
        	if(queryCols[6] != null)        		
        		stapMaker.setDescriptionURL(rs.getString(queryCols[6]));
        	if(queryCols[7] != null)
        		stapMaker.setInstrumentID(rs.getString(queryCols[7]));
        	if(queryCols.length >= 9) {
        		for(int j = 9;j < queryCols.length;j++) {
        			if(queryCols[j] != null) {
        				stapMaker.setColumn(j,rs.getObject(queryCols[j]));
        			}//if
        		}//for
        	}//if
        	
        	if(resultsData[0] != null)
        		stapMaker.setDataID(resultsData[0]);        	
        	if(resultsData[1] != null)
        		stapMaker.setTimeStart(resultsData[1]);
        	if(resultsData[2] != null)
        		stapMaker.setTimeEnd(resultsData[2]);
        	if(resultsData[4] != null)
        		stapMaker.setProvider(resultsData[4]);
        	if(resultsData[5] != null)
        		stapMaker.setDescription(resultsData[5]);
        	if(resultsData[6] != null)
        		stapMaker.setDescriptionURL(resultsData[6]);
        	if(resultsData[7] != null)        		
        		stapMaker.setInstrumentID(resultsData[7]);
        	if(resultsData.length >= 9) {
        		for(int j = 9;j < resultsData.length;j++) {
        			if(resultsData[j] != null) {
        				//System.out.println("ouch its in result data for some reason");
        				stapMaker.setColumn(j,(Object)resultsData[j]);
        			}//if
        		}//for        		
        	}
            stapMaker.addRow();
          }
        	if(stapMaker.getRowCount() > 0) {
        		stapMaker.writeTable(out);
        	}
        }catch(SQLException sqe) {
        	sqe.printStackTrace();
        	out.write(sqe.getMessage());
        	return;
        }catch(ParseException pe) {
        	pe.printStackTrace();
        	out.write(pe.getMessage());
        	return;
        }
        finally {
        	try {
        		if(stmt != null)
        			stmt.close();
        		if(conn != null)
        			conn.close();
        	}catch(SQLException sqe) {
        		sqe.printStackTrace();
        		out.write(sqe.getMessage());
        		return;
        	}
            stapMaker.writeEndVOTable(out);
        }
    }
    
    private String getFormat(String accessRefExtension) {
        if(accessRefExtension.lastIndexOf('.') != -1)
            accessRefExtension = accessRefExtension.substring(accessRefExtension.lastIndexOf('.')+1);
        
        if(accessRefExtension.equalsIgnoreCase("fits") || accessRefExtension.equalsIgnoreCase("fts")) {
        	return conf.getString("format.ending.fits","FITS");
        }else if(accessRefExtension.equalsIgnoreCase("jpg") || accessRefExtension.equalsIgnoreCase("gif") ) {
        	return "GRAPHICS";
        }else if(accessRefExtension.equalsIgnoreCase("cdf")) {
        	return conf.getString("format.ending.cdf","TIME_SERIES-CDF");        	
        }else if(accessRefExtension.equalsIgnoreCase("txt")) {
        	return conf.getString("format.ending.txt","TIME_SERIES-ASCII");        	
        }else if(accessRefExtension.equalsIgnoreCase("vot")) {
        	return conf.getString("format.ending.vot","TIME_SERIES-VOT");        	
        }
        return STAP_DEFAULT_FORMAT;
    }
    
}