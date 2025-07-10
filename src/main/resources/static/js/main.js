const conteudo = document.getElementById('conteudo');
const navOptions = document.querySelectorAll('#nav-options li');

// Load Modules and Sidebar
document.addEventListener("DOMContentLoaded", () => {
  updateSidebar();
  loadModule();  
});

// HTMX: reload module after navigation
document.addEventListener("htmx:afterSwap", e => {
  if (e.target.id === conteudo.id) {
    loadModule();
    updateSidebar();
  }
});

function updateSidebar() {
  const currentPage = location.pathname.split('/')[1] || 'home';  

  navOptions.forEach(node => {
    const option = node.querySelector('a');
    if (option.dataset.page === currentPage) {
      option.classList.add('active');
      option.classList.remove('link-dark');
    } else {
      option.classList.remove('active');
      option.classList.add('link-dark');
    }
  });
}

function navigateTo(page) {
  const url = `/${page === 'home' ? '' : page}`;
  htmx.ajax('GET', url, { target: '#conteudo' });
  history.pushState({}, '', url);
  updateSidebar();
}

function loadModule() {
  const pageId = document.querySelector("[data-page-id]")?.dataset.pageId;
  if (!pageId) return;

  import(`/js/modules/${pageId}.js`)
    .then(module => module.init())
    .catch(err => console.warn(`Não foi possível carregar módulo para ${pageId}`, err));
  
}

// Click on sidebar
navOptions.forEach(node => {
  const option = node.querySelector('a');
  option.addEventListener('click', e => {
    e.preventDefault();
    const page = option.dataset.page;
    history.pushState({}, '', `/${page === 'home' ? '' : page}`);
    navigateTo(page);
  });
});

// Click on back/forward button
window.addEventListener('popstate', () => {
  const currentPage = location.pathname.split('/')[1] || 'home';
  const url = `/${currentPage === 'home' ? '' : currentPage}`;
  htmx.ajax('GET', url, { target: '#conteudo' }, );
});

// Collapse sidebar
document.getElementById('toggleSidebar').addEventListener('click', () => {
  const sidebar = document.getElementById('sidebar');
  const content = document.getElementById('conteudo');

  sidebar.classList.toggle('collapsed');
  content.classList.toggle('collapsed');
});
