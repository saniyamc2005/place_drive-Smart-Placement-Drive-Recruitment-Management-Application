const API = 'http://localhost:8080/api';

const Session = {
  save(d)  { sessionStorage.setItem('pd_user', JSON.stringify(d)); },
  get()    { const d = sessionStorage.getItem('pd_user'); return d ? JSON.parse(d) : null; },
  clear()  { sessionStorage.removeItem('pd_user'); },
  role()   { const u = this.get(); return u?.role; },
  studentId(){ return this.get()?.studentId; },
  companyId(){ return this.get()?.companyId; },
  name()   { return this.get()?.name || ''; },
};

async function apiRequest(method, ep, body = null) {
  const opts = { method, headers: { 'Content-Type': 'application/json' } };
  if (body) opts.body = JSON.stringify(body);
  const r = await fetch(API + ep, opts);
  if (!r.ok) throw new Error('HTTP ' + r.status);
  return r.json();
}
const api = {
  get:    ep       => apiRequest('GET', ep),
  post:   (ep, b)  => apiRequest('POST', ep, b),
  put:    (ep, b)  => apiRequest('PUT', ep, b),
  delete: ep       => apiRequest('DELETE', ep),
};

function showToast(message, type = 'info', duration = 3200) {
  let c = document.getElementById('toast-container');
  if (!c) { c = document.createElement('div'); c.id = 'toast-container'; document.body.appendChild(c); }
  const icons = { success: '', error: '', info: 'ℹ' };
  const t = document.createElement('div');
  t.className = `toast ${type}`;
  t.innerHTML = `<span style="width:20px;height:20px;border-radius:50%;background:rgba(255,255,255,.15);display:flex;align-items:center;justify-content:center;font-size:.7rem;flex-shrink:0">${icons[type]||icons.info}</span><span>${message}</span>`;
  c.appendChild(t);
  requestAnimationFrame(() => requestAnimationFrame(() => t.classList.add('show')));
  setTimeout(() => { t.classList.remove('show'); setTimeout(() => t.remove(), 400); }, duration);
}

function openModal(id) {
  const o = document.getElementById(id); if (!o) return;
  o.classList.remove('hidden');
  requestAnimationFrame(() => o.classList.add('active'));
  document.body.style.overflow = 'hidden';
}
function closeModal(id) {
  const o = document.getElementById(id); if (!o) return;
  o.classList.remove('active');
  setTimeout(() => { o.classList.add('hidden'); document.body.style.overflow = ''; }, 250);
}
document.addEventListener('click', e => {
  if (e.target.classList.contains('modal-overlay')) {
    e.target.classList.remove('active');
    setTimeout(() => { e.target.classList.add('hidden'); document.body.style.overflow = ''; }, 250);
  }
});

function badgeHtml(status) {
  const m = { Applied:'badge-applied', Shortlisted:'badge-shortlisted', Selected:'badge-selected', Rejected:'badge-rejected', Open:'badge-open', Closed:'badge-closed' };
  return `<span class="badge ${m[status]||'badge-primary'}">${status}</span>`;
}
function avatarLetter(n) { return n ? n.charAt(0).toUpperCase() : '?'; }
function emptyState(msg = 'No data found', sub = '') {
  return `<div class="empty-state"><span class="empty-icon"></span><h4>${msg}</h4>${sub?`<p>${sub}</p>`:''}</div>`;
}
function formatDate(d) { if (!d) return '—'; return new Date(d).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' }); }
function packageLabel(lpa) { return lpa ? `₹${lpa} LPA` : '—'; }

function requireAuth(roles = []) {
  const u = Session.get();
  if (!u) { window.location.href = 'index.html'; return false; }
  if (roles.length && !roles.includes(u.role)) { window.location.href = 'index.html'; return false; }
  return true;
}
function logout() { Session.clear(); window.location.href = 'index.html'; }

function renderSidebarUser() {
  const u = Session.get(); if (!u) return;
  const n = document.getElementById('sidebar-user-name');
  const r = document.getElementById('sidebar-user-role');
  const a = document.getElementById('sidebar-user-avatar');
  if (n) n.textContent = u.name;
  if (r) r.textContent = u.role.charAt(0).toUpperCase() + u.role.slice(1);
  if (a) a.textContent = avatarLetter(u.name);
}