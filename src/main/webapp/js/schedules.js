let schedules = [];
let editingSchedule = null;
let clients = [];
let coaches = [];
let workouts = [];

const clientMap = new Map();
const coachMap = new Map();
const workoutMap = new Map();

// Загрузка справочников и расписания
async function loadSchedules() {
    try {
        const [clientsData, coachesData, workoutsData, schedulesData] = await Promise.all([
            clientsApi.getAll(),
            coachesApi.getAll(),
            workoutsApi.getAll(),
            schedulesApi.getAll()
        ]);

        clients = clientsData || [];
        coaches = coachesData || [];
        workouts = workoutsData || [];
        schedules = schedulesData || [];

        rebuildMaps();
        fillSelects();
        renderSchedules();
    } catch (error) {
        showMessage('Ошибка при загрузке расписания: ' + error.message, 'error');
    }
}

function rebuildMaps() {
    clientMap.clear();
    coachMap.clear();
    workoutMap.clear();

    clients.forEach(c => clientMap.set(c.id, c));
    coaches.forEach(c => coachMap.set(c.id, c));
    workouts.forEach(w => workoutMap.set(w.id, w));
}

function fillSelects() {
    fillSelect('schedule-client-id', clients, 'name');
    fillSelect('schedule-coach-id', coaches, 'name');
    fillSelect('schedule-workout-id', workouts, 'name');
}

function fillSelect(selectId, items, labelField) {
    const select = document.getElementById(selectId);
    if (!select) return;
    const placeholder = select.querySelector('option')?.outerHTML || '<option value="">Выберите</option>';
    select.innerHTML = placeholder + items
        .map(item => `<option value="${item.id}">${item[labelField] || item.id}</option>`)
        .join('');
}

// Отображение расписания в таблице
function renderSchedules() {
    const tbody = document.getElementById('schedules-tbody');
    if (schedules.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">Нет записей в расписании</td></tr>';
        return;
    }

    tbody.innerHTML = schedules.map(schedule => `
        <tr>
            <td>${schedule.id}</td>
            <td>${getNameById(clientMap, schedule.clientId, 'клиент')}</td>
            <td>${getNameById(coachMap, schedule.coachId, 'тренер')}</td>
            <td>${getNameById(workoutMap, schedule.workoutId, 'тренировка')}</td>
            <td>${formatDate(schedule.date)}</td>
            <td class="actions">
                <button class="btn" onclick="editSchedule(${schedule.id})">Редактировать</button>
                <button class="btn btn-danger" onclick="deleteSchedule(${schedule.id})">Удалить</button>
            </td>
        </tr>
    `).join('');
}

function getNameById(map, id, fallbackLabel) {
    const entity = map.get(id);
    return entity ? entity.name : `ID ${id || ''} (${fallbackLabel})`;
}

// Открытие модального окна для редактирования
function openEditModal() {
    editingSchedule = null;
    document.getElementById('modal-title').textContent = 'Добавить запись в расписание';
    document.getElementById('schedule-form').reset();
    document.getElementById('editModal').style.display = 'block';
}

// Редактирование записи
function editSchedule(id) {
    const schedule = schedules.find(s => s.id === id);
    if (!schedule) return;

    editingSchedule = schedule;
    document.getElementById('modal-title').textContent = 'Редактировать запись';
    document.getElementById('schedule-client-id').value = schedule.clientId;
    document.getElementById('schedule-coach-id').value = schedule.coachId;
    document.getElementById('schedule-workout-id').value = schedule.workoutId;
    document.getElementById('schedule-date').value = schedule.date;
    document.getElementById('editModal').style.display = 'block';
}

// Закрытие модального окна
function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
    editingSchedule = null;
}

// Удаление записи
async function deleteSchedule(id) {
    if (!confirm('Вы уверены, что хотите удалить эту запись?')) {
        return;
    }

    try {
        await schedulesApi.delete(id);
        showMessage('Запись успешно удалена');
        loadSchedules();
    } catch (error) {
        showMessage('Ошибка при удалении записи: ' + error.message, 'error');
    }
}

// Обработка формы
document.getElementById('schedule-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const scheduleData = {
        clientId: parseInt(document.getElementById('schedule-client-id').value),
        coachId: parseInt(document.getElementById('schedule-coach-id').value),
        workoutId: parseInt(document.getElementById('schedule-workout-id').value),
        date: document.getElementById('schedule-date').value
    };

    if (!scheduleData.clientId || !scheduleData.coachId || !scheduleData.workoutId) {
        showMessage('Выберите клиента, тренера и тренировку', 'error');
        return;
    }

    try {
        if (editingSchedule) {
            await schedulesApi.update(editingSchedule.id, scheduleData);
            showMessage('Запись успешно обновлена');
        } else {
            await schedulesApi.create(scheduleData);
            showMessage('Запись успешно создана');
        }
        closeEditModal();
        loadSchedules();
    } catch (error) {
        showMessage('Ошибка при сохранении записи: ' + error.message, 'error');
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
document.addEventListener('DOMContentLoaded', loadSchedules);

