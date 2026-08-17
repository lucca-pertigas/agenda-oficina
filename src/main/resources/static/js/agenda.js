let agendamentoSelecionadoId = null;


// ========================================
// MODAL NOVO
// ========================================

function abrirFormulario() {

    const modal =
        document.getElementById(
            "modalAgendamento"
        );

    document
        .getElementById("mensagemFormulario")
        .textContent = "";

    modal.classList.add("ativo");
}


function fecharFormulario() {

    document
        .getElementById("modalAgendamento")
        .classList.remove("ativo");
}


// ========================================
// CADASTRAR
// ========================================

async function salvarAgendamento() {

    const tecnicoId =
        document.getElementById("tecnico").value;

    const elevadorId =
        document.getElementById("elevador").value;

    const servicoId =
        document.getElementById("servico").value;

    const dataHoraInicio =
        document
            .getElementById("dataHoraInicio")
            .value;

    const mensagem =
        document
            .getElementById(
                "mensagemFormulario"
            );


    if (
        !tecnicoId ||
        !elevadorId ||
        !servicoId ||
        !dataHoraInicio
    ) {

        mensagem.textContent =
            "Preencha todos os campos.";

        return;
    }


    if (!validarHorarioOficina(
        dataHoraInicio,
        mensagem
    )) {
        return;
    }


    const dados = {

        tecnicoId:
            Number(tecnicoId),

        elevadorId:
            Number(elevadorId),

        servicoId:
            Number(servicoId),

        dataHoraInicio:
        dataHoraInicio
    };


    try {

        const response =
            await fetch(
                "/agendamentos",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(dados)
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            mensagem.textContent =
                resultado.mensagem ||
                "Erro ao criar agendamento.";

            return;
        }


        limparFormularioNovo();

        fecharFormulario();

        /*
         * Não precisamos montar o card aqui.
         *
         * O backend divide corretamente:
         * - manhã
         * - almoço
         * - tarde
         * - próximo dia
         *
         * O WebSocket fará a atualização da tela.
         */

    } catch (erro) {

        console.error(
            "Erro ao salvar:",
            erro
        );

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


function limparFormularioNovo() {

    document
        .getElementById("tecnico")
        .value = "";

    document
        .getElementById("elevador")
        .value = "";

    document
        .getElementById("servico")
        .value = "";

    document
        .getElementById("dataHoraInicio")
        .value = "";
}


// ========================================
// DETALHES
// ========================================

function abrirDetalhesAgendamento(card) {

    agendamentoSelecionadoId =
        Number(
            card.dataset.agendamentoId
        );


    document
        .getElementById("detalheServico")
        .textContent =
        card.dataset.servico;


    document
        .getElementById("detalheTecnico")
        .textContent =
        card.dataset.tecnico;


    document
        .getElementById("detalheElevador")
        .textContent =
        "Elevador " +
        card.dataset.elevador;


    document
        .getElementById("detalheInicio")
        .textContent =
        card.dataset.inicio;


    document
        .getElementById("detalheFim")
        .textContent =
        card.dataset.fim;


    document
        .getElementById("detalheStatus")
        .textContent =
        card.dataset.status;


    document
        .getElementById("modalDetalhes")
        .classList.add("ativo");
}


function fecharDetalhesAgendamento() {

    document
        .getElementById("modalDetalhes")
        .classList.remove("ativo");
}


// ========================================
// EDITAR
// ========================================

function abrirEdicaoAgendamento() {

    const card =
        buscarCardAgendamento(
            agendamentoSelecionadoId
        );


    if (!card) {

        alert(
            "Não foi possível localizar o agendamento."
        );

        return;
    }


    document
        .getElementById("editarTecnico")
        .value =
        card.dataset.tecnicoId;


    document
        .getElementById("editarElevador")
        .value =
        card.dataset.elevadorId;


    document
        .getElementById("editarServico")
        .value =
        card.dataset.servicoId;


    document
        .getElementById(
            "editarDataHoraInicio"
        )
        .value =
        normalizarDataParaInput(
            card.dataset.dataHoraInicio
        );


    document
        .getElementById("mensagemEdicao")
        .textContent = "";


    fecharDetalhesAgendamento();


    document
        .getElementById(
            "modalEditarAgendamento"
        )
        .classList.add("ativo");
}


function fecharEdicaoAgendamento() {

    document
        .getElementById(
            "modalEditarAgendamento"
        )
        .classList.remove("ativo");
}


async function salvarEdicaoAgendamento() {

    if (!agendamentoSelecionadoId) {
        return;
    }


    const tecnicoId =
        document
            .getElementById("editarTecnico")
            .value;


    const elevadorId =
        document
            .getElementById("editarElevador")
            .value;


    const servicoId =
        document
            .getElementById("editarServico")
            .value;


    const dataHoraInicio =
        document
            .getElementById(
                "editarDataHoraInicio"
            )
            .value;


    const mensagem =
        document
            .getElementById(
                "mensagemEdicao"
            );


    if (
        !tecnicoId ||
        !elevadorId ||
        !servicoId ||
        !dataHoraInicio
    ) {

        mensagem.textContent =
            "Preencha todos os campos.";

        return;
    }


    if (!validarHorarioOficina(
        dataHoraInicio,
        mensagem
    )) {
        return;
    }


    const dados = {

        tecnicoId:
            Number(tecnicoId),

        elevadorId:
            Number(elevadorId),

        servicoId:
            Number(servicoId),

        dataHoraInicio:
        dataHoraInicio
    };


    try {

        const response =
            await fetch(
                `/agendamentos/${agendamentoSelecionadoId}`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(dados)
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            mensagem.textContent =
                resultado.mensagem ||
                "Erro ao atualizar agendamento.";

            return;
        }


        fecharEdicaoAgendamento();

    } catch (erro) {

        console.error(
            "Erro ao editar:",
            erro
        );

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


// ========================================
// CONCLUIR
// ========================================

async function concluirAgendamento() {

    if (!agendamentoSelecionadoId) {
        return;
    }


    try {

        const response =
            await fetch(
                `/agendamentos/${agendamentoSelecionadoId}/concluir`,
                {
                    method: "PUT"
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            alert(
                resultado.mensagem ||
                "Não foi possível concluir."
            );

            return;
        }


        fecharDetalhesAgendamento();

    } catch (erro) {

        console.error(
            "Erro ao concluir:",
            erro
        );

        alert(
            "Erro ao comunicar com o servidor."
        );
    }
}


// ========================================
// CANCELAR
// ========================================

async function cancelarAgendamento() {

    if (!agendamentoSelecionadoId) {
        return;
    }


    const confirmar =
        window.confirm(
            "Deseja realmente cancelar este agendamento?"
        );


    if (!confirmar) {
        return;
    }


    try {

        const response =
            await fetch(
                `/agendamentos/${agendamentoSelecionadoId}/cancelar`,
                {
                    method: "PUT"
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            alert(
                resultado.mensagem ||
                "Não foi possível cancelar."
            );

            return;
        }


        fecharDetalhesAgendamento();

    } catch (erro) {

        console.error(
            "Erro ao cancelar:",
            erro
        );

        alert(
            "Erro ao comunicar com o servidor."
        );
    }
}


// ========================================
// VALIDAÇÃO DO HORÁRIO
// ========================================

function validarHorarioOficina(
    dataHoraInicio,
    elementoMensagem
) {

    const partes =
        dataHoraInicio.split("T");


    if (partes.length < 2) {

        elementoMensagem.textContent =
            "Data e horário inválidos.";

        return false;
    }


    const horario =
        partes[1]
            .substring(0, 5)
            .split(":");


    const hora =
        Number(horario[0]);

    const minuto =
        Number(horario[1]);


    if (
        Number.isNaN(hora) ||
        Number.isNaN(minuto)
    ) {

        elementoMensagem.textContent =
            "Horário inválido.";

        return false;
    }


    const horarioEmMinutos =
        hora * 60 + minuto;


    const abertura =
        8 * 60;

    const inicioAlmoco =
        12 * 60;

    const fimAlmoco =
        13 * 60;

    const fechamento =
        17 * 60;


    if (
        horarioEmMinutos <
        abertura
    ) {

        elementoMensagem.textContent =
            "A oficina abre às 08:00.";

        return false;
    }


    if (
        horarioEmMinutos >=
        fechamento
    ) {

        elementoMensagem.textContent =
            "A oficina fecha às 17:00.";

        return false;
    }


    if (
        horarioEmMinutos >= inicioAlmoco &&
        horarioEmMinutos < fimAlmoco
    ) {

        elementoMensagem.textContent =
            "Não é possível iniciar um serviço entre 12:00 e 13:00.";

        return false;
    }


    elementoMensagem.textContent = "";

    return true;
}


// ========================================
// BUSCAR CARD
// ========================================

function buscarCardAgendamento(id) {

    return document.querySelector(
        `.agendamento-card[data-agendamento-id="${id}"]`
    );
}


// ========================================
// DATA / HORÁRIO
// ========================================

function normalizarDataParaInput(
    dataHora
) {

    if (!dataHora) {
        return "";
    }


    return dataHora.substring(
        0,
        16
    );
}


// ========================================
// FECHAR MODAIS PELO FUNDO
// ========================================

document.addEventListener(
    "click",
    function (evento) {

        if (
            evento.target.classList.contains(
                "modal-overlay"
            )
        ) {

            evento.target
                .classList
                .remove("ativo");
        }
    }
);


// ========================================
// WEBSOCKET
// ========================================

const protocoloWebSocket =
    window.location.protocol === "https:"
        ? "wss"
        : "ws";


const client =
    new StompJs.Client({

        brokerURL:
            `${protocoloWebSocket}://${window.location.host}/ws`,

        reconnectDelay:
            5000,


        onConnect:
            function () {

                console.log(
                    "WebSocket conectado"
                );


                client.subscribe(
                    "/topic/agendamentos",

                    function (message) {

                        const evento =
                            JSON.parse(
                                message.body
                            );


                        console.log(
                            "Evento recebido:",
                            evento
                        );


                        /*
                         * IMPORTANTE:
                         *
                         * Não criamos mais o card
                         * diretamente pelo JavaScript.
                         *
                         * O backend monta os trechos
                         * corretamente considerando:
                         *
                         * 08:00 - 12:00
                         * 12:00 - 13:00 almoço
                         * 13:00 - 17:00
                         * continuação no próximo dia
                         *
                         * Por isso recarregamos a agenda.
                         */

                        if (
                            evento.tipo === "CRIADO" ||
                            evento.tipo === "ATUALIZADO" ||
                            evento.tipo === "CANCELADO" ||
                            evento.tipo === "CONCLUIDO"
                        ) {

                            window.location.reload();
                        }
                    }
                );
            },


        onWebSocketError:
            function (erro) {

                console.error(
                    "Erro WebSocket:",
                    erro
                );
            },


        onStompError:
            function (frame) {

                console.error(
                    "Erro STOMP:",
                    frame.headers?.message
                );

                console.error(
                    frame.body
                );
            }
    });


client.activate();