document.addEventListener("DOMContentLoaded", () => {
    const deleteForm = document.querySelector("form[action*='/delete']");
    if (deleteForm) {
        deleteForm.addEventListener("submit", (e) => {
            if (!confirm("Are you sure you want to delete this teacher?")) {
                e.preventDefault();
            }
        });
    }
});

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

