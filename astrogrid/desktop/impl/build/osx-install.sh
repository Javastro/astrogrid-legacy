#!/bin/sh -
/usr/bin/ditto -x --rsrc "%{INSTALL_PATH}/%{cpio}" "%{INSTALL_PATH}"
/bin/rm "%{INSTALL_PATH}/%{cpio}"
/bin/mv "%{INSTALL_PATH}/lib/%{jarname}" "%{INSTALL_PATH}/%{appname}.app/Contents/Resources/Java"
/bin/rm "%{INSTALL_PATH}/launch.sh"
/bin/rmdir "%{INSTALL_PATH}/lib"
/bin/rm "%{INSTALL_PATH}/logo.png"
/bin/rm "%{INSTALL_PATH}/osx-install.sh"