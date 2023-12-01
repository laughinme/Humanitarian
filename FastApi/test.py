# import requests
# url = 'http://127.0.0.1:8000/v1/users/register'
# request = requests.post(url, params={"username" : "admin", "password" : "admin", "name" : "admin", "age" : 10, "school" : "ELNSTU"})

# print(request.url)
# print(request.text)
# print(request.status_code)

# import json
# data = json.dumps({"username": "username", "name": "name", "age": 10, "school": "school", "role": "role", "code" : "code"})
# print(data)

<<<<<<< Updated upstream
import requests
import webbrowser
from PIL import Image
from io import BytesIO
import urllib.parse
import tempfile


url = f'http://api.qrserver.com/v1/create-qr-code/?data={urllib.parse.quote("http://us.pylex.me:8677/user?code=531235")}&size=100x100'
response = requests.get(url)

if response.status_code == 200:
    img = Image.open(BytesIO(response.content))
    
    with tempfile.NamedTemporaryFile(suffix='.png', delete=False) as temp_file:
        img.save(temp_file.name, format='PNG')  # Сохраняем изображение во временном файле

    webbrowser.open('file://' + temp_file.name)  # Открываем изображение в браузере по умолчанию
else:
    print('Ошибка при создании QR-кода:', response.status_code)
=======
# import requests
# import webbrowser
# from PIL import Image
# from io import BytesIO
import urllib.parse
# import tempfile


# url = f'http://api.qrserver.com/v1/create-qr-code/?data={urllib.parse.quote("http://us.pylex.me:8677/user?code=000000")}&size=100x100'
# response = requests.get(url)

# if response.status_code == 200:
#     img = Image.open(BytesIO(response.content))
    
#     with tempfile.NamedTemporaryFile(suffix='.png', delete=False) as temp_file:
#         img.save(temp_file.name, format='PNG')  # Сохраняем изображение во временном файле

#     webbrowser.open('file://' + temp_file.name)  # Открываем изображение в браузере по умолчанию
# else:
#     print('Ошибка при создании QR-кода:', response.status_code)

link = f'http://api.qrserver.com/v1/create-qr-code/?data={urllib.parse.quote(f"http://us.pylex.me:8677/user?code={str(79691)}")}&size=100x100'
print(link)
>>>>>>> Stashed changes
