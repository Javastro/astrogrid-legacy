package org.astrogrid.solarsearch.impl.cdaw;

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

import org.astrogrid.solarsearch.ws.cdaw.ViewDescription;
import org.astrogrid.solarsearch.ws.cdaw.DatasetDescription;
import org.astrogrid.solarsearch.ws.cdaw.ResultDescription;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowListStarTable;
import uk.ac.starlink.votable.VOSerializer;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOTableWriter;

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
        "yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        
        String []missionName = new String[1];
        printMap(info);
        if(!info.containsKey("mission")) {
            output.print("could not find mission");
        }
        System.out.println("the info.get(mission) = " + info.get("mission"));
        missionName[0] = ((String [])info.get("mission"))[0];
        Calendar startTimeCal = Calendar.getInstance();
        Calendar endTimeCal = Calendar.getInstance();
        
        org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        
        ViewDescription[] value = null;        
        ViewDescription[] views = binding.getAllViewDescriptions();

        ColumnInfo[] colInfos = new ColumnInfo[ 4 ];
        colInfos[ 0 ] = new ColumnInfo( "DatasetName", String.class, "Dataset Name" );
        colInfos[ 1 ] = new ColumnInfo( "EarliestTime", String.class, "Earliest Time in Dataset" );
        colInfos[ 2 ] = new ColumnInfo( "LatestTime", String.class, "Latest Time in Dataset" );
        colInfos[ 3 ] = new ColumnInfo( "URLData", String.class, "URL to the Data" );
        
        RowListStarTable astro = new RowListStarTable( colInfos );


        BufferedWriter out =
           new BufferedWriter( output );
        out.write( "<VOTABLE version='1.1'>\n" );
        out.write( "<RESOURCE>\n" );
        out.write( "<DESCRIPTION>Tables for " + missionName[0] + "</DESCRIPTION>\n" );
        
        Calendar startTemp = Calendar.getInstance();
        Calendar endTemp = Calendar.getInstance();
        
        for(int i = 0;i < views.length;i++) {
            if(views[i].isPublicAccess()) {
               System.out.println("getting mission groups from view = " + views[i].getTitle());
               try {
                   binding = (org.astrogrid.solarsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                                 new org.astrogrid.solarsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort(new URL(views[i].getEndpointAddress()));
               }
               catch (javax.xml.rpc.ServiceException jre) {
                   if(jre.getLinkedCause()!=null)
                       jre.getLinkedCause().printStackTrace();
                   throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
               }               
               
               String []missionGroups = binding.getAllMissionGroups();
               for(int j = 0;j < missionGroups.length;j++) {
                  System.out.println("comparing " + missionName[0].toUpperCase() + " with " + missionGroups[j].toUpperCase());
                  if(missionName[0].toUpperCase().equals(missionGroups[j].toUpperCase())) {
                     System.out.println("found a match; yeah");
                     DatasetDescription []dsd = binding.getDatasets(missionName, new String[0]);
                     for(int k = 0;k < dsd.length;k++) {
                        printDataSet(dsd[k]);
                        System.out.println("getting urls from dataset = " + dsd[k].getLabel());

                        if(startTime.before(dsd[k].getStartTime())) {
                           System.out.println("WARNING: Your given start time is before the dataset end time; using dataset start time");
                           startTemp = dsd[k].getStartTime();
                        }
                        if(endTime.after(dsd[k].getEndTime())) {
                           System.out.println("WARNING: Your given end time is after the datasets end time; using the dataset end time");
                           endTemp = dsd[k].getEndTime();
                        }
                        String []dsURLS = binding.getDataUrls(dsd[k].getId(),startTemp,endTemp);
                        System.out.println("urls found for the dataset");
                        if(dsURLS != null && dsURLS.length > 0) {
                           for(int m = 0;m < dsURLS.length;m++) {
                              System.out.println("url number = " + m + " is " + dsURLS[m]);
                              astro.addRow( new Object[] { dsd[k].getLabel(),
                                    dateFormat.format(dsd[k].getStartTime().getTime()), dateFormat.format(dsd[k].getEndTime().getTime()),dsURLS[m] } );
                           }
                           VOSerializer vos = VOSerializer.makeSerializer( DataFormat.TABLEDATA, astro );
                           vos.writeInlineTableElement(out);
                           astro.clearRows();
                           vos = null;                           
                        }
                     }//for
                  }//if
               }//for
            }else {
               System.out.println("Skipping analyzing missions from this view, no public access title = " + views[i].getTitle());
            }
        }//for        
        

        out.write( "</RESOURCE>\n" );
        out.write( "</VOTABLE>\n" );
        out.flush();
    }
    
    private static void printDataSet(DatasetDescription dsd) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        System.out.println("Begin - printing DataSet Information ...");
        System.out.println("Label = " + dsd.getLabel() + " id = " + dsd.getId() + " pi Affiliate = " + dsd.getPiAffiliation() + " piName = " + dsd.getPiName() + " starttime = " + dateFormat.format(dsd.getStartTime().getTime()) + " and endTime = " + dateFormat.format(dsd.getEndTime().getTime()));
        System.out.println("End of printing out dataset info for label = " + dsd.getLabel());
     }    
    
}

