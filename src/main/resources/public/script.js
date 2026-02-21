const API_URL = "http://localhost:7070/thoughts";

let currentOffset = 0;
const LIMIT = 5;

// 1. Fetch and display thoughts
async function loadThoughts(append = false) {
    if (!append) currentOffset = 0; // Reset if it's a fresh search or refresh

    const url = `${API_URL}?limit=${LIMIT}&offset=${currentOffset}`;
    const response = await fetch(url);
    const thoughts = await response.json();

    const container = document.getElementById('thoughtsContainer');
    if (!append) container.innerHTML = "";

    thoughts.forEach(t => {
        const card = document.createElement('div');
        card.className = 'thought-card';
        card.innerHTML = `
            <div class="meta">
                <div>
                    <span class="category-tag">${t.category}</span>
                    <span>${new Date(t.date).toLocaleDateString()}</span>
                </div>
                <span class="delete-btn" onclick="deleteThought(${t.id})">🗑️</span>
            </div>
            <p>${t.content}</p>
        `;
        container.appendChild(card);
    });

    // Hide button if no more thoughts
    document.getElementById('loadMoreBtn').style.display = thoughts.length < LIMIT ? "none" : "block";
}

// 2. Save a new thought
document.getElementById('loadMoreBtn').addEventListener('click', () => {
    currentOffset += LIMIT;
    loadThoughts(true);
});

// Delete Function
async function deleteThought(id) {
    if (!confirm("Delete this thought forever?")) return;
    const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    if (response.ok) loadThoughts();
}

// 3. Search Logic (Expanding Bar)
const searchContainer = document.getElementById('searchContainer');
const searchIconBtn = document.getElementById('searchIconBtn');
const searchInput = document.getElementById('searchInput');

searchIconBtn.addEventListener('click', (e) => {
    e.stopPropagation(); // Prevents instant closing
    searchContainer.classList.toggle('active');
    if (searchContainer.classList.contains('active')) {
        searchInput.focus();
    }
});

// Real-time search as you type
searchInput.addEventListener('input', (e) => {
    loadThoughts(e.target.value);
});

// Close search if clicking outside
document.addEventListener('click', (e) => {
    if (!searchContainer.contains(e.target)) {
        searchContainer.classList.remove('active');
        // If they close it, reload all thoughts
        if (searchInput.value !== "") {
            searchInput.value = "";
            loadThoughts();
        }
    }
});

// Initial Load
loadThoughts();