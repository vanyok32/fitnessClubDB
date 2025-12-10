// Автоматическое определение контекстного пути приложения
function getContextPath() {
    const path = window.location.pathname;
    // Удаляем имя файла и trailing slash (например, /index.html или /)
    let pathWithoutFile = path.replace(/\/[^\/]*\.html?$/, '').replace(/\/$/, '');

    // Если путь пустой или только "/", значит нет контекстного пути
    if (!pathWithoutFile || pathWithoutFile === '/') {
        return '';
    }

    // Возвращаем контекстный путь (например, /fitnessClubDB-1.0-SNAPSHOT)
    return pathWithoutFile;
}

const API_BASE_URL = getContextPath() + '/api';

// Для отладки (можно удалить после проверки)
console.log('API Base URL:', API_BASE_URL);
console.log('Current path:', window.location.pathname);

// Утилиты для работы с API
const api = {
    async get(url) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`);
            if (!response.ok) {
                const contentType = response.headers.get('content-type') || '';
                if (contentType.includes('application/json')) {
                    try {
                        const error = await response.json();
                        throw new Error(error.error || `Ошибка ${response.status}: ${response.statusText || 'Неизвестная ошибка'}`);
                    } catch (e) {
                        if (e instanceof SyntaxError) {
                            throw new Error(`Ошибка ${response.status}: Сервер вернул невалидный JSON. Проверьте, что сервер запущен и путь API правильный.`);
                        }
                        throw e;
                    }
                } else {
                    // Если пришел не JSON (например, HTML страница ошибки)
                    const statusText = response.statusText || 'Неизвестная ошибка';
                    throw new Error(`Ошибка ${response.status}: ${statusText}. Проверьте, что сервер запущен и путь API правильный. URL: ${API_BASE_URL}${url}`);
                }
            }
            const contentType = response.headers.get('content-type') || '';
            if (!contentType.includes('application/json')) {
                throw new Error(`Сервер вернул не JSON ответ (Content-Type: ${contentType}). Проверьте путь API. URL: ${API_BASE_URL}${url}`);
            }
            return await response.json();
        } catch (error) {
            // Если это уже наша ошибка, пробрасываем её дальше
            if (error.message.includes('Ошибка') || error.message.includes('Сервер')) {
                throw error;
            }
            // Иначе это сетевая ошибка
            throw new Error(`Ошибка сети: ${error.message}. Проверьте, что сервер запущен и доступен. URL: ${API_BASE_URL}${url}`);
        }
    },

    async post(url, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            });
            if (!response.ok) {
                const contentType = response.headers.get('content-type') || '';
                if (contentType.includes('application/json')) {
                    try {
                        const error = await response.json();
                        throw new Error(error.error || `Ошибка ${response.status}: ${response.statusText || 'Неизвестная ошибка'}`);
                    } catch (e) {
                        if (e instanceof SyntaxError) {
                            throw new Error(`Ошибка ${response.status}: Сервер вернул невалидный JSON. URL: ${API_BASE_URL}${url}`);
                        }
                        throw e;
                    }
                } else {
                    const statusText = response.statusText || 'Неизвестная ошибка';
                    throw new Error(`Ошибка ${response.status}: ${statusText}. URL: ${API_BASE_URL}${url}`);
                }
            }
            const contentType = response.headers.get('content-type') || '';
            if (contentType.includes('application/json')) {
                return await response.json();
            }
            return null;
        } catch (error) {
            if (error.message.includes('Ошибка')) {
                throw error;
            }
            throw new Error(`Ошибка сети: ${error.message}. URL: ${API_BASE_URL}${url}`);
        }
    },

    async put(url, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            });
            if (!response.ok) {
                const contentType = response.headers.get('content-type') || '';
                if (contentType.includes('application/json')) {
                    try {
                        const error = await response.json();
                        throw new Error(error.error || `Ошибка ${response.status}: ${response.statusText || 'Неизвестная ошибка'}`);
                    } catch (e) {
                        if (e instanceof SyntaxError) {
                            throw new Error(`Ошибка ${response.status}: Сервер вернул невалидный JSON. URL: ${API_BASE_URL}${url}`);
                        }
                        throw e;
                    }
                } else {
                    const statusText = response.statusText || 'Неизвестная ошибка';
                    throw new Error(`Ошибка ${response.status}: ${statusText}. URL: ${API_BASE_URL}${url}`);
                }
            }
            const contentType = response.headers.get('content-type') || '';
            if (contentType.includes('application/json')) {
                return await response.json();
            }
            return null;
        } catch (error) {
            if (error.message.includes('Ошибка')) {
                throw error;
            }
            throw new Error(`Ошибка сети: ${error.message}. URL: ${API_BASE_URL}${url}`);
        }
    },

    async delete(url) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                method: 'DELETE'
            });
            if (!response.ok && response.status !== 204) {
                const contentType = response.headers.get('content-type') || '';
                if (contentType.includes('application/json')) {
                    try {
                        const error = await response.json();
                        throw new Error(error.error || `Ошибка ${response.status}: ${response.statusText || 'Неизвестная ошибка'}`);
                    } catch (e) {
                        if (e instanceof SyntaxError) {
                            throw new Error(`Ошибка ${response.status}: Сервер вернул невалидный JSON. URL: ${API_BASE_URL}${url}`);
                        }
                        throw e;
                    }
                } else {
                    const statusText = response.statusText || 'Неизвестная ошибка';
                    throw new Error(`Ошибка ${response.status}: ${statusText}. URL: ${API_BASE_URL}${url}`);
                }
            }
            if (response.status === 204) {
                return null;
            }
            const contentType = response.headers.get('content-type') || '';
            if (contentType.includes('application/json')) {
                return await response.json();
            }
            return null;
        } catch (error) {
            if (error.message.includes('Ошибка')) {
                throw error;
            }
            throw new Error(`Ошибка сети: ${error.message}. URL: ${API_BASE_URL}${url}`);
        }
    }
};

// Функции для работы с клубами
const clubsApi = {
    getAll: () => api.get('/clubs'),
    getById: (id) => api.get(`/clubs/${id}`),
    update: (id, data) => api.put(`/clubs/${id}`, data),
    delete: (id) => api.delete(`/clubs/${id}`)
};

// Функции для работы с тренерами
const coachesApi = {
    getAll: () => api.get('/coaches'),
    getById: (id) => api.get(`/coaches/${id}`),
    getByClub: (clubId) => api.get(`/coaches?clubId=${clubId}`),
    getByClubAndSpec: (clubId, specId) => api.get(`/coaches?clubId=${clubId}&specId=${specId}`),
    delete: (id) => api.delete(`/coaches/${id}`)
};

// Функции для работы с расписанием
const schedulesApi = {
    getAll: () => api.get('/schedules'),
    getById: (id) => api.get(`/schedules/${id}`),
    getByCoach: (coachId) => api.get(`/schedules?coachId=${coachId}`),
    getByClient: (clientId) => api.get(`/schedules?clientId=${clientId}`),
    create: (data) => api.post('/schedules', data),
    update: (id, data) => api.put(`/schedules/${id}`, data),
    delete: (id) => api.delete(`/schedules/${id}`)
};

// Функции для работы с тренировками
const workoutsApi = {
    getAll: () => api.get('/workouts'),
    getById: (id) => api.get(`/workouts/${id}`),
    create: (data) => api.post('/workouts', data),
    update: (id, data) => api.put(`/workouts/${id}`, data),
    delete: (id) => api.delete(`/workouts/${id}`)
};

// Функции для работы со специализациями
const specializationsApi = {
    getAll: () => api.get('/specializations'),
    getById: (id) => api.get(`/specializations/${id}`),
    create: (data) => api.post('/specializations', data),
    update: (id, data) => api.put(`/specializations/${id}`, data),
    delete: (id) => api.delete(`/specializations/${id}`)
};

// Функции для работы с клиентами
const clientsApi = {
    getAll: () => api.get('/clients'),
    update: (id, data) => api.put(`/clients/${id}`, data),
    delete: (id) => api.delete(`/clients/${id}`)
};

// Функции для аутентификации
const authApi = {
    login: (email) => api.post('/auth/login', { email }),
    register: (data) => api.post('/auth/register', data)
};

// Функции для работы с отзывами
const feedbackApi = {
    getBySchedule: (scheduleId) => api.get(`/feedback?scheduleId=${scheduleId}`),
    create: (data) => api.post('/feedback', data),
    update: (data) => api.put('/feedback', data),
    delete: (scheduleId) => api.delete(`/feedback?scheduleId=${scheduleId}`)
};

// Функции для работы с членством
const membershipsApi = {
    getAll: () => api.get('/memberships'),
    getByClient: (clientId) => api.get(`/memberships?clientId=${clientId}`),
    create: (data) => api.post('/memberships', data),
    update: (data) => api.put('/memberships', data),
    activate: (id) => api.post(`/memberships/${id}/activate`),
    delete: (id) => api.delete(`/memberships/${id}`)
};

// Утилиты для отображения сообщений
function showMessage(message, type = 'success') {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    messageDiv.textContent = message;
    document.body.insertBefore(messageDiv, document.body.firstChild);
    setTimeout(() => messageDiv.remove(), 5000);
}

// Утилиты для форматирования дат
function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU');
}

