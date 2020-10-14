/*
    测试 SQL
*/
select *
from course;

# 根据表名获取表的注释信息
select table_comment
from information_schema.TABLES
where TABLE_NAME = 'chapter';

# 获取表的所有字段信息
show full columns from chapter;