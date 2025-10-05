export function init() {
    const DEFAULT_DATE_OPTIONS = ['TODAY', 'MONTH', 'SEMESTER', 'YEAR'];
    const vendaOptionsEl = document.getElementById('venda-options');

    vendaOptionsEl.addEventListener('click', async (e) => {
        const selected = e.target.value.toUpperCase();
        if (DEFAULT_DATE_OPTIONS.includes(selected)) {
            const date = getSelectedDate(selected);
            loadRelatorioVendaByDate(date);
        }
    });

    class DatePeriod {
        constructor(start, end) {
            this.start = start,
                this.end = end
        }
    }

    function getSelectedDate(selected) {
        const today = new Date();

        const formatDate = (date) =>
            date.toISOString().split('T')[0];

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