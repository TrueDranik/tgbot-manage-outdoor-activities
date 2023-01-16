package com.bot.sup.service.client;

import com.bot.sup.mapper.BookingMapper;
import com.bot.sup.mapper.ClientMapper;
import com.bot.sup.model.dto.BookingCreateDto;
import com.bot.sup.model.dto.ClientCreateDto;
import com.bot.sup.model.dto.ClientDto;
import com.bot.sup.model.entity.Client;
import com.bot.sup.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final BookingMapper bookingMapper;
    private final ClientMapper clientMapper;

    private final ClientRepository clientRepository;

    @Override
    public ClientDto createClient(BookingCreateDto bookingCreateDto) {
        ClientCreateDto clientCreateDto = bookingMapper.dtoToDto(bookingCreateDto);
        Client client = clientRepository.findByPhoneNumber(clientCreateDto.getPhoneNumber());
        if (client == null) {
            Client createdClient = new Client();
            createdClient.setFirstName(clientCreateDto.getFirstName());
            createdClient.setLastName(clientCreateDto.getLastName());
            createdClient.setPhoneNumber(clientCreateDto.getPhoneNumber());
            clientRepository.save(createdClient);
            return clientMapper.domainToDto(createdClient);
        }
        return clientMapper.domainToDto(client);
    }
}
