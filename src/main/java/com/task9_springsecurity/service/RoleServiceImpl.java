package com.task9_springsecurity.service;

import com.task9_springsecurity.dao.RoleDao;
import com.task9_springsecurity.model.Role;
import com.task9_springsecurity.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) { this.roleDao = roleDao; }

    @Override
    public List<Role> findAll() { return roleDao.findAll(); }

    @Override
    public Optional<Role> findById(Long id) { return roleDao.findById(id); }

    @Override
    @Transactional
    public void save(Role role) { roleDao.save(role); }

    @Override
    @Transactional
    public void update(Role role) { roleDao.update(role); }

    @Override
    @Transactional
    public void delete(Long id) { roleDao.delete(id); }
}
