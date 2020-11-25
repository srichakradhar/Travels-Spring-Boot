package com.fresco.tournament.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fresco.tournament.models.Admin;
import com.fresco.tournament.models.Team;
import com.fresco.tournament.repo.AdminRepository;
import com.fresco.tournament.repo.TeamRepository;

@Service
public class UserAuthService implements UserDetailsService {
	@Autowired
	private TeamRepository teamRepo;
	@Autowired
	private AdminRepository adminRepo;

	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		try {
			Optional<Team> team = teamRepo.findByName(name);
			if (team.isPresent())
				return new User(team.get().getName(), team.get().getPassword(),
						Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("USER") }));
			Optional<Admin> admin = adminRepo.findByName(name);
			if (admin.isPresent())
				return new User(admin.get().getName(), admin.get().getPassword(),
						Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("USER") }));
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new UsernameNotFoundException("User '" + name + "' not found.");
	}
}
