function showMessage(message, type = 'info') {
    const container = document.getElementById('message-container');
    if (!container) {
        console.log(`[${type}] ${message}`);
        return;
    }

    const messageDiv = document.createElement('div');
    messageDiv.className = `message message-${type}`;
    messageDiv.textContent = message;

    container.innerHTML = '';
    container.appendChild(messageDiv);

    // // Автоматически скрыть через 5 секунд
    // setTimeout(() => {
    //     messageDiv.remove();
    // }, 5000);
}

function formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU');
}