package pe.com.mapfre.pocia.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.mapfre.pocia.infrastructure.persistence.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}

