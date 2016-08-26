
(function() {
	// Load plugin specific language pack
	//tinymce.PluginManager.requireLangPack('componentesmicros');

	tinymce.create('tinymce.plugins.ComponentesmicrosPlugin', {
		init : function(ed, url) {
			// Register commands
			ed.addCommand('mceComponentemicro', function() {
				ed.windowManager.open({
					file : '/sacmicroback/listartinycomponentes.do',
					width : 600 + parseInt(ed.getLang('componentesmicros.delta_width', 0)),
					height : 400 + parseInt(ed.getLang('componentesmicros.delta_height', 0)),
					inline : 1,
					scrollbars: "yes"
				}, {
					plugin_url : url
				});
			});

			ed.addButton('componentesmicros', {image: url + '/images/componentes.gif', title : 'Insereix Component', cmd : 'mceComponentemicro' });

		},

		getInfo : function() {
			return {
				longname : 'Componentes',
				author : 'VRS',
				authorurl : 'http://www.indra.es',
				infourl : 'http://www.indra.es',
				version : tinymce.majorVersion + "." + tinymce.minorVersion
			};
		}
	});

	// Register plugin
	tinymce.PluginManager.add('componentesmicros', tinymce.plugins.ComponentesmicrosPlugin);
})();