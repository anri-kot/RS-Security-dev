const conteudo = document.getElementById('conteudo');
const navOptions = document.querySelectorAll('#nav-options li');
const TITLE = 'RSSECURITY';
let currentPage = '';
let pageNumNavEl = null;

// Load Modules and Sidebar
document.addEventListener("DOMContentLoaded", () => {
	updateSidebar();
	importModule();
	setupPageNumNav();
});

// HTMX: reload module after navigation
document.addEventListener("htmx:afterSwap", e => {
	const pathname = location.pathname.replace("/", "");
	const targetId = e.target.id;

	if (targetId === conteudo.id) {
		importModule();
		updateSidebar();
		setupPageNumNav();
		document.title = `${TITLE} - ${currentPage}`;
	} else if (targetId === `tabela-${pathname}` || targetId === `cards-${pathname}`) {
		setupPageNumNav();
	}
});

// Click on sidebar
navOptions.forEach(node => {
	const option = node.querySelector('a');
	if (option === null) return;
	option.addEventListener('click', e => {
		e.preventDefault();
		const page = option.dataset.page;
		//history.pushState({}, '', `/${page === 'home' ? '' : page}`);
		navigateToPage(page);
	});
});

// Click on back/forward button
window.addEventListener('popstate', () => {
	const currentPage = location.pathname.split('/')[1] || 'home';
	const url = `/${currentPage === 'home' ? '' : currentPage}`;
	htmx.ajax('GET', url, { target: '#conteudo' },);
});

// Collapse sidebar
document.getElementById('toggleSidebar').addEventListener('click', () => {
	const sidebar = document.getElementById('sidebar');
	const content = document.getElementById('conteudo');

	sidebar.classList.toggle('collapsed');
	content.classList.toggle('collapsed');
});

function updateSidebar() {
	const currentPage = location.pathname.split('/')[1] || 'home';
	loadUsername();

	navOptions.forEach(node => {
		const option = node.querySelector('a');
		if (option === null) return;
		if (option.dataset.page === currentPage) {
			option.classList.add('active');
			option.classList.remove('link-dark');
		} else {
			option.classList.remove('active');
			option.classList.add('link-dark');
		}
	});
}

function navigateToPage(page) {
	const url = `/${page === 'home' ? '' : page}`;
	htmx.ajax('GET', url, { target: '#conteudo' });
	history.pushState({}, '', url);
	updateSidebar();
}

function importModule() {
	const pageId = document.querySelector("[data-page-id]")?.dataset.pageId;
	if (!pageId) return;

	import(`/js/modules/${pageId}.js`)
		.then(module => module.init())
		.catch(err => console.warn(`Não foi possível carregar módulo para ${pageId}`, err));

	currentPage = pageId ? capitalize(pageId) : 'Home';
}

function setupPageNumNav() {
	const currentParams = new URLSearchParams(window.location.search);
	const basePath = location.pathname;

	document.querySelectorAll("#pages-container [data-page]").forEach(linkEl => {
		const pageNum = linkEl.getAttribute("data-page");

        if (!pageNum || linkEl.classList.contains("disabled")) return;

        const params = new URLSearchParams(currentParams); // clone
        params.set("page", pageNum);

        const newUrl = `${basePath}?${params.toString()}`;
        const anchor = linkEl.querySelector("a");

        anchor.setAttribute("hx-get", newUrl);
        anchor.setAttribute("hx-target", "#conteudo");
        anchor.setAttribute("hx-push-url", "true");
        anchor.setAttribute("href", newUrl);
	});
}

async function loadUsername() {
	fetch('/user/info')
		.then(res => res.json())
		.then(data => {
			document.getElementById('user-username').textContent = data.username;
		});
}

function capitalize(str) {
	return str.charAt(0).toUpperCase() + str.slice(1);
}
