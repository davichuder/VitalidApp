function actualizarVistaPrevia() {
    var archivoSeleccionado = document.getElementById("formFile").files[0];
    var vistaPrevia = document.getElementById("previewImg");
    var lector = new FileReader();
    lector.onloadend = function () {
        vistaPrevia.src = lector.result;
    }
    if (archivoSeleccionado) {
        lector.readAsDataURL(archivoSeleccionado);
    } else {
        vistaPrevia.src = "/img/defaultProfile.jpg";
    }
}
