package com.endava.internship.cryptomarket.confservice.business;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.endava.internship.cryptomarket.confservice.business.mappers.UserMapper;
import com.endava.internship.cryptomarket.confservice.business.model.UserDto;
import com.endava.internship.cryptomarket.confservice.data.UserRepository;
import com.endava.internship.cryptomarket.confservice.data.model.User;

import static com.endava.internship.cryptomarket.confservice.data.model.Roles.ADMIN;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.CLIENT;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.OPERAT;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.ACTIVE;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.INACTV;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final String testUsername = "user";
    @Captor
    ArgumentCaptor<User> userCaptor;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    private User testUser;
    private UserDto testUserDto;
    private User testRequester;

    @BeforeEach
    void setUp() {
        testUser = User.builder().username("user").email("email@gmail.com")
                .role(OPERAT).status(ACTIVE).build();
        testUserDto = UserDto.builder().username("user").email("email@gmail.com")
                .role(OPERAT).status(ACTIVE).build();
        testRequester = User.builder().username("admin").email("admin@gmail.com").role(ADMIN).status(ACTIVE).build();
    }

    @Test
    void whenGetAllUsers_thenReturnAllUsersFromRepository() {
        List<User> repositoryResponse = List.of(testUser);
        UserDto[] expectedResponse = new UserDto[]{testUserDto};
        when(userRepository.findAll()).thenReturn(repositoryResponse);
        when(userMapper.entityToDto(testUser)).thenReturn(testUserDto);

        List<UserDto> receivedList = userService.getAllUsers(testRequester);

        assertThat(receivedList).containsExactly(expectedResponse);
        verify(userRepository).findAll();
    }

    @Test
    void whenGetUser_thenReturnUserFromRepository() {
        when(userRepository.findById(testUsername)).thenReturn(Optional.of(testUser));
        when(userMapper.entityToDetailedDto(testUser)).thenReturn(testUserDto);

        UserDto receivedUser = userService.getUser(testUsername, testRequester);

        assertThat(receivedUser).isEqualTo(testUserDto);
        verify(userRepository).findById(testUsername);
    }

    @Test
    void whenCreateUser_thenNoExceptionThrown() {
        LocalDateTime testDate = now();
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.dtoToEntity(testUserDto)).thenReturn(testUser);

        assertThatNoException().isThrownBy(() -> userService.createUser(testUserDto, testRequester));

        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getCreatedOn()).isAfterOrEqualTo(testDate);
    }

    @Test
    void whenAmendExistentUser_thenUserIsModified() {
        LocalDateTime testDate = now();
        final String newEmail = "newMail@gmail.com";
        UserDto modifiedUser = new UserDto(testUsername, newEmail, CLIENT, INACTV,
                null, null, null);
        when(userRepository.findById(testUsername)).thenReturn(Optional.of(testUser));

        assertThatNoException().isThrownBy(() -> userService.amendUser(testUsername, modifiedUser, testRequester));

        verify(userRepository).findById(testUsername);
        assertThat(testUser.getRole()).isEqualTo(CLIENT);
        assertThat(testUser.getStatus()).isEqualTo(INACTV);
        assertThat(testUser.getEmail()).isEqualTo(newEmail);
        assertThat(testUser.getUpdatedOn()).isAfterOrEqualTo(testDate);
        assertThat(testUser.getUpdatedBy()).isEqualTo(testRequester.getUsername());
    }

    @Test
    void whenDeleteExistentUser_thenDeleteUserFromRepository() {
        assertThatNoException().isThrownBy(() -> userService.deleteUser(testUsername, testRequester));
        verify(userRepository).deleteById(testUsername);
    }


    @Test
    void whenGetUserInternal_thenReturnOptionalFromRepository() {
        when(userRepository.findById(testUsername)).thenReturn(Optional.ofNullable(testUser));

        Optional<User> actualUser = userService.getRequesterUser(testUsername);

        assertThat(actualUser.get()).isEqualTo(testUser);
    }

    @Test
    void whenUserExists_thenReturnTrue(){
        when(userRepository.existsById(testUser.getUsername())).thenReturn(true);

        assertThat(userService.userExists(testUser)).isTrue();

        verify(userRepository).existsById(testUser.getUsername());
    }
}
