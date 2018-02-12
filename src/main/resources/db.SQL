CREATE TABLE `guestbook`.`guest_records` (
    `id` CHAR(36) NULL ,
    `first_name` VARCHAR(128) NOT NULL ,
    `last_name` VARCHAR(128) NOT NULL ,
    `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB CHARSET=utf8 COLLATE utf8_polish_ci;