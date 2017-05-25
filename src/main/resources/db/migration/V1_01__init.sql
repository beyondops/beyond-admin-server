# CREATE DATABASE `beyondops` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
# GRANT ALL PRIVILEGES ON beyondops.* TO beyondops@localhost
# IDENTIFIED BY 'beyondops-dev';
# FLUSH PRIVILEGES;


SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`           INT          NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `username`     VARCHAR(50)  NOT NULL
  COMMENT 'Username',
  `password`     VARCHAR(100) NOT NULL
  COMMENT 'Password',
  `fullname`     VARCHAR(50)  NOT NULL
  COMMENT 'Full name',
  `gender`       INT                   DEFAULT NULL
  COMMENT 'gender,0:man,1:female',
  `auth_type`    INT          NOT NULL DEFAULT 1
  COMMENT 'Authentication Type, 1: Local password，2: LDAP',
  `email`        VARCHAR(50)  NOT NULL DEFAULT ''
  COMMENT 'Email',
  `phone_number` VARCHAR(50)           DEFAULT NULL
  COMMENT 'Phone number',
  `status`       INT          NOT NULL DEFAULT FALSE
  COMMENT 'User status, 0: Invalid, 1: Valid',
  `last_login`   DATETIME              DEFAULT NULL
  COMMENT 'Last login time',
  `remark`       VARCHAR(200)          DEFAULT NULL
  COMMENT 'User remark',
  `created_at`   DATETIME     NOT NULL
  COMMENT 'Create time',
  `updated_at`   DATETIME     NOT NULL
  COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`username`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System user info';

DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api` (
  `id`         INT          NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `api_name`   VARCHAR(50)  NOT NULL
  COMMENT 'Api name',
  `api_url`    VARCHAR(100) NOT NULL
  COMMENT 'Api url',
  `api_method` INT          NOT NULL
  COMMENT 'Api method, 1:POST, 2:GET, 3:PUT, 4:PATCH, 5:DELETE',
  `api_config` VARCHAR(200)          DEFAULT NULL
  COMMENT 'Api config',
  `created_at` DATETIME     NOT NULL
  COMMENT 'Create time',
  `updated_at` DATETIME     NOT NULL
  COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`api_url`, `api_method`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System api info';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id`          INT         NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `role_name`   VARCHAR(50) NOT NULL
  COMMENT 'Role name',
  `role_config` VARCHAR(200)         DEFAULT NULL
  COMMENT 'Role configuration',
  `created_at`  DATETIME    NOT NULL
  COMMENT 'Create time',
  `updated_at`  DATETIME    NOT NULL
  COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`role_name`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System role info';

DROP TABLE IF EXISTS `sys_role_api`;
CREATE TABLE `sys_role_api` (
  `id`         INT      NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `api_id`     INT      NOT NULL
  COMMENT 'Api id',
  `role_id`    INT      NOT NULL
  COMMENT 'Role id',
  `created_at` DATETIME NOT NULL
  COMMENT 'Create time',
  `updated_at` DATETIME NOT NULL
  COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`role_id`, `api_id`),
  FOREIGN KEY (`api_id`) REFERENCES `sys_api` (`id`)
    ON DELETE CASCADE,
  FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System role api info';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id`         INT      NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `user_id`    INT      NOT NULL
  COMMENT 'User id',
  `role_id`    INT      NOT NULL
  COMMENT 'Role id',
  `created_at` DATETIME NOT NULL
  COMMENT 'Create time',
  `updated_at` DATETIME NOT NULL
  COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`user_id`, `role_id`),
  KEY (`role_id`),
  FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`)
    ON DELETE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System user role info';

DROP TABLE IF EXISTS `sys_selection`;
CREATE TABLE `sys_selection` (
  `id`         INT          NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `name`       VARCHAR(100) NOT NULL
  COMMENT 'Name',
  `value`      VARCHAR(50)  NOT NULL
  COMMENT 'Value',
  `text`       VARCHAR(200) NOT NULL
  COMMENT 'Text',
  `remark`     VARCHAR(200)          DEFAULT NULL
  COMMENT 'Remark',
  `created_at` DATETIME     NOT NULL
  COMMENT 'Create time',
  `updated_at` DATETIME     NOT NULL
  COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`, `value`, `text`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System selection configuration';

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id`           INT          NOT NULL AUTO_INCREMENT,
  `config_key`   VARCHAR(100) NOT NULL
  COMMENT 'Config key',
  `config_value` TEXT         NOT NULL
  COMMENT 'Config value',
  `remark`       VARCHAR(100) NOT NULL
  COMMENT 'Config remark',
  `created_at`   DATETIME     NOT NULL
  COMMENT 'Create time',
  `updated_at`   DATETIME     NOT NULL
  COMMENT 'Update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`config_key`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System configuration';

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id`            INT         NOT NULL AUTO_INCREMENT,
  `user_id`       INT         NOT NULL
  COMMENT 'User id',
  `log_time`      DATETIME    NOT NULL
  COMMENT 'Log time',
  `log_module_id` INT         NOT NULL
  COMMENT 'Log module object id',
  `log_type`      TINYINT     NOT NULL
  COMMENT 'Log type(1:create, 2:retrieve, 3:update, 4:delete)',
  `log_level`     TINYINT     NOT NULL
  COMMENT 'Log level(0:Fatal, 1:Error, 2:Warn, 3:Info, 4:Debug, 5:Trace)',
  `log_ip`        VARCHAR(20)          DEFAULT NULL
  COMMENT 'User ip',
  `log_module`    VARCHAR(50) NOT NULL
  COMMENT 'Log module class name',
  `log_info`      VARCHAR(250)         DEFAULT NULL
  COMMENT 'Log info',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `log_time` (`log_time`),
  KEY `log_module` (`log_module`),
  KEY `log_level` (`log_level`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System log';


DROP TABLE IF EXISTS `sys_inbox`;
CREATE TABLE `sys_inbox` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `type`       TINYINT      NOT NULL
  COMMENT '0:Notice, 1: Warning, 2: Error',
  `read`       BOOLEAN      NOT NULL DEFAULT FALSE
  COMMENT 'Read message or not',
  `from_uid`   INT          NOT NULL
  COMMENT 'From user',
  `from_name`  VARCHAR(50)  NOT NULL
  COMMENT 'From username',
  `to_uid`     INT          NOT NULL
  COMMENT 'From user id',
  `content`    VARCHAR(500) NOT NULL
  COMMENT 'Message content',
  `created_at` DATETIME     NOT NULL
  COMMENT 'Create time',
  `updated_at` DATETIME     NOT NULL
  COMMENT 'Update time',

  PRIMARY KEY (`id`),
  KEY (`to_uid`),
  FOREIGN KEY (`to_uid`) REFERENCES `sys_user` (`id`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System inbox';

DROP TABLE IF EXISTS `sys_application`;
CREATE TABLE `sys_application` (
  `id`         INT          NOT NULL AUTO_INCREMENT,
  `user_id`    INT          NOT NULL
  COMMENT 'User id',
  `app_name`   VARCHAR(100) NOT NULL
  COMMENT 'Application name',
  `token`      VARCHAR(500) NOT NULL
  COMMENT 'Application security token',
  `expired_at` DATETIME              DEFAULT NULL
  COMMENT 'Expired time',
  `created_at` DATETIME     NOT NULL
  COMMENT 'Create time',
  PRIMARY KEY (`id`),
  KEY (`app_name`),
  FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'System application info';


SET FOREIGN_KEY_CHECKS = 1;
