var fieldClassMap = new Object();

function agSetError(fieldId) {
  var field = document.getElementById(fieldId);
  if(field) {
    fieldClassMap[fieldId] = field.className;
    field.className = field.className = "-error";
  }
}

function agUnsetError(fieldId) {
  var field = document.getElementById(fieldId);
  if(field) {
    field.className = fieldClassMap[fieldId];
  }
}
