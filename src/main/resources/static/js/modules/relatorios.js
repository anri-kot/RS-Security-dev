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
        let period;
        let start, end;

        switch (selected) {
            case 'TODAY': {
                const date = `${today.getFullYear()}-${today.getMonth() + 1}-${today.getDate()}`;
                period = new DatePeriod(date, date);
                break;
            }

            case 'MONTH': {
                const endOfMonth = new Date(today);
                endOfMonth.setMonth(today.getMonth() + 1, 0);
                start = `${today.getFullYear()}-${today.getMonth() + 1}-01`;
                end = `${today.getFullYear()}-${today.getMonth() + 1}-${endOfMonth.getDate()}`;
                period = new DatePeriod(start, end);
                break;
            }

            case 'SEMESTER': {
                let startOfSemester = new Date(today);
                let endOfSemester = new Date(today);

                if (today.getMonth() <= 5) {
                    startOfSemester.setMonth(0, 1);
                    endOfSemester.setMonth(6, 0);
                } else {
                    startOfSemester.setMonth(6, 1);
                    endOfSemester.setMonth(12, 0);
                }

                start = `${startOfSemester.getFullYear()}-${startOfSemester.getMonth() + 1}-01`;
                end = `${endOfSemester.getFullYear()}-${endOfSemester.getMonth() + 1}-${endOfSemester.getDate()}`;
                period = new DatePeriod(start, end);
                break;
            }

            case 'YEAR': {
                const endOfYear = new Date(today);
                endOfYear.setMonth(12, 0);
                start = `${today.getFullYear()}-01-01`;
                end = `${today.getFullYear()}-12-${endOfYear.getDate()}`;
                period = new DatePeriod(start, end);
                break;
            }

            default:
                throw new Error("Período selecionado inválido");
        }

        return period;
    }

    // start, end: yyyy-mm-dd
    async function loadRelatorioVendaByDate(period) {
        const url = `/relatorios/vendas?start=${period.start}&end=${period.end}`
        try {
            //htmx.ajax('GET', url, { target: '#conteudo' });
            window.location.href = url;
        } catch (err) {
            console.error(`Erro ao carregar relatório de vendas: ${err.message}`);
        }
    }
}