/*
    TODO:
    - Send Vendas via POST and PUT
    - Testing
    - Pagination
*/

export function init() {

    const filterEl = document.getElementById('filter');
    const searchBy = document.getElementById('search-type');
    const metodoPagamentoEl = document.getElementById('metodoPagamento');
    const observacaoEl = document.getElementById('observacao');
    const vendaModalEl = document.getElementById('venda-modal');

    let currentItemIndex = null;

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
    const modalQuantidadeEl = document.getElementById('modal-venda-quantidade');
    const modalValorUnitarioEl = document.getElementById('modal-venda-valorUnitario');
    const modalDescontoEl = document.getElementById('modal-venda-desconto');
    const modalCurrentProdutoEl = document.getElementById('current-product');
    const modalFuncionarioIdEl = document.getElementById('modal-venda-funcionario-id');
    const modalFuncionarioUsernameEl = document.getElementById('modal-venda-funcionario-username');
    const selectedProdutoIdEl = document.getElementById('selected-product-id');
    const selectedProdutoNameEl = document.getElementById('selected-product-name')
    const addItemBtn = document.getElementById('add-button');
    // Confirm modal
    const modalConfirmEl = document.getElementById('confirma-modal');
    const modalConfirmLabelEl = document.getElementById('confirmar-label');
    const modalConfirmMsgEl = document.getElementById('confirmar-mensagem');
    const modalConfirmOkButtonEl = document.getElementById('confirmar-acao');

    filterEl.addEventListener('change', () => {
        toggleDateInputs();
    });

    searchBy.addEventListener('change', () => {
        document.getElementById('search-term').name = searchBy.value;

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

    const vendaModal = new bootstrap.Modal(vendaModalEl);
    let itens = [];

    // VENDA MODAL

    // Listens to new Venda button
    document.getElementById('new-venda').addEventListener('click', () => {
        showVendaModal();
    });

    // Listens to edit/remove buttons
    document.getElementById('cards-vendas').addEventListener('click', (e) => {
        if (e.target.closest('button')) {
            const el = e.target.closest('button');

            if (el.dataset.action == 'edit') {
                showVendaModal(el.dataset.id);
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

    // Checks if DINHEIRO is selected
    modalMetodoPagamentoEl.addEventListener('change', (e) => {
        if (e.target.value !== 'DINHEIRO') {
            modalTrocoEl.setAttribute('disabled', true);
        } else {
            modalTrocoEl.removeAttribute('disabled');
        }
    });

    async function showVendaModal(id) {
        clearVendaModalFields();        

        if (id || id > 0) {
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
        modalFuncionarioIdEl.value = venda.usuario.idUsuario;
        modalFuncionarioUsernameEl.value = venda.usuario.username;

        refreshItems();
    }

    function clearVendaModalFields() {
        modalTrocoEl.setAttribute('disabled', true);
        vendaModalEl.querySelectorAll('input').forEach(el => {
            el.value = '';
        });
        modalItensEl.innerHTML = '';
        modalValorRecebidoEl.setAttribute('min', '0.05');
        modalItemsTotalEl.innerText = 'R$ 0,00';
        modalCurrentProdutoEl.innerText = 'Aguardando selecionar produto...';

        currentItemIndex = null;
        itens = [];
    }

    // MODAL ITEMS
    let lastQuery;
    const searchType = document.getElementById('item-search-type');
    const searchProduct = document.getElementById('search-product');
    const searchUser = document.getElementById('modal-venda-funcionario');
    const autocompleteProdutoOptions = document.getElementById('autocomplete-produto-options');
    const autocompleteFuncionarioOptions = document.getElementById('autocomplete-funcionario-options');

    // Listens for ADICIONAR button click
    addItemBtn.addEventListener('click', () => {
        addUpdateItem();        
    });

    // Validates funcionario
    searchUser.addEventListener('change', (e) => {
        const value = e.target.value;
        if (value !== e.dataset.nome) {
            searchUser.setAttribute('isvalid', false);
        }
    });

    // Cancel submit
    document.getElementById('items-form').addEventListener('submit', e => e.preventDefault());

    function addUpdateItem() {
        const idProduto = parseInt(selectedProdutoIdEl.value);
        const nome = selectedProdutoNameEl.value;
        const quantidade = parseInt(modalQuantidadeEl.value);
        const valorUnitario = parseFloat(modalValorUnitarioEl.value);
        let desconto = null;

        if (currentItemIndex != null) {
            itens[currentItemIndex].quantidade = quantidade;
            itens[currentItemIndex].valorUnitario = valorUnitario;
            if (modalDataEl.value.length !== 0 && parseFloat(modalDescontoEl.value) > 0) {
                itens[currentItemIndex].desconto = parseFloat(modalDescontoEl.value);
            }
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

        refreshItems();
    }

    function refreshItems() {
        let total = 0;
        modalItensEl.innerHTML = '';
        itens.forEach(item => {
            modalItensEl.appendChild(renderVendaItem(item));
            
            // calculating total
            const discount = parseFloat(item.desconto || 0);
            const price = parseFloat(item.valorUnitario - (item.valorUnitario * (discount / 100)));
            total += price * item.quantidade;
        });

        modalValorRecebidoEl.value = total.toFixed(2);
        modalValorRecebidoEl.setAttribute('min', total);
        modalItemsTotalEl.innerText = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    }

    let tempId = 0;
    function renderVendaItem(item) {
        const li = document.createElement('li');
        li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-start');

        // Calculate price with discount
        const desconto = item.desconto || 0;
        const precoComDesconto = item.valorUnitario * (1 - desconto / 100);
        const total = precoComDesconto * item.quantidade;

        // Produto info container
        const mainContainer = document.createElement('div');
        mainContainer.classList.add('flex-grow-1');

        const nomeEl = document.createElement('div');
        nomeEl.classList.add('fw-semibold');
        nomeEl.textContent = item.produto.nome;

        const qtdPrecoEl = document.createElement('small');
        qtdPrecoEl.classList.add('text-muted', 'd-block');
        qtdPrecoEl.textContent = `Qtd. ${item.quantidade} × R$ ${item.valorUnitario.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}`;

        const descontoEl = document.createElement('small');
        descontoEl.classList.add('text-danger', 'd-block');
        if (desconto > 0) {
            descontoEl.textContent = `${desconto}%`;
        }

        mainContainer.appendChild(nomeEl);
        mainContainer.appendChild(qtdPrecoEl);
        if (desconto > 0) mainContainer.appendChild(descontoEl);

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

    function populateItemFields(item) {
        modalCurrentProdutoEl.innerHTML = `Editando <strong>${item.produto.nome}</strong>`;        

        modalQuantidadeEl.value = item.quantidade || 1;
        modalValorUnitarioEl.value = parseFloat(item.valorUnitario || item.produto.precoAtual || '0.05').toFixed(2);
        if (item.desconto && item.desconto !== 0) {
            modalDescontoEl.value = parseFloat(item.desconto || '0.00').toFixed(2);
        }
        document.getElementById('add-button').removeAttribute('disabled');
        selectedProdutoIdEl.value = item.produto.idProduto;
        selectedProdutoNameEl.value = item.produto.nome;
    }

    // CONFIRM MODAL

    const confirmModal = new bootstrap.Modal(modalConfirmEl);

    modalConfirmOkButtonEl.addEventListener('click', (e) => {
        deleteVenda(e.target.dataset.id);
    });

    function showConfirmModal(id) { 
        if (!id) return;

        modalConfirmLabelEl.innerText = 'Confirmar delete';
        modalConfirmOkButtonEl.dataset.id = id;
        modalConfirmMsgEl.innerHTML = `Tem certeza que deseja deletar a venda número <strong>#${id}</strong>?`;

        confirmModal.show();
    }

    /* === DROPDOWNS === */

    // Listens for DROPDOWN clicks
    autocompleteProdutoOptions.addEventListener('click', (e) => {
        const itemEl = e.target.closest('li');
        autocompleteProdutoOptions.classList.remove('show');
        clickDropdown(itemEl, 'produto');
    });
    autocompleteFuncionarioOptions.addEventListener('click', (e) => {
        const itemEl = e.target.closest('li');
        autocompleteFuncionarioOptions.classList.remove('show');
        clickDropdown(itemEl, 'funcionario');
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
    searchProduct.addEventListener('focus', () => {
        updateDropdown('produto');
    });
    searchUser.addEventListener('focus', () => {
        updateDropdown('funcionario');
    });

    // Hide dropdown when user clicks outside the search field
    document.addEventListener('click', (event) => {
        if (!autocompleteProdutoOptions.contains(event.target) && event.target !== searchProduct) {
            autocompleteProdutoOptions.classList.remove('show');
        }
    });

    // checks if search field is blank or < 3 before request
    document.body.addEventListener("htmx:beforeRequest", (event) => {
        if (!searchProduct) return;

        if (event.target.id === searchProduct.id) {
            const value = searchProduct.value.trim();

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
        if (event.target.id === autocompleteProdutoOptions.id) {
            lastQuery = searchProduct.value.trim();
            updateDropdown('produto');
        }
    });

    async function clickDropdown(itemEl, source) {
        if (source === 'funcionario') {
            searchUser.value = itemEl.dataset.nome;
            modalFuncionarioIdEl.value = itemEl.dataset.idUsuario;
            modalFuncionarioUsernameEl.value = itemEl.dataset.username;
        } else {

            const idProduto = parseInt(itemEl.dataset.idProduto);
            const itemIndex = itens.findIndex(item => item.produto.idProduto === idProduto);
    
            searchProduct.value = itemEl.dataset.nomeProduto;
    
            if (itemIndex !== -1) {
                populateItemFields(itens[itemIndex]);
                currentItemIndex = itemIndex;
            } else {
                const produto = await getProduto(idProduto);
                populateItemFields({produto})
                modalCurrentProdutoEl.innerHTML = `Inserindo <strong>${itemEl.dataset.nomeProduto}</strong>`
                currentItemIndex = null;
            };
    
            document.getElementById('add-button').removeAttribute('disabled');
            selectedProdutoIdEl.value = idProduto;
            selectedProdutoNameEl.value = itemEl.dataset.nomeProduto;
        }
    }

    function updateDropdown(source) {
        if (searchType.value === 'nome' && source === 'produto') {
            if (searchProduct.value.trim().length >= 3) {
                autocompleteProdutoOptions.classList.add('show');
            }
        } else if (source === 'produto') {
            if (searchProduct.value.trim().length != 0) {
                autocompleteProdutoOptions.classList.add('show');
            }
        } else {
            autocompleteFuncionarioOptions.classList.add('show');
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
        } catch (e) {}
    }

    // ACTIONS CREATE / DELETE / UPDATE

    document.getElementById('confirm-register').addEventListener('click', (e) => {
        const id = parseInt(modalIdEl.value) || 0;

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

            if (!response.ok) {
                const errorData = await response.json();
                alert(`Erro ${errorData.status}: ${errorData.message}`);
                vendaModal.hide();
                return;
            } else {
                alert('Ação executada com sucesso.');
                vendaModal.hide();
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
                alert(`Erro ${errorData.status}: ${errorData.message}`);
                return;
            } else {
                alert('Ação executada com sucesso.');
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
        const troco = parseFloat(modalTrocoEl.value) || null;
        const idFuncionario = parseInt(modalFuncionarioIdEl.value);
        const username = modalFuncionarioUsernameEl.value;

        return {
            idVenda: idVenda,
            data: data,
            itens: itens,
            observacao: observacao,
            metodoPagamento: metodoPagamento,
            valorRecebido: valorRecebido,
            troco: troco,
            usuario: { idUsuario: idFuncionario, username: username }
        }
    }

}