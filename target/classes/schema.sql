    CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
    );

    CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL
    );

    CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    year_published INT,
    category_id INT,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
    );

    INSERT INTO users (username, password, role)
    SELECT 'admin', '$2a$10$vI8757zS7O5YV.pIeT1R5.Z6A5.N8GqS9N6zR1G1zR1G1zR1G1zR1', 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');