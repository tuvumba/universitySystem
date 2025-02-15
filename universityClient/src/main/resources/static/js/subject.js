function startStudying(subjectId) {
    fetch(`/subjects/${subjectId}/study`, { method: "POST" })
        .then(() => location.reload());
}

function stopStudying(subjectId) {
    fetch(`/subjects/${subjectId}/study`, { method: "DELETE" })
        .then(() => location.reload());
}

function startTeaching(subjectId) {
    fetch(`/subjects/${subjectId}/teach`, { method: "POST" })
        .then(() => location.reload());
}

function stopTeaching(subjectId) {
    fetch(`/subjects/${subjectId}/teach`, { method: "DELETE" })
        .then(() => location.reload());
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


