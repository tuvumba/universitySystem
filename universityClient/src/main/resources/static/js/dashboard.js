function toggleForm(formId) {
    const form = document.getElementById(formId);
    form.style.display = form.style.display === "block" ? "none" : "block";
}

function validateForm(formId) {
    const form = document.getElementById(formId);
    const inputs = form.querySelectorAll('input');
    let isValid = true;

    inputs.forEach(input => {
        if (!input.value.trim()) {
            isValid = false;
            input.classList.add('invalid');
        } else {
            input.classList.remove('invalid');
        }
    });

    return isValid;
}

document.addEventListener("DOMContentLoaded", function() {
    const foldableHeaders = document.querySelectorAll('.foldable-header');

    foldableHeaders.forEach(header => {
        header.addEventListener('click', function() {
            const content = header.nextElementSibling;
            header.classList.toggle('active');
            content.style.display = content.style.display === 'block' ? 'none' : 'block';
        });
    });
});





