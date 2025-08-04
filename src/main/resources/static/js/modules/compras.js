// ======== INIT + STATE ========
export function init() {
    let alreadySet = false;

    let lastSearch = location.search;
    let lastEditId = null;
    let itens = [];
    let total = 0;
    let currentItemIndex = null;
    let lastQuery = null;

    const STORAGE_KEY = 'compra-form-draft';

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
    const compraFormEl = document.getElementById('compra-form');
    const modalIdEl = document.getElementById('modal-compra-idCompra');
    const modalDataEl = document.getElementById('modal-compra-data');
    const modalObservacaoEl = document.getElementById('modal-compra-observacao');
    const modalFornecedorEl = document.getElementById('modal-compra-fornecedor');
    const modalItensEl = document.getElementById('modal-compra-itens');
    // Compra Items
    const modalItemsTotalEl = document.getElementById('modal-items-total');
    const modalItemsTotalOldEl = document.getElementById('modal-items-total-old');
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
    const modalConfirmCancelButtonEl = document.getElementById('cancelar-acao');

    restoreFormDraft();

    // Keeps track of the current search and restores modal
    document.addEventListener('htmx:afterSwap', (e) => {
        if (!location.pathname.includes('compras')) return;

        if (!alreadySet) {
            lastSearch = location.search;
            restoreFormDraft();
            alreadySet = true;
        }

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
        lastSearch = location.search;
        const url = `/compras${lastSearch}`;
        htmx.ajax('GET', url, { target: '#conteudo', selected: '#cards-compras' });
    }

    /* ================== COMPRA MODAL ================== */

    // Listens to new Compra button
    document.getElementById('new-compra').addEventListener('click', () => {
        showCompraModal();
    });

    // Listens to edit/remove buttons
    document.getElementById('cards-compras').addEventListener('click', async (e) => {
        if (e.target.closest('button')) {
            const el = e.target.closest('button');

            if (el.dataset.action == 'edit') {
                showCompraModal(el.dataset.id);
            } else if (el.dataset.action == 'delete') {
                const id = el.dataset.id;
                const message = `Tem certeza que deseja deletar a compra número <strong>#${id}</strong>?`;
                
                const confirmed = await showConfirmModal(id, 'delete', message);
                if (confirmed) {
                    deleteCompra(id);
                }
                
                const confirmModal = bootstrap.Modal.getOrCreateInstance(modalConfirmEl);
                confirmModal.hide();
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

    // Saves form to local storage
    compraFormEl.addEventListener('input', (e) => {
        const el = e.target.closest('form');
        saveFormDraft(el);
    });

    function saveFormDraft() {
        const draft = {
            form: {
                idCompra: document.querySelector("#modal-compra-idCompra").value,
                data: document.querySelector("#modal-compra-data").value,
                observacao: document.querySelector("#modal-compra-observacao").value,
                fornecedor: document.querySelector("#modal-compra-fornecedor").value
            },
            items: {
                itens: itens
            }
        };

        localStorage.setItem(STORAGE_KEY, JSON.stringify(draft));
    }

    function restoreFormDraft() {
        const draft = localStorage.getItem(STORAGE_KEY);
        if (!draft) return;

        try {
            const parsed = JSON.parse(draft);

            const form = parsed.form;
            if (form) {
                document.querySelector("#modal-compra-idCompra").value = form.idCompra || "";
                document.querySelector("#modal-compra-data").value = form.data || "";
                document.querySelector("#modal-compra-observacao").value = form.observacao || "";
                document.querySelector("#modal-compra-fornecedor").value = form.fornecedor || "";

                const id = parseInt(form.idCompra) || 0;
                lastEditId = id;
            }

            const itemsWrapper = parsed.items;
            if (itemsWrapper && itemsWrapper.itens && Array.isArray(itemsWrapper.itens)) {
                itens = itemsWrapper.itens; 
                refreshItems(); 
            }
        } catch (e) {
            console.error("Erro ao restaurar rascunho:", e);
        }
    }

    function clearFormDraft() {
        localStorage.removeItem(STORAGE_KEY);
    }

    async function showCompraModal(id = 0) {
        const isTheSame = lastEditId === (parseInt(id) || 0);

        if (lastEditId !== null) {
            if (!isTheSame && itens.length > 0) {
                let message;
                if (lastEditId > 0) {
                    message = `Você estava editando a compra de <strong>ID ${lastEditId}</strong>. Se continuar as alterações não serão aplicadas.`;
                } else {
                    message = `Você estava editando uma nova compra. Se continuar o registro será perdido.`;
                }

                const confirmed = await showConfirmModal(id, 'switch-edit', message);
                if (!confirmed) {
                    return;
                }

                lastEditId = null;
                clearCompraModalFields();
            }
        } else {
            clearCompraModalFields();
        }

        if (id > 0 && !isTheSame) {
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

        lastEditId = parseInt(id);
        const compraModal = bootstrap.Modal.getOrCreateInstance(compraModalEl);

        saveFormDraft();
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
            if (el.type.includes('date')) {
                const today = new Date();
                today.setHours(today.getHours(), 0, 0, 0);

                const year = today.getFullYear();
                const month = String(today.getMonth() + 1).padStart(2, '0');
                const day = String(today.getDate()).padStart(2, '0');
                const hours = String(today.getHours()).padStart(2, '0');
                const minutes = String(today.getMinutes()).padStart(2, '0');

                el.value = `${year}-${month}-${day}T${hours}:${minutes}`;
            }
        });
        modalItensEl.innerHTML = '';
        modalItemsTotalEl.innerText = 'R$ 0,00';

        modalItemsTotalOldEl.innerHTML = '';
        modalItemsTotalOldEl.classList.add('d-none');
    }

    function clearCompraModalFields() {

        clearCompraFormFields();
        clearItemsFormFields();
        currentItemIndex = null;
        itens = [];
    }

    // ===== COMPRA ITEMS

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
        addItemBtn.setAttribute('disabled', true);
        modalCurrentProdutoEl.innerText = 'Aguardando selecionar produto...';
    }

    function addUpdateItem() {
        const itemsForm = document.getElementById('items-form');
        if (!itemsForm.checkValidity()) {
            itemsForm.reportValidity();
            return;
        }

        const idProduto = parseInt(selectedProdutoIdEl.value) || null;
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

        saveFormDraft();
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
        let totalCents = 0;
        let oldTotalCents = 0;
        let hasDeletedProduto = false;

        itens.forEach(item => {
            modalItensEl.prepend(renderCompraItem(item));

            const unitPriceCents = Math.round(parseFloat(item.valorUnitario) * 100);
            const quantity = parseInt(item.quantidade);
            const itemTotalCents = unitPriceCents * quantity;

            if (item.produto) {
                totalCents += itemTotalCents;
            } else {
                oldTotalCents += itemTotalCents + totalCents;
                hasDeletedProduto = true;
            }
        });

        total = totalCents / 100;
        modalItemsTotalEl.innerText = total.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        });

        if (hasDeletedProduto) {
            const oldTotal = oldTotalCents / 100;
            modalItemsTotalOldEl.innerText = oldTotal.toLocaleString('pt-BR', {
                style: 'currency',
                currency: 'BRL'
            });
            modalItemsTotalOldEl.classList.remove('d-none');
        } else {
            modalItemsTotalOldEl.classList.add('d-none');
        }
    }

    let tempId = 0;
    function renderCompraItem(item) {
        const li = document.createElement('li');
        li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-start');

        // Calculate price with discount
        const precoCents = Math.round(item.valorUnitario * 100);
        const total = (precoCents * item.quantidade) / 100;

        // Produto info container
        const mainContainer = document.createElement('div');
        mainContainer.classList.add('flex-grow-1');

        const nomeEl = document.createElement('div');
        nomeEl.classList.add('fw-semibold');

        const qtdPrecoEl = document.createElement('small');
        qtdPrecoEl.classList.add('text-muted', 'd-block');

        if (item.produto == null) {
            nomeEl.textContent = '[Produto removido]';
            nomeEl.classList.add('text-muted');
            qtdPrecoEl.innerHTML = `<del>Qtd. ${item.quantidade} × R$ ${item.valorUnitario.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</del>`;
        } else {
            nomeEl.textContent = item.produto.nome;
            qtdPrecoEl.textContent = `Qtd. ${item.quantidade} × R$ ${item.valorUnitario.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
        }


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

        if (item.produto != null) {
            const ID = parseInt(item.produto.idProduto);

            const editButton = document.createElement('button');
            editButton.classList.add('btn', 'btn-sm', 'btn-outline-primary');
            editButton.setAttribute('title', 'Editar');
            editButton.innerHTML = '<i class="bi bi-pencil"></i>';

            const deleteButton = document.createElement('button');
            deleteButton.classList.add('btn', 'btn-sm', 'btn-outline-danger');
            deleteButton.setAttribute('title', 'Excluir');
            deleteButton.innerHTML = '<i class="bi bi-trash"></i>';

            editButton.dataset.idProduto = ID;
            deleteButton.dataset.idProduto = ID;

            editButton.addEventListener('click', () => {
                const itemIndex = itens.findIndex(it => it.produto && it.produto.idProduto === ID);
                currentItemIndex = itemIndex;
                populateItemFields(itens[itemIndex]);
            });

            deleteButton.addEventListener('click', () => {
                const index = itens.findIndex(it => it.produto && it.produto.idProduto === ID);
                if (index !== -1) {
                    itens.splice(index, 1);
                    refreshItems();
                }
            });

            buttonsContainer.appendChild(editButton);
            buttonsContainer.appendChild(deleteButton);
        } else {
            totalEl.classList.add('text-decoration-line-through')
        }

        rightContainer.appendChild(buttonsContainer);
        rightContainer.appendChild(totalEl);

        // Mount item
        li.appendChild(mainContainer);
        li.appendChild(rightContainer);

        return li;
    }

    /* ================== CONFIRM MODAL ================== */

    function showConfirmModal(id, action, message) {
        return new Promise((resolve) => {
            const confirmModal = bootstrap.Modal.getOrCreateInstance(modalConfirmEl);

            modalConfirmLabelEl.innerText = `Tem certeza de que deseja continuar?`;
            modalConfirmMsgEl.innerHTML = message;

            modalConfirmOkButtonEl.dataset.id = id;
            modalConfirmOkButtonEl.dataset.action = action;

            const okHandler = () => {
                cleanUp();
                resolve(true);
            };

            const cancelHandler = () => {
                cleanUp();
                resolve(false)
            };

            const cleanUp = () => {
                modalConfirmOkButtonEl.removeEventListener('click', okHandler);
                modalConfirmCancelButtonEl.removeEventListener('click', cancelHandler);
                confirmModal.hide();
            };

            modalConfirmOkButtonEl.addEventListener('click', okHandler);
            modalConfirmCancelButtonEl.addEventListener('click', cancelHandler);

            confirmModal.show();
        });
    }

    /* ================== DROPDOWNS ================== */

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
    itemSearchProduct.addEventListener("htmx:beforeRequest", (event) => {
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
            const itemIndex = itens.findIndex(item => item.produto != null && item.produto.idProduto === idProduto);

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

            addItemBtn.removeAttribute('disabled');
            modalQuantidadeEl.removeAttribute('disabled');
            modalValorUnitarioEl.removeAttribute('disabled');
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

    document.getElementById('confirm-register').addEventListener('click', () => {
        const id = parseInt(modalIdEl.value) || 0;

        modalFornecedorEl.querySelectorAll('option').forEach(opt => {
            if (opt.value === modalFornecedorEl.value) {
                modalFornecedorEl.dataset.id = opt.dataset.id;
                return;
            }
        });

        sendCompra(id);
    });

    document.getElementById('cancel-register').addEventListener('click', async () => {
        if (itens.length <= 0) return;

        const id = parseInt(modalIdEl.value) || 0;
        let message;
        message = `Tem certeza de que deseja cancelar o registro? Os inseridos no formulário serão perdidos.<br>Caso deseje sair temporariamente, clique em cancelar ou fora da janela.`;
        const confirm = await showConfirmModal(id, 'cancel-register', message);

        if (confirm) {
            lastEditId = null;
        } else {
            const modal = bootstrap.Modal.getOrCreateInstance(compraModalEl);
            modal.show();
        }
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
                const errorData = await response.text();
                document.getElementById('error-container').innerHTML = errorData;
                compraModal.hide();
                return;
            } else {
                // alert('Ação executada com sucesso.');
                refreshCompras();
                clearCompraModalFields();
                clearFormDraft();
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
                document.getElementById('error-container').innerHTML = errorData;
                return;
            } else {
                // alert('Ação executada com sucesso.');
                refreshCompras();
                return;
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
        const theItems = itens.filter(item => item.produto != null);

        return {
            idCompra: idCompra,
            data: data,
            itens: theItems,
            observacao: observacao,
            fornecedor: { idFornecedor: idFornecedor }
        }
    }
}