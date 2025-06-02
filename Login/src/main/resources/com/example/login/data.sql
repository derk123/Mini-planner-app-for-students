CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       city VARCHAR(255),
                       email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       user_id INTEGER NOT NULL,
                       description TEXT NOT NULL,
                       date DATE NOT NULL,
                       completed BOOLEAN DEFAULT FALSE,
                       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
