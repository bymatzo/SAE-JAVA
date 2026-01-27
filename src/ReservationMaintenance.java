public class ReservationMaintenance extends Reservation {
    @Override
    public TypeReservation getType() {
        return TypeReservation.MAINTENANCE;
    }
}

