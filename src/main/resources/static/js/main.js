document.addEventListener("DOMContentLoaded", loadModule);
let currentModule;
const conteudo = document.getElementById('conteudo');

document.addEventListener("htmx:afterSettle", (e) => {
  if (e.target.id === conteudo.id) {
    loadModule();
  }
});

function loadModule() {
  const pageId = document.querySelector("[data-page-id]")?.dataset.pageId;
  
  if (!pageId) return;

  if (currentModule !== null && currentModule !== pageId) {

  import(`/js/modules/${pageId}.js`)
    .then(module => module.init())
    .catch(err => console.warn(`Não foi possível carregar módulo para ${pageId}`, err));
  }
  currentModule = pageId;
}

function loadPage(page) {
  fetch(`/${page}`)
    .then(res => res.text())
    .then(html => {
      document.getElementById("conteudo").innerHTML = html;
    })
    .catch(() => alert("Erro ao carregar a página."));
}