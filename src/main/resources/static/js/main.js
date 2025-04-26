document.addEventListener("DOMContentLoaded", () => {
  const pathname = window.location.pathname.replace("/", "");

  if (pathname == "" || pathname == "home") {
    loadPage("home")
  }

  loadPage(pathname);
})

function loadPage(page) {
  fetch(`/${page}`, {
    headers: {"is-update": "true"}
  })
      .then(res => res.text())
      .then(html => {
          document.getElementById("content").innerHTML = html;
      })
      .catch(() => alert("Erro ao carregar a página."));
}

function searchFornecedor() {
  nome = document.getElementById("fornecedor").value;

  if (nome.length > 2) {
    fetch('/api/fornecedor/search?nome=' + nome)
      .then(res => res.json())
      .then(data => {
        fornecedores.innerHTML = "";

        data.forEach(fornecedor => {
          item = document.createElement("option");
          item.value = fornecedor.nome;
          fornecedores.appendChild(item);
        });
      })
  }
}