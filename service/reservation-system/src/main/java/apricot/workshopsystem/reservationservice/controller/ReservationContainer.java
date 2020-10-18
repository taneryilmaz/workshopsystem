package apricot.workshopsystem.reservationservice.controller;

import apricot.workshopsystem.common.dto.ReservationDTO;

public class ReservationContainer implements Comparable<ReservationContainer> {
    private final ReservationDTO reservation;

    public ReservationContainer(ReservationDTO reservation) {
        this.reservation = reservation;
    }

    @Override
    public int compareTo(final ReservationContainer that) {
        return reservation.getStartDateTime().compareTo(that.reservation.getStartDateTime());
    }

    public ReservationDTO getReservation() {
        return reservation;
    }

    @Override
    public String toString() {
        return "ReservationContainer{"
                + "reservation=" + reservation
                + '}';
    }
}
