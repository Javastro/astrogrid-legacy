// x_util.js
// X v3.14.1, Cross-Browser DHTML Library from Cross-Browser.com
// Copyright (c) 2002,2003 Michael Foster (mike@cross-browser.com)
// This library is distributed under the terms of the LGPL (gnu.org)

/* xGetElementsByClassName()
   Returns an array of elements which are
   descendants of parentEle and have tagName and clsName.
   If parentEle is null or not present, document will be used.
   if tagName is null or not present, "*" will be used.
*/
function xGetElementsByClassName(clsName, parentEle, tagName) {
	var elements = null;
	var found = new Array();
	var re = new RegExp('\\b'+clsName+'\\b');
	if (!parentEle) parentEle = document;
	if (!tagName) tagName = '*';
	if (parentEle.getElementsByTagName) {elements = parentEle.getElementsByTagName(tagName);}
	else if (document.all) {elements = document.all.tags(tagName);}
	if (elements) {
		for (var i = 0; i < elements.length; ++i) {
			if (elements[i].className.search(re) != -1) {
				found[found.length] = elements[i];
			}
		}
	}
	return found;
}

function xHasPoint(ele, iLeft, iTop, iClpT, iClpR, iClpB, iClpL) {
  if (!xNum(iClpT)){iClpT=iClpR=iClpB=iClpL=0;}
  else if (!xNum(iClpR)){iClpR=iClpB=iClpL=iClpT;}
  else if (!xNum(iClpB)){iClpL=iClpR; iClpB=iClpT;}
  var thisX = xPageX(ele), thisY = xPageY(ele);
  return (iLeft >= thisX + iClpL && iLeft <= thisX + xWidth(ele) - iClpR &&
          iTop >=thisY + iClpT && iTop <= thisY + xHeight(ele) - iClpB );
}

// end x_util.js