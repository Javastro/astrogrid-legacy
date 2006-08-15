/**
 * CoordinatedDataAnalysisSystem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.cdaw;

public interface CoordinatedDataAnalysisSystem extends java.rmi.Remote {
    public java.lang.String[] getAllInstrumentTypes() throws java.rmi.RemoteException;
    public java.lang.String[][] getAllInstruments() throws java.rmi.RemoteException;
    public java.lang.String[] getAllMissionGroups() throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.ViewDescription[] getAllViewDescriptions() throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.FileDescription[] getCdfmlDataFiles(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, java.lang.String[] arrayOfString_4, int int_5) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.FileDescription[] getCdfmlDataFiles2(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, int int_4) throws java.rmi.RemoteException;
    public java.lang.String[] getCdfmlDataUrls(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, java.lang.String[] arrayOfString_4, int int_5) throws java.rmi.RemoteException;
    public java.lang.String[] getCdfmlDataUrls2(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, int int_4) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.ResultDescription getDataAsText(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, java.lang.String[] arrayOfString_4) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.FileDescription[] getDataFiles(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.FileDescription[] getDataFiles2(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, java.lang.String[] arrayOfString_4) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.ResultDescription getDataGraph(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, java.lang.String[] arrayOfString_4) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.ResultDescription getDataGraph2(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, java.lang.String[] arrayOfString_4, long long_5) throws java.rmi.RemoteException;
    public java.lang.String[] getDataUrls(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3) throws java.rmi.RemoteException;
    public java.lang.String[] getDataUrls2(java.lang.String string_1, java.util.Calendar date_2, java.util.Calendar date_3, java.lang.String[] arrayOfString_4) throws java.rmi.RemoteException;
    public java.lang.String[][] getDatasetVariables(java.lang.String string_1) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.DatasetDescription[] getDatasets(java.lang.String[] arrayOfString_1, java.lang.String[] arrayOfString_2) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.DatasetDescription[] getDatasetsByInstrument(java.lang.String[] arrayOfString_1, java.lang.String[] arrayOfString_2) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.DatasetDescription[] getDatasetsBySource(java.lang.String[] arrayOfString_1, java.lang.String[] arrayOfString_2) throws java.rmi.RemoteException;
    public java.lang.String[] getInstrumentTypes(java.lang.String[] arrayOfString_1) throws java.rmi.RemoteException;
    public java.lang.String[][] getInstruments(java.lang.String[] arrayOfString_1) throws java.rmi.RemoteException;
    public java.lang.String[][] getSources(java.lang.String[] arrayOfString_1) throws java.rmi.RemoteException;
    public java.lang.String[][] getSourcesByInstrument(java.lang.String[] arrayOfString_1) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.cdaw.ResultDescription getThumbnailExpansion(org.astrogrid.stapsearch.ws.cdaw.ThumbnailDescription thumbnailDescription_1, int int_2) throws java.rmi.RemoteException;
}
