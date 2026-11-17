const API = '/api/formations';
const $ = (id) => document.getElementById(id);

function afficherMessage(texte, ok) {
  const m = $('message');
  m.textContent = texte;
  m.className = 'message ' + (ok ? 'ok' : 'erreur');
  m.hidden = false;
  setTimeout(() => (m.hidden = true), 4000);
}

async function appel(url, options = {}) {
  const res = await fetch(url, { headers: { 'Content-Type': 'application/json' }, ...options });
  const data = await res.json().catch(() => ({}));
  if (!res.ok) throw new Error(data.message || 'Erreur ' + res.status);
  return data;
}

function echapper(s) {
  const d = document.createElement('div');
  d.textContent = s;
  return d.innerHTML;
}

async function chargerFormations() {
  const formations = await appel(API);
  $('liste-formations').innerHTML = formations.map(f => `
    <tr>
      <td>${echapper(f.titre)}</td>
      <td>${new Date(f.dateDebut).toLocaleDateString('fr-FR')}</td>
      <td>${f.places}</td>
      <td>${f.inscrits}</td>
      <td class="${f.placesRestantes === 0 ? 'complet' : ''}">${f.placesRestantes === 0 ? 'Complet' : f.placesRestantes}</td>
      <td><button class="lien" onclick="voirInscrits(${f.id}, '${echapper(f.titre).replace(/'/g, "\\'")}')">Inscrits</button></td>
    </tr>`).join('') || '<tr><td colspan="6">Aucune formation pour le moment.</td></tr>';

  $('select-formation').innerHTML = '<option value="">-- Choisir une formation --</option>' +
    formations.map(f => `<option value="${f.id}">${echapper(f.titre)} (${f.placesRestantes} place(s) restante(s))</option>`).join('');
}

async function voirInscrits(id, titre) {
  const inscrits = await appel(`${API}/${id}/inscriptions`);
  $('titre-inscrits').textContent = 'Inscrits — ' + titre;
  $('liste-inscrits').innerHTML = inscrits.map(i => `<li>${echapper(i.nom)} — ${echapper(i.email)}</li>`).join('')
    || '<li>Aucun inscrit.</li>';
  $('bloc-inscrits').hidden = false;
}

$('form-formation').addEventListener('submit', async (e) => {
  e.preventDefault();
  const d = Object.fromEntries(new FormData(e.target));
  try {
    await appel(API, { method: 'POST', body: JSON.stringify({ ...d, places: Number(d.places) }) });
    e.target.reset();
    afficherMessage('Formation ajoutée.', true);
    chargerFormations();
  } catch (err) { afficherMessage(err.message, false); }
});

$('form-inscription').addEventListener('submit', async (e) => {
  e.preventDefault();
  const { formationId, nom, email } = Object.fromEntries(new FormData(e.target));
  try {
    await appel(`${API}/${formationId}/inscriptions`, { method: 'POST', body: JSON.stringify({ nom, email }) });
    e.target.reset();
    afficherMessage('Inscription enregistrée.', true);
    chargerFormations();
  } catch (err) { afficherMessage(err.message, false); }
});

chargerFormations().catch(err => afficherMessage(err.message, false));
