# Forum-X
Api для формума. Используется **Spring Boot**, **Spring Security**, **PostgreSQL**, **Flyway** для миграций.

## Функционал:
1. Регистрация, аутентификация по JWT-токену.
2. Создание, удаление, изменение категорий форума. Категории могут иметь дочерние категории.
3. Создание, удаление, изменение тем форума. Темы могут быть созданы только в категориях нижнего уровня
4. Создание, удаление, изменение сообщений форума. 
5. Получение полной структуры всех категорий форума в виде иерархии.
6. Получение списка тем форума с пагинацией и сортировкой.
7. Получение сообщений определенной темы форума с пагинацией и сортировкой.
8. Поиск категорий/тем/сообщений по подстроке.

Проект создан в учебных целях. В настоящее время находится в разработке, в скором времени будет добавлен новый функционал.