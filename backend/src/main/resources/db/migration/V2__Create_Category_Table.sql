CREATE TABLE categories(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    category VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL

)
