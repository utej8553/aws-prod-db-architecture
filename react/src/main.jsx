import { StrictMode, useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

async function request(path, options = {}) {
  const response = await fetch(`${API_URL}${path}`, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  });

  if (!response.ok) {
    const message = response.status === 404 ? 'Student not found.' : `Request failed (${response.status}).`;
    throw new Error(message);
  }

  return response.status === 204 ? null : response.json().catch(() => null);
}

function App() {
  const [students, setStudents] = useState([]);
  const [form, setForm] = useState({ name: '', roll: '', branch: '' });
  const [searchRoll, setSearchRoll] = useState('');
  const [searchResult, setSearchResult] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [searching, setSearching] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });

  const loadStudents = async () => {
    setLoading(true);
    try {
      const data = await request('/students');
      setStudents(Array.isArray(data) ? data : []);
    } catch (error) {
      setMessage({ type: 'error', text: `Could not load students. ${error.message}` });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadStudents();
  }, []);

  const handleFormChange = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  };

  const addStudent = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    setMessage({ type: '', text: '' });
    try {
      await request('/students', { method: 'POST', body: JSON.stringify(form) });
      setForm({ name: '', roll: '', branch: '' });
      setMessage({ type: 'success', text: 'Student added successfully.' });
      await loadStudents();
    } catch (error) {
      setMessage({ type: 'error', text: `Could not add student. ${error.message}` });
    } finally {
      setSubmitting(false);
    }
  };

  const searchStudent = async (event) => {
    event.preventDefault();
    if (!searchRoll.trim()) return;
    setSearching(true);
    setSearchResult(null);
    setMessage({ type: '', text: '' });
    try {
      const student = await request(`/students/${encodeURIComponent(searchRoll.trim())}`);
      setSearchResult(student);
    } catch (error) {
      setMessage({ type: 'error', text: error.message });
    } finally {
      setSearching(false);
    }
  };

  const deleteStudent = async (roll) => {
    if (!window.confirm(`Delete the student with roll number ${roll}?`)) return;
    try {
      await request(`/students/${encodeURIComponent(roll)}`, { method: 'DELETE' });
      setMessage({ type: 'success', text: 'Student deleted successfully.' });
      if (searchResult?.roll === roll) setSearchResult(null);
      await loadStudents();
    } catch (error) {
      setMessage({ type: 'error', text: `Could not delete student. ${error.message}` });
    }
  };

  return (
    <main className="page-shell">
      <header className="page-header">
        <div className="brand-mark" aria-hidden="true">SD</div>
        <div>
          <p className="eyebrow">AWS DATABASE ARCHITECTURE DEMO</p>
          <h1>Student Database</h1>
          <p className="subtitle">A focused workspace for managing student records.</p>
        </div>
      </header>

      {message.text && <div className={`alert ${message.type}`} role="status">{message.text}</div>}

      <section className="content-grid">
        <section className="panel form-panel" aria-labelledby="add-heading">
          <div className="section-heading">
            <div>
              <p className="eyebrow">CREATE RECORD</p>
              <h2 id="add-heading">Add a student</h2>
            </div>
            <span className="step-number">01</span>
          </div>
          <form onSubmit={addStudent}>
            <label>Name<input name="name" value={form.name} onChange={handleFormChange} placeholder="e.g. Ananya Rao" required /></label>
            <label>Roll number<input name="roll" value={form.roll} onChange={handleFormChange} placeholder="e.g. CS-204" required /></label>
            <label>Branch<input name="branch" value={form.branch} onChange={handleFormChange} placeholder="e.g. Computer Science" required /></label>
            <button className="primary-button" type="submit" disabled={submitting}>{submitting ? 'Adding student...' : 'Add Student'}</button>
          </form>
        </section>

        <section className="panel search-panel" aria-labelledby="search-heading">
          <div className="section-heading">
            <div>
              <p className="eyebrow">LOOK UP RECORD</p>
              <h2 id="search-heading">Find by roll number</h2>
            </div>
            <span className="step-number">02</span>
          </div>
          <form className="search-form" onSubmit={searchStudent}>
            <label htmlFor="search-roll">Roll number</label>
            <div className="search-row">
              <input id="search-roll" value={searchRoll} onChange={(event) => setSearchRoll(event.target.value)} placeholder="Enter roll number" required />
              <button className="secondary-button" type="submit" disabled={searching}>{searching ? 'Searching...' : 'Search'}</button>
            </div>
          </form>
          {searchResult && <div className="search-result"><span>Match found</span><strong>{searchResult.name}</strong><p>{searchResult.roll} <i /> {searchResult.branch}</p></div>}
        </section>
      </section>

      <section className="records-section" aria-labelledby="records-heading">
        <div className="records-heading">
          <div><p className="eyebrow">LIVE FROM SPRING BOOT</p><h2 id="records-heading">All students</h2></div>
          <span className="record-count">{students.length} {students.length === 1 ? 'record' : 'records'}</span>
        </div>
        <div className="table-wrap">
          {loading ? <div className="table-message">Loading students...</div> : students.length === 0 ? <div className="table-message"><strong>No students yet</strong><span>Add the first student using the form above.</span></div> : <table><thead><tr><th>Name</th><th>Roll number</th><th>Branch</th><th><span className="sr-only">Actions</span></th></tr></thead><tbody>{students.map((student) => <tr key={student.roll}><td data-label="Name">{student.name}</td><td data-label="Roll number"><span className="roll-tag">{student.roll}</span></td><td data-label="Branch">{student.branch}</td><td data-label="Action" className="action-cell"><button className="delete-button" onClick={() => deleteStudent(student.roll)}>Delete</button></td></tr>)}</tbody></table>}
        </div>
      </section>
      <footer>Connected to <strong>{API_URL}</strong></footer>
    </main>
  );
}

createRoot(document.getElementById('root')).render(<StrictMode><App /></StrictMode>);
