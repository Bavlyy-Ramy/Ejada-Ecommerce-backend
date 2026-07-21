INSERT INTO users (
    username,
    email,
    password,
    first_name,
    last_name
)
VALUES (
           'admin',
           'admin@ecommerce.com',
           '$2a$10$L8tIUf.jliRPp6cjqYkDSu2D44LQ.yGwjndQJH6w13.iX7A46VV2q',
           'System',
           'Administrator'
       );

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ADMIN'
WHERE u.username = 'admin';