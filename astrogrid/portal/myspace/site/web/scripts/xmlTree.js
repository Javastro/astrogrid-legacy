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

function setParentIVORNAgsl(parent_ivorn, parent_agsl){
  parentDoc = window.opener.document;
  
  parent_ivorn = parentDoc.getElementById(parent_ivorn);
  parent_agsl = parentDoc.getElementById(parent_agsl);
  
  if(parent_ivorn && parent_agsl) {
    myspace_ivorn = document.getElementById('myspace-ivorn');
    parent_ivorn.value = myspace_ivorn.value;
    
    myspace_agsl = document.getElementById('myspace-agsl');
    parent_agsl.value = myspace_agsl.value;
  }
  else {
    alert('No parent IVORN/Agsl to set');
  }
  
  window.close();
}
