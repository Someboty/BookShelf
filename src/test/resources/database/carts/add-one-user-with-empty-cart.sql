insert into users (id, email, password, first_name, last_name, shipping_address, is_deleted) values (1, 'test@mail.com', 'test_password', 'test_name', 'test_last_name', 'Ukraine', false);
insert into users_roles (user_id, role_id) values (1, 1);
insert into carts (id, user_id) values (1, 1);
