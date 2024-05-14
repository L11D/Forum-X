# Forum-X
Api для форума. Используется **Spring Boot**, **Spring Security**, **PostgreSQL**, **Flyway** для миграций.

## Функционал:
1. Регистрация, аутентификация по JWT-токену.
    - Используется **Access-токен** и **refresh-токен.**
2. Создание, удаление, изменение категорий форума. Категории могут иметь дочерние категории.
3. Создание, удаление, изменение тем форума. Темы могут быть созданы только в категориях нижнего уровня
4. Создание, удаление, изменение сообщений форума. 
5. Получение полной структуры всех категорий форума в виде иерархии.
6. Получение списка тем форума с пагинацией и сортировкой.
7. Получение сообщений определенной темы форума с пагинацией и сортировкой.
8. Поиск категорий/тем/сообщений по подстроке.
9. Поиск сообщений по набору фильтров:
   - Текст сообщения.
   - Дата создания (возможность задания диапазона дат - от и до).
   - Автор сообщения (nickname).
   - Тема форума .
   - Категория форума.
10. Функционал админа:
    - Создание, удаление, изменение пользователей.
    - Блокировка пользователей.
    - Назначение польователям ролей (USER, MODERATOR, ADMIN).
11. Проверка соответствия действия пользователя и его прав:
    - Категории, темы, сообщения форума, созданные пользователем могут быть редактированным только этим пользователем, модератором и админом.
    - Модератор назначается на определенную категорию форума.
12. Добавление вложение к сообщению:
    - Вложение сохраняется в Minio.

Проект создан в учебных целях. В настоящее время находится в разработке, в скором времени будет добавлен новый функционал.
