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

function myspace_votable_view() {
  form = document.getElementById('myspace-explorer-form');
  if(form) {
    form.action = "/astrogrid-portal/mount/myspace/webstart/topcat-full.xml";
    form.submit();
  }
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
