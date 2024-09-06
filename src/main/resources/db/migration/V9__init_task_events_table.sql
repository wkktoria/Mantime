drop table if exists task_events;
create table task_events
(
    id         bigint primary key auto_increment,
    task_id    bigint,
    occurrence datetime,
    name       varchar(30)
);