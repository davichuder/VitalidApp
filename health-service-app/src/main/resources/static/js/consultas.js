

var nombreProvincias = document.getElementById("provincias");
var nombreEspecialidades = document.getElementById("especialidades");


nombreProvincias.querySelectorAll("option").forEach(function(opcion) {
  var valor = opcion.value;
  valor = valor.toLowerCase().replace(/_/g, " ");
  valor = valor.replace(/\b[a-z]/g, function(letra) {
    return letra.toUpperCase();
  });
  opcion.text = valor;
});

nombreEspecialidades.querySelectorAll("option").forEach(function(opcion) {
  var valor = opcion.value;
  valor = valor.toLowerCase().replace(/_/g, " ");
  valor = valor.replace(/\b[a-z]/g, function(letra) {
    return letra.toUpperCase();
  });
  opcion.text = valor;
});


