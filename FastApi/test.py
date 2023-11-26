import requests
url = 'http://127.0.0.1:8000/users/register'
request = requests.post(url, params={"username" : "admin", "password" : "admin", "name" : "admin", "age" : 10, "school" : "ELNSTU"})

print(request.url)
print(request.text)
print(request.status_code)