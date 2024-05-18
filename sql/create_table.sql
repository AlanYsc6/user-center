-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '用户主键id，自增'
        primary key,
    username     varchar(256) charset utf8mb4       null comment '昵称',
    userAccount  varchar(256) collate utf8mb4_bin   not null comment '用户名',
    avatarUrl    varchar(1024) charset utf8mb4      null comment '头像',
    gender       tinyint                            null comment '性别，0女，1男',
    userPassword varchar(256) charset utf8mb4       not null comment '密码，非空',
    phone        varchar(128) charset utf8mb4       null comment '电话',
    email        varchar(512) charset utf8mb4       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态，默认0：正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '逻辑删除，默认0：否',
    userRole     int      default 0                 not null comment '用户角色：0普通用户，1管理员',
    planetCode   varchar(128)                       null comment '星球编号'
)
    comment '用户表' collate = utf8mb4_general_ci;

