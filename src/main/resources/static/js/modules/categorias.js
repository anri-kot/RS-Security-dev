export function init() {
    const tabelaCategoriasEl = document.getElementById('tabela-categorias');
    const newCategoriaBtnEl = document.getElementById('new-categoria');
    const categoriaModalEl = document.getElementById('categoriaModal');
    const modalIdEl = document.getElementById('modal-categoria-id');
    const modalNomeEl = document.getElementById('modal-categoria-nome');
    const confirmRegisterEl = document.getElementById('confirm-register');

    newCategoriaBtnEl.addEventListener('click', () => {
        showCategoriaModal();
    });

    tabelaCategoriasEl.addEventListener('click', e => {
        const clickedButton = e.target.closest('button');
        if (clickedButton) {
            const action = clickedButton.dataset.action;

            if (action === 'edit') {
                const id = parseInt(clickedButton.dataset.id);
                showCategoriaModal(id);
            }
        }
    });

    confirmRegisterEl.addEventListener('click', () => {
        const categoria = getCategoriaFromModal();
        sendCategoria(categoria);
    });

    async function showCategoriaModal(id = 0) {
        const modal = bootstrap.Modal.getOrCreateInstance(categoriaModalEl);

        if (id > 0) {
            const url = `/api/categoria/${id}`;
            const response = await fetch(url);

            if (!response.ok) {
                handleBadResponse(response);
            } else {
                const categoria = await response.json();
                populateCategoriaFields(categoria);
            }
        } else {
            clearCategoriaModalFields();
        }

        modal.show();
    }

    function populateCategoriaFields(categoria) {
        modalIdEl.value = categoria.idCategoria;
        modalNomeEl.value = categoria.nome;
    }

    function clearCategoriaModalFields() {
        const form = document.getElementById('modal-categoria-form');
        form.querySelectorAll('input').forEach(el => el.value = '');
    }

    async function refreshCategorias() {
        const lastSearch = location.search;
        const url = `/categorias${lastSearch}`
        htmx.ajax('GET', url, { target: '#conteudo', selected: '#tabela-categorias' });
    }

    async function sendCategoria(categoria) {
        if (categoria == null) return;
        const id = categoria.idCategoria;
        const action = id !== null ? 'update' : 'create';

        let url;
        let method;
        const body = JSON.stringify(categoria);
        const modal = bootstrap.Modal.getOrCreateInstance(categoriaModalEl);

        try {
            if (action === 'create') {
                url = "/api/categoria";
                method = "POST";
            } else {
                url = `/api/categoria/${id}`;
                method = "PUT";
            }

            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'HX-Request': 'true',
                },
                body: body
            });

            if (!response.ok) {
                const msg = await response.text();
                handleBadResponse(msg);
            } else {
                refreshCategorias();
                alert(`Categoria criada com sucesso!`);
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
        modal.hide();
    }

    function getCategoriaFromModal() {
        const id = parseInt(modalIdEl.value) || null;
        const nome = modalNomeEl.value.trim();

        return { idCategoria: id, nome: nome }
    }

    function handleBadResponse(message) {
        const errorContainerEl = document.getElementById('error-container');
        errorContainerEl.innerHTML = message;
    }
}