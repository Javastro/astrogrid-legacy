var openImg = new Image();
openImg.src = "/astrogrid-portal/icons/Folder.png";
var closedImg = new Image();
closedImg.src = "/astrogrid-portal/icons/Open.png";

function showBranch(branch){
  var objBranch = 
     document.getElementById(branch).style;
  if(objBranch.display=="block")
     objBranch.display="none";
  else
     objBranch.display="block";
  swapFolder('I' + branch);
}

function swapFolder(img){
  objImg = document.getElementById(img);
  if(objImg.src.indexOf('Open.png')>-1)
     objImg.src = openImg.src;
  else
     objImg.src = closedImg.src;
}

function setOldMySpaceName(oldMySpaceName){
  myspace_old_name = document.getElementById('myspace-src');
  myspace_old_name.value = oldMySpaceName;
}

function setNewMySpaceName(newMySpaceName){
  myspace_new_name = document.getElementById('myspace-dest');
  myspace_new_name.value = newMySpaceName;
}

function setIVORNAgsl(newIvorn, newAgsl){
  myspace_ivorn = document.getElementById('myspace-ivorn');
  myspace_ivorn.value = newIvorn;

  myspace_agsl = document.getElementById('myspace-agsl');
  myspace_agsl.value = newAgsl;
}

function createIVORN() {
  result = '';
  
	myspace_ivorn = document.getElementById('myspace-ivorn');
	myspace_item = document.getElementById('myspace-item');

  // Do we have an IVORN set?
	if(myspace_ivorn && myspace_ivorn.value && myspace_ivorn.value.length > 0) {
	  result = myspace_ivorn.value;
	}
	// Do we have an item name set?
	else if(myspace_item && myspace_item.value && myspace_item.value.length > 0) {
	  myspace_endpoint = document.getElementById('myspace-endpoint');
  	myspace_agsl = document.getElementById('myspace-agsl');
	  result = myspace_endpoint.value + '#' + myspace_agsl.value;
	  if(myspace_agsl.value.charAt(myspace_agsl.value.length -1) != "/") {
	    result = result + "/";
	  }
	  result = result + myspace_item.value;
	}

//  alert('MySpace IVORN: ' + result);

  return result;
}

function createAGSL() {
  result = '';
  
	myspace_agsl = document.getElementById('myspace-agsl');
	myspace_item = document.getElementById('myspace-item');

  // Do we have an AGSL set?
	// Do we have an item name set?
	if(myspace_item && myspace_item.value && myspace_item.value.length > 0) {
  	myspace_agsl = document.getElementById('myspace-agsl');
	  result = myspace_agsl.value;
	  if(myspace_agsl.value.charAt(myspace_agsl.value.length -1) != "/") {
	    result = result + "/";
	  }
	  result = result + myspace_item.value;
	}
	else if(myspace_agsl && myspace_agsl.value && myspace_agsl.value.length > 0) {
	  result = myspace_agsl.value;
	}

//  alert('MySpace AGSL: ' + result);

  return result;
}

function setParentIVORNAgsl(parent_ivorn, parent_agsl){
  parentDoc = window.opener.document;
  
  parent_ivorn = parentDoc.getElementById(parent_ivorn);
  parent_agsl = parentDoc.getElementById(parent_agsl);
  
  // TODO: comment out
//  createIVORN();
//  createAGSL();
  // TODO: comment out
  
  if(parent_ivorn) {
    myspace_ivorn = createIVORN();
    parent_ivorn.value = myspace_ivorn.value;
  }

  if(parent_agsl) {
    myspace_agsl = createAGSL();
    parent_agsl.value = myspace_agsl.value;
  }
}

function submitParentForm(form_name, action) {
  parentDoc = window.opener.document;
  parent_form = parentDoc.getElementById(form_name);
  if(parent_form) {
    if(action && action.length() > 0) {
      parent_form.action = action;
    }
    parent_form.submit();
  }
  
  window.close();
}
