-- Выберите по возрастанию ID первых 10 постов, у которых либо нет комментариев, либо он один.

with comment_count as (
    select p.post_id
    from post p
             inner join comment c on p.post_id = c.post_id
    group by p.post_id
    having count(c.comment_id) = 1

    union

    select post.post_id
    from post
             left join comment c on post.post_id = c.post_id
    where c.comment_id is null
)

select comment_count.post_id
from comment_count
order by post_id limit 10;