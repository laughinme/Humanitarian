from fastapi import FastAPI, Response, status, Request
from fastapi.responses import JSONResponse, HTMLResponse
from starlette.responses import RedirectResponse
import json
import urllib.parse

# from pydantic import BaseModel
import sqlite3
import random
import string
# from typing import Optional
import uvicorn


app = FastAPI(
    title='Full NEGROID'
)


# @app.get("/")
# async def root():
#     return {"message": "Hello World"}


def generate_code():
    count = 1
    with sqlite3.connect('FastApi/users.db') as conn:
        cur = conn.cursor()
        while count > 0:
            code = ''.join(random.choices(string.digits, k=6))
            cur.execute("SELECT COUNT(*) FROM users WHERE code = ?", (code,))
            count = cur.fetchone()[0]
        return code
    
@app.post("/v1/users/register")
async def register(username: str, password: str, age: str=None, school: str=None, name: str=None):
    with sqlite3.connect('FastApi/users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'SELECT COUNT(*) FROM users WHERE username = "{username}"')
        exist = cur.fetchone()[0]
        if not exist:
            code = generate_code()
            # age = int(age)
            cur.execute(f'INSERT INTO users (username, name, age, school, password, code) VALUES (?, ?, ?, ?, ?, ?)', (username, name, int(age) if age else None, school, password, code))
            conn.commit()
            return {"message": "Успешная регистрация!", "code": code, "role": "user"}
        
        else:
            return JSONResponse(status_code=status.HTTP_302_FOUND, content={"message": "Такой пользователь существует!", "username" : username})
    

@app.post("/v1/users/login")
async def login(username: str, password: str):
    with sqlite3.connect('FastApi/users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'SELECT password FROM users WHERE username = "{username}"')
        dbpassword = cur.fetchone()
        if not dbpassword:
            return JSONResponse(status_code=status.HTTP_404_NOT_FOUND, content={"message": "Такого пользователя не существует!"})
        else:
            dbpassword = dbpassword[0]
            if dbpassword == password:
                cur.execute('SELECT name, age, school, role, code FROM users WHERE username = ?', (username,))
                name, age, school, role, code = cur.fetchone()
                link = f'http://api.qrserver.com/v1/create-qr-code/?data={urllib.parse.quote(f"http://us.pylex.me:8677/user?code={str(code)}")}&size=100x100'
                return {"username" : username, "password" : password, "name" : name, "age" : age, "school" : school, "code": code, "role": role, "link" : link}
            
            elif dbpassword != password:
                return JSONResponse(status_code=status.HTTP_401_UNAUTHORIZED, content={"message": "Неверный пароль!"})
        

@app.get("/v1/users/get")
async def get_user(code: int):
    with sqlite3.connect('FastApi/users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'SELECT username FROM users WHERE code = "{code}"')
        username = cur.fetchone()
        if not username:
            return JSONResponse(status_code=status.HTTP_404_NOT_FOUND, content={"message": "Такого пользователя не существует!"})
        else:
            cur.execute(f'SELECT name, age, school FROM users WHERE code = "{code}"')
            name, age, school = cur.fetchone()
            return {"message": "Пользователь найден!", "username": username[0], "name": name, "age" : age, "school" : school}


@app.post("/v1/users/grant")
async def grant_user(code: int):
    with sqlite3.connect('FastApi/users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'SELECT COUNT(*) FROM users WHERE code = "{code}"')
        if cur.fetchone():
            cur.execute(f'UPDATE users SET role = "checker" WHERE code = "{code}"')
            conn.commit()
            return {"message": "Роль пользователя успешно изменена!"}
        else:
            return JSONResponse(status_code=status.HTTP_404_NOT_FOUND, content={"message": "Такого пользователя не существует!"})

# # Создаем экземпляр Jinja2Templates
# templates = Jinja2Templates(directory="FastApi")  # Укажите путь к вашим HTML-шаблонам

@app.get('/user')
async def user_info(code: int):
    with sqlite3.connect('FastApi/users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'SELECT username, name, age, school, role FROM "users" WHERE code = "{code}"')
        user = cur.fetchone()
        if not user: return JSONResponse(status_code=status.HTTP_404_NOT_FOUND, content={"message": "Такого пользователя не существует!"})
        username, name, age, school, role = user
        data = json.dumps({"username": username, "name": name, "age": age, "school": school, "role": role, "code" : code})
        app_url = f"hackathon://user_data?data={data}"  # замените на схему вашего приложения и данные для передачи
        return RedirectResponse(url=app_url)

#     return templates.TemplateResponse("user_info.html", {"request": request, "username": username, "name": name, "age": age, "school": school, "role": role, "code" : code})



if __name__ == "__main__":
    uvicorn.run(app, host='0.0.0.0', port=8677)
