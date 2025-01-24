CREATE TABLE article_categories (
    article_id INT NOT NULL,
    category_id INT NOT NULL,
    FOREIGN KEY (article_id) REFERENCES articles (id),
    FOREIGN KEY (category_id) REFERENCES categories (id),
    PRIMARY KEY (article_id, category_id)
);
