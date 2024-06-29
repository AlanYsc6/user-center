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


-- 标签表（可以不创建，因为标签字段已经放到了用户表中）
create table tag
(
    id         bigint auto_increment comment 'id'
        primary key,
    tagName    varchar(256) null comment '标签名称',
    userId     bigint null comment '用户 id',
    parentId   bigint null comment '父标签 id',
    isParent   tinyint null comment '0 - 不是, 1 - 父标签',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete   tinyint  default 0 not null comment '是否删除',
    constraint uniIdx_tagName
        unique (tagName)
) comment '标签';

create index idx_userId
    on tag (userId);