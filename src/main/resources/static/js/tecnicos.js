// ========================================================
// NOVO TÉCNICO
// ========================================================

function abrirModalNovoTecnico() {

    document
        .getElementById("novoNomeTecnico")
        .value = "";

    document
        .getElementById("mensagemNovoTecnico")
        .textContent = "";

    document
        .getElementById("modalNovoTecnico")
        .classList.add("ativo");
}


function fecharModalNovoTecnico() {

    document
        .getElementById("modalNovoTecnico")
        .classList.remove("ativo");
}


async function salvarNovoTecnico() {

    const nome =
        document
            .getElementById("novoNomeTecnico")
            .value
            .trim();


    const mensagem =
        document
            .getElementById("mensagemNovoTecnico");


    if (!nome) {

        mensagem.textContent =
            "Informe o nome do técnico.";

        return;
    }


    try {

        const response =
            await fetch(
                "/tecnicos",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            nome: nome,
                            ativo: true
                        })
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            mensagem.textContent =
                resultado.mensagem ||
                "Erro ao cadastrar técnico.";

            return;
        }


        window.location.reload();


    } catch (erro) {

        console.error(
            "Erro ao cadastrar técnico:",
            erro
        );


        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}



// ========================================================
// EDITAR TÉCNICO
// ========================================================

function abrirModalEditarTecnico(botao) {

    const id =
        botao.dataset.id;

    const nome =
        botao.dataset.nome;

    const ativo =
        botao.dataset.ativo === "true";


    document
        .getElementById("editarTecnicoId")
        .value = id;


    document
        .getElementById("editarNomeTecnico")
        .value = nome;


    document
        .getElementById("editarTecnicoAtivo")
        .checked = ativo;


    document
        .getElementById("mensagemEditarTecnico")
        .textContent = "";


    document
        .getElementById("modalEditarTecnico")
        .classList.add("ativo");
}


function fecharModalEditarTecnico() {

    document
        .getElementById("modalEditarTecnico")
        .classList.remove("ativo");
}


async function salvarEdicaoTecnico() {

    const id =
        document
            .getElementById("editarTecnicoId")
            .value;


    const nome =
        document
            .getElementById("editarNomeTecnico")
            .value
            .trim();


    const ativo =
        document
            .getElementById("editarTecnicoAtivo")
            .checked;


    const mensagem =
        document
            .getElementById("mensagemEditarTecnico");


    if (!nome) {

        mensagem.textContent =
            "Informe o nome do técnico.";

        return;
    }


    try {

        const response =
            await fetch(
                `/tecnicos/${id}`,
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
                "Erro ao atualizar técnico.";

            return;
        }


        window.location.reload();


    } catch (erro) {

        console.error(
            "Erro ao editar técnico:",
            erro
        );


        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}



// ========================================================
// DESATIVAR TÉCNICO
// ========================================================

async function desativarTecnico(botao) {

    const id =
        botao.dataset.id;


    const confirmar =
        window.confirm(
            "Deseja realmente desativar este técnico?"
        );


    if (!confirmar) {
        return;
    }


    try {

        const response =
            await fetch(
                `/tecnicos/${id}/desativar`,
                {
                    method: "PUT"
                }
            );


        if (!response.ok) {

            const resultado =
                await response.json();


            alert(
                resultado.mensagem ||
                "Erro ao desativar técnico."
            );

            return;
        }


        window.location.reload();


    } catch (erro) {

        console.error(
            "Erro ao desativar técnico:",
            erro
        );


        alert(
            "Erro ao comunicar com o servidor."
        );
    }
}



// ========================================================
// ABRIR INDISPONIBILIDADES
// ========================================================

function abrirModalIndisponibilidade(botao) {

    const tecnicoId =
        botao.dataset.id;

    const tecnicoNome =
        botao.dataset.nome;


    document
        .getElementById("indisponibilidadeTecnicoId")
        .value =
        tecnicoId;


    document
        .getElementById("indisponibilidadeTecnicoNome")
        .textContent =
        tecnicoNome;


    document
        .getElementById("indisponibilidadeDataInicio")
        .value = "";


    document
        .getElementById("indisponibilidadeDataFim")
        .value = "";


    document
        .getElementById("indisponibilidadeMotivo")
        .value = "";


    document
        .getElementById("mensagemIndisponibilidade")
        .textContent = "";


    document
        .getElementById("modalIndisponibilidade")
        .classList
        .add("ativo");


    carregarIndisponibilidades(
        tecnicoId
    );
}


function fecharModalIndisponibilidade() {

    document
        .getElementById("modalIndisponibilidade")
        .classList
        .remove("ativo");
}



// ========================================================
// SALVAR INDISPONIBILIDADE
// ========================================================

async function salvarIndisponibilidade() {

    const tecnicoId =
        document
            .getElementById("indisponibilidadeTecnicoId")
            .value;


    const dataInicio =
        document
            .getElementById("indisponibilidadeDataInicio")
            .value;


    const dataFim =
        document
            .getElementById("indisponibilidadeDataFim")
            .value;


    const motivo =
        document
            .getElementById("indisponibilidadeMotivo")
            .value
            .trim();


    const mensagem =
        document
            .getElementById("mensagemIndisponibilidade");


    if (!tecnicoId) {

        mensagem.textContent =
            "Técnico não informado.";

        return;
    }


    if (!dataInicio) {

        mensagem.textContent =
            "Informe a data inicial.";

        return;
    }


    if (!dataFim) {

        mensagem.textContent =
            "Informe a data final.";

        return;
    }


    if (dataFim < dataInicio) {

        mensagem.textContent =
            "A data final não pode ser anterior à data inicial.";

        return;
    }


    if (!motivo) {

        mensagem.textContent =
            "Informe o motivo da indisponibilidade.";

        return;
    }


    const dados = {

        tecnico: {
            id: Number(tecnicoId)
        },

        dataInicio:
        dataInicio,

        dataFim:
        dataFim,

        motivo:
        motivo
    };


    try {

        const response =
            await fetch(
                "/indisponibilidades-tecnicos",
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
                "Erro ao cadastrar indisponibilidade.";

            return;
        }


        mensagem.textContent = "";


        document
            .getElementById("indisponibilidadeDataInicio")
            .value = "";


        document
            .getElementById("indisponibilidadeDataFim")
            .value = "";


        document
            .getElementById("indisponibilidadeMotivo")
            .value = "";


        await carregarIndisponibilidades(
            tecnicoId
        );


    } catch (erro) {

        console.error(
            "Erro ao cadastrar indisponibilidade:",
            erro
        );


        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}



// ========================================================
// LISTAR INDISPONIBILIDADES
// ========================================================

async function carregarIndisponibilidades(
    tecnicoId
) {

    const container =
        document.getElementById(
            "listaIndisponibilidades"
        );


    container.innerHTML =
        `<p class="carregando">
            Carregando...
        </p>`;


    try {

        const response =
            await fetch(
                `/indisponibilidades-tecnicos/tecnico/${tecnicoId}`
            );


        if (!response.ok) {

            container.innerHTML =
                `<p class="mensagem">
                    Erro ao carregar indisponibilidades.
                </p>`;

            return;
        }


        const indisponibilidades =
            await response.json();


        if (indisponibilidades.length === 0) {

            container.innerHTML =
                `
                <div class="nenhuma-indisponibilidade">

                    Nenhuma indisponibilidade cadastrada.

                </div>
                `;

            return;
        }


        container.innerHTML = "";


        indisponibilidades.forEach(
            indisponibilidade => {

                const item =
                    document.createElement(
                        "div"
                    );


                item.classList.add(
                    "indisponibilidade-item"
                );


                item.innerHTML = `
                    <div class="indisponibilidade-info">

                        <strong>
                            ${escapeHtml(
                    indisponibilidade.motivo ||
                    "Indisponível"
                )}
                        </strong>

                        <span>
                            ${formatarData(
                    indisponibilidade.dataInicio
                )}
                            até
                            ${formatarData(
                    indisponibilidade.dataFim
                )}
                        </span>

                    </div>

                    <button
                        type="button"
                        class="btn btn-small btn-danger"
                        onclick="excluirIndisponibilidade(
                            ${indisponibilidade.id},
                            ${tecnicoId}
                        )">

                        Excluir

                    </button>
                `;


                container.appendChild(
                    item
                );
            }
        );


    } catch (erro) {

        console.error(
            "Erro ao carregar indisponibilidades:",
            erro
        );


        container.innerHTML =
            `
            <p class="mensagem">
                Erro ao comunicar com o servidor.
            </p>
            `;
    }
}



// ========================================================
// EXCLUIR INDISPONIBILIDADE
// ========================================================

async function excluirIndisponibilidade(
    id,
    tecnicoId
) {

    const confirmar =
        window.confirm(
            "Deseja realmente excluir esta indisponibilidade?"
        );


    if (!confirmar) {
        return;
    }


    try {

        const response =
            await fetch(
                `/indisponibilidades-tecnicos/${id}`,
                {
                    method: "DELETE"
                }
            );


        if (!response.ok) {

            let mensagem =
                "Erro ao excluir indisponibilidade.";


            try {

                const resultado =
                    await response.json();


                mensagem =
                    resultado.mensagem ||
                    mensagem;

            } catch (erro) {
                // resposta sem JSON
            }


            alert(mensagem);

            return;
        }


        await carregarIndisponibilidades(
            tecnicoId
        );


    } catch (erro) {

        console.error(
            "Erro ao excluir indisponibilidade:",
            erro
        );


        alert(
            "Erro ao comunicar com o servidor."
        );
    }
}



// ========================================================
// FORMATAR DATA
// ========================================================

function formatarData(data) {

    if (!data) {
        return "";
    }


    const partes =
        data.split("-");


    return (
        partes[2]
        + "/"
        + partes[1]
        + "/"
        + partes[0]
    );
}



// ========================================================
// SEGURANÇA HTML
// ========================================================

function escapeHtml(texto) {

    const elemento =
        document.createElement("div");


    elemento.textContent =
        texto ?? "";


    return elemento.innerHTML;
}