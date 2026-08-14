function abrirModalNovoServico() {

    document
        .getElementById("novoCodigoServico")
        .value = "";

    document
        .getElementById("novoNomeServico")
        .value = "";

    document
        .getElementById("novaDuracaoServico")
        .value = "";

    document
        .getElementById("mensagemNovoServico")
        .textContent = "";

    document
        .getElementById("modalNovoServico")
        .classList.add("ativo");
}


function fecharModalNovoServico() {

    document
        .getElementById("modalNovoServico")
        .classList.remove("ativo");
}


async function salvarNovoServico() {

    const codigo =
        document
            .getElementById("novoCodigoServico")
            .value;

    const nome =
        document
            .getElementById("novoNomeServico")
            .value
            .trim();

    const duracaoMinutos =
        document
            .getElementById("novaDuracaoServico")
            .value;

    const mensagem =
        document
            .getElementById("mensagemNovoServico");


    if (!codigo || Number(codigo) <= 0) {

        mensagem.textContent =
            "Informe um código válido.";

        return;
    }


    if (!nome) {

        mensagem.textContent =
            "Informe o nome do serviço.";

        return;
    }


    if (!duracaoMinutos ||
        Number(duracaoMinutos) <= 0) {

        mensagem.textContent =
            "Informe uma duração válida.";

        return;
    }


    try {

        const response =
            await fetch(
                "/servicos",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            codigo: Number(codigo),
                            nome: nome,
                            duracaoMinutos:
                                Number(duracaoMinutos),
                            ativo: true
                        })
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            mensagem.textContent =
                resultado.mensagem ||
                "Erro ao cadastrar serviço.";

            return;
        }


        window.location.reload();

    } catch (erro) {

        console.error(
            "Erro ao cadastrar serviço:",
            erro
        );

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


function abrirModalEditarServico(botao) {

    const id =
        botao.dataset.id;

    const codigo =
        botao.dataset.codigo;

    const nome =
        botao.dataset.nome;

    const duracao =
        botao.dataset.duracao;

    const ativo =
        botao.dataset.ativo === "true";


    document
        .getElementById("editarServicoId")
        .value = id;


    document
        .getElementById("editarCodigoServico")
        .value = codigo;


    document
        .getElementById("editarNomeServico")
        .value = nome;


    document
        .getElementById("editarDuracaoServico")
        .value = duracao;


    document
        .getElementById("editarServicoAtivo")
        .checked = ativo;


    document
        .getElementById("mensagemEditarServico")
        .textContent = "";


    document
        .getElementById("modalEditarServico")
        .classList.add("ativo");
}


function fecharModalEditarServico() {

    document
        .getElementById("modalEditarServico")
        .classList.remove("ativo");
}


async function salvarEdicaoServico() {

    const id =
        document
            .getElementById("editarServicoId")
            .value;

    const codigo =
        document
            .getElementById("editarCodigoServico")
            .value;

    const nome =
        document
            .getElementById("editarNomeServico")
            .value
            .trim();

    const duracaoMinutos =
        document
            .getElementById("editarDuracaoServico")
            .value;

    const ativo =
        document
            .getElementById("editarServicoAtivo")
            .checked;

    const mensagem =
        document
            .getElementById("mensagemEditarServico");


    if (!codigo || Number(codigo) <= 0) {

        mensagem.textContent =
            "Informe um código válido.";

        return;
    }


    if (!nome) {

        mensagem.textContent =
            "Informe o nome do serviço.";

        return;
    }


    if (!duracaoMinutos ||
        Number(duracaoMinutos) <= 0) {

        mensagem.textContent =
            "Informe uma duração válida.";

        return;
    }


    const dados = {
        codigo: Number(codigo),
        nome: nome,
        duracaoMinutos:
            Number(duracaoMinutos),
        ativo: ativo
    };


    try {

        const response =
            await fetch(
                `/servicos/${id}`,
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
                "Erro ao atualizar serviço.";

            return;
        }


        window.location.reload();

    } catch (erro) {

        console.error(
            "Erro ao atualizar serviço:",
            erro
        );

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


async function desativarServico(botao) {

    const id =
        botao.dataset.id;


    const confirmar =
        window.confirm(
            "Deseja realmente desativar este serviço?"
        );


    if (!confirmar) {
        return;
    }


    try {

        const response =
            await fetch(
                `/servicos/${id}/desativar`,
                {
                    method: "PUT"
                }
            );


        if (!response.ok) {

            const resultado =
                await response.json();

            alert(
                resultado.mensagem ||
                "Erro ao desativar serviço."
            );

            return;
        }


        window.location.reload();

    } catch (erro) {

        console.error(
            "Erro ao desativar serviço:",
            erro
        );

        alert(
            "Erro ao comunicar com o servidor."
        );
    }
}