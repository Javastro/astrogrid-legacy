package org.astrogrid.warehouse.service;

import electric.registry.Registry;
import electric.server.http.HTTP;

public class Server {
  public static void main (String[] args) throws Exception {
    HTTP.startup ("http://localhost:8004/glue");
    Registry.publish( "warehouse", new WarehouseServiceImpl() );
  }
}
