var     winH, winW, winTop, winLeft;
var afgColour = "#ffff00";
var vfgColour = "#000000";
var abgColour = "#000080";
var vbgColour = "#ffffff";
var pub, glossy;
var GLO = new Array(); 

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

function openUp(idtag, posi, total, paddi){
	var tag, box, guten, match, pod;
/*	alert( "padding area: " + paddi);*/
	tag = idtag + "Tab" + posi;
	box = idtag + "Box" + posi;
	guten = document.getElementById(tag);
	match = document.getElementById(box);
	pod   = document.getElementById(paddi);
/*	if(guten != null){ }*/
	if(match != null){
		if(match.style.display == "none"){
			match.style.display = "";
			guten.className = "agActiveSpan2";
			if(pod != null){
				pod.style.display = "none";
			}
		} else {
			match.style.display = "none";
			guten.className = "agInActiveSpan2";
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
			guten.className = "agInActiveSpan2";
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
  /*
  */
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

function findSelection() {
  var choice=false;
  selector = document.RegistryBrowser.selection;
  if ( selector.length > 1 ) {
    for ( var i=0; i < selector.length; i++ ) {
       if ( selector[i].checked ) {
          choice = true;
          identifier = selector[i].value;
          break;
       }
    }
  } else {
    choice = true;
    identifier = document.RegistryBrowser.selection.value
  }
  if ( !choice ) alert( "Please select a resource" );
  else {
    var url = "/astrogrid-portal/bare/mount/datacenter/variablesFromMB.html?action=getTable&uniqueID=";
    url = url + identifier;
	window.close();
	opener.parent.location.href = url;
	opener.parent.focus();  
  }
}


function getSelectionId(authId, resourceKey) {
  parentDoc = window.opener.document;
  parentId = parentDoc.getElementById(authId);
  parentKey = parentDoc.getElementById(resourceKey);

  var choice=false;
  selector = document.RegistryBrowser.selection;
  if ( selector.length > 1 ) {
    for ( var i=0; i < selector.length; i++ ) {
       if ( selector[i].checked ) {
          choice = true;
          identifier = selector[i].value;
          break;
       }
    }
  } else {
    choice = true;
    identifier = document.RegistryBrowser.selection.value
  } 
  if ( !choice ) alert( "Please select a resource" );
  else {
     j = identifier.indexOf( '/' );
     id = identifier.substring(0, j);
     key = identifier.substring(j+1);

     if ( parentId && parentKey ) 
     {
       parentKey.value = key;
       parentId.value = id;       
     }
     else if (parentId)
     {
       parentId.value = identifier;
     }
     else alert ( "\nauthId = " + id +
            "\nresourceKey = " + key );
     var choice=false;

     window.close();
  }
}

