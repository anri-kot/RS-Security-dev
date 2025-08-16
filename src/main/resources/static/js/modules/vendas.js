export function init() {
    let alreadySet = false;

    let total = 0;
    let lastSearch = '';
    let lastEditId = null;
    let itens = [];

    let isVendaPage = location.pathname.includes('vendas');
    const STORAGE_KEY = 'venda-form-draft';

    const filterEl = document.getElementById('filter');
    const searchBy = document.getElementById('search-type');
    const searchTerm = document.getElementById('search-term');
    const metodoPagamentoEl = document.getElementById('metodoPagamento');
    const observacaoEl = document.getElementById('observacao');
    const vendaModalEl = document.getElementById('venda-modal');
    const autocompleteSearchProdutos = document.getElementById('autocompleteProdutosSearch');

    let currentItemIndex = null;

    const vendaFormEl = document.getElementById('venda-form');
    // Venda Modal fields
    const modalIdEl = document.getElementById('modal-venda-idVenda');
    const modalDataEl = document.getElementById('modal-venda-data');
    const modalObservacaoEl = document.getElementById('modal-venda-observacao');
    const modalMetodoPagamentoEl = document.getElementById('modal-venda-metodoPagamento');
    const modalValorRecebidoEl = document.getElementById('modal-venda-valorRecebido');
    const modalTrocoEl = document.getElementById('modal-venda-troco');
    const modalFuncionarioEl = document.getElementById('modal-venda-funcionario');
    const modalItensEl = document.getElementById('modal-venda-itens');
    // Venda Itens Modal fields
    const modalItemsTotalEl = document.getElementById('modal-items-total');
    const modalItemsTotalOldEl = document.getElementById('modal-items-total-old');
    const modalQuantidadeEl = document.getElementById('modal-venda-quantidade');
    const modalValorUnitarioEl = document.getElementById('modal-venda-valorUnitario');
    const modalDescontoEl = document.getElementById('modal-venda-desconto');
    const modalCurrentProdutoEl = document.getElementById('current-product');
    const modalFuncionarioIdEl = document.getElementById('modal-venda-funcionario-id');
    const modalFuncionarioUsernameEl = document.getElementById('modal-venda-funcionario-username');
    const modalFuncionarioValidationEl = document.getElementById('funcionario-validation');
    const selectedProdutoIdEl = document.getElementById('selected-product-id');
    const selectedProdutoNameEl = document.getElementById('selected-product-name')
    const addItemBtn = document.getElementById('add-button');
    // Confirm modal
    const modalConfirmEl = document.getElementById('confirma-modal');
    const modalConfirmLabelEl = document.getElementById('confirmar-label');
    const modalConfirmMsgEl = document.getElementById('confirmar-mensagem');
    const modalConfirmCancelButtonEl = document.getElementById('cancelar-acao');
    const modalConfirmOkButtonEl = document.getElementById('confirmar-acao');

    restoreFormDraft();

    // Keeps track of the current search and restores modal
    document.addEventListener('htmx:afterSwap', (e) => {
        isVendaPage = location.pathname.includes('vendas');
        if (!isVendaPage) return;

        if (!alreadySet) {
            lastSearch = location.search;
            restoreFormDraft();
            alreadySet = true;
        }

    });

    // Keeps track of the current search
    document.addEventListener('htmx:afterSettle', (e) => {
        lastSearch = location.search;
    });

    // Updates inputs when the filter changes
    filterEl.addEventListener('change', () => {
        toggleDateInputs();
    });

    // Updates filters according to the selected search method
    searchBy.addEventListener('change', () => {
        if (searchBy.value === 'idVenda') {
            for (let i = 0; i < filterEl.children.length; i++) {
                filterEl.children[i].removeAttribute('selected');
            }

            filterEl.children[0].setAttribute('selected', true);
            filterEl.setAttribute('disabled', true);
            metodoPagamentoEl.setAttribute('disabled', true);
            observacaoEl.setAttribute('disabled', true);

            toggleDateInputs();

        } else {
            filterEl.removeAttribute('disabled');
            metodoPagamentoEl.removeAttribute('disabled');
            observacaoEl.removeAttribute('disabled');
        }
    });

    searchTerm.addEventListener('htmx:config-request', (e) => {

        if (!searchBy.value.includes('produto')) {
            e.preventDefault();
        }
    });

    async function refreshVendas() {
        lastSearch = location.search;
        const url = `/vendas${lastSearch}`;
        htmx.ajax('GET', url, { target: '#conteudo', selected: '#cards-vendas' });
    }

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

    // MODALS

    // VENDA MODAL

    // Listens to new Venda button
    document.getElementById('new-venda').addEventListener('click', () => {
        showVendaModal();
    });

    // Listens to edit/remove buttons
    document.getElementById('cards-vendas').addEventListener('click', async (e) => {
        if (e.target.closest('button')) {
            const el = e.target.closest('button');

            if (el.dataset.action == 'edit') {
                showVendaModal(el.dataset.id);
            } else if (el.dataset.action == 'delete') {
                const id = el.dataset.id;
                const message = `Tem certeza que deseja deletar a venda número <strong>#${id}</strong>?`;

                const confirmed = await showConfirmModal(id, 'delete', message);
                if (confirmed) {
                    deleteVenda(id);
                }
                
                const confirmModal = bootstrap.Modal.getOrCreateInstance(modalConfirmEl);
                confirmModal.hide();
            }
        }
    });

    // Updates arrow symbol on toggle collapse
    document.getElementById('show-items-btn').addEventListener('click', () => {
        updateArrowToggle();
    });

    // Checks if DINHEIRO is selected
    modalMetodoPagamentoEl.addEventListener('change', (e) => {
        if (e.target.value !== 'DINHEIRO') {
            modalValorRecebidoEl.value = total.toFixed(2);
            modalTrocoEl.value = '0';
        } else {
            modalValorRecebidoEl.value = total.toFixed(2);
            updateTroco(parseFloat(modalValorRecebidoEl.value));
        }
    });

    // Updates troco if valorRecebido > total and parse valorRecebido value
    modalValorRecebidoEl.addEventListener('change', (e) => {
        if (modalMetodoPagamentoEl.value === 'DINHEIRO') {
            updateTroco(parseFloat(e.target.value));
        }
        e.target.value = parseFloat(e.target.value).toFixed(2);
    });
    modalValorRecebidoEl.addEventListener('keyup', (e) => {
        const value = e.target.value;
        if (modalMetodoPagamentoEl.value === 'DINHEIRO' && value >= total) {
            updateTroco(parseFloat(e.target.value));
        }
    });

    // Saves form to local storage
    vendaFormEl.addEventListener('input', (e) => {
        const el = e.target.closest('form');
        saveFormDraft(el);
    });

    modalFuncionarioEl.addEventListener('input', () => {
        validateFuncionario();
    });

    function updateArrowToggle() {
        const iconEl = document.getElementById('show-items-icon');
        if (iconEl.classList.contains('bi-chevron-down')) {
            iconEl.classList.remove('bi-chevron-down');
            iconEl.classList.add('bi-chevron-up');
        } else {
            iconEl.classList.remove('bi-chevron-up');
            iconEl.classList.add('bi-chevron-down');
        }
    }

    function validateFuncionario() {
        const value = modalFuncionarioEl.value;
        const nome = modalFuncionarioEl.dataset.nome;

        if (value === nome) {
            const id = parseInt(modalFuncionarioIdEl.value) || 0;
            const username = modalFuncionarioUsernameEl.value;

            if (id > 0 && username.length > 0) {
                modalFuncionarioValidationEl.innerText = `ID: ${id} USERNAME: ${username}`;
                modalFuncionarioValidationEl.classList.add("valid-feedback");
                modalFuncionarioValidationEl.classList.remove("invalid-feedback");
                modalFuncionarioEl.classList.add('is-valid');
                modalFuncionarioEl.classList.remove('is-invalid');
                return true;
            }
        }
        modalFuncionarioEl.classList.add('is-invalid');
        modalFuncionarioEl.classList.remove('is-valid');
        modalFuncionarioValidationEl.innerText = 'Funcionário não selecionado ou inválido. Selecione novamente.';
        modalFuncionarioValidationEl.classList.add('invalid-feedback');
        modalFuncionarioValidationEl.classList.remove('valid-feedback');
        return false;
    }

    function saveFormDraft() {
        const draft = {
            form: {
                idVenda: document.querySelector("#modal-venda-idVenda").value,
                data: document.querySelector("#modal-venda-data").value,
                observacao: document.querySelector("#modal-venda-observacao").value,
                metodoPagamento: document.querySelector("#modal-venda-metodoPagamento").value,
                valorRecebido: document.querySelector("#modal-venda-valorRecebido").value,
                troco: document.querySelector("#modal-venda-troco").value,
                funcionario: { 
                    id: parseInt(modalFuncionarioIdEl.value) || null,
                    nome: modalFuncionarioEl.dataset.nome,
                    username: modalFuncionarioUsernameEl.value
                }
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
                modalIdEl.value = form.idVenda || "";
                modalDataEl.value = form.data || "";
                modalObservacaoEl.value = form.observacao || "";
                modalMetodoPagamentoEl.value = form.metodoPagamento || "";
                modalValorRecebidoEl.value = form.valorRecebido || "";
                modalTrocoEl.value = form.troco || "";
                modalFuncionarioEl.value = form.funcionario.nome || "";
                modalFuncionarioEl.dataset.nome = form.funcionario.nome || "";
                modalFuncionarioIdEl.value = form.funcionario.id || "";
                modalFuncionarioUsernameEl.value = form.funcionario.username || "";

                const id = parseInt(form.idVenda) || 0;
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

    function updateTroco(receivedMoney) {
        const receivedMoneyCents = Math.round(receivedMoney * 100);
        const totalCents = Math.round(total * 100);
        const troco = (receivedMoneyCents - totalCents) / 100;

        if (total !== receivedMoney && troco < 0) {
            modalValorRecebidoEl.classList.add('is-invalid');
            modalTrocoEl.classList.add('is-invalid');
            vendaFormEl.reportValidity();
        } else {
            modalValorRecebidoEl.classList.remove('is-invalid');
            modalTrocoEl.classList.remove('is-invalid');
        }

        modalTrocoEl.value = troco.toFixed(2);
    }

    async function showVendaModal(id = 0) {
        const isTheSame = lastEditId === (parseInt(id) || 0);

        if (lastEditId !== null) {
            if (!isTheSame && itens.length > 0) {
                let message;
                if (lastEditId > 0) {
                    message = `Você estava editando a venda de <strong>ID ${lastEditId}</strong>. Se continuar as alterações não serão aplicadas.`;
                } else {
                    message = `Você estava editando uma nova venda. Se continuar o registro será perdido.`;
                }

                const confirmed = await showConfirmModal(id, 'switch-edit', message);
                if (!confirmed) {
                    return;
                }

                lastEditId = null;
                clearVendaModalFields();
            }
        } else {
            clearVendaModalFields();
        }

        if (id > 0 && !isTheSame) {
            let venda;

            try {
                const response = await fetch(`/api/venda/${id}`);
                if (!response.ok) throw new Error('Erro na requisição');
                venda = await response.json();
            } catch (e) {
                console.error('Erro inesperado:', e);
                alert('Ocorreu um erro inesperado.');
                return;
            }

            itens = venda.itens;
            populateVendaModal(venda);
        }
        
        lastEditId = parseInt(id);
        const vendaModal = bootstrap.Modal.getOrCreateInstance(vendaModalEl);

        saveFormDraft();
        validateFuncionario();
        vendaModal.show();
    }

    function populateVendaModal(venda) {
        modalIdEl.value = venda.idVenda;
        modalDataEl.value = venda.data;

        modalObservacaoEl.value = venda.observacao;

        modalMetodoPagamentoEl.value = venda.metodoPagamento;
        modalValorRecebidoEl.value = parseFloat(venda.valorRecebido).toFixed(2);
        modalTrocoEl.value = parseFloat(venda.troco).toFixed(2) || '';
        modalFuncionarioEl.value = `${venda.usuario.nome} ${venda.usuario.sobrenome}`;
        modalFuncionarioEl.dataset.nome = modalFuncionarioEl.value;
        modalFuncionarioIdEl.value = venda.usuario.idUsuario;
        modalFuncionarioUsernameEl.value = venda.usuario.username;

        refreshItems();
    }

    function clearVendaModalFields() {

        clearVendaFormFields();
        clearItemsFormFields();
        currentItemIndex = null;
        itens = [];
    }

    function clearVendaFormFields() {
        modalTrocoEl.setAttribute('disabled', true);
        vendaFormEl.querySelectorAll('input').forEach(el => {
            el.value = '';

            if (el.type.includes('date')) {
                const today = new Date();

                const year = today.getFullYear();
                const month = String(today.getMonth() + 1).padStart(2, '0');
                const day = String(today.getDate()).padStart(2, '0');
                const hours = String(today.getHours()).padStart(2, '0');
                const minutes = String(today.getMinutes()).padStart(2, '0');

                el.value = `${year}-${month}-${day}T${hours}:${minutes}`;
            }
        });
        modalItensEl.innerHTML = '';
        modalValorRecebidoEl.setAttribute('min', '0.05');
        modalItemsTotalEl.innerText = 'R$ 0,00';

        modalItemsTotalOldEl.innerHTML = '';
        modalItemsTotalOldEl.classList.add('d-none');
    }

    // MODAL ITEMS

    let lastQuery;
    const searchType = document.getElementById('item-search-type');
    const searchProduct = document.getElementById('search-product');
    const autocompleteProdutoOptions = document.getElementById('autocomplete-produto-options');
    const autocompleteFuncionarioOptions = document.getElementById('autocomplete-funcionario-options');

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
        const itemsFormEl = document.getElementById('items-form');
        if (itemsFormEl.checkValidity()) {
            addUpdateItem();
        } else {
            itemsFormEl.reportValidity();
        }
    });

    // Validates funcionario
    modalFuncionarioEl.addEventListener('change', (e) => {
        const value = e.target.value;
        if (value !== e.target.dataset.nome) {
            modalFuncionarioEl.setAttribute('isvalid', false);
        }
    });

    function clearItemsFormFields() {
        const itemsForm = document.getElementById('items-form');
        itemsForm.querySelectorAll('input').forEach(el => el.value = '');
        modalCurrentProdutoEl.innerText = 'Aguardando selecionar produto...';

        modalValorUnitarioEl.setAttribute('disabled', true);
        modalQuantidadeEl.setAttribute('disabled', true);
        modalDescontoEl.setAttribute('disabled', true);
        modalDescontoEl.value = '0';

        addItemBtn.setAttribute('disabled', true);
    }

    function addUpdateItem() {

        const idProduto = parseInt(selectedProdutoIdEl.value);
        const nome = selectedProdutoNameEl.value;
        const quantidade = parseInt(modalQuantidadeEl.value);
        const valorUnitario = parseFloat(modalValorUnitarioEl.value);
        let desconto = parseFloat(modalDescontoEl.value) || null;

        if (currentItemIndex != null) {
            itens[currentItemIndex].quantidade = quantidade;
            itens[currentItemIndex].valorUnitario = valorUnitario;
            itens[currentItemIndex].desconto = parseFloat(modalDescontoEl.value);
        } else {
            itens.push({
                quantidade: quantidade,
                valorUnitario: valorUnitario,
                produto: {
                    idProduto: idProduto,
                    nome: nome
                },
                desconto: desconto
            });
        }

        saveFormDraft();
        refreshItems();
        clearItemsFormFields();
    }

    function refreshItems() {        
        modalItensEl.innerHTML = '';
        let oldTotalCents = 0;
        let totalCents = 0;
        let hasDeletedProduto = false;

        itens.forEach(item => {
            modalItensEl.appendChild(renderVendaItem(item));

            // calculating total
            const unitPriceCents = Math.round(parseFloat(item.valorUnitario) * 100);
            const discount = parseFloat(item.desconto || 0) / 100;
            const discountMultiplier = 1 - (discount / 100);
            const quantity = parseInt(item.quantidade);
            const itemTotalCents = unitPriceCents * discountMultiplier * quantity;

            if (item.produto) {
                totalCents += itemTotalCents;
            } else {
                oldTotalCents += itemTotalCents + totalCents;
                hasDeletedProduto = true;
            }
        });

        // cents to real
        total = totalCents / 100;

        modalValorRecebidoEl.setAttribute('min', total);
        modalItemsTotalEl.innerText = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

        const currentValue = parseFloat(modalValorRecebidoEl.value);
        if (modalMetodoPagamentoEl.value !== 'DINHEIRO') {

            if (currentValue < total) {
                modalValorRecebidoEl.value = total.toFixed(2);
            }
        } else {
            updateTroco(currentValue);
        }

        if (hasDeletedProduto) {
            const oldTotal = oldTotalCents / 100;
            modalItemsTotalOldEl.innerText = oldTotal.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            modalItemsTotalOldEl.classList.remove('d-none');
        }
    }

    function renderVendaItem(item) {
        const li = document.createElement('li');
        li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-start');

        const desconto = item.desconto || 0;
        const precoComDesconto = item.valorUnitario * (1 - desconto / 100);
        const total = precoComDesconto * item.quantidade;

        const mainContainer = document.createElement('div');
        mainContainer.classList.add('flex-grow-1');

        const nomeEl = document.createElement('div');
        nomeEl.classList.add('fw-semibold');

        const qtdPrecoEl = document.createElement('small');
        qtdPrecoEl.classList.add('text-muted', 'd-block');

        const descontoEl = document.createElement('small');
        descontoEl.classList.add('text-danger', 'd-block');

        if (item.produto == null) {
            nomeEl.textContent = '[Produto removido]';
            nomeEl.classList.add('text-muted');
            qtdPrecoEl.innerHTML = `<del>Qtd. ${item.quantidade} × R$ ${item.valorUnitario.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}</del>`;
            descontoEl.classList.add('text-muted', 'text-decoration-line-through');
        } else {
            nomeEl.textContent = item.produto.nome;
            qtdPrecoEl.textContent = `Qtd. ${item.quantidade} × R$ ${item.valorUnitario.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;
            if (desconto > 0) {
                descontoEl.textContent = `${desconto}%`;
                mainContainer.appendChild(descontoEl);
            }
        }

        mainContainer.appendChild(nomeEl);
        mainContainer.appendChild(qtdPrecoEl);

        const rightContainer = document.createElement('div');
        rightContainer.classList.add('d-flex', 'flex-column', 'justify-content-between', 'align-items-end');
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
                
                bootstrap.Collapse.getOrCreateInstance("#modal-venda-items-container").show();
                updateArrowToggle();
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

        li.appendChild(mainContainer);
        li.appendChild(rightContainer);

        return li;
    }

    function populateItemFields(item) {
        modalCurrentProdutoEl.innerHTML = `Editando <strong>${item.produto.nome}</strong>`;

        modalQuantidadeEl.value = item.quantidade || 1;
        modalValorUnitarioEl.value = parseFloat(item.valorUnitario || item.produto.precoAtual || '0.05').toFixed(2);
        modalValorUnitarioEl.setAttribute('min', item.valorUnitario);
        if (item.desconto && item.desconto !== 0) {
            modalDescontoEl.value = parseFloat(item.desconto || '0.00').toFixed(2);
        }

        modalValorUnitarioEl.removeAttribute('disabled');
        modalQuantidadeEl.removeAttribute('disabled');
        modalDescontoEl.removeAttribute('disabled');
        addItemBtn.removeAttribute('disabled');

        selectedProdutoIdEl.value = item.produto.idProduto;
        selectedProdutoNameEl.value = item.produto.nome;
    }

    // CONFIRM MODAL

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

    /* === DROPDOWNS === */

    const dropdownsIds = [autocompleteFuncionarioOptions.id, autocompleteProdutoOptions.id, autocompleteSearchProdutos.id];

    // Listens for DROPDOWN clicks
    dropdownsIds.forEach((id) => {
        document.getElementById(id).addEventListener('click', (e) => {
            const itemEl = e.target.closest('li');
            e.target.classList.remove('show');
            clickDropdown(itemEl, id);
        });
    });

    // Disables categoria if search types === ID
    searchType.addEventListener('change', (e) => {
        if (e.target.value === 'id') {
            document.getElementById('categoria').setAttribute('disabled', true);
        } else {
            document.getElementById('categoria').removeAttribute('disabled');
        }
    });

    // Show dropdown on focus
    searchTerm.addEventListener('focus', () => {
        if (searchBy.value === 'produtoId' || searchBy.value === 'produtoNome') {
            updateDropdown('term');
        }
    });
    searchProduct.addEventListener('focus', () => {
        updateDropdown('produto');
    });
    modalFuncionarioEl.addEventListener('focus', () => {
        updateDropdown('funcionario');
    });

    // Hide dropdowns when user clicks outside the search field
    document.addEventListener('click', (event) => {
        if (dropdownsIds.includes(event.target.id) || !event.target.classList.contains('input-dropdown')) {
            document.querySelectorAll('.dropdown-menu').forEach((el) => { el.classList.remove('show') });
        }
    });

    // checks if search field is blank or < 3 before request
    document.body.addEventListener("htmx:beforeRequest", (event) => {
        if (!isVendaPage) return;
        if (event.target.id === searchProduct.id) {
            const value = event.target.value.trim();

            if (value.length < 1) {
                event.preventDefault();
                return;
            } else if (searchType.value !== 'id' && (value.length < 3 || lastQuery === value)) {
                event.preventDefault();
            }
        }
    });

    // Updated dropdown on htmx requests
    document.body.addEventListener('htmx:afterSwap', (event) => {
        if (!isVendaPage) return;
        if (event.target.id === autocompleteProdutoOptions.id) {
            lastQuery = searchProduct.value.trim();
            updateDropdown('produto');
        }
    });

    async function clickDropdown(itemEl, targetId) {
        switch (targetId) {
            case autocompleteFuncionarioOptions.id:
                modalFuncionarioEl.value = itemEl.dataset.nome;
                modalFuncionarioEl.setAttribute('data-nome', itemEl.dataset.nome);
                modalFuncionarioIdEl.value = itemEl.dataset.idUsuario;
                modalFuncionarioUsernameEl.value = itemEl.dataset.username;
                validateFuncionario();
                break;
            case autocompleteProdutoOptions.id:
                const idProduto = parseInt(itemEl.dataset.idProduto);
                const itemIndex = itens.findIndex(item => item.produto != null && item.produto.idProduto === idProduto);

                searchProduct.value = itemEl.dataset.nomeProduto;

                if (itemIndex !== -1) {
                    populateItemFields(itens[itemIndex]);
                    currentItemIndex = itemIndex;
                } else {
                    const produto = await getProduto(idProduto);
                    populateItemFields({ produto })
                    modalCurrentProdutoEl.innerHTML = `Inserindo <strong>${itemEl.dataset.nomeProduto}</strong>`
                    currentItemIndex = null;
                };

                selectedProdutoIdEl.value = idProduto;
                selectedProdutoNameEl.value = itemEl.dataset.nomeProduto;
                break;
            case autocompleteSearchProdutos.id:
                searchTerm.value = itemEl.dataset.nomeProduto;
                break;

            default:
                break;
        }
    }

    function updateDropdown(source) {
        switch (source) {
            case 'produto':
                if (searchType.value === 'nome' && searchProduct.value.trim().length >= 3) {
                    autocompleteProdutoOptions.classList.add('show');
                } else if (searchProduct.value.trim().length != 0) {
                    autocompleteProdutoOptions.classList.add('show');
                }
                break;
            case 'funcionario':
                autocompleteFuncionarioOptions.classList.add('show');
                break;
            case 'term':
                autocompleteSearchProdutos.classList.add('show');
                break;
            default:
                break;
        }
    }

    async function getProduto(id) {
        let produto;
        try {
            const response = await fetch(`/api/produto/${id}`);
            if (!response.ok) {
                alert('Erro ao pesquisar produto.');
                console.error(response.json().message);
                return null;
            }

            produto = await response.json();
            return produto;
        } catch (e) { }
    }

    // ACTIONS CREATE / DELETE / UPDATE

    document.getElementById('confirm-register').addEventListener('click', (e) => {
        const id = parseInt(modalIdEl.value) || 0;
        if (!vendaFormEl.checkValidity() || !validateFuncionario()) {
            vendaFormEl.reportValidity();
            return;
        }

        modalFuncionarioEl.querySelectorAll('option').forEach(opt => {
            if (opt.value === modalFuncionarioEl.value) {
                modalFuncionarioEl.dataset.username = opt.dataset.username;
                return;
            }
        });

        sendVenda(id);
    });

    async function sendVenda(id) {
        let url;
        let method;
        const body = JSON.stringify(getVendaObj());

        if (id > 0) {
            url = `/api/venda/${id}`;
            method = 'PUT';
        } else {
            url = `/api/venda`;
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

            const vendaModal = bootstrap.Modal.getInstance(vendaModalEl);

            if (!response.ok) {
                const errorData = await response.text();
                document.getElementById('error-container').innerHTML = errorData;
                vendaModal.hide();
                return;
            } else {
                alert('Ação executada com sucesso.');
                refreshVendas();
                vendaModal.hide();
                clearFormDraft();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
    }

    async function deleteVenda(id) {
        try {
            const response = await fetch(`/api/venda/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Hx-Request': true
                },
                body: {}
            });

            if (!response.ok) {
                const errorData = await response.json();
                console.error(errorData);
                const errorMsg = errorData.message;
                document.getElementById('error-container').innerHTML = errorMsg;
                return;
            } else {
                alert('Ação executada com sucesso.');
                refreshVendas();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }

    }

    function getVendaObj() {
        const idVenda = parseInt(modalIdEl.value) || null;
        const data = modalDataEl.value;
        const observacao = modalObservacaoEl.value;
        const metodoPagamento = modalMetodoPagamentoEl.value;
        const valorRecebido = parseFloat(modalValorRecebidoEl.value);
        const troco = parseFloat(modalTrocoEl.value) || 0;
        const idFuncionario = parseInt(modalFuncionarioIdEl.value);
        const username = modalFuncionarioUsernameEl.value;
        const theItems = itens.filter(item => item.produto != null);

        return {
            idVenda: idVenda,
            data: data,
            itens: theItems,
            observacao: observacao,
            metodoPagamento: metodoPagamento,
            valorRecebido: valorRecebido,
            troco: troco,
            usuario: { idUsuario: idFuncionario, username: username }
        }
    }

}