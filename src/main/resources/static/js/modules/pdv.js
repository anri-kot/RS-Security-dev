export function init() {

    let lastQuery = '';
    let total = 0;
    let cart = [];
    let tempProduto = null;

    const cartItems = document.getElementById('cart-items');
    let obs = '';

    const searchProduct = document.getElementById('search-product');
    const autocompleteOptions = document.getElementById('autocomplete-options');
    const modalElement = document.getElementById("produtoModal");
    const searchType = document.getElementById('search-type');
    const descontoToTotalEl = document.getElementById('desconto-to-total-input');
    const descontoToTotalButtonEl = document.getElementById('desconto-to-total-btn');

    const dinheiroFields = document.getElementById('dinheiro-fields');


    /* === DROPDOWN === */

    // Bloqueia request se campo estiver vazio ou repetido
    document.body.addEventListener("htmx:beforeRequest", (event) => {
        if (event.target.id === searchProduct.id) {
            const value = event.target.value.trim();
            const isNomeSearch = searchType.value === 'nome';

            if ((isNomeSearch && value.length < 1) || (isNomeSearch && (value.length < 3 || lastQuery === value))) {
                event.preventDefault();
            }
        }
    });

    // Atualiza dropdown após o HTMX responder
    document.body.addEventListener('htmx:afterSwap', (event) => {
        if (event.target.id === autocompleteOptions.id) {
            lastQuery = searchProduct.value.trim();
            updateDropdown();
        }
    });

    // Lida com cliques (seleção OU fechamento)
    document.addEventListener('click', (event) => {
        const li = event.target.closest('li.list-group-item');

        if (li && autocompleteOptions.contains(li)) {
            const idProduto = li.dataset.idProduto;
            const nomeProduto = li.dataset.nomeProduto;
            selectProduto(idProduto, nomeProduto);
        } else if (!autocompleteOptions.contains(event.target) && event.target !== searchProduct) {
            autocompleteOptions.classList.remove('show');
        }
    });

    // Pressionar "Enter" seleciona primeiro item
    searchProduct.addEventListener('keydown', e => {
        if (e.key === 'Enter') {
            e.preventDefault();
            const first = autocompleteOptions.querySelector('li');
            if (!first) return;

            const id = first.dataset.idProduto;
            const nome = first.dataset.nomeProduto;
            selectProduto(id, nome);
        }
    });

    // Mostra dropdown ao focar
    searchProduct.addEventListener('focus', () => {
        updateDropdown();
    });

    // Atualiza visibilidade do dropdown
    function updateDropdown() {
        const value = searchProduct.value.trim();
        const isIdSearch = searchType.value === 'id';
        const hasOptions = autocompleteOptions.querySelectorAll('li').length > 0;

        if (
            (isIdSearch && value.length > 0) ||
            (!isIdSearch && value.length >= 3)
        ) {
            if (hasOptions) {
                autocompleteOptions.classList.add('show');
            }
        } else {
            autocompleteOptions.classList.remove('show');
        }
    }

    /* === CART === */

    // DESCONTO TO TOTAL
    descontoToTotalButtonEl.addEventListener('click', () => {
        const desconto = parseFloat(descontoToTotalEl.value);
        if (total > 0 && desconto > 0 && cart.length > 0) {
            cart.forEach(item => {
                item.desconto = desconto.toFixed(2);
            });
            renderCart();
        }
    });

    // SHOW MODAL
    document.querySelector('#search-form').addEventListener('submit', (event) => {

        event.preventDefault();
        const selectedId = document.querySelector('#selected-id').value;

        showProdutoModal(selectedId);
    });

    // REMOVE/EDIT ITEM
    cartItems.addEventListener("click", (e) => {
        if (e.target.classList.contains("btn-danger")) {
            const index = parseInt(e.target.dataset.index);
            if (!isNaN(index)) {
                cart.splice(index, 1);
                renderCart();
            }
        } else if (e.target.classList.contains("btn-secondary")) {
            const index = parseInt(e.target.dataset.index);
            editCart(index);
        }
    });

    function selectProduto(idProduto, nomeProduto) {
        document.querySelector('#selected-id').value = idProduto;
        document.querySelector('#search-product').value = nomeProduto;

        showProdutoModal(idProduto);

        // Hides dropdown after selection
        autocompleteOptions.classList.remove('show');
    }

    function addProductToCart(produto, quantidade, preco, desconto) {
        let exists = false;

        // If add item already exists in the cart
        cart.forEach(item => {
            if (item.produto.idProduto === parseInt(produto.idProduto)) {

                item.quantidade = parseInt(quantidade);
                item.desconto = parseFloat(desconto);
                item.preco = parseInt(preco);
                exists = true;
                return;
            }
        });

        if (!exists) {
            cart.push({
                "produto": produto,
                "quantidade": parseInt(quantidade),
                "valorUnitario": parseFloat(preco),
                "desconto": parseFloat(desconto)
            });
        }

        renderCart();
    }

    function editCart(index) {
        const selectedId = cart[index].produto.idProduto;
        showProdutoModal(selectedId);
    }

    function renderCart() {
        cartItems.innerHTML = '';

        total = 0;
        cart.forEach((item, index) => {
            const discount = item.desconto / 100 ?? 0;
            const priceCents = Math.round(item.valorUnitario * 100);
            const fullPriceCents = item.quantidade * priceCents;
            const discountCents = Math.round(fullPriceCents * discount);
            const finalPrice = (fullPriceCents - discountCents) / 100;
            total += finalPrice;

            const tr = document.createElement('tr');
            tr.innerHTML = `
            <th>${cart.indexOf(item) + 1}</th>
            <td>${item.produto.nome}</td>
            <td>${item.quantidade}</td>
            <td>R$ ${item.valorUnitario.toFixed(2)}</td>
            <td>${discount * 100}</td>
            <td>R$ ${finalPrice.toFixed(2)}</td>
            <td><button class="btn btn-sm btn-danger" data-index="${index}">Remover</button>
            <button class="btn btn-sm btn-secondary" data-index="${index}">Editar</button></td>`;

            cartItems.appendChild(tr);
        });

        document.getElementById('total').textContent = total.toLocaleString('pt-BR', { minimumFractionDigits: 2 });
    }

    /* === MODAL === */

    // Colects modal data
    modalElement.addEventListener("click", function (event) {

        if (event.target.dataset.action === "confirm-modal") {

            const idProduto = document.getElementById("modal-produto-id").value;
            const nome = document.getElementById("modal-produto-nome").value;
            const quantidade = document.getElementById("modal-produto-quantidade").value;
            const preco = document.getElementById("modal-produto-preco").value;
            const desconto = document.getElementById("modal-produto-desconto").value;

            if (!quantidade || quantidade <= 0) {
                alert("Informe uma quantidade válida.");
                return;
            }

            if (parseFloat(desconto) / 100 > 0.99) {
                alert("Desconto inválido");
                return;
            }

            const produto = tempProduto;
            tempProduto = null;

            addProductToCart(produto, quantidade, preco, desconto);

            const modal = bootstrap.Modal.getInstance(modalElement);
            modal.hide();
        }
    });

    async function showProdutoModal(selectedId) {

        if (!selectedId) {
            alert("Selecione um produto primeiro.");
            return;
        }

        try {
            const modal = new bootstrap.Modal(modalElement);

            if (cart != null) {
                for (let item of cart) {
                    if (parseInt(selectedId) === item.produto.idProduto) {
                        tempProduto = item.produto;
                        populateModal(tempProduto, item.quantidade, item.valorUnitario, item.desconto);
                        modal.show();
                        return;
                    }
                }
            }

            const response = await fetch(`/api/produto/${selectedId}`);
            if (!response.ok) throw new Error("Erro na requisição");

            tempProduto = await response.json();

            // Populate fields
            populateModal(tempProduto, 1, tempProduto.precoAtual, '0.00');

            modal.show();

        } catch (e) {
            alert("Erro ao carregar produto.");
            console.error(e);
        }
    }

    // Populate fields
    function populateModal(produto, quantidade, valorUnitario, desconto) {
        document.getElementById("modal-produto-id").value = produto.idProduto;
        document.getElementById("modal-produto-nome").value = produto.nome;
        document.getElementById("modal-produto-categoria").value = produto.categoria.nome;
        document.getElementById("modal-produto-preco").value = parseFloat(valorUnitario).toFixed(2);
        document.getElementById("modal-produto-desconto").value = parseFloat(desconto).toFixed(2) || '0.00';
        document.getElementById("modal-produto-quantidade").value = quantidade;
    }

    /* === SELL ==*/

    const sellModal = new bootstrap.Modal(document.getElementById('confirmarVendaModal'));
    const valorRecebidoEl = document.getElementById('valorRecebido');
    const trocoContainer = document.getElementById('troco-container')
    const trocoEl = document.getElementById('troco-value');

    // Show confirm sell modal
    document.getElementById('finalizar-venda').addEventListener('click', () => {
        if (cart.length === 0) {
            alert('Carrinho vazio');
            return;
        }

        document.getElementById('total-confirm').innerText = total.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

        sellModal.show();
    });

    // Listens to metodoPagamento
    document.getElementById('metodoPagamento').addEventListener('change', (e) => {
        if (e.target.value = 'DINHEIRO') {
            valorRecebidoEl.value = total.toFixed(2);
            valorRecebidoEl.removeAttribute('disabled');

            dinheiroFields.classList.remove('d-none');
            trocoContainer.classList.remove('d-none');
        } else {
            dinheiroFields.classList.add('d-none');
            trocoContainer.classList.add('d-none');
            valorRecebidoEl.setAttribute('disabled', true);
        }
    });

    // Listens to valorRecebido and calculates troco
    valorRecebidoEl.addEventListener('keyup', (e) => {
        const valorRecebido = valorRecebidoEl.value;
        trocoEl.innerText = getChange(valorRecebido).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    });

    // Listens to valorRecebido on change
    // Listens to valorRecebido and calculates troco
    valorRecebidoEl.addEventListener('change', (e) => {
        const valorRecebido = valorRecebidoEl.value;
        trocoEl.innerText = getChange(valorRecebido).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    });

    // Confirm
    document.getElementById('confirmar-venda').addEventListener('click', sellCart);

    // Cancel
    document.getElementById('cancelar-venda').addEventListener('click', () => {
        cart = [];
        renderCart();
    });

    function getChange(valorRecebido) {
        // to cents
        const receivedCents = valorRecebido * 100;
        const totalCents = total * 100;

        return (receivedCents - totalCents) / 100;
    }

    async function sellCart() {
        const observacao = document.getElementById('observacao');
        const metodoPagamento = document.getElementById('metodoPagamento').value;

        if (!metodoPagamento || metodoPagamento.length == 0) {
            alert('Insira um método de pagamento');
            return;
        }

        let valorRecebido;

        if (metodoPagamento === 'DINHEIRO') {
            valorRecebido = parseFloat(valorRecebidoEl.value);
        } else {
            valorRecebido = total;
        }

        obs = observacao.value;

        if (cart.length === 0) {
            alert('Carrinho vazio');
            return;
        }

        const json = JSON.stringify({
            itens: cart,
            observacao: obs,
            metodoPagamento: metodoPagamento,
            valorRecebido: valorRecebido
        });

        try {
            const response = await fetch('/pdv/finalizar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'HX-Request': 'true'
                },
                body: json
            });

            if (response.ok) {
                const text = await response.text();
                alert(text);
                cart = [];
                obs = '';
                observacao.value = '';

                renderCart();
                sellModal.hide();
            } else {
                const errorData = await response.json();
                const errorMsg = errorData.message;
                console.error(`Erro ${errorData.status}: ${errorMsg}`);
                document.getElementById('error-container').innerHTML = `Erro ao salvar produto: ${errorMsg}`;
                observacao.value = '';
                sellModal.hide();
                return;
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
    }
}
