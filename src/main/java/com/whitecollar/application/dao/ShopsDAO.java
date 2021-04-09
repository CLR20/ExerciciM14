package com.whitecollar.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.whitecollar.application.entities.Shops;

@Component
public interface ShopsDAO extends JpaRepository<Shops, Long>{
	public Shops getById(long id);
}
