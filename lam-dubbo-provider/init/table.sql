/**banka*/
CREATE TABLE `t_banka_user` (
`user_id`  int(11) UNSIGNED NOT NULL ,
`money`  double UNSIGNED NOT NULL ,
`create_time`  datetime NOT NULL ,
`update_time`  datetime NOT NULL ,
PRIMARY KEY (`user_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='银行A的用户表'
ROW_FORMAT=COMPACT
;

/**bankb*/
CREATE TABLE `t_bankb_account` (
`user_id`  int(11) UNSIGNED NOT NULL ,
`money`  double UNSIGNED NOT NULL ,
`create_time`  datetime NOT NULL ,
`update_time`  datetime NOT NULL ,
PRIMARY KEY (`user_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='银行B的账号表'
ROW_FORMAT=COMPACT
;
CREATE TABLE `t_bankb_transfer` (
`message_id`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '消息id' ,
`from_user_id`  int(11) UNSIGNED NOT NULL ,
`from_brand`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`to_user_id`  int(11) UNSIGNED NOT NULL ,
`money`  double UNSIGNED NOT NULL ,
`status`  smallint(6) UNSIGNED NOT NULL ,
`create_time`  datetime NOT NULL ,
`update_time`  datetime NOT NULL ,
PRIMARY KEY (`message_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='消息消费表'
ROW_FORMAT=COMPACT
;