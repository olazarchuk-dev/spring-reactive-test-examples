package ch.andreevi.examples.spring.repositories;

import ch.andreevi.examples.spring.models.User;
import ch.andreevi.examples.spring.repositories.CustomUserRepositoryImpl;
import ch.andreevi.examples.spring.services.UserServiceImpl;
//import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoClients;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import com.mongodb.ConnectionString;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CustomUserRepositoryImplTest {

    @InjectMocks
    private UserServiceImpl service; // TODO: Этот класс (бин), который должен быть явно вызван и протестирован

    @Mock
    private UserRepository repository;
//    @Spy
//    private UserRepository repository = new SimpleReactiveMongoRepository(new ReactiveMongoRepository<User, String>(), CustomUserRepository);
//    private UserRepository repository = new SimpleReactiveMongoDatabaseFactory(MongoClients.create(), "users");
//    private UserRepository repository = new SimpleReactiveMongoDatabaseFactory(new ConnectionString("mongodb://localhost:27017"));
//    private UserRepository repository = new CustomUserRepositoryImpl(new ReactiveMongoTemplate(MongoClients.create()));

    @InjectMocks
    private CustomUserRepositoryImpl customUserRepository; // TODO: Этот класс (бин), который должен быть явно вызван и протестирован

    @Mock
    private ReactiveMongoTemplate mongoTemplate; // TODO: Это связанный класс(ы) не должен явно вызываться, но он привязан к тестируемому классу (здесь просто указываем ожидаемый результат...)

    @Test
    void changePasswordTest() {
        //  GIVEN
        // 1.1 Мокаю результат возвращаемый из репозитория (UserRepository), которые якобы ожидаются из БД (MongoDB)
        Mono<User> expectedResponse = Mono.just(User.dummy());

        //  WHEN
        // 1.2 Мокаю параметр передаваемый в запросе для репозитория И привязываю к нему ожидаемый результат из репозитория
        String requestUserId = "userId";
        String requestPassword = "password";
        Query query = new Query(Criteria.where("userId").is(requestUserId));
        Update update = new Update().set("password", requestPassword);
        when( mongoTemplate.findAndModify(query, update, User.class) )
                .thenReturn( expectedResponse );

        //  THEN
        // 2.1 Делаю явный вызов тестируемого класса (бина)
        StepVerifier.create( customUserRepository.changePassword(requestUserId, requestPassword) )
                // 2.2 Проверяю ожидаемые результаты для тестируемого класса (бина)
                .assertNext(user -> assertThat(user)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("email", "email")
                    .hasFieldOrPropertyWithValue("userId", "userId")
                    .hasFieldOrPropertyWithValue("password", "password")
                    .hasNoNullFieldsOrPropertiesExcept("roles"))
            .verifyComplete();

        //

        //  WHEN
        // 1.2 Мокаю параметр передаваемый в запросе для репозитория И привязываю к нему ожидаемый результат из репозитория
//        when( customUserRepository.changePassword(requestUserId, requestPassword) )
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