// xmenu4_1.js
// xMenu4 Demo 1, Cascading menus from nested ULs!
// Copyright (c) 2002,2003 Michael Foster (mike@cross-browser.com)
// This code is distributed under the terms of the LGPL (gnu.org)

////--- Loader

if (document.getElementById || document.all) {
  document.write("<script type='text/javascript' src='../x.js'></script>");
  document.write("<script type='text/javascript' src='../x_util.js'></script>");
  document.write("<link rel='stylesheet' type='text/css' href='xmenu4_1_dhtml.css' />");
  document.write("<script type='text/javascript' src='xmenu4.js'></script>");
  window.onload = xOnload;
}

////--- Load Event Listener

function xOnload()
{
  var m = new xMenu4(
    'myMenu1',                // id str or ele obj of outermost UL
    true,                     // outer UL position: true=absolute, false=static
    0, 1,                     // box horizontal and vertical offsets
    [-3, -10, -6, -10],       // lbl focus clip array
    [-30, null, null, null],  // box focus clip array
    // css class names:
    'xmBar', 'xmBox',
    'xmBarLbl', 'xmBarLblHvr',
    'xmBarItm', 'xmBarItmHvr',
    'xmBoxLbl', 'xmBoxLblHvr',
    'xmBoxItm', 'xmBoxItmHvr'
  );

  xMnuMgr.add(m);

  xMoveTo(m.ele, xPageX('menuMarker'), xPageY('menuMarker'));
  
  xMnuMgr.load();
  xMnuMgr.paint();
  xAddEventListener(window, 'resize', xmWinOnResize, false);
}

////--- Window Resize Event Listener

function xmWinOnResize()
{
  xMoveTo(xMnuMgr.activeMenu.ele, xPageX('menuMarker'), xPageY('menuMarker'));
  xMnuMgr.paint();
}

