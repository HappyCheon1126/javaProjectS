show tables;

create table schedule2 (
	idx		int 			not null auto_increment primary key,
	mid 	varchar(20) not null,
	sDate datetime	not null,			/* 일정 등록한 날짜 */
	part	varchar(20) not null,		/* 1.모임, 2.업무, 3.학습, 4.여행, 0:기타 */
	content text not null					/* 일정 상세 내역 */
);

desc schedule2;

insert into schedule2 values (default, 'hkd1234','2024-01-04', '학습', '프로젝트 설계서 제안..');
insert into schedule2 values (default, 'hkd1234','2024-01-11', '학습', 'DB설계서 완성하기');
insert into schedule2 values (default, 'hkd1234','2024-01-11', '업무', '15시까지 하반기 계획서 제출하기');
insert into schedule2 values (default, 'hkd1234','2024-01-12', '학습', '프로젝트 계획서 제출');
insert into schedule2 values (default, 'kms1234','2024-01-13', '학습', '프로젝트 시작~~');
insert into schedule2 values (default, 'kms1234','2024-01-16', '업무', '업무일지 정리');
insert into schedule2 values (default, 'kms1234','2024-01-18', '기타', '등산장비 정리');
insert into schedule2 values (default, 'hkd1234','2024-01-22', '학습', '프로젝트 중간점검');
insert into schedule2 values (default, 'hkd1234','2024-02-03', '모임', '가족회의, 장소:집, 시간:19시');
insert into schedule2 values (default, 'hkd1234','2024-02-13', '모임', '초등 동창회, 장소:충북대4거리 커피숍, 시간:13시');
insert into schedule2 values (default, 'kms1234','2024-02-13', '기타', '집안 청소하기');
insert into schedule2 values (default, 'hkd1234','2024-02-19', '기타', '온라인 교육서 제출');
insert into schedule2 values (default, 'hkd1234','2024-02-21', '모임', '동창회, 장소:사창사거리, 시간:18시');
insert into schedule2 values (default, 'kms1234','2024-02-23', '업무', '년간일정 회의');
insert into schedule2 values (default, 'hkd1234','2024-02-23', '업무', '년간일정 회의');

select * from schedule2;

select * from schedule2 where mid='hkd1234' order by sDate;
select * from schedule2 where mid='hkd1234' and sDate='2024-2' order by sDate;  /* X */
select * from schedule2 where mid='hkd1234' and sDate='2024-02' order by sDate; /* X */
select * from schedule2 where mid='hkd1234' and sDate='2024-02-13' order by sDate;
select * from schedule2 where mid='hkd1234' and substring(sDate,1,7)='2024-02' order by sDate;
select * from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m')='2024-02' order by sDate;
select * from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m')='2024-02' group by substring(sDate,1,7) order by sDate;
select sDate,count(*) from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m')='2024-02' group by substring(sDate,1,7) order by sDate;
select sDate,part from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m')='2024-02' order by sDate, part;

select * from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m-%d')='2024-02-13';

select * from schedule2 group by date_format(sDate, '%Y-%m')='2024-01' order by sDate, part;  -- (x)
select *,count(*) from schedule2 group by date_format(sDate, '%Y-%m')='2024-01' order by sDate, part;  -- (x)

select * from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m')='2024-01' group by sDate order by sDate, part;
select *,count(*) from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m')='2024-01' group by sDate order by sDate, part;
select *,count(*) from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m')='2024-01' group by part  order by sDate, part;

select *,count(*) as partCnt from schedule2 where mid='hkd1234' and date_format(sDate, '%Y-%m')='2024-01' group by sDate,part order by sDate, part;
select *,count(*) as partCnt from schedule2 where date_format(sDate, '%Y-%m')='2024-01' group by sDate,part order by sDate, part;

select *, count(part) as partCnt from schedule2 where mid = 'hkd1234' and date_format(sDate, '%Y-%m') = '2024-01' group by sDate,part order by sDate, part;