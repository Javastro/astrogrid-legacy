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

import org.astrogrid.tools.votable.stap.v0_1.STAPMaker;

import org.astrogrid.solarsearch.ws.cdaw.ViewDescription;
import org.astrogrid.solarsearch.ws.cdaw.DatasetDescription;
import org.astrogrid.solarsearch.ws.cdaw.ResultDescription;



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
        String formatReq = null;
        if(info.containsKey("FORMAT")) {
            formatReq = (String)info.get("FORMAT");
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

        STAPMaker stapMaker = new STAPMaker();

        BufferedWriter out =
           new BufferedWriter( output );
        stapMaker.writeBeginVOTable(out,missionName[0]);

        Calendar startTemp = Calendar.getInstance();
        Calendar endTemp = Calendar.getInstance();
        
        for(int i = 0;i < views.length;i++) {
            if(views[i].isPublicAccess()) {
               //System.out.println("getting mission groups from view = " + views[i].getTitle());
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
               String [][]instruments = binding.getAllInstruments();
               /*
               System.out.println("instruments length = " + instruments.length);
               for(int r = 0;r < instruments.length;r++) {
                   System.out.println("short name = " + instruments[r][0] + " long name = " + instruments[r][1]);                   
               }
               String []instrumentType = binding.getAllInstrumentTypes();
               
               System.out.println("instrument types length = " + instrumentType.length);
               for(int r = 0;r < instrumentType.length;r++) {
                   System.out.println("instrument type = " + instrumentType[r]);                   
               } 
               */              
               for(int j = 0;j < missionGroups.length;j++) {
                  //System.out.println("comparing " + missionName[0].toUpperCase() + " with " + missionGroups[j].toUpperCase());
                  if(missionName[0].toUpperCase().equals(missionGroups[j].toUpperCase())) {
                     DatasetDescription []dsd = binding.getDatasets(missionName, new String[0]);
                     for(int k = 0;k < dsd.length;k++) {
                        if(startTime.before(dsd[k].getStartTime())) {
                           System.out.println("WARNING: Your given start time is before the dataset end time; using dataset start time");
                           startTemp = dsd[k].getStartTime();
                        }
                        if(endTime.after(dsd[k].getEndTime())) {
                           System.out.println("WARNING: Your given end time is after the datasets end time; using the dataset end time");
                           endTemp = dsd[k].getEndTime();
                        }
                        String []dsURLS = binding.getDataUrls(dsd[k].getId(),startTemp,endTemp);
                        if(dsURLS != null && dsURLS.length > 0) {
                           
                           for(int m = 0;m < dsURLS.length;m++) {
                              if(correctFormat(formatReq,dsURLS[m].substring(dsURLS[m].lastIndexOf('.')))) {
                                  stapMaker.setDataID(dsd[k].getLabel());
                                  stapMaker.setTimeStart(dateFormat.format(dsd[k].getStartTime().getTime()));
                                  stapMaker.setTimeEnd(dateFormat.format(dsd[k].getEndTime().getTime()));
                                  stapMaker.setAccessReference(dsURLS[m]);
                                  stapMaker.setProvider("CDAW");
                                  stapMaker.setDescription(dsd[k].getLabel());
                                  if(dsd[k].getNotesUrl() != null && dsd[k].getNotesUrl().trim().length() > 0) {
                                      stapMaker.setDescriptionURL(dsd[k].getNotesUrl());
                                  }//if                              
                                  stapMaker.setFormat(
                                  (String)info.get("format.ending." + dsURLS[m].substring(dsURLS[m].lastIndexOf('.'))) != null ?
                                  (String)info.get("format.ending." + dsURLS[m].substring(dsURLS[m].lastIndexOf('.'))) :
                                  (String)info.get("format.default"));
                                  stapMaker.addRow();
                              }
                           }
                           if(stapMaker.getRowCount() > 0)
                               stapMaker.writeTable(out);
                        }
                     }//for
                  }//if
               }//for
            }else {
               System.out.println("Skipping analyzing missions from this view, no public access title = " + views[i].getTitle());
            }
        }//for        
        stapMaker.writeEndVOTable(out);
    }
    
    private boolean correctFormat(String format, String accessRefExtension) {
        if(format == null || format.trim().length() == 0) {
            return true;
        }        
        if(format.equals("Graphic") && 
           (accessRefExtension.equalsIgnoreCase("fits") || accessRefExtension.equalsIgnoreCase("jpg") ||
            accessRefExtension.equalsIgnoreCase("gif"))) {
            return true;
        }
        if(format.equals("TIME_SERIES") && 
                (accessRefExtension.equalsIgnoreCase("cdf") || accessRefExtension.equalsIgnoreCase("txt") ||
                 accessRefExtension.equalsIgnoreCase("vot"))) {
                 return true;
        }
        return false;
    }
    
    
    private static void printDataSet(DatasetDescription dsd) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        System.out.println("Begin - printing DataSet Information ...");
        System.out.println("Label = " + dsd.getLabel() + " id = " + dsd.getId() + " pi Affiliate = " + dsd.getPiAffiliation() + " piName = " + dsd.getPiName() + " starttime = " + dateFormat.format(dsd.getStartTime().getTime()) + " and endTime = " + dateFormat.format(dsd.getEndTime().getTime()));
        System.out.println("End of printing out dataset info for label = " + dsd.getLabel());
     }    
    
}