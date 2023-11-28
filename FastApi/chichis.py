from fastapi import FastAPI, Response, status, Request
from fastapi.responses import JSONResponse, HTMLResponse
from starlette.responses import RedirectResponse
import json

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
async def register(username: str, password: str, age: int=None, school: str=None, name: str=None):
    with sqlite3.connect('FastApi/users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'SELECT COUNT(*) FROM users WHERE username = "{username}"')
        exist = cur.fetchone()[0]
        if not exist:
            code = generate_code()
            cur.execute(f'INSERT INTO users (username, name, age, school, password, code) VALUES (?, ?, ?, ?, ?, ?)', (username, name, age, school, password, code))
            conn.commit()
            return {"message": "Успешная регистрация!", "auth_code": str(code), "role": "user"}
        
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
                cur.execute('SELECT role, code FROM users WHERE username = ?', (username,))
                role, code = cur.fetchone()
                return {"message": "Успешный вход!", "auth_code": str(code), "role": role}
            
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
        app_url = f"myapp://data={data}"  # замените на схему вашего приложения и данные для передачи
        return RedirectResponse(url=app_url)

#     return templates.TemplateResponse("user_info.html", {"request": request, "username": username, "name": name, "age": age, "school": school, "role": role, "code" : code})



# uvicorn.run(app, host='0.0.0.0', port=8677)
# uvicorn.run(app)