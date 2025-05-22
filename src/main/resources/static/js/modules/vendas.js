export function init() {

    const filterEl = document.getElementById('filter');
    const searchBy = document.getElementById('search-type');
    const metodoPagamentoEl = document.getElementById('metodoPagamento');
    const observacaoEl = document.getElementById('observacao');
    const vendaModalEl = document.getElementById('venda-modal');

    // Venda Modal fields
    const modalIdEl = document.getElementById('modal-venda-idVenda');
    const modalDataEl = document.getElementById('modal-venda-data');
    const modalObservacaoEl = document.getElementById('modal-venda-observacao');
    const modalMetodoPagamentoEl = document.getElementById('modal-venda-metodoPagamento');
    const modalValorRecebidoEl = document.getElementById('modal-venda-valorRecebido');
    const modalTrocoEl = document.getElementById('modal-venda-troco');
    const modalFuncionarioEl = document.getElementById('modal-venda-funcionario');
    const modalItensEl = document.getElementById('modal-venda-itens');

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

    // VENDA MODAL

    const vendaModal = new bootstrap.Modal(vendaModalEl);

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
            }
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
        modalFuncionarioEl.value = venda.usuario.idUsuario;

        venda.itens.forEach(item => {
            modalItensEl.appendChild(renderVendaItem(item));
        });
    }

    function clearVendaModalFields() {
        modalTrocoEl.setAttribute('disabled', true);
        vendaModalEl.querySelectorAll('input').forEach(el => {
            el.value = '';
        });
        modalItensEl.innerHTML = '';
    }

    // Renders an 'li' element in the 'Itens' section of the modal
    function renderVendaItem(item) {
        const li = document.createElement('li');

        // calculating total
        const price = item.valorUnitario - (item.valorUnitario * item.desconto / 100) || item.valorUnitario;

        const total = price * item.quantidade;

        const mainContainer = document.createElement('div');
        const totalContainer = document.createElement('div');
        const nomeEl = document.createElement('div');
        const qtdPrecoEl = document.createElement('small');
        const descontoEl = document.createElement('small');
        const totalEl = document.createElement('span');

        // styling
        li.classList.add('list-group-item');
        totalContainer.classList.add('text-end');
        nomeEl.classList.add('fw-semibold');
        qtdPrecoEl.classList.add('text-muted', 'd-block');
        descontoEl.classList.add('text-danger', 'd-block');
        totalEl.classList.add('fw-bold', 'text-dark');

        // setting inner text
        nomeEl.innerText = item.produto.nome;
        qtdPrecoEl.innerText = `Qtd. ${item.quantidade} × R$ ${item.valorUnitario.toLocaleString('pt-BR', { minimumFractionDigits: 2})}`
        descontoEl.innerText = `${item.desconto}%`;
        totalEl.innerText = `R$ ${total.toLocaleString('pt-BR', { minimumFractionDigits: 2})}`;

        mainContainer.appendChild(nomeEl);
        mainContainer.append(qtdPrecoEl);
        if ((item.desconto) && item.desconto > 0) mainContainer.appendChild(descontoEl);
        totalContainer.appendChild(totalEl);
        li.appendChild(mainContainer);
        li.appendChild(totalContainer);

        return li;
    }

    /* === DROPDOWN === */
    let lastQuery;
    const searchProduct = document.getElementById('search-product');
    const autocompleteOptions = document.getElementById('autocomplete-options')

    // checks if search field is blank or < 3 before request
    document.body.addEventListener("htmx:beforeRequest", (event) => {
        if (!searchProduct) return;

        if (event.target.id === searchProduct.id) {
            const searchType = document.getElementById('search-type');
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
        if (event.target.id === autocompleteOptions.id) {
            lastQuery = searchProduct.value.trim();
            updateDropdown();
        }
    });

    function updateDropdown() {
        if (searchType.value === 'nome') {
            if (searchProduct.value.trim().length >= 3) {
                autocompleteOptions.classList.add('show');
            }
        } else {
            if (searchProduct.value.trim().length != 0) {
                autocompleteOptions.classList.add('show');
            }
        }
    }

    // ACTIONS CREATE / DELETE / UPDATE

    document.getElementById('confirm-register').addEventListener('click', (e) => {
        const id = parseInt(modalIdEl.value) || 0;
        sendVenda(id);
    });

    async function sendVenda(id) {
        let URL;
        let method;
        if (id > 0) {
            URL = `/api/venda/${id}`;
            method = 'PUT';
        } else {
            URL = `/api/venda`;
            method = 'POST'
        }

        getVendaObj()
    }

    function getVendaObj() {
        const idVenda = parseInt(modalIdEl.value) || null;
        const data = modalDataEl.value;
        const metodoPagamento = modalMetodoPagamentoEl.value;
        const valorRecebido = parseFloat(modalValorRecebidoEl.value);
        const troco = parseFloat(modalTrocoEl) || null;
        const idFuncionario = parseInt(modalFuncionarioEl.value);

        return {
            idVenda: idVenda,
            data: data,
            metodoPagamento: metodoPagamento,
            valorRecebido: valorRecebido,
            troco: troco,
            usuario: { idUsuario: idFuncionario }
        }
    }

}