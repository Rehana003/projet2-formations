# Projet 2 — Inscription à des formations

Application cloud conteneurisée (UTM — Dr SOMDA, 2025-2026).

Un centre publie des formations ; des étudiants s'y inscrivent. L'application refuse l'inscription si la formation est complète ou si l'étudiant (e-mail) est déjà inscrit, et affiche les inscrits et les places restantes.

## Architecture

| Service | Image / build | Rôle |
|---|---|---|
| `web` | `nginx:1.27-alpine` (./web) | Sert l'interface HTML/JS et redirige `/api/` vers `app` |
| `app` | Spring Boot 3 / Java 21 (./app) | API REST |
| `db`  | `postgres:17` | Base de données, données persistées dans le volume `pgdata` |

Seul `web` est publié, et uniquement sur `127.0.0.1:8080` (accès via tunnel SSH).

## API

| Méthode | URL | Description |
|---|---|---|
| GET  | `/api/formations` | Liste des formations (inscrits + places restantes) |
| POST | `/api/formations` | Ajouter `{titre, dateDebut, places}` |
| GET  | `/api/formations/{id}/inscriptions` | Liste des inscrits |
| POST | `/api/formations/{id}/inscriptions` | Inscrire `{nom, email}` → 409 si complet ou déjà inscrit |

## Base de données

```
formation(id, titre, date_debut, places)
inscription(id, formation_id, nom, email)  -- UNIQUE (formation_id, email)
```
Le schéma est créé au démarrage (`app/src/main/resources/schema.sql`).

## Lancement en local

```bash
cp .env.example .env        # puis modifier DB_PASSWORD
docker compose up -d --build
docker compose ps
```
Ouvrir http://localhost:8080

## Déploiement sur AWS EC2

```bash
# Instance Ubuntu, t3.small, 20 Go, port 22 ouvert uniquement depuis votre IP
ssh -i cle.pem ubuntu@ADRESSE_IP
curl -fsSL https://get.docker.com | sudo sh
git clone <URL_DU_DEPOT> && cd <dossier>
cp .env.example .env && nano .env
sudo docker compose up -d --build
sudo docker compose ps

# Depuis votre poste :
ssh -i cle.pem -N -L 8080:127.0.0.1:8080 ubuntu@ADRESSE_IP
# puis http://localhost:8080
```

Arrêter l'instance après chaque séance ; la résilier après la soutenance.

## Commandes utiles

```bash
docker compose logs -f app                 # logs de l'API
docker compose exec db psql -U app projet  # console SQL
docker compose down                        # arrêt (les données restent dans pgdata)
```
