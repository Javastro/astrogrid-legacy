#!/bin/bash

rm -fr build/webapp/web
cp -r site/* build/webapp/
rm -fr build/dest/*
cp -r site/* build/dest/

cp site/WEB-INF/menu/menu-schema.xsd build/dest/
# cp site/web/style/css/main.css build/dest/
# cp site/web/scripts/paneltab.js build/dest/

mkdir build/webapp/WEB-INF/lib
cp -ru $COCOON_HOME/build/webapp/WEB-INF/lib/* build/webapp/WEB-INF/lib

cd bin
jar cvf ../build/webapp/WEB-INF/lib/astrogrid-portal.jar *
cd ..
