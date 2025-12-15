// ============================================
// МОДУЛЬ УПРАВЛЕНИЯ ТРЕНИРОВКАМИ И ОТЗЫВАМИ
// ============================================

let myWorkouts = [];
let clientId = null;
let currentScheduleId = null;

// ============================================
// УТИЛИТЫ
// ============================================

/**
 * Получение ID клиента из localStorage
 */
function getClientId() {
    try {
        const userStr = localStorage.getItem('user');
        if (userStr) {
            const user = JSON.parse(userStr);
            return user?.id || null;
        }
    } catch (error) {
        console.error('Error parsing user from localStorage:', error);
    }
    return null;
}

// ============================================
// ЗАГРУЗКА И ОТОБРАЖЕНИЕ ТРЕНИРОВОК
// ============================================

/**
 * Загрузка тренировок клиента
 */
async function loadMyWorkouts() {
    const tbody = document.getElementById('my-workouts-tbody');
    if (!tbody) return;

    clientId = getClientId();
    if (!clientId) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #999;">Войдите в систему для просмотра тренировок</td></tr>';
        return;
    }

    try {
        tbody.innerHTML = '<tr><td colspan="6" class="loading">Загрузка тренировок...</td></tr>';
        myWorkouts = await clientWorkoutsApi.getByClientId(clientId);
        renderMyWorkouts();
    } catch (error) {
        console.error('Error loading workouts:', error);
        tbody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: #e74c3c;">Ошибка при загрузке тренировок: ${error.message}</td></tr>`;
        showMessage('Ошибка при загрузке тренировок: ' + error.message, 'error');
    }
}

/**
 * Отображение тренировок в таблице
 */
function renderMyWorkouts() {
    const tbody = document.getElementById('my-workouts-tbody');
    if (!tbody) return;

    if (!myWorkouts || myWorkouts.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #999;">У вас пока нет запланированных тренировок</td></tr>';
        return;
    }

    tbody.innerHTML = myWorkouts.map(workout => {
        const hasFeedback = workout.feedback && workout.feedback.rating;
        const feedbackButton = hasFeedback
            ? '<span style="color: #27ae60;">✓ Отзыв добавлен</span>'
            : `<button class=\"btn btn-feedback\" data-schedule-id=\"${workout.id}\" type=\"button\">Добавить отзыв</button>`;

        const feedbackDisplay = hasFeedback
            ? `<div style="padding: 0.5rem;">
                <div><strong>Оценка:</strong> <span style="color: #f39c12;">${'★'.repeat(workout.feedback.rating)}${'☆'.repeat(5 - workout.feedback.rating)}</span> (${workout.feedback.rating}/5)</div>
                ${workout.feedback.comment ? `<div style="margin-top: 0.5rem;"><strong>Комментарий:</strong> ${workout.feedback.comment}</div>` : ''}
               </div>`
            : '<span style="color: #999;">Нет отзыва</span>';

        return `
        <tr>
            <td>${workout.id || '-'}</td>
            <td>${workout.coachId || '-'}</td>
            <td>${workout.workoutId || '-'}</td>
            <td>${formatDate(workout.date) || '-'}</td>
            <td>${feedbackDisplay}</td>
            <td class="actions">${feedbackButton}</td>
        </tr>
        `;
    }).join('');
}

// ============================================
// УПРАВЛЕНИЕ МОДАЛЬНЫМ ОКНОМ ОТЗЫВОВ
// ============================================

/**
 * Инициализация модального окна отзывов
 */
function initFeedbackModal() {
    const modal = document.getElementById('feedbackModal');
    if (!modal) {
        console.error('Feedback modal not found in DOM');
        return;
    }

    // Обработчик закрытия по клику на фон
    modal.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeFeedbackModal();
        }
    });

    // Обработчик закрытия по кнопке X
    const closeBtn = document.getElementById('close-feedback-modal');
    if (closeBtn) {
        closeBtn.addEventListener('click', closeFeedbackModal);
    }

    // Обработчик закрытия по кнопке Отмена
    const cancelBtn = document.getElementById('cancel-feedback-btn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', closeFeedbackModal);
    }

    // Обработчик закрытия по ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && modal.style.display === 'block') {
            closeFeedbackModal();
        }
    });

    // Обработчик отправки формы
    const feedbackForm = document.getElementById('feedback-form');
    if (feedbackForm) {
        feedbackForm.addEventListener('submit', handleFeedbackSubmit);
    } else {
        console.error('Feedback form not found');
    }
}

/**
 * Открытие модального окна для добавления отзыва
 */
function openFeedbackModal(scheduleId) {
    if (!scheduleId) {
        console.error('ScheduleId is required');
        showMessage('Ошибка: не указан ID тренировки', 'error');
        return;
    }

    currentScheduleId = scheduleId;
    const modal = document.getElementById('feedbackModal');
    const ratingSelect = document.getElementById('feedback-rating');
    const commentTextarea = document.getElementById('feedback-comment');

    if (!modal) {
        console.error('Modal element not found');
        showMessage('Ошибка: модальное окно не найдено', 'error');
        return;
    }

    // Сброс формы
    if (ratingSelect) ratingSelect.value = '';
    if (commentTextarea) commentTextarea.value = '';

    // Показываем модальное окно
    modal.style.display = 'block';
    document.body.style.overflow = 'hidden'; // Блокируем прокрутку фона
}

/**
 * Закрытие модального окна
 */
function closeFeedbackModal() {
    const modal = document.getElementById('feedbackModal');
    if (modal) {
        modal.style.display = 'none';
        document.body.style.overflow = ''; // Восстанавливаем прокрутку
    }
    currentScheduleId = null;
}

/**
 * Обработка отправки формы отзыва
 */
async function handleFeedbackSubmit(e) {
    e.preventDefault();

    if (!currentScheduleId) {
        showMessage('Ошибка: не выбран ID тренировки', 'error');
        return;
    }

    const ratingSelect = document.getElementById('feedback-rating');
    const commentTextarea = document.getElementById('feedback-comment');

    if (!ratingSelect || !commentTextarea) {
        showMessage('Ошибка: элементы формы не найдены', 'error');
        return;
    }

    const rating = parseInt(ratingSelect.value);
    const comment = (commentTextarea.value || '').trim();

    // Валидация
    if (!rating || isNaN(rating) || rating < 1 || rating > 5) {
        showMessage('Пожалуйста, выберите оценку от 1 до 5', 'error');
        ratingSelect.focus();
        return;
    }

    try {
        // Показываем индикатор загрузки
        const submitButton = e.target.querySelector('button[type="submit"]');
        const originalText = submitButton?.textContent;
        if (submitButton) {
            submitButton.disabled = true;
            submitButton.textContent = 'Сохранение...';
        }

        const feedbackData = {
            scheduleId: currentScheduleId,
            rating: rating,
            comment: comment
        };

        await feedbacksApi.create(feedbackData);
        showMessage('Отзыв успешно добавлен!', 'success');
        closeFeedbackModal();

        // Перезагружаем список тренировок
        await loadMyWorkouts();
    } catch (error) {
        console.error('Error submitting feedback:', error);
        showMessage('Ошибка при добавлении отзыва: ' + (error.message || 'Неизвестная ошибка'), 'error');
    } finally {
        // Восстанавливаем кнопку
        const submitButton = e.target.querySelector('button[type="submit"]');
        if (submitButton) {
            submitButton.disabled = false;
            submitButton.textContent = 'Сохранить отзыв';
        }
    }
}

// ============================================
// ЗАГРУЗКА И ОТОБРАЖЕНИЕ АБОНЕМЕНТА
// ============================================

/**
 * Загрузка информации об абонементе
 */
async function loadMembership() {
    const membershipInfo = document.getElementById('membership-info');
    if (!membershipInfo) {
        console.error('membership-info element not found');
        return;
    }

    // Показываем индикатор загрузки
    membershipInfo.innerHTML = '<p class="loading">Загрузка информации об абонементе...</p>';

    clientId = getClientId();
    if (!clientId) {
        membershipInfo.innerHTML = '<div class="card"><p style="color: #999; text-align: center; padding: 1rem;">Войдите в систему для просмотра информации об абонементе</p></div>';
        return;
    }

    try {
        const membership = await membershipsApi.getByClientId(clientId);
        if (membership) {
            renderMembership(membership);
        } else {
            membershipInfo.innerHTML = '<div class="card"><p style="color: #999; text-align: center; padding: 1rem;">У вас пока нет активного абонемента</p></div>';
        }
    } catch (error) {
        console.error('Error loading membership:', error);

        // Определяем тип ошибки
        const isNotFound = error.status === 404 ||
            error.message?.includes('not found') ||
            error.message?.includes('404') ||
            error.message?.includes('Membership with clientId') ||
            error.message?.includes('SC_NOT_FOUND');

        if (isNotFound) {
            membershipInfo.innerHTML = `
                <div class="card">
                    <h3 style="color: #2c3e50; margin-bottom: 1rem;">Мой абонемент</h3>
                    <p style="color: #999; text-align: center; padding: 1rem;">
                        У вас пока нет активного абонемента.<br>
                        Обратитесь к администратору для оформления абонемента.
                    </p>
                </div>
            `;
        } else {
            membershipInfo.innerHTML = `
                <div class="card">
                    <p style="color: #e74c3c; text-align: center; padding: 1rem;">
                        Ошибка при загрузке информации об абонементе.<br>
                        <small>${error.message || 'Неизвестная ошибка'}</small>
                    </p>
                </div>
            `;
        }
    }
}

/**
 * Отображение информации об абонементе
 */
function renderMembership(membership) {
    const membershipInfo = document.getElementById('membership-info');
    if (!membershipInfo || !membership) return;

    const startDate = formatDate(membership.startDate);
    const endDate = formatDate(membership.endDate);
    const isActive = membership.isActive === true || membership.isActive === 'true';

    // Проверяем, не истек ли абонемент
    const endDateObj = membership.endDate ? new Date(membership.endDate) : null;
    const isExpired = endDateObj && endDateObj < new Date();
    const actualStatus = isActive && !isExpired;

    const statusClass = actualStatus ? 'success' : 'error';
    const statusText = actualStatus ? 'Активен' : (isExpired ? 'Истек' : 'Неактивен');
    const statusIcon = actualStatus ? '✓' : '✗';

    membershipInfo.innerHTML = `
        <div class="card">
            <h3 style="color: #2c3e50; margin-bottom: 1.5rem; display: flex; align-items: center; gap: 0.5rem;">
                <span>Мой абонемент</span>
                <span class="message message-${statusClass}" style="margin-left: auto; padding: 0.25rem 0.75rem; font-size: 0.9rem;">
                    ${statusIcon} ${statusText}
                </span>
            </h3>
            <table style="width: 100%; border-collapse: collapse;">
                <tr style="border-bottom: 1px solid #eee;">
                    <td style="padding: 0.75rem 0; font-weight: bold; color: #2c3e50;">Дата начала:</td>
                    <td style="padding: 0.75rem 0; color: #666;">${startDate || 'Не указана'}</td>
                </tr>
                <tr style="border-bottom: 1px solid #eee;">
                    <td style="padding: 0.75rem 0; font-weight: bold; color: #2c3e50;">Дата окончания:</td>
                    <td style="padding: 0.75rem 0; color: #666;">${endDate || 'Не указана'}</td>
                </tr>
                ${isExpired ? `
                <tr>
                    <td colspan="2" style="padding: 0.75rem 0; color: #e74c3c; font-size: 0.9rem;">
                        ⚠ Абонемент истек. Обратитесь к администратору для продления.
                    </td>
                </tr>
                ` : ''}
            </table>
        </div>
    `;
}

// ============================================
// ИНИЦИАЛИЗАЦИЯ И ОБРАБОТЧИКИ СОБЫТИЙ
// ============================================

/**
 * Инициализация обработчиков событий
 */
function initEventHandlers() {
    // Делегирование событий для кнопок добавления отзыва
    // Используем document для гарантии работы даже после перерисовки
    document.addEventListener('click', (e) => {
        const button = e.target.closest('.btn-feedback');
        if (button) {
            e.preventDefault();
            e.stopPropagation();

            const scheduleId = parseInt(button.getAttribute('data-schedule-id'));
            if (scheduleId && !isNaN(scheduleId)) {
                openFeedbackModal(scheduleId);
            } else {
                console.error('Invalid scheduleId:', button.getAttribute('data-schedule-id'));
                showMessage('Ошибка: неверный ID тренировки', 'error');
            }
        }
    });

    // Инициализация модального окна
    initFeedbackModal();
}

/**
 * Инициализация при загрузке страницы
 */
function init() {
    // Загружаем данные
    loadMyWorkouts();
    loadMembership();

    // Инициализируем обработчики событий
    initEventHandlers();
}

// Запускаем при загрузке DOM
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    // DOM уже загружен
    init();
}

// Экспортируем функции для глобального доступа (на случай использования в HTML)
window.addFeedback = openFeedbackModal;
window.closeFeedbackModal = closeFeedbackModal;
