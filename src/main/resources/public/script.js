const API_URL = "http://localhost:7070/thoughts";

// 1. Fetch and display thoughts when the page loads
async function loadThoughts() {
    const response = await fetch(API_URL);
    const thoughts = await response.json();

    const container = document.getElementById('thoughtsContainer');
    container.innerHTML = ""; // Clear current view

    thoughts.forEach(t => {
        const card = document.createElement('div');
        card.className = 'thought-card';
        card.innerHTML = `
            <div class="meta">
                <span class="category-tag">${t.category}</span>
                <span>${new Date(t.date).toLocaleDateString()}</span>
            </div>
            <p>${t.content}</p>
        `;
        container.appendChild(card);
    });
}

// 2. Save a new thought
document.getElementById('saveBtn').addEventListener('click', async () => {
    const content = document.getElementById('thoughtContent').value;
    const category = document.getElementById('categorySelect').value;

    if (!content) return alert("Write something first!");

    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content, category })
    });

    if (response.ok) {
        document.getElementById('thoughtContent').value = ""; // Clear input
        loadThoughts(); // Refresh the list
    }
});

// Initial Load
loadThoughts();