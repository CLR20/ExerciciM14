package com.whitecollar.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitecollar.application.entities.Shops;

public interface ShopsDAO extends JpaRepository<Shops, Long>{
	public Shops getById(long id);
}
