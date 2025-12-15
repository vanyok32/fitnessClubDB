let coaches = [];
let clubs = [];
let specializations = [];
let coachSpecs = [];

const clubMap = new Map();
const specMap = new Map();
const coachSpecMap = new Map(); // coachId -> [spec names]

async function loadReferenceData() {
    const [clubsData, specsData, coachSpecsData] = await Promise.all([
        clubsApi.getAll(),
        specializationsApi.getAll(),
        coachSpecializationsApi.getAll()
    ]);
    clubs = clubsData || [];
    specializations = specsData || [];
    coachSpecs = coachSpecsData || [];
    rebuildMaps();
    fillFilterSelects();
}

function rebuildMaps() {
    clubMap.clear();
    specMap.clear();
    coachSpecMap.clear();

    clubs.forEach(c => clubMap.set(c.id, c));
    specializations.forEach(s => specMap.set(s.id, s));

    coachSpecs.forEach(pair => {
        const name = specMap.get(pair.specId)?.name;
        if (!name) return;
        if (!coachSpecMap.has(pair.coachId)) {
            coachSpecMap.set(pair.coachId, []);
        }
        coachSpecMap.get(pair.coachId).push(name);
    });
}

function fillFilterSelects() {
    const clubSelect = document.getElementById('filter-club');
    const specSelect = document.getElementById('filter-spec');
    if (clubSelect) {
        clubSelect.innerHTML = '<option value="">Все клубы</option>' +
            clubs.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
    }
    if (specSelect) {
        specSelect.innerHTML = '<option value="">Все специализации</option>' +
            specializations.map(s => `<option value="${s.id}">${s.name}</option>`).join('');
    }
}

async function loadCoaches() {
    try {
        await loadReferenceData();
        await fetchCoaches();
        renderCoaches();
    } catch (error) {
        showMessage('Ошибка при загрузке тренеров: ' + error.message, 'error');
    }
}

async function fetchCoaches() {
    const clubId = document.getElementById('filter-club').value;
    const specId = document.getElementById('filter-spec').value;
    const sort = document.getElementById('sort-by').value;

    coaches = await coachesApi.getAll({
        clubId: clubId || undefined,
        specId: specId || undefined,
        sort
    });
}

function renderCoaches() {
    const tbody = document.getElementById('coaches-tbody');
    if (!tbody) return;
    if (!coaches || coaches.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">Тренеры не найдены</td></tr>';
        return;
    }

    tbody.innerHTML = coaches.map(coach => `
        <tr>
            <td>${coach.id}</td>
            <td>${coach.name}</td>
            <td>${coach.email}</td>
            <td>${clubMap.get(coach.clubId)?.name || coach.clubId || '-'}</td>
            <td>${(coachSpecMap.get(coach.id) || []).join(', ') || '—'}</td>
        </tr>
    `).join('');
}

function applyFilters() {
    fetchCoaches()
        .then(renderCoaches)
        .catch(error => showMessage('Не удалось применить фильтр: ' + error.message, 'error'));
}

document.addEventListener('DOMContentLoaded', loadCoaches);

