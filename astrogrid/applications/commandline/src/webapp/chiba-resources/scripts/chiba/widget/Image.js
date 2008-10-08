/*
	Copyright 2001-2007 ChibaXForms GmbH
	All Rights Reserved.
*/
dojo.provide("chiba.widget.Image");

dojo.widget.defineWidget(
	// widget name and class
	"chiba.widget.Image",

	// superclass
	dojo.widget.HtmlWidget,

	// properties and methods
	{
		// settings
		widgetType: "Image",
		isContainer: true,
		templatePath: dojo.uri.dojoUri("../chiba/widget/templates/Image.html"),

        // parameters : This is the parameters of the Boolean widget
        id: "",
        title: "",
        value: ""
	}
);