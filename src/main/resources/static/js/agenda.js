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

    document.getElementById("tecnico").value = "";
    document.getElementById("elevador").value = "";
    document.getElementById("servico").value = "";
    document.getElementById("dataHoraInicio").value = "";
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
// CARD
// ========================================

function adicionarAgendamentoNaTela(
    agendamento
) {

    if (!agendamentoPertenceAoDiaAtual(
        agendamento
    )) {
        return;
    }


    removerCardAgendamento(
        agendamento.id
    );


    const horarioFaixa =
        calcularFaixaHorario(
            agendamento.dataHoraInicio
        );


    const seletor =
        `.agenda-celula` +
        `[data-elevador-id="${agendamento.elevadorId}"]` +
        `[data-horario="${horarioFaixa}"]`;


    const celula =
        document.querySelector(seletor);


    if (!celula) {

        console.log(
            "Célula não encontrada:",
            seletor
        );

        return;
    }


    const card =
        document.createElement("div");


    card.classList.add(
        "agendamento-card"
    );


    adicionarClasseStatus(
        card,
        agendamento.status
    );


    card.dataset.agendamentoId =
        agendamento.id;

    card.dataset.tecnicoId =
        agendamento.tecnicoId;

    card.dataset.tecnico =
        agendamento.tecnicoNome;

    card.dataset.elevadorId =
        agendamento.elevadorId;

    card.dataset.elevador =
        agendamento.elevadorNumero;

    card.dataset.servicoId =
        agendamento.servicoId;

    card.dataset.servico =
        agendamento.servicoNome;

    card.dataset.dataHoraInicio =
        agendamento.dataHoraInicio;

    card.dataset.inicio =
        formatarDataHora(
            agendamento.dataHoraInicio
        );

    card.dataset.fim =
        formatarDataHora(
            agendamento.dataHoraFim
        );

    card.dataset.status =
        agendamento.status;


    card.onclick =
        function () {

            abrirDetalhesAgendamento(
                card
            );
        };


    const altura =
        (
            agendamento.duracaoMinutos
            / 30
        ) * 70;


    const deslocamento =
        (
            agendamento.minutoInicio
            % 30
        )
        / 30
        * 70;


    card.style.height =
        altura + "px";

    card.style.top =
        deslocamento + "px";


    const inicio =
        extrairHorario(
            agendamento.dataHoraInicio
        );


    const fim =
        extrairHorario(
            agendamento.dataHoraFim
        );


    card.innerHTML = `
        <strong>
            ${escapeHtml(
        agendamento.servicoNome
    )}
        </strong>

        <span>
            ${escapeHtml(
        agendamento.tecnicoNome
    )}
        </span>

        <small>
            ${inicio} - ${fim}
        </small>
    `;


    celula.appendChild(card);
}


function atualizarAgendamentoNaTela(
    agendamento
) {

    removerCardAgendamento(
        agendamento.id
    );


    adicionarAgendamentoNaTela(
        agendamento
    );
}


function removerCardAgendamento(id) {

    const card =
        buscarCardAgendamento(id);


    if (card) {
        card.remove();
    }
}


function buscarCardAgendamento(id) {

    return document.querySelector(
        `.agendamento-card` +
        `[data-agendamento-id="${id}"]`
    );
}


// ========================================
// STATUS
// ========================================

function adicionarClasseStatus(
    card,
    status
) {

    if (status === "AGENDADO") {

        card.classList.add(
            "status-agendado"
        );
    }


    if (status === "CONCLUIDO") {

        card.classList.add(
            "status-concluido"
        );
    }


    if (status === "CANCELADO") {

        card.classList.add(
            "status-cancelado"
        );
    }
}


// ========================================
// DATA / HORÁRIO
// ========================================

function calcularFaixaHorario(
    dataHora
) {

    const horario =
        dataHora.substring(11, 16);


    const partes =
        horario.split(":");


    const hora =
        partes[0];


    const minuto =
        Number(partes[1]);


    const minutoFaixa =
        minuto < 30
            ? "00"
            : "30";


    return (
        hora +
        ":" +
        minutoFaixa
    );
}


function agendamentoPertenceAoDiaAtual(
    agendamento
) {

    const grid =
        document
            .getElementById(
                "agendaGrid"
            );


    if (!grid) {
        return false;
    }


    const dataAgenda =
        grid.dataset.dataAgenda;


    const dataAgendamento =
        agendamento
            .dataHoraInicio
            .substring(0, 10);


    return (
        dataAgenda ===
        dataAgendamento
    );
}


function extrairHorario(
    dataHora
) {

    return dataHora.substring(
        11,
        16
    );
}


function formatarDataHora(
    dataHora
) {

    const data =
        dataHora.substring(0, 10);

    const horario =
        dataHora.substring(11, 16);


    const partes =
        data.split("-");


    return (
        partes[2] +
        "/" +
        partes[1] +
        "/" +
        partes[0] +
        " " +
        horario
    );
}


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
// SEGURANÇA DO HTML
// ========================================

function escapeHtml(texto) {

    const elemento =
        document.createElement(
            "div"
        );


    elemento.textContent =
        texto ?? "";


    return elemento.innerHTML;
}


// ========================================
// WEBSOCKET
// ========================================

const client =
    new StompJs.Client({

        brokerURL:
            `ws://${window.location.host}/ws`,

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


                        if (
                            evento.tipo ===
                            "CRIADO"
                        ) {

                            adicionarAgendamentoNaTela(
                                evento.agendamento
                            );
                        }


                        if (
                            evento.tipo ===
                            "ATUALIZADO"
                        ) {

                            atualizarAgendamentoNaTela(
                                evento.agendamento
                            );
                        }


                        if (
                            evento.tipo ===
                            "CANCELADO"
                        ) {

                            removerCardAgendamento(
                                evento
                                    .agendamento
                                    .id
                            );
                        }


                        if (
                            evento.tipo ===
                            "CONCLUIDO"
                        ) {

                            atualizarAgendamentoNaTela(
                                evento.agendamento
                            );
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
                    frame
                );
            }
    });


client.activate();