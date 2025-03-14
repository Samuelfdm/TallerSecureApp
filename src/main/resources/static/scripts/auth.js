const API_URL = 'https://localhost:8443';

// Función para manejar el login
document.getElementById('login-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${API_URL}/users/auth`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            // Redirige a properties.html después del login exitoso
            window.location.href = 'properties.html';
        } else {
            alert('Invalid email or password.');
        }
    } catch (error) {
        console.error('Error during authentication:', error);
        alert('An error occurred. Please try again later.');
    }
});

// Función para manejar el registro
document.getElementById('register-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${API_URL}/users`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            alert('Registration successful! Please login.');
            window.location.href = 'index.html';
        } else {
            alert('Registration failed. Please try again.');
        }
    } catch (error) {
        console.error('Error during registration:', error);
        alert('An error occurred. Please try again later.');
    }
});