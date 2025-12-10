# 💪 Fitness Club Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7.5-blue?style=for-the-badge&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apache-maven)
![Jakarta Servlet](https://img.shields.io/badge/Jakarta%20Servlet-6.0-green?style=for-the-badge&logo=jakarta-ee)

**Современная система управления фитнес-клубом с веб-интерфейсом**

[Описание](#-описание) • [Технологии](#️-технологии) • [Архитектура](#-архитектура) • [API](#-api-endpoints) • [Установка](#-установка-и-запуск)

</div>

---

## 📋 Описание

**Fitness Club Management System** — это полнофункциональное веб-приложение для управления фитнес-клубом. Система позволяет управлять клубами, тренерами, клиентами, расписанием занятий, тренировками и членством. Приложение построено на основе трехслойной архитектуры (Controller-Service-Repository) и предоставляет RESTful API для взаимодействия с фронтендом.

### ✨ Основные возможности

- 🏢 **Управление клубами** — создание, редактирование и удаление фитнес-клубов
- 👨‍🏫 **Управление тренерами** — работа с тренерами и их специализациями
- 📅 **Расписание занятий** — планирование и управление расписанием тренировок
- 💪 **Типы тренировок** — каталог различных видов тренировок
- 👥 **Управление клиентами** — регистрация и управление клиентами
- ⭐ **Система отзывов** — отзывы клиентов о тренировках
- 🎫 **Членство** — управление абонементами и их активацией
- 🔐 **Аутентификация** — система входа и регистрации

---

## 🛠️ Технологии

### Backend

| Технология | Версия | Назначение |
|------------|--------|------------|
| ☕ **Java** | 21 | Основной язык программирования |
| 🌐 **Jakarta Servlet API** | 6.0.0 | Веб-фреймворк для создания REST API |
| 📦 **Jackson** | 2.19.0 | Сериализация/десериализация JSON |
| 🗄️ **PostgreSQL** | 42.7.5 | Реляционная база данных |
| 🔧 **Lombok** | 1.18.30 | Упрощение кода (аннотации) |
| 📝 **SLF4J** | 2.0.17 | Логирование |
| 🔑 **Java JWT** | 4.5.0 | Работа с JWT токенами |

### Frontend

| Технология | Описание |
|------------|----------|
| 📄 **HTML5** | Разметка страниц |
| 🎨 **CSS3** | Стилизация интерфейса |
| ⚡ **JavaScript (Vanilla)** | Клиентская логика и взаимодействие с API |

### Инструменты разработки

| Инструмент | Назначение |
|------------|------------|
| 🔨 **Maven** | Система сборки проекта |
| 🧪 **JUnit 5** | Тестирование |
| 🎭 **Mockito** | Мокирование для тестов |

---

## 🏗️ Архитектура

Проект следует принципам **трехслойной архитектуры**:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (Controllers / Servlets)                │
│  🎮 ClientController                    │
│  🎮 ClubController                      │
│  🎮 CoachController                     │
│  🎮 ScheduleController                  │
│  ...                                    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Business Layer                   │
│  (Services)                              │
│  💼 ClientService                        │
│  💼 ClubService                         │
│  💼 CoachService                        │
│  💼 ScheduleService                     │
│  ...                                    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Data Access Layer               │
│  (Repositories)                         │
│  💾 ClientRepository                    │
│  💾 ClubRepository                     │
│  💾 CoachRepository                    │
│  💾 ScheduleRepository                 │
│  ...                                    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Database                        │
│  🗄️ PostgreSQL                         │
└─────────────────────────────────────────┘
```

### 📁 Структура проекта

```
fitnessClubDB/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── fitness/club/
│   │   │       ├── controller/      # 🎮 REST контроллеры (сервлеты)
│   │   │       ├── service/         # 💼 Бизнес-логика
│   │   │       ├── repository/      # 💾 Доступ к данным
│   │   │       ├── entity/          # 📦 Сущности базы данных
│   │   │       ├── dto/             # 📋 Data Transfer Objects
│   │   │       ├── mapper/          # 🔄 Маппинг DTO ↔ Entity
│   │   │       ├── exeptions/       # ⚠️ Исключения
│   │   │       └── util/            # 🛠️ Утилиты
│   │   ├── resources/
│   │   │   ├── application.properties  # ⚙️ Конфигурация БД
│   │   │   └── logback.xml            # 📝 Настройки логирования
│   │   └── webapp/
│   │       ├── index.html            # 🏠 Главная страница
│   │       ├── clubs.html             # 🏢 Страница клубов
│   │       ├── coaches.html           # 👨‍🏫 Страница тренеров
│   │       ├── schedules.html        # 📅 Страница расписания
│   │       ├── workouts.html          # 💪 Страница тренировок
│   │       ├── login.html             # 🔐 Страница входа
│   │       ├── css/
│   │       │   └── style.css         # 🎨 Стили
│   │       └── js/
│   │           ├── api.js            # 🌐 API клиент
│   │           ├── clubs.js          # 🏢 Логика клубов
│   │           ├── coaches.js        # 👨‍🏫 Логика тренеров
│   │           ├── schedules.js     # 📅 Логика расписания
│   │           ├── workouts.js       # 💪 Логика тренировок
│   │           └── login.js          # 🔐 Логика входа
│   └── test/                          # 🧪 Тесты
├── pom.xml                            # 📦 Maven конфигурация
└── README.md                          # 📖 Документация
```

---

## 🎮 Контроллеры (REST API)

Все контроллеры реализованы как **Jakarta Servlets** с аннотацией `@WebServlet`:

| Контроллер | Endpoint | Описание |
|------------|----------|----------|
| 🎮 `ClientController` | `/api/clients/*` | Управление клиентами |
| 🎮 `ClubController` | `/api/clubs/*` | Управление клубами |
| 🎮 `CoachController` | `/api/coaches/*` | Управление тренерами |
| 🎮 `ScheduleController` | `/api/schedules/*` | Управление расписанием |
| 🎮 `SpecializationController` | `/api/specializations/*` | Управление специализациями |
| 🎮 `WorkoutController` | `/api/workouts/*` | Управление тренировками |
| 🎮 `AuthController` | `/api/auth/*` | Аутентификация |
| 🎮 `FeedbackController` | `/api/feedback/*` | Управление отзывами |
| 🎮 `MembershipController` | `/api/memberships/*` | Управление членством |
| 🔒 `CorsFilter` | `/*` | CORS фильтр для фронтенда |

---

## 📡 API Endpoints

### 🏢 Клубы (`/api/clubs`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/clubs` | Получить все клубы |
| `GET` | `/api/clubs/{id}` | Получить клуб по ID |
| `POST` | `/api/clubs` | Создать новый клуб |
| `PUT` | `/api/clubs/{id}` | Обновить клуб |
| `DELETE` | `/api/clubs/{id}` | Удалить клуб |

**Пример запроса:**

POST /api/clubs
```json
{
  "name": "Fitness Center",
  "address": "ул. Примерная, 123"
}
```
### 👨‍🏫 Тренеры (`/api/coaches`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/coaches` | Получить всех тренеров |
| `GET` | `/api/coaches?clubId={id}` | Получить тренеров по клубу |
| `GET` | `/api/coaches?clubId={clubId}&specId={specId}` | Получить тренеров по клубу и специализации |
| `GET` | `/api/coaches/{id}` | Получить тренера по ID |
| `DELETE` | `/api/coaches/{id}` | Удалить тренера |

### 📅 Расписание (`/api/schedules`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/schedules` | Получить все расписания |
| `GET` | `/api/schedules?coachId={id}` | Получить расписание по тренеру |
| `GET` | `/api/schedules?clientId={id}` | Получить расписание по клиенту |
| `GET` | `/api/schedules/{id}` | Получить расписание по ID |
| `POST` | `/api/schedules` | Создать запись в расписании |
| `PUT` | `/api/schedules/{id}` | Обновить запись в расписании |
| `DELETE` | `/api/schedules/{id}` | Удалить запись в расписании |

**Пример запроса:**

POST /api/schedules
```json
{
  "clientId": 1,
  "coachId": 2,
  "workoutId": 3,
  "date": "2024-12-25"
}
```

### 💪 Тренировки (`/api/workouts`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/workouts` | Получить все тренировки |
| `GET` | `/api/workouts/{id}` | Получить тренировку по ID |
| `POST` | `/api/workouts` | Создать тренировку |
| `PUT` | `/api/workouts/{id}` | Обновить тренировку |
| `DELETE` | `/api/workouts/{id}` | Удалить тренировку |

### 🎯 Специализации (`/api/specializations`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/specializations` | Получить все специализации |
| `GET` | `/api/specializations/{id}` | Получить специализацию по ID |
| `POST` | `/api/specializations` | Создать специализацию |
| `PUT` | `/api/specializations/{id}` | Обновить специализацию |
| `DELETE` | `/api/specializations/{id}` | Удалить специализацию |

### 👥 Клиенты (`/api/clients`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/clients` | Получить всех клиентов |
| `PUT` | `/api/clients/{id}` | Обновить клиента |
| `DELETE` | `/api/clients/{id}` | Удалить клиента |

### 🔐 Аутентификация (`/api/auth`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `POST` | `/api/auth/login` | Вход (требуется email) |
| `POST` | `/api/auth/register` | Регистрация (требуется name, email, clubId) |

**Пример регистрации:**

POST /api/auth/register
```json
{
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "clubId": 1
}
```


### ⭐ Отзывы (`/api/feedback`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/feedback?scheduleId={id}` | Получить отзыв по расписанию |
| `POST` | `/api/feedback` | Создать отзыв |
| `PUT` | `/api/feedback` | Обновить отзыв |
| `DELETE` | `/api/feedback?scheduleId={id}` | Удалить отзыв |

### 🎫 Членство (`/api/memberships`)

| Метод | Endpoint | Описание |
|-------|----------|----------|
| `GET` | `/api/memberships` | Получить все членства |
| `GET` | `/api/memberships?clientId={id}` | Получить членство по клиенту |
| `POST` | `/api/memberships` | Создать членство |
| `POST` | `/api/memberships/{id}/activate` | Активировать членство |
| `PUT` | `/api/memberships` | Обновить членство |
| `DELETE` | `/api/memberships/{id}` | Удалить членство |

---

## 🚀 Установка и запуск

### Предварительные требования

- ☕ **Java 21** или выше
- 🗄️ **PostgreSQL** 12+
- 🔨 **Maven** 3.8+
- 🌐 **Tomcat** 11.0+ или другой сервлет-контейнер

### Шаг 1: Клонирование репозитория

```bash
git clone <repository-url>
cd fitnessClubDB
```

### Шаг 2: Настройка базы данных

1. Создайте базу данных PostgreSQL:
```sql
CREATE DATABASE fitness_club;
```

2. Настройте подключение в `src/main/resources/application.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/fitness_club
db.username=your_username
db.password=your_password
```

3. Выполните SQL скрипты для создания таблиц (если есть)

### Шаг 3: Сборка проекта

```bash
mvn clean package
```

После успешной сборки будет создан файл `target/fitnessClubDB-1.0-SNAPSHOT.war`

### Шаг 4: Развертывание

#### Вариант 1: Tomcat

1. Скопируйте `fitnessClubDB-1.0-SNAPSHOT.war` в директорию `webapps` Tomcat
2. Запустите Tomcat
3. Приложение будет доступно по адресу: `http://localhost:8080/fitnessClubDB-1.0-SNAPSHOT/`

#### Вариант 2: IntelliJ IDEA

1. Настройте конфигурацию запуска Tomcat
2. Укажите артефакт `club:war exploded`
3. Запустите приложение через Run/Debug

### Шаг 5: Доступ к приложению

Откройте браузер и перейдите по адресу:
```
http://localhost:8080/fitnessClubDB-1.0-SNAPSHOT/
```

---

## 📝 Особенности реализации

### ✅ Что реализовано

- ✨ **RESTful API** — все endpoints следуют REST принципам
- 🔒 **CORS поддержка** — фильтр для работы с фронтендом
- 📦 **JSON сериализация** — использование Jackson для работы с JSON
- ⚠️ **Обработка ошибок** — централизованная обработка исключений
- 🎨 **Современный UI** — адаптивный дизайн с использованием CSS3
- 📱 **Responsive Design** — интерфейс адаптируется под разные устройства
- 🔄 **Асинхронные запросы** — использование Fetch API для AJAX запросов

### 🏗️ Архитектурные решения

- **Трехслойная архитектура** — разделение на Controller, Service, Repository
- **DTO паттерн** — использование Data Transfer Objects для передачи данных
- **Mapper паттерн** — преобразование между Entity и DTO
- **Repository паттерн** — абстракция доступа к данным
- **Dependency Injection** — через статические провайдеры в Entity

---

## 🧪 Тестирование

Для запуска тестов:

```bash
mvn test
```

Проект использует:
- 🧪 **JUnit 5** для unit-тестов
- 🎭 **Mockito** для мокирования зависимостей

---

## 📚 Дополнительная информация

### Формат ответов API

Все успешные ответы возвращаются в формате JSON:

```json
{
  "id": 1,
  "name": "Пример"
}
```

Ошибки возвращаются в формате:

```json
{
  "error": "Описание ошибки"
}
```

### HTTP коды ответов

- `200 OK` — успешный запрос
- `201 Created` — ресурс создан
- `204 No Content` — успешное удаление
- `400 Bad Request` — ошибка валидации
- `401 Unauthorized` — ошибка аутентификации
- `404 Not Found` — ресурс не найден
- `500 Internal Server Error` — внутренняя ошибка сервера

---

## 👥 Авторы

Разработано для управления фитнес-клубом

---

## 📄 Лицензия

Этот проект создан в образовательных целях.

---

<div align="center">

**Сделано с ❤️ используя Java и современные веб-технологии**

⭐ Если проект был полезен, поставьте звезду!

</div>
