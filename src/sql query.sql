use blog;

select * from users;
select * from articles;
select * from tags;
select * from article_tag_map;

select * from article_tag_map AT left join tags T on AT.tag_id = T.tag_id
left join articles A on AT.article_id = A.article_id
where  T.slug = 'mvc' or A.header like '%Testing%' ;

select article_id  from article_tag_map AT where AT.tag_id = 1 order by article_id desc limit 1,1;



select * from tags where slug = 'spring';
select * from article_tag_map where tag_id = 2;
select * from Articles where article_id in (1, 4, 13, 14, 12);
select * from Articles where header like '%testing%';