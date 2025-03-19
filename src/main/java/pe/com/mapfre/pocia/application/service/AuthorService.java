package pe.com.mapfre.pocia.application.service;


import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import pe.com.mapfre.pocia.infrastructure.mesaggins.AuthorEvent;
import pe.com.mapfre.pocia.presentation.api.dto.AuthorDTO;

public interface AuthorService {
    List<AuthorDTO> findAll();
    Optional<AuthorDTO> findById(Long id);


    AuthorDTO create(AuthorDTO authorDTO);
    void deleteById(Long id);
   // AuthorDTO update(Long id, AuthorDTO authorDTO);

}
