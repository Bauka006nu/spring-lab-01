package kz.iitu.springlab.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record Book(Long id, @NotBlank String title, @NotBlank String author, @Positive int year) {}
