CREATE TABLE  stadium_tb(
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(255) NOT NULL,
                            created_At TIMESTAMP,
                            UNIQUE INDEX idx_name (name)
);


CREATE TABLE team_tb (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         stadium_id INT NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         created_At TIMESTAMP,
                         FOREIGN KEY (stadium_id) REFERENCES stadium_tb (id),
                         UNIQUE INDEX idx_name (name)
);



CREATE TABLE player_tb (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           team_id INT,
                           name VARCHAR(255),
                           position VARCHAR(255),
                           created_at TIMESTAMP,
                           UNIQUE INDEX idx_team_position (team_id, position),
                           FOREIGN KEY (team_id) REFERENCES team_tb(id)
);

CREATE TABLE out_player_tb (
                               id INT PRIMARY KEY AUTO_INCREMENT,
                               player_id INT NOT NULL,
                               reason VARCHAR(255),
                               created_at TIMESTAMP,
                               FOREIGN KEY (player_id) REFERENCES player_tb (id)
);