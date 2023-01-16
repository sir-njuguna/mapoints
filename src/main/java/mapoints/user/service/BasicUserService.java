package mapoints.user.service;

import mapoints.lib.service.BaseService;
import mapoints.user.model.User;
import mapoints.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BasicUserService extends BaseService<User, UserRepository> {
}
