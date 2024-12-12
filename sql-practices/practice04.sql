-- 서브쿼리(SUBQUERY) SQL 문제입니다.

-- 단 조회결과는 급여의 내림차순으로 정렬되어 나타나야 합니다. 

-- 문제1.
-- 현재 전체 사원의 평균 급여보다 많은 급여를 받는 사원은 몇 명이나 있습니까?
select count(*)
from salaries b
where b.salary > (select avg(a.salary) as 'avg_salary'
				from salaries a
				where a.to_date = '9999-01-01')
and b.to_date = '9999-01-01';


-- 문제2.
-- 현재, 각 부서별로 최고의 급여를 받는 사원의 사번, 이름, 부서 급여을 조회하세요. 단 조회결과는 급여의 내림차순으로 정렬합니다.
-- 각 부서별로 최고의 급여
select a.emp_no, concat(a.first_name, ' ', a.last_name) as name, c.salary
from employees a, dept_emp b, salaries c
where a.emp_no = b.emp_no
and b.emp_no = c.emp_no
and (b.dept_no, c.salary) in (select dept_no, max(a.salary)
							from salaries a, dept_emp b
							where a.emp_no = b.emp_no
							and a.to_date = '9999-01-01'
                            and b.to_date = '9999-01-01'
							group by b.dept_no)
and b.to_date = '9999-01-01'
and c.to_date = '9999-01-01'
order by c.salary desc;


-- 문제3. (다시풀기)
-- 현재, 사원 자신들의 부서의 평균급여보다 급여가 많은 사원들의 사번, 이름 그리고 급여를 조회하세요 
-- 부서의 평균급여
SELECT a.emp_no, CONCAT(a.first_name, ' ', a.last_name), b.salary
FROM employees a,
	salaries b,
    dept_emp c,
    (SELECT c.dept_no, AVG(b.salary) AS avg_salary
    FROM employees a, salaries b, dept_emp c
    WHERE a.emp_no = b.emp_no
	AND a.emp_no = c.emp_no
    AND b.to_date = '9999-01-01'
    AND c.to_date = '9999-01-01'
    GROUP BY c.dept_no) d
WHERE a.emp_no = b.emp_no
AND a.emp_no = c.emp_no
AND c.dept_no = d.dept_no
AND b.salary > d.avg_salary
AND b.to_date = '9999-01-01'
AND c.to_date = '9999-01-01';

        
-- 문제4. (다시풀기)
-- 현재, 사원들의 사번, 이름, 그리고 매니저 이름과 부서 이름을 출력해 보세요.
SELECT a.emp_no, CONCAT(a.first_name, ' ', a.last_name) as name, CONCAT(d.first_name, ' ', d.last_name) as manager_name, e.dept_name
FROM employees a, dept_emp b, dept_manager c, employees d, departments e
WHERE a.emp_no = b.emp_no
AND b.dept_no = c.dept_no
AND d.emp_no = d.emp_no
AND c.dept_no = e.dept_no
AND b.to_date = '9999-01-01'
AND c.to_date = '9999-01-01';


-- 문제5.
-- 현재, 평균급여가 가장 높은 부서의 사원들의 사번, 이름, 직책 그리고 급여를 조회하고 급여 순으로 출력하세요.
select c.emp_no, concat(c.first_name, ' ', c.last_name) as name, d.title, b.salary
from dept_emp a, salaries b, employees c, titles d
where a.emp_no = b.emp_no
and b.emp_no = c.emp_no
and c.emp_no = d.emp_no
and a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
and d.to_date = '9999-01-01'
and a.dept_no = (
		select p.dept_no
		from salaries s, dept_emp p
		where s.emp_no = p.emp_no
		and s.to_date = '9999-01-01'
		and p.to_date = '9999-01-01'
		group by p.dept_no
		order by avg(s.salary) desc
		limit 1)
order by salary desc;
;


-- 문제6.
-- 현재, 평균 급여가 가장 높은 부서의 이름 그리고 평균급여를 출력하세요.
select dept_name, avg(salary)
from departments d, dept_emp p, salaries s
where d.dept_no = p.dept_no
and p.emp_no = s.emp_no
and p.to_date = '9999-01-01'
and s.to_date = '9999-01-01'
and p.dept_no = (
			select b.dept_no
			from salaries a, dept_emp b
			where a.emp_no = b.emp_no
			and a.to_date = '9999-01-01'
			and b.to_date = '9999-01-01'
			order by avg(salary) desc
			limit 1
            );


-- 문제7.
-- 현재, 평균 급여가 가장 높은 직책의 타이틀 그리고 평균급여를 출력하세요.
select b.title, avg(a.salary) as 'avg_salary'
from salaries a, titles b
where a.emp_no = b.emp_no
and a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
group by title
having title = (
				select t.title
				from salaries s, titles t
				where s.emp_no = t.emp_no
				and s.to_date = '9999-01-01'
				and t.to_date = '9999-01-01'
				order by avg(salary) desc
				limit 1
                );

