var fieldClassMap = new Object();

function agSetError(fieldId) {
  var field = document.getElementById(fieldId);
  if(field) {
    fieldClassMap[fieldId] = field.className;
    field.className = field.className = "-error";
  }
  
  return field;
}

function agUnsetError(fieldId) {
  var field = document.getElementById(fieldId);
  if(field) {
    field.className = fieldClassMap[fieldId];
    removeErrorMsg(fieldId, field);
  }
  
  return field;
}

function agSetErrorMsg(fieldId, heading, message) {
  var field = agSetError(fieldId);
  if(field) {
    addErrorMessage(fieldId, field, heading, message);
  }
  
  return field;
}

function addErrorMessage(fieldId, field, heading, message) {
  // TODO: get error list
  var errors = document.getElementById('ag-errors');
  if(errors) {
    alert('No error node: cannot add error message');
    
    // TODO: add new error node name '<fieldId>-error-message'
    
    // TODO: add heading text.
    var heading = document.createElement('span');
    heading.className ='ag-error-heading';
    
    var headingText = document.createTextNode('Error:');
    
    heading.appendChild(headingText);
    errors.appendChild(heading);
    
    // TODO: add anchor
/*
    var anchor = document.createElement('span');
    anchor.className = 'ag-error-anchor';
    
    var anchorRef = document.createElement('a');
    anchorRef.setAttribute('', '');
    
    anchor.appendChild(anchorRef);
    errors.appendChild(anchor);
*/

    // TODO: add message text
    var errorMsg = document.createElement('span');
    errorMsg.className = 'ag-error-message';
    
    var errorText = document.createTextNode(message);
    
    errorMsg.appendChild(errorText);
    errors.appendChild(errorMsg);
    
/*
<xsl:if test="@anchor">
  <span class="ag-error-anchor"><a>
    <xsl:attribute name="href">#<xsl:value-of select="@anchor"/></xsl:attribute>
    Go!
  </a></span>
</xsl:if>

<span class="ag-error-message">message: <xsl:value-of select="normalize-space(./node()[local-name() = 'ag-message'])"/></span>
*/
}

function removeErrorMsg(fieldId, field) {
  // TODO: remove error node name '<fieldId>-error-message'
}

