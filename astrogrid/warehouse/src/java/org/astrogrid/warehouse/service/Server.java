package org.astrogrid.warehouse.service;

import electric.registry.Registry;
import electric.server.http.HTTP;

public class Server {
  public static void main (String[] args) throws Exception {
    
    // This is the service implementation. It behaves differently
    // when inside an Axis container, so we reassure it that it's
    // safe inside GLUE.
    WarehouseServiceImpl w = new WarehouseServiceImpl();
    w.invokedViaAxis = false;
    
    // This is the HTTP server.  We start it running (i.e.
    // start up one or more daemon threads and then push
    // the warehouse-service implementation into it.
    HTTP.startup ("http://localhost:8008/glue");
    Registry.publish ("warehouse", w);
  }
}
