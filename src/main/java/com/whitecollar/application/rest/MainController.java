package com.whitecollar.application.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.whitecollar.application.dao.CollarsDAO;
import com.whitecollar.application.dao.ShopsDAO;
import com.whitecollar.application.dao.impl.CollarsDaoImpl;
import com.whitecollar.application.dao.impl.ShopsDaoImpl;
import com.whitecollar.application.entities.Collars;
import com.whitecollar.application.entities.Shops;

@RestController
@RequestMapping("/whitecollar")
@Profile("prod")
public class MainController {
	
	@Autowired
	ShopsDAO shopsDao = new ShopsDaoImpl();
	@Autowired
	CollarsDAO collarsDao = new CollarsDaoImpl();
	
	// Starting the app, creating some records in the tables.
	@GetMapping
	public String start() {
	shopsDao.deleteAll();
	Shops shop1 = shopsDao.save(new Shops ("Shop 1", 2));
	Shops shop2 = shopsDao.save(new Shops ("Shop 2", 5));
	shopsDao.findAll();	
	Collars collar1 = new Collars("Collar 1", "Creator 1", 1000, "01-01-2021", shop1);
	collarsDao.save(collar1);
	Collars collar2 = new Collars("Collar 2", null, 1200, "01-01-2021", shop1);
	collarsDao.save(collar2);
	Collars collar3 = new Collars("Collar 3", "Creator 2", 1400, "01-01-2021", shop2);
	collarsDao.save(collar3);
	return "WHITECOLLAR APP STARTED";
	}
	
	// Getting shops list.
	@GetMapping("/shops")
	public List<Shops> getShops() throws Exception {
		try {List<Shops> shops = shopsDao.findAll();
			return shops;
		} catch (RuntimeException e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shops in the list.");
		}
	}
	
	// Getting collars list.
	@GetMapping("/collars")
	public List<?> getCollars() throws RuntimeException {
		List<Collars> collars = collarsDao.findAll();
		if (!collars.isEmpty()) {
			return collars;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No collars in the list.");
		}
	}
	
	// Posting new shops.
	@PostMapping("/shops")
	public ResponseEntity<Shops> postShop(@RequestBody Shops s) throws RuntimeException{
		try {
			Shops shop = new Shops(s);
			shopsDao.save(shop);
			return ResponseEntity.ok(shop);
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible to add shop.");
		}
	}
	
	// Getting shop by id.
	@GetMapping("/shops/{id}")
	public String getShopById(@PathVariable("id") long id) throws Exception {
		try {
			return shopsDao.getById(id).toString();
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No shop with id " + id + ".");
		}
	}
	
	// Getting collars by shop.
	@GetMapping("/shops/{id}/collars")
	public ResponseEntity<List<Collars>> getShopsCollars(@PathVariable("id") long id) {
		try {
			shopsDao.getById(id).toString();
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No shop with id " + id + ".");
		} 		
		List<Collars> shopCollars = new ArrayList<Collars>();		
		for (Collars c: collarsDao.findAll()) {
			if (c.getShopId() == id) {
				shopCollars.add(c);
			}
		}			
		if (!shopCollars.isEmpty()) {
			return ResponseEntity.ok(shopCollars);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No collars in shop " + id + ".");
		}
	}
	
	// Posting collars by shop id.
	@PostMapping("/shops/{id}/collars")
	public ResponseEntity<String> postShopsCollar(@PathVariable("id") long id,
			@RequestBody Collars collar) {
		try {
			shopsDao.getById(id).toString();
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shop with id " + id + ".");
		}
		int actualCollars = 0;
		for (Collars c: collarsDao.findAll()) {
			if (c.getShopId() == id) {
				actualCollars++;
			}
		}
		Shops actualShop = shopsDao.findById(id).get();
		if (actualCollars < actualShop.getCapacity()) {
			Collars newCollar = new Collars();
			newCollar.setName(collar.getName());
			newCollar.setCreator(collar.getCreator());
			newCollar.setPrice(collar.getPrice());
			newCollar.setEntryDate(collar.getEntryDate());
			for (Shops s: shopsDao.findAll()) {
				if (s.getId() == id) {
					newCollar.setShop(s);
					break;
				}
			}
			collarsDao.save(newCollar);
			return ResponseEntity.ok("Collar added to shop");
			}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible to add collar to shop. "
					+ "Maximum capacity already reached.");
		}
	}
	
	// Deleting collars by shop id.
	@DeleteMapping("/shops/{id}/collars")
	public ResponseEntity <String> deleteShopsCollars(@PathVariable("id") long id) {
		try {
			shopsDao.findById(id).get();
		} catch (RuntimeException e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shop with id " + id + ".");
		}	
		int shopCollars = 0;
		List <Long> collarsId = new ArrayList<Long>();
		for (Collars collar: collarsDao.findAll()) {
			if (collar.getShopId() == id) {
				collarsId.add(collar.getId());
				shopCollars++;
			} 
		}
		if (shopCollars > 0) {
			for (long ids: collarsId) {
				collarsDao.deleteById(ids);
			}
			return ResponseEntity.ok("Collars erased.");
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible to run command."
					+ "No collars in this shop.");
		}
	}
	
}
