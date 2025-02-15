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