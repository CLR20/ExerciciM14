package com.whitecollar.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitecollar.application.entities.Collars;

public interface CollarsDAO extends JpaRepository<Collars, Long>{

}