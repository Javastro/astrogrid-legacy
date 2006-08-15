package org.astrogrid.stapsearch.impl.astrogrid_dsa;

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
    
    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
       if(conf == null) {
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
       }
    }       
    
    public StapSearch() {
        noDuplMap = new HashMap();
    }
    
    private void printMap(Map info) {
        Set st = info.keySet();
        Object []keyarray = st.toArray();
        for(int i = 0;i < keyarray.length;i++) {
            System.out.println("key = " + keyarray[i] + " val = " + ((String [])info.get(keyarray[i]))[0]  );
        }//for
    }
    
    
    public void execute(Calendar startTime, Calendar endTime, Map info, PrintWriter output) throws IOException {

        STAPMaker stapMaker = new STAPMaker();
    	String formatReq = null;
        if(info.containsKey("FORMAT")) {
            formatReq = ((String [])info.get("FORMAT"))[0];
        }
        
        String convertFormat = conf.getString("convert.time.format","yyyy-MM-dd'T'HH:mm:ss");
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
        

        String []resultsData = new String[8];
        String []queryCols = new String[8];
        String cols = "";
        
        resultsData[0] = conf.getString("results.dataid",null);
        resultsData[1] = conf.getString("results.start",null);
        resultsData[2] = conf.getString("results.end",null);
        resultsData[3] = conf.getString("results.accessref",null);
        resultsData[4] = conf.getString("results.provider",null);
        resultsData[5] = conf.getString("results.description",null);
        resultsData[6] = conf.getString("results.descriptionURL",null);
        resultsData[7] = conf.getString("results.instrumentID",null);
        
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
        try {
        Statement stmt = conn.createStatement();
        String query = conf.getString("full.sql.syntax");
        String sql = query.replaceAll("__start__", startTimeSQL).replaceAll("__end__", endTimeSQL);        
        
        ResultSet rs = stmt.executeQuery(sql);
       

  
        
        stapMaker.writeBeginVOTable(out,conf.getString("results.name","DSA"));        
        Calendar startTimeCal = Calendar.getInstance();
        Calendar endTimeCal = Calendar.getInstance();

        
        Calendar startTemp = Calendar.getInstance();
        Calendar endTemp = Calendar.getInstance();
        
        String tempFormat = null;
        while(rs.next()){
         //System.out.println("the title = " + views[i].getTitle() + " id = " + views[i].getId());
        	if(queryCols[0] != null)
        		stapMaker.setDataID(rs.getString(queryCols[0]));        	
        	if(queryCols[1] != null) {
        		tempFormat = rs.getString(queryCols[1]);
        		if(convertFormat.equals("milliseconds")) {
        			startTemp.setTimeInMillis(Long.valueOf(tempFormat).longValue());
        		}else {
        			startTemp.setTime(dateFormat.parse(rs.getString(queryCols[1])));
        		}
        		stapMaker.setTimeStart(stapDateFormat.format(startTemp));
        	}
        	if(queryCols[2] != null) {
        		tempFormat = rs.getString(queryCols[1]);
        		if(convertFormat.equals("milliseconds")) {
        			endTemp.setTimeInMillis(Long.valueOf(tempFormat).longValue());
        		}else {
        			endTemp.setTime(dateFormat.parse(rs.getString(queryCols[2])));
        		}
        		stapMaker.setTimeEnd(stapDateFormat.format(endTemp));
        	}
        		
        	if(queryCols[3] != null)
        		stapMaker.setAccessReference(accRefPrePend + rs.getString(queryCols[3]));
        	if(queryCols[4] != null)
        		stapMaker.setProvider(rs.getString(queryCols[4]));
        	if(queryCols[5] != null)
        		stapMaker.setDescription(rs.getString(queryCols[5]));
        	if(queryCols[6] != null)
        		stapMaker.setInstrumentID(rs.getString(queryCols[6]));
        	if(queryCols[7] != null)
        		stapMaker.setDescriptionURL(rs.getString(queryCols[7]));
        	
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
        		stapMaker.setInstrumentID(resultsData[6]);
        	if(resultsData[7] != null)
        		stapMaker.setDescriptionURL(resultsData[7]);
        	
        	
            tempFormat = DEFAULT_FORMAT;            
            stapMaker.setFormat(tempFormat);                                  
            stapMaker.addRow();
            if(stapMaker.getRowCount() > 0) {
                stapMaker.writeTable(out);
            }
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
            stapMaker.writeEndVOTable(out);
        }
    }
    
    private static final String DEFAULT_FORMAT = "TIME_SERIES";
    
    private boolean correctFormat(String format, String accessRefExtension) {
        boolean timeSeries = false;
        boolean graphics = false;
        if(format == null || format.trim().length() == 0) {
            return true;
        }
        if(accessRefExtension.lastIndexOf('.') != -1)
            accessRefExtension = accessRefExtension.substring(accessRefExtension.lastIndexOf('.')+1);
        
        if((accessRefExtension.equalsIgnoreCase("fits") || accessRefExtension.equalsIgnoreCase("jpg") ||
            accessRefExtension.equalsIgnoreCase("gif") || accessRefExtension.equalsIgnoreCase("fts"))) {
            graphics = true;
        }
        
        if((accessRefExtension.equalsIgnoreCase("cdf") || accessRefExtension.equalsIgnoreCase("txt") ||
                 accessRefExtension.equalsIgnoreCase("vot"))) {
            timeSeries = true;
        }
        if(format.equalsIgnoreCase("TIME_SERIES") && timeSeries)
            return true;
        else if(format.equalsIgnoreCase("GRAPHICS") && graphics)
            return true;
        else if(!timeSeries && !graphics && format.equalsIgnoreCase(DEFAULT_FORMAT))
            return true;

        return false;
    }
    
}