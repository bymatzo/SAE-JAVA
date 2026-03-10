import java.time.LocalDateTime;

public class ReservationEmprunt extends Reservation {
	
	
	
	public ReservationEmprunt(Utilisateur utilisateur, Ressource ressource, LocalDateTime dateDebut,int dureeMinutes) {
		
		super(utilisateur, ressource, dateDebut, dureeMinutes);
		
				}

	
    @Override
    public TypeReservation getType() {
        return TypeReservation.EMPRUNT;
    }
}

