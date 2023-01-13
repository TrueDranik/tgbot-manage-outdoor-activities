package com.bot.sup.service.client;

import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.dto.ClientCreateDto;
import com.bot.sup.model.dto.ClientDto;
import com.bot.sup.model.entity.Client;

public interface ClientService {
    ClientDto createClient(BookingCreateDto bookingCreateDto);
}
