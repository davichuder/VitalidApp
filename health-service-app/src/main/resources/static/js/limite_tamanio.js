function validarTamanoArchivo() {
  var input = document.getElementById("formFile");
  var files = input.files;
  var maxTamano = 1048576; // 1MB en bytes
  if (files.length > 0) {
    var fileSize = files[0].size;
    if (fileSize > maxTamano) {
      input.value = null;
      document.getElementById("mensaje-error").innerHTML = "El archivo seleccionado es demasiado grande. Por favor, seleccione un archivo de menos de 1MB.";
      document.getElementById("mensaje-error").style.color = "red";
    } else {
      document.getElementById("mensaje-error").innerHTML = "Archivo seleccionado correctamente.";
      document.getElementById("mensaje-error").style.color = "green";
    }
  }
}