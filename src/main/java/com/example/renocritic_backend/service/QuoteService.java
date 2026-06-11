package com.example.renocritic_backend.service;

import com.example.renocritic_backend.dto.QuoteDTO;
import com.example.renocritic_backend.exception.ResourceNotFoundException;
import com.example.renocritic_backend.model.Quote;
import com.example.renocritic_backend.model.User;
import com.example.renocritic_backend.repository.QuoteRepository;
import com.example.renocritic_backend.repository.UserRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuoteService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuoteRepository quoteRepository;

    public List<Quote> saveAll(List<QuoteDTO> QuoteDTOS) throws ResourceNotFoundException{

        // the bearer token is passed in and used by authentication
        // to extract the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new ResourceNotFoundException("User not found."));

        List<Quote> savedQuotes = new ArrayList<>();

        QuoteDTOS.forEach(quoteDTO -> {
            Quote quote = Quote.builder()
                    .submission_id(quoteDTO.getSubmission_id())
                    .work_description(quoteDTO.getWork_description())
                    .quantity(quoteDTO.getQuantity())
                    .unitRate(quoteDTO.getUnit_rate())
                    .price(quoteDTO.getPrice())
                    .verdict(quoteDTO.getVerdict())
                    .user(user)
                    .build();

            savedQuotes.add(quote);
        });

        return quoteRepository.saveAll(savedQuotes);
    }

    public List<QuoteDTO> findUserQuotes() throws ResourceNotFoundException {

        // the bearer token is passed in and used by authentication
        // to extract the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new ResourceNotFoundException("User not found."));

        List<QuoteDTO> quoteDTOS = new ArrayList<>();
        List<Quote> foundUserQuotes = quoteRepository.findByUserId(user.getId());

        foundUserQuotes.forEach(quote -> {
            QuoteDTO quoteDTO = QuoteDTO.builder()
                    .submission_id(quote.getSubmission_id())
                    .work_description(quote.getWork_description())
                    .quantity(quote.getQuantity())
                    .unit_rate(quote.getUnitRate())
                    .price(quote.getPrice())
                    .verdict(quote.getVerdict())
                    .build();

            quoteDTOS.add(quoteDTO);
        });

        return quoteDTOS;

    }
}
