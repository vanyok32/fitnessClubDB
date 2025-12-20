// Простая обертка над fetch для работы с REST API
async function request(url, {method = 'GET', data} = {}) {
    const options = {method, headers: {}};
    if (data !== undefined) {
        options.headers['Content-Type'] = 'application/json';
        options.body = JSON.stringify(data);
    }

    const response = await fetch(url, options);
    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `HTTP ${response.status}`);
    }
    if (response.status === 204) {
        return null;
    }
    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/json')) {
        return response.json();
    }
    return response.text();
}

function buildQuery(params = {}) {
    const searchParams = new URLSearchParams();
    Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== '') {
            searchParams.append(key, value);
        }
    });
    const queryString = searchParams.toString();
    return queryString ? `?${queryString}` : '';
}

function createCrudApi(basePath) {
    return {
        async getAll(params) {
            return request(`${basePath}${buildQuery(params)}`);
        },
        async get(id) {
            return request(`${basePath}/${id}`);
        },
        async create(data) {
            return request(basePath, {method: 'POST', data});
        },
        async update(id, data) {
            return request(`${basePath}/${id}`, {method: 'PUT', data});
        },
        async delete(id) {
            return request(`${basePath}/${id}`, {method: 'DELETE'});
        }
    };
}

const clubsApi = createCrudApi('/api/clubs');
const coachesApi = createCrudApi('/api/coaches');
const schedulesApi = createCrudApi('/api/schedules');
const workoutsApi = createCrudApi('/api/workouts');
const clientsApi = createCrudApi('/api/clients');
const specializationsApi = createCrudApi('/api/specializations');
const coachSpecializationsApi = createCrudApi('/api/coach-specializations');
const clientWorkoutsApi = createCrudApi('/api/client/workouts');
const membershipsApi = createCrudApi('/api/memberships');
const feedbacksApi = createCrudApi('/api/feedbacks');
const authApi = {
    async login(email, password) {
        return request('/api/auth/login', {method: 'POST', data: {email, password}});
    },
    async register(data) {
        return request('/api/auth/register', {method: 'POST', data});
    }
};

