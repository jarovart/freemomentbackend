package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.repository.UserRepository;
import de.jarovart.freemoment.server.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        Pageable pageable = PageRequest.of(0, 20);
        return UserMapper.toUserResponse(userRepository.findAll(pageable).getContent());
    }

    public List<UserResponse> searchByQuery(String query) {
        if (query == null || query.trim().length() < 3) {
            return getAllUsers();
        }
        return UserMapper.toUserResponse(userRepository.searchUsers(query, PageRequest.of(0, 20)).getContent());
    }
}
