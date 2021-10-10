for /F %%i in ('type pid.log') do ( taskkill -f /pid %%i)
