package sv.edu.udb.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sv.edu.udb.repository.domain.Movie;
import sv.edu.udb.service.implementation.MovieServiceImpl;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Por cuestiones didácticas utilizamos la anotación
 * @SpringBootTest para poder levantar un contexto de Spring
 * y poder hacer las pruebas de integración sobre inyección de
 * dependencias. EN LA PRÁCTICA SE HACE TESTING POR CAPAS.
 */
@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieServiceImpl movieService;

    @Test
    void shouldMovieServiceNotNull_When_SpringContextWorks() {
        assertNotNull(movieService);
    }

    @Test
    void shouldMovieRepositoryNotNul_When_DIWorks() {
        assertNotNull(movieService.getMovieRepository());
    }

    @Test
    void shouldGetAMovie_When_TheMovieIdExists() {
        final Long expectedMovieId = 1L;
        final String expectedMovieName = "Inception";
        final Integer expectedReleaseYear = 2010;

        final Movie actualMovie = movieService.findMovieById(expectedMovieId);

        assertEquals(expectedMovieId, actualMovie.getId());
        assertEquals(expectedMovieName, actualMovie.getName());
        assertEquals(expectedReleaseYear, actualMovie.getReleaseYear());
    }

    @Test
    void shouldThrowNoSuchElementException_When_MovieIdDoesNotExists() {
        final Long fakeId = 4L;
        final String expectedErrorMessage = "Movie doesn't exist";

        final Exception exception = assertThrows(NoSuchElementException.class,
                () -> movieService.findMovieById(fakeId));

        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    // 🔽 NUEVAS PRUEBAS

    @Test
    void shouldHaveCorrectType_When_FindingMovieById() {
        final Movie movie = movieService.findMovieById(2L);
        assertTrue(movie.getType().contains("Thriller"),
                "La película debe contener 'Thriller' en su tipo");
    }

    @Test
    void shouldReturnSameObjectReference_ForSameId() {
        final Movie movie1 = movieService.findMovieById(3L);
        final Movie movie2 = movieService.findMovieById(3L);
        assertSame(movie1, movie2, "Debe retornar la misma instancia del objeto en memoria");
    }

    @Test
    void shouldNotContainNullFields_When_MovieIsFound() {
        final Movie movie = movieService.findMovieById(1L);
        assertAll("movie",
                () -> assertNotNull(movie.getId(), "ID no debe ser nulo"),
                () -> assertNotNull(movie.getName(), "Nombre no debe ser nulo"),
                () -> assertNotNull(movie.getType(), "Tipo no debe ser nulo"),
                () -> assertNotNull(movie.getReleaseYear(), "Año de lanzamiento no debe ser nulo")
        );
    }
}


