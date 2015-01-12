/*-------------------- Thymol - the flavour of Thymeleaf --------------------*

   Thymol version 1.1.0-SNAPSHOT Copyright 2012-2013 James J. Benson.
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
var dataThPrefix = "data-th";
var thVersion = "1.1.0-SNAPSHOT";
var thReleaseDate = "2013-12-03";
var thCache = new Object;

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

(function($) {

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

	/**
	 * @memberOf $.fn
	 */

	$.fn.extend({

		getComments : function() {
			return this.filter(function() {
				return this.nodeType == 8;
			});
		},

		getThDecorated : function(thInst, processFunc) {
			var result = false;
			var instances = this.filter(thInst.escpName).add(this.filter(thInst.escpSynonym));
			for (var i = 0; i < instances.length; i++) {
				for (var j = 0; j < instances[i].attributes.length; j++) {
					if (thInst.name == instances[i].attributes[j].name || thInst.synonym == instances[i].attributes[j].name) {
						result = processElement(processFunc, instances[i], instances[i].attributes[j], thInst);
					}
				}
			}
			return result;
		}

	});
})($);

var thymol = function() {

	var urlParams = {};
	var booleanAndNullTokens = createBooleanAndNullTokens();

	var protocol = getThParam("thProtocol", false, false, "file:///");
	var debug = getThParam("thDebug", true, false, false);
	var root = getThParam("thRoot", false, true, "");
	var path = getThParam("thPath", false, true, "");
	var allowNullText = getThParam("thAllowNullText", true, false, true);
	var showNullOperands = getThParam("thShowNullOperands", true, false, false);

	var numberSpec = "[+\-]?[0-9]*?[.]?[0-9]*?";

	var hardSpec = "true|false|null|(?:" + numberSpec + ")";

	var litSpec = "[\'][^\']*?[\']";
	var nonLitSpec = "[^\']*";
	var refSpec = "[#]?\\w*(?:(?:\\.?\\w*)*(\\[" + litSpec + "|" + nonLitSpec + "\\])*)*";
	var opsSpec = "[+\-\/%><]|\\*(?!\\{)|==|!=|>=|<=|and|or|gt|lt|ge|le|not|eq|neq|ne";

	var varSpec = "[\$\*#@]{1}\{(?:!?[^}]*)\}"; // Forget the content
	var litSubstSpec = "[\|][^\|]*?[\|]";

	var termSpec = litSubstSpec + "|" + varSpec + "|" + litSpec + "|" + refSpec + "|" + hardSpec;
	var binSpec = "\\s*(" + termSpec + ")?\\s*(" + opsSpec + ")?\\s*(" + termSpec + ")?\\s*";
	var parndBinSpec = "\\s*[\(]" + binSpec + "[\)]\\s*";

	var streamSpec = parndBinSpec + "|" + binSpec;
	var streamExpr = new RegExp(streamSpec);

	var opsExpr = new RegExp("^(?:" + opsSpec + "){1}$");

	var refSplitSpec = "\\.|\\[|\\]";

	var numericSpec = "^" + numberSpec + "$";

	var varValSpec = "[\$\*]\{(.*)\}";

	var varSpec2 = "[\$\*#@]{1}\{(!?[^}]*)\}"; // Retain the content

	var nonURLSpec = "[\$\*#]{1}\{(?:!?[^}]*)\}";

	var varExpr = new RegExp(varSpec2);
	var refSplitExpr = new RegExp(refSplitSpec);

	var parndSpec = "[(][^)]*?[)]";
	var condSpec = "((?:(?:" + litSpec + ")*|(?:" + parndSpec + ")*|[^?]*?)*)[\?]?((?:(?:" + litSpec + ")*|(?:" + parndSpec + ")*|[^:]*?)*)[:]?((?:(?:" + litSpec + ")*|(?:" + parndSpec + ")*|.*)*)";
	var condExpr = new RegExp(condSpec);

	var jsonDeclSpec = "(?:\\W*([\\'][A-Za-z]+\\w*[\\'])\\s*[:])?\\s*([#][A-Za-z]+\\w*)\\W*";
	var jsonDeclExpr = new RegExp(jsonDeclSpec);

	var nonURLExpr = new RegExp(nonURLSpec);

	var numericExpr = new RegExp(numericSpec);

	var varValExpr = new RegExp(varValSpec);

	var textFuncSynonym = "~~~~";

	var varParSpec = "([^(]*)\\s*[(]([^)]*?)[)]";
	var varParExpr = new RegExp(varParSpec);

	var domSelectSpec = "([/]{1,2})?(\\w*(?:[\\(][\\)])?)?([^\\[]\\S\\w*(?:[\\(][\\)])?[/]*(?:[\\./#]?[^\\[]\\S\\w*(?:[\\(][\\)])?[/]*)*)?([\\[][^\\]]*?[\\]])?";
	var domSelectExpr = new RegExp(domSelectSpec);

	var varRefExpr = /([$#]{.*?})/;

	var literalTokenExpr = /^[a-zA-Z0-9\[\]\.\-_]*$/;

	var linkExpr = /^@{(.*?)([\(][^\)]*?[\)])?}$/;

	var startParserLevelCommentExpr = /^\s*\/\*\s*$/;
	var endParserLevelCommentExpr = /^\s*\*\/\s*$/;

	var startParserLevelCommentExpr2 = /^\/\*[^/].*/;
	var endParserLevelCommentExpr2 = /.*[^/]\*\/$/;

	var prototypeOnlyCommentEscpExpr = /\/\*\/(.*)\/\*\//;

	var javascriptInlineCommentExpr = /\/\*\[\[(.*)\]\]\*\//;
	var javascriptInlineRemainderExpr = /\s*(?:['][^']*['])*(?:["][^"]*["])*(?:[\(][^\(\)]*[\)])*(?:[\{][^\{\}]*[\}])*(?:[\[][^\[\]]*[\]])*((?:[;,\(\)\[\]:\{\}](?=(?:\s*\/\/.*?(?:\n|$)))(?:\s*\/\/.*?(?:\n|$)))|(?:\s*\/\/.*?(?:\n|$))|(?:[;,\(\)\[\]:\{\}](?=(?:\s*(?:\n|$)))(?:\s*(?:\n|$)))|(?:\s*(?:\n|$)))/;

	var textInlineCommentExpr = /\[\[(.*)\]\]/;

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

	var thAltTitle = new ThObj("alt-title");
	var thAssert = new ThObj("assert");
	var thAttr = new ThObj("attr");
	var thAttrAppend = new ThObj("attrappend");
	var thAttrPrepend = new ThObj("attrprepend");
	var thCase = new ThObj("case");
	var thClassAppend = new ThObj("classappend");
	var thEach = new ThObj("each");
	var thFragment = new ThObj("fragment");
	var thObject = new ThObj("object");
	var thIf = new ThObj("if");
	var thInline = new ThObj("inline");
	var thInclude = new ThObj("include");
	var thLangXmlLang = new ThObj("lang-xmllang");
	var thRemove = new ThObj("remove");
	var thReplace = new ThObj("replace");
	var thSubstituteby = new ThObj("substituteby");
	var thSwitch = new ThObj("switch");
	var thText = new ThObj("text");
	var thUnless = new ThObj("unless");
	var thUtext = new ThObj("utext");
	var thWith = new ThObj("with");

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

	var eventAttrList = [
							"onabort", "onafterprint", "onbeforeprint", "onbeforeunload", "onblur", "oncanplay", "oncanplaythrough", "onchange", "onclick", "oncontextmenu",
							"ondblclick", "ondrag", "ondragend", "ondragenter", "ondragleave", "ondragover", "ondragstart", "ondrop", "ondurationchanged", "onemptied",
							"onended", "onerror", "onfocus", "onformchange", "onforminput", "onhashchange", "oninput", "oninvalid", "onkeydown", "onkeypress",
							"onkeyup", "onload", "onloadeddata", "onloadedmetadata", "onloadstart", "onmessage", "onmousedown", "onmousemove", "onmouseout", "onmouseover",
							"onmouseup", "onmousewheel", "onoffline", "ononline", "onpause", "onplay", "onplaying", "onpopstate", "onprogress", "onratechange",
							"onreadystatechange", "onredo", "onreset", "onresize", "onscroll", "onseeked", "onseeking", "onselect", "onshow", "onstalled",
							"onstorage", "onsubmit", "onsuspend", "ontimeupdate", "onundo", "onunload", "onvolumechange", "onwaiting"
	                    ];

	var thSpecAttrModList = appendToAttrList([], specAttrModList);
	thSpecAttrModList = appendToAttrList(thSpecAttrModList, eventAttrList);
	var thFixedValBoolAttrList = appendToAttrList([], fixedValBoolAttrList);

	var thBlock = new ThObj("block");
	var endThBlockName = "/" + thBlock.name;
	var thBlockSynonym = "th-block";
	var endThBlockSynonym = "/" + thBlockSynonym;

	if (!(typeof thDisable === "undefined")) {
		$(thDisable).each(function() {
			var thv = this.valueOf();
			switch (thv) {
			case "include":
				thInclude.disable();
				break;
			case "inline":
				thInline.disable();
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
			case "assert":
				thAssert.disable();
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
			try {
				getChildren(n);
			}
			catch (err) {
				window.alert("parser error!" + err);
			}
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

		processComments(base);

		var changed = false;
		var froot = $(base.thDoc);
		var fstar = $(froot).add(froot.find("*"));

		fstar.filter(thInclude.escpName).add(fstar.filter(thInclude.escpSynonym)).add(fstar.filter(thReplace.escpName)).add(fstar.filter(thReplace.escpSynonym)).add(fstar.filter(thSubstituteby.escpName)).add(fstar.filter(thSubstituteby.escpSynonym)).each(function() {
			var element = this;
			$(element.attributes).each(function() {
				var theAttr = this;
				if (thInclude.name == theAttr.name || thInclude.synonym == theAttr.name || thReplace.name == theAttr.name || thReplace.synonym == theAttr.name || thSubstituteby.name == theAttr.name || thSubstituteby.synonym == theAttr.name) {
					var child = processImport(element, base, theAttr);
					if (child != null) {
						changed = true;
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

		return changed;

	}

	function processComments(base) {
		var comments = null;
		var froot, fstar;
		var changed;
		do {
			froot = $(base.thDoc);
			fstar = froot.find("*");
			comments = fstar.contents().getComments();
			changed = false;
			for (var i = 0; i < comments.length; i++) {

				var startComment = comments[i];
				var parent = startComment.parentNode;
				var startValue = startComment.nodeValue.trim();

				if (startParserLevelCommentExpr.test(startValue)) {
					var pointer = startComment;
					while (pointer != null) {
						if (endParserLevelCommentExpr.test(pointer.nodeValue)) {
							changed = (parent.removeChild(pointer) != null);
							break;
						}
						var nextPointer = pointer.nextSibling;
						changed = (parent.removeChild(pointer) != null);
						pointer = nextPointer;
					}
				}
				else if (startParserLevelCommentExpr2.test(startValue) && endParserLevelCommentExpr2.test(startValue)) { // Last option so it doesn't conflict with previous case
					parent.removeChild(startComment);
					changed = true;
				}
			}
		} while (changed);
		processPrototypeOnlyComments(base);
	}

	function processPrototypeOnlyComments(base) {
		var comments = null;
		var froot, fstar;
		var changed;
		do {
			froot = $(base.thDoc);
			fstar = froot.find("*");
			comments = fstar.contents().getComments();
			changed = false;
			var indexOfLast = comments.length - 1;
			for (var i = 0; i < comments.length; i++) {
				var startComment = comments[i];
				var parent = startComment.parentNode;
				if (parent != null) {
					var startValue = startComment.nodeValue.trim();
					var deletions = [];
					deletions.push(startComment);
					startValue = startValue.replace(/\n/g, "");
					var res = startValue.match(prototypeOnlyCommentEscpExpr);
					if (res) {
						var fullText = startValue;
						var newElements = null;
						if (parent.localName == "table" || parent.localName == "tbody") {
							if (startValue.indexOf(thBlock.name) >= 0 || startValue.indexOf(thBlockSynonym) >= 0) {
								if (startValue.indexOf(endThBlockName) < 0 || startValue.indexOf(endThBlockSynonym) < 0) { // whole th:block is NOT embedded
									fullText = fullText.replace(res[0], res[1]);
									var innerNodes = [];
									var done = false;
									var next = startComment;
									var done = false;
									do {
										next = next.nextSibling;
										if (next != null) {
											deletions.push(next);
											if (i < indexOfLast) {
												if (next == comments[i + 1]) {
													var commentText = next.nodeValue;
													if (commentText.indexOf(endThBlockName) >= 0 || commentText.indexOf(endThBlockSynonym) >= 0) {
														var res2 = commentText.match(prototypeOnlyCommentEscpExpr);
														if (res2) {
															commentText = commentText.replace(res2[0], res2[1]);
															fullText = fullText + commentText;
														}
														done = true;
													}
												}
												else {
													innerNodes.push(next);
												}
											}
										}
										else {
											done = true;
										}
									} while (!done);
									var blockElement = null;
									var blockDoc = new DOMParser().parseFromString(fullText, "text/html"); // Block is non-HTML5 so we can't use jQuery to find it!
									var blockDocBody = $(blockDoc).find("body")[0];
									for (var i = 0; i < blockDocBody.childNodes.length; i++) {
										if (blockDocBody.childNodes[i].localName == thBlock.name || blockDocBody.childNodes[i].localName == thBlockSynonym) {
											blockElement = blockDocBody.childNodes[i];
											for (var j = 0; j < innerNodes.length; j++) {
												var newNode = innerNodes[j].cloneNode(true);
												blockElement.appendChild(newNode);
											}
										}
									}
									if (blockElement != null) {
										var blockBase = new ThNode(blockDoc, false, null, null, null, blockDoc.nodeName, "::", false, blockDoc);
										processChildren(blockBase);
										changed = insertUncommented(blockBase.thDoc, deletions, parent);
									}
									else {
										parent.removeChild(startComment); // Delete it!
										changed = true;
									}
								}
								else { // Embedded?
									parent.removeChild(startComment); // Delete it!
									changed = true;
								}
							}
						}
						else {
							startValue = startValue.substring(3, startValue.length - 3);
							var newDoc = new DOMParser().parseFromString(startValue, "text/html");
							changed = insertUncommented(newDoc, deletions, parent);
						}
					}
				}
			}
		} while (changed);
	}

	function insertUncommented(doc, deletions, parent) {
		var docBody = $(doc).find("body")[0];
		for (var i = 0; i < docBody.childNodes.length; i++) {
			var newNode = docBody.childNodes[i].cloneNode(true);
			parent.insertBefore(newNode, deletions[0]);
		}
		for (var i = 0; i < deletions.length; i++) {
			parent.removeChild(deletions[i]);
		}
		return true;
	}

	function processChildren(base) {
		var froot, fstar, repeat = false;
		do {
			froot = $(base.thDoc);
			fstar = $(froot).add(froot.find("*"));
			repeat = fstar.getThDecorated(thEach, processEach);
		} while (repeat);
		froot = $(base.thDoc);
		fstar = $(froot).add(froot.find("*"));
		fstar.getThDecorated(thInline, processInline);
		fstar.getThDecorated(thIf, processConditional);
		fstar.getThDecorated(thUnless, processConditional);
		fstar.getThDecorated(thSwitch, processSwitch);
		fstar.getThDecorated(thObject, processObject);
		fstar.getThDecorated(thWith, processWith);
		fstar.getThDecorated(thAttr, processAttr);
		fstar.getThDecorated(thAttrAppend, processAttr);
		fstar.getThDecorated(thAttrPrepend, processAttr);
		for (var i = 0; i < thSpecAttrModList.length; i++) {
			fstar.getThDecorated(thSpecAttrModList[i], processSpecAttrMod);
		}
		for (var i = 0; i < thFixedValBoolAttrList.length; i++) {
			fstar.getThDecorated(thFixedValBoolAttrList[i], processFixedValBoolAttr);
		}
		fstar.getThDecorated(thAltTitle, processPairedAttr);
		fstar.getThDecorated(thLangXmlLang, processPairedAttr);
		fstar.getThDecorated(thText, processText);
		fstar.getThDecorated(thUtext, processText);
		fstar.getThDecorated(thRemove, processRemove);
		var allTags = froot.find("*");
		for (var i = 0; i < allTags.length; i++) {
			if (allTags[i].localName == thBlock.name || allTags[i].localName == thBlockSynonym) {
				doRemoveTag(allTags[i]);
			}
		}
		fstar.getThDecorated(thAssert, processAssert);
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

	function appendToAttrList(attrList, attrArray) {
		var i = attrList.length;
		for (var j = 0; j < attrArray.length; j++) {
			attrList[i++] = new ThObj(attrArray[j]);
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
						if (expr && (expr instanceof Object) && expr.length > 0) {
							var node = element;
							element.removeAttribute(thUrlAttr.name);
							for (var i = 0; i < expr.length; i++) {
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
						else {
							root.removeChild(element);
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
								if (thUrlAttr.name == thAttrAppend.name || thUrlAttr.name == thAttrAppend.synonym || thUrlAttr.name == thAttrPrepend.name || thUrlAttr.name == thAttrPrepend.synonym) {
									var existing = element.getAttribute(attrName);
									if (existing) {
										if (thUrlAttr.name == thAttrAppend.name || thUrlAttr.name == thAttrAppend.synonym) {
											url = existing + url;
										}
										else if (thUrlAttr.name == thAttrPrepend.name || thUrlAttr.name == thAttrPrepend.synonym) {
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

	function processInline(element, thUrlAttr, thAttrObj) {
		var mode = getThAttribute(thUrlAttr.value, element);
		if (mode == "text") {
			doInlineText(element);
		}
		else if (mode == "javascript" || mode == "dart") {
			doInlineJavascript(element);
		}
		else {
			if (debug) {
				window.alert("thymol.processInline cannot process scripting mode: \"" + mode + "\" - it isn't supported by version \"" + thVersion + "\"\n");
			}
		}
		element.removeAttribute(thUrlAttr.name);
	}

	function doInlineText(element) {
		for (var i = 0; i < element.childNodes.length; i++) {
			var changed, value;
			do {
				changed = false;
				if (element.childNodes[i].nodeType == 1) {
					doInlineText(element.childNodes[i]);
				}
				else if (element.childNodes[i].nodeType == 3) {
					value = element.childNodes[i].nodeValue;
					if (value) {
						var expr = textInlineCommentExpr.exec(value);
						if (expr) {
							var term = "";
							if (expr.length > 1) {
								term = "[[" + expr[1] + "]]";
							}
							if (expr.length > 1) {
								var result = getThAttribute(expr[1], element);
								result = value.replace(term, result);
								element.childNodes[i].nodeValue = result;
								changed = true;
							}
							expr = null;
						}
					}
				}
			} while (changed);
		}
	}

	function doInlineJavascript(element) {
		for (var i = 0; i < element.childNodes.length; i++) {
			var changed, value, second;
			do {
				second = null;
				changed = false;
				value = element.childNodes[i].nodeValue;
				if (value) {
					var expr = javascriptInlineCommentExpr.exec(value);
					var scraps, remainder;
					if (expr) {
						var termIndx = expr.index;
						var term = "";
						if (expr.length > 1) {
							term = "/*[[" + expr[1] + "]]*/";
						}
						termIndx = termIndx + term.length;
						remainder = value.substring(termIndx);
						scraps = javascriptInlineRemainderExpr.exec(remainder);
						if (scraps) {
							if (scraps.length > 1) {
								var secondIndx = remainder.indexOf(scraps[1]);
								second = remainder.substring(secondIndx);
								value = value.substring(0, termIndx);
								value = value + second;
							}
						}
						if (expr.length > 1) {
							var result = resolveExpression(expr[1], getSubstMode(expr[1]), element); // Doing this is dangerous!
							if (!isLiteral(result)) {
								result = getStringView(result);
							}
							result = value.replace(term, result);
							element.childNodes[i].nodeValue = result;
							changed = true;
						}
						expr = null;
						scraps = null;
					}
				}
			} while (changed);
		}
	}

	function getStringView(param) {
		var view = "";
		if (typeof param === 'string') {
			view = view + "'" + param + "'";
		}
		else if (typeof param === 'number' || typeof param === 'boolean') {
			view = view + param;
		}
		else if (typeof param === 'object') {
			if (param instanceof Object) {
				var objType = Object.prototype.toString.call(param);
				if ("[object Array]" == objType) {
					view = getStringViewArray(param);
				}
				else if ("[object Object]" == objType) {
					view = getStringViewObject(param);
				}
			}
		}
		return view;
	}

	function getStringViewArray(param) {
		var view = "[";
		for (var i = 0; i < param.length; i++) {
			view = view + getStringView(param[i]);
			if (i < param.length - 1) {
				view = view + ",";
			}
		}
		view = view + "]";
		return view;
	}

	function getStringViewObject(param) {
		var view = "{";
		for ( var key in param) {
			if (key) {
				if (view != "{") {
					view = view + ",";
				}
				view = view + getStringView(key) + ":";
				view = view + getStringView(param[key]);
			}
		}
		view = view + "}";
		return view;
	}

	function processSpecAttrMod(element, thUrlAttr, thAttrObj) {
		var url = getThAttribute(thUrlAttr.value, element);
		element.setAttribute(thAttrObj.suffix, url);
		element.removeAttribute(thUrlAttr.name);
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

	function dereference(param, element) {
		var par = param;
		if (param) {
			if ((par.indexOf("${") == 0) || (par.indexOf("*{") == 0) || (par.indexOf("@{") == 0) || (par.indexOf("#{") == 0)) {
				if (par.charAt(par.length - 1) == '}') {
					var par2 = par.substring(2, par.length - 1).trim();
					if (par2.charAt(par2.length - 1) == '}' && (par2.indexOf("${") == 0) || (par2.indexOf("*{") == 0) || (par2.indexOf("@{") == 0) || (par2.indexOf("#{") == 0)) {
						var nest = getThAttribute(par2, element);
						if (nest != null) {
							par = nest;
						}
					}
					else if ((par2.indexOf("${") < 0) && (par2.indexOf("*{") < 0) && (par2.indexOf("@{") < 0) && (par2.indexOf("#{") < 0)) {
						par = par2;
					}
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
		if (par) {
			if (typeof par === "string") {
				par = par.trim();
				if (par.charAt(0) == '(') {
					if (par.charAt(par.length - 1) == ')') {
						par = par.substring(1, par.length - 1);
					}
				}
			}
		}
		return par;
	}

	function unBracket(param) {
		var par = param;
		if (typeof par === "string") {
			par = par.trim();
		}
		if (par) {
			if (par.charAt(0) == '[') {
				if (par.charAt(par.length - 1) == ']') {
					par = par.substring(1, par.length - 1);
				}
			}
		}
		return par;
	}

	function isBracketed(param) {
		var result = false;
		var par = param;
		if (typeof par === "string") {
			par = par.trim();
		}
		if (par) {
			if (par.charAt(0) == '[') {
				if (par.charAt(par.length - 1) == ']') {
					result = true;
				}
			}
		}
		return result;
	}

	function isLiteralSubst(param) {
		var result = false;
		var par = param;
		if (typeof par === "string") {
			par = par.trim();
		}
		if (par) {
			if (par.charAt(0) == '|') {
				if (par.charAt(par.length - 1) == '|') {
					result = true;
				}
			}
		}
		return result;
	}

	function processPairedAttr(element, thUrlAttr) {
		var url = getThAttribute(thUrlAttr.value, element);
		if (url != "") {
			if (thAltTitle.name == thUrlAttr.name || thAltTitle.synonym == thUrlAttr.name) {
				element.setAttribute("alt", url);
				element.setAttribute("title", url);
			}
			if (thLangXmlLang.name == thUrlAttr.name || thLangXmlLang.synonym == thUrlAttr.name) {
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
		else {
			if (url instanceof Param) {
				url = url.value;
			}
		}
		try {
			while (element.firstChild != null) {
				element.removeChild(element.firstChild);
				if (element.firstChild == null) {
					break;
				}
			}
			if (thText.name == thUrlAttr.name || thText.synonym == thUrlAttr.name) {
				var newTextNode = document.createTextNode(url);
				element.appendChild(newTextNode);
			}
			if (thUtext.name == thUrlAttr.name || thUtext.synonym == thUrlAttr.name) {
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
		doWith(element, thUrlAttr.value);
		element.removeAttribute(thUrlAttr.name);
	}

	function doWith(element, content) {
		var argValue = content.trim();
		var argCount = 0;
		if (argValue) {
			var assigs = argValue.split(",");
			for (var i = 0; i < assigs.length; i++) {
				var term = assigs[i];
				if (term) {
					var pair = term.split("=");
					if (pair) {
						var varName = pair[0].trim();
						if (varName) {
							argCount++;
							var varVal = pair[1].trim();
							if (varVal) {
								var localVar = null;
								var val = resolveExpression(varVal, getSubstMode(varVal), element);
								if (val != null) {
									localVar = val;
								}
								else {
									varVal = unQuote(varVal);
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
			}
		}
		return argCount;
	}

	function doList(element, content) {
		var argValue = content.trim();
		var argsCount = 0;
		var argsList = [];
		if (argValue) {
			var assigs = argValue.split(",");
			for (var i = 0; i < assigs.length; i++) {
				var val = resolveExpression(assigs[i], getSubstMode(assigs[i]), element);
				argsList[i] = val;
			}
			if (!element.thLocalVars) {
				element.thLocalVars = {};
			}
			element.thLocalVars["..."] = argsList;
			argsCount = argsList.length;
		}
		return argsCount;
	}

	function processRemove(element, thUrlAttr) {
		var arg = getThAttribute(thUrlAttr.value, element);
		if ("all" == arg) {
			if (element.parentNode != null) {
				element.parentNode.removeChild(element);
			}
		}
		else if ("body" == arg) {
			element.innerHTML = "";
		}
		else if ("tag" == arg) {
			doRemoveTag(element);
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
		else if ("none" == arg || null == arg) { // V2.1 do nothing!
		}
		element.removeAttribute(thUrlAttr.name);
	}

	function processAssert(element, thUrlAttr) {
		var argValue = thUrlAttr.value.trim();
		var result = true;
		var term = "";
		if (argValue) {
			var terms = argValue.split(",");
			for (var i = 0; i < terms.length; i++) {
				term = terms[i];
				var expr = unParenthesise(term);
				if (expr != null) {
					var val = resolveExpression(expr, getSubstMode(expr), element);
					if (val) {
						var flag = getBoolean(val, element);
						if (!flag) {
							result = false;
							break;
						}
					}
					else {
						result = false;
						break;
					}
				}
				else {
					result = false;
					break;
				}
			}
		}
		if (!result) {
			if (argValue != term) {
				argValue = " list is: " + argValue;
			}
			else {
				argValue = "";
			}
			if (term != "") {
				term = " false term is: \"" + term + "\"";
			}
			window.alert("thymol.processAssert assertion failure -" + argValue + term + "\n");
		}
		element.removeAttribute(thUrlAttr.name);
	}

	function doRemoveTag(element) {
		for (var i = 0; i < element.childNodes.length; i++) {
			element.parentNode.insertBefore(element.childNodes[i].cloneNode(true), element);
		}
		element.parentNode.removeChild(element);
	}

	function processConditional(element, attr) {
		if (attr.value) {
			doIfOrUnless(element, attr.value, (thIf.name == attr.name || thIf.synonym == attr.name));
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
		if (param == null) {
			return false;
		}
		if (typeof param === "boolean") {
			return param;
		}
		else if (typeof param === 'number') {
			return param != 0;
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
		var thCaseSpecs = $(thCase.escpName, element);
		thCaseSpecs.each(function() {
			var caseClause = this;
			var remove = true;
			$(caseClause.attributes).each(function() {
				var ccAttr = this;
				if (thCase.name == ccAttr.name || thCase.synonym == ccAttr.name) {
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
			fragmentPart = names[1].trim();
		}
		if ("this" == filePart) {
			filePart = "";
		}
		if (filePart != null) {
			var parts = filePart.match(varParExpr);
			var fragmentArgsList = null;
			if (parts) {
				if (parts.length > 1) {
					filePart = parts[1].trim();
				}
				if (parts.length > 2) {
					fragmentArgsList = parts[2].trim();
				}
			}
			var isNode = thReplace.name == attr.localName || thReplace.synonym == attr.localName || thSubstituteby.name == attr.localName || thSubstituteby.synonym == attr.localName;
			if (thCache[filePart] != null && thCache[filePart][fragmentPart] != null) {
				isNode = isNode || fragmentPart == "::";
				importNode = new ThNode(thCache[filePart][fragmentPart], false, base, null, null, filePart, fragmentPart, isNode, element);
			}
			else {
				var fragment = null;
				if (filePart != "") { // Signifies v2.1 local fragment
					var fileName = filePart + ".html";
					$.get(fileName, function(textContent, status) {
						if ("success" == status) {
							var content = new DOMParser().parseFromString(textContent, "text/html");
							fragment = getImportNode(element, thCache, filePart, fragmentPart, fragmentArgsList, content);
						}
						else if (debug) {
							window.alert("thymol.processImport file read failed: " + filePart + " fragment: " + fragmentPart);
						}
					}, "text");
				}
				else {
					fragment = getImportNode(element, thCache, filePart, fragmentPart, fragmentArgsList, document);
				}
				if (fragment == null) {
					if (debug) {
						window.alert("thymol.processImport fragment import failed: " + filePart + " fragment: " + fragmentPart);
					}
				}
				else {
					importNode = new ThNode(fragment, false, base, null, null, filePart, fragmentPart, isNode, element);
				}
			}
		}
		element.removeAttribute(attr.name);
		return importNode;
	}

	function getImportNode(element, thCache, filePart, fragmentArg, fragmentArgsList, content) {
		var result = null;
		var fragmentName = fragmentArg.trim();
		var fragmentPart = fragmentName;
		fragmentName = fragmentName.replace(/text\(\)/g, textFuncSynonym);
		var parts = fragmentName.match(varParExpr);
		if (parts == null && fragmentArgsList != null) {
			parts = [];
			parts[1] = fragmentName;
			parts[2] = fragmentArgsList;
		}
		var argsCount = 0;
		if (parts) {
			if (parts.length > 1) {
				fragmentName = parts[1].trim();
				if (parts.length > 2) {
					if (parts[2].indexOf("=") > 0) {
						argsCount = doWith(element, parts[2]);
					}
					else {
						argsCount = doList(element, parts[2]);
					}
				}
			}
		}
		if (thCache[filePart] == null) {
			thCache[filePart] = new Object();
		}
		var matched = false;
		var fragment = null;
		if (fragmentName == "::") {
			var htmlContent = $("html", content)[0];
			result = htmlContent;
			matched = true;
		}
		else {
			var fragArray = $(thFragment.escpName, content);
			$(fragArray).each(function() {
				fragment = this;
				$(this.attributes).each(function() {
					var clean = this.value.replace(/\s/g, "");
					var bare = null;
					var vlParts = clean.match(varParExpr);
					if (vlParts) {
						if (vlParts.length > 1) {
							bare = vlParts[1].trim();
						}
					}
					if (fragmentName == bare && argsCount > 0) {
						if (vlParts.length > 2) {
							var vlArgs = vlParts[2].trim().split(",");
							if (vlArgs) {
								if (vlArgs.length == argsCount) {
									var argsList = element.thLocalVars["..."];
									if (argsList != null) {
										for (var i = 0; i < argsCount; i++) {
											var varName = vlArgs[i].trim();
											element.thLocalVars[varName] = argsList[i];
										}
										element.thLocalVars["..."] = null;
									}
									matched = true;
									return false;
								}
								else if (vlArgs.length > argsCount) {
									return false;
								}
							}
						}
					}
					if (fragmentName == clean || fragmentPart == clean || fragmentName == bare) {
						matched = true;
						return false;
					}
				});
				if (matched) {
					result = fragment;
					return false;
				}
			});
		}
		if (!matched) {
			fragment = getDOMSelection(fragmentName, content);
			if (fragment) {
				result = fragment;
			}
			else {
				if (debug) {
					window.alert("thymol.getImportNode cannot match fragment: \"" + fragmentName + "\"");
				}
			}
		}
		if (result != null && result.nodeType == 1) {
			result.removeAttribute(thFragment.name);
			result.removeAttribute(thFragment.synonym);
		}
		thCache[filePart][fragmentPart] = result;
		return result;
	}

	function getDOMSelection(spec, content) {
		var result = null;
		var scope = "";
		var query = new Array();
		var parts = "";
		var innr = unBracket(spec);
		if (spec != innr && innr.charAt(innr.length - 1) == ']') { // Wrapped in [] and ends with ]]
			spec = innr;
		}
		while (spec != "") {
			parts = spec.match(domSelectExpr);
			if (parts != null && parts.length > 1) {
				var token = null;
				for (var i = 1; i < parts.length; i++) {
					if (parts[i] != null) {
						var token = parts[i];
						var indx = null;
						var innr = unBracket(token);
						if (token != innr) {
							if (innr.match(numericExpr)) {
								indx = innr;
							}
						}
						var saved = spec;
						spec = spec.replace(token, ""); // Only replace 1st occurrence
						if (saved == spec) {
							spec = "";
						}
						if (indx) {
							token = query[query.length - 1];
							var indxed = new String(token);
							indxed.indx = indx;
							query[query.length - 1] = indxed;
						}
						else {
							query.push(token.trim());
						}
						break;
					}
				}
			}
			else {
				break;
			}
		}
		var start = 0;
		if (query.length > 0 && query[0] != "" && query[0].charAt(0) == '/') {
			scope = query[0];
			start = 1;
		}
		var selection = [];
		selection.push(content);
		var descend = false;

		for (var i = start; i < query.length; i++) {
			var subQuery = query[i];

			var innr = unBracket(subQuery);
			if (subQuery != innr) {
				innr = innr.replace(/[']/g, "\"");
				subQuery = "";
				var exprFrags = innr.split(/\s{1}\s*((?:and)|(?:or))\s{1}\s*/);
				for (var j = 0; j < exprFrags.length; j++) {
					if (exprFrags[j] != "and" && exprFrags[j] != "or") {
						var classSpecs = exprFrags[j].match(/[@]?\s*(?:class)\s*(\W?[=])\s*[\"]((?:\w*[\-_]*)*)[\"]/);
						if (classSpecs && classSpecs.length > 0) {
							if (classSpecs[1] == "=") {
								subQuery = subQuery + "[class~='" + classSpecs[2] + "']";
							}
							if (classSpecs[1] == "^=") {
								subQuery = subQuery + "[class^='" + classSpecs[2] + "'],[class*=' " + classSpecs[2] + "']";
							}
						}
						else {
							subQuery = subQuery + "[" + exprFrags[j] + "]";
						}
					}
					else if (exprFrags[j] == "or") {
						subQuery = subQuery + ",";
					}
				}
			}

			var qTerms = subQuery.split("/");
			for (var j = 0; j < qTerms.length; j++) {
				if (qTerms[j] != "") {
					qTerms[j] = qTerms[j].replace(/[@]/g, "");
					if (subQuery.indx != null) {
						qTerms[j] = qTerms[j] + ":eq(" + subQuery.indx + ")";
					}
					var subSelect = [];
					for (var k = 0; k < selection.length; k++) {
						var partial = null;
						if (qTerms[j] == textFuncSynonym) {
							partial = $(selection[k]).contents().filter(function() {
								return this.nodeType === 3; // Node.TEXT_NODE
							});
						}
						else if (descend) {
							partial = $(selection[k]).children(qTerms[j]);
						}
						else if (j == 0) {
							if (scope == "/") {
								var html = $("html", selection[k]);
								if (html.length > 0) {
									selection[k] = html;
								}
								partial = $(selection[k]).children("body").children(qTerms[j]);
								scope = "";
							}
							else {
								if (i == 0 || scope == "//") {
									partial = $(selection[k]).find(qTerms[j]);
									scope = "";
								}
								else {
									partial = $(selection[k]).filter(qTerms[j]);
								}
							}
						}
						else {
							partial = $(selection[k]).children(qTerms[j]);
						}
						if (partial != null) {
							for (var m = 0; m < partial.length; m++) {
								subSelect.push(partial[m]);
							}
						}
					}
					selection = subSelect;
				}
			}
			descend = (qTerms[qTerms.length - 1] == ""); // If qTerms ended with a '/' apply next query to children
		}
		result = selection;
		if (result != null && !(result.length === undefined)) {
			if (result.length > 1) {
				var newNode = document.createDocumentFragment();
				for (var i = 0; i < result.length; i++) {
					newNode.appendChild(result[i]);
				}
				result = newNode;
			}
			else {
				result = result[0];
			}
		}
		return result;
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
		var result = unParenthesise(part);
		var isLink = false;
		var argsList = null;
		var args = part.match(linkExpr);
		if (args) {
			if (args[1]) {
				isLink = true;
				result = args[1].trim();
				if (args[2]) {
					argsList = unParenthesise(args[2].trim());
				}
			}
		}

		var mode = getSubstMode(result);

		var expr = null;
		var unq = unQuote(result);
		if (unq != result) {
			result = unq;
		}
		else {
			if (!literalTokenExpr.test(result)) {
				result = dereference(result, element);
				if (!(result.charAt(0) == '/')) {
					expr = resolveExpression(result, mode, element);
					if (expr != null) {
						if ((expr instanceof Param) || (expr instanceof Object)) {
							if (debug) {
								window.alert("thymol.processExpression cannot resolve expression: \"" + part + "\"");
								return null;
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
			}
			else {
				var token = booleanAndNullTokens[result];
				if (!(typeof token === "undefined")) {
					result = token;
				}
			}
		}

		var mapped = getMapped(result, true);
		if (mapped) {
			result = getWithProtocol(mapped);
		}
		if (isLink) {
			if (result == null) {
				result = "";
			}
			else {
				result = result.toString().trim();
			}
			if (!/.*:\/\/.*/.test(result)) { // Absolute URL?
				if (/^~?\/.*$/.test(result)) { // Server-relative or Context-relative?
					if (/^~.*$/.test(result)) { // Context-relative?
						result = result.substring(1);
					}
					if (/^\/\/.*$/.test(result)) {
						result = getWithProtocol(result);
					}
					else {
						result = getWithProtocol(root + result.substring(1));
					}
				}
			}
			if (argsList) {
				var commaSplit = argsList.split(",");
				for (var i = 0; i < commaSplit.length; i++) {
					var arg = commaSplit[i];
					var eqSplit = commaSplit[i].split("=");
					if (i == 0) {
						result = result + "?" + eqSplit[0];
					}
					else {
						result = result + "&" + eqSplit[0];
					}
					if (eqSplit.length > 1 && eqSplit[1]) {
						var rhs = resolveExpression(eqSplit[1], getSubstMode(eqSplit[1]), element);
						if (rhs != null) {
							result = result + "=" + encodeURIComponent(rhs);
						}
					}
				}
			}
		}
		return result;
	}

	function getMapped(uri, extended) {
		var mapped = null;
		if (uri && typeof uri === "string") {
			if (mappings) {
				for (var i = 0; i < mappings.length; i++) {
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

	function substitute(initial, element, lenient) { // It looks pretty good but it's soon to be deprecated !!!
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
		var mode = 0;
		if (argValue.length > 1) {
			if (argValue.charAt(1) == "{") {
				var ch0 = argValue.charAt(0);
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
			}
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
					if (isLiteralSubst(initial)) {
						initial = decodeLiteralSubst(initial);
					}
					result = "";
					var expr = initial;
					var operator = null;
					var parts = "";
					while (parts != null && expr != "") {
						expr = expr.replace(/\s*\(\s*\)\s*/g, ""); // Strip any empty parenthesis pairs
						parts = expr.match(streamExpr);
						if (parts != null && parts.length > 0 && parts[0] != "") {
							var token = null;
							for (var i = 1; i < parts.length; i++) {
								if (parts[i] != null) {
									token = parts[i];
									break;
								}
							}
							if (token != null) {
								var part = token;
								token = token.replace(/\[/g, "\\[").replace(/\]/g, "\\]").replace(/[|]/g, "[|]").replace(/[$]/g, "[$]").replace(/[*]/g, "[*]").replace(/\'/g, "\\'").replace(/[+]/g, "[\+]").replace(/\(/g, "\\(").replace(/\)/g, "\\)");
								var re = new RegExp(token);
								var saved = expr;
								expr = expr.replace(re, "");
								if (saved == expr) {
									expr = "";
								}
								else {
									expr = expr.trim();
								}
								if (isLiteralSubst(part)) {
									part = decodeLiteralSubst(part);
									expr = part + expr;
								}
								else {
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
													var partMode = mode;
													var deRefpart = dereference(part, element);
													if (deRefpart != part) {
														partMode = getSubstMode(part);
														part = deRefpart;
													}
													var subs = substituteParam(part, partMode, element);
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
							}
						}
						else {
							parts = null;
						}
					}
					if (result == initial && (typeof result == typeof initial)) { // Unsubstituted
						result = null;
					}
					else if (typeof result === 'string') {
						result = result.replace(/&#39;/g, "\'");
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

	function decodeLiteralSubst(param) {
		var result = param;
		result = result.trim();
		result = result.substring(1, result.length - 1);
		result = result.replace(/[\']/g, "&#39;");
		var parts = result.split(varRefExpr);
		if (parts && parts.length > 0) {
			var rep = "";
			for (var i = 0; i < parts.length; i++) {
				if (parts[i] != "") {
					if (!parts[i].match(varRefExpr)) {
						parts[i] = "'" + parts[i] + "'";
					}
					if (rep == "") {
						rep = parts[i];
					}
					else {
						rep = rep + "+" + parts[i];
					}
				}
			}
			result = rep;
		}
		return result;
	}

	function performOp(part, operator, result) {
		switch (operator) {
		case "==":
		case "eq":
			if (result == part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case "!=":
		case "ne":
		case "neq":
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
		case "lt":
			if (result < part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case ">":
		case "gt":
			if (result > part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case ">=":
		case "ge":
			if (result >= part) {
				result = true;
			}
			else {
				result = false;
			}
			break;
		case "<=":
		case "le":
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
		case "not":
			result = !part;
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
					for (var i = 1; i < fields.length; i++) {
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
					if (subs == null && varName == "#object" && element.thObjectVar) {
						subs = getField(element.thObjectVar, fieldName, element);
					}
					if (subs == null) {
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
			var property = avar;
			var fields = apath.split(refSplitExpr);
			for (var i = 0; i < fields.length; i++) {
				if (fields[i] != "") {
					var expr = fields[i].valueOf();
					var subs = getThAttribute(expr, element);
					if (subs != null) {
						expr = subs;
					}
					property = property[expr];
				}
			}
			field = property;
		}
		return field;
	}

	function doReplace(isNode, element, content) {
		if (isNode) {
			var node = document.importNode(content, true);
			node.thLocalVars = element.thLocalVars;
			if (node.nodeName.toLowerCase() == "html") {
				doInsertion(element, content, function(e, n) {
					if (n.nodeType == 1) {
						n.removeAttribute(thFragment.name);
						n.removeAttribute(thFragment.synonym);
					}
					e.parentNode.insertBefore(n, e);
				});
				element.parentNode.removeChild(element);
			}
			else {
				if (node.nodeType == 1) {
					node.removeAttribute(thFragment.name);
					node.removeAttribute(thFragment.synonym);
				}
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
					if (n.nodeType == 1) {
						n.removeAttribute(thFragment.name);
						n.removeAttribute(thFragment.synonym);
					}
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
		this.synonym = dataThPrefix + "-" + suffix;
		this.escpName = "[" + thPrefix + "\\:" + suffix + "]";
		this.escpSynonym = "[" + this.synonym + "]";
		this.disable = function() {
			this.name = thPrefix + ":nonesuch";
			this.escpName = "[" + thPrefix + "\\:nonesuch]";
			this.escpSynonym = "[" + dataThPrefix + "-nonesuch]";
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
		return param;
	}

	function createBooleanAndNullTokens() {
		var tokens = [];
		tokens["null"] = createVariable("null", null);
		tokens["true"] = createVariable("true", true);
		tokens["false"] = createVariable("false", false);
		return tokens;
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
