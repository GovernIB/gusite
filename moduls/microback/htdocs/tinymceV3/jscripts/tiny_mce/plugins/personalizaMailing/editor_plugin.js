/**
 * $Id: editor_plugin.js,v 1.2 2011-07-26 10:39:52 u91856 Exp $
 *
 * @author Moxiecode
 * @copyright Copyright � 2004-2008, Moxiecode Systems AB, All rights reserved.
 */

(function() {
	tinymce.create('tinymce.plugins.PersonalizaMailing', {
		init : function(ed, url) {
			var t = this;

			t.editor = ed;
			
			ed.addCommand('mceInsertaNom', function() {
				var nom = t.editor.execCallback('fill_content_callback','addname');
				ed.execCommand('mceInsertContent', false, nom);
			});	
			
			ed.addCommand('mceInsertaCognom', function() {
				var cognom = t.editor.execCallback('fill_content_callback','addcognom');
				ed.execCommand('mceInsertContent', false, cognom);
			});	
			
			ed.addCommand('mceInsertaUrlEncuesta', function() {				
				var urlEnquesta = t.editor.execCallback('fill_content_callback','addurlencuesta');
				ed.execCommand('mceInsertContent', false, urlEnquesta);
			});
			
			ed.addCommand('mceBaixaMailing', function() {				
				var baixa = t.editor.execCallback('fill_content_callback','addbaixa');
				ed.execCommand('mceInsertContent', false, baixa);
			});
			
			ed.addButton('addname', 
					{title : 'Nom', 
					image : url + '/images/name.gif', 
					cmd : 'mceInsertaNom'});
			
			ed.addButton('addcognom', 
					{title : 'Cognom', 
					image : url + '/images/cognom.gif', 
					cmd : 'mceInsertaCognom'});
			
			ed.addButton('addurlencuesta', 
					{title : 'Enquesta', 
					image : url + '/images/encuesta.gif', 
					cmd : 'mceInsertaUrlEncuesta'});
			
			ed.addButton('addbaixa', 
					{title : 'Baixa', 
					image : url + '/images/baixa.gif', 
					cmd : 'mceBaixaMailing'});
		},

		getInfo : function() {
			return {
				longname : 'Inserta el nom del destinatari, i l adreça de l enquesta',
				author : 'Salvador Antich',
				authorurl : 'http://www.caib.es',
				infourl : 'http://www.caib.es',
				version : tinymce.majorVersion + "." + tinymce.minorVersion
			};
		}
	});

	// Register plugin
	tinymce.PluginManager.add('personalizaMailing', tinymce.plugins.PersonalizaMailing);
})();