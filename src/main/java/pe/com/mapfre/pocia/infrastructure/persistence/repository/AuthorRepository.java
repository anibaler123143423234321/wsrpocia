package pe.com.mapfre.pocia.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.mapfre.pocia.infrastructure.persistence.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
	
//	@Modifying
//    @Transactional
//    @Query("INSERT INTO Author (authorId, firstName, lastName, email, requestId) " +
//           "VALUES (:authorId, :firstName, :lastName, :email, :requestId)")
//    void saveAuthor(@Param("authorId") Long authorId,
//                    @Param("firstName") String firstName,
//                    @Param("lastName") String lastName,
//                    @Param("email") String email,
//                    @Param("requestId") String requestId);
	
}
