@echo off 
start javaw -Xmx512m -Djava.library.path="$INSTALL_PATH\lib" -cp "$INSTALL_PATH\lib\$jarname;$INSTALL_PATH\lib\jdic_stub_Windows-$jdicversion.jar" $mainclass %1 %2 %3 %4 %5

