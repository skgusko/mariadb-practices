--
-- inner join
--

-- 예제 1: 현재, 근무하고 있는 직원의 이름과 직책을 모두 출력하세요.
select concat(first_name, ' ', last_name) as name, e.gender, t.title
from employees e, titles t
where e.emp_no = t.emp_no			-- join 조건(n-1)
and t.to_date = '9999-01-01';		-- row 선택 조건

-- 예제 2: 현재, 근무하고 있는 직원의 이름과 직책을 모두 출력하되, 여성 엔지니어(Engineer)만 출력하세요.
select concat(first_name, ' ', last_name) as name, e.gender, t.title
from employees e, titles t
where e.emp_no = t.emp_no			-- join 조건(n-1)
and t.to_date = '9999-01-01'		-- row 선택 조건
and e.gender = 'F';

--
-- ANSI / ISO SQL1999 JOIN 표준 문법
--

-- 1) natural join
-- 조인 대상이 되는 두 테이블에 이름이 같은 공통 컬럼이 있는 경우
-- 조인 조건을 명시하지 않고 암묵적으로 조인이 된다.
select concat(first_name, ' ', last_name) as name, e.gender, t.title
from employees e natural join titles t
where t.to_date = '9999-01-01';		-- row 선택 조건

-- 2) join ~ using
-- natural join의 문제점 (의도하지 않은 애들까지 같은 이름이라는 이유로 join이 됨)
select count(*)
from salaries a natural join titles b
where a.to_date = '9999-01-01'
and b.to_date = '9999-01-01';

select count(*)
from salaries a join titles b using(emp_no)
where a.to_date = '9999-01-01'
and b.to_date = '9999-01-01';

-- 3) join ~ on
select count(*)
from salaries a join titles b on a.emp_no = b.emp_no
where a.to_date = '9999-01-01'
and b.to_date = '9999-01-01';

-- 실습 문제 1. 현재 직책 별 평균 연봉을 큰 순서대로 출력하세요
select a.title, avg(salary) as avg_salary
from titles a join salaries b
on a.emp_no = b.emp_no
where a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
group by a.title
order by avg_salary desc;

-- 실습 문제 2. 현재 직책 별 평균 연봉과 직원 수를 구하되 직원 수가 100명 이상인 직책만 출력
select b.title, avg(salary) as 'avg_salary', count(*) as 'employee_cnt'
from salaries a join titles b
on a.emp_no = b.emp_no
where a.to_date = '9999-01-01'
and b.to_date = '9999-01-01'
group by b.title
having count(*) >= 100
order by avg_salary desc;

-- 실습 문제 3. 현재 부서 별로 직책이 Engineer인 직원들에 대한 평균 연봉을 구하세요.
-- projection: 부서이름 평균연봉
select a.dept_name, avg(c.salary)
from departments a, dept_emp b, salaries c, titles d
where a.dept_no = b.dept_no
and b.emp_no = c.emp_no
and c.emp_no = d.emp_no
and b.to_date = '9999-01-01'
and c.to_date = '9999-01-01'
and d.to_date = '9999-01-01'
and d.title='Engineer'
group by a.dept_name
order by avg(c.salary) desc;