function abrirModalNovoModelo() {

    document
        .getElementById("novoNomeModelo")
        .value = "";

    document
        .getElementById("mensagemNovoModelo")
        .textContent = "";

    document
        .getElementById("modalNovoModelo")
        .classList.add("ativo");
}


function fecharModalNovoModelo() {

    document
        .getElementById("modalNovoModelo")
        .classList.remove("ativo");
}


// ========================================
// SALVAR NOVO MODELO
// ========================================

async function salvarNovoModelo() {

    const nome =
        document
            .getElementById("novoNomeModelo")
            .value
            .trim();

    const mensagem =
        document
            .getElementById("mensagemNovoModelo");


    if (!nome) {

        mensagem.textContent =
            "Informe o nome do modelo.";

        return;
    }


    try {

        const response =
            await fetch(
                "/modelos-veiculo",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            nome: nome
                        })
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            mensagem.textContent =
                resultado.mensagem ||
                "Erro ao cadastrar modelo.";

            return;
        }


        fecharModalNovoModelo();

        window.location.reload();


    } catch (erro) {

        console.error(
            "Erro ao cadastrar modelo:",
            erro
        );

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


// ========================================
// ABRIR MODAL EDITAR
// ========================================

function abrirModalEditarModelo(botao) {

    const id =
        botao.dataset.id;

    const nome =
        botao.dataset.nome;

    const ativo =
        botao.dataset.ativo === "true";


    document
        .getElementById("editarModeloId")
        .value = id;


    document
        .getElementById("editarNomeModelo")
        .value = nome;


    document
        .getElementById("editarModeloAtivo")
        .checked = ativo;


    document
        .getElementById("mensagemEditarModelo")
        .textContent = "";


    document
        .getElementById("modalEditarModelo")
        .classList.add("ativo");
}


function fecharModalEditarModelo() {

    document
        .getElementById("modalEditarModelo")
        .classList.remove("ativo");
}


// ========================================
// SALVAR EDIÇÃO
// ========================================

async function salvarEdicaoModelo() {

    const id =
        document
            .getElementById("editarModeloId")
            .value;


    const nome =
        document
            .getElementById("editarNomeModelo")
            .value
            .trim();


    const ativo =
        document
            .getElementById("editarModeloAtivo")
            .checked;


    const mensagem =
        document
            .getElementById("mensagemEditarModelo");


    if (!nome) {

        mensagem.textContent =
            "Informe o nome do modelo.";

        return;
    }


    try {

        const response =
            await fetch(
                `/modelos-veiculo/${id}`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            nome: nome,
                            ativo: ativo
                        })
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            mensagem.textContent =
                resultado.mensagem ||
                "Erro ao atualizar modelo.";

            return;
        }


        fecharModalEditarModelo();

        window.location.reload();


    } catch (erro) {

        console.error(
            "Erro ao atualizar modelo:",
            erro
        );

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


// ========================================
// DESATIVAR MODELO
// ========================================

async function desativarModelo(botao) {

    const id =
        botao.dataset.id;


    const confirmar =
        window.confirm(
            "Deseja realmente desativar este modelo?"
        );


    if (!confirmar) {
        return;
    }


    try {

        const response =
            await fetch(
                `/modelos-veiculo/${id}/desativar`,
                {
                    method: "PUT"
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            alert(
                resultado.mensagem ||
                "Erro ao desativar modelo."
            );

            return;
        }


        window.location.reload();


    } catch (erro) {

        console.error(
            "Erro ao desativar modelo:",
            erro
        );

        alert(
            "Erro ao comunicar com o servidor."
        );
    }
}


// ========================================
// FECHAR MODAL AO CLICAR NO FUNDO
// ========================================

document.addEventListener(
    "click",
    function (evento) {

        if (
            evento.target
                .classList
                .contains("modal-overlay")
        ) {

            evento.target
                .classList
                .remove("ativo");
        }
    }
);