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

        console.error(erro);

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


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

        console.error(erro);

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


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

        console.error(erro);

        alert(
            "Erro ao comunicar com o servidor."
        );
    }
}