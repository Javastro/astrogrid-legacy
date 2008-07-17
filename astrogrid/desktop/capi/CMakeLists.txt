cmake_minimum_required(VERSION 2.6)
project (ACRCAPI)
add_subdirectory(idl)


include_directories ( core xmlrpc )

add_library(acr-capi SHARED intf.cpp intfclasses.cpp core/AR.cpp		core/JavaProperties.cpp	core/URL.cpp		core/arcontainers.cpp	core/plastic.cpp xmlrpc/XmlRpcClient.cpp			xmlrpc/XmlRpcServer.cpp			xmlrpc/XmlRpcServerMethod.cpp		xmlrpc/XmlRpcSource.cpp			xmlrpc/XmlRpcValue.cpp
xmlrpc/XmlRpcDispatch.cpp		xmlrpc/XmlRpcServerConnection.cpp	xmlrpc/XmlRpcSocket.cpp			xmlrpc/XmlRpcUtil.cpp
)
add_executable(acr-capi-example acr-capi-example.c testintf.c )
target_link_libraries(acr-capi-example acr-capi)