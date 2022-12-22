
create table web_user
(
  USER_NO      VARCHAR2(32) not null
    primary key,
  USER_NAME    VARCHAR2(64),
  USER_EXPLAIN CLOB
);
comment on table web_user is '用户';
comment on column web_user .USER_NO is '用户编号';
comment on column web_user .USER_NAME is '用户名';
comment on column web_user .USER_EXPLAIN is '说明';

insert into web_user(user_no, user_name, user_explain) values ('1', 'rone', '开发调试');
insert into web_user(user_no, user_name, user_explain) values ('2', 'snow', '开发调试2');
insert into web_user(user_no, user_name, user_explain) values ('3', 'fuck', '开发调试3');