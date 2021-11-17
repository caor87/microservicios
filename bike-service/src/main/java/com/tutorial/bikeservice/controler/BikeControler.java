package com.tutorial.bikeservice.controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.bikeservice.entity.Bike;
import com.tutorial.bikeservice.service.BikeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/bike")
public class BikeControler {
	
	@Autowired
	BikeService bikeService;
	
	@GetMapping
	public ResponseEntity<List<Bike>> getAll(){
		 List<Bike> bikes = bikeService.getALl();
		 if(bikes.isEmpty())
			 return ResponseEntity.noContent().build();
		 return ResponseEntity.ok(bikes);
	}
	
	@GetMapping("/byUser/{userId}")
	public ResponseEntity<List<Bike>> getByUserId(@PathVariable("userId") int userId){
		 List<Bike> bikes = bikeService.byUserId(userId);
		 return ResponseEntity.ok(bikes);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Bike> getUserById(@PathVariable("id") int id){
		 Bike bikes = bikeService.getCarById(id);
		 if(bikes == null)
			 return ResponseEntity.notFound().build();
		 return ResponseEntity.ok(bikes);
	}
	
	
	@PostMapping
	public ResponseEntity<Bike> save(@RequestBody Bike bike){
		log.info("dato Bikes: {}",  bike);
		Bike  bikeNew = bikeService.save(bike);
		return ResponseEntity.ok(bikeNew);
	}

}
