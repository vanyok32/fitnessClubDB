let workouts = [];
let editingId = null;

// Загрузка тренировок
async function loadWorkouts() {
    try {
        workouts = await workoutsApi.getAll();
        renderWorkouts();
    } catch (error) {
        showMessage('Ошибка при загрузке тренировок: ' + error.message, 'error');
    }
}

// Отображение тренировок в таблице
function renderWorkouts() {
    const tbody = document.getElementById('workouts-tbody');
    if (workouts.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4">Нет тренировок</td></tr>';
        return;
    }

    tbody.innerHTML = workouts.map(workout => `
        <tr>
            <td>${workout.id}</td>
            <td>${workout.name}</td>
            <td>${workout.duration}</td>
            <td class="actions">
                <button class="btn" onclick="editWorkout(${workout.id})">Редактировать</button>
                <button class="btn btn-danger" onclick="deleteWorkout(${workout.id})">Удалить</button>
            </td>
        </tr>
    `).join('');
}

// Открытие модального окна для редактирования
function openEditModal() {
    editingId = null;
    document.getElementById('modal-title').textContent = 'Добавить тренировку';
    document.getElementById('workout-form').reset();
    document.getElementById('workout-id').value = '';
    document.getElementById('editModal').style.display = 'block';
}

// Редактирование тренировки
function editWorkout(id) {
    const workout = workouts.find(w => w.id === id);
    if (!workout) return;

    editingId = id;
    document.getElementById('modal-title').textContent = 'Редактировать тренировку';
    document.getElementById('workout-id').value = id;
    document.getElementById('workout-name').value = workout.name;
    document.getElementById('workout-duration').value = workout.duration;
    document.getElementById('editModal').style.display = 'block';
}

// Закрытие модального окна
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
    editingId = null;
}

// Удаление тренировки
async function deleteWorkout(id) {
    if (!confirm('Вы уверены, что хотите удалить эту тренировку?')) {
        return;
    }

    try {
        await workoutsApi.delete(id);
        showMessage('Тренировка успешно удалена');
        loadWorkouts();
    } catch (error) {
        showMessage('Ошибка при удалении тренировки: ' + error.message, 'error');
    }
}

// Обработка формы
document.getElementById('workout-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const workoutData = {
        name: document.getElementById('workout-name').value,
        duration: parseInt(document.getElementById('workout-duration').value)
    };

    try {
        if (editingId) {
            await workoutsApi.update(editingId, workoutData);
            showMessage('Тренировка успешно обновлена');
        } else {
            await workoutsApi.create(workoutData);
            showMessage('Тренировка успешно создана');
        }
        closeEditModal();
        loadWorkouts();
    } catch (error) {
        showMessage('Ошибка при сохранении тренировки: ' + error.message, 'error');
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
document.addEventListener('DOMContentLoaded', loadWorkouts);

