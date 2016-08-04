
(function() {
	// Load plugin specific language pack
	tinymce.PluginManager.requireLangPack('insertararchivos');

	tinymce.create('tinymce.plugins.InsertararchivosPlugin', {
		init : function(ed, url) {
			// Register commands
			ed.addCommand('mceInsertararchivos', function() {
				var idform = tinyMCE.activeEditor.settings.idform;
				ed.windowManager.open({
					file : '/sacmicroback/recursos.do?onlydocs=yes&page='+idform,
					width : 600 + parseInt(ed.getLang('insertararchivos.delta_width', 0)),
					height : 300 + parseInt(ed.getLang('insertararchivos.delta_height', 0)),
					inline : 1,
					scrollbars: "yes"
				}, {
					plugin_url : url
				});
			});

			ed.addButton('insertararchivos', {image: url + '/images/archivo.gif', title : 'insertararchivos.desc', cmd : 'mceInsertararchivos' });

		},

		getInfo : function() {
			return {
				longname : 'Inserta archivos',
				author : 'VRS',
				authorurl : 'http://www.indra.es',
				infourl : 'http://www.indra.es',
				version : tinymce.majorVersion + "." + tinymce.minorVersion
			};
		}
	});

	// Register plugin
	tinymce.PluginManager.add('insertararchivos', tinymce.plugins.InsertararchivosPlugin);
})();