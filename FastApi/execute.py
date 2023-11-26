import sqlite3

# Create the users table if it doesn't exist
with sqlite3.connect('users.db') as conn:
    cur = conn.cursor()
    cur.execute(f'DROP TABLE IF EXISTS users')
    cur.execute('''
        CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT,
        password TEXT,
        name TEXT,
        age FLOAT,
        school TEXT,
        code INTEGER,
        role TEXT DEFAULT "user"
    )
    ''')
    conn.commit()