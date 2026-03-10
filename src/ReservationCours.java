import java.time.LocalDateTime;

public class ReservationCours extends Reservation {
	
		
	public ReservationCours(Utilisateur utilisateur, Ressource ressource, LocalDateTime dateDebut,int dureeMinutes) {
		
	super(utilisateur, ressource, dateDebut, dureeMinutes);
		
	}
	
	
	
    @Override
    public TypeReservation getType() {
        return TypeReservation.COURS;
    }
}
