import Formatter from "/js/formatter.js";
export function init() {
    let lastSearch = null;

    const searchTypeEl = document.getElementById('search-type');
    const tabelaUsuariosEl = document.getElementById('tabela-usuarios');
    // == MODALS
    // PRODUTO
    const usuarioModalEl = document.getElementById('modal-usuario');
    const modalIdUsuarioEl = document.getElementById('modal-usuario-idUsuario');
    const modalUsernameEl = document.getElementById('modal-usuario-username');
    const modalNomeoEl = document.getElementById('modal-usuario-nome');
    const modalSobrenomeEl = document.getElementById('modal-usuario-sobrenome');
    const modalCpfEl = document.getElementById('modal-usuario-cpf');
    const modalEnderecoEl = document.getElementById('modal-usuario-endereco');
    const modalEmailEl = document.getElementById('modal-usuario-email');
    const modalTelefoneEl = document.getElementById('modal-usuario-telefone');
    const modalSalarioEl = document.getElementById('modal-usuario-salario');
    const modalAdminEl = document.getElementById('modal-usuario-admin');
    // === SENHA
    const updateSenhaContainerEl = document.getElementById('update-senha-container');
    const senhaContainerEl = document.getElementById('senha-container');
    // Radios
    const radioChangePwTrue = document.getElementById('change-pw-true');
    const radioChangePwFalse = document.getElementById('change-pw-false');
    const modalSenhaLabelEl = document.getElementById('modal-usuario-senha-label');
    const modalSenhaEl = document.getElementById('modal-usuario-senha');
    const modalConfirmSenhaLabelEl = document.getElementById('modal-usuario-confirmSenha-label');
    const modalConfirmSenhaEl = document.getElementById('modal-usuario-confirmSenha');
    // CONFIRM
    const confirmModalEl = document.getElementById('confirm-modal');
    const confirmActionEl = document.getElementById('confirmar-acao');

    let toDelete = 0;

    document.addEventListener('DOMContentLoaded', () => lastSearch = location.search);
    document.addEventListener('htmx:afterSwap', () => lastSearch = location.search);

    tabelaUsuariosEl.addEventListener('click', (e) => {
        const btn = e.target.closest('button');
        if (!btn) return;

        const action = btn.dataset.action;
        const id = btn.dataset.id;

        if (action === 'edit') {
            showUsuarioModal(id);
        } else if (action === 'delete') {
            showConfirmModal('delete', id);
        }
    });

    async function refreshUsuarios() {
        lastSearch = location.search;
        const url = `/usuarios${lastSearch}`;
        htmx.ajax('GET', url, { target: '#conteudo', selected: '#cards-compras' });
    }

    /* ============ MODALS ============= */
    const NEW_USUARIO = 0;
    let lastUsuarioId = null;
    
    // USUARIOS MODAL ******
    document.getElementById('new-usuario').addEventListener('click', () => showUsuarioModal());
    document.getElementById('confirm-register').addEventListener('click', () => {
        const form = document.getElementById("usuario-form");

        if (form.checkValidity()) {            
            if (validateSenha() !== true) return;
            showConfirmModal('addUpdate')
        } else {
            form.reportValidity();
        }
    });

    radioChangePwTrue.addEventListener('change', toggleSenhaFields);
    radioChangePwFalse.addEventListener('change', toggleSenhaFields);

    // formatting
    modalCpfEl.addEventListener('input', e => {
        e.target.value = Formatter.formatCpf(e.target.value);
    });
    modalTelefoneEl.addEventListener('input', e => {
        e.target.value = Formatter.formatPhone(e.target.value);
    });

    function toggleSenhaFields() {
        const doEnable = radioChangePwTrue.checked;
        if (doEnable) {
            senhaContainerEl.classList.remove('d-none');
            modalSenhaEl.removeAttribute('disabled');
            modalConfirmSenhaEl.removeAttribute('disabled');
        } else {
            senhaContainerEl.classList.add('d-none');
            modalSenhaEl.setAttribute('disabled', true);
            modalConfirmSenhaEl.setAttribute('disabled', true);
            clearSenhaValidation();
        }
    }

    // Senha live validation
    modalSenhaEl.addEventListener('input', validateSenha);
    modalConfirmSenhaEl.addEventListener('input', validateSenha);

    function validateSenha() {
        if (modalSenhaEl.disabled === true && modalConfirmSenhaEl.disabled === true) return true;

        const senha = modalSenhaEl.value;
        const confirm = modalConfirmSenhaEl;
        const senhaValidationEl = document.getElementById('senha-validation');

        let isLengthValid = false;
        let isConfirmValid = false;
        let hasNoSpaces = false;

        modalSenhaEl.classList.remove('is-valid');
        modalSenhaEl.classList.add('is-invalid');

        if (senha.length < 8) {
            senhaValidationEl.innerText = `A senha deve ter pelo menos 8 caracteres.`;
        } else if (senha.indexOf(" ") !== -1) {
            senhaValidationEl.innerText = 'A senha não deve conter espaços';
        } else {
            isLengthValid = true;
            hasNoSpaces = true;
            modalSenhaEl.classList.remove('is-invalid');
            modalSenhaEl.classList.add('is-valid');
            senhaValidationEl.innerText = '';
        }

        if (senha === confirm && confirm.length > 0) {
            modalConfirmSenhaEl.classList.remove('is-invalid');
            modalConfirmSenhaEl.classList.add('is-valid');
            isConfirmValid = true;
        } else {
            modalConfirmSenhaEl.classList.remove('is-valid');
            modalConfirmSenhaEl.classList.add('is-invalid');
        }

        return isLengthValid && isConfirmValid && hasNoSpaces;
    }

    function clearSenhaValidation() {
        modalSenhaEl.classList.remove('is-valid', 'is-invalid');
        modalConfirmSenhaEl.classList.remove('is-valid', 'is-invalid');
        modalSenhaEl.value = '';
        modalConfirmSenhaEl.value = '';
    }

    function showSenhaContainer(doEnable) {
        if (doEnable) {
            senhaContainerEl.classList.remove('d-none');
            modalSenhaEl.removeAttribute('disabled');
            modalConfirmSenhaEl.removeAttribute('disabled');
        } else {
            senhaContainerEl.classList.add('d-none');
            modalSenhaEl.setAttribute('disabled', true);
            modalConfirmSenhaEl.setAttribute('disabled', true);
        }
    }

    function showUsuarioModal(id = NEW_USUARIO) {        
        const modal = bootstrap.Modal.getOrCreateInstance(usuarioModalEl);

        switch (id) {
            case NEW_USUARIO:
                clearUsuarioModal(NEW_USUARIO);
                lastUsuarioId = id;
                modal.show();
                break;
            
            case lastUsuarioId:
                modal.show();
                break;

            default:
                clearUsuarioModal();
                fetchUsuarioData(id).then(usuario => {populateUsuarioModal(usuario)});
                modal.show();
                lastUsuarioId = id;
                break;
        }
    }

    function populateUsuarioModal(usuario) {
        if (!usuario) {
            return;
        }

        modalIdUsuarioEl.value = usuario.idUsuario;
        modalNomeoEl.value = usuario.nome;
        modalUsernameEl.value = usuario.username;
        modalSobrenomeEl.value = usuario.sobrenome;
        modalCpfEl.value = usuario.cpf;
        modalEnderecoEl.value = usuario.endereco;
        modalEmailEl.value = usuario.email;
        modalTelefoneEl.value = usuario.telefone;
        modalSalarioEl.value = usuario.salario;
        modalAdminEl.value = usuario.admin;
    }

    function clearUsuarioModal(isNew) {
        radioChangePwFalse.checked = true;
        clearSenhaValidation();
        if (isNew === NEW_USUARIO) {            
            updateSenhaContainerEl.classList.add('d-none');
            modalSenhaLabelEl.innerText = 'Senha';
            modalConfirmSenhaLabelEl.innerText = 'Confirmar Senha';
            showSenhaContainer(true);
        } else {
            updateSenhaContainerEl.classList.remove('d-none');
            modalSenhaLabelEl.innerText = 'Nova Senha';
            modalConfirmSenhaLabelEl.innerText = 'Confirmar Nova Senha';
            showSenhaContainer(false);
        }
        const form = usuarioModalEl.querySelector('form');
        form.querySelectorAll('input').forEach((el) => {
            if (el.name !== "modal-usuario-changePw") {
                el.value = "";
            }
        });
        modalAdminEl.value = "false";
    }

    async function fetchUsuarioData(id) {
        try {
            const url = `/api/usuario/${id}`;
            const response = await fetch(url);

            if (!response.ok) {
                alert('Erro ao buscar usuário.');
                console.error(await response.json());
                return null;
            }
            return await response.json();
        } catch (e) {
            console.error('Erro inesperado ao buscar usuario:', e);
            return null;
        }
    }

    // CONFIRM MODAL *****
    confirmActionEl.addEventListener('click', e => {        
        const action = e.target.dataset.action;
        
        if (action === 'addUpdate') {
            addUpdateUsuario();
        } else if (action === 'delete') {
            deleteUsuario(toDelete);
        }
        const modal = bootstrap.Modal.getOrCreateInstance(confirmModalEl);
        modal.hide();
    })

    function showConfirmModal(action, id = 0) {
        if (action === 'addUpdate') {
            const modal = bootstrap.Modal.getOrCreateInstance(confirmModalEl);
            confirmActionEl.dataset.action = action;
            modal.show();
            return;
        }
        const modal = bootstrap.Modal.getOrCreateInstance(confirmModalEl);
        confirmActionEl.dataset.action = action;
        toDelete = id;
        
        modal.show();
    }

    /* =========== INSERT UPDATE DELETE ===========  */

    async function addUpdateUsuario() {
        const obj = getUsuarioObj();
        const usuario = obj.usuario;
        const changePw = obj.changePw;

        try {
            let url = '';
            const method = usuario.idUsuario == null ? 'POST' : 'PUT';
            let body = JSON.stringify(usuario);

            if (method === 'PUT') {
                url = `/api/usuario/${usuario.idUsuario}?changePW=${changePw}`;
            } else {
                url = `/api/usuario`;
            }

            const response = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'HX-Request': 'true'
                },
                body
            });

            if (!response.ok) {
                let errorMsg = 'Erro desconhecido';
                const errorData = await response.text();
                errorMsg = errorData || 'Erro ao processar requisição.';

                document.getElementById('error-container').innerHTML = errorMsg;
            } else {
                refreshUsuarios();
                bootstrap.Modal.getOrCreateInstance(usuarioModalEl).hide();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Erro inesperado. Veja o console para mais detalhes.');
        }
    }

    async function deleteUsuario(id) {
        toDelete = 0;

        try {
            const url = `/api/usuario/${id}`;
            const method = 'DELETE';
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'HX-Request': 'true'
                }
            });
            if (!response.ok) {
                let errorMsg = 'Erro desconhecido';
                try {
                    const errorData = await response.json();
                    console.error('Erro da API:', errorData);

                    errorMsg = errorData.message || 'Erro ao processar requisição.';
                    if (errorData.details) {
                        console.warn('Detalhes técnicos:', errorData.details);
                    }
                } catch (e) {
                    console.warn('Resposta não era JSON.');
                }

                document.getElementById('error-container').innerHTML = errorMsg;
            } else {
                refreshUsuarios();
            }
        } catch (e) {
            console.error('Erro inesperado:', e);
            alert('Erro inesperado. Veja o console para mais detalhes.');
        }
    }

    function unmask(value) {
        return value.trim().replace(/\D/g, '');
    }

    function getUsuarioObj() {
        const idValue = modalIdUsuarioEl.value === "" ? null : parseInt(modalIdUsuarioEl.value);
        const idUsuario = idValue;
        const nome = modalNomeoEl.value.trim();
        const username = modalUsernameEl.value.trim();
        const sobrenome = modalSobrenomeEl.value.trim();
        const cpf = unmask(modalCpfEl.value);
        const endereco = modalEnderecoEl.value.trim();
        const telefone = unmask(modalTelefoneEl.value);
        const email = modalEmailEl.value.trim();
        const senha = modalSenhaEl.value;
        const salario = parseFloat(modalSalarioEl.value);
        const admin = modalAdminEl.value === 'true';

        const changePw = document.querySelector('input[name="modal-usuario-changePw"]:checked')?.value === 'true';

        return {
            usuario: {
                idUsuario: idUsuario,
                nome: nome,
                username: username,
                sobrenome: sobrenome,
                cpf: cpf,
                endereco: endereco,
                telefone: telefone,
                email: email,
                salario: salario,
                admin: admin,
                senha: senha
            },
            changePw: changePw
        }
    }

}