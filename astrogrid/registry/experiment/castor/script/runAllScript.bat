REM set classpath running in a local computer
set CLASSPATH=%CLASSPATH%;D:\castor\xmlfiles\local\lib\castor-0.9.5.3rc1.jar
set CLASSPATH=%CLASSPATH%;D:\castor\xmlfiles\local\lib\castor-0.9.5.3-xml.jar
set CLASSPATH=%CLASSPATH%;D:\castor\xmlfiles\local\lib\jakarta-regexp-1.1.jar
set CLASSPATH=%CLASSPATH%;D:\castor\xmlfiles\local\lib\xalan.jar
set CLASSPATH=%CLASSPATH%;D:\castor\xmlfiles\local\lib\xercesImpl.jar
set CLASSPATH=%CLASSPATH%;D:\castor\xmlfiles\local\lib\xmlParserAPIs.jar
set CLASSPATH=%CLASSPATH%;D:\castor\xmlfiles\local\lib\log4j-1.2.8.jar
set CLASSPATH=%CLASSPATH%;D:\castor\xmlfiles\local\lib\commons-logging.jar 

REM run SourceGenerator
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\ConeSearch-v0.2.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\SIA-v0.6.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VOCommunity-v0.2.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VODataServiceCoverage.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VODataServiceCoverage-v0.1.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VODataServiceCoverage-v0.2.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VODataService-v0.4.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VORegistry-v0.2.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VOResourceRelType.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VOResourceRelType-v0.2.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VOResource-v0.9.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
java org.exolab.castor.builder.SourceGenerator -i D:\castor\xmlfiles\local\VOTable.xsd -package org.astrogrid.registry.generated.package dest D:\\castor\\xmlfiles\\ -f
