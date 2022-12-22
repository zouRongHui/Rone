-- auto-generated definition
create table web_user
(
    user_no      varchar(32) not null comment '用户编号'
        primary key,
    user_name    varchar(64) null comment '用户名',
    user_explain text        null comment '说明'
)
    comment '用户';

