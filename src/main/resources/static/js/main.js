document.getElementById("listarProdutos").addEventListener("click", () => {
    console.log("test")
  fetch("/rssecurity/api/produto")
    .then(res => res.json())
    .then(produtos => {
      const div = document.getElementById("resultado");
      if (produtos.length === 0) {
        div.innerHTML = "<p>Nenhum produto encontrado.</p>";
        return;
      }

      div.innerHTML = produtos.map(p =>
        `<div class="card mb-2">
          <div class="card-body">
            <h5>${p.nome}</h5>
            <p>Preço: R$ ${p.precoAtual.toFixed(2)}</p>
          </div>
        </div>`
      ).join("");
    });
});

document.getElementById("formProduto").addEventListener("submit", (e) => {
  e.preventDefault();

  const nome = document.getElementById("nome").value;
  const preco = document.getElementById("preco").value;

  fetch("/resecurity/api/produto", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nome, precoAtual: parseFloat(preco) })
  })
  .then(res => {
    if (!res.ok) throw new Error("Erro ao salvar produto.");
    return res.json();
  })
  .then(() => {
    document.getElementById("formProduto").reset();
    document.querySelector("#modalProduto .btn-close").click();
    document.getElementById("listarProdutos").click(); // atualiza a lista
  })
  .catch(err => alert(err.message));
});