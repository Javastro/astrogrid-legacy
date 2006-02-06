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
import java.util.Calendar;
import java.util.Set;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowListStarTable;
import uk.ac.starlink.votable.VOSerializer;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOTableWriter;

import org.astrogrid.solarsearch.ws.vso.ProviderQueryResponse;
import org.astrogrid.solarsearch.ws.vso.QueryRequest;
import org.astrogrid.solarsearch.ws.vso.QueryRequestBlock;
import org.astrogrid.solarsearch.ws.vso.QueryResponseBlock;
import org.astrogrid.solarsearch.ws.vso.Extent;
import org.astrogrid.solarsearch.ws.vso.Wave;
import org.astrogrid.solarsearch.ws.vso.Time;

import java.io.IOException;

public class SolarSearch implements ISolarSearch {
    
    public SolarSearch() {
        System.out.println("instantiated");
    }
    
    private void printMap(Map info) {
        Set st = info.keySet();
        Object []keyarray = st.toArray();
        for(int i = 0;i < keyarray.length;i++) {
            System.out.println("key = " + keyarray[i] + " val = " + ((String [])info.get(keyarray[i]))[0]  );
        }//for
    }
    
    
    public void execute(Calendar startTime, Calendar endTime, Map info, PrintWriter output) throws IOException {
        System.out.println("enter execute");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        
        String []instrumentName = new String[1];
        printMap(info);
        if(!info.containsKey("instr")) {
            output.print("could not find instrument for query");
        }
        instrumentName[0] = ((String [])info.get("instr"))[0];
        Calendar startTimeCal = Calendar.getInstance();
        Calendar endTimeCal = Calendar.getInstance();
        
        org.astrogrid.solarsearch.ws.vso.VSOiBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.vso.VSOiBindingStub)
                          new org.astrogrid.solarsearch.ws.vso.VSOiServiceLocator().getsdacVSOi();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        
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
                        
        
        RowListStarTable astro = new RowListStarTable( colInfos );
        
        ProviderQueryResponse[] value = null;
        
        Time queryTime = new Time(dateFormat.format(startTime.getTime()), dateFormat.format(endTime.getTime()));
        //ok try TRACE   CDS  COSTEP 2001-10-01 00:00:00   until 2001-10-01 03:00:00
        QueryRequestBlock qrb = new QueryRequestBlock();        
        qrb.setInstrument(instrumentName[0]);
        qrb.setTime(queryTime);
        
        Float versionNumber = new Float(1.2);
        try {
            if(info.containsKey("version")) {
                versionNumber = new Float ( ((String [])info.get("version"))[0]);
            }
        }catch(Exception e2) {
            System.out.println("could not parse version");
            e2.printStackTrace();
        }
        System.out.println("version number being used = " + versionNumber.toString());
        
        System.out.println("making query on instrument = " + instrumentName[0] + " time start = " + queryTime.getStart() + " end time = " + queryTime.getEnd());
        value = binding.query(new org.astrogrid.solarsearch.ws.vso.QueryRequest(versionNumber,qrb));
        
        System.out.println("numbers of actual providerqueryresponse = "  + value.length);
        BufferedWriter out =
            new BufferedWriter( output );
         out.write( "<VOTABLE version='1.1'>\n" );
         out.write( "<RESOURCE>\n" );
         out.write( "<DESCRIPTION>Tables for " + instrumentName[0] + "</DESCRIPTION>\n" );
        
        
        for(int j = 0;j < value.length;j++) {
            QueryResponseBlock []qrbResults = value[j].getRecord();
            System.out.println("results of providerqueryresponse: version = " + value[j].getVersion() + " provider = " + value[j].getProvider() + " number of rec found = " + value[j].getNo_of_records_found());
            if(qrbResults != null && qrbResults.length > 0) {
                System.out.println("numuber of query response blocks = " + qrbResults.length);
                for(int k = 0;k < qrbResults.length;k++) {
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
                    if(qrbResults[k].getWave() != null) {
                        tempWave.setWavemin(qrbResults[k].getWave().getWavemin());
                        tempWave.setWavemax(qrbResults[k].getWave().getWavemax());
                        tempWave.setWaveunit(qrbResults[k].getWave().getWaveunit());
                        tempWave.setWavetype(qrbResults[k].getWave().getWavetype());
                    }
                    astro.addRow( new Object[] { 
                            new Float(value[j].getVersion()),
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
                }//for
                VOSerializer vos = VOSerializer.makeSerializer( DataFormat.TABLEDATA, astro );
                vos.writeInlineTableElement(out);
                astro.clearRows();
                vos = null;
            }//if            
        }//for        
        out.write( "</RESOURCE>\n" );
        out.write( "</VOTABLE>\n" );
        out.flush();
    }
        
}

