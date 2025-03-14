const API_URL = 'https://localhost:8443';

window.addEventListener('load', async function() {
    try {
        const response = await fetch(`${API_URL}/users/auth`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: '', password: '' }) // Puedes enviar datos vacíos o manejar esto de otra manera
        });

        if (response.ok) {
            // Si el usuario ya está autenticado, redirige a la página principal
            window.location.href = 'index.html';
        }
    } catch (error) {
        console.error('Error checking authentication:', error);
    }
});

// Función para manejar el login
document.getElementById('login-form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Evita que el formulario se envíe de manera tradicional

    // Obtén los valores del formulario
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        // Intenta autenticar al usuario
        const response = await fetch(`${API_URL}/users/auth`, {
            method: 'POST',
            credentials: 'include', // Incluye cookies (si las usas para manejar sesiones)
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password }) // Envía el email y la contraseña
        });

        if (response.ok) {
            // Si la autenticación es exitosa, oculta los botones de login/registro
            document.getElementById('auth-buttons').style.display = 'none';
            // Muestra la sección de gestión de propiedades
            document.getElementById('property-management').style.display = 'block';
            // Carga las propiedades (si es necesario)
            loadProperties(currentPage);
            // Redirige al usuario a la página principal o muestra un mensaje de éxito
            alert('Login successful!');
            window.location.href = 'index.html'; // Redirige al usuario
        } else {
            // Si la autenticación falla, muestra un mensaje de error
            alert('Invalid email or password.');
            // Muestra los botones de login/registro (por si estaban ocultos)
            document.getElementById('auth-buttons').style.display = 'block';
            // Oculta la sección de gestión de propiedades
            document.getElementById('property-management').style.display = 'none';
        }
    } catch (error) {
        // Maneja errores de red o del servidor
        console.error('Error during authentication:', error);
        alert('An error occurred. Please try again later.');
    }
});

// Función para manejar el registro
document.getElementById('register-form')?.addEventListener('submit', async function(event) {
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
            alert('Registration successful!');
            window.location.href = 'login.html';
        } else if (response.status === 400) {
            // Error de validación (por ejemplo, email ya registrado)
            alert('Registration failed. The email may already be registered.');
        } else {
            // Otros errores (por ejemplo, 500)
            alert('Registration failed. Please try again.');
        }
    } catch (error) {
        console.error('Error during registration:', error);
        alert('An error occurred. Please try again later.');
    }
});