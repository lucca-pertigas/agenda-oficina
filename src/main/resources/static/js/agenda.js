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

    atualizarDuracaoTotalNovo();

    modal.classList.add("ativo");
}


function fecharFormulario() {

    document
        .getElementById("modalAgendamento")
        .classList.remove("ativo");
}


// ========================================
// SERVIÇOS SELECIONADOS
// ========================================

function obterServicosSelecionados(tipo) {

    let seletor;

    if (tipo === "editar") {

        seletor =
            ".editar-servico-checkbox:checked";

    } else {

        seletor =
            ".servico-checkbox:checked";
    }


    return Array.from(
        document.querySelectorAll(seletor)
    ).map(
        checkbox =>
            Number(checkbox.value)
    );
}


// ========================================
// DURAÇÃO TOTAL - NOVO
// ========================================

function atualizarDuracaoTotalNovo() {

    let total = 0;

    const checkboxes =
        document.querySelectorAll(
            ".servico-checkbox:checked"
        );


    checkboxes.forEach(
        checkbox => {

            const duracao =
                Number(
                    checkbox.getAttribute(
                        "data-duracao"
                    )
                );


            if (!Number.isNaN(duracao)) {
                total += duracao;
            }
        }
    );


    const elemento =
        document.getElementById(
            "duracaoTotalNovo"
        );


    if (elemento) {

        elemento.textContent =
            formatarDuracao(total);
    }
}


// ========================================
// DURAÇÃO TOTAL - EDITAR
// ========================================

function atualizarDuracaoTotalEditar() {

    let total = 0;

    const checkboxes =
        document.querySelectorAll(
            ".editar-servico-checkbox:checked"
        );


    checkboxes.forEach(
        checkbox => {

            const duracao =
                Number(
                    checkbox.getAttribute(
                        "data-duracao"
                    )
                );


            if (!Number.isNaN(duracao)) {
                total += duracao;
            }
        }
    );


    const elemento =
        document.getElementById(
            "duracaoTotalEditar"
        );


    if (elemento) {

        elemento.textContent =
            formatarDuracao(total);
    }
}


// ========================================
// FORMATAR DURAÇÃO
// ========================================

function formatarDuracao(minutos) {

    if (!minutos) {
        return "0 min";
    }


    if (minutos < 60) {
        return minutos + " min";
    }


    const horas =
        Math.floor(minutos / 60);

    const restante =
        minutos % 60;


    if (restante === 0) {

        return horas +
            (horas === 1
                ? " hora"
                : " horas");
    }


    return horas +
        (horas === 1
            ? " hora e "
            : " horas e ")
        + restante
        + " min";
}


// ========================================
// CADASTRAR
// ========================================

async function salvarAgendamento() {

    const nomeCliente =
        document
            .getElementById("nomeCliente")
            .value
            .trim();


    const placaVeiculo =
        document
            .getElementById("placaVeiculo")
            .value
            .trim()
            .toUpperCase();


    const tecnicoId =
        document
            .getElementById("tecnico")
            .value;


    const elevadorId =
        document
            .getElementById("elevador")
            .value;


    const servicosIds =
        obterServicosSelecionados(
            "novo"
        );


    const dataHoraInicio =
        document
            .getElementById(
                "dataHoraInicio"
            )
            .value;


    const mensagem =
        document
            .getElementById(
                "mensagemFormulario"
            );


    // ========================================
    // VALIDAÇÕES
    // ========================================

    if (!nomeCliente) {

        mensagem.textContent =
            "Informe o nome do cliente.";

        return;
    }


    if (!placaVeiculo) {

        mensagem.textContent =
            "Informe a placa do veículo.";

        return;
    }


    if (!tecnicoId) {

        mensagem.textContent =
            "Selecione um técnico.";

        return;
    }


    if (!elevadorId) {

        mensagem.textContent =
            "Selecione um elevador.";

        return;
    }


    if (servicosIds.length === 0) {

        mensagem.textContent =
            "Selecione pelo menos um serviço.";

        return;
    }


    if (!dataHoraInicio) {

        mensagem.textContent =
            "Informe a data e o horário.";

        return;
    }


    if (!validarHorarioOficina(
        dataHoraInicio,
        mensagem
    )) {

        return;
    }


    // ========================================
    // DADOS ENVIADOS AO JAVA
    // ========================================

    const dados = {

        nomeCliente:
        nomeCliente,

        placaVeiculo:
        placaVeiculo,

        tecnicoId:
            Number(tecnicoId),

        elevadorId:
            Number(elevadorId),

        servicosIds:
        servicosIds,

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


// ========================================
// LIMPAR FORMULÁRIO NOVO
// ========================================

function limparFormularioNovo() {

    document
        .getElementById("nomeCliente")
        .value = "";


    document
        .getElementById("placaVeiculo")
        .value = "";


    document
        .getElementById("tecnico")
        .value = "";


    document
        .getElementById("elevador")
        .value = "";


    document
        .querySelectorAll(
            ".servico-checkbox"
        )
        .forEach(
            checkbox => {

                checkbox.checked = false;
            }
        );


    document
        .getElementById(
            "dataHoraInicio"
        )
        .value = "";


    atualizarDuracaoTotalNovo();
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
        .getElementById(
            "detalheNomeCliente"
        )
        .textContent =
        card.dataset.nomeCliente || "";


    document
        .getElementById(
            "detalhePlacaVeiculo"
        )
        .textContent =
        card.dataset.placaVeiculo || "";


    document
        .getElementById(
            "detalheServico"
        )
        .textContent =
        card.dataset.servicos;


    document
        .getElementById(
            "detalheTecnico"
        )
        .textContent =
        card.dataset.tecnico;


    document
        .getElementById(
            "detalheElevador"
        )
        .textContent =
        "Elevador " +
        card.dataset.elevador;


    document
        .getElementById(
            "detalheInicio"
        )
        .textContent =
        card.dataset.inicio;


    document
        .getElementById(
            "detalheFim"
        )
        .textContent =
        card.dataset.fim;


    document
        .getElementById(
            "detalheStatus"
        )
        .textContent =
        card.dataset.status;


    document
        .getElementById(
            "modalDetalhes"
        )
        .classList.add("ativo");
}


function fecharDetalhesAgendamento() {

    document
        .getElementById(
            "modalDetalhes"
        )
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


    // CLIENTE

    document
        .getElementById(
            "editarNomeCliente"
        )
        .value =
        card.dataset.nomeCliente || "";


    // PLACA

    document
        .getElementById(
            "editarPlacaVeiculo"
        )
        .value =
        card.dataset.placaVeiculo || "";


    // TÉCNICO

    document
        .getElementById(
            "editarTecnico"
        )
        .value =
        card.dataset.tecnicoId;


    // ELEVADOR

    document
        .getElementById(
            "editarElevador"
        )
        .value =
        card.dataset.elevadorId;


    // SERVIÇOS DO AGENDAMENTO

    const servicosIds =
        card.dataset.servicosIds
            ? card.dataset.servicosIds
                .split(",")
                .filter(
                    id => id !== ""
                )
                .map(Number)
            : [];


    document
        .querySelectorAll(
            ".editar-servico-checkbox"
        )
        .forEach(
            checkbox => {

                checkbox.checked =
                    servicosIds.includes(
                        Number(
                            checkbox.value
                        )
                    );
            }
        );


    atualizarDuracaoTotalEditar();


    // DATA E HORÁRIO

    document
        .getElementById(
            "editarDataHoraInicio"
        )
        .value =
        normalizarDataParaInput(
            card.dataset.dataHoraInicio
        );


    document
        .getElementById(
            "mensagemEdicao"
        )
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


// ========================================
// SALVAR EDIÇÃO
// ========================================

async function salvarEdicaoAgendamento() {

    if (!agendamentoSelecionadoId) {
        return;
    }


    const nomeCliente =
        document
            .getElementById(
                "editarNomeCliente"
            )
            .value
            .trim();


    const placaVeiculo =
        document
            .getElementById(
                "editarPlacaVeiculo"
            )
            .value
            .trim()
            .toUpperCase();


    const tecnicoId =
        document
            .getElementById(
                "editarTecnico"
            )
            .value;


    const elevadorId =
        document
            .getElementById(
                "editarElevador"
            )
            .value;


    const servicosIds =
        obterServicosSelecionados(
            "editar"
        );


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


    // ========================================
    // VALIDAÇÕES
    // ========================================

    if (!nomeCliente) {

        mensagem.textContent =
            "Informe o nome do cliente.";

        return;
    }


    if (!placaVeiculo) {

        mensagem.textContent =
            "Informe a placa do veículo.";

        return;
    }


    if (!tecnicoId) {

        mensagem.textContent =
            "Selecione um técnico.";

        return;
    }


    if (!elevadorId) {

        mensagem.textContent =
            "Selecione um elevador.";

        return;
    }


    if (servicosIds.length === 0) {

        mensagem.textContent =
            "Selecione pelo menos um serviço.";

        return;
    }


    if (!dataHoraInicio) {

        mensagem.textContent =
            "Informe a data e o horário.";

        return;
    }


    if (!validarHorarioOficina(
        dataHoraInicio,
        mensagem
    )) {

        return;
    }


    // ========================================
    // DADOS
    // ========================================

    const dados = {

        nomeCliente:
        nomeCliente,

        placaVeiculo:
        placaVeiculo,

        tecnicoId:
            Number(tecnicoId),

        elevadorId:
            Number(elevadorId),

        servicosIds:
        servicosIds,

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
        horarioEmMinutos >=
        inicioAlmoco
        &&
        horarioEmMinutos <
        fimAlmoco
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
// EVENTOS DOS CHECKBOXES
// ========================================

document.addEventListener(
    "change",
    function (evento) {

        if (
            evento.target
                .classList
                .contains(
                    "servico-checkbox"
                )
        ) {

            atualizarDuracaoTotalNovo();
        }


        if (
            evento.target
                .classList
                .contains(
                    "editar-servico-checkbox"
                )
        ) {

            atualizarDuracaoTotalEditar();
        }
    }
);


// ========================================
// PLACA SEMPRE EM MAIÚSCULO
// ========================================

document.addEventListener(
    "input",
    function (evento) {

        if (
            evento.target.id ===
            "placaVeiculo"
            ||
            evento.target.id ===
            "editarPlacaVeiculo"
        ) {

            evento.target.value =
                evento.target.value
                    .toUpperCase();
        }
    }
);


// ========================================
// NAVEGAÇÃO POR LETRA NOS SERVIÇOS
// ========================================

function ativarNavegacaoPorLetra(
    containerId,
    itemSelector
) {

    const container =
        document.getElementById(
            containerId
        );


    if (!container) {
        return;
    }


    container.setAttribute(
        "tabindex",
        "0"
    );


    container.addEventListener(
        "keydown",
        function (evento) {

            const tecla =
                evento.key
                    .toLowerCase();


            if (
                tecla.length !== 1
                ||
                !/[a-záàâãéèêíïóôõöúç0-9]/i
                    .test(tecla)
            ) {

                return;
            }


            const itens =
                Array.from(
                    container.querySelectorAll(
                        itemSelector
                    )
                );


            const encontrado =
                itens.find(
                    item => {

                        const texto =
                            item
                                .innerText
                                .trim()
                                .toLowerCase();


                        return texto
                            .startsWith(
                                tecla
                            );
                    }
                );


            if (encontrado) {

                encontrado
                    .scrollIntoView({
                        behavior:
                            "smooth",

                        block:
                            "nearest"
                    });


                encontrado
                    .classList
                    .add(
                        "servico-destacado"
                    );


                setTimeout(
                    () => {

                        encontrado
                            .classList
                            .remove(
                                "servico-destacado"
                            );

                    },
                    800
                );
            }
        }
    );
}


// ========================================
// INICIALIZAÇÃO
// ========================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        atualizarDuracaoTotalNovo();

        atualizarDuracaoTotalEditar();


        ativarNavegacaoPorLetra(
            "servicos",
            ".servico-item"
        );


        ativarNavegacaoPorLetra(
            "editarServicos",
            ".servico-item"
        );
    }
);


// ========================================
// FECHAR MODAIS PELO FUNDO
// ========================================

document.addEventListener(
    "click",
    function (evento) {

        if (
            evento.target
                .classList
                .contains(
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


                        if (
                            evento.tipo === "CRIADO"
                            ||
                            evento.tipo === "ATUALIZADO"
                            ||
                            evento.tipo === "CANCELADO"
                            ||
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