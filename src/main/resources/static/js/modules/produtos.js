export function init() {
    const searchForm = document.getElementById('search-form');
    const searchType = document.getElementById('search-type');
    const categoria = document.getElementById('categoria');
    const produtoModalEl = document.getElementById('produtoModal');
    const confirmModalEl = document.getElementById('confirma-modal');

    let lastSearch = '';

    searchForm.addEventListener('submit', (event) => {
        event.preventDefault();
    });

    // Disables filter when NOME type is not selected
    searchType.addEventListener('change', e => {
        const type = e.target.value;
        if (type !== 'nome') {
            categoria.setAttribute('disabled', true);
        } else {
            categoria.removeAttribute('disabled');
        }
    });

    // PRODUTO MODAL
    let isNewField = document.getElementById('is-new');
    const modal = new bootstrap.Modal(produtoModalEl);

    document.getElementById('new-produto').addEventListener('click', () => {
        showProdutoModal(-1);
    });
    
    // Listens to edit/delete buttons
    document.getElementById('tabela-produtos').addEventListener('click', (e) => {
        const btn = e.target.closest('button');
        if (!btn) return;

        const action = btn.dataset.action;
        const id = btn.dataset.id;

        if (action === 'edit') {
            showProdutoModal(id);
        } else if (action === 'delete') {
            const nome = btn.closest('tr').querySelectorAll('td')[1]?.innerText;
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
        const codigoBarras = document.getElementById('modal-produto-codigo').value;
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
            url = `/api/produto`;
            method = 'POST';
        }

        const json = JSON.stringify({
            idProduto: idProduto,
            nome: nome,
            codigoBarras: codigoBarras,
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
                const errorMsg = errorData.message;
                console.error(`Erro ${errorData.status}: ${errorMsg}`);
                document.getElementById('error-container').innerHTML = `Erro ao salvar produto: ${errorMsg}`;
                modal.hide();
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
                document.getElementById('error-container').innerHTML = errorData;
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
        document.getElementById('modal-produto-codigo').value = produto.codigoBarras;
        document.getElementById('modal-produto-categoria-id').value = produto.categoria.idCategoria;
        document.getElementById('modal-produto-preco').value = produto.precoAtual.toFixed(2);
        document.getElementById('modal-produto-descricao').value = produto.descricao;
        document.getElementById('modal-produto-estoque').value = produto.estoque;
    }

    function clearProdutoModal() {
        document.getElementById('modal-produto-idProduto').value = '';
        document.getElementById('is-new').value = 'true';
        document.getElementById('modal-produto-nome').value = '';
        document.getElementById('modal-produto-nome').value = '';
        document.getElementById('modal-produto-categoria-id').value = '';
        document.getElementById('modal-produto-preco').value = '00.00';
        document.getElementById('modal-produto-descricao').value = '';
        document.getElementById('modal-produto-estoque').value = '0';
    }

    async function refresh() {
        lastSearch = location.search;
        const url = `/produtos${lastSearch}`
        htmx.ajax('GET', url, { target: '#conteudo', selected: '#tabela-produtos'});
    }
}