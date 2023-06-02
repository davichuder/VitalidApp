$(function cargarDias() {
    var fechasDisponibles = lista;
    $("#calendario").datepicker({
      beforeShowDay: function (date) {
        var dateString = $.datepicker.formatDate('yy-mm-dd', date);
        return [fechasDisponibles.includes(dateString)];
      },
      dateFormat: 'yy-mm-dd'
    });
  });