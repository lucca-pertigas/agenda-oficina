function abrirFormulario() {
    const modal = document.getElementById("modalAgendamento");

    if (modal) {
        modal.classList.add("ativo");
    }
}

function fecharFormulario() {
    const modal = document.getElementById("modalAgendamento");

    if (modal) {
        modal.classList.remove("ativo");
    }
}