/*-------------------- Thymol - the flavour of Thymeleaf --------------------*

   Thymol version 1.0.0 Copyright 2012-2013 James J. Benson.
   jjbenson .AT. users.sf.net

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" basis,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 *---------------------------------------------------------------------------*/

var thURL = "http://www.thymeleaf.org";
var thScriptName = "thymol.js";
var thPrefix = "th";
var thCache = new Object;
var thVersion = "1.0.0";
var thReleaseDate = "2013-11-24";

$(function() {
	thymol();
});

(function(DOMParser) {
	"use strict";
	var DOMParser_proto = DOMParser.prototype, real_parseFromString = DOMParser_proto.parseFromString;
	try {
		if ((new DOMParser).parseFromString("", "text/html")) {
			return;
		}
	}
	catch (ex) {
	}
	DOMParser_proto.parseFromString = function(markup, type) {
		if (/^\s*text\/html\s*(?:;|$)/i.test(type)) {
			var doc = document.implementation.createHTMLDocument("");
			if (markup.toLowerCase().indexOf('<!doctype') > -1) {
				doc.documentElement.innerHTML = markup;
			}
			else {
				doc.body.innerHTML = markup;
			}
			return doc;
		}
		else {
			return real_parseFromString.apply(this, arguments);
		}
	};
}(DOMParser));

var thymol = function() {

	var urlParams = {};

	var protocol = getThParam("thProtocol", false, false, "file:///");
	var debug = getThParam("thDebug", true, false, false);
	var root = getThParam("thRoot", false, true, "");
	var path = getThParam("thPath", false, true, "");
	var allowNullText = getThParam("thAllowNullText", true, false, true);
	var showNullOperands = getThParam("thShowNullOperands", true, false, false);

	var litSpec = "[\'][^\']*?[\']";
	var nonLitSpec = "[^\']*";
	var refSpec = "[#]?\\w*(?:(?:\\.?\\w*)*(\\[" + litSpec + "|" + nonLitSpec + "\\])*)*";
	var opsSpec = ">=|<=|[+\\-\\/%><]|\\*(?!\\{)|==|!=|and|or";

	var varSpec = "[$\\*#@]{1}\{(?:!?[^}]*)\}"; // Forget the content
	var varSpec2 = "[$\\*#@]{1}\{(!?[^}]*)\}"; // Retain the content

	var nonURLSpec = "[$\\*#]{1}\{(?:!?[^}]*)\}";

	var refSplitSpec = "\\.|\\[|\\]";
	
	var numericSpec = "^[+\\-]?[0-9]*?[.]?[0-9]*?$";
	
	var varValSpec = "[$\\*]\{(.*)\}";

	var opsExpr = new RegExp("^(?:" + opsSpec + "){1}$");
	var streamSpec = "(" + opsSpec + "|" + varSpec + "|" + litSpec + "|" + refSpec + ")";
	var streamExpr = new RegExp(streamSpec);

	var varExpr = new RegExp(varSpec2);
	var refSplitExpr = new RegExp(refSplitSpec);

	var brktdSpec = "[(][^)]*?[)]";
	var condSpec = "((?:(?:" + litSpec + ")*|(?:" + brktdSpec + ")*|[^?]*?)*)[\?]?((?:(?:" + litSpec + ")*|(?:" + brktdSpec + ")*|[^:]*?)*)[:]?((?:(?:" + litSpec + ")*|(?:" + brktdSpec + ")*|.*)*)";
	var condExpr = new RegExp(condSpec);

	var jsonDeclSpec = "(?:\\W*([\\'][A-Za-z]+\\w*[\\'])\\s*[:])?\\s*([#][A-Za-z]+\\w*)\\W*";
	var jsonDeclExpr = new RegExp(jsonDeclSpec);

	var nonURLExpr = new RegExp(nonURLSpec);

	var numericExpr = new RegExp(numericSpec);

	var varValExpr = new RegExp(varValSpec);

	
	if (!(typeof thVars === "undefined")) {
		$(thVars).each(function() {
			createVariable(this[0], this[1]);
		});
	}

	(function() {
		var e, f, a = /\+/g, r = /([^&=]+)=?([^&]*)/g, d = function(s) {
			return decodeURIComponent(s.replace(a, " "));
		}, q = window.location.search.substring(1);
		var surl, params, values, scriptUrl = "";
		$("script").each(function() {
			surl = this.src;
			if (surl.indexOf(thScriptName) >= 0) {
				scriptUrl = d(surl);
				return false;
			}
		});
		while (e = r.exec(scriptUrl)) {
			f = e[1].split("?");
			switch (f[1]) {
			case "thProtocol":
				protocol = e[2];
				break;
			case "thDebug":
				debug = e[2];
				break;
			case "thRoot":
				root = e[2];
				break;
			case "thPath":
				path = e[2];
				break;
			case "thAllowNullText":
				allowNullText = e[2];
				break;
			case "thShowNullOperands":
				showNullOperands = e[2];
				break;
			default:
				createVariable(e[1], e[2]);
			}
		}
		while (e = r.exec(q)) {
			createVariable(d(e[1]), e[2]);
		}
	})();

	resolveJSONReferences();

	protocol = override("thProtocol", protocol);
	debug = override("thDebug", debug);
	root = override("thRoot", root);
	path = override("thPath", path);
	allowNullText = override("thAllowNullText", allowNullText);
	showNullOperands = override("thShowNullOperands", showNullOperands);

	var mappings = null;
	if (!(typeof thMappings === "undefined")) {
		mappings = [];
		$(thMappings).each(function() {
			mappings.push([ this[0], this[1] ]);
		});
		mappings.sort(function(a, b) {
			return a[0].length > b[0].length ? -1 : 1;
		});
	}

	var messages = null;
	if (!(typeof thMessages === "undefined")) {
		messages = new Object();
		$(thMessages).each(function() {
			messages[this[0]] = this[1];
		});
	}

	$.ajaxSetup({
		async : false,
		isLocal : true,
		dataType : "text"
	});

	(function() {
		var htmlTag = $("html")[0];
		$(htmlTag.attributes).each(function() {
			if (thURL == this.value) {
				var nsspec = this.localName.split(":");
				if (nsspec.length > 0) {
					thPrefix = nsspec[nsspec.length - 1];
					return;
				}
			}
		});
	})();

	var thIncl = new ThObj("include");
	var thReplace = new ThObj("replace");
	var thSubstituteby = new ThObj("substituteby");
	var thEach = new ThObj("each");
	var thIf = new ThObj("if");
	var thUnless = new ThObj("unless");
	var thSwitch = new ThObj("switch");
	var thCase = new ThObj("case");

	var thAttr = new ThObj("attr");
	var thAttrAppend = new ThObj("attrappend");
	var thAttrPrepend = new ThObj("attrprepend");

	var thAltTitle = new ThObj("alt-title");
	var thLangXmlLang = new ThObj("lang-xmllang");

	var thClassAppend = new ThObj("classappend");

	var specAttrModList = [
    	        "abbr", "accept", "accept-charset", "accesskey", "action", "align", "alt", "archive", "audio", "autocomplete",
    	        "axis", "background", "bgcolor", "border", "cellpadding", "cellspacing", "challenge", "charset", "cite", "class",
    	        "classid", "codebase", "codetype", "cols", "colspan", "compact", "content", "contenteditable", "contextmenu", "data",
    	        "datetime", "dir", "draggable", "dropzone", "enctype", "for", "form", "formaction", "formenctype", "formmethod",
    	        "formtarget", "frame", "frameborder", "headers", "height", "high", "href", "hreflang", "hspace", "http-equiv",
    	        "icon", "id", "keytype", "kind", "label", "lang", "list", "longdesc", "low", "manifest",
    	        "marginheight", "marginwidth", "max", "maxlength", "media", "method", "min", "name", "optimum", "pattern",
    	        "placeholder", "poster", "preload", "radiogroup", "rel", "rev", "rows", "rowspan", "rules", "sandbox",
    	        "scheme", "scope", "scrolling", "size", "sizes", "span", "spellcheck", "src", "srclang", "standby",
    	        "start", "step", "style", "summary", "tabindex", "target", "title", "type", "usemap", "value",
    	        "valuetype", "vspace", "width", "wrap", "xmlbase", "xmllang", "xmlspace"
		   	];

	var fixedValBoolAttrList = [
	   			"async", "autofocus", "autoplay", "checked", "controls", "declare", "default", "defer", "disabled", "formnovalidate",
	   			"hidden", "ismap", "loop", "multiple", "novalidate", "nowrap", "open", "pubdate", "readonly", "required",
	   			"reversed", "scoped", "seamless", "selected"
	   		];

	var thText = new ThObj("text");
	var thUtext = new ThObj("utext");

	var thSpecAttrModList = getAttrList(specAttrModList);
	var thFixedValBoolAttrList = getAttrList(fixedValBoolAttrList);

	var thFragment = new ThObj("fragment");
	var thFragEscp = thFragment.escp.substring(0, thFragment.escp.length - 1) + "='";

	var thObject = new ThObj("object");
	var thWith = new ThObj("with");
	var thRemove = new ThObj("remove");

	if (!(typeof thDisable === "undefined")) {
		$(thDisable).each(function() {
			var thv = this.valueOf();
			switch (thv) {
			case "include":
				thIncl.disable();
				break;
			case "replace":
				thReplace.disable();
				break;
			case "substituteby":
				thSubstituteby.disable();
				break;
			case "if":
				thIf.disable();
				break;
			case "each":
				thEach.disable();
				break;
			case "unless":
				thUnless.disable();
				break;
			case "switch":
				thSwitch.disable();
				break;
			case "case":
				thCase.disable();
				break;
			case "attr":
				thAttr.disable();
				break;
			case "attrappend":
				thAttrAppend.disable();
				break;
			case "attrprepend":
				thAttrPrepend.disable();
				break;
			case "alt-title":
				thAltTitle.disable();
				break;
			case "lang-xmllang":
				thLangXmlLang.disable();
				break;
			case "classappend":
				thClassAppend.disable();
				break;
			case "text":
				thText.disable();
				break;
			case "utext":
				thUtext.disable();
				break;
			case "object":
				thObject.disable();
				break;
			case "with":
				thWith.disable();
				break;
			case "remove":
				thRemove.disable();
				break;
			default:
				doDisable(thv);
			}
		});
	}

	var base = new ThNode(document, false, null, null, null, document.nodeName, "::", false, document);

	process(base);
	return;

	function process(base) {
		var n = base;
		while (n.thDoc) {
			getChildren(n);
			if (n.firstChild && n.firstChild.thDoc && !n.visited) {
				n.visited = true;
				n = n.firstChild;
			}
			else {
				if (n.element != n.thDoc) {
					doReplace(n.isNode, n.element, n.thDoc);
					if (!n.isNode) {
						n.thDoc = n.element;
					}
				}
				if (n.nextSibling && n.nextSibling.thDoc) {
					n = n.nextSibling;
				}
				else {
					processChildren(n);
					if (n == base) {
						break;
					}
					else {
						n = n.parentDoc;
					}
				}
			}
		}
	}

	function getChildren(base) {

		var count = 0;
		var last = null;
		var expanded = false;

		var froot = $(base.thDoc);
		var fstar = froot.find("*");

		fstar.filter(thIncl.escp).add(fstar.filter(thReplace.escp)).add(fstar.filter(thSubstituteby.escp)).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				var theAttr = this;
				if (thIncl.name == theAttr.name || thReplace.name == theAttr.name || thSubstituteby.name == theAttr.name) {
					var child = processImport(element, base, theAttr);
					if (child != null) {
						expanded = true;
						if (count == 0) {
							base.firstChild = child;
						}
						else {
							last.nextSibling = child;
						}
						last = child;
						count++;
					}
				}
			});
		});

		return expanded;

	}

	function processChildren(base) {

		var froot = $(base.thDoc);
		var fstar = $(froot).add(froot.find("*"));

		do {
			repeat = false;
			fstar.filter(thEach.escp).each(function() {
				var element = this;
				$(element.attributes).each(function() {
					if (thEach.name == this.name) {
						repeat = processElement(processEach, element, this);
					}
				});
			});
			froot = $(base.thDoc);
			fstar = $(froot).add(froot.find("*"));
		} while (repeat);

		fstar.filter(thIf.escp).add(fstar.filter(thUnless.escp)).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thIf.name == this.name || thUnless.name == this.name) {
					processElement(processConditional, element, this);
				}
			});
		});

		fstar.filter(thSwitch.escp).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thSwitch.name == this.name) {
					processElement(processSwitch, element, this);
				}
			});
		});

		fstar.filter(thObject.escp).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thObject.name == this.name) {
					processElement(processObject, element, this);
				}
			});
		});

		fstar.filter(thWith.escp).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thWith.name == this.name) {
					processElement(processWith, element, this);
				}
			});
		});

		fstar.filter(thAttr.escp).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thAttr.name == this.name) {
					processElement(processAttr, element, this);
				}
			});
		});

		fstar.filter(thAttrAppend.escp).add(fstar.filter(thAttrPrepend.escp)).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thAttrAppend.name == this.name || thAttrPrepend.name == this.name) {
					processElement(processAttr, element, this);
				}
			});
		});

		for ( var i = 0; i < thSpecAttrModList.length; i++) {
			fstar.filter(thSpecAttrModList[i].escp).each(function() {
				var element = this;
				$(element.attributes).each(function() {
					if (thSpecAttrModList[i].name == this.name) {
						processElement(processSpecAttrMod, element, this, thSpecAttrModList[i]);
					}
				});
			});
		}

		for ( var i = 0; i < thFixedValBoolAttrList.length; i++) {
			fstar.filter(thFixedValBoolAttrList[i].escp).each(function() {
				var element = this;
				$(element.attributes).each(function() {
					if (thFixedValBoolAttrList[i].name == this.name) {
						processElement(processFixedValBoolAttr, element, this, thFixedValBoolAttrList[i]);
					}
				});
			});
		}

		fstar.filter(thAltTitle.escp).add(fstar.filter(thLangXmlLang.escp)).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thAltTitle.name == this.name || thLangXmlLang.name == this.name) {
					processElement(processPairedAttr, element, this);
				}
			});
		});

		fstar.filter(thText.escp).add(fstar.filter(thUtext.escp)).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thText.name == this.name || thUtext.name == this.name) {
					processElement(processText, element, this);
				}
			});
		});

		fstar.filter(thRemove.escp).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				if (thRemove.name == this.name) {
					processElement(processRemove, element, this);
				}
			});
		});

	}

	function processElement(func, element, arg, obj) {
		var parent = element.parentElement;
		if (element.thObjectVar == null) {
			parent = element.parentElement;
			while (parent != null) {
				if (parent.thObjectVar) {
					element.thObjectVar = parent.thObjectVar;
					break;
				}
				parent = parent.parentElement;
			}
		}
		parent = element.parentElement;
		while (parent != null) {
			if (parent.thLocalVars) {
				element.thLocalVars = mergeVars(element.thLocalVars, parent.thLocalVars);
				break;
			}
			parent = parent.parentElement;
		}
		var result = func(element, arg, obj);
		return result;
	}

	function override(paramName, paramValue) {
		var thv = urlParams[paramName];
		if (thv instanceof Param) {
			paramValue = thv.value;
		}
		else {
			paramValue = thv;
		}
		return paramValue;
	}

	function doDisable(attrName) {
		var tha = getThAttrByName(thSpecAttrModList, attrName);
		if (tha != null) {
			tha.disable();
		}
		tha = getThAttrByName(thFixedValBoolAttrList, attrName);
		if (tha != null) {
			tha.disable();
		}
	}

	function getAttrList(attrArray) {
		var attrList = [];
		for ( var i = 0; i < attrArray.length; i++) {
			attrList[i] = new ThObj(attrArray[i]);
		}
		return attrList;
	}

	function getThAttrByName(attrList, name) {
		var theAttr = null;
		$(attrList).each(function() {
			if (name == this.suffix) {
				theAttr = this;
				return;
			}
		});
		return theAttr;
	}

	function mergeVars(current, other) {
		if (!current) {
			current = {};
		}
		for ( var prop in other) {
			if (prop) {
				if (current[prop] == null) {
					current[prop] = other[prop];
				}
			}
		}
		return current;
	}

	function processEach(element, thUrlAttr) {
		var elementsAdded = false;
		var initial = thUrlAttr.value.trim();
		var pair = initial.split(":");
		if (pair) {
			if (pair[0]) {
				var varName = pair[0].trim();
				if (varName) {
					var varNames = varName.split(",");
					varName = varNames[0].trim();
					var statVarName;
					if (varNames.length > 1) {
						statVarName = varNames[1].trim();
					}
					else {
						statVarName = varName + "Stat";
					}
					if (pair[1]) {
						var expr = pair[1].trim();
						expr = resolveExpression(expr, getSubstMode(expr), element);
						var root = element.parentNode;
						var node = element;
						if (expr && (expr instanceof Object) && expr.length > 0) {
							element.removeAttribute(thUrlAttr.name);
							for ( var i = 0; i < expr.length; i++) {
								var tho = expr[i];
								var stat = new Object();
								stat.current = tho;
								stat.size = expr.length;
								stat.index = i;
								var count = i + 1;
								stat.count = count;
								if (i == 0) {
									stat.first = true;
								}
								else {
									stat.first = false;
								}
								if (i == expr.length - 1) {
									stat.last = true;
								}
								else {
									stat.last = false;
								}
								if (i % 2) {
									stat.odd = true;
									stat.even = false;
								}
								else {
									stat.odd = false;
									stat.even = true;
								}
								if (!node.thLocalVars) {
									node.thLocalVars = {};
								}
								node.thLocalVars[varName] = tho;
								node.thLocalVars[statVarName] = stat;
								if (count < expr.length) {
									var newNode = element.cloneNode(true);
									var next;
									if (node.nextElementSibling != null) {
										next = root.insertBefore(newNode, node.nextElementSibling);
									}
									else {
										next = root.appendChild(newNode);
									}
									node = next;
									elementsAdded = true;
								}
							}
						}
					}
				}
			}
		}
		return elementsAdded;
	}

	function processAttr(element, thUrlAttr) {
		var parts = thUrlAttr.value.split(",");
		$(parts).each(function() {
			if (this) {
				var pair = this.split("=");
				if (pair) {
					var attrName = pair[0];
					if (attrName) {
						if (pair[1]) {
							var pos = fixedValBoolAttrList.indexOf(attrName);
							if (pos >= 0) {
								doFixedValBoolAttr(pair[1], element, thFixedValBoolAttrList[pos].suffix);
							}
							else {
								var url = getThAttribute(pair[1], element);
								if (thUrlAttr.name == thAttrAppend.name || thUrlAttr.name == thAttrPrepend.name) {
									var existing = element.getAttribute(attrName);
									if (existing) {
										if (thUrlAttr.name == thAttrAppend.name) {
											url = existing + url;
										}
										else if (thUrlAttr.name == thAttrPrepend.name) {
											url = url + existing;
										}
									}
								}
								element.setAttribute(attrName, url);
							}
						}
					}
				}
			}
		});
		element.removeAttribute(thUrlAttr.name);
	}

	function processSpecAttrMod(element, thUrlAttr, thAttrObj) {
		var url = getThAttribute(thUrlAttr.value, element);
		if (url != "") {
			element.setAttribute(thAttrObj.suffix, url);
			element.removeAttribute(thUrlAttr.name);
		}
		else {
			if (debug) {
				window.alert("thymol.processSpecAttrMod cannot process: " + thUrlAttr.name + "=\"" + thUrlAttr.value + "\"\n" + element.innerHTML);
			}
		}
	}

	function processFixedValBoolAttr(element, thUrlAttr, thAttrObj) {
		var val = doFixedValBoolAttr(thUrlAttr.value, element, thAttrObj.suffix);
		if (val != null) {
			element.removeAttribute(thUrlAttr.name);
		}
		else {
			if (debug) {
				window.alert("thymol.processFixedValBoolAttr cannot process: " + thUrlAttr.name + "=\"" + thUrlAttr.value + "\"\n" + element.innerHTML);
			}
		}
	}

	function doFixedValBoolAttr(val, element, attr) {
		var val = getBoolean(val, element);
		if (val) {
			element.setAttribute(attr, attr);
		}
		return val;
	}

	function isLiteral(val) {
		if (typeof val === "string") {
			var first = val.charAt(0);
			var last = val.charAt(val.length - 1);
			if (first == '\'' && last == '\'') {
				return true;
			}
			if (first == '"' && last == '"') {
				return true;
			}
		}
		return false;
	}

	function dereference(param) {
		var par = param;
		if (param) {
			if ((par.indexOf("${") == 0) || (par.indexOf("*{") == 0) || (par.indexOf("@{") == 0) || (par.indexOf("#{") == 0)) {
				if (par.charAt(par.length - 1) == '}') {
					par = par.substring(2, par.length - 1);
				}
			}
		}
		return par;
	}

	function unQuote(param) {
		var par = param;
		if (param) {
			if (par.charAt(0) == '\'') {
				if (par.charAt(par.length - 1) == '\'') {
					par = par.substring(1, par.length - 1);
				}
			}
			else if (par.charAt(0) == '"') {
				if (par.charAt(par.length - 1) == '"') {
					par = par.substring(1, par.length - 1);
				}
			}
		}
		return par;
	}

	function unParenthesise(param) {
		var par = param;
		if (typeof par === "string") {
			par = par.trim();
		}
		if (par) {
			if (par.charAt(0) == '(') {
				if (par.charAt(par.length - 1) == ')') {
					par = par.substring(1, par.length - 1);
				}
			}
		}
		return par;
	}

	function processPairedAttr(element, thUrlAttr) {
		var url = getThAttribute(thUrlAttr.value, element);
		if (url != "") {
			if (thAltTitle.name == thUrlAttr.name) {
				element.setAttribute("alt", url);
				element.setAttribute("title", url);
			}
			if (thLangXmlLang.name == thUrlAttr.name) {
				element.setAttribute("lang", url);
				element.setAttribute("xml:lang", url);
			}
			element.removeAttribute(thUrlAttr.name);
		}
		else {
			if (debug) {
				window.alert("thymol.processPairedAttr cannot process: " + thUrlAttr.name + "=\"" + thUrlAttr.value + "\"\n" + element.innerHTML);
			}
		}
	}

	function processText(element, thUrlAttr) {
		var url = getThAttribute(thUrlAttr.value, element);
		if (url == null) {
			if (!allowNullText) {
				if (debug) {
					window.alert("thymol.processText cannot process: " + thUrlAttr.name + "=\"" + thUrlAttr.value + "\"\n" + element.innerHTML);
				}
				return;
			}
			url = "";
		}
		try {
			while (element.firstChild != null) {
				element.removeChild(element.firstChild);
				if (element.firstChild == null) {
					break;
				}
			}
			if (thText.name == thUrlAttr.name) {
				var newTextNode = document.createTextNode(url);
				element.appendChild(newTextNode);
			}
			if (thUtext.name == thUrlAttr.name) {
				element.innerHTML = url;
			}
			element.removeAttribute(thUrlAttr.name);
		}
		catch (err) {
			window.alert("text replace error");
		}
	}

	function processObject(element, thUrlAttr) {
		var argValue = thUrlAttr.value.trim();
		var args = argValue.match(varValExpr);
		if (args) {
			var mode = getSubstMode(argValue);
			var val = substituteParam(args[1], mode, element);
			if (val) {
				element.thObjectVar = val;
			}
		}
		element.removeAttribute(thUrlAttr.name);
	}

	function processWith(element, thUrlAttr) {
		var argValue = thUrlAttr.value.trim();
		if (argValue) {
			var assigs = argValue.split(",");
			$(assigs).each(function() {
				if (this) {
					var pair = this.split("=");
					if (pair) {
						var varName = pair[0].trim();
						if (varName) {
							var varVal = pair[1].trim();
							if (varVal) {
								var args = varVal.match(varValExpr);
								var localVar = null;
								if (args) {
									var mode = getSubstMode(varVal);
									var val = resolveExpression(args[1], mode, element);
									if (val) {
										localVar = val;
									}
								}
								else {
									localVar = varVal;
								}
								if (!element.thLocalVars) {
									element.thLocalVars = {};

								}
								element.thLocalVars[varName] = localVar;
							}
						}
					}
				}
			});
		}
		element.removeAttribute(thUrlAttr.name);
	}

	function processRemove(element, thUrlAttr) {
		var arg = thUrlAttr.value.trim();
		var error = false;
		if (arg != "") {
			if ("all" == arg) {
				if (element.parentNode != null) {
					element.parentNode.removeChild(element);
				}
			}
			else if ("body" == arg) {
				element.innerHTML = "";
			}
			else if ("tag" == arg) {
				var nodes = element.childNodes;
				if (element.parentNode != null) {
					$(element.parentNode.childNodes).each(function() {
						if (element == this) {
							$(nodes).each(function() {
								element.parentNode.insertBefore(this.cloneNode(true), element);
							});
							element.parentNode.removeChild(element);
							return false;
						}
					});
				}
			}
			else if ("all-but-first" == arg) {
				var nodes = element.childNodes;
				var first = true;
				$(nodes).each(function() {
					if (this.nodeType == 1) {
						if (!first) {
							element.removeChild(this);
						}
						first = false;
					}
				});
			}
			else {
				error = true;
			}
		}
		if (error && debug) {
			window.alert("thymol cannot process: " + thUrlAttr.name + "=\"" + thUrlAttr.value + "\"\n" + element.innerHTML);
		}
		element.removeAttribute(thUrlAttr.name);
	}

	function processConditional(element, attr) {
		if (attr.value) {
			doIfOrUnless(element, attr.value, (thIf.name == attr.name));
		}
		element.removeAttribute(attr.name);
	}

	function doIfOrUnless(element, value, isIf) {
		var processed = false;
		if (value) {
			var flag = getBoolean(value, element);
			processed = true;
			if (!flag) {
				if (isIf) { // true for "if", false for "unless"
					element.parentNode.removeChild(element);
				}
			}
			else {
				if (!isIf) { // false for "if", true for "unless"
					element.parentNode.removeChild(element);
				}
			}
		}
		if (!processed && debug) {
			window.alert("thymol.processConditional cannot process conditional: " + value + "\n" + element.innerHTML);
		}
	}

	function getBoolean(param, element) {
		if (typeof param === "boolean") {
			return param;
		}
		if (param == null) {
			return false;
		}
		var initial = unParenthesise(param);
		var negate = false;
		if (initial.charAt(0) == '!') {
			negate = true;
			initial = initial.substring(1, initial.length);
			initial = unParenthesise(initial);
		}
		var val = getThAttribute(initial, element);
		if (val == null) {
			var args = initial.match(varExpr); // Check for negated null values
			if (args) {
				if (args[1].charAt(0) == '!') {
					negate = !negate;
				}
			}
		}
		var flag = false;
		if (typeof val === 'boolean') {
			flag = val;
		}
		else if (typeof val === 'number') {
			flag = (val != 0);
		}
		else if (typeof val === 'string') {
			var args = val.match(nonURLExpr);
			if (args) {
				val = args[1];
				flag = testParam(val);
			}
			else {
				flag = testLiteralNotFalse(val);
			}
		}
		if (negate) {
			flag = !flag;
		}
		return flag;
	}

	function testParam(initial) {
		var result = false;
		if (typeof initial === 'boolean') {
			result = initial;
		}
		else {
			var theParam = null;
			var negate = false;
			if (typeof initial === 'object' && initial instanceof Param) {
				theParam = initial;
			}
			else {
				initial = initial.valueOf();
				if (initial.charAt(0) == '!') {
					negate = true;
					initial = initial.substring(1);
				}
			}
			theParam = urlParams[initial];
			if (theParam != null) {
				result = theParam.getBooleanValue();
			}
			if (negate) {
				result = !result;
			}
		}
		return result;
	}

	function testLiteral(initial) {
		if (typeof initial === 'boolean') {
			return initial;
		}
		else if (typeof initial === 'object') {
			if (initial instanceof Param) {
				return initial.getBooleanValue();
			}
		}
		return initial != null;
	}

	function testLiteralTrue(initial) {
		if (typeof initial === 'string') {
			var val = initial.toLowerCase();
			return (val == "true" || val == "on" || val == "yes");
		}
		return testLiteral(initial);
	}

	function testLiteralFalse(initial) {
		var result = false;
		if (typeof initial === 'string') {
			var val = initial.toLowerCase();
			result = (val == "false" || val == "off" || val == "no");
		}
		return result;
	}

	function testLiteralNotFalse(initial) { // True If value is non-null, is a String and is not "false", "off" or "no"
		return !testLiteralFalse(initial);
	}

	function processSwitch(element, attr) {
		var val = unParenthesise(attr.value);
		val = getThAttribute(val, element);
		if (typeof val === 'string') {
			var args = val.match(nonURLExpr);
			if (args) {
				val = args[1];
			}
		}
		var matched = false;
		val = unQuote(val);
		var thCaseSpecs = $(thCase.escp, element);
		thCaseSpecs.each(function() {
			var caseClause = this;
			var remove = true;
			$(caseClause.attributes).each(function() {
				var ccAttr = this;
				if (thCase.name == ccAttr.name) {
					if (!matched) {
						matched = processCase(element, ccAttr, val);
						if (matched) {
							remove = false;
						}
					}
					caseClause.removeAttribute(ccAttr.name);
				}
			});
			if (remove) {
				element.removeChild(caseClause);
			}
		});
		return true;
	}

	function processCase(element, attr, param) {
		var val = substitute(attr.value, element);
		var val = unQuote(val);
		if (val == "*" || (param && (param == val))) {
			return true;
		}
		return false;
	}

	function processImport(element, base, attr) {
		var importNode = null;
		var filePart = null;
		var fragmentPart = "::";
		if (attr.value.indexOf("::") < 0) {
			filePart = getFilePart(attr.value, element);
		}
		else {
			var names = attr.value.split("::");
			filePart = getFilePart(names[0].trim(), element);
			fragmentPart = substitute(names[1].trim(), element);
		}
		if (filePart != null) {
			var isNode = thReplace.name == attr.localName || thSubstituteby.name == attr.localName;
			if (thCache[filePart] != null && thCache[filePart][fragmentPart] != null) {
				isNode = isNode || fragmentPart == "::";
				importNode = new ThNode(thCache[filePart][fragmentPart], false, base, null, null, filePart, fragmentPart, isNode, element);
			}
			else {
				var fileName = filePart + ".html";
				$.get(fileName, function(textContent, status) {
					if ("success" == status) {
						var content = new DOMParser().parseFromString(textContent, "text/html");
						if (thCache[filePart] == null) {
							thCache[filePart] = new Object();
						}
						if (fragmentPart == "::") {
							var htmlContent = $("html", content)[0];
							thCache[filePart][fragmentPart] = htmlContent;
						}
						else {
							var fragSpec = thFragEscp + fragmentPart + "']";
							var fragArray = $(fragSpec, content);
							$(fragArray).each(function() {
								this.removeAttribute(thFragment.name);
								thCache[filePart][fragmentPart] = this;
							});
						}
						importNode = new ThNode(thCache[filePart][fragmentPart], false, base, null, null, filePart, fragmentPart, isNode, element);
					}
					else if (debug) {
						window.alert("thymol.processImport file read failed: " + filePart + " fragment: " + fragmentPart);
					}
				}, "text");
				if (importNode == null && debug) {
					window.alert("thymol.processImport fragment import failed: " + filePart + " fragment: " + fragmentPart);
				}
			}
		}
		element.removeAttribute(attr.name);
		return importNode;
	}

	function getFilePart(part, element) {
		var result = substitute(part, element);
		var mapped = null;
		if (result) {
			if (mappings) {
				mapped = getMapped(result, false);
			}
		}
		if (mapped) {
			result = protocol + mapped;
		}
		else {
			if (result && result.charAt(0) != '.') { // Initial period character indicates a relative path
				var slashpos = result.indexOf('/');
				if (slashpos >= 0) { // If it doesn't start with a '.', and there are no path separators, it's also treated as relative
					if (slashpos == 0) {
						result = result.substring(1);
					}
					result = protocol + root + path + result;
				}
			}
		}
		return result;
	}

	function getThAttribute(part, element) {
		var result = unParenthesise(part);
		var args = null;
		if (result.indexOf("?") >= 0) {
			args = result.match(condExpr);
		}
		var shortCut = true;
		if (args != null) {
			var haveArgs2 = false;
			if (args[2] != null) {
				haveArgs2 = args[2] != "";
			}
			var haveArgs3 = false;
			if (args[3] != null) {
				haveArgs3 = args[3] != "";
			}
			if ((args[1] != null && args[1] != "") && (haveArgs2 || haveArgs3)) {
				shortCut = false;
				if (haveArgs3 && !haveArgs2) { // Elvis is here
					var test = getThAttribute(args[1], element);
					if (test == null) {
						result = getThAttribute(args[3], element);
					}
					else {
						result = test;
					}
				}
				else if (haveArgs2 && !haveArgs3) { // Else expressions can also be omitted, in which case a null value is returned if the condition is false
					if (getBoolean(args[1], element)) {
						result = getThAttribute(args[2], element);
					}
					else {
						result = null;
					}
				}
				else { // Full tertiary expression
					if (getBoolean(args[1], element)) {
						result = getThAttribute(args[2], element);
					}
					else {
						result = getThAttribute(args[3], element);
						;
					}
				}
			}
		}
		if (shortCut) {
			result = processExpression(result, element);
		}
		return result;
	}

	function processExpression(part, element) {
		var args = part.match(/[@]{(.*)}/);
		var result = unParenthesise(part);
		var mode = getSubstMode(result);
		result = dereference(result);
		if (!(result.charAt(0) == '/')) {
			var expr = resolveExpression(result, mode, element);
			if (expr != null && expr != result) {
				if ((expr instanceof Param) || (expr instanceof Object)) {
					if (debug) {
						window.alert("thymol.processExpression cannot resolve expression: \"" + part + "\"");
					}
				}
				else {
					result = expr;
				}
			}
			else {
				result = null;
			}
		}
		var mapped = getMapped(result, true);
		if (mapped) {
			result = getWithProtocol(mapped);
		}
		else if (args) {
			if (result == null) {
				if (args[1]) {
					result = args[1].valueOf().trim();
				}
			}
			if (!/.*:\/\/.*/.test(result)) { // Absolute URL?
				if (/^~?\/.*$/.test(result)) { // Server-relative or Context-relative?
					if (/^~.*$/.test(result)) { // Context-relative?
						result = result.substring(1);
					}
					result = getWithProtocol(root + result.substring(1));
				}
			}
		}
		return result;
	}

	function getMapped(uri, extended) {
		var mapped = null;
		if (uri && typeof uri === "string") {
			if (mappings) {
				for ( var i = 0; i < mappings.length; i++) {
					var key = mappings[i][0];
					if (uri == key) {
						mapped = mappings[i][1];
						break;
					}
					else if (extended) {
						if (uri.indexOf(key) == 0) {
							mapped = uri.substring(key.length);
							mapped = mappings[i][1] + mapped;
							break;
						}
					}
				}
			}
		}
		return mapped;
	}

	function getWithProtocol(initial) {
		var result = initial;
		if (typeof result === "string") {
			result = result.trim();
		}
		if (!/^http:.*$/i.test(result)) { // Is it not an URL?
			result = protocol + result;
		}
		return result;
	}

	function substitute(initial, element, lenient) { // This function is soon to be depracated !!!
		var argValue = initial;
		if (typeof argValue === "string") {
			argValue = argValue.trim();
		}
		var result = argValue;
		var args = "";
		while (args != null) {
			var args = argValue.match(/.*([$\*#@]{(!?[^}]*)}).*/);
			if (args != null && args.length > 0) {
				if (args.length == 3) { // Found an embedded expression
					var token = args[1];
					token = token.replace(/[$]/g, "[$]").replace(/[*]/g, "[*]").replace(/[\']/g, "[\']").replace(/[+]/g, "[\+]").replace(/[\(]/g, "[\(]").replace(/[\)]/g, "[\)]");
					var re = new RegExp(token);
					var mode = getSubstMode(argValue);
					var subs = resolveExpression(args[2], mode, element);
					if (subs != args[2]) {
						result = result.replace(re, subs, "g");
						if (result == "null") {
							result = null;
						}
					}
					else {
						subs = ""; // Substitution failed
						if (debug && !lenient) {
							window.alert("thymol variable substitution failed: \"" + initial + "\"");
						}
					}
					var saved = argValue;
					argValue = argValue.replace(re, subs, "g");
					if (saved == argValue) {
						argValue = "";
					}
				}
			}
		}
		return result;
	}

	function getSubstMode(argValue) {
		var ch0 = argValue.charAt(0);
		var mode = 0;
		if (ch0 == '$') {
			mode = 1;
		}
		else if (ch0 == '@') {
			mode = 2;
		}
		else if (ch0 == '*') {
			mode = 3;
		}
		else if (ch0 == '#') {
			mode = 4;
		}
		return mode;
	}

	function resolveExpression(argValue, mode, element) {
		var result = argValue;
		if (typeof argValue === 'string') {
			var initial = argValue.trim();
			result = initial;
			if (result) {
				var shortCut = urlParams[result];
				if (shortCut) {
					if (shortCut instanceof Param) {
						result = shortCut.value;
					}
					else {
						result = shortCut;
					}
					if (typeof result === 'string' && result.match(numericExpr)) { // Numeric?
						result = parseInt(result);
					}
				}
				else {
					initial = unParenthesise(result);
					var negate = false;
					if (initial.charAt(0) == '!') {
						negate = true;
						initial = initial.substring(1, initial.length);
						initial = unParenthesise(initial);
					}
					result = "";
					var expr = initial;
					var operator = null;
					var parts = "";
					while (parts != null && expr != "") {
						var parts = expr.match(streamExpr);
						if (parts != null && parts.length > 0 && parts[0] != "") {
							var token = parts[1];
							token = token.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/[$]/g, "[$]").replace(/[*]/g, "[*]").replace(/\'/g, "\\'").replace(/[+]/g, "[\+]").replace(/\(/g, "\\(").replace(/\)/g, "\\)");
							var re = new RegExp(token);
							var saved = expr;
							expr = expr.replace(re, "");
							if (saved == expr) {
								expr = "";
							}
							else {
								expr = expr.trim();
							}
							var part = parts[1];
							if (!(typeof part === "string")) {
								part = (new String(part)).trim();
							}
							if (part.match(opsExpr)) {
								operator = part;
							}
							else {
								if (part) {
									var unq = unQuote(part);
									if (unq == part) {
										if (typeof part === 'string' && part.match(numericExpr)) { // Numeric?
											part = parseInt(part);
										}
										else if (!(typeof part === 'numeric') && !(typeof part === 'boolean')) {
											part = dereference(part);
											var subs = substituteParam(part, mode, element);
											if (subs != part) {
												part = subs;
											}
											if (typeof part === 'string' && part.match(numericExpr)) { // Numeric?
												part = parseInt(part);
											}
										}
									}
									else {
										part = unq;
									}
									if (operator) {
										result = performOp(part, operator, result);
										operator = null;
									}
									else {
										result = part;
									}
								}
							}
						}
						else {
							parts = null;
						}
					}
					if (result == initial) { // Unsubstituted!
						result = null;
					}
					if (negate) {
						if (typeof result === 'boolean') {
							result = !result;
						}
						else if (typeof result === 'number') {
							result = (result == 0);
						}
						else if (typeof result === 'string') {
							result = testLiteralFalse(result);
						}
					}
				}
			}
		}
		return result;
	}

	function performOp(part, operator, result) {
		switch (operator) {
		case "==":
			if (result == part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case "!=":
			if (result != part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case "+":
			if (part == null && showNullOperands) {
				part = "null";
			}
			if (result == null) {
				result = part;
			}
			else if (part != null) {
				result = result + part;
			}
			break;
		case "-":
			result = result - part;
			break;
		case "*":
			result = result * part;
			break;
		case "/":
			result = result / part;
			break;
		case "%":
			result = result % part;
			break;
		case "<":
			if (result < part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case ">":
			if (result > part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case ">=":
			if (result >= part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case "<=":
			if (result <= part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case "and":
			if (result && part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case "or":
			if (result || part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		default:
			if (debug) {
				window.alert("thymol.performOp expression resolution failed : " + argValue + " unknown operator : " + operator);
			}
		}
		return result;
	}

	function substituteParam(argValue, mode, element) {
		var result = argValue;
		if (result) {
			var subs = null;
			if (mode == 3) {
				if (element.thObjectVar) {
					subs = getField(element.thObjectVar, result, element);
				}
			}
			else if (mode == 4) {
				if (messages) {
					var msg = messages[result];
					if (msg) {
						subs = msg;
					}
				}
			}
			else {
				var varName = result;
				var param = urlParams[varName];
				if (param) {
					subs = param;
				}
				else {
					var fieldName = "";
					var fields = result.split(refSplitExpr);
					varName = fields[0];
					for ( var i = 1; i < fields.length; i++) {
						if (fields[i] != "") {
							if (fieldName != "") {
								fieldName = fieldName + ".";
							}
							fieldName = fieldName + fields[i];
						}
					}
					if (element.thLocalVars) {
						var lvd = element.thLocalVars[varName];
						$(element.thLocalVars).each(function() {
							var tdv = this[varName];
							if (tdv != null) {
								if (fieldName == "") {
									subs = tdv;
								}
								else {
									subs = getField(tdv, fieldName, element);
								}
								return false;
							}
						});
					}
					if (!subs && varName == "#object" && element.thObjectVar) {
						subs = getField(element.thObjectVar, fieldName, element);
					}
					if (!subs) {
						var param = urlParams[varName];
						if (param) {
							subs = getField(param, fieldName, element);
						}
					}
				}
			}
			result = subs;
			if (subs instanceof Param) {
				result = subs.value;
			}
		}
		return result;
	}

	function getField(avar, apath, element) {
		var field = null;
		if (apath) {
			property = avar;
			var fields = apath.split(refSplitExpr);
			$(fields).each(function() {
				if (this != "") {
					var expr = this.valueOf();
					var subs = getThAttribute(expr, element);
					if (subs != null) {
						expr = subs;
					}
					property = property[expr];
				}
			});
			field = property;
		}
		return field;
	}

	function doReplace(isNode, element, content) {
		if (isNode) {
			var node = document.importNode(content, true);
			if (node.nodeName.toLowerCase() == "html") {
				doInsertion(element, content, function(e, n) {
					e.parentNode.insertBefore(n, e);
				});
				element.parentNode.removeChild(element);
			}
			else {
				element.parentNode.replaceChild(node, element);
			}
		}
		else {
			try {
				while (element.firstChild != null) {
					element.removeChild(element.firstChild);
					if (element.firstChild == null) {
						break;
					}
				}
				doInsertion(element, content, function(e, n) {
					e.appendChild(n);
				});
			}
			catch (err) { // Work-around for IE
				element.innerHTML = content.innerHTML;
			}
		}
	}

	function doInsertion(element, content, func) {
		var topLevel = true;
		if (element.parentElement != null) {
			topLevel = (element.parentElement.nodeName.toLowerCase() == "html");
		}
		$(content.childNodes).each(function() {
			if (!topLevel) {
				var elementName = this.nodeName.toLowerCase();
				if (elementName != "head") { // Don't insert head if not at top level
					if (elementName == "body") { // Skip body element if not at top level
						$(this.childNodes).each(function() {
							var node = document.importNode(this, true);
							func(element, node);
						});
					}
					else {
						var node = document.importNode(this, true);
						func(element, node);
					}
				}
			}
			else { // Insert anything at top level
				var node = document.importNode(this, true);
				func(element, node);
			}
		});
	}

	function ThNode(thDoc, visited, parentDoc, firstChild, nextSibling, fileName, fragName, isNode, element) {
		this.thDoc = thDoc;
		this.visited = visited;
		this.parentDoc = parentDoc;
		this.firstChild = firstChild;
		this.nextSibling = nextSibling;
		this.fileName = fileName;
		this.fragName = fragName;
		this.isNode = isNode;
		this.element = element;
	}

	function ThObj(suffix) {
		this.suffix = suffix;
		this.name = thPrefix + ":" + suffix;
		this.escp = "[" + thPrefix + "\\:" + suffix + "]";
		this.disable = function() {
			this.name = thPrefix + ":nonesuch";
			this.escp = "[" + thPrefix + "\\:nonesuch]";
		};
	}

	function Param(valueArg) {
		this.value = valueArg;
		this.getBooleanValue = function() {
			return testLiteralNotFalse(this.value);
		};
		this.getStringValue = function() {
			return this.value;
		};
		this.getNumericValue = function() {
			return Number(this.value);
		};
	}

	function getThParam(paramName, isBoolean, isPath, defaultValue) {
		var localValue = defaultValue;
		var theParam = urlParams[paramName];
		if (isBoolean && theParam) {
			localValue = theParam.getBooleanValue();
		}
		else {
			try {
				var paramValue = window[paramName];
				if (!(typeof paramValue === "undefined")) {
					if (paramValue != null) {
						if (isBoolean) {
							localValue = (paramValue == true);
						}
						else {
							localValue = paramValue;
						}
					}
				}
			}
			catch (err) {
				if (err instanceof ReferenceError) {
				}
				if (err instanceof EvalError) {
				}
			}
		}
		if (!isBoolean && isPath && localValue.length > 0 && localValue.charAt(localValue.length - 1) != '/') {
			localValue = localValue + '/';
		}
		createVariable(paramName, localValue);
		return localValue;
	}

	function createVariable(name, value) {
		var param = null;
		var tt = (typeof value);
		if (tt == "string") {
			value = decodeURIComponent(value);
		}
		if (tt == "boolean" || tt == "number") {
			param = new Param(value);
		}
		else if (value || value == "") {
			var literalBoolean = testLiteralTrue(value);
			if (literalBoolean) {
				param = literalBoolean;
			}
			else {
				literalBoolean = testLiteralFalse(value);
				if (literalBoolean) {
					param = !literalBoolean;
				}
				else {
					var initial = new String(value);
					initial = initial.trim();
					if (initial.charAt(0) == '#') {
						initial = initial.substring(1);
						try {
							param = createJSONVariable(initial);
						}
						catch (err) {
							if (err instanceof ReferenceError) {
							}
							if (err instanceof EvalError) {
							}
							if (param == null) {
								param = new Param(value); // Just save it
							}
						}
					}
					else {
						param = new Param(initial);
					}
				}
			}
		}
		urlParams[name] = param;
	}

	function createJSONVariable(initial) {
		var current = initial.trim();
		var insts = new Object();
		var parts = " ";
		var substIndex = thVars.length + 1;
		while (parts) {
			parts = current.match(jsonDeclExpr);
			if (parts && parts.length > 2) {
				var token = parts[2];
				token = token.replace(/[\']/g, "[\']");
				var re = new RegExp(token);
				var vName = "thVars[" + substIndex + "]";
				thVars[substIndex] = new Object();
				thVars[substIndex].name = parts[2].substring(1);
				substIndex = substIndex + 1;
				current = current.replace(re, "'" + vName + "'", "g");
			}
		}
		current = current.replace(/[\']/g, "\"");
		return $.parseJSON(current);
	}

	function resolveJSONReferences() {
		for ( var key in urlParams) {
			if (key) {
				var param = urlParams[key];
				if (param != null) {
					if (!(param instanceof Param)) {
						for ( var prop in param) {
							if (prop) {
								var val = param[prop];
								if (typeof val === "string") {
									if (val.indexOf("thVars[") == 0) {
										var subst = null;
										if (prop.match(/\d*/)) { // Array index!
											var ref = val.substring(7, val.length - 1);
											ref = thVars[ref];
											subst = urlParams[ref.name];
										}
										else {
											subst = urlParams[prop];
										}
										param[prop] = subst;
									}
								}
							}
						}
					}
					else if (typeof param.value === "string" && param.value.charAt(0) == '#') {
						var subst = urlParams[param.value.substring(1)];
						urlParams[key] = subst;
					}
				}
			}
		}
	}

};
