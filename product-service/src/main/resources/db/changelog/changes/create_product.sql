CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price NUMERIC(10, 2) NOT NULL,
                          image_url VARCHAR(1024) NOT NULL,
                          amount INTEGER NOT NULL
);

INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шапка', 'Біла бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/13/8f/e9/138fe9589bbb82d39cb27a23c50230d1.jpg', 2);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шарф', 'Теплий та затишний шарф, який зігріє взимку', 250, 'https://i.pinimg.com/originals/65/1f/17/651f17c50fa3ad3ba669b5dc1d1eb093.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шапка', 'Рожева бавовняна Шапка, яка дарує комфорт та тепло', 300, 'https://i.pinimg.com/originals/13/8f/e9/138fe9589bbb82d39cb27a23c50230d1.jpg', 3);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шарф', 'Теплий та затишний шарф, який зігріє взимку', 400, 'https://i.pinimg.com/originals/65/1f/17/651f17c50fa3ad3ba669b5dc1d1eb093.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шапка', 'Чорна бавовняна Шапка, яка дарує комфорт та тепло', 600, 'https://i.pinimg.com/originals/13/8f/e9/138fe9589bbb82d39cb27a23c50230d1.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шарф', 'Теплий та затишний шарф, який зігріє взимку', 270, 'https://i.pinimg.com/originals/65/1f/17/651f17c50fa3ad3ba669b5dc1d1eb093.jpg', 3);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шапка', 'Сіра бавовняна Шапка, яка дарує комфорт та тепло', 440, 'https://i.pinimg.com/originals/13/8f/e9/138fe9589bbb82d39cb27a23c50230d1.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шарф', 'Теплий та затишний шарф, який зігріє взимку', 800, 'https://i.pinimg.com/originals/65/1f/17/651f17c50fa3ad3ba669b5dc1d1eb093.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шапка', 'Голуба бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/13/8f/e9/138fe9589bbb82d39cb27a23c50230d1.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шарф', 'Теплий та затишний шарф, який зігріє взимку', 250, 'https://i.pinimg.com/originals/65/1f/17/651f17c50fa3ad3ba669b5dc1d1eb093.jpg', 2);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шапка', 'Бежева бавовняна Шапка, яка дарує комфорт та тепло', 300, 'https://i.pinimg.com/originals/13/8f/e9/138fe9589bbb82d39cb27a23c50230d1.jpg', 3);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шарф', 'Теплий та затишний шарф, який зігріє взимку', 400, 'https://i.pinimg.com/originals/65/1f/17/651f17c50fa3ad3ba669b5dc1d1eb093.jpg', 4);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шапка', 'Мʼятна бавовняна Шапка, яка дарує комфорт та тепло', 600, 'https://i.pinimg.com/originals/13/8f/e9/138fe9589bbb82d39cb27a23c50230d1.jpg', 5);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шарф', 'Теплий та затишний шарф, який зігріє взимку', 270, 'https://i.pinimg.com/originals/65/1f/17/651f17c50fa3ad3ba669b5dc1d1eb093.jpg', 2);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шапка', 'Лілова бавовняна Шапка, яка дарує комфорт та тепло', 440, 'https://i.pinimg.com/originals/13/8f/e9/138fe9589bbb82d39cb27a23c50230d1.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Шарф', 'Теплий та затишний шарф, який зігріє взимку', 800, 'https://i.pinimg.com/originals/65/1f/17/651f17c50fa3ad3ba669b5dc1d1eb093.jpg', 1);
