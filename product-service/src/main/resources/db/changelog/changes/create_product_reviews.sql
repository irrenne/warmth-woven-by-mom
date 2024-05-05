CREATE TABLE product_reviews (
                          id BIGSERIAL PRIMARY KEY,
                          user_id VARCHAR(255) NOT NULL,
                          comment TEXT,
                          product_id bigint NOT NULL,
                          FOREIGN KEY (product_id) REFERENCES products(id)
);