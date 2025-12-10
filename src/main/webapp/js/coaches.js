let coaches = [];
let clubs = [];

// Загрузка тренеров
async function loadCoaches(clubId = null) {
    try {
        if (clubId) {
            coaches = await coachesApi.getByClub(clubId);
        } else {
            coaches = await coachesApi.getAll();
        }
        renderCoaches();
    } catch (error) {
        showMessage('Ошибка при загрузке тренеров: ' + error.message, 'error');
    }
}

// Загрузка клубов для фильтра
async function loadClubs() {
    try {
        clubs = await clubsApi.getAll();
        const select = document.getElementById('club-filter');
        select.innerHTML = '<option value="">Все клубы</option>' +
            clubs.map(club => `<option value="${club.id}">${club.name}</option>`).join('');
    } catch (error) {
        console.error('Ошибка при загрузке клубов:', error);
    }
}

// Фильтрация тренеров по клубу
function filterCoaches() {
    const clubId = document.getElementById('club-filter').value;
    loadCoaches(clubId || null);
}

// Отображение тренеров в таблице
function renderCoaches() {
    const tbody = document.getElementById('coaches-tbody');
    if (coaches.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">Нет тренеров</td></tr>';
        return;
    }

    tbody.innerHTML = coaches.map(coach => `
        <tr>
            <td>${coach.id}</td>
            <td>${coach.name}</td>
            <td>${coach.email}</td>
            <td>${coach.clubId}</td>
            <td class="actions">
                <button class="btn btn-danger" onclick="deleteCoach(${coach.id})">Удалить</button>
            </td>
        </tr>
    `).join('');
}

// Удаление тренера
async function deleteCoach(id) {
    if (!confirm('Вы уверены, что хотите удалить этого тренера?')) {
        return;
    }

    try {
        await coachesApi.delete(id);
        showMessage('Тренер успешно удален');
        loadCoaches();
    } catch (error) {
        showMessage('Ошибка при удалении тренера: ' + error.message, 'error');
    }
}

// Загрузка при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    loadClubs();
    loadCoaches();
});

