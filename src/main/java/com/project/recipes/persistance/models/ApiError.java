package com.project.recipes.persistance.models;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ApiError {

	private HttpStatus status;
	
	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime date;
	
	private String message;
	
	public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.date = LocalDateTime.now();
    }

}
