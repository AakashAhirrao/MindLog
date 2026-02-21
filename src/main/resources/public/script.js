const API_URL = "http://localhost:7070/thoughts";

let currentOffset = 0;
const LIMIT = 5;
let currentKeyword = ""; // We need to remember the search term for pagination!

// 1. Fetch and display thoughts
async function loadThoughts(append = false, keyword = "") {
    if (!append) {
        currentOffset = 0; // Reset offset if it's a fresh search or refresh
        currentKeyword = keyword; // Update the active search word
    }

    // Build the URL with pagination
    let url = `${API_URL}?limit=${LIMIT}&offset=${currentOffset}`;
    // If there is a search word, add it to the URL
    if (currentKeyword) {
        url += `&keyword=${encodeURIComponent(currentKeyword)}`;
    }

    try {
        const response = await fetch(url);
        const thoughts = await response.json();

        const container = document.getElementById('thoughtsContainer');
        if (!append) container.innerHTML = ""; // Clear existing thoughts if not appending

        thoughts.forEach(t => {
            const card = document.createElement('div');
            card.className = 'thought-card';

            // Format the date nicely
            const dateStr = new Date(t.date).toLocaleDateString(undefined, {
                month: 'short', day: 'numeric', year: 'numeric'
            });

            card.innerHTML = `
                <div class="meta">
                    <div>
                        <span class="category-tag">${t.category}</span>
                        <span>${dateStr}</span>
                    </div>
                    <span class="delete-btn" onclick="deleteThought(${t.id})">🗑️</span>
                </div>
                <p>${t.content}</p>
            `;
            container.appendChild(card);
        });

        // Hide the "Show More" button if the database returned fewer than 5 thoughts
        document.getElementById('loadMoreBtn').style.display = thoughts.length < LIMIT ? "none" : "block";
    } catch (err) {
        console.error("Failed to load thoughts:", err);
    }
}

// 2. Save a new thought (The missing piece!)
document.getElementById('saveBtn').addEventListener('click', async () => {
    const content = document.getElementById('thoughtContent').value;
    const category = document.getElementById('categorySelect').value;

    if (!content.trim()) {
        alert("Write something first!");
        return;
    }

    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content, category })
    });

    if (response.ok) {
        document.getElementById('thoughtContent').value = ""; // Clear the text box
        loadThoughts(); // Reload the feed from the top
    }
});

// 3. Load More Button
document.getElementById('loadMoreBtn').addEventListener('click', () => {
    currentOffset += LIMIT;
    loadThoughts(true, currentKeyword); // Pass 'true' to append, and remember the search word
});

// 4. Delete Function
async function deleteThought(id) {
    if (!confirm("Are you sure you want to delete this log?")) return;
    const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    if (response.ok) {
        loadThoughts(false, currentKeyword); // Reload the current view
    }
}

// 5. Search Logic (Expanding Bar)
const searchContainer = document.getElementById('searchContainer');
const searchIconBtn = document.getElementById('searchIconBtn');
const searchInput = document.getElementById('searchInput');

searchIconBtn.addEventListener('click', (e) => {
    e.stopPropagation();
    searchContainer.classList.toggle('active');
    if (searchContainer.classList.contains('active')) {
        searchInput.focus();
    }
});

// Real-time search as you type
searchInput.addEventListener('input', (e) => {
    loadThoughts(false, e.target.value); // False means "clear the feed and show search results"
});

// Close search if clicking outside
document.addEventListener('click', (e) => {
    if (!searchContainer.contains(e.target)) {
        searchContainer.classList.remove('active');
        // If they close the search bar, reset the feed
        if (searchInput.value !== "") {
            searchInput.value = "";
            loadThoughts();
        }
    }
});

// Initial Load when page opens
loadThoughts();