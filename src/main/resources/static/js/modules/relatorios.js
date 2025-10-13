export function init() {
    const DEFAULT_DATE_OPTIONS = ['TODAY', 'MONTH', 'SEMESTER', 'YEAR'];
    const vendaOptionsEl = document.getElementById('venda-options');
    const compraOptionsEl = document.getElementById('compra-options');
    const relatorioModalEl = document.getElementById('relatorioModal');

    const dataInicioEl = relatorioModalEl.querySelector('#dataInicio');
    const dataFimEl = relatorioModalEl.querySelector('#dataFim');

    const formatDate = (date) =>
        date.toISOString().split('T')[0];

    class DatePeriod {
        constructor(start, end) {
            this.start = start,
                this.end = end
        }
    }

    vendaOptionsEl.addEventListener('click', async (e) => {
        const selected = e.target.tagName.toLowerCase();
        if (selected !== 'button') return;

        const option = e.target.value.toUpperCase();
        if (DEFAULT_DATE_OPTIONS.includes(option)) {
            const date = getSelectedDate(option);
            loadRelatorioVendaByDate(date);
        }
    });

    compraOptionsEl.addEventListener('click', async e => {
        const selected = e.target.tagName.toLowerCase();
        if (selected !== 'button') return;
        e.preventDefault();

        alert('Funcionalidade ainda não implementada nesta versão.')
    });

    if (relatorioModalEl) {        
        const confirmBtnEl = relatorioModalEl.querySelector('#confirm-register');

        relatorioModalEl.addEventListener('show.bs.modal', event => {
            
            const button = event.relatedTarget;
            const tabelaName = button.getAttribute('data-bs-tabela');

            const modalTitleEl = relatorioModalEl.querySelector('.modal-title');
            modalTitleEl.textContent = tabelaName;
            confirmBtnEl.setAttribute('data-source', tabelaName.toLowerCase());
        });

        confirmBtnEl.addEventListener('click', e => {
            if (!validateModal()) return;
            const source = e.target.getAttribute('data-source').toLowerCase();

            if (source.length === 0) return;

            if (source === 'venda') {
                loadRelatorioVendaByDate( getModalPeriod() )
            } else {
                // TODO: leadRelatorioCompraByDate
            }
        });
    }

    function getSelectedDate(selected) {
        const today = new Date();

        let startDate, endDate;

        switch (selected) {
            case 'TODAY': {
                startDate = new Date(today);
                endDate = new Date(today);
                break;
            }

            case 'MONTH': {
                startDate = new Date(today.getFullYear(), today.getMonth(), 1);
                endDate = new Date(today.getFullYear(), today.getMonth() + 1, 0);
                break;
            }

            case 'SEMESTER': {
                const isFirstSemester = today.getMonth() < 6;
                startDate = new Date(today.getFullYear(), isFirstSemester ? 0 : 6, 1);
                endDate = new Date(today.getFullYear(), isFirstSemester ? 6 : 12, 0);
                break;
            }

            case 'YEAR': {
                startDate = new Date(today.getFullYear(), 0, 1);
                endDate = new Date(today.getFullYear(), 12, 0);
                break;
            }

            default:
                throw new Error("Período selecionado inválido");
        }

        return new DatePeriod(formatDate(startDate), formatDate(endDate));
    }

    function getModalPeriod() {
        const dataInicio = dataInicioEl.value;
        const dataFim = dataFimEl.value;
        return new DatePeriod(formatDate(new Date(dataInicio)), formatDate(new Date(dataFim)));
    }

    function validateModal() {
        const form = relatorioModalEl.querySelector('form');
        const periodoErrorEl = relatorioModalEl.querySelector('#periodoError');

        let isValid = form.checkValidity();

        if (isValid) {
            const dataInicio = new Date(dataInicioEl.value);
            const dataFim = new Date(dataFimEl.value);
    
            isValid = isValid && (dataInicio <= dataFim);
        }

        if (!isValid) {
            periodoErrorEl.classList.remove('d-none');
            dataInicioEl.classList.add('is-invalid');
            dataFimEl.classList.add('is-invalid');
        } else {
            periodoErrorEl.classList.add('d-none');
            dataInicioEl.classList.remove('is-invalid');
            dataFimEl.classList.remove('is-invalid');
            // prossegue com o envio ou lógica normal
        }

        return isValid;
    }

    // start, end: yyyy-mm-dd
    async function loadRelatorioVendaByDate(period) {
        const url = `/relatorios/vendas?dataInicio=${period.start}&dataFim=${period.end}`
        try {
            //htmx.ajax('GET', url, { target: '#conteudo' });
            window.location.href = url;
        } catch (err) {
            console.error(`Erro ao carregar relatório de vendas: ${err.message}`);
        }
    }
}