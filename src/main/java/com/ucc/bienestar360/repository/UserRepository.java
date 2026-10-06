package com.ucc.bienestar360.repository;

import com.ucc.bienestar360.model.Role;
import com.ucc.bienestar360.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByAcademicProgram(String academicProgram);
}
