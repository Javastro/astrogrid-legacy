function myspace_extension_days() {
  ret = prompt('Number of days to extend by',"1");
  if(ret && ret.length > 0) {
    field = document.getElementById('myspace-extension-days');
    if(field) {
      field.value = ret;
  
      form = document.getElementById('myspace-explorer-form');
      if(form) {
        form.submit();
      }
    }
  }
}

function myspace_chown() {
  ret = prompt('New owner ID',"");
  if(ret && ret.length > 0) {
    field = document.getElementById('myspace-chown');
    if(field) {
      field.value = ret;
  
      form = document.getElementById('myspace-explorer-form');
      if(form) {
        form.submit();
      }
    }
  }
}

function myspace_votable_view(webstarter, mySpaceName) {
  action = '/astrogrid-portal/mount/myspace/webstart/' + webstarter;
  mySpace = document.getElementById(mySpaceName);
  if(mySpace) {
    url = action + "?myspace-old-name=" + mySpace.value;
    window.open(url);
  }
}

function myspace_votable_topcat() {
  myspace_votable_view('topcat-full.xml', 'myspace-old-name');
}

function myspace_votable_aladin() {
  myspace_votable_view('aladin-full.xml', 'myspace-old-name');
}

function myspace_upload_url() {
  urlRet = prompt('URL to upload',"");
  catRet = prompt('File category',"");
  if(urlRet && urlRet.length > 0 && catRet && catRet.length > 0) {
    url = document.getElementById('myspace-upload-url');
    cat = document.getElementById('myspace-upload-category');
    
    if(url) {
      url.value = urlRet;
      cat.value = catRet;
      
      form = document.getElementById('myspace-explorer-form');
      if(form) {
        form.submit();
      }
    }
  }
}

function myspace_submit_form(formId, actionId, actionValue) {
  formEl = document.getElementById(formId);
  if(formEl) {
    actionEl = document.getElementById(actionId);
    if(actionEl) {
      actionEl.value = actionValue;
      formEl.submit();
    }
    else {
      alert('Invalid action field: <' + actionId + '>');
    }
  }
  else {
    alert('Cannot submit form: <' + formId + '>');
  }
}

function myspace_download(urlId) {
  urlEl = document.getElementById(urlId);
  if(urlEl) {
    window.open(urlEl.value);
  }
  else {
    alert('invalid url element: ' + urlId);
  }
}

function myspace_properties(fileId) {
  fileEl = document.getElementById(fileId);
  if(fileEl) {
    window.open("/astrogrid-portal/mount/myspace/storefile-properties?myspace-src=" + fileEl.value, "storefileProperties", "toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=500, height=400");
  }
  else {
    alert('invalid file element: ' + fileId);
  }
}

function myspace_clipboard_copy(srcId, clipId) {
  srcEl = document.getElementById(srcId);
  if(!srcEl) {
    alert('no source element: ' + srcId);
    return;
  }
  
  clipEl = document.getElementById(clipId);
  if(!clipEl) {
    alert('no clipboard element: ' + clipId);
    return;
  }
  
  clipEl.value = srcEl.value;
}

function myspace_clipboard_paste(srcId, clipId, agslId) {
  clipEl = document.getElementById(clipId);
  if(!clipEl) {
    alert('no clipboard element: ' + clipId);
    return;
  }
  
//  alert(clipEl.value);

  agslEl = document.getElementById(agslId);
  if(!agslEl) {
    alert('no AGSL element: ' + agslId);
    return;
  }
  myspace_micro_browser("", agslId, "myspace-explorer-form", "/astrogrid-portal/main/mount/myspace/myspace-explorer", "myspace-action", "myspace-copy");
}

function myspace_clipboard_move(srcId, clipId, agslId) {
  clipEl = document.getElementById(clipId);
  if(!clipEl) {
    alert('no clipboard element: ' + clipId);
    return;
  }
  
//  alert(clipEl.value);

  agslEl = document.getElementById(agslId);
  if(!agslEl) {
    alert('no AGSL element: ' + agslId);
    return;
  }
  myspace_micro_browser("", agslId, "myspace-explorer-form", "/astrogrid-portal/main/mount/myspace/myspace-explorer", "myspace-action", "myspace-move");
}

function myspace_micro_browser(ivornId, agslId, formName, formAction, fieldName, fieldValue) {
  window.open("/astrogrid-portal/lean/mount/myspace/myspace-micro?ivorn=" + ivornId + "&agsl=" + agslId + "&form_name=" + formName + "&form_action=" + formAction + "&field_name=" + fieldName + "&field_value=" + fieldValue, "mySpaceMicro", "toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=300, height=200");
}
