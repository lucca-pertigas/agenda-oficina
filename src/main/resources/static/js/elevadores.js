function abrirModalNovoElevador() {

    document
        .getElementById("novoNumeroElevador")
        .value = "";

    document
        .getElementById("mensagemNovoElevador")
        .textContent = "";

    document
        .getElementById("modalNovoElevador")
        .classList.add("ativo");
}


function fecharModalNovoElevador() {

    document
        .getElementById("modalNovoElevador")
        .classList.remove("ativo");
}


async function salvarNovoElevador() {

    const numero =
        document
            .getElementById("novoNumeroElevador")
            .value;

    const mensagem =
        document
            .getElementById("mensagemNovoElevador");


    if (!numero) {

        mensagem.textContent =
            "Informe o número do elevador.";

        return;
    }


    try {

        const response =
            await fetch(
                "/elevadores",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            numero: Number(numero),
                            ativo: true
                        })
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            mensagem.textContent =
                resultado.mensagem ||
                "Erro ao cadastrar elevador.";

            return;
        }


        window.location.reload();

    } catch (erro) {

        console.error(
            "Erro ao cadastrar elevador:",
            erro
        );

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


function abrirModalEditarElevador(botao) {

    const id =
        botao.dataset.id;

    const numero =
        botao.dataset.numero;

    const ativo =
        botao.dataset.ativo === "true";


    document
        .getElementById("editarElevadorId")
        .value = id;


    document
        .getElementById("editarNumeroElevador")
        .value = numero;


    document
        .getElementById("editarElevadorAtivo")
        .checked = ativo;


    document
        .getElementById("mensagemEditarElevador")
        .textContent = "";


    document
        .getElementById("modalEditarElevador")
        .classList.add("ativo");
}


function fecharModalEditarElevador() {

    document
        .getElementById("modalEditarElevador")
        .classList.remove("ativo");
}


async function salvarEdicaoElevador() {

    const id =
        document
            .getElementById("editarElevadorId")
            .value;


    const numero =
        document
            .getElementById("editarNumeroElevador")
            .value;


    const ativo =
        document
            .getElementById("editarElevadorAtivo")
            .checked;


    const mensagem =
        document
            .getElementById("mensagemEditarElevador");


    if (!numero) {

        mensagem.textContent =
            "Informe o número do elevador.";

        return;
    }


    try {

        const response =
            await fetch(
                `/elevadores/${id}`,
                {
                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            numero: Number(numero),
                            ativo: ativo
                        })
                }
            );


        const resultado =
            await response.json();


        if (!response.ok) {

            mensagem.textContent =
                resultado.mensagem ||
                "Erro ao atualizar elevador.";

            return;
        }


        window.location.reload();

    } catch (erro) {

        console.error(
            "Erro ao editar elevador:",
            erro
        );

        mensagem.textContent =
            "Erro ao comunicar com o servidor.";
    }
}


async function desativarElevador(botao) {

    const id =
        botao.dataset.id;


    const confirmar =
        window.confirm(
            "Deseja realmente desativar este elevador?"
        );


    if (!confirmar) {
        return;
    }


    try {

        const response =
            await fetch(
                `/elevadores/${id}/desativar`,
                {
                    method: "PUT"
                }
            );


        if (!response.ok) {

            const resultado =
                await response.json();

            alert(
                resultado.mensagem ||
                "Erro ao desativar elevador."
            );

            return;
        }


        window.location.reload();

    } catch (erro) {

        console.error(
            "Erro ao desativar elevador:",
            erro
        );

        alert(
            "Erro ao comunicar com o servidor."
        );
    }
}