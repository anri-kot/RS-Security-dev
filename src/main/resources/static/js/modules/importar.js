export function init() {

    document.body.addEventListener('htmx:responseError', function(evt) {
        const uploadStatus = document.querySelector('#uploadStatus');
        if (uploadStatus) {
            uploadStatus.innerHTML = evt.detail.xhr.responseText;
        }
    });

    // Form validation
    //const form = document.getElementById('csvUploadForm');
    //     form.addEventListener('submit', (event) => {
    //         if (!form.checkValidity()) {
    //             event.preventDefault();
    //             event.stopPropagation();
    //         } else {
    //             document.getElementById('uploadStatus').style.display = 'block';
    //         }
    //         form.classList.add('was-validated');
    //     }, false);
    // })();

    // Download document
    // document.getElementById('downloadModeloBtn').addEventListener('click', () => {
    //     const tipo = document.getElementById('modeloSelect').value;
    //     window.location.href = `/modelos/${tipo}.csv`;
}