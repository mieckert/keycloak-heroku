(certutil.exe -encodehex ..\\k8s-config\\okteto\\config config 1 && type config | powershell -Command "(gc config) -join ''" | clip.exe) && del config

