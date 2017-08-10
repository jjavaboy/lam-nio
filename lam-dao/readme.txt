CREATE TABLE `t_id_sequence` (
`name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`step`  int(11) UNSIGNED NOT NULL ,
`old_value`  bigint(20) UNSIGNED NOT NULL ,
`new_value`  bigint(20) UNSIGNED NOT NULL ,
`created_time`  datetime NOT NULL ,
`updated_time`  datetime NOT NULL ,
PRIMARY KEY (`name`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
COMMENT='t_id_sequence'
ROW_FORMAT=COMPACT
;