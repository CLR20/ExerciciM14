package com.whitecollar.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.whitecollar.application.entities.Collars;

@Component
public interface CollarsDAO extends JpaRepository<Collars, Long>{

}