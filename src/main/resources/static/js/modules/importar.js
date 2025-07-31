export function init() {

    document.body.addEventListener('htmx:responseError', function(evt) {
        const uploadStatus = document.querySelector('#uploadStatus');
        if (uploadStatus) {
            uploadStatus.innerHTML = evt.detail.xhr.responseText;
        }
    });

    
}