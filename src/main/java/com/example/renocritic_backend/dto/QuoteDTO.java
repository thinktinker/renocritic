package com.example.renocritic_backend.dto;

import com.example.renocritic_backend.model.EnumVerdict;
import com.example.renocritic_backend.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuoteDTO {

    Integer submission_id;                                                      // Integer submission_id
    String work_description;                                                    // String work_description
    Integer quantity;                                                           // Integer quantity
    Double unit_rate;                                                            // Double unitRate
    Double price;                                                               // Double price
    EnumVerdict verdict = EnumVerdict.Fair;                                     // EnumVerdict

}
