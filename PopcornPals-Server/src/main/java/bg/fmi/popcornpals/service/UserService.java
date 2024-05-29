package bg.fmi.popcornpals.service;

import bg.fmi.popcornpals.dto.PlaylistDTO;
import bg.fmi.popcornpals.dto.UserDTO;
import bg.fmi.popcornpals.dto.UserRequestDTO;
import bg.fmi.popcornpals.mapper.PlaylistMapper;
import bg.fmi.popcornpals.mapper.UserMapper;
import bg.fmi.popcornpals.model.User;
import bg.fmi.popcornpals.repository.PlaylistRepository;
import bg.fmi.popcornpals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final UserMapper userMapper;
    private final PlaylistMapper playlistMapper;

    @Autowired
    public UserService(UserRepository userRepository, PlaylistRepository playlistRepository,
                       UserMapper userMapper, PlaylistMapper playlistMapper) {
        this.userRepository = userRepository;
        this.playlistRepository = playlistRepository;
        this.userMapper = userMapper;
        this.playlistMapper = playlistMapper;
    }

    public List<UserDTO> getUsers(Integer pageNo, Integer pageSize, String name, String username) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> users = null;
        if(name != null) {
            users = userRepository.findByNameIgnoreCaseContaining(name, pageable);
        }
        else if(username != null) {
            users = userRepository.findByUsernameIgnoreCaseContaining(username, pageable);
        }
        else {
            users = userRepository.findAll(pageable);
        }
        return users.getContent().stream().map(u -> userMapper.toDTO(u)).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return userMapper.toDTO(user);
    }

    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        User newUser = userRepository.save(userMapper.toEntity(userRequestDTO));
        return userMapper.toDTO(newUser);
    }

    public UserDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            return null;
        }

        if(userRequestDTO.getName() != null) {
            user.setName(userRequestDTO.getName());
        }
        if(userRequestDTO.getUsername() != null) {
            user.setUsername(userRequestDTO.getUsername());
        }
        if(userRequestDTO.getDescription() != null) {
            user.setDescription(userRequestDTO.getDescription());
        }
        if(userRequestDTO.getBirthday() != null) {
            user.setBirthday(userRequestDTO.getBirthday());
        }
        return userMapper.toDTO(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        User toDelete = userRepository.findById(userId).orElseThrow();
        userRepository.delete(toDelete);
    }

    public List<PlaylistDTO> findPlaylistsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return playlistRepository.findAllByUser(user.getID()).stream().map(p -> playlistMapper.toDTO(p)).collect(Collectors.toList());
    }
}
