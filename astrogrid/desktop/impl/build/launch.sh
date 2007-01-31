#!/bin/sh -
args=-Djava.library.path="%{INSTALL_PATH}/lib"
cp="%{INSTALL_PATH}/lib/%{jarname}:%{INSTALL_PATH}/lib/jdic_stub_Linux-%{jdicversion}.jar:%{INSTALL_PATH}/lib/jdic_stub_Solaris-%{jdicversion}.jar"
"%{JAVA_HOME}/bin/java" -Xmx512m $args -cp $cp  %{mainclass}$*