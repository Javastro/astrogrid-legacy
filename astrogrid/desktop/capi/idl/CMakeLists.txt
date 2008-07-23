set (IDL_INC_DIR "/" CACHE PATH "IDL include directory")
set (IDL_LIB_DIR "/" CACHE PATH "IDL library directory")

include_directories(${ACRCAPI_SOURCE_DIR}/core ${ACRCAPI_SOURCE_DIR}/xmlrpc ${IDL_INC_DIR} ${ACRCAPI_SOURCE_DIR})
link_directories(${IDL_LIB_DIR})
#IF(APPLE)
#SET (CMAKE_SHARED_LIBRARY_CREATE_CXX_FLAGS "-bundle -flat_namespace -undefined suppress" )
#SET(CMAKE_CXX_CREATE_SHARED_LIBRARY
#      "<CMAKE_CXX_COMPILER> <CMAKE_SHARED_LIBRARY_CXX_FLAGS> <LANGUAGE_COMPILE_FLAGS> <LINK_FLAGS> 
#<CMAKE_SHARED_LIBRARY_CREATE_CXX_FLAGS> -o <TARGET> <OBJECTS> <LINK_LIBRARIES>")

#ENDIF(APPLE)

set (CMAKE_BACKWARDS_COMPATIBILITY 2.2)
add_library(acr_idl SHARED idl_acr.cpp idlhelper.cpp IDLStruct.cpp)
set_target_properties(acr_idl PROPERTIES PREFIX "" SUFFIX ".so" )
add_library(dlm_test MODULE dlm_test.cpp idl_acr.cpp idlhelper.cpp IDLStruct.cpp)
set_target_properties(dlm_test PROPERTIES PREFIX "" SUFFIX ".so")

target_link_libraries(acr_idl acr-capi idl)
target_link_libraries(dlm_test acr-capi idl)

#add_executable (idl_test main.cpp)
#target_link_libraries(idl_test acr_idl acr-capi idl)
