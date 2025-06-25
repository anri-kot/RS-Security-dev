export function init() {

    let lastSearch = '';

    // ================== ELEMENTS ==================
    // ===== MAIN SEARCH
    const filterEl = document.getElementById('filter');
    const searchTypeEl = document.getElementById('search-type');
    const observacaoEl = document.getElementById('observacao');
    const searchTermEl = document.getElementById('search-term');
    const autocompleteSearchProdutos = document.getElementById('autocompleteProdutosSearch');

    // ===== MODALS
    // Compra
    const compraModalEl = document.getElementById('compra-modal');
    const modalIdEl = document.getElementById('modal-compra-idCompra');
    const modalDataEl = document.getElementById('modal-compra-data');
    const modalObservacaoEl = document.getElementById('modal-compra-observacao');
    const modalFornecedorEl = document.getElementById('modal-compra-fornecedor');
    const modalItensEl = document.getElementById('modal-compra-itens');
    // Compra Items
    const modalItemsTotalEl = document.getElementById('modal-items-total');
    const modalQuantidadeEl = document.getElementById('modal-compra-quantidade');
    const modalValorUnitarioEl = document.getElementById('modal-compra-valorUnitario');
    const modalCurrentProdutoEl = document.getElementById('current-product');
    const modalFornecedorIdEl = document.getElementById('modal-compra-fornecedor-id');
    const selectedProdutoIdEl = document.getElementById('selected-product-id');
    const selectedProdutoNameEl = document.getElementById('selected-product-name')
    const addItemBtn = document.getElementById('add-button');
    // Compra Items Search
    const itemSearchTypeEl = document.getElementById('item-search-type');
    const itemSearchProduct = document.getElementById('search-product');
    const itemAutocompleteProdutoOptions = document.getElementById('autocomplete-produto-options');
    const itemAutocompleteFornecedorOptions = document.getElementById('autocomplete-fornecedor-options');
    const categoriaEl = document.getElementById('categoria');
    // Confirm Modal
    const modalConfirmEl = document.getElementById('confirma-modal');
    const modalConfirmLabelEl = document.getElementById('confirmar-label');
    const modalConfirmMsgEl = document.getElementById('confirmar-mensagem');
    const modalConfirmOkButtonEl = document.getElementById('confirmar-acao');

    // Keeps track of the current search
    document.addEventListener('htmx:afterSettle', (e) => {
        lastSearch = location.search;
    });

    // ================== MAIN SEARCH ==================

    // Updates inputs when the filter changes
    filterEl.addEventListener('change', () => {
        toggleDateInputs();
    });

    // Updates filters according to the selected search method
    searchTypeEl.addEventListener('change', () => {
        if (searchTypeEl.value === 'idCompra') {
            for (let i = 0; i < filterEl.children.length; i++) {
                filterEl.children[i].removeAttribute('selected');
            }

            filterEl.children[0].setAttribute('selected', true);
            filterEl.setAttribute('disabled', true);
            observacaoEl.setAttribute('disabled', true);

            toggleDateInputs();

        } else {
            filterEl.removeAttribute('disabled');
            observacaoEl.removeAttribute('disabled');
        }
    });

    // Blocks htmx requests when 'produto' is not selected
    searchTermEl.addEventListener('htmx:config-request', (e) => {
        if (!searchTypeEl.value.includes('produto')) {
            e.preventDefault();
        }
    });

    function toggleDateInputs() {
        const filter = filterEl.value;

        const dataInicioContainer = document.getElementById('data-inicio-container');
        const dataFimContainer = document.getElementById('data-fim-container');

        const dataInicio = document.getElementById('dataInicio');
        dataInicio.value = '';
        dataInicio.setAttribute('disabled', true);
        const dataFim = document.getElementById('dataFim');
        dataFim.value = '';
        dataFim.setAttribute('disabled', true);

        switch (filter) {
            case 'entre':
                dataInicioContainer.style.display = 'block';
                dataFimContainer.style.display = 'block';
                dataInicio.removeAttribute('disabled');
                dataFim.removeAttribute('disabled');
                break;

            case 'antes':
                dataInicioContainer.style.display = 'none';
                dataFimContainer.style.display = 'block';
                dataFim.removeAttribute('disabled');
                break;

            case 'depois':
                dataInicioContainer.style.display = 'block';
                dataInicio.removeAttribute('disabled');
                dataFimContainer.style.display = 'none';
                break;

            default:
                dataInicioContainer.style.display = 'none';
                dataFimContainer.style.display = 'none';
                break;
        }
    }

    /* ================== REFRESH REGISTERS ================== */

    async function refreshCompras() {
        const url = `/compras${lastSearch}`;
        htmx.ajax('GET', url, { target: '#conteudo', selected: '#cards-compras' });
    }

    /* ================== COMPRA MODAL ================== */

    let itens = [];
    let total = 0;

    // Listens to new Compra button
    document.getElementById('new-compra').addEventListener('click', () => {
        showCompraModal();
    });

    // Listens to edit/remove buttons
    document.getElementById('cards-compras').addEventListener('click', (e) => {
        if (e.target.closest('button')) {
            const el = e.target.closest('button');

            if (el.dataset.action == 'edit') {
                showCompraModal(el.dataset.id);
            } else if (el.dataset.action == 'delete') {
                showConfirmModal(el.dataset.id);
            }
        }
    });

    // Updates arrow symbol on toggle collapse
    document.getElementById('show-items-btn').addEventListener('click', () => {
        const iconEl = document.getElementById('show-items-icon');
        if (iconEl.classList.contains('bi-chevron-down')) {
            iconEl.classList.remove('bi-chevron-down');
            iconEl.classList.add('bi-chevron-up');
        } else {
            iconEl.classList.remove('bi-chevron-up');
            iconEl.classList.add('bi-chevron-down');
        }
    });

    async function showCompraModal(id) {
        clearCompraModalFields();

        if (id || id > 0) {
            let compra;

            try {
                const response = await fetch(`/api/compra/${id}`);
                if (!response.ok) throw new Error('Erro na requisição');
                compra = await response.json();
            } catch (e) {
                console.error('Erro inesperado:', e);
                alert('Ocorreu um erro inesperado.');
                return;
            }

            itens = compra.itens;
            populateCompraModal(compra);
        }
        const compraModal = bootstrap.Modal.getOrCreateInstance(compraModalEl);

        compraModal.show();
    }

    function populateCompraModal(compra) {
        modalIdEl.value = compra.idCompra;
        modalDataEl.value = compra.data;
        modalObservacaoEl.value = compra.observacao;
        modalFornecedorEl.value = compra.fornecedor.nome;
        modalFornecedorIdEl.value = compra.fornecedor.idFornecedor;

        refreshItems();
    }

    function clearCompraFormFields() {
        const compraForm = document.getElementById('compra-form');
        compraForm.querySelectorAll('input').forEach(el => {
            el.value = '';
        });
        modalItensEl.innerHTML = '';
        modalItemsTotalEl.innerText = 'R$ 0,00';
    }

    function clearCompraModalFields() {

        clearCompraFormFields();
        clearItemsFormFields();
        currentItemIndex = null;
        itens = [];
    }

    // ===== COMPRA ITEMS

    let currentItemIndex = null;

    // Cancel submit
    document.getElementById('items-form').addEventListener('submit', e => e.preventDefault());

    // Parse valorUnitarioEl
    modalValorUnitarioEl.addEventListener('change', (e) => {
        if (e.target.value.length > 0) {
            const value = parseFloat(e.target.value);
            modalValorUnitarioEl.value = value.toFixed(2);
        }
    });

    // Listens for ADICIONAR button click
    addItemBtn.addEventListener('click', () => {
        addUpdateItem();
    });

    // Validates fornecedor
    modalFornecedorEl.addEventListener('change', (e) => {
        const value = e.target.value;
        if (value !== e.target.dataset.nome) {
            modalFornecedorEl.setAttribute('isvalid', false);
        }
    });

    function clearItemsFormFields() {
        const itemsForm = document.getElementById('items-form');
        itemsForm.querySelectorAll('input').forEach(el => el.value = '');
        modalCurrentProdutoEl.innerText = 'Aguardando selecionar produto...';
    }

    function addUpdateItem() {
        const idProduto = parseInt(selectedProdutoIdEl.value);
        const nome = selectedProdutoNameEl.value;
        const quantidade = parseInt(modalQuantidadeEl.value);
        const valorUnitario = parseFloat(modalValorUnitarioEl.value);

        if (currentItemIndex != null) {
            itens[currentItemIndex].quantidade = quantidade;
            itens[currentItemIndex].valorUnitario = valorUnitario;
        } else {
            itens.push({
                quantidade: quantidade,
                valorUnitario: valorUnitario,
                produto: {
                    idProduto: idProduto,
                    nome: nome
                },
            });
        }

        refreshItems();
        clearItemsFormFields();
    }

    function populateItemFields(item) {
        modalCurrentProdutoEl.innerHTML = `Editando <strong>${item.produto.nome}</strong>`;

        modalQuantidadeEl.value = item.quantidade || 1;
        modalValorUnitarioEl.value = parseFloat(item.valorUnitario || item.produto.precoAtual || '0.05').toFixed(2);
        document.getElementById('add-button').removeAttribute('disabled');
        selectedProdutoIdEl.value = item.produto.idProduto;
        selectedProdutoNameEl.value = item.produto.nome;
    }

    function refreshItems() {
        modalItensEl.innerHTML = '';
        total = 0;
        itens.forEach(item => {
            modalItensEl.appendChild(renderCompraItem(item));

            // calculating total
            const unitPriceCents = parseFloat(item.valorUnitario * 100);
            total += (unitPriceCents * item.quantidade) / 100;
        });

        modalItemsTotalEl.innerText = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    }

    let tempId = 0;
    function renderCompraItem(item) {
        const li = document.createElement('li');
        li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-start');

        // Calculate price with discount
        const preco = item.valorUnitario;
        const total = preco * item.quantidade;

        // Produto info container
        const mainContainer = document.createElement('div');
        mainContainer.classList.add('flex-grow-1');

        const nomeEl = document.createElement('div');
        nomeEl.classList.add('fw-semibold');
        nomeEl.textContent = item.produto.nome;

        const qtdPrecoEl = document.createElement('small');
        qtdPrecoEl.classList.add('text-muted', 'd-block');
        qtdPrecoEl.textContent = `Qtd. ${item.quantidade} × R$ ${item.valorUnitario.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;

        mainContainer.appendChild(nomeEl);
        mainContainer.appendChild(qtdPrecoEl);

        // total and buttons container
        const rightContainer = document.createElement('div');
        rightContainer.classList.add('d-flex', 'flex-column', 'justify-content-between')
        const buttonsContainer = document.createElement('div');
        buttonsContainer.classList.add('text-end', 'd-flex', 'align-items-center', 'gap-2', 'mb-2');

        const totalEl = document.createElement('small');
        totalEl.classList.add('fw-bold', 'text-dark');
        totalEl.textContent = `R$ ${total.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;

        // buttons
        const editButton = document.createElement('button');
        editButton.classList.add('btn', 'btn-sm', 'btn-outline-primary');
        editButton.setAttribute('title', 'Editar');
        editButton.innerHTML = '<i class="bi bi-pencil"></i>'; // Bootstrap Icons
        editButton.dataset.idProduto = item.produto.idProduto;

        const deleteButton = document.createElement('button');
        deleteButton.classList.add('btn', 'btn-sm', 'btn-outline-danger');
        deleteButton.setAttribute('title', 'Excluir');
        deleteButton.innerHTML = '<i class="bi bi-trash"></i>';
        deleteButton.dataset.idProduto = item.produto.idProduto;

        // edit/delete listeners
        editButton.addEventListener('click', () => {
            const itemIndex = itens.findIndex(item => item.produto.idProduto === parseInt(editButton.dataset.idProduto));
            currentItemIndex = itemIndex;
            populateItemFields(itens[itemIndex]);
        });
        deleteButton.addEventListener('click', () => {
            itens.splice(itens.findIndex(item => item.produto.idProduto === parseInt(deleteButton.dataset.idProduto)), 1);
            refreshItems();
        });


        buttonsContainer.appendChild(editButton);
        buttonsContainer.appendChild(deleteButton);
        rightContainer.appendChild(buttonsContainer);
        rightContainer.appendChild(totalEl);

        // Mount item
        li.appendChild(mainContainer);
        li.appendChild(rightContainer);

        return li;
    }

    /* ================== CONFIRM MODAL ================== */

    modalConfirmOkButtonEl.addEventListener('click', (e) => {
        deleteCompra(e.target.dataset.id);
        const modal = bootstrap.Modal.getOrCreateInstance(modalConfirmEl);
        modal.hide();
    });

    function showConfirmModal(id) {
        const confirmModal = bootstrap.Modal.getOrCreateInstance(modalConfirmEl);
        if (!id) return;

        modalConfirmLabelEl.innerText = 'Confirmar delete';
        modalConfirmOkButtonEl.dataset.id = id;
        modalConfirmMsgEl.innerHTML = `Tem certeza que deseja deletar a compra número <strong>#${id}</strong>?`;

        confirmModal.show();
    }

    /* ================== DROPDOWNS ================== */

    let lastQuery = null;

    const dropdowns = {
        fornecedor: itemAutocompleteFornecedorOptions,
        produto: itemAutocompleteProdutoOptions,
        term: autocompleteSearchProdutos
    };

    const searchInputs = {
        produto: itemSearchProduct,
        term: searchTermEl,
        fornecedor: modalFornecedorEl
    };

    // Listens to dropdown clicks
    Object.values(dropdowns).forEach(dropdown => {
        dropdown.addEventListener('click', (e) => {
            const itemEl = e.target.closest('li');
            if (!itemEl) return;
            dropdown.classList.remove('show');
            handleDropdownClick(itemEl, dropdown.id);
        });
    });

    // Disables 'categoria' when ID is selected
    itemSearchTypeEl.addEventListener('change', () => {
        categoriaEl.toggleAttribute('disabled', itemSearchTypeEl.value === 'id');
    });

    // Show dropdown on focus
    Object.entries(searchInputs).forEach(([key, input]) => {
        input.addEventListener('focus', () => updateDropdown(key));
    });

    // Hide dropdown when clicking outside the input
    document.addEventListener('click', (event) => {
        if (!event.target.closest('.dropdown-menu') && !event.target.classList.contains('input-dropdown')) {
            document.querySelectorAll('.dropdown-menu.show').forEach(el => el.classList.remove('show'));
        }
    });

    // Validates htmx request
    document.body.addEventListener("htmx:beforeRequest", (event) => {
        if (event.target.id !== itemSearchProduct.id) return;

        const value = itemSearchProduct.value.trim();
        const isIdSearch = itemSearchTypeEl.value === 'id';

        const isTooShort = isIdSearch
            ? value.length < 1
            : value.length < 3;

        const isRepeatedQuery = value === lastQuery;

        if (isTooShort || isRepeatedQuery) {
            event.preventDefault();
        }
    });

    // Updates dropdown after htmx request
    document.body.addEventListener('htmx:afterSwap', (event) => {
        if (event.target.id === itemAutocompleteProdutoOptions.id) {
            lastQuery = itemSearchProduct.value.trim();
            updateDropdown('produto');
        }
    });

    async function handleDropdownClick(itemEl, dropdownId) {
        const dataset = itemEl.dataset;

        if (dropdownId === dropdowns.fornecedor.id) {
            modalFornecedorEl.value = dataset.nome;
            modalFornecedorEl.dataset.nome = dataset.nome;
            modalFornecedorIdEl.value = dataset.idUsuario;

        } else if (dropdownId === dropdowns.produto.id) {
            const idProduto = parseInt(dataset.idProduto);
            const itemIndex = itens.findIndex(item => item.produto.idProduto === idProduto);

            itemSearchProduct.value = dataset.nomeProduto;

            if (itemIndex !== -1) {
                populateItemFields(itens[itemIndex]);
                currentItemIndex = itemIndex;
            } else {
                const produto = await getProduto(idProduto);
                if (produto) {
                    populateItemFields({ produto });
                    modalCurrentProdutoEl.innerHTML = `Inserindo <strong>${dataset.nomeProduto}</strong>`;
                    currentItemIndex = null;
                }
            }

            document.getElementById('add-button').removeAttribute('disabled');
            selectedProdutoIdEl.value = idProduto;
            selectedProdutoNameEl.value = dataset.nomeProduto;

        } else if (dropdownId === dropdowns.term.id) {
            searchTermEl.value = dataset.nomeProduto;
        }
    }

    function updateDropdown(type) {
        const dropdown = dropdowns[type];
        if (!dropdown) return;

        const input = searchInputs[type];
        const value = input?.value?.trim() ?? "";

        const shouldShow = (
            type === 'produto'
                ? (itemSearchTypeEl.value === 'nome' && value.length >= 3) || value.length > 0
                : true
        );

        dropdown.classList.toggle('show', shouldShow);
    }

    async function getProduto(id) {
        try {
            const response = await fetch(`/api/produto/${id}`);
            if (!response.ok) {
                alert('Erro ao pesquisar produto.');
                console.error(await response.json());
                return null;
            }
            return await response.json();
        } catch (e) {
            console.error('Erro inesperado ao buscar produto:', e);
            return null;
        }
    }

    /* ================== INSERT, UPDATE, DELETE ================== */

    document.getElementById('confirm-register').addEventListener('click', (e) => {
        const id = parseInt(modalIdEl.value) || 0;

        modalFornecedorEl.querySelectorAll('option').forEach(opt => {
            if (opt.value === modalFornecedorEl.value) {
                modalFornecedorEl.dataset.id = opt.dataset.id;
                return;
            }
        });

        sendCompra(id);
    });

    async function sendCompra(id) {
        let url;
        let method;
        const body = JSON.stringify(getCompraObj());

        if (id > 0) {
            url = `/api/compra/${id}`;
            method = 'PUT';
        } else {
            url = `/api/compra`;
            method = 'POST'
        }

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'HX-Request': 'true'
                },
                body: body
            });

            const compraModal = bootstrap.Modal.getInstance(compraModalEl);

            if (!response.ok) {
                const errorData = await response.json();
                alert(`Erro ${errorData.status}: ${errorData.message}`);
                compraModal.hide();
                return;
            } else {
                // alert('Ação executada com sucesso.');
                refreshCompras();
                compraModal.hide();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
    }

    async function deleteCompra(id) {
        try {
            const response = await fetch(`/api/compra/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Hx-Request': true
                },
                body: {}
            });

            if (!response.ok) {
                const errorData = await response.json();
                alert(`Erro ${errorData.status}: ${errorData.message}`);
                return;
            } else {
                // alert('Ação executada com sucesso.');
                refreshCompras();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
    }

    function getCompraObj() {
        const idCompra = parseInt(modalIdEl.value) || null;
        const data = modalDataEl.value;
        const observacao = modalObservacaoEl.value;
        const idFornecedor = parseInt(modalFornecedorIdEl.value);

        return {
            idCompra: idCompra,
            data: data,
            itens: itens,
            observacao: observacao,
            fornecedor: { idFornecedor: idFornecedor }
        }
    }
}