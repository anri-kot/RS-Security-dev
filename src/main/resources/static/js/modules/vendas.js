export function init () {

    const filterEl = document.getElementById('filter');
    const searchBy = document.getElementById('search-type');
    const metodoPagamentoEl = document.getElementById('metodoPagamento');
    const observacaoEl = document.getElementById('observacao');

    filterEl.addEventListener('change', () => {
        toggleDateInputs();
    });

    searchBy.addEventListener('change', () => {
        if (searchBy.value === 'id') {
            for (let i = 0; i < filterEl.children.length; i++) {
                filterEl.children[i].removeAttribute('selected');
            }

            filterEl.children[0].setAttribute('selected', true);
            filterEl.setAttribute('disabled', true);
            metodoPagamentoEl.setAttribute('disabled', true);
            observacaoEl.setAttribute('disabled', true);


            toggleDateInputs();

        } else {
            filterEl.removeAttribute('disabled');
            metodoPagamentoEl.removeAttribute('disabled');
            observacaoEl.removeAttribute('disabled');
        }
    });

    function toggleDateInputs() {
        const filter = filterEl.value;
    
        const dataInicioContainer = document.getElementById('data-inicio-container');
        const dataFimContainer = document.getElementById('data-fim-container');

        const dataInicio = document.getElementById('dataInicio');
        dataInicio.value = '';
        dataInicio.setAttribute('disabled', true);
        const dataFim = document.getElementById('dataFim');
        dataFim.value = '';
        dataFim.setAttribute('disabled', true);

        switch (filter) {
            case 'entre':
                dataInicioContainer.style.display = 'block';
                dataFimContainer.style.display = 'block';
                dataInicio.removeAttribute('disabled');
                dataFim.removeAttribute('disabled');
                break;

            case 'antes':
                dataInicioContainer.style.display = 'none';
                dataFimContainer.style.display = 'block';
                dataFim.removeAttribute('disabled');
                break;
            
            case 'depois':
                dataInicioContainer.style.display = 'block';
                dataInicio.removeAttribute('disabled');
                dataFimContainer.style.display = 'none';
                break;
        
            default:
                dataInicioContainer.style.display = 'none';
                dataFimContainer.style.display = 'none';
                break;
        }
      }

}