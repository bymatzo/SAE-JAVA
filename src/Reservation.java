
import java.time.LocalDateTime;

public abstract class Reservation {
    protected Utilisateur utilisateur;
    protected Ressource ressource;
    protected LocalDateTime dateDebut;
    protected int dureeMinutes;

    public abstract TypeReservation getType();
}

