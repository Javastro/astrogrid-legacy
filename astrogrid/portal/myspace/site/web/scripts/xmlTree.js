var openImg = new Image();
openImg.src = "/astrogrid-portal/icons/Folder.png";
var closedImg = new Image();
closedImg.src = "/astrogrid-portal/icons/Open.png";

var oldField = "";
var oldFieldClassName = "";

function showBranch( branch, branchPath ){
  var element = document.getElementById(branch) ;
  var objBranch = element.style ;
  status = element.getAttribute("status") ;
  // alert( status) ;
  if(objBranch.display=="block") {
     objBranch.display="none";
     swapFolder('I' + branch); 
     branchCollection.remove( branchPath ) ;    
  } 
  else {
     objBranch.display="block";
     swapFolder('I' + branch);
     branchCollection.add( branchPath ) ;
     if( status == "NOT_LOADED" ) {
        refreshTreeView( branchPath ) ;
     }
  }

}


function swapFolder(img){
  var objImg = document.getElementById(img);
  if(objImg.src.indexOf('Open.png')>-1)
     objImg.src = openImg.src;
  else
     objImg.src = closedImg.src;
}

function setOldMySpaceName(oldMySpaceName){
  //alert( "setOldMySpaceName" ) ;
  //var myspace_old_name = document.getElementById('myspace-src');
  //myspace_old_name.value = oldMySpaceName;
  //var myspace_clipboard = document.getElementById('myspace-clipboard');
  //myspace_clipboard.value = oldMySpaceName;
  
  setHighlight('', '');
}

function setOldMySpaceNameUrl(oldMySpaceName, oldMySpaceUrl){
  setOldMySpaceName(oldMySpaceName);
  var myspace_clipboard_url = document.getElementById('myspace-clipboard-url');
  myspace_clipboard_url.value = oldMySpaceUrl;
}

function setNewMySpaceName(newMySpaceName){
  //alert( "setNewMySpaceName" ) ;
  //var myspace_new_name = document.getElementById('myspace-dest');
  //myspace_new_name.value = newMySpaceName;
}

function setIVORNAgsl(newIvorn, newAgsl){
  var myspace_ivorn = document.getElementById('myspace-ivorn');
  myspace_ivorn.value = newIvorn;

  var myspace_agsl = document.getElementById('myspace-agsl');
  myspace_agsl.value = newAgsl;
}

function createIVORN() {
  var result = '';
  
  var myspace_ivorn = document.getElementById('myspace-ivorn');
  var myspace_item = document.getElementById('myspace-item');

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
  var result = '';
  
  var myspace_agsl = document.getElementById('myspace-agsl');
  var myspace_item = document.getElementById('myspace-item');

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
  var parentDoc = window.opener.document;
  
  var parent_ivorn = parentDoc.getElementById(parent_ivorn);
  var parent_agsl = parentDoc.getElementById(parent_agsl);
  
  if(parent_ivorn) {
    var myspace_ivorn = document.getElementById('myspace-ivorn');
    parent_ivorn.value = myspace_ivorn.value;
  }

  if(parent_agsl) {
    var myspace_agsl = document.getElementById('myspace-agsl');
    var myspace_item = document.getElementById('myspace-item');
    parent_agsl.value = newAgsl( myspace_agsl.value, myspace_item.value);
  }
  
//  alert('parent ivorn: ' + myspace_ivorn.value);
//  alert('parent agsl:  ' +
//        newAgsl(
//                myspace_agsl.value,
//                myspace_item.value));
}

function setParentHiddenField(field_name, field_value) {
  if(field_name) {
    var parentDoc = window.opener.document;
    var parent_field = parentDoc.getElementById(field_name);    
    if(field_value && field_value.length > 0) {
      parent_field.value = field_value;
    }
  }
}

function submitParentForm(form_name, action) {
//  alert(form_name);
  var parentDoc = window.opener.document;
//  alert(parentDoc);
  var parent_form = parentDoc.getElementById(form_name); 
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
 	var myspace_ivorn = document.getElementById('myspace-ivorn');
 	var myspace_baseIvorn = document.getElementById('myspace-endpoint');
 	var myspace_agsl = document.getElementById('myspace-agsl');
	var myspace_item = document.getElementById('myspace-item');
  
  myspace_ivorn.value =
      newIvorn(
               myspace_baseIvorn.value,
               myspace_agsl.value,
               myspace_item.value);

  return myspace_ivorn.value; 
}

function newIvorn(baseIvorn, path, file) {
  var errorMsg = '';
  var result = '';
  
  if(!baseIvorn || baseIvorn.length == 0) {
    errorMsg = 'No base IVORN.';
  }

  if(!path || path.length == 0 ) {
    if(errorMsg.length != 0 ) {
      errorMsg = errorMsg + '\n';
    }
    errorMsg = errorMsg + 'Location missing. Please select a location.';
  }
  
  if( file && file.length > 0 ) {
     // replace leading and trailing spaces...
     file = (file.replace(/^\s+/,'')).replace(/\s+$/,'');
  }

  // name must exist...
  if(!file || file.length == 0 ) {
    if(errorMsg.length != 0 ) {
      errorMsg = errorMsg + '\n';
    }
    errorMsg = errorMsg + 'Name missing. Please enter or select a name.';
  }
  // name must not contain white space or special characters...
  else if( file.match(/[\s\/@><]/) ) {
    if(errorMsg.length != 0 ) {
      errorMsg = errorMsg + '\n';
    }
    errorMsg = errorMsg + 'Invalid name. A name cannot contain blanks or special characters';
  }
  
  if(errorMsg.length != 0) {
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

  if( file && file.length > 0 ) {
     // replace leading and trailing spaces...
     file= (file.replace(/^\s+/,'')).replace(/\s+$/,'');
  }

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
  
  var myspace_agsl = document.getElementById('myspace-agsl');
  myspace_agsl.value = path;

  var myspace_item = document.getElementById('myspace-item');
  myspace_item.value = file;

  return result;
}

function setAgslParts(path, file) {
	var myspace_agsl = document.getElementById('myspace-agsl');
	var myspace_item = document.getElementById('myspace-item');

  myspace_agsl.value = path;
  myspace_item.value = file;
}

function setHighlight(fieldId, newClass) {
  //  alert( "setHighlight: " + "\nfieldId: "+ fieldId + "\nnewClass: "+ newClass );
  var oldFieldEl = document.getElementById(oldField);
  if(oldFieldEl) {
    oldFieldEl.className = oldFieldClassName;
  }
  
  var fieldEl = document.getElementById(fieldId);
  if(fieldEl) {
    oldField = fieldId;
    oldFieldClassName = fieldEl.className;
    fieldEl.className = newClass;
  }
}

function callParentFunction(parent_func) {
  var parent = window.opener;
  
  if(parent && parent_func && parent_func.length > 0) {
    var parent_func_expr = "parent." + parent_func;
    eval(parent_func_expr);
  } 
}


function processMicroBrowserOK( ivorn, agsl, fieldName, fieldValue, formName, formAction, parentFunction ) {

  var result = setNewIvorn();	
  
  // alert( "Jeff debugging. Please ignore. ...\n ivorn = [" + ivorn +"]\n agsl = [" + agsl + "]\n fieldName = [" + fieldName + "]\n fieldValue = [" + fieldValue + "]\n formName = [" + formName + "]\n formAction = [" + formAction + "]\n parentFunction = [" + parentFunction + "]\n result = [" + result + "]" ) ;
 
  if( result != '' ) {
     setParentIVORNAgsl(ivorn, agsl );
     setParentHiddenField(fieldName, fieldValue);
     submitParentForm(formName, formAction);
     callParentFunction(parentFunction);
  }
  
}


function processMicroBrowserRefresh( agslDestid, funcName ) {
  
  if(funcName) {
     window.opener.Browser("/astrogrid-portal/lean/mount/myspace/myspace-micro?myspace-refresh-cache=yes&parent_func=" + funcName + "()&agsl=" + agslDestid );
  }
  else {
     window.opener.popupBrowser("/astrogrid-portal/lean/mount/myspace/myspace-micro?myspace-refresh-cache=yes&agsl=" + agslDestid);
  }
  
}




// A crude collection object used to store open branches
// so that we can restore the tree view to what the user expects...
function Branches() {
   this.collection = new Array(16) ;
   this.initialized = false ;
}
new Branches() ;

//***
function Branches_init() {
   if( this.initialized == true )
      return ;
   var stringInit = document.getElementById( 'myspace-tree-open-branches' ).value ;
   if( stringInit != null && stringInit.length > 0 )
      this.collection = stringInit.split( "*" ) ;   
   //alert( "Branches.init(): " + this.getContents() ) ;
   this.initialized = true ;
}
Branches.prototype.init = Branches_init ;

//***
function Branches_groupAdd( stringGroup ) {
   if( stringGroup == null )
      return ; 
   this.collection = this.collection.concat( stringGroup.split("*") ) ;
}
Branches.prototype.groupAdd = Branches_groupAdd ;

//***
function Branches_add( branch ) {
   // alert ( "Branches_add" ) ;
   this.init();
   var set = false ;
   for( var i=0; i < this.collection.length ; i++ ) {
      if( this.collection[i] == branch ) {
         set = true ;
         break ;
      }
      else if( this.collection[i] == null ) {
         set = true ;
         this.collection[i] = branch ;
         break ;
      }
   }// endfor
   
   if( set == false )
      this.collection[ this.collection.length ] = branch ;
   
} // end Branches_add
Branches.prototype.add = Branches_add ;

//***
function Branches_remove( branch ) {
   // alert ( "Branches_remove" ) ;
   this.init() ;
   for( var i=0; i < this.collection.length ; i++ ) {
      if( this.collection[i] == branch ) {
         this.collection[i] = null ;
         break ;
      }
   }// endfor
   
} // end Branches_remove
Branches.prototype.remove = Branches_remove ;

//***
function Branches_getContents() {
    // alert( "Branches_getContents" ) ;
    var stringContents = "" ;
	for( var i=0; i < this.collection.length ; i++ ) {
	    if( this.collection[i] != null && this.collection[i].length > 0 ) {
	       stringContents +=  this.collection[i] + '*' ;
	    }
	}
    return stringContents.substring( 0, stringContents.length-1 ) ;
}
Branches.prototype.getContents = Branches_getContents ;

// Create a branches collection for this document scope...
// var branchCollection = new Branches() ;
