export function init() {

    let lastSearch = '';

    const fornecedorModalEl = document.getElementById('fornecedor-modal');
    const table = document.getElementById('tabela-fornecedores');
    const confirmWarningBtn = document.getElementById('confirmar-acao');

    // LISTENS TO 'Novo Fornecedor' PRESS
    document.getElementById('new-fornecedor').addEventListener('click', () => {
        showFornecedorModal(-1);
    });

    // LISTENS TO EDIT AND DELETE BUTTONS
    table.addEventListener('click', (e) => {
        if (e.target.classList.contains('btn-outline-secondary')) {
            showFornecedorModal(e.target.dataset.id);
        } else if (e.target.classList.contains('btn-outline-danger')) {
            const id = e.target.dataset.id;
            const nome = e.target.closest('tr').querySelectorAll('td')[1].innerText;

            triggerDeleteWarning(id, nome);
        }
    });

    // SAVES LAST SEARCH MADE
    document.addEventListener('htmx:afterSettle', () => {
        lastSearch = location.search;
    });

    // FORNECEDOR MODAL

    // fields
    const idFornecedorEl = document.getElementById('modal-fornecedor-idFornecedor');
    const nomeEl = document.getElementById('modal-fornecedor-nome');
    const cnpjEl = document.getElementById('modal-fornecedor-cnpj');
    const telefoneEl = document.getElementById('modal-fornecedor-telefone');
    const emailEl = document.getElementById('modal-fornecedor-email');

    const fornecedorModal = new bootstrap.Modal(fornecedorModalEl);

    // Listens to confirm
    document.getElementById('confirm-register').addEventListener('click', () => {
        sendFornecedor();
    });

    async function showFornecedorModal(id) {
        if (id > 0) {
            try {
                const response = await fetch(`/api/fornecedor/${id}`);

                if (!response.ok) throw new Error('Erro na requisição');
                populateFornecedorFields(await response.json());
            } catch (e) {
                console.error('Erro inesperado:', e);
                alert('Ocorreu um erro inesperado.');
                return;
            }
        } else {
            populateFornecedorFields();
        }
        fornecedorModal.show();
    }

    function populateFornecedorFields(fornecedor = {}) {
        idFornecedorEl.value = fornecedor.idFornecedor ?? "";
        nomeEl.value = fornecedor.nome ?? "";
        cnpjEl.value = fornecedor.cnpj ?? "";
        telefoneEl.value = fornecedor.telefone ?? "";
        emailEl.value = fornecedor.email ?? "";
    }

    // CREATE / UPDATE / DELETE

    async function sendFornecedor() {
        const fornecedor = getFornecedorObj();

        try {
            let url;
            let method;

            if ((fornecedor.idFornecedor) && fornecedor.idFornecedor > 0) {
                url = `/api/fornecedor/${idFornecedorEl.value}`;
                method = 'PUT';
            } else {
                url = `/api/fornecedor`;
                method = 'POST';
            }

            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'HX-Request': 'true'
                },
                body: JSON.stringify(fornecedor)
            });

            if (!response.ok) {
                const errorData = await response.json();
                alert(`Erro ${errorData.status}: ${errorData.message}`);
                fornecedorModalEl.hide();
                return;
            } else {
                alert('Ação executada com sucesso.');
                fornecedorModal.hide();
                refresh();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
    }

    async function deleteFornecedor(id) {
        if (!id) return;

        try {
            const url = `/api/fornecedor/${id}`;
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    "Content-Type": "application/json",
                    "HX-Request": "true"
                },
                body: {}
            });

            if (!response.ok) {
                const errorData = await response.json();
                alert(`Erro ${errorData.status}: ${errorData.message}`);
                return;
            } else {
                alert('Ação executada com sucesso.');
                refresh();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
    }

    function getFornecedorObj() {
        let idFornecedor;

        if (idFornecedorEl.value.length > 0 && parseInt(idFornecedorEl.value) > 0) {
            idFornecedor = parseInt(idFornecedorEl.value);
        } else {
            idFornecedor = null;
        }

        const nome = nomeEl.value;
        const cnpj = cnpjEl.value;
        const telefone = telefoneEl.value;
        const email = emailEl.value;

        return {
            idFornecedor: idFornecedor,
            nome: nome,
            cnpj: cnpj,
            telefone: telefone,
            email: email
        };
    }

    // WARNING MODAL
    const warningModal = new bootstrap.Modal(document.getElementById('confirma-modal'));

    // Listens to confirm
    confirmWarningBtn.addEventListener('click', () => {
        deleteFornecedor(parseInt(confirmWarningBtn.dataset.id));
        confirmWarningBtn.removeAttribute('data-id');
        warningModal.hide();
    });

    function triggerDeleteWarning(id, nome) {
        const msgEl = document.getElementById('modal-mensagem');
        msgEl.innerHTML = `Tem certeza de que deseja exluir <bold>${nome}</bold><small>${id}</small>?`;
        confirmWarningBtn.dataset.id = id;

        warningModal.show();
    }

    // REFRESH

    async function refresh() {
        const url = `/fornecedores${lastSearch}`;
        htmx.ajax('GET', url, { target: '#conteudo', selected: '#tabela-fornecedores'});
    }
}