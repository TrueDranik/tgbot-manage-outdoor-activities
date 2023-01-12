package com.bot.sup.repository;

import com.bot.sup.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByScheduleId(Long scheduleId);

    @Query("select b from Booking b where b.schedule = :scheduleId and b.paymentStatus = :paymentStatus")
    List<Booking> findBookingByScheduleIdByPaymentStatus(@Param("scheduleId") Long scheduleId, @Param("paymentStatus") String paymentStatus);



    void delete(Booking entity);

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
            "where sc.schedule_id = ?1 and sc.client_id = ?2", nativeQuery = true)
    Optional<Booking> findBookingByClientIByScheduleId(Long scheduleId, Long clientId);

}
