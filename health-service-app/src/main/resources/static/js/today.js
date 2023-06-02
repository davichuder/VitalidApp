function setMaxDate() {
    var today = new Date().toISOString().split('T')[0];
    document.getElementById("fecha").setAttribute("max", today);
}
setMaxDate();