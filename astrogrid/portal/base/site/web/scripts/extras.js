var     winH, winW, winTop, winLeft;
var afgColour = "#ffff00";
var vfgColour = "#000000";
var abgColour = "#000080";
var vbgColour = "#ffffff";
var pub, glossy;
var GLO = new Array(); 
var oldClass, hOldClass;
var priorClass, hPriorClass;
var bar, bar2, iname;
var imageCompress = "/astrogrid-portal/sort_up2.gif"
var imageExpand = "/astrogrid-portal/sort_down.gif"


/*
  window.status = "checking: " + fir.name;
function rotweiller(all_box, boxes) { 
}

  */

function getOzSize(){
	if (parseInt(navigator.appVersion)>3) {
		if (navigator.appName=="Netscape") {
			winW = top.innerWidth;
			winH = top.innerHeight;
			winTop = window.screenY;
			winLeft = window.screenX;
		}
		if (navigator.appName.indexOf("Microsoft")!=-1) {
			winW = document.body.offsetWidth;
			winH = document.body.offsetHeight;
			winTop = window.screenTop;
			winLeft = window.screenLeft;
		}
	}
}

function findPosX(obj)
{
	var curleft = 0;
	if (obj.offsetParent) 
	{
		while (obj.offsetParent)
		{
			curleft += obj.offsetLeft
			obj = obj.offsetParent;
		}
	}
	else if (obj.x)
		curleft += obj.x;
	return curleft;
}

function findPosY(obj)
{
	var curtop = 0;
	if (obj.offsetParent)
	{
		while (obj.offsetParent)
		{
			curtop += obj.offsetTop
			obj = obj.offsetParent;
		}
	}
	else if (obj.y)
		curtop += obj.y;
	return curtop;
}

function showHiddenDiv(theDiv){
	var a = document.getElementById(theDiv);
	getOzSize();
	var boxWidth = winW - 200;
	if(a != null){
		a.style.position="absolute";
		a.style.left= 95 + "px";
		a.style.top= "2px";
		a.style.textAlign= "left";
		a.style.width= boxWidth+"px";
/*		a.style.width= 100+"px";
	alert("showHiddenDev was invoked for " + theDiv + " " + a.style.display + " width: " + boxWidth);
*/
		if(a.style.display == "none"){
			a.style.display = "";
		} else {
			a.style.display = "none";
		}
	} else {
		var msg = theDiv + "  is null";
		alert(msg);
	}
}

function showHelpFrame(theDiv){
	var a = document.getElementById(theDiv);
	var fram = theDiv + "F";
	var f = document.getElementById(fram);
	getOzSize();
	var boxWidth = winW - 200;
	var boxHeight = winH - 20;
	if(f != null){
		f.style.height= boxHeight+"px";
	}
	if(a != null){
		a.style.position="absolute";
		a.style.left= 95 + "px";
		a.style.top= "2px";
		a.style.textAlign= "left";
		a.style.width= boxWidth+"px";
/*		a.style.height= boxHeight+"px";*/
/*		a.style.width= 100+"px";
	alert("showHiddenDev was invoked for " + theDiv + " " + a.style.display + " width: " + boxWidth);
*/
		if(a.style.display == "none"){
			a.style.display = "";
		} else {
			a.style.display = "none";
		}
	} else {
		var msg = theDiv + "  is null";
		alert(msg);
	}
}

function showHiddenDiv(theDiv){
	var a = document.getElementById(theDiv);
	getOzSize();
	var boxWidth = winW - 200;
	if(a != null){
		a.style.position="absolute";
		a.style.left= 95 + "px";
		a.style.top= "2px";
		a.style.textAlign= "left";
		a.style.width= boxWidth+"px";
/*		a.style.width= 100+"px";
	alert("showHiddenDev was invoked for " + theDiv + " " + a.style.display + " width: " + boxWidth);
*/
		if(a.style.display == "none"){
			a.style.display = "";
		} else {
			a.style.display = "none";
		}
	} else {
		var msg = theDiv + "  is null";
		alert(msg);
	}
}


function hinter(elem){
	var idd = elem.id + "stick";
	var qposx = findPosX(elem);
	var qposy = findPosY(elem);
	getOzSize();
	var boxWidth = Math.round(winW * 0.6);
/*	var boxWidth = 600;*/
	var boxPos;
	if((qposx + 40 + boxWidth) < winW){
		boxPos = qposx+40;
	} else {
		boxPos = qposx-20 - boxWidth;
	}
	if(document.getElementById(idd)!= null) {
		var a = document.getElementById(idd);
		a.style.position="absolute";
		a.style.left= boxPos + "px";
		a.style.top= qposy+"px";
		a.style.textAlign= "left";
		a.style.width= boxWidth+"px";

		if(a.style.display == "none"){
			a.style.display = "";
		} else {
			a.style.display = "none";
		}
	}
}

/* ------------------------------------------------*/

function nofooter(){
	if(document.getElementById("imgfootbar")!= null) {
		var fussimg = document.getElementById("imgfootbar");
		fussimg.style.display = "none";
	}
}

function footer(){
	var fuss = document.getElementById("imgfoot");
	var qposx = findPosX(fuss);
	var qposy = findPosY(fuss);
	getOzSize();
	var gypos = winH-20;
	if(document.getElementById("imgfootbar")!= null) {
		var fussimg = document.getElementById("imgfootbar");
		if(qposy < gypos){
			fussimg.style.left = "0px";
			fussimg.style.top = gypos + "px";
			fussimg.style.display = "";
			fussimg.style.position="absolute";
		} else {
			fussimg.style.display = "";
		}
	}
}

function expandTextArea(areaName, height){
	var area = document.getElementById(areaName);
	if(area != null){
		area.rows = height;
	}
	nofooter();
}

function clearTextArea(areaName){
	var area = document.getElementById(areaName);
	if(area != null){
		area.value = "";
	}
}

function TEK(i){
var update, old;
old = document.main.adqlQuery.value;
update = old + i ;
document.main.adqlQuery.value = update;
document.main.adqlQuery.focus();
}

function xTEK(i){
var update, old;
old = parent.deploy.document.main.adqlQuery.value;
update = old + i ;
parent.deploy.document.main.adqlQuery.value = update;
parent.deploy.document.main.adqlQuery.focus();
}

function cabc(linkObj){
	linkObj.style.background = abgColour;
	linkObj.style.color = afgColour;
} 

function cvbc(linkObj){
	linkObj.style.background = vbgColour;
	linkObj.style.color = vfgColour;
}       

function openUp(idtag, posi, total, paddi, aclass, iclass){
	var tag, box, guten, match, pod, class_a, class_i;
/*	alert( "padding area: " + paddi);*/
	tag = idtag + "Tab" + posi;
	box = idtag + "Box" + posi;
	if(aclass!= null){ class_a = aclass;
	} else { class_a = "agActiveSpan2"; }
	if(iclass!= null){ class_i = iclass;
	} else { class_i = "agInActiveSpan2"; }
	guten = document.getElementById(tag);
	match = document.getElementById(box);
	pod   = document.getElementById(paddi);
/*	if(guten != null){ }*/
	if(match != null){
		if(match.style.display == "none"){
			match.style.display = "";
			guten.className = class_a;
			if(pod != null){
				pod.style.display = "none";
			}
		} else {
			match.style.display = "none";
			guten.className = class_i;
			if(pod != null){
				pod.style.display = "";
			}
		}
	}
	for(var i=0;i<total;i++){
		if(i == posi) continue;
		tag = idtag + "Tab" + i;
		box = idtag + "Box" + i;
		guten = document.getElementById(tag);
		match = document.getElementById(box);
		if(guten != null){
			guten.className = class_i;
		}
		if(match != null){
			match.style.display = "none";
		}
	}
/*	posi += 1;*/
}

function closeAllTabs(idtag, total){
	var tag, box, guten, match;
	for(var i=0;i<total;i++){
		tag = idtag + "Tab" + i;
		box = idtag + "Box" + i;
		guten = document.getElementById(tag);
		match = document.getElementById(box);
		if(guten != null){
			guten.className = "agInActiveSpan2";
		}
		if(match != null){
			match.style.display = "none";
		}
	}
}

function ButtonHelpy(text){
	if(text != ""){
		var scar = document.getElementById('scratchArea');
		var refim = document.getElementById('guideImage');
		var qposy = findPosY(refim);
/*		window.status = text;*/
		scar.style.position="absolute";
		scar.style.left= "20px";
		scar.style.top= qposy+"px";
		scar.firstChild.nodeValue = text;
/*		scar.style.width= "300px";*/

		if(scar.style.display == "none"){
			scar.style.display = "";
		} else {
			scar.style.display = "none";
		}
	}
}

function moreOrLess(){
var more = document.getElementById('moreButtons');
if(more != null){
	var forMore = document.getElementById('askmore').firstChild;
	if(more.style.display == "none"){
		more.style.display = "";
		forMore.nodeValue = "less...";
	} else {
		more.style.display = "none";
		forMore.nodeValue = "more...";
	}
	nofooter();
}
}

function rotweiller(all_box, boxes){
  var form = all_box.form ;
  var n = form.elements.length;
  var o = form.elements ;
  var nom = all_box.name ;
  var stat = all_box.checked ;
/*	alert("dog found and named " + nom + ", it has " + n + " puppies");*/
  for (var i=0; i<n; i++) { 
    if (o[i].type == 'checkbox' && o[i].name == nom) {
      o[i].checked = stat? stat : o[i].defaultChecked; 
    }
  }
  if (boxes) {
    for (var i=0; i<n; i++) {
      if (o[i].type == 'checkbox' && o[i].name == boxes){
	o[i].checked = stat? stat : o[i].defaultChecked; 
      }
    }
  }
}

function makeADQL(){
	var f = document.assisted;
	var e = f.elements;
	var n = e.length;
	var condi = "";
	var select = "";
	var i;
	var nom, vnom;
	var zel = /zelect_/;
/*	window.status = "This is found: " + diese.id + " and " + n;*/
	if(f.selectAll.checked){
		select = " * ";
	} else {
		for(i=0;i<n;i++){
			if (e[i].type == 'checkbox' && e[i].name == "show" &&
					e[i].checked){
				if(select == ""){
					select = "T1." + e[i].value;
				} else {
					select += " " + "T1." + e[i].value;
				}
			}
		}
	}
	if(select == ""){ // No selection has been made. Unacceptable!
		alert("Please select some columns to view");
		return;
	}
	for(i=0;i<n;i++){
		nom = e[i].name;
		if(zel.test(nom)){
			if(e[i].value != ""){ // found a condition
				vnom = nom.replace(/zelect_/, "");
				if(condi == ""){
					condi = "T1." + vnom + " " + e[i].value;
				} else {
					condi += " AND " +  "T1." + vnom + " " + e[i].value;
				}
			}
		}
	}
/*	window.status = "SELECT " + select + " WHERE " + condi;*/
	document.main.adqlQuery.value = "FROM " + currentTable +
		" as T1 SELECT " + select;

	if(condi != ""){
		document.main.adqlQuery.value += " WHERE " + condi;
	}
	openLeftTab();
	
}


function popupBrowser(url, uwidth, uheight){
	var twidth = uwidth;
	var theight = uheight;
	getOzSize();
	var	pubX = winLeft + 20;
	var	pubY = winTop + 20;
/*	window.status = "Top,left = " + winTop + ", " + winLeft;*/
	if(uwidth == null){ twidth = winW - 40; }
	if(uheight == null){ theight = winH; }
	var options = "toolbar=no, directories=no, location=no, status=no, ";
	options += "menubar=no, left=" + pubX + ", top=" + pubY;
	options += ", resizable=yes, scrollbars=yes, width=";
	options += twidth + ", height=" + theight;
	pub = window.open(url, "AG_PUB", options);
	pub.focus();
}

function closeDynamic(moi){
	var body = document.getElementById(moi);
	body.style.display = "none";
}

function openDynamic(moi){
	var body = document.getElementById(moi);
	body.style.display = "";
	/*
	if(body != null){
		if(body.style.display == "none"){
			body.style.display = "";
		} else {
			body.style.display = "none";
		}
	}
	*/
}

function closeGlossary(){
	glossy.close();
}

function popupGlossary(moi, url, uwidth, uheight){
	var twidth = uwidth;
	var theight = uheight;
/*	URL = "http://yahoo.co.uk";*/
	var URL = "/astrogrid-portal/bare/mount/help/glossary.glo?item=" + url;
	if(GLO[moi] == null){
		getOzSize();
		var	pubX = winLeft + 20;
		var	pubY = winTop + 20;
/*		window.status = "Top,left = " + winTop + ", " + winLeft;*/
		if(uwidth == null){ twidth = winW - 40; }
		if(uheight == null){ theight = 200; }
	var options = "toolbar=no, directories=no, location=no, status=no, ";
		options += "menubar=no, left=" + pubX + ", top=" + pubY;
		options += ", resizable=yes, scrollbars=yes, width=";
		options += twidth + ", height=" + theight;
		glossy = window.open(URL, "glossary", options);
		glossy.focus();
		GLO[moi] = 1;
	} else {
		GLO[moi] = null;
		glossy.close();
	}
}

function hideOrShow(base){
	var bodyID = base.id + "BODY";
	var tableID = base.id + "T";
	var body = document.getElementById(bodyID);
	var table = document.getElementById(tableID);
	if(body != null){
		if(body.style.display == "none"){
			if(table != null){
				table.style.backgroundColor="#ffffcc";
			}
			body.style.display = "";
			base.innerHTML = "[hide]";
		} else {
			if(table != null){
				table.style.backgroundColor="#ffffff";
			}
			body.style.display = "none";
			base.innerHTML = "[show]";
		}
	}
}

function backToParentDC(ref){
	var url = "/astrogrid-portal/bare/mount/datacenter/xscreen.xsp?tableID="
		+ ref;
/*	alert("Opener name: " +  opener.name);*/
	window.close();
	opener.parent.location.href = url;
	opener.parent.focus();
}

function backToRightFrameDC(ref){
	var url = "/astrogrid-portal/bare/mount/datacenter/variables.xsp?tableID="
		+ ref;
/*	alert("URL: " +  url);*/
	window.close();
	opener.parent.frames[2].location.href = url;
	opener.parent.focus();
}

function focusit(a){
/*	a.style.background = "#aaffaa";*/
	a.style.background = "#ffff00";
}

function defocusit(a){
	a.style.background = "#ffffff";
}

/* ----------- functions needed for menus only --------------- */


function swapImages(destiny, image1, image2, width, height){
	var theImage = destiny.id + "IMG";
	currentImage = document.getElementById(theImage);
	window.status = "images: "+ image1 + " " + image2;
	if(image2 == null){ return; }
	if(currentImage.src == image1){
		currentImage.src = image2;
	} else {
		currentImage.src = image1;
	}
}

function OnMouseComplex(destiny, classy, menuID, nitems){
	var thisName = destiny.id;
	oldClass = destiny.className;
	destiny.className = classy;
}

/*
function OnMooseClick(destiny, classy, menuID, nitems){
	var thisName = destiny.id + "bar";
	bar2 = document.getElementById(thisName);
	if(bar2.style.display == "none"){
		bar2.style.display = "";
	} else {
		bar2.style.display = "none";
	}
}
*/

function OnMiceClick(destiny, classy, menuID, nitems){
	var thisName = destiny.id + "bar";
	bar2 = document.getElementById(thisName);
	var e=document.all? document.all : document.getElementsByTagName("*");
	for(i=0;i<e.length;i++){
		if(e[i].id == thisName){
	window.status = "I got this SOB! " + thisName +"  "+ e[i].summary;
		}
	}
	if(bar2.style.display == "none"){
		bar2.style.display = "";
	} else {
		bar2.style.display = "none";
	}
}

function OnMouseVComplex(destiny, classy, menuID, nitems){
	var thisName = destiny.id;
	oldClass = destiny.className;
	destiny.className = classy;
	thisName += "bar";
}


function OnMousyClick(destiny, classy, menuID, nitems, voff, hoff, ori, level, href){
	getOzSize();
	var thisName = destiny.id;
	var thisImage = thisName + "I";
	var newY, newX, margin;
	var thisDiv = thisName + "DIV";
	var left = destiny.left;
	var imref = thisName + "imRef";
	thisName += "bar";
	var xposi = findPosX(destiny);
	var yposi = findPosY(destiny);
	var parentYposition =  yposi;
	margin = winW - hoff -20;
//window.status = "this name is: "+thisName + "  " + thisDiv + " "+thisImage;
//window.status = "left point: "+ xposi + " top: " + yposi+ " Voff: "+voff + " hoff: " + hoff + " Orientation: " + ori;
//window.status = "width: " + winW + " Height: " + winH;
	if(level == "top"){
	var e=document.all? document.all : document.getElementsByTagName("*");
		for(i=0;i<e.length;i++){
			if(e[i].className == menuID && e[i].id != thisName){
				if(e[i].style.display != "none"){
					e[i].style.display = "none";
				}
			}
		}
	}
	if(document.getElementById(thisName)!= null) {
		bar2 = document.getElementById(thisName);
		ima = document.getElementById(thisImage);
		if(bar2.style.display == "none"){
			destiny.title = "close the extra options";
			if(ori == 'horizontal'){
				bar = document.getElementById(imref);
				var yposi = findPosY(bar);
window.status += " " + parentYposition + " vs " + yposi;
				if(yposi < parentYposition){
window.status += " imref: "+ imref + " barra: " + bar.id + " ypos: "+yposi;
					newY = yposi+voff;
				} else {
window.status += " reference is different: " + imref;
					newY = yposi;
				}
				if(href == 1){
					bar2.style.left= xposi +"px";
window.status += " xpos is modified: " + xposi;
				}
				bar2.style.top= newY+"px";
			} else {
/*window.status = "Vertical bar"*/
				newX = xposi+hoff*0.9;
				while(newX > margin){
					newX -= 10;
				}
				bar2.style.left= newX+"px";
				bar2.style.top= yposi+"px";
			}
			bar2.style.position="absolute";
			bar2.style.display = "";
			ima.src = imageCompress;
		} else {
			bar2.style.display = "none";
			destiny.title = "Click HERE for more options";
			ima.src = imageExpand;
		}
	} else {
window.status = "Failed to identify " + thisName;
	}
}

function OutMouseComplex(destiny){
	var thisName = destiny.id;
	destiny.className = oldClass;
}

function OutMouseVComplex(destiny){
	var thisName = destiny.id;
	destiny.className = oldClass;
}

/*
function OnMouseSingle(destiny, classy){
	var thisName = destiny.id;
	thisName = thisName + "H";
	oldClass = destiny.className;
	destiny.className = classy;
	if(document.getElementById(thisName)!= null) {
		bar2 = document.getElementById(thisName);
		hOldClass = bar2.className;
		bar2.className = classy;
	}
}
*/

function OutMouseSingle(destiny){
	var thisName = destiny.id;
	thisName = thisName + "H";
	destiny.className = oldClass;
	if(document.getElementById(thisName)!= null) {
		bar2 = document.getElementById(thisName);
		bar2.className = hOldClass;
	}
}

