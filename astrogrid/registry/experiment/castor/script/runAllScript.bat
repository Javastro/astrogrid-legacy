REM set classpath running in a local computer
set CLASSPATH=%CLASSPATH%;D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\lib\castor-0.9.5.3rc1.jar
set CLASSPATH=%CLASSPATH%;D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\lib\castor-0.9.5.3-xml.jar
set CLASSPATH=%CLASSPATH%;D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\lib\jakarta-regexp-1.1.jar
set CLASSPATH=%CLASSPATH%;D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\lib\xalan.jar
set CLASSPATH=%CLASSPATH%;D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\lib\xercesImpl.jar
set CLASSPATH=%CLASSPATH%;D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\lib\xmlParserAPIs.jar
set CLASSPATH=%CLASSPATH%;D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\lib\log4j-1.2.8.jar
set CLASSPATH=%CLASSPATH%;D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\lib\commons-logging.jar 

REM run SourceGenerator
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\ConeSearch-v0.2.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\SIA-v0.6.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VOCommunity-v0.2.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VODataServiceCoverage.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VODataServiceCoverage-v0.2.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VODataService-v0.4.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VORegistry-v0.2.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VOResourceRelType.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VOResourceRelType-v0.2.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VOResource-v0.9.xsd -package org.astrogrid.registry
java org.exolab.castor.builder.SourceGenerator -i D:\eclipse\workspace\astrogrid\astrogrid\registry\experiment\castor\schema\VOTable.xsd -package org.astrogrid.registry
