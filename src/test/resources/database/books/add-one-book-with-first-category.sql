insert into categories(id, name, description, is_deleted) values (1, 'drama', 'some sad stuff', false);
insert into books (id, title, author, isbn, price, description, cover_image, is_deleted) values (1, 'The Book', 'Modest Author', '978-3-16-148410-0', 19.95, 'Annotation', 'scary url', false);
insert into books_categories(book_id, category_id) values (1, 1);
