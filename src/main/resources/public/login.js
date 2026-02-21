const API_URL = "http://localhost:7070";

let isLoginMode = true; // Tracks if we are logging in or signing up

// Grab the elements from the screen
const loginBtn = document.getElementById('loginBtn');
const toggleSpan = document.getElementById('showSignupBtn');
const subtitle = document.querySelector('.login-subtitle');

// --- 1. THE TOGGLE SWITCH ---
// This flips the form between "Log In" and "Sign Up" smoothly
toggleSpan.addEventListener('click', () => {
    isLoginMode = !isLoginMode;

    if (isLoginMode) {
        loginBtn.textContent = "Log In";
        subtitle.textContent = "Welcome back to your space.";
        toggleSpan.textContent = "Sign Up";
        toggleSpan.previousSibling.textContent = "Don't have an account? ";
    } else {
        loginBtn.textContent = "Create Account";
        subtitle.textContent = "Start your journaling journey.";
        toggleSpan.textContent = "Log In";
        toggleSpan.previousSibling.textContent = "Already have an account? ";
    }
});

// --- 2. THE ACTION BUTTON ---
loginBtn.addEventListener('click', async () => {
    const username = document.getElementById('usernameInput').value;
    const password = document.getElementById('passwordInput').value;

    // Basic validation
    if (!username.trim() || !password.trim()) {
        return alert("Please fill in both fields! 🕵️‍♂️");
    }

    // Decide which Java gate to knock on based on the mode
    const endpoint = isLoginMode ? "/login" : "/register";

    try {
        const response = await fetch(API_URL + endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (isLoginMode) {
            // --- LOGIN LOGIC ---
            if (response.ok) {
                const user = await response.json();

                // 🔥 THE MAGIC: Save the user ID in the browser's memory!
                localStorage.setItem('mindlog_user_id', user.id);
                localStorage.setItem('mindlog_username', user.username);

                // Redirect them to the main journal page!
                window.location.href = "/index.html";
            } else {
                alert("Invalid username or password! ❌");
            }
        } else {
            // --- REGISTRATION LOGIC ---
            if (response.ok) {
                alert("Account created successfully! You can now log in. 🎉");
                // Switch the form back to login mode automatically
                toggleSpan.click();
                document.getElementById('passwordInput').value = ""; // Clear password for safety
            } else {
                const errorText = await response.text();
                alert(errorText); // Shows "Username already exists!"
            }
        }
    } catch (err) {
        console.error("Connection Error:", err);
        alert("Cannot connect to server. Is IntelliJ running?");
    }
});