// Обработка формы входа
document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    try {
        const user = await authApi.login(email, password);
        showMessage('Успешный вход! Добро пожаловать, ' + user.name, 'success');
        localStorage.setItem('user', JSON.stringify(user));
        window.location.href = 'index.html';
    } catch (error) {
        showMessage('Ошибка при входе: ' + error.message, 'error');
    }
});

// Обработка формы регистрации
document.getElementById('register-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const registerData = {
        name: document.getElementById('register-name').value,
        email: document.getElementById('register-email').value,
        clubId: parseInt(document.getElementById('register-club-id').value),
        password: document.getElementById('register-password').value
    };

    try {
        const user = await authApi.register(registerData);
        showMessage('Регистрация успешна! Добро пожаловать, ' + user.name, 'success');
        localStorage.setItem('user', JSON.stringify(user));
        document.getElementById('register-form').reset();
    } catch (error) {
        showMessage('Ошибка при регистрации: ' + error.message, 'error');
    }
});



