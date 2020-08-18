@echo off

set curdirapp=%cd%
cd %curdirapp%
start my_tools.exe sa/@@jdbc:h2:tcp://localhost/~/test C:\\Users\Administrator\\Desktop\\sql_eclipse.sql 测试excel.xlsx