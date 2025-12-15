let clubs = [];
let editingId = null;

// Загрузка клубов
async function loadClubs() {
    try {
        clubs = await clubsApi.getAll();
        renderClubs();
    } catch (error) {
        showMessage('Ошибка при загрузке клубов: ' + error.message, 'error');
    }
}

// Отображение клубов в таблице
function renderClubs() {
    const tbody = document.getElementById('clubs-tbody');
    if (clubs.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4">Нет клубов</td></tr>';
        return;
    }

    tbody.innerHTML = clubs.map(club => `
        <tr>
            <td>${club.id}</td>
            <td>${club.name}</td>
            <td>${club.address}</td>
            <td class="actions">
                <button class="btn" onclick="editClub(${club.id})">Редактировать</button>
                <button class="btn btn-danger" onclick="deleteClub(${club.id})">Удалить</button>
            </td>
        </tr>
    `).join('');
}

// Открытие модального окна для редактирования
function openEditModal() {
    editingId = null;
    document.getElementById('modal-title').textContent = 'Добавить клуб';
    document.getElementById('club-form').reset();
    document.getElementById('club-id').value = '';
    document.getElementById('editModal').style.display = 'block';
}

// Редактирование клуба
function editClub(id) {
    const club = clubs.find(c => c.id === id);
    if (!club) return;

    editingId = id;
    document.getElementById('modal-title').textContent = 'Редактировать клуб';
    document.getElementById('club-id').value = id;
    document.getElementById('club-name').value = club.name;
    document.getElementById('club-address').value = club.address;
    document.getElementById('editModal').style.display = 'block';
}

// Закрытие модального окна
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
    editingId = null;
}

// Удаление клуба
async function deleteClub(id) {
    if (!confirm('Вы уверены, что хотите удалить этот клуб?')) {
        return;
    }

    try {
        await clubsApi.delete(id);
        showMessage('Клуб успешно удален');
        loadClubs();
    } catch (error) {
        showMessage('Ошибка при удалении клуба: ' + error.message, 'error');
    }
}

// Обработка формы
document.getElementById('club-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const clubData = {
        name: document.getElementById('club-name').value,
        address: document.getElementById('club-address').value
    };

    try {
        if (editingId) {
            await clubsApi.update(editingId, clubData);
            showMessage('Клуб успешно обновлен');
        } else {
            await clubsApi.create(clubData);
            showMessage('Клуб успешно создан');
        }
        closeEditModal();
        loadClubs();
    } catch (error) {
        showMessage('Ошибка при сохранении клуба: ' + error.message, 'error');
    }
});

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    const modal = document.getElementById('editModal');
    if (event.target === modal) {
        closeEditModal();
    }
}

// Загрузка при загрузке страницы
document.addEventListener('DOMContentLoaded', loadClubs);



