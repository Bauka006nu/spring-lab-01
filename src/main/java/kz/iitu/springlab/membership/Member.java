package kz.iitu.springlab.membership;

import java.time.LocalDate;

public record Member(Long id, String name, LocalDate expiresOn) {}
