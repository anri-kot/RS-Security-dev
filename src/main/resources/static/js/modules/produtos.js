export function init() {

    /* 
        TODO: Show modal on edit
    */

    const searchForm = document.getElementById('search-form');
    const produtoModalEl = document.getElementById('produtoModal');
    const confirmModalEl = document.getElementById('confirma-modal');

    let lastSearch = '';

    searchForm.addEventListener('submit', (event) => {
        event.preventDefault();
    });

    document.addEventListener('htmx:afterSettle', () => {
        lastSearch = location.search;
    });

    // PRODUTO MODAL
    let isNewField = document.getElementById('is-new');
    const modal = new bootstrap.Modal(produtoModalEl);

    document.getElementById('new-produto').addEventListener('click', () => {
        showProdutoModal(-1);
    });

    document.getElementById('tabela-produtos').addEventListener('click', (e) => {
        if (e.target.classList.contains('btn-outline-secondary')) {
            showProdutoModal(e.target.dataset.id);
        } else if (e.target.classList.contains('btn-outline-danger')) {
            const id = e.target.dataset.id;
            const nome = e.target.closest('tr').querySelectorAll('td')[1].innerText;

            showConfirmModal(id, nome, 'deletar');
        }
    });

    document.getElementById('confirm-register').addEventListener('click', () => {
        sendProduto();
    });

    async function showProdutoModal(id) {

        if (id > 0) {
            const response = await fetch(`/api/produto/${id}`);

            if (!response.ok) throw new Error('Erro na requisição');
            populateProdutoModal(id, await response.json());
            isNewField.value = 'false';
        } else {
            isNewField.value = 'true';
            clearProdutoModal();
        }
        
        modal.show();
    }

    async function sendProduto() {
        const isNew = (!isNewField) || isNewField.value === 'true';        

        const nome = document.getElementById('modal-produto-nome').value;
        const categoriaId= parseInt(document.getElementById('modal-produto-categoria-id').value);
        const precoAtual = parseFloat(document.getElementById('modal-produto-preco').value);
        const descricao = document.getElementById('modal-produto-descricao').value;
        const estoque = parseInt(document.getElementById('modal-produto-estoque').value);

        let idProduto;
        let url;
        let method;
        if (!isNew) {
            idProduto = parseInt(document.getElementById('modal-produto-idProduto').value);
            url = `/api/produto/${idProduto}`;
            method = 'PUT';
        } else {
            idProduto = null;
            url = `/produto`;
            method = 'POST';
        }

        const json = JSON.stringify({
            idProduto: idProduto,
            nome: nome,
            descricao: descricao,
            precoAtual: precoAtual,
            estoque: estoque,
            categoria: {
                idCategoria: categoriaId,
            }
        });

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'HX-Request': 'true'
                },
                body: json
            });

            if (!response.ok) {
                const errorData = await response.json();
                alert(`Erro ${errorData.status}: ${errorData.message}`);
                produtoModalEl.hide();
                return;
            } else {
                alert('Ação executada com sucesso.');
                modal.hide();
                clearProdutoModal();
                refresh();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
    }

    // CONFIRM MODAL

    let currentAction = null;
    let confirmModal = null;

    // Confirm action
    document.getElementById('confirmar-acao').addEventListener('click', (e) => {
        if (currentAction === 'deletar') {
            deleteProduto(e.target.dataset.id);
        }
        confirmModal.hide();
    });

    function showConfirmModal(id, nome, action) {
        const mensagem = document.getElementById('modal-mensagem');
        mensagem.innerText = `Tem certeza de que quer ${action} '${nome}'?`;

        confirmModal = new bootstrap.Modal(confirmModalEl);
        confirmModal.show();

        document.getElementById('confirmar-acao').dataset.id = id;
        currentAction = action;
    }

    async function deleteProduto(id) {
        try {
            const url = `/api/produto/${id}`;
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'HX-Request': 'true' },
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

    function populateProdutoModal(id, produto) {        
        document.getElementById('modal-produto-idProduto').value = id;
        document.getElementById('is-new').value = 'false';
        document.getElementById('modal-produto-nome').value = produto.nome;
        document.getElementById('modal-produto-categoria-id').value = produto.categoria.idCategoria;
        document.getElementById('modal-produto-preco').value = produto.precoAtual.toFixed(2);
        document.getElementById('modal-produto-descricao').value = produto.descricao;
        document.getElementById('modal-produto-estoque').value = produto.estoque;
    }

    function clearProdutoModal() {
        document.getElementById('modal-produto-idProduto').value = '';
        document.getElementById('is-new').value = 'true';
        document.getElementById('modal-produto-nome').value = '';
        document.getElementById('modal-produto-categoria-id').value = '';
        document.getElementById('modal-produto-preco').value = '00.00';
        document.getElementById('modal-produto-descricao').value = '';
        document.getElementById('modal-produto-estoque').value = '1';
    }

    async function refresh() {
        const url = `/produtos${lastSearch}`
        htmx.ajax('GET', url, { target: '#conteudo', selected: '#tabela-produtos'});
    }
}