function copy_from_to(fromId, toId) {
  fromField = document.getElementById(fromId);
  toField = document.getElementById(toId);
  
  if(fromField && toField) {
    toField.value = fromField.value;
  }
}

