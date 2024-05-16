CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price NUMERIC(10, 2) NOT NULL,
                          image_url VARCHAR(1024) NOT NULL,
                          amount INTEGER NOT NULL
);

INSERT INTO products (name, description, price, image_url, amount) VALUES ('Бежева Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/64/ee/f7/64eef756879ff2674103ccdb08ec6bcf.jpg', 2);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Пудрова Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 450, 'https://i.pinimg.com/originals/1d/42/b6/1d42b62da84a82746c6be9187dc3b6b0.jpg', 3);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Чорна Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/ab/a9/fc/aba9fc7083f872501dc084035d9e8610.jpg', 4);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Сіра Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/c2/99/77/c2997701a996e7dd321acabce44b3bbf.jpg', 4);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Голуба Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 510, 'https://i.pinimg.com/originals/09/54/c5/0954c5a48c9f86dd3102a81a208c928c.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Бордовий Берет', 'Бавовняний Берет, стильний та теплий', 520, 'https://i.pinimg.com/originals/f9/1a/0e/f91a0e7bd0e180661840b7e1d789e629.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Мʼятна Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/ea/a6/c0/eaa6c0028bcfa5b23aed107dea672417.jpg', 2);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Лілова Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 440, 'https://i.pinimg.com/originals/ef/2a/4a/ef2a4ab44e9d51ea9f3a043a60902b32.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Фіолетова Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 440, 'https://i.pinimg.com/originals/00/6a/29/006a29da3ca19c0c28a0598db92f3559.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Коричнева Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/46/53/f3/4653f3152919d45a50de41f43850eb41.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Світло-коричнева Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 490, 'https://i.pinimg.com/originals/cf/01/06/cf010642c24c843dc2eb395bf118f033.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Рожева Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/83/f6/af/83f6af66fbe46d69f332b3665bca9cf5.jpg', 5);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Темно-зелена Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 500, 'https://i.pinimg.com/originals/60/5a/6e/605a6e614fdc5990b8abf791b699b5ec.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Темно-коричнева Шапка', 'Бавовняна Шапка, яка дарує комфорт та тепло', 480, 'https://i.pinimg.com/originals/e5/73/b8/e573b893c57f91e6756c35a3896beb87.jpg', 1);
INSERT INTO products (name, description, price, image_url, amount) VALUES ('Бежевий Шарф', 'Теплий та затишний шарф, який зігріє взимку', 1400, 'https://i.pinimg.com/originals/3b/36/44/3b36443c7dc59cb12e0547781bf5bdf3.jpg', 1);
