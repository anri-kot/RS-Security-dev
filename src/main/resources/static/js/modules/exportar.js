const tabelaEl = document.getElementById('tabela');
const errorContainer = document.getElementById('eror-container');
const exportBtnEl = document.getElementById('export-btn')
let selected = '';
export function init() {
    tabelaEl.addEventListener('change', e => {
        selected = e.target.value;
    });
    exportBtnEl.addEventListener("click", async e => {
        if (!selected || selected.length <= 0) {
            return;
        }
    
        const url = `api/${selected}/export`;
    
        try {
            const resp = await fetch(url, {
                headers: {
                    "HX-Request": "true"
                },
                method: 'GET'
            });
    
            // se for erro (400/500)
            if (!resp.ok) {
                const errorText = await resp.text(); // pode ser JSON ou HTML
                errorContainer.innerHTML = errorText;
                return;
            }
    
            // se for arquivo Excel (blob)
            const blob = await resp.blob();
            const contentDisposition = resp.headers.get("Content-Disposition");
            let filename = "export.xlsx";
    
            // tenta extrair o filename do header
            if (contentDisposition && contentDisposition.includes("filename=")) {
                filename = contentDisposition.split("filename=")[1].replace(/"/g, "");
            }
    
            // cria link temporário para download
            const link = document.createElement("a");
            link.href = URL.createObjectURL(blob);
            link.download = filename;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
    
        } catch (err) {
            errorContainer.innerHTML = "Erro de conexão: " + err.message;
        }
    });
}