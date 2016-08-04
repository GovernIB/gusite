 /**
 * $Id: acronym.js,v 1.9 2010-11-02 08:05:31 u102068 Exp $
 *
 * @author Moxiecode - based on work by Andrew Tetlaw
 * @copyright Copyright © 2004-2007, Moxiecode Systems AB, All rights reserved.
 */

function preinit() {
	// Initialize
	tinyMCE.setWindowArg('mce_windowresize', false);
}

function init() {
	tinyMCEPopup.resizeToInnerSize();
	SXE.initElementDialog('acronym');
	if (SXE.currentAction == "update") {
		SXE.showRemoveButton();
	}
}

function insertAcronym() {
	SXE.insertElement('acronym');
	tinyMCEPopup.close();
}

function removeAcronym() {
	SXE.removeElement('acronym');
	tinyMCEPopup.close();
}