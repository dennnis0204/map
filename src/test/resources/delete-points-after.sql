DELETE
FROM charging_points
WHERE user_id = (SELECT u.id FROM users u WHERE u.email = 'john@gmail.com');