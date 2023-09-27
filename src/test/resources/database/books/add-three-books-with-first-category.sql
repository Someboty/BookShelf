insert into categories(id, name, description, is_deleted) values (1, 'science', 'some clever stuff', false);
insert into books (id, title, author, isbn, price, description, cover_image, is_deleted) values (1, 'The First Book', 'Old Doctor', '978-3-16-148410-0', 29.99, 'Something about medicine', 'red url', false);
insert into books (id, title, author, isbn, price, description, cover_image, is_deleted) values (2, 'The Second Book', 'Scientist', '978-3-16-148410-1', 9.50, 'Something about science', 'microscopic url', false);
insert into books (id, title, author, isbn, price, description, cover_image, is_deleted) values (3, 'The Third Book', 'Engineer', '978-3-16-148410-3', 19.00, 'Something about cars', 'speedy url', false);
insert into books_categories(book_id, category_id) values (1, 1);
insert into books_categories(book_id, category_id) values (2, 1);
insert into books_categories(book_id, category_id) values (3, 1);
