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
