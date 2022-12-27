package com.bot.sup.repository;

import com.bot.sup.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findBookingByClient_Id(Long clientId);

    @Query(value = "SELECT SUM(invited_users) " +
            "FROM booking b " +
            "inner join client c on c.id = b.client_id " +
            "inner join schedule_client sc on c.id = sc.client_id " +
            "inner join schedule s on s.id = sc.schedule_id " +
            "where sc.schedule_id = ?1", nativeQuery = true)
    Long findSumBookingClientByScheduleId(Long scheduleId);

    @Query(value = "SELECT * " +
            "FROM booking b " +
            "inner join client c on c.id = b.client_id " +
            "inner join schedule_client sc on c.id = sc.client_id " +
            "inner join schedule s on s.id = sc.schedule_id " +
            "where sc.schedule_id = ?1 and b.client_id = ?2", nativeQuery = true)
    Optional<Booking> findBookingByClientIByScheduleId(Long scheduleId, Long clientId);
}
