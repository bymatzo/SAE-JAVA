import java.time.*;
import java.util.*;

public class HistoriqueReservations {

    private final List<Reservation> reservations = new ArrayList<>();
    private final List<Utilisateur> utilisateurs = new ArrayList<>();
    private final List<Ressource> ressources = new ArrayList<>();

    public HistoriqueReservations() {
    }

    public Utilisateur verifierUtilisateur(Utilisateur u) {
        if (u == null) {
            throw new IllegalArgumentException("Utilisateur null");
        }

        for (Utilisateur existant : utilisateurs) {
            if (existant.getNom().equals(u.getNom())) {
                return existant;
            }
        }
        utilisateurs.add(u);
        return u;
    }

    public List<Utilisateur> getUtilisateurs() {
        return List.copyOf(utilisateurs);
    }

    public boolean supprimerUtilisateurParNom(String nom) {
        if (nom == null) {
            throw new IllegalArgumentException("Nom utilisateur null");
        }
        return utilisateurs.removeIf(u -> u.getNom().equals(nom));
    }

    public Utilisateur getUtilisateurParNom(String nom) {
        if (nom == null) {
            throw new IllegalArgumentException("Nom utilisateur null");
        }
        for (Utilisateur u : utilisateurs) {
            if (u.getNom().equals(nom)) {
                return u;
            }
        }
        return null;
    }

    public Ressource verifierRessource(Ressource r) {
        if (r == null) {
            throw new IllegalArgumentException("Ressource null");
        }

        for (Ressource existante : ressources) {
            if (existante.getNom().equals(r.getNom())) {
                return existante;
            }
        }
        ressources.add(r);
        return r;
    }

    public List<Ressource> getRessources() {
        return List.copyOf(ressources);
    }

    public boolean supprimerRessourceParNom(String nom) {
        if (nom == null) {
            throw new IllegalArgumentException("Nom ressource null");
        }
        return ressources.removeIf(r -> r.getNom().equals(nom));
    }

    public Ressource getRessourceParNom(String nom) {
        if (nom == null) {
            throw new IllegalArgumentException("Nom ressource null");
        }
        for (Ressource r : ressources) {
            if (r.getNom().equals(nom)) {
                return r;
            }
        }
        return null;
    }

    public void ajouter(Reservation r) {
        if (r == null) {
            throw new IllegalArgumentException("Reservation null");
        }
        reservations.add(r);
    }

    public boolean supprimer(Reservation r) {
        if (r == null) {
            throw new IllegalArgumentException("Reservation null");
        }
        return reservations.remove(r);
    }

    public List<Reservation> getAll() {
        return new ArrayList<>(reservations);
    }

    public boolean supprimerParId(int id) {
        return reservations.removeIf(r -> r.getId() == id);
    }

    public Reservation getReservationParId(int id) {
        for (Reservation r : reservations) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public boolean modifierReservation(int id, Reservation nouvelle) {
        if (nouvelle == null) {
            throw new IllegalArgumentException("Reservation null");
        }
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == id) {
                reservations.set(i, nouvelle);
                return true;
            }
        }
        return false;
    }

    public List<Reservation> rechercherParUtilisateur(Utilisateur u) {
        if (u == null) throw new IllegalArgumentException("Utilisateur null");

        List<Reservation> res = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getUtilisateur().equals(u)) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reservation> rechercherParRessource(Ressource ress) {
        if (ress == null) throw new IllegalArgumentException("Ressource null");

        List<Reservation> res = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getRessource().equals(ress)) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reservation> rechercherParType(TypeReservation type) {
        if (type == null) throw new IllegalArgumentException("Type null");

        List<Reservation> res = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getType() == type) {
                res.add(r);
            }
        }
        return res;
    }

    public List<Reservation> rechercherEntre(LocalDateTime debut, LocalDateTime fin) {
        if (debut == null || fin == null) {
            throw new IllegalArgumentException("Dates null");
        }
        if (fin.isBefore(debut)) {
            throw new IllegalArgumentException("fin < debut");
        }

        List<Reservation> res = new ArrayList<>();
        for (Reservation r : reservations) {
            LocalDateTime d = r.getDateDebut();
            if ((d.isEqual(debut) || d.isAfter(debut)) && (d.isEqual(fin) || d.isBefore(fin))) {
                res.add(r);
            }
        }
        return res;
    }
}