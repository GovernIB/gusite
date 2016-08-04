tinyMCE.importPluginLanguagePack('insertarelementos');

var TinyMCE_InsertaElementosPlugin = {
	getInfo : function() {
		return {
			longname : 'Insertar Noticia, Banner, Agenda',
			author : 'Aitor Tomas Pin',
			authorurl : 'http://www.indra.es',
			infourl : '',
			version : tinyMCE.majorVersion + "." + tinyMCE.minorVersion
		};
	},

	/**
	 * Returns the HTML contents of the insertdate, inserttime controls.
	 */
	getControlHTML : function(cn) {
		switch (cn) {
			case "insertanoticia":
				return tinyMCE.getButtonHTML(cn, 'lang_insertanoticia_desc', '{$pluginurl}/images/micronoticia.gif', 'mceInsertaNoticia');

			case "insertaagenda":
				return tinyMCE.getButtonHTML(cn, 'lang_insertaagenda_desc', '{$pluginurl}/images/microagenda.gif', 'mceInsertaAgenda');
			
			case "insertabanner":
				return tinyMCE.getButtonHTML(cn, 'lang_insertabanner_desc', '{$pluginurl}/images/microbanner.gif', 'mceInsertaBanner');

		}

		return "";
	},

	execCommand : function(editor_id, element, command, user_interface, value) {

		// Handle commands
		switch (command) {
			case "mceInsertaNoticia":
				tinyMCE.execInstanceCommand(editor_id,'mceInsertContent',false,'<img property="\MCRST_DUMMY_NOT\" src=\"imgs/plugins/microtag_noticias.gif\" alt=\"Esta imagen es de muestreo y es utilizada por la aplicacion de microsites para reemplazarla por las noticias\" class=mceItemNonEditable contenteditable=false/>');
				return true;
				
			case "mceInsertaAgenda":
				tinyMCE.execInstanceCommand(editor_id,'mceInsertContent',false,'<img property="\MCRST_DUMMY_AGE\" src=\"imgs/plugins/microtag_agenda.gif\"  alt=\"Esta imagen es de muestreo y es utilizada por la aplicacion de microsites para reemplazarla por la agenda\" class=mceItemNonEditable contenteditable=false/>');
				return true;
				
			case "mceInsertaBanner":
				tinyMCEexecInstanceCommand(editor_id,'mceInsertContent',false,'<img property="\MCRST_DUMMY_BAN\" src=\"imgs/plugins/microtag_banner.gif\" alt=\"Esta imagen es de muestreo y es utilizada por la aplicacion de microsites para reemplazarla por un banner\" class=mceItemNonEditable contenteditable=false/>');
				return true;				
		}
		
		// Pass to next handler in chain
		return false;
	}
};

tinyMCE.addPlugin("insertarelementos", TinyMCE_InsertaElementosPlugin);
