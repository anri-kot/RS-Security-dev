// js/modules/pdv.js
export function init() {

    /* TODO:
        DISPLAY ERRORS ON THE PAGE
    */

    let lastQuery = '';

    let cart = [];
    const cartItems = document.getElementById('cart-items');
    let obs = '';

    const searchProduct = document.getElementById('search-product');
    const autocompleteOptions = document.getElementById('autocomplete-options');
    const modalElement = document.getElementById("produtoModal");
    const finishSell = document.getElementById('finalizar-venda');
    const cancelSell = document.getElementById('cancelar-venda');

    /* === DROPDOWN === */

    // checks if search field is blank or < 3 before request
    document.body.addEventListener("htmx:beforeRequest", (event) => {

        if (!searchProduct) return;
        const value = searchProduct.value.trim();

        if (value.length < 3 || lastQuery.endsWith(value)) {
            event.preventDefault();
        }
    });

    // Updated dropdown on htmx requests
    document.body.addEventListener('htmx:afterSwap', (event) => {
        if (event.target.id === autocompleteOptions.id) {
            lastQuery = searchProduct.value.trim();
            updateDropdown();
        }
    });

    // Listens for click events in the dropdown
    document.body.addEventListener('click', (event) => {

        const li = event.target.closest('li.list-group-item');
        if (li && autocompleteOptions.contains(li)) {
            const idProduto = li.getAttribute('data-id-produto');
            const nomeProduto = li.getAttribute('data-nome-produto');

            selectProduto(idProduto, nomeProduto);
        }
    });


    // on 'enter', select the first element of the dropdown
    searchProduct.addEventListener('keydown', e => {
        if (e.key === 'Enter') {
            e.preventDefault();
            const first = document.querySelectorAll('#autocomplete-options li')[0];
            const id = first.dataset.idProduto;
            const nome = first.dataset.nomeProduto;

            selectProduto(id, nome);
        }
    });

    // Show dropdown on focus
    searchProduct.addEventListener('focus', () => {
        updateDropdown();
    });

    // Hide dropdown when user clicks outside the search field
    document.addEventListener('click', (event) => {
        if (!autocompleteOptions.contains(event.target) && event.target !== searchProduct) {
            autocompleteOptions.classList.remove('show');
        }
    });

    function updateDropdown() {

        if (searchProduct.value.trim().length < 3) {
            autocompleteOptions.classList.remove('show');
        } else {
            autocompleteOptions.classList.add('show');
        }
    }

    /* === ADD TO CART === */

    // listens for submit action and shows the modal
    document.querySelector('#search-form').addEventListener('submit', (event) => {

        event.preventDefault();
        const selectedId = document.querySelector('#selected-id').value;

        showProdutoModal(selectedId);
    });

    // listens for click on 'remove' button and removes the item from the cart
    cartItems.addEventListener("click", (e) => {
        if (e.target.classList.contains("btn-danger")) {
            const index = parseInt(e.target.dataset.index);
            if (!isNaN(index)) {
                cart.splice(index, 1);
                renderCart();
            }
        }
    });

    function selectProduto(idProduto, nomeProduto) {
        document.querySelector('#selected-id').value = idProduto;
        document.querySelector('#search-product').value = nomeProduto;

        showProdutoModal(idProduto);

        // Hides dropdown after selection
        autocompleteOptions.classList.remove('show');
    }

    function addProductToCart(idProduto, nome, quantidade, preco, desconto) {
        let exists = false;

        // If add item already exists in the cart
        cart.forEach(item => {
            if (item.produto.idProduto === parseInt(idProduto)) {

                item.quantidade += parseInt(quantidade);
                item.desconto = parseFloat(desconto);
                item.preco = parseInt(preco);
                exists = true;
                return;
            }
        });

        if (!exists) {
            cart.push({
                "produto": {
                    'idProduto': parseInt(idProduto),
                    "nome": nome
                },
                "quantidade": parseInt(quantidade),
                "valorUnitario": parseFloat(preco),
                "desconto": parseFloat(desconto)
            });
        }

        renderCart();
    }

    function renderCart() {
        cartItems.innerHTML = '';

        let total = 0;
        cart.forEach((item, index) => {
            const discount = item.desconto ?? 0;
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
            <td>${(discount * 100).toFixed(0)}</td>
            <td>R$ ${finalPrice.toFixed(2)}</td>
            <td><button class="btn btn-sm btn-danger" data-index="${index}">Remover</button></td>`;

            cartItems.appendChild(tr);
        });

        document.getElementById('total').textContent = total.toFixed(2);
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

            addProductToCart(idProduto, nome, quantidade, preco, desconto);

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
            const response = await fetch(`/pdv/produto/${selectedId}`);
            if (!response.ok) throw new Error("Erro na requisição");

            const produto = await response.json();

            // Populate fields
            document.getElementById("modal-produto-id").value = produto.idProduto;
            document.getElementById("modal-produto-nome").value = produto.nome;
            document.getElementById("modal-produto-categoria").value = produto.categoria.nome;
            document.getElementById("modal-produto-preco").value = produto.precoAtual.toFixed(2);
            document.getElementById("modal-produto-desconto").value = produto.desconto?.toFixed(2) || '0.00';

            const modal = new bootstrap.Modal(modalElement);
            modal.show();

        } catch (e) {
            alert("Erro ao carregar produto.");
            console.error(e);
        }
    }

    /* === SELL ==*/

    finishSell.addEventListener('click', sellCart);

    async function sellCart() {
        if (cart.length === 0) {
            alert('Carrinho vazio');
            return;
        }

        const json = JSON.stringify({
            itens: cart,
            observacao: obs,
            usuario: {
                'idUsuario': 3,
                'username': 'user',
                'nome': 'John',
                'sobrenome': 'Doe'
            }
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

                renderCart();
            } else {
                const errorData = await response.json();
                alert(`Erro ${errorData.status}: ${errorData.message}`);
                return;
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Ocorreu um erro inesperado.');
        }
    }

    cancelSell.addEventListener('click', () => {
        cart = [];
        renderCart();
    })
}
