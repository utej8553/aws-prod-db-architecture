# Student Database Frontend

A minimal React + Vite frontend for the Spring Boot student API in `../spring/main`.

## Requirements

- Node.js 18 or newer
- The Spring Boot backend running on `http://localhost:8080`

## Install and run

```bash
cd react
npm install
npm run dev
```

Open the local URL printed by Vite, normally `http://localhost:5173`.

To create a production build:

```bash
npm run build
```

## Backend connection

The frontend calls the backend directly with `fetch`. It uses `http://localhost:8080` by default. To use another backend URL, create `react/.env.local`:

```env
VITE_API_URL=http://localhost:8080
```

Restart Vite after changing the environment file.

## API endpoints

- `POST /students` - create a student with `name`, `roll`, and `branch`
- `GET /students` - load all students
- `GET /students/{roll}` - search by roll number
- `DELETE /students/{roll}` - delete a student

The backend controller permits requests from the local Vite development origin (`http://localhost:5173`). If the frontend is served from another origin, update the `@CrossOrigin` origin in `StudentController.java` or configure CORS in Spring Boot.
