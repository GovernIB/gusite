/**
 * Created by tcerda on 11/03/2015.
 */
$(document).ready(function () {

    var textarea = $('textarea[name="contenido"]').hide();
    var editor = ace.edit("description");
    editor.setTheme("ace/theme/chrome");
    editor.getSession().setMode("ace/mode/css");
    editor.getSession().setMode("ace/mode/html");
    editor.getSession().setValue(textarea.val());
    editor.getSession().on('change', function(){
        textarea.val(editor.getSession().getValue());
    });

    $('.btnFullScreen').click(function () {
        $("#description").toggleClass('fullScreen');
    });

});