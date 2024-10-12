package yuquiz.domain.quiz.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yuquiz.domain.quiz.entity.Quiz;
import yuquiz.domain.user.entity.User;

import java.util.Optional;


public interface QuizRepository extends JpaRepository<Quiz, Long>, CustomQuizRepository {
    Page<Quiz> findAllByWriter(User writer, Pageable pageable);

    @Query("select q.writer.id from Quiz q where q.id = :id")
    Optional<Long> findWriterById(@Param("id") Long id);
}
