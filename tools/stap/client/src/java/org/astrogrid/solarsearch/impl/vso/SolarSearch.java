package org.astrogrid.solarsearch.impl.vso;

import org.astrogrid.solarsearch.ISolarSearch;
import java.util.Date;
import java.util.Map;
import java.io.PrintWriter;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Set;
import java.util.HashMap;

import org.astrogrid.tools.votable.stap.v0_1.STAPMaker;
import org.astrogrid.config.Config;

import org.astrogrid.solarsearch.ws.vso.Data;
import org.astrogrid.solarsearch.ws.vso.DataRequest;
import org.astrogrid.solarsearch.ws.vso.VSOGetDataRequest;
import org.astrogrid.solarsearch.ws.vso.GetDataRequest;
import org.astrogrid.solarsearch.ws.vso.ProviderGetDataResponse;
import org.astrogrid.solarsearch.ws.vso.ProviderQueryResponse;
import org.astrogrid.solarsearch.ws.vso.QueryRequest;
import org.astrogrid.solarsearch.ws.vso.QueryRequestBlock;
import org.astrogrid.solarsearch.ws.vso.QueryResponseBlock;
import org.astrogrid.solarsearch.ws.vso.Extent;
import org.astrogrid.solarsearch.ws.vso.Wave;
import org.astrogrid.solarsearch.ws.vso.Time;
import org.astrogrid.solarsearch.ws.vso.VSOiBindingStub;

import java.io.InputStream;
import java.io.IOException;

public class SolarSearch implements ISolarSearch {
    
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
    
    public SolarSearch() {
        //System.out.println("instantiated");
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
        
        VSOiBindingStub binding;
        try {
            binding = (VSOiBindingStub)
                          new org.astrogrid.solarsearch.ws.vso.VSOiServiceLocator().getsdacVSOi();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        if(info.containsKey("fileid")) {
            getData(binding,info,output);
        }else {
            query(binding,startTime, endTime, info, output);
        }        
        output.close();
    }
    
    private void getData(VSOiBindingStub binding,Map info, PrintWriter output) throws IOException {
        Float versionNumber = new Float(1.0);
        try {
            if(info.containsKey("version")) {
                versionNumber = new Float ( ((String [])info.get("version"))[0]);
            }
        }catch(Exception e2) {
            System.out.println("could not parse version");
            e2.printStackTrace();
        }
        String []fileID = new String[1];
        fileID[0] = ((String [])info.get("fileid"))[0];
        String provider = ((String [])info.get("provider"))[0];
        System.out.println("the fileid for this request = " + fileID[0]);
        GetDataRequest gdr = new GetDataRequest();
        String []methods = {"URL"};
        gdr.setMethod(methods);
        DataRequest []dr = new DataRequest[1];
        dr[0] = new DataRequest(provider,fileID);
        gdr.setData(dr);
        VSOGetDataRequest vdr = new VSOGetDataRequest(versionNumber,gdr);
        ProviderGetDataResponse []pdr = binding.getData(vdr);
        Data []data;
        String urlString = null;
        URL url;
        int temp = 0;
        InputStream is;

        for(int j = 0;j < pdr.length;j++) {
            data = pdr[j].getData();
            for(int i = 0;i < data.length;i++) {
                urlString = data[i].getUrl();
                System.out.println("here is the urlString = " + urlString);
                url = new URL(urlString);            
                is = url.openStream();
                temp = 0;
                while( (temp = is.available()) > 0) {
                    byte []resultData = new byte[temp];
                    is.read(resultData);
                    output.write(new String(resultData));
                }//while
            }//for
        }//for
    }
    
    private void query(VSOiBindingStub binding, Calendar startTime, Calendar endTime, Map info, PrintWriter output) throws IOException {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        
        SimpleDateFormat dateFormatResponse = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss");
        dateFormatResponse.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));        
        
        String dataFetchURL = null;
        
        String []instrumentName = new String[1];
        //printMap(info);
        if(!info.containsKey("INSTRUMENT_ID")) {
            output.print("could not find instrument for query");
        }
        
        
        String formatReq = null;
        
        if(info.containsKey("FORMAT")) {
            formatReq = ((String [])info.get("FORMAT"))[0];
        }

        instrumentName[0] = ((String [])info.get("INSTRUMENT_ID"))[0];
        Calendar startTimeCal = Calendar.getInstance();
        Calendar endTimeCal = Calendar.getInstance();
        
        /*
        ColumnInfo[] colInfos = new ColumnInfo[ 24 ];
        colInfos[ 0 ] = new ColumnInfo( "Version", Float.class, "Version" );
        colInfos[ 1 ] = new ColumnInfo( "Provider", String.class, "Provider" );
        colInfos[ 2 ] = new ColumnInfo( "NoRecordsFound", Integer.class, "Number of records found" );
        colInfos[ 3 ] = new ColumnInfo( "NoRecordsReturned", Integer.class, "Number of records returned" );
        colInfos[ 4 ] = new ColumnInfo( "Error", String.class, "Error Messages" );
        colInfos[ 5 ] = new ColumnInfo( "Status", String.class, "Status Message" );
        colInfos[ 6 ] = new ColumnInfo( "QRB_Provider", String.class, "Provider" );
        colInfos[ 7 ] = new ColumnInfo( "QRB_Source", String.class, "Source" );
        colInfos[ 8 ] = new ColumnInfo( "QRB_Instrument", String.class, "Instrument" );
        colInfos[ 9 ] = new ColumnInfo( "QRB_PhysicalObs", String.class, "Physical Observations" );
        colInfos[ 10 ] = new ColumnInfo( "QRB_StartTime", String.class, "Start Time" );
        colInfos[ 11 ] = new ColumnInfo( "QRB_EndTime", String.class, "End Time" );
        colInfos[ 12 ] = new ColumnInfo( "QRB_WaveMin", Float.class, "Wave Min" );
        colInfos[ 13 ] = new ColumnInfo( "QRB_WaveMax", Float.class, "Wave Max" );
        colInfos[ 14 ] = new ColumnInfo( "QRB_WaveUnit", String.class, "Wave Unit" );
        colInfos[ 15 ] = new ColumnInfo( "QRB_WaveType", String.class, "Wave Type" );        
        colInfos[ 16 ] = new ColumnInfo( "QRB_ExtentX", String.class, "Extent X" );
        colInfos[ 17 ] = new ColumnInfo( "QRB_ExtentY", String.class, "Extent Y" );
        colInfos[ 18 ] = new ColumnInfo( "QRB_ExtentWidth", String.class, "Extent Width" );
        colInfos[ 19 ] = new ColumnInfo( "QRB_ExtentLength", String.class, "Extent Length" );
        colInfos[ 20 ] = new ColumnInfo( "QRB_ExtentType", String.class, "Extent Type" );
        colInfos[ 21 ] = new ColumnInfo( "QRB_FileID", String.class, "FileID" );
        colInfos[ 22 ] = new ColumnInfo( "QRB_Size", Float.class, "Size" );
        colInfos[ 23 ] = new ColumnInfo( "QRB_Info", String.class, "Info" );        
        */
        
        STAPMaker stapMaker = new STAPMaker();
        BufferedWriter out =
            new BufferedWriter( output );
        stapMaker.writeBeginVOTable(out,instrumentName[0]);        
        try {
        ProviderQueryResponse[] value = null;
        
        Time queryTime = new Time(dateFormat.format(startTime.getTime()), dateFormat.format(endTime.getTime()));
        //ok try TRACE   CDS  COSTEP 2001-10-01 00:00:00   until 2001-10-01 03:00:00
        QueryRequestBlock qrb = new QueryRequestBlock();        
        qrb.setInstrument(instrumentName[0]);
        qrb.setTime(queryTime);
        
        Float versionNumber = new Float(1.0);
        try {
            if(info.containsKey("version")) {
                versionNumber = new Float ( ((String [])info.get("version"))[0]);
            }
        }catch(Exception e2) {
            System.out.println("could not parse version");
            e2.printStackTrace();
        }
        //System.out.println("version number being used = " + versionNumber.toString());
        String serviceURL = conf.getString("service.url","http://localhost:8080/");
        
        //System.out.println("making query on instrument = " + instrumentName[0] + " time start = " + queryTime.getStart() + " end time = " + queryTime.getEnd());
        System.out.println("doing query now");
        value = binding.query(new org.astrogrid.solarsearch.ws.vso.QueryRequest(versionNumber,qrb));
        System.out.println("done with query");
        
        //System.out.println("numbers of actual providerqueryresponse = "  + value.length);
        String tempFormat = null;
        for(int j = 0;j < value.length;j++) {
            QueryResponseBlock []qrbResults = value[j].getRecord();
            noDuplMap.clear();
            //System.out.println("results of providerqueryresponse: version = " + value[j].getVersion() + " provider = " + value[j].getProvider() + " number of rec found = " + value[j].getNo_of_records_found());
            if(qrbResults != null && qrbResults.length > 0) {
                //System.out.println("numuber of query response blocks = " + qrbResults.length);
                for(int k = 0;k < qrbResults.length;k++) {
                    if(correctFormat(formatReq,qrbResults[k].getFileid()) && !noDuplMap.containsKey(qrbResults[k].getFileid())) {
                        noDuplMap.put(qrbResults[k].getFileid(),null);
                        Extent tempExtent = new Extent();
                        if(qrbResults[k].getExtent() != null) {
                            tempExtent.setX(qrbResults[k].getExtent().getX());
                            tempExtent.setY(qrbResults[k].getExtent().getY());
                            tempExtent.setWidth(qrbResults[k].getExtent().getWidth());
                            tempExtent.setLength(qrbResults[k].getExtent().getLength());
                            tempExtent.setType(qrbResults[k].getExtent().getType());
                        }
                        Time tempTime = new Time();
                        if(qrbResults[k].getTime() != null) {
                            tempTime.setStart(qrbResults[k].getTime().getStart());
                            tempTime.setEnd(qrbResults[k].getTime().getEnd());
                        }
                        Wave tempWave = new Wave();
                        tempWave.setWavemin(Float.NaN);
                        tempWave.setWavemax(Float.NaN);
                        if(qrbResults[k].getWave() != null) {
                            tempWave.setWavemin(qrbResults[k].getWave().getWavemin());
                            tempWave.setWavemax(qrbResults[k].getWave().getWavemax());
                            tempWave.setWaveunit(qrbResults[k].getWave().getWaveunit());
                            tempWave.setWavetype(qrbResults[k].getWave().getWavetype());
                        }
                        stapMaker.setDataID(qrbResults[k].getPhysobs());                        
                        try {
                        stapMaker.setTimeStart(dateFormatResponse.format(dateFormat.parse(tempTime.getStart())));                        
                        stapMaker.setTimeEnd(dateFormatResponse.format(dateFormat.parse(tempTime.getEnd())));
                        }catch(Exception e2) {
                            e2.printStackTrace();
                        }
                        dataFetchURL = serviceURL + "?service=vso"; 
                        dataFetchURL += "&START=" + dateFormatResponse.format(startTime.getTime());
                        dataFetchURL += "&END=" + dateFormatResponse.format(endTime.getTime());
                        dataFetchURL += "&provider=" + qrbResults[k].getProvider();
                        dataFetchURL += "&version=" + versionNumber.toString();
                        dataFetchURL += "&fileid=" + qrbResults[k].getFileid();
                        stapMaker.setAccessReference(dataFetchURL);//URLEncoder.encode(dataFetchURL,"UTF-8"));                    
                        stapMaker.setProvider("VSO - " + value[j].getProvider());
                        stapMaker.setDescription("Physical Object = " + qrbResults[k].getPhysobs() + "-- Source = " + qrbResults[k].getSource() +
                                " Wave: min = " + tempWave.getWavemin() + " max = " + tempWave.getWavemax() + " unit = " + tempWave.getWaveunit() +
                                " type = " + tempWave.getWavetype()); 
                        stapMaker.setInstrumentID(qrbResults[k].getInstrument());
                        
                        tempFormat = DEFAULT_FORMAT;
                        if(qrbResults[k].getFileid().lastIndexOf('.') != -1) {
                            tempFormat = conf.getString("format.ending." + qrbResults[k].getFileid().substring(qrbResults[k].getFileid().lastIndexOf('.')+1), tempFormat);
                        }
                        stapMaker.setFormat(tempFormat);
                        /*
                        astro.addRow( new Object[] { 
                                new Float(value[j].getVersion())
                                value[j].getProvider(),
                                value[j].getNo_of_records_found(),
                                value[j].getNo_of_records_returned(),
                                value[j].getError(),
                                value[j].getStatus(),                                                
                                qrbResults[k].getProvider(),
                                qrbResults[k].getSource(),
                                qrbResults[k].getInstrument(),
                                qrbResults[k].getPhysobs(),
                                tempTime.getStart(),
                                tempTime.getEnd(),
                                new Float(tempWave.getWavemin()),
                                new Float(tempWave.getWavemax()),
                                tempWave.getWaveunit(),
                                tempWave.getWavetype(),
                                tempExtent.getX(),
                                tempExtent.getY(),
                                tempExtent.getWidth(),
                                tempExtent.getLength(),
                                tempExtent.getType(),
                                qrbResults[k].getFileid(),
                                qrbResults[k].getSize(),
                                qrbResults[k].getInfo()} );                    
                   
                    */
                    stapMaker.addRow();
                    }//if
                }//for
                if(stapMaker.getRowCount() > 0)
                    stapMaker.writeTable(out);
            }//if            
        }//for
        }finally {
            stapMaker.writeEndVOTable(out);
        }
    }
    
    private static final String DEFAULT_FORMAT = "GRAPHICS";
    
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

