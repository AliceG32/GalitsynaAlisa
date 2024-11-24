-- Количество пользователей, которые не создали ни одного поста.

select count(*)
from profile
         left join post on profile.profile_id = post.profile_id
where post.profile_id is null;
