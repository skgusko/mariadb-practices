--
-- Subquery
--

--
-- 1) select 절
--
select (select 1+1 from dual) from dual;
-- insert into t1 values(null, (select max(no) + 1 from t1));

--
--
-- 2) from 절의 
--
select now() as n, sysdate() as s, 3 + 1 as r from dual;
select a.n, a.r
from (select now() as n, sysdate() as s, 3 + 1 as r from dual) a;

--
-- 3) where 절의 서브쿼리
-- 

-- 예) 현재 Fai Bale이 근무하는 부서에서 근무하는 직원의 사번, 전체 이름을 출력해보세요.
select a.dept_no
from dept_emp a join employees b
on a.emp_no = b.emp_no
where concat(b.first_name, ' ', b.last_name) = 'Fai Bale'
and to_date='9999-01-01';

-- d004
select b.emp_no, b.first_name
from dept_emp a join employees b
on a.emp_no = b.emp_no
where a.to_date='9999-01-01'
and a.dept_no = (select a.dept_no
				from dept_emp a join employees b
				on a.emp_no = b.emp_no
				where concat(b.first_name, ' ', b.last_name) = 'Fai Bale'
				and to_date='9999-01-01')
;

-- 3-1) 단일행 연산자: =, >, <, >=, <=, <>, !=
-- 실습 문제 1
-- 현재 전체 사원의 평균 연봉보다 적은 급여를 받는 사원의 이름과 급여를 출력하세요.
select concat(a.first_name, ' ', a.last_name), b.salary
from employees a join salaries b
on a.emp_no=b.emp_no
where b.to_date='9999-01=01'
and b.salary < (select avg(salary)
				from salaries
                where to_date='9999-01-01')
order by b.salary desc;

-- 실습 문제 2
-- 현재 직책 별 평균 급여 중에 가장 적은 평균 급여의 직책 이름과 그 평균 급여를 출력하세요.
-- 1) 직책 별 평균 급여
select b.title, avg(a.salary)
from salaries a, titles b
where a.emp_no=b.emp_no
and a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
group by b.title;
    
-- 2) 직책 별 가장 적은 평균 급여
select min(t.avg_salary)
from (select b.title, avg(a.salary) as 'avg_salary'
	from salaries a, titles b
	where a.emp_no=b.emp_no
	and a.to_date = '9999-01-01'
	and b.to_date = '9999-01-01'
	group by b.title) t;

-- 3) sol: where절 subquery(=)
select b.title, avg(a.salary)
from salaries a, titles b
where a.emp_no=b.emp_no
and a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
group by b.title
having avg(a.salary) = (select min(t.avg_salary)
						from (select b.title, avg(a.salary) as 'avg_salary'
						from salaries a, titles b
						where a.emp_no=b.emp_no
						and a.to_date = '9999-01-01'
						and b.to_date = '9999-01-01'
						group by b.title) t);

-- 4) sol2: top-k
select b.title, avg(a.salary)
from salaries a, titles b
where a.emp_no=b.emp_no
and a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
group by b.title
order by avg(a.salary) asc
limit 1;
    
-- 3-1) 복수행 연산자: in, not in, 비교연산자any, 비교연산자ALL

-- any 사용법
-- 1. =any: in
-- 2. >any, >=any: 최솟값
-- 3. <any, <=any: 최댓값
-- 4. <>any, !=any: not in

-- all 사용법
-- 1. =all: (x)
-- 2. >all, >=all: 최대값
-- 3. <all, <=all: 최소값
-- 4. <>all, !=all

-- 실습문제 3
-- 현재 급여가 50000 이상인 직원의 이름과 급여를 출력하세요.
-- 둘리 60000
-- 마이콜 55000

-- sol01
select a.first_name, b.salary
from employees a, salaries b
where a.emp_no=b.emp_no
and b.to_date='9999-01-01'
and b.salary > 50000
order by b.salary asc;

-- sol02
select b.first_name, a.salary
from salaries a join employees b
on a.emp_no=b.emp_no
where a.to_date='9999-01-01'
and (b.emp_no, a.salary) in (select emp_no, salary
							from salaries
							where to_date='9999-01-01'
							and salary >= 50000
							)
order by a.salary asc;

-- 실습문제4:
-- 현재 각 부서 별 최고 급여를 받고 있는 직원의 이름, 부서 이름, 급여를 출력해 보세요.
-- 부서 별 최고 급여
-- sub : depart_no, max(salary)
select p.dept_no, max(s.salary) as max_salary
from salaries s, dept_emp p
where s.emp_no=p.emp_no
and s.to_date='9999-01-01'
and p.to_date='9999-01-01'
group by dept_no;

-- sol01: where in
select concat(c.first_name, ' ', c.last_name), a.dept_name, salary
from departments a, dept_emp b, employees c, salaries d
where a.dept_no=b.dept_no
and b.emp_no=c.emp_no
and c.emp_no=d.emp_no
and b.to_date='9999-01-01'
and d.to_date='9999-01-01'
and (a.dept_no, d.salary) in (select p.dept_no, max(s.salary)
							from salaries s, dept_emp p
							where s.emp_no=p.emp_no
                            and s.to_date='9999-01-01'
                            and p.to_date='9999-01-01'
							group by dept_no);

-- sol02: from절 subquery 
select concat(c.first_name, ' ', c.last_name), a.dept_name, salary
from departments a, 
		dept_emp b,
        employees c, 
        salaries d, 
			(select p.dept_no, max(s.salary) as max_salary
			from salaries s, dept_emp p
			where s.emp_no=p.emp_no
			and s.to_date='9999-01-01'
			and p.to_date='9999-01-01'
			group by dept_no) e
where a.dept_no=b.dept_no
and b.emp_no=c.emp_no
and c.emp_no=d.emp_no
and b.dept_no=e.dept_no
and b.to_date='9999-01-01'
and d.to_date='9999-01-01'
and e.max_salary=d.salary;