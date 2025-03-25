# CollageProgect

Вот описание для GitHub:  

---

# 🏠 Dormitory Booking System  

### 📌 Описание проекта  
**Dormitory Booking System** – это веб-приложение для бронирования мест в общежитии. Система автоматизирует процесс заселения, повышая удобство для студентов и администрации.  

### 🚀 Функционал  
✅ **Регистрация и авторизация** (Spring Security, JWT)  
✅ **Роли пользователей:** студент, администратор  
✅ **Бронирование мест** в общежитии через удобный интерфейс  
✅ **Подтверждение брони и уведомления через SMS**  
✅ **REST API** для взаимодействия с фронтендом или мобильными приложениями  
✅ **Админ-панель** для управления пользователями и бронированиями  

### 🛠 Используемые технологии  
- **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate  
- **Database:** PostgreSQL / MySQL  
- **API:** RESTful API  
- **Безопасность:** JWT (JSON Web Token), Spring Security  
- **Уведомления:** SMS API (Twilio / другой сервис)  

### 🔧 Запуск проекта  
1. Клонировать репозиторий:  
   ```sh
   git clone https://github.com/your-username/dormitory-booking-system.git
   cd dormitory-booking-system
   ```  
2. Настроить **application.properties** (указать параметры БД, JWT-секрет, SMS API).  
3. Запустить проект:  
   ```sh
   mvn spring-boot:run
   ```  
4. API будет доступен на `http://localhost:8080`.  

### 📜 Лицензия  
Этот проект распространяется под лицензией **MIT**.  

---
