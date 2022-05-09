package ch.andreevi.examples.spring.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.andreevi.examples.spring.models.User;
import ch.andreevi.examples.spring.repositories.UserRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks private UserServiceImpl service; // TODO: Этот класс (бин), который должен быть явно вызван и протестирован

    @Mock private UserRepository repository; // TODO: Это связанный класс(ы) не должен явно вызываться, но он привязан к тестируемому классу (здесь просто указываем ожидаемый результат...)

    @Test
    void createUserTest() {
        User entity = User.dummy();
        Mono<User> source = Mono.just(entity);
        when(repository.save(any(User.class))).thenReturn(source);
        StepVerifier.create(service.createUser(entity))
            .assertNext(user -> assertThat(user)
                        .isNotNull()
                        .hasFieldOrPropertyWithValue("email", "email")
                        .hasFieldOrPropertyWithValue("userId", "userId")
                        .hasFieldOrPropertyWithValue("password", "password")
                        .hasNoNullFieldsOrPropertiesExcept("roles"))
            .verifyComplete();
    }

    @Test
    void findUserByIdSuccessTest() {
        User entity = User.dummy();
        String userId = entity.getUserId();
        Mono<User> source = Mono.just(entity);
        when(repository.findById(userId)).thenReturn(source);
        StepVerifier.create(service.findOne(userId))
            .assertNext(user -> assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("email", "email")
                .hasFieldOrPropertyWithValue("userId", "userId")
                .hasFieldOrPropertyWithValue("password", "password")
                .hasNoNullFieldsOrPropertiesExcept("roles")).verifyComplete();
    }

    @Test
    void findUserByIdFailureTest() {
        String userId = "no-such-user-id";
        Mono<User> source = Mono.empty();
        when(repository.findById(userId)).thenReturn(source);
        StepVerifier.create(service.findOne(userId)).verifyComplete();
    }

    @Test
    void changePasswordTest() {
        //  GIVEN
        // 1.1 Мокаю результат возвращаемый из репозитория (UserRepository), которые якобы ожидаются из БД (MongoDB)
        Mono<User> expectedResponse = Mono.just(User.dummy());

        //  WHEN
        // 1.2 Мокаю параметр передаваемый в запросе для репозитория И привязываю к нему ожидаемый результат из репозитория
        String requestUserId = "userId";
        String requestPassword = "password";
        when( repository.changePassword(requestUserId, requestPassword) )
                .thenReturn( expectedResponse );

        //  THEN
        // 2.1 Делаю явный вызов тестируемого класса (бина)
        StepVerifier.create( service.changePassword(requestUserId, requestPassword) )
                // 2.2 Проверяю ожидаемые результаты для тестируемого класса (бина)
                .assertNext(user -> assertThat(user)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("email", "email")
                    .hasFieldOrPropertyWithValue("userId", "userId")
                    .hasFieldOrPropertyWithValue("password", "password")
                    .hasNoNullFieldsOrPropertiesExcept("roles"))
            .verifyComplete();
    }
}