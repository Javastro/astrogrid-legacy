package org.astrogrid.warehouse.service;

import electric.registry.Registry;
import electric.server.http.HTTP;

public class Server {
  public static void main (String[] args) throws Exception {
    String serverUrl = null;
    if (args.length > 0) {
      serverUrl = args[0];
    }
    else {
      serverUrl = "http://localhost:8004/glue";
    }

    // This is the service implementation. It behaves differently
    // when inside an Axis container, so we reassure it that it's
    // safe inside GLUE.
    WarehouseServiceImpl w = new WarehouseServiceImpl();
    w.invokedViaAxis = false;
    
    // This is the HTTP server.  We start it running (i.e.
    // start up one or more daemon threads and then push
    // the warehouse-service implementation into it.
    HTTP.startup (serverUrl);
    Registry.publish ("warehouse", w);
  }
}
