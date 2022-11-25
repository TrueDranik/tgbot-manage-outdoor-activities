package com.bot.sup.repository;

import com.bot.sup.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c JOIN c.schedule s WHERE s.id = ?1")
    List<Client> findClientByScheduleId(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM schedule_client sc WHERE sc.client_id = ?1")
    void deleteClientFromSchedule(Long id);

    boolean existsByTelegramId(Long chatId);

    Optional<Client> findByTelegramId(Long telegramId);
}