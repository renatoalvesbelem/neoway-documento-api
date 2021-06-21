package com.neoway.documento.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "database_sequences")
public class DatabaseSequenceDto {

    @Id
    private String id;

    private long seq;

    public DatabaseSequenceDto() {}

}
