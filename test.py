import requests
url = 'http://127.0.0.1:8000/users/register'
request = requests.post(url, params={"name" : "negro", "ochkis" : "egor"})

print(request.url)
print(request.text)
print(request.json())