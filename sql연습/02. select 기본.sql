--
-- select 기본
--

-- 예제1. departments 테이블의 모든 컬럼 출력 
select * from departments;

-- projection
-- 예제 2. employees 테이블에서 직원의 전체이름, 성별, 입사일을 입사일 순으로 출력
select first_name, gender, hire_date
from employees;

-- as(alias, 생략 가능)
-- 예제 3. employees 테이블에서 직원의 전체이름, 성별, 입사일을 입사일 순으로 출력
select concat(first_name, ' ', last_name) as '이름', 
	   gender as '성별',
       hire_date as '입사일'
from employees;

-- distinct 
-- 예제 4. 직급 이름을 한 번씩만 출력하기
select title
from titles;

select distinct(title)
from titles;

-- where

-- 비교연산자
-- 예제 5. employees 테이블에서 1991년 이전에 입사한 직원의 이름, 성별, 입사일을 출력
select first_name,
	   gender,
       hire_date
from employees
where hire_date < '1991-01-01';

-- 논리연산자
-- 예제 6. employees 테이블에서 1991년 이전에 입사한 여직원의 이름, 성별, 입사일을 출력
select first_name,
	   gender,
       hire_date
from employees
where hire_date < '1991-01-01'
and gender='F';


-- in 연산자
-- 예제 7. dept_emp 테이블에서 부서 번호가 d005나 d009에 속한 사원의 사번, 부서번호 출력
select emp_no, dept_no
from dept_emp
where dept_no in('d005', 'd009');

-- like 검색
-- 예제 8. employees 테이블에서 1989년에 입사한 직원의 이름, 입사일을 출력
select first_name, hire_date
from employees
where hire_date like '1989-%';

--
-- order by
--

-- 예제 9. employees 테이블에서 직원의 이름, 성별, 입사일을 입사일 순으로 출력
select first_name, gender, hire_date
from employees
order by hire_date asc;

-- 예제 10. salaries 테이블에서 2001년 월급이 가장 높은순으로 사번, 월급순을 출력
select emp_no, salary
from salaries
where from_date like '2001-%'
and to_date like '2001-%'
order by salary desc;

-- 예제 11. 직원의 사번과 월급을 사번(asc), 월급(desc) 순으로 출력
select emp_no, salary
from salaries
order by emp_no asc, salary desc;