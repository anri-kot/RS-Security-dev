const Formatter = (() => {
    function applyMask(value, maskPattern) {
        const digits = value.replace(/\D/g, '');
        let formatted = '';
        let digitIndex = 0;

        for (let i = 0; i < maskPattern.length; i++) {
            if (digitIndex >= digits.length) break;

            if (maskPattern[i] === 'N') {
                formatted += digits[digitIndex++];
            } else {
                formatted += maskPattern[i];
            }
        }

        return formatted;
    }

    function formatCpf(value) {
        return applyMask(value, 'NNN.NNN.NNN-NN');
    }

    function formatCnpj(value) {
        return applyMask(value, 'NN.NNN.NNN/NNNN-NN');
    }

    function formatPhone(value) {
        const digits = value.replace(/\D/g, '');
        if (digits.length > 10) {
            return applyMask(digits, '(NN) NNNNN-NNNN');
        } else {
            return applyMask(digits, '(NN) NNNN-NNNN');
        }
    }

    return {
        formatCpf,
        formatCnpj,
        formatPhone,
        applyMask, 
    };
})();

export default Formatter;
