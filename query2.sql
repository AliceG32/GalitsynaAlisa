-- Выберите по возрастанию ID всех постов, у которых 2 комментария,
-- title начинается с цифры, а длина content больше 20 (все три условия должны соблюдаться одновременно).

with comments_count as (
    select post.post_id
    from post
             left join comment on post.post_id = comment.post_id
    group by post.post_id
    having count(comment.comment_id) = 2
)

select post.post_id
from post,
     comments_count
where char_length(post.content) > 20
  and substr(post.title, 1, 1) BETWEEN '0' AND '9'
  and post.post_id in (comments_count.post_id)
order by post.post_id
