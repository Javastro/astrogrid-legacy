package org.astrogrid.desktop.modules.ui;

import java.net.URI;

import org.votech.plastic.PlasticHubListener;

/** 
 * standard interface that all workbench UI components shoud implement if they
 * are accessible as 'plastic applications'
 * */
public interface PlasticWrapper {
    /** access the hub ths application is connected to */
    public PlasticHubListener getHub();
    /** access the plastic id assigned to this application by the hub.*/
    public URI getPlasticId();
}