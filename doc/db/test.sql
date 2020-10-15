/*
    测试 SQL
*/
select *
from course_category;

# 根据表名获取表的注释信息
select table_comment
from information_schema.TABLES
where TABLE_NAME = 'chapter';

# 获取表的所有字段信息
show full columns from chapter;

# 将小节的课程时长累加到课程的总时长上面
update course c
set time = (select sum(time) from section where course_id = '00000001')
where c.id = '00000001';