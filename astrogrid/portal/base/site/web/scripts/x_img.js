// x_img.js
// X v3.14.1, Cross-Browser DHTML Library from Cross-Browser.com
// Copyright (c) 2002,2003 Michael Foster (mike@cross-browser.com)
// This library is distributed under the terms of the LGPL (gnu.org)

// This file is still experimental.

/** xImgRollSetup
  Can not be called before the window onload event.
  Pass image IDs starting with 4th argument.
  Assumes this image file naming convention:
    out img = path + imgEleId + fileExt
    over img = path + imgEleId + ovrSuffix + fileExt
*/  

function xImgRollSetup(path, ovrSuffix, fileExt) 
{
  for (var i=3; i<arguments.length; ++i) {
    xImgRollBind(arguments[i], path, ovrSuffix, fileExt);
  }
}  

function xImgRollBind(imgEleId, path, ovrSuffix, fileExt)
{
  var ele = xGetElementById(imgEleId);
  if (ele) {
    ele.xOutUrl = path + imgEleId + fileExt;
    ele.xOvrObj = new Image();
    ele.xOvrObj.src = path + imgEleId + ovrSuffix + fileExt;
    xAddEventListener(ele, 'mouseout', xImgRollListener, false);
    xAddEventListener(ele, 'mouseover', xImgRollListener, false);
  }
}

function xImgRollListener(e)
{
  var evt = new xEvent(e);
  var ele = evt.target;
  if (ele && ele.xOvrObj) {
    if (evt.type == 'mouseout') {
      ele.src = ele.xOutUrl;
    }
    else if (ele.xOvrObj.complete) {
      ele.src = ele.xOvrObj.src;
    }
  }
}
