/**
 * Created by bry-at4 on 16/02/2015.
 */

var ie = document.all;
var dom = document.getElementById && !document.all;

function posicionOffset(obj,tipo) {
    var totalOffset = (tipo == "left") ? obj.offsetLeft : obj.offsetTop;
    var parentEl = obj.offsetParent;
    while (parentEl != null){
        totalOffset = (tipo == "left") ? totalOffset+parentEl.offsetLeft : totalOffset+parentEl.offsetTop;
        parentEl = parentEl.offsetParent;
    }
    return totalOffset;
}

function voreMenu(obj, e, menuID) {
    subMenu = document.getElementById(menuID);
    if (typeof subMenu != "undefined") subMenu.style.display = 'none';
    clearhidemenu();
    // evitamos la expansión del evento
    if (window.event) event.cancelBubble = true;
    else if (e.stopPropagation) e.stopPropagation();
    // buscamos posición de la opcion
    subMenu.x = posicionOffset(obj.parentNode, "left");
    subMenu.y = posicionOffset(obj.parentNode, "top");
    subMenu.w = obj.parentNode.offsetWidth;
    // mostramos el submenu y posicionamos
    subMenu.style.display = 'block';
    subMenu.style.left = subMenu.x + "px";
    subMenu.style.top = subMenu.y + "px";
    subMenu.style.width = subMenu.w - 2 + "px";
    subMenu.onmouseover = clearhidemenu;
    subMenu.onmouseout = ie ? function() { dynamichide(event); } : function(event) { dynamichide(event); }
    subMenu.onclick = function() { subMenu.style.display = 'none'; };
    // iframe
    if (typeof document.body.style.maxHeight == "undefined") {
        if(document.getElementById("menuIframe") == null) {
            iframe = document.createElement("iframe");
            iframe.setAttribute("id","menuIframe");
            iframe.setAttribute("scrolling","no");
            iframe.setAttribute("frameBorder","0");
            iframe.src = "iframe.html";
            document.body.appendChild(iframe);
        }
        iframe = document.getElementById("menuIframe");
        iframe.style.display = 'block';
        iframe.style.left = subMenu.x + "px";
        iframe.style.top = subMenu.y + "px";
        iframe.style.width = subMenu.w + "px";
        iframe.style.height = subMenu.offsetHeight + "px";
    }
}

function dynamichide(e) {
    if (ie && !subMenu.contains(e.toElement)) delayhidemenu();
    else if (dom && e.currentTarget != e.relatedTarget) delayhidemenu();
}

function delayhidemenu() {
    delayhide = setTimeout("esconderMenu()",100);
}

function esconderMenu() {
    subMenu.style.display='none';
    if(document.getElementById("menuIframe") != null) {
        iframe.style.display='none';
    }
}

function clearhidemenu() {
    if (typeof delayhide != "undefined") clearTimeout(delayhide);
}