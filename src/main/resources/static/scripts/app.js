const API_URL = 'https://localhost:8443/properties';
let currentPage = 0; // Página actual
const pageSize = 3;  // Tamaño de la página
let isSearchActive = false; // Indica si hay una búsqueda activa

document.getElementById('propertyForm').addEventListener('submit', async function(event) {
    event.preventDefault();
    const property = {
        address: document.getElementById('address').value,
        price: parseFloat(document.getElementById('price').value),
        size: parseInt(document.getElementById('size').value),
        description: document.getElementById('description').value
    };

    const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(property)
    });

    if (response.ok) {
        loadProperties(currentPage); // Recargar la página actual
        document.getElementById('propertyForm').reset();
    } else {
        alert('Error adding property');
    }
});

async function loadProperties(page) {
    const url = isSearchActive
        ? `${API_URL}/search?query=${document.getElementById('searchQuery').value}&page=${page}&size=${pageSize}`
        : `${API_URL}?page=${page}&size=${pageSize}`;

    const response = await fetch(url, {
        credentials: 'include'  // Incluir cookies de sesión
    });
    const data = await response.json();
    const properties = data.content; // Propiedades de la página actual
    const totalPages = data.totalPages; // Número total de páginas

    const list = document.getElementById('propertyList');
    list.innerHTML = '';
    properties.forEach(property => {
        const div = document.createElement('div');
        div.className = 'property';
        div.innerHTML = `
                <strong>${property.address}</strong> - $${property.price} - ${property.size} (m²)
                <p>${property.description}</p>
                <button onclick="showEditForm('${property.id}', '${property.address}', ${property.price}, ${property.size}, '${property.description}')">Update</button>
                <button onclick="deleteProperty('${property.id}')">Delete</button>
            `;
        list.appendChild(div);
    });

    // Actualizar controles de paginación
    document.getElementById('pageInfo').innerText = `Page ${page + 1} of ${totalPages}`;
    document.getElementById('prevPage').disabled = page === 0;
    document.getElementById('nextPage').disabled = page === totalPages - 1;
}

function loadNextPage() {
    currentPage++;
    loadProperties(currentPage);
}

function loadPreviousPage() {
    currentPage--;
    loadProperties(currentPage);
}

async function deleteProperty(id) {
    const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    if (response.ok) {
        // Verificar si la página actual quedó vacía después de eliminar
        const responseAfterDelete = await fetch(`${API_URL}?page=${currentPage}&size=${pageSize}`);
        const dataAfterDelete = await responseAfterDelete.json();
        if (dataAfterDelete.content.length === 0) {
            // Si la página actual quedó vacía, retroceder a la página anterior (si existe)
            if (currentPage > 0) {
                currentPage--; // Retroceder a la página anterior
            }
        }

        // Recargar la página actual
        loadProperties(currentPage);
    } else {
        alert('Error deleting property');
    }
}

function showEditForm(id, address, price, size, description) {
    // Deshabilitar todos los botones e inputs de la lista de propiedades
    const propertyButtons = document.querySelectorAll('.property button');
    const propertyInputs = document.querySelectorAll('.property input');
    propertyButtons.forEach(button => button.classList.add('disabled'));
    propertyInputs.forEach(input => input.disabled = true);

    // Deshabilitar los botones de paginación
    document.getElementById('prevPage').disabled = true;
    document.getElementById('nextPage').disabled = true;

    // Deshabilitar el campo de búsqueda y los botones de búsqueda/limpiar
    document.getElementById('searchQuery').disabled = true;
    document.querySelector('button[onclick="searchProperties()"]').disabled = true;
    document.querySelector('button[onclick="clearSearch()"]').disabled = true;

    // Deshabilitar el formulario de agregar propiedades
    document.getElementById('propertyForm').querySelectorAll('input').forEach(input => input.disabled = true);
    document.getElementById('propertyForm').querySelector('button').disabled = true;

    // Clonar el formulario de edición y mostrarlo en la parte superior
    const editForm = document.getElementById('editFormTemplate').cloneNode(true);
    editForm.id = 'editForm';
    editForm.style.display = 'block';

    // Llenar el formulario con los datos actuales
    editForm.querySelector('#editAddress').value = address;
    editForm.querySelector('#editPrice').value = price;
    editForm.querySelector('#editSize').value = size;
    editForm.querySelector('#editDescription').value = description;

    // Agregar el formulario de edición al contenedor en la parte superior
    const editFormContainer = document.getElementById('editFormContainer');
    editFormContainer.innerHTML = ''; // Limpiar el contenedor
    editFormContainer.appendChild(editForm);

    // Manejar el envío del formulario de edición
    editForm.querySelector('form').addEventListener('submit', async function(event) {
        event.preventDefault();
        const updatedProperty = {
            address: editForm.querySelector('#editAddress').value,
            price: parseFloat(editForm.querySelector('#editPrice').value),
            size: parseInt(editForm.querySelector('#editSize').value),
            description: editForm.querySelector('#editDescription').value
        };

        const response = await fetch(`${API_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedProperty)
        });

        if (response.ok) {
            loadProperties(currentPage);
            cancelEdit();
        } else {
            alert('Error updating property');
        }
    });
}

function cancelEdit() {
    // Ocultar el formulario de edición
    const editFormContainer = document.getElementById('editFormContainer');
    editFormContainer.innerHTML = '';

    // Habilitar todos los botones e inputs de la lista de propiedades
    const propertyButtons = document.querySelectorAll('.property button');
    const propertyInputs = document.querySelectorAll('.property input');
    propertyButtons.forEach(button => button.classList.remove('disabled'));
    propertyInputs.forEach(input => input.disabled = false);

    // Habilitar los botones de paginación
    document.getElementById('prevPage').disabled = false;
    document.getElementById('nextPage').disabled = false;

    // Habilitar el campo de búsqueda y los botones de búsqueda/limpiar
    document.getElementById('searchQuery').disabled = false;
    document.querySelector('button[onclick="searchProperties()"]').disabled = false;
    document.querySelector('button[onclick="clearSearch()"]').disabled = false;

    // Habilitar el formulario de agregar propiedades
    document.getElementById('propertyForm').querySelectorAll('input').forEach(input => input.disabled = false);
    document.getElementById('propertyForm').querySelector('button').disabled = false;
}

async function searchProperties() {
    const query = document.getElementById('searchQuery').value;
    if (!query) {
        alert('Please enter a search term.');
        return;
    }

    isSearchActive = true;
    currentPage = 0; // Reiniciar a la primera página

    const url = `${API_URL}/search?query=${encodeURIComponent(query)}&page=${currentPage}&size=${pageSize}`;
    const response = await fetch(url);

    if (response.ok) {
        const data = await response.json();
        if (data.content.length === 0) {
            alert('No properties found matching your search.');
        }
        loadProperties(currentPage); // Mostrar los resultados de la búsqueda
    } else {
        alert('Error searching properties');
    }
}

function clearSearch() {
    isSearchActive = false;
    document.getElementById('searchQuery').value = ''; // Limpiar el campo de búsqueda
    currentPage = 0; // Reiniciar a la primera página
    loadProperties(currentPage);
}

// Cargar la primera página al iniciar
loadProperties(currentPage);