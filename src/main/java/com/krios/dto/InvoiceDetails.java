package com.krios.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class InvoiceDetails {
	
    private BookDto bookDto; 
    @JsonIgnore 
    private List<UserDto> userDto = new ArrayList<>(); 
}
