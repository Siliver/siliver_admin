create table t_user
(
    id                    int auto_increment comment '主键'
        primary key,
    create_time           timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    create_user           varchar(50)                         not null comment '创建人名称',
    update_time           timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user           varchar(50)                         not null comment '更新人名称',
    username              varchar(50)                         not null comment '用户名称',
    password              varchar(100)                        not null comment '用户密码',
    version               int       default 0                 not null comment '乐观锁',
    delete_flag           tinyint   default 0                 not null comment '用户可用状态(删除状态：0，未删除；1.已删除)',
    credentialsNonExpired tinyint   default 1                 not null comment '凭证是否过期',
    accountNonLocked      tinyint   default 1                 not null comment '账号是否被锁定',
    accountNonExpired     tinyint   default 1                 not null comment '账号是否过期',
    lock_count            int       default 5                 not null comment '要锁定的次数；初始次数为5次',
    unlock_time           timestamp                           null comment '自动解锁时间',
    last_login_time       timestamp default CURRENT_TIMESTAMP not null comment '最后一次登录时间',
    constraint username
        unique (username)
)
    comment '用户表';

create table t_user_extend
(
    id          int auto_increment comment '主键'
        primary key,
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    nick_name   varchar(50)                         null comment '用户昵称',
    sex         int       default 3                 not null comment '用户性别：1 男；2 女；3 未知',
    signature   varchar(255)                        null comment '用户签名',
    user_id     int                                 not null comment '用户ID',
    constraint t_user_extend_user_id_uindex
        unique (user_id)
)
    comment '用户扩展表';

create table t_group
(
    id          int auto_increment comment '主键'
        primary key,
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    create_user varchar(50)                         not null comment '创建人',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user varchar(50)                         not null comment '更新人',
    version     int       default 0                 not null comment '乐观锁',
    delete_flag int       default 0                 not null comment '删除标识',
    group_code  varchar(36)                         not null comment '组编号',
    group_name  varchar(50)                         not null comment '组名称',
    super_id    int       default 0                 not null comment '父级组编号，根节点为0',
    constraint t_group_code_index
        unique (group_code) comment '用户编号唯一索引',
    constraint t_group_super_index
        unique (super_id) comment '父级索引'
)
    comment '用户组（用户部门）';

create table t_user_group
(
    id          int auto_increment comment '主键'
        primary key,
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    create_user varchar(50)                         not null comment '创建人',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user varchar(50)                         not null comment '更新人',
    version     int       default 0                 not null comment '乐观锁',
    delete_flag int       default 0                 not null comment '删除标识',
    user_id     int                                 not null comment '用户ID',
    group_id    int                                 not null comment '组ID'
)
    comment '用户与组关系表';

create index t_user_group_group_index
    on t_user_group (group_id)
    comment '组索引';

create index t_user_group_user_index
    on t_user_group (user_id)
    comment '用户索引';

create table t_role
(
    id          int auto_increment comment '主键'
        primary key,
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    create_user varchar(50)                         not null comment '创建人',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user varchar(50)                         not null comment '更新人',
    version     int       default 0                 not null comment '乐观锁',
    delete_flag int       default 0                 not null comment '删除标识',
    role_code   varchar(36)                         not null comment '角色编号',
    role_name   varchar(50)                         not null comment '角色名称',
    constraint t_role_role_code_uindex
        unique (role_code) comment '角色编号索引'
)
    comment '角色表';

create table t_group_role
(
    id          int auto_increment comment '主键'
        primary key,
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    create_user varchar(50)                         not null comment '创建人',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user varchar(50)                         not null comment '更新人',
    version     int       default 0                 not null comment '乐观锁',
    delete_flag int       default 0                 not null comment '删除标识',
    group_id    int                                 not null comment '组ID',
    role_id     int                                 not null comment '角色ID',
    constraint t_user_role_index
        unique (group_id, role_id) comment '用户角色索引'
)
    comment '用户组角色表';

create table t_menu
(
    id          int auto_increment comment '主键'
        primary key,
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建事件',
    create_user varchar(36)                         not null comment '创建人',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user varchar(36)                         not null comment '更新人',
    version     int       default 0                 not null comment '乐观锁',
    delete_flag int       default 0                 not null comment '删除标识',
    menu_name   varchar(50)                         not null comment '菜单名称',
    parent_id   int       default 0                 not null comment '上级菜单ID',
    menu_icon   varchar(50)                         null comment '菜单icon的名称',
    menu_url varchar(255) null comment '菜单地址'
)
    comment '菜单表';

create index t_menu_parent_index
    on t_menu (parent_id)
    comment '父级菜单索引';

create table t_role_menu
(
    id          int auto_increment comment '主键'
        primary key,
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间·',
    create_user varchar(36)                         not null comment '创建人',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user varchar(36)                         not null comment '更新人',
    version     int       default 0                 not null comment '乐观锁',
    delete_flag int       default 0                 not null comment '删除标识·',
    role_id     int                                 not null comment '角色ID',
    menu_id     int                                 not null comment '菜单ID'
)
    comment '角色菜单表对应';

create table t_dictionary
(
    id               int auto_increment comment '主键'
        primary key,
    create_time      timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    create_user      varchar(36)                         not null comment '创建人',
    update_time      timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    update_user      varchar(36)                         not null comment '更新人',
    version          int       default 0                 not null comment 'mybatisplus乐观锁',
    delete_flag      int       default 0                 not null comment '删除标识：0未删除；1已删除',
    dictionary_group varchar(25)                         not null comment '字典组编号',
    dictionary_code  varchar(25)                         not null comment '字典编号',
    dictionary_name  varchar(25)                         not null comment '字典名称',
    show_flag        int       default 0                 not null comment '展示标识 0：全部；1. 用户端；2管理端；'
)
    comment '字典表';