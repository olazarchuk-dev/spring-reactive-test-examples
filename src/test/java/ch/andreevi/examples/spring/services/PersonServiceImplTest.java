package ch.andreevi.examples.spring.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.andreevi.examples.spring.models.Person;
import ch.andreevi.examples.spring.repositories.PersonRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository repository; // TODO: Этот класс не должен явно вызываться, но он привязан к тестируемому классу (здесь просто указываем ожидаемый результат...)

    @InjectMocks
    private PersonServiceImpl service; // TODO: Этот класс (бин), который должен быть явно вызван и протестирован

    @Test
    void createPersonTest() {
        Person data = new Person("id", "John", "Doe", 28);
        Mono<Person> source = Mono.just(data);
        when(repository.save(any(Person.class))).thenReturn(source);
        StepVerifier.create(service.create(data)).assertNext(person -> 
            assertThat(person).hasNoNullFieldsOrProperties()).verifyComplete();
    }

    @Test
    void findByIdTest() {
        //  GIVEN
        // 1.1 Мокаю результат возвращаемый из репозитория (PersonRepository), которые якобы ожидаются из БД (MongoDB)
        Person response = new Person("someId", "John", "Doe", 28);
        Mono<Person> expectedResponse = Mono.just(response);

        //  WHEN
        // 1.2 Мокаю параметр передаваемый в запросе для репозитория И привязываю к нему ожидаемый результат из репозитория
        String expectedRequest = "someId";
        when( repository.findById(expectedRequest) )
                .thenReturn(expectedResponse);

        //  THEN
        // 2.1 Делаю явный вызов тестируемого класса (бина)
        StepVerifier.create( service.findById(expectedRequest) )
                // 2.2 Проверяю ожидаемые результаты для тестируемого класса (бина)
                .assertNext(person -> {
                    assertThat(person).hasNoNullFieldsOrProperties();
                    assertThat(person.getId()).isEqualTo("someId");
                    assertThat(person.getFirstName()).isEqualTo("John");
                    assertThat(person.getLastName()).isEqualTo("Doe");
                    assertThat(person.getAge()).isEqualTo(28);
                })
                .verifyComplete();
    }

    @Test
    void removeTest() {
        String id = "id";
        when(repository.deleteById(anyString())).thenReturn(Mono.empty());
        StepVerifier.create(service.remove(id)).verifyComplete();
    }
}