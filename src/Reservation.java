import java.time.*;

public abstract class Reservation {

    private static int compteur = 0;

    private final int id;
    private Utilisateur utilisateur;
    private Ressource ressource;
    private LocalDateTime dateDebut;
    private int dureeMinutes;

    public Reservation(Utilisateur utilisateur, Ressource ressource, LocalDateTime dateDebut, int dureeMinutes) {
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur null");
        }
        if (ressource == null) {
            throw new IllegalArgumentException("Ressource null");
        }
        if (dateDebut == null) {
            throw new IllegalArgumentException("DateDebut null");
        }
        if (dureeMinutes < 0) {
            throw new IllegalArgumentException("Durée négative");
        }

        this.id = ++compteur;
        this.utilisateur = utilisateur;
        this.ressource = ressource;
        this.dateDebut = dateDebut;
        this.dureeMinutes = dureeMinutes;
    }

    public int getId() {
        return id;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur null");
        }
        this.utilisateur = utilisateur;
    }

    public void setRessource(Ressource ressource) {
        if (ressource == null) {
            throw new IllegalArgumentException("Ressource null");
        }
        this.ressource = ressource;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        if (dateDebut == null) {
            throw new IllegalArgumentException("DateDebut null");
        }
        this.dateDebut = dateDebut;
    }

    public void setDureeMinutes(int dureeMinutes) {
        if (dureeMinutes < 0) {
            throw new IllegalArgumentException("Durée négative");
        }
        this.dureeMinutes = dureeMinutes;
    }

    public LocalDateTime getDateFin() {
        return dateDebut.plusMinutes(dureeMinutes);
    }

    public abstract TypeReservation getType();
}