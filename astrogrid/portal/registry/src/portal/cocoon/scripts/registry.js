/*
 *
 * Moved to extras.js (portal/base/site/web/scripts) following changes to microbrowser
 * Make any ammendments there
 *
 */


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
