CREATE TABLE IF NOT EXISTS ADMIN (id BIGINT, name varchar(65535));
CREATE TABLE IF NOT EXISTS MANAGER (id BIGINT, name varchar(65535));
CREATE TABLE IF NOT EXISTS SPEAKER (id BIGINT, name varchar(65535));
CREATE TABLE IF NOT EXISTS EVENT (id BIGINT, name varchar(65535), managerId BIGINT, FOREIGN KEY(managerId) references MANAGER(id));
CREATE TABLE IF NOT EXISTS ZONE (id BIGINT, name varchar(65535), dateStart INT, dateEnd INT, status BOOLEAN, eventId BIGINT, FOREIGN KEY (eventId) references EVENT(id) ON DELETE CASCADE);
CREATE TABLE IF NOT EXISTS CHANNEL (id BIGINT, name varchar(65535), status BOOLEAN, language varchar(65535), zoneId BIGINT, FOREIGN KEY (zoneId) references ZONE(id) ON DELETE CASCADE );