
(function() {
	// Load plugin specific language pack
	tinymce.PluginManager.requireLangPack('tipoarchivos');

	tinymce.create('tinymce.plugins.TipoarchivosPlugin', {
		init : function(ed, url) {
			// Register commands
			ed.addCommand('mceTipoarchivos', function() {
				ed.windowManager.open({
					file : '/sacmicroback/tinymce/jscripts/tiny_mce/plugins/tipoarchivos/tipoarchivos.htm',
					width : 160 + parseInt(ed.getLang('tipoarchivos.delta_width', 0)),
					height : 160 + parseInt(ed.getLang('tipoarchivos.delta_height', 0)),
					inline : 1
				}, {
					plugin_url : url
				});
			});

			ed.addButton('tipoarchivos', {image: url + '/images/tipoarchivos.gif', title : 'tipoarchivos.desc', cmd : 'mceTipoarchivos' });

		},

		getInfo : function() {
			return {
				longname : 'Tipos de archivos',
				author : 'VRS',
				authorurl : 'http://www.indra.es',
				infourl : 'http://www.indra.es',
				version : tinymce.majorVersion + "." + tinymce.minorVersion
			};
		}
	});

	// Register plugin
	tinymce.PluginManager.add('tipoarchivos', tinymce.plugins.TipoarchivosPlugin);
})();