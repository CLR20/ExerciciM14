package com.whitecollar.application.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.whitecollar.application.dao.CollarsDAO;
import com.whitecollar.application.dao.ShopsDAO;
import com.whitecollar.application.dao.impl.CollarsDaoImpl;
import com.whitecollar.application.dao.impl.ShopsDaoImpl;
import com.whitecollar.application.entities.Collars;
import com.whitecollar.application.entities.Shops;

@Controller
@RequestMapping("")
public class MainController {
	
	@Autowired
	ShopsDAO shopsDao = new ShopsDaoImpl();
	@Autowired
	CollarsDAO collarsDao = new CollarsDaoImpl();
	
	// Starting the app, creating some records in the tables.
	@GetMapping("/whitecollar")
	@ResponseStatus(HttpStatus.OK)
	public String start(Model model) {
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
	return "start";
	}
	
	// Getting shops list.
	@GetMapping("/whitecollar/shops")
	@ResponseStatus(HttpStatus.OK)
	public String getShops(Model model) throws Exception {
		try {
			List<Shops> shops = shopsDao.findAll();
			model.addAttribute("shops", shops);
			return "shopsManagement";
		} catch (RuntimeException e){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shops in the list.");
		}
	}
	
	// Getting collars list.
	@GetMapping("/whitecollar/collars")
	public String getCollars(Model model) throws RuntimeException {
		List<Collars> collars = collarsDao.findAll();
		if (!collars.isEmpty()) {
			model.addAttribute("collars", collars);
			return "collarsManagement";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No collars in the list.");
		}
	}
	
	// Posting new shop.
	@PostMapping("/whitecollar/shops")
	public String postShop(String name, int capacity, Model model) throws RuntimeException{
		try {
			Shops shop = new Shops(name, capacity);
			shopsDao.save(shop);
			List<Shops> shops = shopsDao.findAll();
			model.addAttribute("shops", shops);
			return "shopsManagement";
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible to add shop.");
		}
	}
	
	// Getting collars by shop.
	@GetMapping("/whitecollar/shops/{id}/collars")
	public String getShopsCollars(String id, Model model) {
		long longId = Long.parseLong(id);
		try {
			shopsDao.getById(longId).toString();
		} catch (RuntimeException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No shop with id " + id + ".");
		} 		
		List<Collars> collars = new ArrayList<Collars>();		
		for (Collars c: collarsDao.findAll()) {
			if (c.getShopId() == longId) {
				collars.add(c);
			}
		}			
		if (!collars.isEmpty()) {
			model.addAttribute("collars", collars);
			return "filteredCollarsManagement";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No collars in shop " + id + ".");
		}
	}
	
	// Posting collars by shop id.
	@PostMapping("/whitecollar/shops/{shop}/collars")
	public String postShopsCollar(String idshop, String name, String creator, int price, String entryDate,
			Model model) {
		long id = Long.parseLong(idshop);
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
			Collars newCollar = new Collars(name, creator, price, entryDate, actualShop);
			collarsDao.save(newCollar);
			List<Collars> collars = collarsDao.findAll();
			model.addAttribute("collars", collars);
			return "collarsManagement";
			}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible to add collar to shop. "
					+ "Maximum capacity already reached.");
		}
	}
	
	// Deleting collars by shop id.
	@PostMapping("/whitecollar/shops/{id}/collars_deleted")
	public String deleteShopsCollars(String idshop, Model model) {
		long id = Long.parseLong(idshop);
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
			List <Collars> collars = collarsDao.findAll();
			model.addAttribute("collars", collars);
			return "collarsManagement";
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Impossible to run command."
					+ "No collars in this shop.");
		}
	}
	
	// Deleting shop.
		@PostMapping("/whitecollar/shops_deleted")
		public String deleteShops(String idshop, Model model) {
			long id = Long.parseLong(idshop);
			try {
				Shops shop = shopsDao.findById(id).get();
				shopsDao.delete(shop);
				for (Collars collar: collarsDao.findAll()) {
					if (collar.getShopId() == id) {
						collarsDao.delete(collar);
					}
				}
				List<Shops> shops = shopsDao.findAll();
				model.addAttribute("shops", shops);
				return "shopsManagement";				
			} catch (RuntimeException e){
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shop with id " + id + ".");
			}	
		}
	
}
