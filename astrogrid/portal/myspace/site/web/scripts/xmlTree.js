var openImg = new Image();
openImg.src = "/astrogrid-portal/icons/Folder.png";
var closedImg = new Image();
closedImg.src = "/astrogrid-portal/icons/Open.png";

var oldField = "";
var oldFieldClassName = "";

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
  myspace_clipboard = document.getElementById('myspace-clipboard');
  myspace_clipboard.value = oldMySpaceName;
  
  setHighlight('', '');
}

function setOldMySpaceNameUrl(oldMySpaceName, oldMySpaceUrl){
  setOldMySpaceName(oldMySpaceName);
  myspace_clipboard_url = document.getElementById('myspace-clipboard-url');
  myspace_clipboard_url.value = oldMySpaceUrl;
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
  
  if(parent_ivorn) {
    myspace_ivorn = document.getElementById('myspace-ivorn');
    parent_ivorn.value = myspace_ivorn.value;
  }

  if(parent_agsl) {
    myspace_agsl = document.getElementById('myspace-agsl');
    myspace_item = document.getElementById('myspace-item');
    parent_agsl.value =
        newAgsl(
                myspace_agsl.value,
                myspace_item.value);
  }
  
//  alert('parent ivorn: ' + myspace_ivorn.value);
//  alert('parent agsl:  ' +
//        newAgsl(
//                myspace_agsl.value,
//                myspace_item.value));
}

function setParentHiddenField(field_name, field_value) {
  if(field_name) {
    parentDoc = window.opener.document;
    parent_field = parentDoc.getElementById(field_name);    
    if(field_value && field_value.length > 0) {
      parent_field.value = field_value;
    }
  }
}

function submitParentForm(form_name, action) {
//  alert(form_name);
  parentDoc = window.opener.document;
//  alert(parentDoc);
  parent_form = parentDoc.getElementById(form_name); 
//  alert(parent_form);
  if(parent_form) {
    if(action && action.length > 0) {
      parent_form.action = action;
    }
//    alert("submitting");
    parent_form.submit();
  }
  
  window.close();
}

function setNewIvorn() {
 	myspace_ivorn = document.getElementById('myspace-ivorn');

 	myspace_baseIvorn = document.getElementById('myspace-endpoint');
 	myspace_agsl = document.getElementById('myspace-agsl');
	myspace_item = document.getElementById('myspace-item');
  
  myspace_ivorn.value =
      newIvorn(
               myspace_baseIvorn.value,
               myspace_agsl.value,
               myspace_item.value);
}

function newIvorn(baseIvorn, path, file) {
  var errorMsg = null;
  var result = '';
  
  if(!baseIvorn || baseIvorn.length == 0) {
    if(errorMsg != null) {
      errorMsg = errorMsg + '\n'
    }
    
    errorMsg = 'No base IVORN.';
  }

  if(!path && path.length == 0) {
    if(errorMsg != null) {
      errorMsg = errorMsg + '\n'
    }
    
    errorMsg = errorMsg + 'Invalid path'
  }

  if(!file && file.length == 0) {
    if(errorMsg != null) {
      errorMsg = errorMsg + '\n'
    }
    
    errorMsg = errorMsg + 'No MySpace name supplied';
  }
  
  if(errorMsg != null) {
    alert(errorMsg);
    result = '';
  }
  else {
    result = baseIvorn + '#' + path;
    if(result.charAt(result.length - 1) != '/') {
      result = result + '/';
    }
    result = result + file;
  }
  
//  alert('new IVORN: ' + result);

  return result;
}

function newAgsl(path, file) {
  var errorMsg = null;
  var result = '';

  if(!path && path.length == 0) {
    if(errorMsg != null) {
      errorMsg = errorMsg + '\n'
    }
    
    errorMsg = errorMsg + 'Invalid path'
  }

  if(errorMsg != null) {
    alert(errorMsg);
    result = '';
  }
  else {
    result = path;
    if(result.charAt(result.length - 1) != '/') {
      result = result + '/';
    }
    result = result + file;
  }
  
//  alert('new agsl: ' + result);
  
  myspace_agsl = document.getElementById('myspace-agsl');
  myspace_agsl.value = path;

  myspace_item = document.getElementById('myspace-item');
  myspace_item.value = file;

  return result;
}

function setAgslParts(path, file) {
	myspace_agsl = document.getElementById('myspace-agsl');
	myspace_item = document.getElementById('myspace-item');

  myspace_agsl.value = path;
  myspace_item.value = file;
}

function setHighlight(fieldId, newClass) {
//  alert("setHighlight");
  oldFieldEl = document.getElementById(oldField);
  if(oldFieldEl) {
    oldFieldEl.className = oldFieldClassName;
  }
  
  fieldEl = document.getElementById(fieldId);
  if(fieldEl) {
    oldField = fieldId;
    oldFieldClassName = fieldEl.className;
    fieldEl.className = newClass;
  }
}
