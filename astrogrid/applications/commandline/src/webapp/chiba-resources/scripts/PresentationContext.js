// PresentationContext: UI State updating.
// Author: unl
// Copyright 2001-2007 ChibaXForms GmbH

/**
 * Constructor.
 */
PresentationContext = function() {
};

// Static member

PresentationContext.CHIBA_PSEUDO_ITEM = "chiba-pseudo-item";
PresentationContext.PROTOTYPE_CLONES = new Array();
PresentationContext.GENERATED_IDS = new Array();
PresentationContext.COUNTER = 0;

// Event handler

/**
 * Handles chiba-load-uri.
 */
PresentationContext.prototype.handleLoadURI = function(uri, show) {
    dojo.debug("PresentationContext.handleLoadURI: uri='" + uri + "', show='" + show + "'");
    window.open(uri, show == "new" ? "_blank" : "_self");
};

/**
 * Handles chiba-render-message.
 */
PresentationContext.prototype.handleRenderMessage = function(message, level) {
    dojo.debug("PresentationContext.handleRenderMessage: message='" + message + "', level='" + level + "'");
    if (level == "modal") {
        alert(message);
    }
    else {
        var ni = dojo.byId('messagePane');
        if (PresentationContext.COUNTER > 0) {
            dojo.dom.removeChildren(ni);
        }
        var newdiv = document.createElement('div');
        PresentationContext.COUNTER++;
        var divIdName = "messagePane-" + PresentationContext.COUNTER;
        newdiv.setAttribute('id', divIdName);
        newdiv.setAttribute('style', "display:none");
        newdiv.innerHTML = 'message';
        ni.appendChild(newdiv);
        dojo.require("dojo.widget.Toaster");
        var params = { type:"Chiba",showDelay: 5000, positionDirection: "bl-up", messageTopic: message};

        dojo.widget.createWidget("Toaster", params, dojo.byId(divIdName));
        dojo.event.topic.publish(message, message);
    }
};

/**
 * Handles chiba-replace-all.
 */
PresentationContext.prototype.handleReplaceAll = function(webcontext) {
    dojo.debug("PresentationContext.handleReplaceAll: ?");
    var sessionKey = document.getElementById("chibaSessionKey").value;
    // add new parameter (params are located before the anchor sign # in an URI)
    var anchorIndex = window.location.href.lastIndexOf("#");
    var queryIndex = window.location.href.lastIndexOf("?");
    var path = window.location.href;
    if (anchorIndex != -1){
      path =  window.location.href.substring(0, anchorIndex);
    }
    if(queryIndex == -1){
      path += "?";
    }
    path += "&submissionResponse&sessionKey="+sessionKey;
    if (anchorIndex != -1){
      path +=  window.location.href.substring(anchorIndex);
    }

    //window.open(webcontext + "/SubmissionResponse?sessionKey="+sessionKey, "_self");
    window.open(path, "_self");
};

/**
 * Handles chiba-state-changed.
 */
PresentationContext.prototype.handleStateChanged = function(targetId, targetName, valid, readonly, required, enabled, value, type) {
    dojo.debug("PresentationContext.handleStateChanged: targetId='" + targetId + "', targetName='" + targetName + "',  valid='" + valid + "',  readonly='" + readonly + "',  required='" + required + "',  enabled='" + enabled + "',  value='" + value + "'");

    var target = document.getElementById(targetId);
    if (target == null) {
        if (type = " xs:string") {
            var parrentId = targetId.substring(1, targetId.length)
            parrentId = parrentId - 2;
            parrentId = "C" + parrentId + "-label";
            dojo.debug("dojo.byId(parrentId) :", parrentId, dojo.byId(parrentId));

            dojo.require("chiba.widget.Image");
            var imageWidget = dojo.widget.createWidget("chiba:Image",
            {
                widgetId: targetId,
                id:targetId,
                value:value,
                alt:value,
                name:"d_" + targetId,
                title:value
            }, dojo.byId(parrentId));

        } else {
            alert("Target " + targetId + " (" + targetName + ") does not exist")
        }
        return;
    }

    if (value != null) {
        PresentationContext._setControlValue(targetId, value);
    //        var tmpColor = dojo.byId(targetId + "-value").style.backgroundColor;
        //        new Effect.Highlight(dojo.byId(targetId + "-value"),{restorecolor:tmpColor});
    }

    if (valid != null) {
        PresentationContext._setValidProperty(target, eval(valid));
    }
    if (readonly != null) {
        PresentationContext._setReadonlyProperty(target, eval(readonly), target);
    }
    if (required != null) {
        PresentationContext._setRequiredProperty(target, eval(required));
    }
    if (enabled != null) {
        PresentationContext._setEnabledProperty(target, eval(enabled));
    }
  //    if(type != null){
    //cutting any prefixes if present cause it can't be known beforehand which prefix is actually used for the types
    if (type != null && type.indexOf(":") != -1) {
        type = type.substring(type.indexOf(":") + 1, type.length);
    }
    var tmpControl = dojo.widget.getWidgetById(targetId + "-value");

  //    if(type ==null && getClassComponent(target.className, 1) != "string"){
    //        type="string";
    //    }
    if (targetName == "output" && (type == undefined || type == "date")) {
        type = "string";
    }
    if (!tmpControl) {
        dojo.debug("PresentationContext.prototype.handleStateChanged: Create new " + type + " widget");
        switch (type) {
            case "boolean":
                dojo.require("chiba.widget.Boolean");
                if (value != null) {
                    if (value == "false") {
                        value = "";
                    }
                    var booleanWidget = dojo.widget.createWidget("chiba:Boolean",
                    {
                        widgetId:targetId + "-value",
                        checked:value,
                        name:"d_" + targetId,
                        title:dojo.byId(targetId).title
                    },
                            dojo.byId(targetId + "-value"));
                } else {
                    var booleanWidget = dojo.widget.createWidget("chiba:Boolean",
                    {
                        widgetId:targetId + "-value",
                        name:"d_" + targetId,
                        title:dojo.byId(targetId).title
                    },
                            dojo.byId(targetId + "-value"));
                }
                break;

            case "anyURI":
                if (targetName == 'output') {
                    dojo.require("chiba.widget.Link");

                    var linkWidget = dojo.widget.createWidget("Link",
                    {
                        id:targetId + "-value",
                        href:value
                    },
                            dojo.byId(targetId + "-value"));
                    break;
                }
            case "hexBinary":
            case "base64Binary":
                if (targetName == 'output') break;

                dojo.require("chiba.widget.Upload");

                var uploadWidget = dojo.widget.createWidget("chiba:Upload",
                {
                    id:targetId + "-value",
                    widgetId:targetId + "-value",
                    css:type,
                    name:"d_" + targetId,
                    title:dojo.byId(targetId + "-value").title
                },
                        dojo.byId(targetId + "-value"));
                break;

            case "date":
            case "time":
            case "dateTime":
                dojo.require("chiba.widget.DropdownDatePicker");

                var dateWidget = dojo.widget.createWidget("chiba:DropdownDatePicker",
                {
                    id:targetId + "-value",
                    widgetId:targetId + "-value",
                    name:"d_" + targetId,
                    value:value,
                    datatype:"date"
                },
                        dojo.byId(targetId + "-value"));

                break;
            case "string":
                dojo.debug("WARNING: type is String, No update mechan")
                if (targetName == "select1") {
                    dojo.debug("Select1.this", this)
                    dojo.debug("Select1.target", target);
                    dojo.debug("Select1.target.childNodes", target.childNodes);

                }

                break;

            default:
            //other types...
                dojo.debug("PresentationContext.prototype.handleStateChanged: Unknown type for control:'", type, "'", this, dojo.byId(targetId));
                break;


        }

    }
     else if (target.className != null && target.className.indexOf("xsd-") != -1 && type != "group") {
        dojo.debug("PresentationContext.prototype.handleStateChanged: Destroy existing widget and create new " + type + " widget");
        var classType = target.className.substring(target.className.indexOf("xsd-"), target.className.length);
        classType = classType.substring(0, classType.indexOf(" "));

        if(classType != "xsd-"+type){
            tmpControl.destroy();
            switch (type) {
                case "boolean":
                    dojo.require("chiba.widget.Boolean");
                    if (value != null) {
                        if (value == "false") {
                            value = "";
                        }
                        dojo.debug("PresentationContext.js: value=" + value);
                        var booleanWidget = dojo.widget.createWidget("chiba:Boolean",
                        {
                            widgetId:targetId + "-value",
                            checked:value,
                            name:"d_" + targetId,
                            title:dojo.byId(targetId).title
                        },
                                dojo.byId(targetId), "last");
                    } else {
                        dojo.debug("PresentationContext: value=null");
                        var booleanWidget = dojo.widget.createWidget("chiba:Boolean",
                        {
                            widgetId:targetId + "-value",
                            name:"d_" + targetId,
                            title:dojo.byId(targetId).title
                        },
                                dojo.byId(targetId), "last");
                    }
                    break;

                case "anyURI":
                    if (targetName == 'output') {
                        dojo.require("chiba.widget.Link");

                        var linkWidget = dojo.widget.createWidget("Link",
                        {
                            id:targetId + "-value",
                            href:value
                        },
                                dojo.byId(targetId), "last");

                        break;
                    }
                case "hexBinary":
                case "base64Binary":
                    dojo.require("chiba.widget.Upload");

                    var uploadWidget = dojo.widget.createWidget("chiba:Upload",
                    {
                        id:targetId + "-value",
                        widgetId:targetId + "-value",
                        css:type,
                        name:"d_" + targetId,
                        title:dojo.byId(targetId + "-value").title
                    },
                            dojo.byId(targetId),
                            "last");
                    break;

                case "date":
                case "time":
                case "dateTime":
                    dojo.require("chiba.widget.DropdownDatePicker");

                    var dateWidget = dojo.widget.createWidget("chiba:DropdownDatePicker",
                    {
                        id:targetId + "-value",
                        widgetId:targetId + "-value",
                        name:"d_" + targetId,
                        value:value,
                        datatype:"date"
                    },
                            dojo.byId(targetId), "last");
                    break;

                case "string":
                    dojo.require("chiba.widget.Inputfield");
                    var inputfieldWidget = dojo.widget.createWidget("chiba:Inputfield",
                    {
                        widgetId:targetId + "-value",
                        name:"d_" + targetId,
                        value:value,
                        title:"title"
                    },
                            dojo.byId(targetId), "last");
                    break;
                default:
                //other types...
                    dojo.debug("Unknown type for control");
                    break;
            }
        }
    }
    else if (type == "xsd-boolean") {
        tmpControl.checked = value;
        tmpControl.xfreadonly = readonly;
    } else if (type == "xsd-date" && value != undefined) {
        tmpControl.setValue(value);
    }

  //dojo.debug(dojo.byId(targetId).className);
    //dojo.debug(getClassComponent(dojo.byId(targetId).className, 1));

    if (target.className != null && target.className.indexOf("xsd-") != -1 && type != "group") {
        var newClassName = target.className.substring(target.className.indexOf("xsd-"), target.className.length);
        newClassName = newClassName.substring(0, newClassName.indexOf(" "));
        dojo.debug("CSS Type: " + newClassName, " New Type: xsd-" + type)
        if (newClassName != "xsd-" + type) {
            _replaceClass(target, getClassComponent(newClassName,0), "xsd-" + type);
        }
    }
};


/**
 * Handles chiba-state-changed for helper elements.
 */
PresentationContext.prototype.handleHelperChanged = function(parentId, type, value) {
    dojo.debug("PresentationContext.handleHelperChanged: parentId='" + parentId + "',  type='" + type + "',  value='" + value + "'");
    switch (type) {
        // Hack to update the label of repeats with appearance = compact;
        case "label":
            var td = document.getElementById(parentId);
            if (td != null && td.nodeName.toLowerCase() == "div") {
                td = td.parentNode;
            }
            if (td != null && td.nodeName.toLowerCase() == "td") {
                var tr = td.parentNode;
                if (tr != null && tr.nodeName.toLowerCase() == "tr") {
                    var tbody = tr.parentNode;
                    if (tbody != null && tbody.nodeName.toLowerCase() == "tbody") {
                        var table = tbody.parentNode;
                        if (table != null && table.nodeName.toLowerCase() == "table") {
                            if (_hasClass(table, "compact-repeat")) {
                                var trhead = tbody.childNodes;
                                for (var i = 0; i < trhead.length; i++) {
                                    if (_hasClass(trhead[i], "repeat-header")) {
                                        var th = trhead[i].cells;
                                        for (var x = 0; x < th.length; x++) {
                                            if (th[x].className == td.className) {
                                                var elem = th[x];
                                                elem.firstChild.firstChild.nodeValue = value;
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
            PresentationContext._setControlLabel(parentId, value);
            return;
        case "help":
            PresentationContext._setControlHelp(parentId, value);
            return;
        case "hint":
            PresentationContext._setControlHint(parentId, value);
            return;
        case "alert":
            PresentationContext._setControlAlert(parentId, value);
            return;
        case "value":
            PresentationContext._setControlValue(parentId, value);
            return;
    }
};


/**
 * Handles chiba-prototype-cloned.
 */
PresentationContext.prototype.handlePrototypeCloned = function(targetId, type, originalId, prototypeId) {
    dojo.debug("PresentationContext.handlePrototypeCloned: targetId='" + targetId + "',  type='" + type + "',  originalId='" + originalId + "',  prototypeId='" + prototypeId + "'");
    if (type == "itemset") {
        PresentationContext._cloneSelectorPrototype(targetId, originalId, prototypeId);
    }
    else {
        PresentationContext._cloneRepeatPrototype(targetId, originalId, prototypeId);
    }
};

/**
 * Handles chiba-id-generated.
 */
PresentationContext.prototype.handleIdGenerated = function(targetId, originalId) {
    dojo.debug("PresentationContext.handleIdGenerated: targetId='" + targetId + "',  originalId='" + originalId + "'");
    PresentationContext._setGeneratedId(targetId, originalId);
};

/**
 * Handles chiba-item-inserted.
 */
PresentationContext.prototype.handleItemInserted = function(targetId, type, originalId, position) {
    dojo.debug("PresentationContext.handleItemInserted: targetId='" + targetId + "',  type='" + type + "',  originalId='" + originalId + "',  position='" + position + "'");
    if (type == "itemset") {
        PresentationContext._insertSelectorItem(targetId, originalId, position);
    }
    else {
        PresentationContext._insertRepeatItem(targetId, originalId, position);
    }
};

/**
 * Handles chiba-item-deleteed.
 */
PresentationContext.prototype.handleItemDeleted = function(targetId, type, originalId, position) {
    dojo.debug("PresentationContext.handleItemDeleted: targetId='" + targetId + "',  type='" + type + "',  originalId='" + originalId + "',  position='" + position + "'");
    if (type == "itemset") {
        PresentationContext._deleteSelectorItem(targetId, originalId, position);
    }
    else {
        PresentationContext._deleteRepeatItem(targetId, originalId, position);
    }
};

PresentationContext.prototype.handleFocus = function(targetId) {
    dojo.byId(targetId + "-value").focus();
}

/**
 * Handles chiba-index-changed.
 */
PresentationContext.prototype.handleIndexChanged = function(targetId, originalId, index) {
    dojo.debug("PresentationContext.handleIndexChanged: targetId='" + targetId + "',  originalId='" + originalId + "',  index='" + index + "'");
    PresentationContext._setRepeatIndex(targetId, originalId, index);
};

/**
 * Handles chiba-switch-toggled.
 */
PresentationContext.prototype.handleSwitchToggled = function(deselectedId, selectedId) {
    dojo.debug("PresentationContext.handleSwitchToggled: deselectedId='" + deselectedId + "', selectedId='" + selectedId + "'");

    if (deselectedId != "switch-toggles") {
        var deselected = document.getElementById(deselectedId);
        _replaceClass(deselected, "selected-case", "deselected-case");
        var inactive = document.getElementById(deselectedId + "-tab");
        _replaceClass(inactive, "active-tab", "inactive-tab");
    }
    if (selectedId != "switch-toggles") {
        var selected = document.getElementById(selectedId);
        _replaceClass(selected, "deselected-case", "selected-case");
        var active = document.getElementById(selectedId + "-tab");
        _replaceClass(active, "inactive-tab", "active-tab");
    }
};

// static utilities

PresentationContext._setValidProperty = function(target, valid) {
    //    dojo.debug("PresentationContext._setValidProperty: " + target + "='" + valid + "'");

    if (valid) {
        _replaceClass(target, "invalid", "valid");
    }
    else {
        _replaceClass(target, "valid", "invalid");
    }
};

PresentationContext._setReadonlyProperty = function(target, readonly, type) {
    //    dojo.debug("PresentationContext._setReadonlyProperty: " + target + "='" + readonly + "'");

    if (readonly) {
        _replaceClass(target, "readwrite", "readonly");
    }
    else {
        _replaceClass(target, "readonly", "readwrite");
    }
    var targetId = target.getAttribute("id");
    if (type=="xsd-date" || type=="xsd-dateTime" || type == "xsd-time"  || _hasClass(dojo.byId(target),"xsd-date")) {
        var tmpWidget = dojo.widget.byId(targetId + "-value");
        if (tmpWidget) {
            tmpWidget.updateReadonly(readonly);
        }
    }
    var value = document.getElementById(targetId + "-value");
    if (value) {
        if (value.nodeName.toLowerCase() == "input" && value.type.toLowerCase() == "hidden") {
            // special treatment for radiobuttons/checkboxes
            PresentationContext._updateSelectors(value, readonly);
            return;
        }

        if (value.nodeName.toLowerCase() == "a") {
            // special treatment for anchors
            if (readonly) {
                if (value.detachEvent) {
                    value.detachEvent("onclick", activate);
                    value.onclick = null;
                }
                else {
                    value.removeAttribute("onclick");
                }
            }
            else {
                if (value.attachEvent) {
                    value.attachEvent("onclick", activate);
                }
                else {
                    value.setAttribute("onclick", "activate(this);")
                }
            }
            return;
        }

        if (readonly) {
            value.setAttribute("disabled", "disabled");
        }
        else {
            value.removeAttribute("disabled");
        }
    }
    //dirty - but won't get better for this version
    value = document.getElementById("clone-" + targetId + "-value");
    if(value != null){
        dojo.debug(value);
        if (readonly) {
            value.setAttribute("disabled", "disabled");
        }
        else {
            value.removeAttribute("disabled");
        }        
    }

};


//--------------------------------------------------
// HELPER METHODS - hidden datastructure for options
//--------------------------------------------------

/**
 * Called on start up to fill in the first values.
 * @param string original_id
 * @param string clone_id
 */
initializeClone = function(original_id, clone_id) {
    //first make sure the begin situation is cloned
    _updateSizeOfClone(original_id, clone_id);
    _updateValueOfClone(original_id, clone_id);
    _updateSelectionOfClone(original_id, clone_id);
};

/**
 * Updates the size of a cloned select control.
 *
 * @param string original_id
 * @param string clone_id
 * @param changedIndex (optional)
 */
_updateSizeOfClone = function(original_id, clone_id, changedIndex, parent) {
    var original;
    var clone;
    if (parent)
    {
        original = _getElementById(parent, original_id);
        clone = _getElementById(parent, clone_id);
    }
    else
    {
        original = document.getElementById(original_id);
        clone = document.getElementById(clone_id);
    }

    var cloneSize = clone.childNodes.length;
    var originalSize = original.options.length;
    if (changedIndex && changedIndex != -1) {
        if (cloneSize < originalSize) {
            //option added
            var option = new Option("", "");

            try {
                clone.add(option, clone.options[changedIndex]); // standards compliant
            }
            catch(ex) {
                clone.add(option, changedIndex); // IE only
            }
        }
        else {
            //remove one
            clone.removeChild(clone.options[changedIndex]);
        }
    }
    else {
        while (cloneSize != originalSize) {
            if (cloneSize < originalSize) {
                //add one
                var option = new Option("", "");
                clone.appendChild(option);
            }
            else {
                //remove one
                clone.removeChild(clone.options[0]);
            }
            cloneSize = clone.childNodes.length;
        }
    }
};

/**
 * Updates the value of a cloned select control.
 *
 * @param string original_id
 * @param string clone_id
 * @param changedIndex (optional)
 */
_updateValueOfClone = function(original_id, clone_id, changedIndex, parent) {
    var original;
    var clone;
    if (parent)
    {
        original = _getElementById(parent, original_id);
        clone = _getElementById(parent, clone_id);
    }
    else
    {
        original = document.getElementById(original_id);
        clone = document.getElementById(clone_id);
    }

    if (changedIndex && changedIndex != -1) {
        var originalOption = original.options[changedIndex];
        var clonedOption = clone.options[changedIndex];
        clonedOption.setAttribute("class", originalOption.getAttribute("class"));
        clonedOption.setAttribute("style", originalOption.getAttribute("style"));
        clonedOption.value = originalOption.value;
        clonedOption.selected = originalOption.selected;
        clonedOption.text = originalOption.text;
    }
    else {
        var originalSize = original.options.length;
        for (var i = 0; i < originalSize; i++) {
            var originalOption = original.options[i];
            var clonedOption = clone.options[i];
            clonedOption.setAttribute("class", originalOption.getAttribute("class"));
            clonedOption.setAttribute("style", originalOption.getAttribute("style"));
            clonedOption.value = originalOption.value;
            clonedOption.selected = originalOption.selected;
            clonedOption.text = originalOption.text;
        }
    }
};

/**
 * Updates the selection of a cloned select control.
 *
 * @param string original_id
 * @param string clone_id
 * @param changedIndex (optional)
 */
_updateSelectionOfClone = function(original_id, clone_id, parent) {
    var original;
    var clone;
    if (parent)
    {
        original = _getElementById(parent, original_id);
        clone = _getElementById(parent, clone_id);
    }
    else
    {
        original = document.getElementById(original_id);
        clone = document.getElementById(clone_id);
    }

    for (var i = 0; i < original.options.length; i++) {
        clone.options[i].selected = original.options[i].selected;
    }
};

/**
 * Called when the clone.onchange() occurs, so we can update the original one.
 * @param string original_id
 * @param string clone_id
 */
updateSelectionOfOriginal = function(original_id, clone_id) {
    //selection has changed on the clone so change the selection on the original
    var original = document.getElementById(original_id);
    var clone = document.getElementById(clone_id);

    for (var i = 0; i < clone.options.length; i++) {
        original.options[i].selected = clone.options[i].selected;
    }
    if (original.onchange) {
        setXFormsValue(original, true);
    }
};


/**
 * Checks if the element has a clone.
 */
_isCloned = function(element_id) {
    var clone = document.getElementById("clone-" + element_id);
    if (clone) {
        return true;
    }
    else {
        return false;
    }
};

/**
 * finds the index of the option
 */
_findIndexOfOption = function(selectElement, option) {
    var len = selectElement.options.length;
    for (var i = 0; i < len; ++i) {
        if (i in selectElement.options && selectElement.options[i] === option) {
            return i;
        }
    }
    return -1;
};

PresentationContext._setRequiredProperty = function(target, required) {
    //    dojo.debug("PresentationContext._setRequiredProperty: " + target + "='" + required + "'");

    if (required) {
        _replaceClass(target, "optional", "required");
//        new Effect.Pulsate(document.getElementById(target.id + "-label"));
    }
    else {
        _replaceClass(target, "required", "optional");
    }
};

PresentationContext._setEnabledProperty = function(target, enabled) {
    //    dojo.debug("PresentationContext._setEnabledProperty: " + target + "='" + enabled + "'");

    //handle compact repeat
    var wrappingElement = dojo.byId(target).parentNode;

    if (enabled) {
        _replaceClass(target, "disabled", "enabled");

        if (wrappingElement.nodeName == "td") {
            if (_hasClass(wrappingElement, "disabled")) {
                _replaceClass(wrappingElement.id, "disabled", "enabled");
            } else {
                _addClass(wrappingElement, "enabled");
            }

        }
    }
    else {
        _replaceClass(target, "enabled", "disabled");
        if (wrappingElement.nodeName == "td") {
            if (_hasClass(wrappingElement, "enabled")) {
                _replaceClass(wrappingElement.id, "enabled", "disabled");
            } else {
                _addClass(wrappingElement, "disabled");
            }
        }
    }


    // handle labels too, they might be rendered elsewhere
    var targetId = target.getAttribute("id");
    var label = document.getElementById(targetId + "-label");
    if (label) {
        if (enabled) {
            _replaceClass(label, "disabled", "enabled");
        }
        else {
            _replaceClass(label, "enabled", "disabled");
        }
    }
};

PresentationContext._setControlValue = function(targetId, value) {
    // SIDOC/CNAF : sidoc-infra-log
    //  dojo.debug("PresentationContext.setControlValue: targetID = '" + targetId + "'");
    //  dojo.debug("PresentationContext.setControlValue: value= '" + value + "'");

    var control = document.getElementById(targetId + "-value");
    if (control == null) {
        alert("value for '" + targetId + "' not found");
        return;
    }

	// SIDOC/CNAF : sidoc-infra-log
    // dojo.debug("PresentationContext.setControlValue: control = '" + control + "'");
    // dojo.debug("PresentationContext.setControlValue: Node's name = '" + control.nodeName.toLowerCase() + "'");

    var listValue = " " + value + " ";
    switch (control.nodeName.toLowerCase()) {
        case "a":
        // <xf:output appearance="anchor"/>
            control.href = value;
        // SIDOC/CNAF : sidoc-infra-204, put the control value
            _setElementText(control, value);
        //dojo.debug("PresentationContext.setControlValue: href = '" + control.href + "'");
            break;
        case "div":
        //TODO: Make save
            break;
        case "img":
        // <xf:output appearance="image"/>
            control.src = value;
            break;
        case "input":
            if (control.type.toLowerCase() == "checkbox") {
                if (control.parentNode && _hasClass(control.parentNode, "selector-item")) {
                    control.value = value;
                }
                else {
                    // special treatment for a single checkbox
                    control.checked = (value == "true") || (value == "1");
                }
                break;
            }

        //TODO: check if "if" statement is still needed, can probably be removed
            if (control.type.toLowerCase() == "hidden") {

                // special treatment for radiobuttons/checkboxes
                var elements = eval("document.chibaform.elements");
                var box;
                var boxValue;
                for (var i = 0; i < elements.length; i++) {
                    if (elements[i].name == control.name && elements[i].type != "hidden") {
                        box = elements[i];
                        boxValue = " " + box.value + " ";
                        if (listValue.indexOf(boxValue) > -1) {
                            box.checked = true;
                        }
                        else {
                            box.checked = false;
                        }
                    }
                }
                break;
            }

            if (control.type.toLowerCase() == "checkbox") {
                var tmpWidget = dojo.widget.byId(targetId + "-value");
                tmpWidget.checked = value;
                if (this.checked == "false") {
                    control.removeAttribute("checked");
                }
                else {
                    control.setAttribute("checked", this.checked);
                }
                break;
            }

            if (control.type.toLowerCase() == "button") {
                // ignore
                break;
            }

            if (control.type.toLowerCase() == "file") {
                // ignore
                break;
            }

            control.value = value;
            break;
        case "option":
            control.value = value;
            if (control.parentNode && control.parentNode.nodeName.toLowerCase() == "optgroup")
            {
                //todo: select_id should be the targetId
                var select_id = control.parentNode.parentNode.getAttribute("id");
                if (_isCloned(select_id))
                {
                    // find the index of the changed option
                    var changedIndex = _findIndexOfOption(control.parentNode.parentNode, control);
                    _updateValueOfClone(select_id, "clone-" + select_id, changedIndex);
                }
            }
            break;
        case "span":
        // <xf:output mediatype="text/html"/>
            if (_hasClass(control, "mediatype-text-html")) {
                control.innerHTML = value;
                break;
            }

            _setElementText(control, value);
            break;
        case "select":
        // special treatment for options
            var options = control.options.length;
            var option;
            var optionValue;
            for (var i = 0; i < options; i++) {
                option = control.options[i];
                optionValue = " " + option.value + " ";
                if (listValue.indexOf(optionValue) > -1) {
                    option.selected = true;
                }
                else {
                    option.selected = false;
                }
            }
            var select_id = control.getAttribute("id");
            if (_isCloned(select_id)) {
                _updateSelectionOfClone(select_id, "clone-" + select_id);
            }
            break;
        case "table":
            if (_hasClass(control, "range-widget")) {
                var oldValue = document.getElementsByClassName('rangevalue', document.getElementById(targetId))[0];
                if (oldValue) {
                    oldValue.className = "step";
                }
                var newValue = document.getElementById(targetId + value);
                if (newValue) {
                    newValue.className = "step rangevalue";
                }
            }
            break;
        case "textarea":
        // <xf:textarea mediatype="text/html"/>
            if (_hasClass(control, "mediatype-text-html")) {
                _styledTextareaSetInnerHTML(control, value);
                break;
            }

        // Classical textarea
            control.value = value;
            break;
        default:
            alert("unknown control '" + control.nodeName + "'");
    }
};

PresentationContext._setControlLabel = function(parentId, value) {
    //    dojo.debug("PresentationContext._setControlLabel: ParrentId" + parentId + ",Value:'" + value + "'");

    var element = document.getElementById(parentId + "-label");
    if (element != null) {
        // update label element
        _setElementText(element, value);
        return;
    }

    // heuristics: look for implicit labels
    var control = document.getElementById(parentId + "-value");
    if(control == null) return;
    switch (control.nodeName.toLowerCase()) {
        case "a":
        // <xf:output appearance="anchor"/>
            _setElementText(control, value);
            break;
        case "span":
        // <xf:output appearance="colorbox"/>
            _setElementText(control, value);
            break;
        case "option":
            control.text = value;
            if (control.parentNode && control.parentNode.nodeName.toLowerCase() == "optgroup")
            {
                var select_id = control.parentNode.parentNode.getAttribute("id");
                if (_isCloned(select_id))
                {
                    // find the index of the changed option
                    var changedIndex = _findIndexOfOption(control.parentNode.parentNode, control);
                    _updateValueOfClone(select_id, "clone-" + select_id, changedIndex);
                }
            }
            break;
        case "input":
            if (control.type.toLowerCase() == "button") {
                control.value = value;
                break;
            }
        // fall through
        default:
        // dirty hack for compact repeats: lookup enclosing table
            var td = document.getElementById(parentId);
            if (td != null && td.nodeName.toLowerCase() == "div") {
                td = td.parentNode;
            }
            if (td != null && td.nodeName.toLowerCase() == "td") {
                var tr = td.parentNode;
                if (tr != null && tr.nodeName.toLowerCase() == "tr") {
                    var tbody = tr.parentNode;
                    if (tbody != null && tbody.nodeName.toLowerCase() == "tbody") {
                        var table = tbody.parentNode;
                        if (table != null && table.nodeName.toLowerCase() == "table") {
                            if (_hasClass(table, "compact-repeat")) {
                                // dojo.debug("ignoring label for '" + parentId + "' in compact repeat");
                                break;
                            }
                        }
                    }
                }
            }

        // complain, finally
            alert("label for '" + parentId + "' not found");
    }
};

PresentationContext._setControlHelp = function(parentId, value) {
    //    dojo.debug("PresentationContext._setControlHelp: " + parentId + "='" + value + "'");

    alert("TODO: PresentationContext._setControlHelp: " + parentId + "='" + value + "'");
};

PresentationContext._setControlHint = function(parentId, value) {
    //    dojo.debug("PresentationContext._setControlHint: " + parentId + "='" + value + "'");

    var element = document.getElementById(parentId + "-value");
    if (element != null) {
        if (element.nodeName.toLowerCase() == "input" && element.type == "hidden") {
            // special treatment for radiobuttons/checkboxes
            var boxes = eval("document.chibaform." + element.name + ".length;");
            var box;
            for (var i = 0; i < boxes; i++) {
                box = eval("document.chibaform." + element.name + "[" + i + "]");
                box.title = value;
            }
        }
        else {
            element.title = value;
        }
    }
    else {
        alert("hint for '" + parentId + "' not found");
    }
};

PresentationContext._setControlAlert = function(parentId, value) {
    //    dojo.debug("PresentationContext._setControlAlert: " + parentId + "='" + value + "'");

    var element = document.getElementById(parentId + "-alert");
    if (element != null) {
        _setElementText(element, value);
    }
    else {
        alert("alert for '" + parentId + "' not found");
    }
};

/**
 * Clones a repeat prototype.
 *
 * @param target the repeat id.
 * @param value the prototype id (original repeat id).
 */
PresentationContext._cloneRepeatPrototype = function(targetId, originalId, prototypeId) {
    //    dojo.debug("PresentationContext._cloneRepeatPrototype: [" + targetId + "/" + originalId + "]='" + prototypeId + "'");

    var clone = document.getElementById(originalId + "-prototype").cloneNode(true);
    clone.setAttribute("id", prototypeId);
    _replaceClass(clone, "repeat-prototype", "repeat-item");
    PresentationContext.PROTOTYPE_CLONES.push(clone);

    var ids = new Array();
    PresentationContext.GENERATED_IDS.push(ids);
};

/**
 * Clones a selector prototype.
 *
 * @param target the selector id.
 * @param value the prototype id (original selector id).
 */
PresentationContext._cloneSelectorPrototype = function(targetId, originalId, prototypeId) {
    //    dojo.debug("PresentationContext._cloneSelectorPrototype: [" + targetId + "/" + originalId + "]='" + prototypeId + "'");

    // clone prototype and make it an item
    var clone;
    var proto = document.getElementById(originalId + "-prototype");

    if (proto.nodeName.toLowerCase() == "select") {
        // special handling for option prototypes, since their prototype
        // element needs a wrapper element for carrying the prototype id
        var optionIndex;
        for (var i = 0; i < proto.childNodes.length; i++) {
            if (proto.childNodes[i].nodeType == 1) {
                optionIndex = i;
                break;
            }
        }
        proto = proto.childNodes[optionIndex];

        // create an option object rather than cloning it, otherwise IE won't
        // display it as an additional option !!!
        clone = new Option("", "");
        clone.selected = false;
        clone.defaultSelected = false;
        clone.id = proto.id;
        clone.title = proto.title;
    }
    else {
        clone = proto.cloneNode(true);
        clone.setAttribute("id", prototypeId);
    }

    clone.className = "selector-item";
    PresentationContext.PROTOTYPE_CLONES.push(clone);

    var ids = new Array();
    PresentationContext.GENERATED_IDS.push(ids);
};

/**
 * Sets a generated id on the current prototype clone.
 *
 * @param target the generated id.
 * @param value the original id.
 */
PresentationContext._setGeneratedId = function(targetId, originalId) {
    //    dojo.debug("PresentationContext._setGeneratedId: " + targetId + "='" + originalId + "'");

    var array = PresentationContext.GENERATED_IDS[PresentationContext.GENERATED_IDS.length - 1];
    array[originalId] = targetId;
    array[originalId + "-value"] = targetId + "-value";
    array[originalId + "-label"] = targetId + "-label";
    array[originalId + "-alert"] = targetId + "-alert";
    array[originalId + "-required"] = targetId + "-required";

    if (!array[PresentationContext.CHIBA_PSEUDO_ITEM]) {
        // we have to add a special 'chiba-pseudo-item' mapping, since for itemsets
        // within repeats there is no item yet at the time the prototype is generated.
        // thus, there is no original id in the prototype !!!
        array[PresentationContext.CHIBA_PSEUDO_ITEM] = targetId;
        array[PresentationContext.CHIBA_PSEUDO_ITEM + "-value"] = targetId + "-value";
        array[PresentationContext.CHIBA_PSEUDO_ITEM + "-label"] = targetId + "-label";
    }
};

/**
 * Inserts the current prototype clone as a repeat item.
 *
 * @param target the repeat id.
 * @param value the insert position.
 */
PresentationContext._insertRepeatItem = function(targetId, originalId, position) {
    //    dojo.debug("PresentationContext._insertRepeatItem: [" + targetId + "/" + originalId + "]='" + position + "'");

    // apply generated ids to prototype
    var prototypeClone = PresentationContext.PROTOTYPE_CLONES.pop();
    var generatedIds = PresentationContext.GENERATED_IDS.pop();
    PresentationContext._applyGeneratedIds(prototypeClone, generatedIds);

    // setup indices
    var currentPosition = 0;
    var targetPosition = parseInt(position);
    var insertIndex = -1;

    // lookup repeat
    var targetElement;
    if (PresentationContext.PROTOTYPE_CLONES.length > 0) {
        // nested repeat
        var enclosingPrototype = PresentationContext.PROTOTYPE_CLONES[PresentationContext.PROTOTYPE_CLONES.length - 1];
        targetElement = _getElementById(enclosingPrototype, originalId);
    }
    else {
        // top-level repeat
        targetElement = document.getElementById(targetId);
    }

    var repeatElement = PresentationContext._getRepeatNode(targetElement);
    var repeatItems = repeatElement.childNodes;

    for (var i = 0; i < repeatItems.length; i++) {
        // lookup elements
        if (repeatItems[i].nodeType == 1) {
            // lookup repeat item
            if (_hasClass(repeatItems[i], "repeat-item")) {
                currentPosition++;

                // store insert index (position *at* insert item)
                if (currentPosition == targetPosition) {
                    insertIndex = i;
                    break;
                }
            }
        }
    }

    // detect reference node
    var referenceNode = null;
    if (insertIndex > -1) {
        referenceNode = repeatItems[insertIndex];
    }

    // insert prototype clone
    prototypeClone.setAttribute("style", "display:none;");
    Effect.Appear(prototypeClone, {duration:0.5});
    repeatElement.insertBefore(prototypeClone, referenceNode);

	// A dirty hack for <textarea mediatype='text/html'> : need to initialize
    // the textarea elements used to support the JScript Editor
    var textareaControls = prototypeClone.getElementsByTagName("textarea");
    for (var i = 0; i < textareaControls.length; i++) {
        if (_hasClass(textareaControls[i], "mediatype-text-html")) {
            initalizeStyledTextarea(textareaControls[i]);
        }
    }
};

/**
 * Inserts the current prototype clone as a selector item.
 *
 * @param target the selector id.
 * @param value the insert position.
 */
PresentationContext._insertSelectorItem = function(targetId, originalId, position) {
    //    dojo.debug("PresentationContext._insertSelectorItem: [" + targetId + "/" + originalId + "]='" + position + "'");

    // apply generated ids to prototype
    var prototypeClone = PresentationContext.PROTOTYPE_CLONES.pop();
    var generatedIds = PresentationContext.GENERATED_IDS.pop();
    PresentationContext._applyGeneratedIds(prototypeClone, generatedIds);

    // setup indices
    var currentPosition = 0;
    var targetPosition = parseInt(position);
    var insertIndex = -1;

    // lookup itemset
    var itemsetElement;
    if (PresentationContext.PROTOTYPE_CLONES.length > 0) {
        // nested repeat
        var enclosingPrototype = PresentationContext.PROTOTYPE_CLONES[PresentationContext.PROTOTYPE_CLONES.length - 1];
        itemsetElement = _getElementById(enclosingPrototype, originalId);
    }
    else {
        // top-level repeat
        itemsetElement = document.getElementById(targetId);
    }

    var itemsetItems = itemsetElement.childNodes;
    for (var i = 0; i < itemsetItems.length; i++) {
        // lookup elements
        if (itemsetItems[i].nodeType == 1) {
            // lookup repeat item
            if (_hasClass(itemsetItems[i], "selector-item")) {
                currentPosition++;

                // store insert index (position *at* insert item)
                if (currentPosition == targetPosition) {
                    insertIndex = i;
                    break;
                }
            }
        }
    }

    // detect reference node
    var referenceNode = null;
    if (insertIndex > -1) {
        referenceNode = itemsetItems[insertIndex];
    }

    // insert prototype clone
    itemsetElement.insertBefore(prototypeClone, referenceNode);

    if (itemsetElement && itemsetElement.nodeName.toLowerCase() == "optgroup")
    {
        //find the id of the select tag above
        var select_id = itemsetElement.parentNode.getAttribute("id");
        if (PresentationContext.PROTOTYPE_CLONES.length > 0)
        {
            if (_getElementById(enclosingPrototype, "clone-" + select_id))
            {
                var optionIndex = _findIndexOfOption(itemsetElement.parentNode, prototypeClone);
                _updateSizeOfClone(select_id, "clone-" + select_id, optionIndex, enclosingPrototype);
            }
        }
        else
        {
            if (_isCloned(select_id))
            {
                var optionIndex = _findIndexOfOption(itemsetElement.parentNode, prototypeClone);
                _updateSizeOfClone(select_id, "clone-" + select_id, optionIndex);
            }
        }
    }
};

/**
 * Deletes a repeat item.
 *
 * @param target the repeat id.
 * @param value the delete position.
 */
PresentationContext._deleteRepeatItem = function(targetId, originalId, position) {
    dojo.debug("PresentationContext._deleteRepeatItem: [" + targetId + "/" + originalId + "]='" + position + "'");

    var currentPosition = 0;
    var targetPosition = parseInt(position);
    var deleteIndex = -1;
    var nextIndex = -1;

    var targetElement = document.getElementById(targetId);
    var repeatElement = PresentationContext._getRepeatNode(targetElement);
    var repeatItems = repeatElement.childNodes;
    for (var i = 0; i < repeatItems.length; i++) {
        // lookup elements
        if (repeatItems[i].nodeType == 1) {
            // lookup repeat item
            if (_hasClass(repeatItems[i], "repeat-item")) {
                currentPosition++;

                // store delete index (position *at* delete item)
                if (currentPosition == targetPosition) {
                    deleteIndex = i;
                }

                // check for next item
                if (currentPosition > targetPosition) {
                    nextIndex = i;
                    break;
                }
            }
        }
    }

    // check for next item to be selected
    var deleteItem = repeatItems[deleteIndex];
    var nextItem;
    if (_hasClass(deleteItem, "repeat-index") && nextIndex > -1) {
        nextItem = repeatItems[nextIndex];

        // reset repeat index manually since it won't change when it is set to
        // the item to delete and there is a following item
        _removeClass(deleteItem, "repeat-item");
        _addClass(nextItem, "repeat-index");
    }

    // delete item
    repeatElement.removeChild(deleteItem);
    //Effect.Fade(deleteItem.id,{duration:0.5});
//    repeatElement.removeChild(deleteItem);
    //    if (nextItem) {
    //        _addClass(nextItem, "repeat-index");
    //    }
};

/**
 * Deletes a selector item.
 *
 * @param target the selector id.
 * @param value the delete position.
 */
PresentationContext._deleteSelectorItem = function(targetId, originalId, position) {
    //    dojo.debug("PresentationContext._deleteSelectorItem: [" + targetId + "/" + originalId + "]='" + position + "'");

    var itemset = document.getElementById(targetId);
    var items = itemset.childNodes;
    var currentPosition = 0;
    var targetPosition = parseInt(position);
    var deleteIndex = -1;

    for (var i = 0; i < items.length; i++) {
        // lookup elements
        if (items[i].nodeType == 1) {
            // lookup repeat item
            if (_hasClass(items[i], "selector-item")) {
                currentPosition++;

                // store delete index (position *at* delete item)
                if (currentPosition == targetPosition) {
                    deleteIndex = i;
                    break;
                }     
            }
        }
    }

    var deleteItem = items[deleteIndex];
    // delete item
    itemset.removeChild(deleteItem);

    if (itemset && itemset.nodeName.toLowerCase() == "optgroup")
    {
        var optionIndex = _findIndexOfOption(itemset.parentNode, deleteItem);
        //find the id of the select tag above
        var select_id = itemset.parentNode.getAttribute("id");
        if (_isCloned(select_id))
        {
            _updateSizeOfClone(select_id, "clone-" + select_id, optionIndex);
        }
    }
};

/**
 * Sets the index of a repeat.
 *
 * @param target the repeat id.
 * @param value the repeat index.
 */
PresentationContext._setRepeatIndex = function(targetId, originalId, index) {
    //    dojo.debug("PresentationContext._setRepeatIndex: [" + targetId + "/" + originalId + "]='" + index + "'");

    var currentPosition = 0;
    var targetPosition = parseInt(index);

    if (targetPosition > 0) {
        var targetElement;
        if (PresentationContext.PROTOTYPE_CLONES.length > 0) {
            // nested repeat
            var enclosingPrototype = PresentationContext.PROTOTYPE_CLONES[PresentationContext.PROTOTYPE_CLONES.length - 1];
            targetElement = _getElementById(enclosingPrototype, originalId);
        }
        else {
            // top-level repeat
            targetElement = document.getElementById(targetId);
        }

        var repeatElement = PresentationContext._getRepeatNode(targetElement);
        var repeatItems = repeatElement.childNodes;
        for (var i = 0; i < repeatItems.length; i++) {
            // lookup repeat items
            if (repeatItems[i].nodeType == 1 && _hasClass(repeatItems[i], "repeat-item")) {
                currentPosition++;

                if (currentPosition == targetPosition) {
                    // select item
                    _addClass(repeatItems[i], "repeat-index");
                }
                else {
                    // deselect item
                    _removeClass(repeatItems[i], "repeat-index");
                }

                // remove preselection
                _removeClass(repeatItems[i], "repeat-index-pre");
            }
        }
    }
};

PresentationContext._updateSelectors = function(control, readonly) {
    var id = control.id.substring(0, control.id.indexOf("-value"));
    var count = eval("document.chibaform." + control.name + ".length;");
    var selector;
    var label;
    var position
    for (var i = 0; i < count; i++) {
        position = i + 1;
        selector = eval("document.chibaform." + control.name + "[" + i + "]");
        label = document.getElementById(id + "-" + position + "-label");
        if (readonly) {
            selector.setAttribute("disabled", "disabled");
            if (label != null) {
                label.setAttribute("disabled", "disabled");
            }
        }
        else {
            selector.removeAttribute("disabled");
            if (label != null) {
                label.removeAttribute("disabled");
            }
        }
    }
};

PresentationContext._getRepeatNode = function(element) {
    var items = element.childNodes;

    for (var i = 0; i < items.length; i++) {
        if (items[i].nodeType == 1 && items[i].nodeName.toLowerCase() == "tbody") {
            return items[i];
        }
    }

    return element;
};

PresentationContext._applyGeneratedIds = function(element, ids) {
    var id = element.getAttribute("id");
    if (id == "-label") {
        element.id = element.parentNode.id + id;
    }
    if (id) {
        var generatedId = ids[id];
        if (generatedId) {
            //            dojo.debug("applying '" + generatedId + "' to '" + id + "'");
            element.setAttribute("id", generatedId);

            if (element.parentNode)
            {
                var clonedElement = _getElementById(element.parentNode, "clone-" + id);
                if (clonedElement)
                {
                    clonedElement.setAttribute("id", "clone-" + generatedId);
                }
            }

            // apply to for-attribute of labels
            if (element.nodeName.toLowerCase() == "label") {
                var generatedFor = generatedId.substring(0, generatedId.length - 6) + "-value";
//                dojo.debug("applying '" + generatedFor + "' for '" + id + "'");
                element.setAttribute("for", generatedFor);
            }
        }
    }

    // hack for hidden inputs and multiple inputs with same name. this doesn't
    // work for radiobuttons in IE, since input fields dyamically have to be
    // created as follows:
    //     document.createElement("<INPUT NAME='...''></INPUT");
    // (this is not a joke, but has been found at msdn.microsoft.com/ie/webdev)
    // todo: configurable data prefix, at least ;-(
    var CHIBA_DATA_PREFIX = "d_";
    if (element.nodeName.toLowerCase() == "input" && element.name && element.name.substring(0, 2) == CHIBA_DATA_PREFIX) {
        id = element.name.substring(2, element.name.length);
        var otherId = ids[id];
        if (otherId) {
            //            dojo.debug("applying '" + CHIBA_DATA_PREFIX + otherId + "' to '" + CHIBA_DATA_PREFIX + id + "'");
            element.setAttribute("name", CHIBA_DATA_PREFIX + otherId);
            if (element.checked) {
                element.checked = false;
                element.defaultChecked = false;
            }
        }
    }

    var toApply = element.childNodes;
    for (var index = 0; index < toApply.length; index++) {
        if (toApply[index].nodeType == 1) {
            PresentationContext._applyGeneratedIds(toApply[index], ids);
        }
    }
};
