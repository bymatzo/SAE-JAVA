import java.time.*;

public class ReservationMaintenance extends Reservation {
	
	
	public ReservationMaintenance(Utilisateur utilisateur, Ressource ressource, LocalDateTime dateDebut, int dureeMinutes) {
	
	super(utilisateur, ressource, dateDebut, dureeMinutes);
	
	}

    @Override
    public TypeReservation getType() {
        return TypeReservation.MAINTENANCE;
    }
}

