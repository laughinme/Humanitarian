from fastapi import FastAPI, Response, status
from fastapi.responses import JSONResponse
from pydantic import BaseModel
import sqlite3
import random
import string
from typing import Optional
import uvicorn


app = FastAPI(
    title='Full NEGROID'
)


# @app.get("/")
# async def root():
#     return {"message": "Hello World"}

# @app.get("/hello")
# def hello():
#     return "Hello World"

def generate_code():
    count = 1
    with sqlite3.connect('users.db') as conn:
        cur = conn.cursor()
        while count > 0:
            code = ''.join(random.choices(string.digits, k=6))
            cur.execute("SELECT COUNT(*) FROM users WHERE code = ?", (code,))
            count = cur.fetchone()[0]
        return code
    
@app.post("/users/register")
async def register(username: str, password: str, age: int=None, school: str=None, name: str=None):
    with sqlite3.connect('users.db') as conn:
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
    

@app.post("/users/login")
async def login(username: str, password: str, name: str = None):
    with sqlite3.connect('users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'SELECT password FROM users WHERE username = "{username}"')
        dbpassword = cur.fetchone()
        if not dbpassword:
            code = generate_code()
            cur.execute(f'INSERT INTO users (username, name, password, code) VALUES (?, ?, ?, ?)', (username, name, password, code))
            conn.commit()
            return {"message": "Успешная регистрация!", "code": "200", "auth_code": str(code), "role": "user"}
        else:
            dbpassword = dbpassword[0]
            if dbpassword == password:
                cur.execute('SELECT role, code FROM users WHERE username = ?', (username,))
                role, code = cur.fetchone()
                return {"message": "Успешный вход!", "code": "200", "auth_code": str(code), "role": role}
            
            elif dbpassword != password:
                return {"message": "Неверный пароль!", "code": "401"}
        

@app.get("/users/get")
async def get_user(code: int):
    with sqlite3.connect('users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'SELECT username FROM users WHERE code = "{code}"')
        username = cur.fetchone()
        if not username:
            return {"message": "Пользователь не найден!", "code": "404"}
        else:
            cur.execute(f'SELECT name FROM users WHERE code = "{code}"')
            name = cur.fetchone()
            return {"message": "Пользователь найден!", "code": "200", "username": username[0], "name": name[0] if name else name}


@app.post("/users/grant")
async def grant_user(code: int):
    with sqlite3.connect('users.db') as conn:
        cur = conn.cursor()
        cur.execute(f'UPDATE users SET role = "checker" WHERE code = "{code}"')
        conn.commit()
        return {"message": "Роль пользователя успешно изменена!", "code": "200"}
    

# uvicorn.run(app)