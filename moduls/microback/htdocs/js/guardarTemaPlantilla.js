/**
 * Created by brújula on 25/02/2015.
 */
$(document).ready(function () {

    $(".btnGuardar").click(function() {
        var form = $(".accion");
        form.val("guardar");
        form.submit();
    });

    $("#tipus").change(function () {
        var sel = $("#tipus").find(":selected").val();
        $.get("/sacmicroback/ajaxPlantillasPer.do", { select: sel }, pintarRespuesta);
    });

    var id = $("#formulario").children()[0].value;
    if (id === "") {
        $(".subList").hide();
    }

    $("#guardarArxiu").click(function () {
        var idArxiu = $("input[name=idArxiu]");
        var operacion = $("input[name=operacion]");
        if (idArxiu.val() == '0') {
            operacion.val('crear');
        } else {
            operacion.val('modificar');
        }
        var accFormLista = document.getElementById('accFormularioLista');
        accFormLista.submit();
    });

});

function pintarRespuesta(data) {
    $(".par").remove();
    var array = JSON.parse(data);
    var codi = "";
    array.forEach(function(entry) {
        codi += "<tr class='par'>";
        codi += "<td class='etiqueta'></td>";
        codi += "<td>";
        codi += "<a href='perPlantillasEdita.do?plan=" + entry.plantilla + "&pp=" + entry.id + "'>" + entry.titulo;
        codi += "</td></a></tr>";
    });
    $(".edicio").append(codi);
};

