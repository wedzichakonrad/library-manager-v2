package pl.manager.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    private int id;
    private int bookId;
    private int userId;
    private Timestamp rentalDate;
    private boolean active;
}